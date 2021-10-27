package com.example.asm_android_network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asm_android_network.adapter.RecyclerAdapter;
import com.example.asm_android_network.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    // Variable declarations
    private String userEmail;
    private String title, price, rating, picture;
    private int idd;
    private TextView textView;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter mAdapter;
    private List<Product> products;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private EditText mTitle,mPrice,mRating;
    private FloatingActionButton mFabChoosePic;
    private CircleImageView mPicture;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

        if (item.getItemId() == R.id.action_settings){

            intent = new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(HomeActivity.this,"Cài đặt !",Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.action_notifications){

            intent = new Intent(HomeActivity.this,NotificationsActivity.class);
            startActivity(intent);
            Toast.makeText(HomeActivity.this,"Thông báo !",Toast.LENGTH_SHORT).show();
        }



        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_menu,menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mToolbar = findViewById(R.id.dashboard_toolbar);
        progressBar = findViewById(R.id.progressbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();

        recyclerView = findViewById(R.id.products_recyclerView);
        manager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerView.setLayoutManager(manager);
        products = new ArrayList<>();

        getProducts();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddProductActivity.class));
            }
        });


    }


    private void getProducts (){
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getBaseUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        //formattedNumber is equal to 1,000,000
                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                String title = object.getString("title");
                                double price = object.getDouble("price");
                                double rating = object.getDouble("rating");
                                String image = object.getString("image");

                                String rate = String.valueOf(rating);
                                float newRate = Float.valueOf(rate);

                                Product product = new Product(title,price, newRate,image);
                                products.add(product);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        mAdapter = new RecyclerAdapter(HomeActivity.this,products);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(HomeActivity.this).add(stringRequest);

    }

    private String getBaseUrl (){
        return "http://"+getResources().getString(R.string.machine_ip_address)+"/android/getProducts.php";
    }

}
