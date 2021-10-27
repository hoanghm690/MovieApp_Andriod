package com.example.movieapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText Email,Password;
    TextView register,login;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TH Play");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212b36")));
        setContentView(R.layout.activity_login);
        Email = findViewById(R.id.log_email);
        Password = findViewById(R.id.log_password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        register = findViewById(R.id.btnRegister);
        login = findViewById(R.id.btnLogin);

        register.setOnClickListener(view -> UserRegistrationProcess());
        login.setOnClickListener(view -> UserLoginProcess());
    }

    private void UserLoginProcess() {
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){
            massage("Vui lòng nhập thông tin để đăng nhập");
        }else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Urls.LOGIN_URL,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("status");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(result.equals("success")){
                                progressDialog.dismiss();

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String fullname_profile = object.getString("fullname");
                                    String email_profile = object.getString("email");
                                    Integer userId_profile = object.getInt("userID");
                                    String img_profile = object.getString("image");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    SharedPreferences sharedPref = getSharedPreferences("User",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("UserName", fullname_profile);
                                    editor.putString("UserEmail", email_profile);
                                    editor.putString("UserImage", img_profile);
                                    editor.putInt("UserID",userId_profile);
                                    editor.putBoolean("isLogin", true);
                                    editor.apply();
                                    startActivity(intent);finish();
                                    massage("Đăng nhập thành công");
                                }
                            }else{
                                progressDialog.dismiss();
                                massage("Đăng nhập thất bại");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }, error -> {
                progressDialog.dismiss();
                massage(error.getMessage());
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("email",email);
                    params.put("password",password);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(request);
        }
    }

    private void UserRegistrationProcess() {
        LayoutInflater inflater = getLayoutInflater();
        View register_Layout = inflater.inflate(R.layout.register_layout,null);
        EditText Fullname = register_Layout.findViewById(R.id.reg_fullname);
        EditText Email = register_Layout.findViewById(R.id.reg_email);
        EditText Password = register_Layout.findViewById(R.id.reg_password);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(register_Layout);
        builder.setPositiveButton("Đăng ký", (dialogInterface, i) -> {
            progressDialog.show();
            final String fullname = Fullname.getText().toString().trim();
            final String email = Email.getText().toString().trim();
            final String password = Password.getText().toString().trim();

            if(fullname.isEmpty() || email.isEmpty() || password.isEmpty()){
                massage("Some fiels are Empty..");
                progressDialog.dismiss();
            }else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.REGISTER_URL,
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
                        params.put("fullname",fullname);
                        params.put("email",email);
                        params.put("password",password);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(stringRequest);
            }
        });
        builder.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            finish();
        }
        return true;
    }

    public void massage(String massage){
        Toast.makeText(this, massage, Toast.LENGTH_LONG).show();
    }
}