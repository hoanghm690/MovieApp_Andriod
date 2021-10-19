package com.example.movieapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText Fullname,Email;
    CardView update,cPass,logout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Fullname = findViewById(R.id.p_fullname);
        Email = findViewById(R.id.p_email);
        update = findViewById(R.id.p_update);
        cPass = findViewById(R.id.p_change_pass);
        logout = findViewById(R.id.p_logout);

        Intent i =getIntent();
        String mFullname = i.getStringExtra("fullname");
        final String mEmail = i.getStringExtra("email");

        Fullname.setText(mFullname);
        Email.setText(mEmail);

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
                            massage(response);
                        }, error -> {
                            progressDialog.dismiss();
                            massage(error.getMessage());
                        }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> updateParams = new HashMap<>();
                        updateParams.put("fullname", fullname);
                        updateParams.put("email", email);
                        updateParams.put("myemail",mEmail);
                        return updateParams;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                queue.add(stringRequest);
            }
        });

        logout.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
    public void massage(String massage){
        Toast.makeText(this, massage, Toast.LENGTH_LONG).show();
    }
}