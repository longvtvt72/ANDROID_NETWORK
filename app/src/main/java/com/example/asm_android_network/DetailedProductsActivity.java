package com.example.asm_android_network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class DetailedProductsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView mImage;
    private TextView mTitle, mRating, mPrice;
    private int id;
    private String picture;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent;
                intent = new Intent(DetailedProductsActivity.this,SettingsActivity.class);
                startActivity(intent);
                Toast.makeText(DetailedProductsActivity.this,"Settings clicked!",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_notifications:

                intent = new Intent(DetailedProductsActivity.this,NotificationsActivity.class);
                startActivity(intent);
                Toast.makeText(DetailedProductsActivity.this,"Notifications clicked!",Toast.LENGTH_SHORT).show();
                return true;

            default: return super.onOptionsItemSelected(item);
        }

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
        setContentView(R.layout.activity_detailed_products);

        mToolbar = findViewById(R.id.toolbar);
        mImage = findViewById(R.id.image_view);
        mPrice = findViewById(R.id.price);
        mRating = findViewById(R.id.rating);
        mTitle = findViewById(R.id.name);

        // Setting up action bar
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));

        // Catching incoming intent
        Intent intent = getIntent();
        double price = intent.getDoubleExtra("price",0);
        float rate = intent.getFloatExtra("rate",0);
        String title = intent.getStringExtra("title");
        String image = intent.getStringExtra("image");

        if (intent !=null){

            mActionBar.setTitle(title);
            mTitle.setText(title);
            mRating.setText("Rating :"+rate+" /5");
            mPrice.setText(String.valueOf(price));
            Glide.with(DetailedProductsActivity.this).load(image).into(mImage);
        }

    }

    void readMode(){

        mTitle.setFocusableInTouchMode(false);
        mPrice.setFocusableInTouchMode(false);
        mRating.setFocusableInTouchMode(false);
        mTitle.setFocusable(false);
        mPrice.setFocusable(false);
        mRating.setFocusable(false);


    }
}
