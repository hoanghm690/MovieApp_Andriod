package com.example.movieapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;

public class ProfileActivity extends AppCompatActivity {
    EditText Fullname,Email;
    CircleImageView image;
    CardView update,cPass;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TH Play");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212b36")));
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Fullname = findViewById(R.id.p_fullname);
        Email = findViewById(R.id.p_email);
        image = findViewById(R.id.profile_image);
        update = findViewById(R.id.p_update);
        cPass = findViewById(R.id.p_change_pass);

        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        String mFullname = sharedPref.getString("UserName",null);
        String mImage = sharedPref.getString("UserImage",null);
        final String mEmail = sharedPref.getString("UserEmail",null);

        if (mImage == null){
            image.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.man, null));
        }else{
            byte[] imgDecode = Base64.getDecoder().decode(mImage);
            Bitmap bmp = BitmapFactory.decodeByteArray(imgDecode, 0, imgDecode.length);
            image.setImageBitmap(bmp);
        }
        Fullname.setText(mFullname);
        Email.setText(mEmail);

        image.setOnClickListener(view -> {
            requestPermission();
        });

        cPass.setOnClickListener(view -> {
            View resetpasswordLayout = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.change_password,null);
            EditText OldPass = resetpasswordLayout.findViewById(R.id.edt_old_password);
            EditText NewPass = resetpasswordLayout.findViewById(R.id.edt_new_password);
            EditText ConfirmPass = resetpasswordLayout.findViewById(R.id.edt_confirm_password);
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("ĐỔI MẬT KHẨU");
            builder.setView(resetpasswordLayout);
            builder.setPositiveButton("Thay đổi", (dialogInterface, i1) -> {
                String oldpassword = OldPass.getText().toString().trim();
                String newpassword = NewPass.getText().toString().trim();
                String confirmpassword = ConfirmPass.getText().toString().trim();

                if(oldpassword.isEmpty() || newpassword.isEmpty() || confirmpassword.isEmpty()){
                    massage("Thay đổi thất bại do bỏ trống thông tin");
                }else {
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.RESET_PASSWORD_URL,
                            response -> {
                                progressDialog.dismiss();
                                massage(response);
                            }, error -> {
                        progressDialog.dismiss();
                        massage(error.getMessage());
                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String,String> params = new HashMap<>();
                            params.put("oldpassword",oldpassword);
                            params.put("newpassword",newpassword);
                            params.put("confirmpassword",confirmpassword);
                            params.put("email",mEmail);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                    queue.add(stringRequest);
                }
            });

            builder.setNegativeButton("cancel", (dialogInterface, i1) -> dialogInterface.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        update.setOnClickListener(view -> {
            String fullname = Fullname.getText().toString().trim();
            String email = Email.getText().toString().trim();

            if(fullname.isEmpty() || email.isEmpty()){
                massage("Thao tác thất bại do bỏ trống thông tin");
            }
            else {
                progressDialog.setTitle("Đang cập nhật ...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_URL,
                        response -> {
                            progressDialog.dismiss();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.remove("UserName");
                            editor.remove("UserEmail");
                            editor.putString("UserName", fullname);
                            editor.putString("UserEmail", email);
                            editor.putBoolean("isLogin", true);
                            editor.apply();
                            massage(response);
                        }, error -> {
                    progressDialog.dismiss();
                    massage(error.getMessage());
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> updateParams = new HashMap<>();
                        updateParams.put("fullname", fullname);
                        updateParams.put("myemail",mEmail);
                        return updateParams;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                queue.add(stringRequest);
            }
        });
    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPermissionGranted() {
                openImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ProfileActivity.this, "Đã từ chối cấp phép\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Nếu bạn từ chối cấp phép thì bạn không thể sử dụng được dịch vụ\n\nHãy cấp phép bằng cách truy cập [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openImagePicker() {

        TedBottomPicker.with(ProfileActivity.this)
                .show(uri -> {
                    try {
                        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
                        final String mEmail = sharedPref.getString("UserEmail",null);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        image.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String imgEncode = Base64.getEncoder().encodeToString(b);
                        Log.i("imgEncode",imgEncode);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_IMAGE_URL,
                                response -> {
                                    progressDialog.dismiss();
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.remove("UserImg");
                                    editor.putString("UserImage", imgEncode);
                                    editor.apply();
                                    massage(response);
                                }, error -> {
                            progressDialog.dismiss();
                            massage(error.getMessage());
                        }){
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> updateParams = new HashMap<>();
                                updateParams.put("image", imgEncode);
                                updateParams.put("myemail",mEmail);
                                return updateParams;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                        queue.add(stringRequest);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void massage(String massage){
        Toast.makeText(this, massage, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            finish();
        }
        return true;
    }
}