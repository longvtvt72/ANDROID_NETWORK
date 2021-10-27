package com.example.asm_android_network.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.asm_android_network.AddProductActivity;
import com.example.asm_android_network.DetailedProductsActivity;
import com.example.asm_android_network.HomeActivity;
import com.example.asm_android_network.model.Product;
import com.example.asm_android_network.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> products = new ArrayList<>();


    public RecyclerAdapter (Context context,List<Product> products){
        this.mContext = context;
        this.products = products;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mPrice;
        private ImageView mImageView, update, delete;
        private RatingBar mRate;
        private LinearLayout mContainer;

        public MyViewHolder (View view){
            super(view);
            mTitle = view.findViewById(R.id.product_title);
            mImageView = view.findViewById(R.id.product_image);
            mRate = view.findViewById(R.id.product_rating);
            mPrice = view.findViewById(R.id.product_price);
            mContainer = view.findViewById(R.id.product_container);

            update = view.findViewById(R.id.update);
            delete = view.findViewById(R.id.delete);
        }

    }

    public void Delete(int item){
        products.remove(item);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.products_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Product product = products.get(position);

        holder.mPrice.setText(""+product.getPrice());
        holder.mRate.setRating(product.getRating());
        holder.mTitle.setText(product.getTitle());
        Glide.with(mContext).load(product.getImage()).into(holder.mImageView);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailedProductsActivity.class);

                intent.putExtra("title",product.getTitle());
                intent.putExtra("price",product.getPrice());
                intent.putExtra("rating",product.getRating());
                intent.putExtra("image",product.getImage());
                mContext.startActivity(intent);

            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View updateLayout = LayoutInflater.from(mContext).inflate(R.layout.activity_update_product,null);
                EditText Title = updateLayout.findViewById(R.id.updateTitle);
                EditText Price = updateLayout.findViewById(R.id.updatePrice);
                EditText Rating = updateLayout.findViewById(R.id.updateRating);
                EditText Image = updateLayout.findViewById(R.id.updateImage);

                holder.mTitle.setText(product.getTitle());
                holder.mPrice.setText(""+product.getPrice());
                holder.mRate.setRating(product.getRating());
                Glide.with(mContext).load(product.getImage()).into(holder.mImageView);



                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Sửa "+product.getTitle());
                builder.setView(updateLayout);
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String title = Title.getText().toString();
                        final Double price = Double.valueOf(Price.getText().toString());
                        final Float rating = Float.valueOf(Rating.getText().toString());
                        final String image = Image.getText().toString();
                        final String oldtitle = product.getTitle();

                        if( title.isEmpty() || image.isEmpty() ){

                            Toast.makeText(mContext,"Vui lòng nhập vào ô còn thiếu",Toast.LENGTH_SHORT).show();

                        }else{
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.40:8088/android/update.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(mContext,error.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String,String> params = new HashMap<>();
                                    params.put("title",title);
                                    params.put("price", String.valueOf(price));
                                    params.put("rating", String.valueOf(rating));
                                    params.put("image",image);
                                    params.put("oldtitle",oldtitle);
                                    return params;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(mContext);
                            queue.add(stringRequest);
                        }
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Xác nhận xóa - "+product.getTitle());
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.40:8088/android/delete.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    String check = object.getString("state");
                                    if (check.equals("delete")){
                                        Delete(position);
                                        Toast.makeText(mContext, "Xóa thành công !",Toast.LENGTH_SHORT).show();
                                        //hàm refresh
                                        Intent myIntent = new Intent(mContext,HomeActivity.class);
                                        mContext.startActivity(myIntent);
                                            }else {
                                                Toast.makeText(mContext, "Xóa thất bại.",Toast.LENGTH_SHORT).show();
                                            }
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(mContext,error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> deleteParams = new HashMap<>();
                                deleteParams.put("title",product.getTitle());
                                return deleteParams;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        requestQueue.add(stringRequest);

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


}
