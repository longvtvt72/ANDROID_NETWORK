package com.example.asm_android_network;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class AddProductActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;

    String strTitle,strPrice,strRating,strImage;
    Button btnThem;
    EditText edtTitle, edtRating, edtPrice, edtImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        btnThem = (Button) findViewById(R.id.btnThem);
        edtTitle = (EditText) findViewById(R.id.updateTitle);
        edtPrice = (EditText) findViewById(R.id.updatePrice);
        edtRating = (EditText) findViewById(R.id.updateRating);
        edtImage = (EditText) findViewById(R.id.updateImage);

        mToolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("Thêm sản phẩm");
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));


        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemSP();
            }
        });
    }

    private void ThemSP(){

        final String title = edtTitle.getText().toString().trim();
        final String price = edtPrice.getText().toString().trim();
        final String rating = edtRating.getText().toString().trim();
        final String image = edtImage.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading:...");

        if (title.isEmpty()){
            Toast.makeText(this,"Thiếu title",Toast.LENGTH_SHORT).show();
            return;
        }else if(price.isEmpty()){
            Toast.makeText(this,"Thiếu price",Toast.LENGTH_SHORT).show();
            return;
        }else if(rating.isEmpty()){
            Toast.makeText(this,"Thiếu rating",Toast.LENGTH_SHORT).show();
            return;
        }else if(image.isEmpty()){
            Toast.makeText(this,"Thiếu image",Toast.LENGTH_SHORT).show();
            return;
        }

        else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.1.40:8088/android/create.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equalsIgnoreCase("success")){
                                progressDialog.dismiss();
                                Intent intent = new Intent(AddProductActivity.this,HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(AddProductActivity.this,"Thêm thành công !",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(AddProductActivity.this, "Thêm thất bại !", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddProductActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String, String>();

                    params.put("title",title);
                    params.put("price",price);
                    params.put("rating",rating);
                    params.put("image",image);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AddProductActivity.this);
            requestQueue.add(request);

        }

    }


}