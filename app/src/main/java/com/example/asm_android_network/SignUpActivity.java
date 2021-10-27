package com.example.asm_android_network;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;


public class SignUpActivity extends AppCompatActivity {

    private EditText fname, lname, email, phone, confirm_password, id_editText, password;
    private Button mSignUp;
    private TextView loginquest;
    private ProgressBar mProgress;
    String str_email,str_password,str_confirm_password,str_fname,str_lname,str_id_editText,str_phone;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email_editText);
        password= findViewById(R.id.user_password_editText);
        confirm_password = findViewById(R.id.confirm_password_editText);
        fname = findViewById(R.id.fname_editText);
        lname = findViewById(R.id.lname_editText);
        id_editText = findViewById(R.id.user_id_editText);
        phone = findViewById(R.id.phone_editText);
        mProgress = findViewById(R.id.progress2);
        mSignUp = findViewById(R.id.sign_up_button);
        loginquest = findViewById(R.id.loginquest);

        loginquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
                openDialog();
            }
        });

    }


    private void Register() {

        mProgress.setVisibility(View.VISIBLE);

        if (email.getText().toString().equals("")) {
            Toast.makeText(this, "Thiếu địa chỉ email.", Toast.LENGTH_SHORT).show();
        }else if (password.getText().toString().equals("")) {
            Toast.makeText(this, "Thiếu mật khẩu.", Toast.LENGTH_SHORT).show();
        }else if (confirm_password.getText().toString().equals("")) {
            Toast.makeText(this, "Thiếu xác nhận mật khẩu.", Toast.LENGTH_SHORT).show();
        }else if (fname.getText().toString().equals("")) {
            Toast.makeText(this, "Thiếu Họ và tên.", Toast.LENGTH_SHORT).show();
        }else if (lname.getText().toString().equals("")) {
            Toast.makeText(this, "Thiếu tên.", Toast.LENGTH_SHORT).show();
        }else if (id_editText.getText().toString().equals("")) {
            Toast.makeText(this, "Thiếu ID.", Toast.LENGTH_SHORT).show();
        }else if (phone.getText().toString().equals("")) {
            Toast.makeText(this, "Thiếu số điện thoại.", Toast.LENGTH_SHORT).show();
        }
        else {
            str_email = email.getText().toString().trim();
            str_password = password.getText().toString().trim();
            str_confirm_password = confirm_password.getText().toString().trim();
            str_fname = fname.getText().toString().trim();
            str_lname = lname.getText().toString().trim();
            str_id_editText = id_editText.getText().toString().trim();
            str_phone = phone.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, getBaseUrl(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        String success = jsonObject.getString("success");
                        String message = jsonObject.getString("message");

                        if (success.equals("1")) {
                            mProgress.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                        if (success.equals("0")) {
                            mProgress.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        mProgress.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignUpActivity.this,error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();

                    params.put("email",str_email);
                    params.put("password",str_password);
                    params.put("fname",str_email);
                    params.put("lname",str_fname);
                    params.put("id_number",str_lname);
                    params.put("phone",str_phone);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
            requestQueue.add(request);

        }

    }

    public void openDialog(){
        builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Tạo tài khoản thành công.")
                .setMessage("Bạn có muốn đăng nhập ngay bây giờ ?")
                .setCancelable(true)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Hủy", null);
        builder.create().show();
    }

    private String getBaseUrl(){
        return "http://"+getResources().getString(R.string.machine_ip_address)+"/android/sign_up.php";
    }


}
