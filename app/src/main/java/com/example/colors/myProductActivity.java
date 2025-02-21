package com.example.colors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import DTO.MyProductDTO;
import DTO.Product_DTO;
import DTO.ResponseDTO;
import DTO.ResponseListDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
import model.MyProduct;
import model.Product;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class myProductActivity extends AppCompatActivity {
    User_DTO user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
        String userjson = sharedPreferences.getString("userData",null);

        if (userjson != null) {
            Gson gson = new Gson();

            user = gson.fromJson(userjson, User_DTO.class);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                OkHttpClient okHttpClient = new OkHttpClient();


                HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/product/loadmyproduct")
                        .newBuilder();
                urlBuilder.addQueryParameter("searchText","");
                urlBuilder.addQueryParameter("userId",String.valueOf(user.getId()));

                // Convert to URL string
                String url = urlBuilder.build().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {

                    Response response = okHttpClient.newCall(request).execute();
                    String responsetext = response.body().string();

                    Log.i("colors-log", responsetext);

                    ResponseListDTO<MyProductDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<MyProductDTO>>(){}.getType());

                    if (responseDTO.isSuccess()) {

                        List<MyProductDTO> Myproduct_dtoList = responseDTO.getContent();



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<MyProduct> productList =   new ArrayList<>();

                                for (MyProductDTO myproduct:Myproduct_dtoList){

                                    productList.add(new MyProduct(String.valueOf(myproduct.getId()),myproduct.getName(),"Rs."+String.valueOf(myproduct.getPrice())+"0",String.valueOf(myproduct.getAvqty()),String.valueOf(myproduct.getSelqty()),String.valueOf(myproduct.getProfit()),myproduct.getImgpath(),myproduct.getStatus(),myproduct.getImgpath2(),myproduct.getImgpath3(),myproduct.getDescription(),myproduct.getCategory()));

                                }


                                RecyclerView recyclerView = findViewById(R.id.myproductRecycleView);

                                recyclerView.setLayoutManager(new GridLayoutManager(myProductActivity.this, 2)); // 2 columns

                                MyProductAdapter productAdapter = new MyProductAdapter(myProductActivity.this,productList);
                                recyclerView.setAdapter(productAdapter);
                            }
                        });
                    } else {


                    }


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        }).start();



        EditText editText = findViewById(R.id.advanceSearchtextInputEditText2);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();

                        OkHttpClient okHttpClient = new OkHttpClient();

                        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/product/loadmyproduct")
                                .newBuilder();
                        urlBuilder.addQueryParameter("searchText", editText.getText().toString());
                        urlBuilder.addQueryParameter("userId",String.valueOf(user.getId()));
                        // Convert to URL string
                        String url = urlBuilder.build().toString();


                        Request request = new Request.Builder()
                                .url(url)
                                .build();

                        try {

                            Response response = okHttpClient.newCall(request).execute();
                            String responsetext = response.body().string();

                            Log.i("colors-log", responsetext);

                            ResponseListDTO<MyProductDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<MyProductDTO>>(){}.getType());

                            if (responseDTO.isSuccess()) {

                                List<MyProductDTO> Myproduct_dtoList = responseDTO.getContent();



                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<MyProduct> productList =   new ArrayList<>();

                                        for (MyProductDTO myproduct:Myproduct_dtoList){

                                            productList.add(new MyProduct(String.valueOf(myproduct.getId()),myproduct.getName(),"Rs."+String.valueOf(myproduct.getPrice()),String.valueOf(myproduct.getAvqty()),String.valueOf(myproduct.getSelqty()),String.valueOf(myproduct.getProfit()),myproduct.getImgpath(),myproduct.getStatus(),myproduct.getImgpath2(),myproduct.getImgpath3(),myproduct.getDescription(),myproduct.getCategory()));

                                        }


                                        RecyclerView recyclerView = findViewById(R.id.myproductRecycleView);

                                        recyclerView.setLayoutManager(new GridLayoutManager(myProductActivity.this, 2)); // 2 columns

                                        MyProductAdapter productAdapter = new MyProductAdapter(myProductActivity.this,productList);
                                        recyclerView.setAdapter(productAdapter);
                                    }
                                });
                            } else {


                            }


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                    }
                }).start();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



        ImageView imageView = findViewById(R.id.addproductimageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(myProductActivity.this, AddProductActivity.class));
            }
        });
    }
}


class  MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder>{

    class MyProductViewHolder extends RecyclerView.ViewHolder{

        //        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;
        public Button Button1;
        public Switch Switch;
        public CardView cardView;

        public ImageView imageView1;
        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.myproductTitle);
            textView2 = itemView.findViewById(R.id.Myproductprice);
            textView3 = itemView.findViewById(R.id.avqty);
            textView4 = itemView.findViewById(R.id.slqty);
            textView5 = itemView.findViewById(R.id.myprofit);
//            imageView = itemView.findViewById(R.id.productImage);
            Button1 = itemView.findViewById(R.id.UpdateButton);
            Switch = itemView.findViewById(R.id.switch1);
            cardView = itemView.findViewById(R.id.Myproduct_card_view);
            imageView1 = itemView.findViewById(R.id.myproductImage);
        }
    }

    public ArrayList<MyProduct> productArrayList;
    private Context context;
    public MyProductAdapter(Context context, ArrayList<MyProduct> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;

    }

    @NonNull
    @Override
    public MyProductAdapter.MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.my_product_item,null);
        MyProductAdapter.MyProductViewHolder productViewHolder = new MyProductAdapter.MyProductViewHolder(view);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter.MyProductViewHolder holder, int position) {

        MyProduct product = productArrayList.get(position);

        holder.textView1.setText(productArrayList.get(position).getName());
        holder.textView2.setText(productArrayList.get(position).getPrice());
        holder.textView3.setText(productArrayList.get(position).getAv_qty());
        holder.textView4.setText(productArrayList.get(position).getSl_qty());
        holder.textView5.setText("Rs."+productArrayList.get(position).getProfit()+"0");

        Glide.with(context)
                .load(productArrayList.get(position).getImgpath())
                .placeholder(R.drawable.loading) // Optional: Placeholder image
                .error(R.drawable.mark) // Optional: Error image
                .into(holder.imageView1);


        holder.Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddProductActivity.class);
                intent.putExtra("productId", product.getId());
                intent.putExtra("productName", product.getName());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("productQty", product.getAv_qty());
                intent.putExtra("productDescription", product.getDescription());
                intent.putExtra("productCategory", product.getCategory());
                intent.putExtra("productimg1", product.getImgpath());
                intent.putExtra("productimg2", product.getImgpath2());
                intent.putExtra("productimg3", product.getImgpath3());
                intent.putExtra("isUpdate", "true");
                context.startActivity(intent);
            }
        });
        if (productArrayList.get(position).getStatus() == 1){
            holder.Switch.setChecked(true);
        }
        holder.Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();

                            OkHttpClient okHttpClient = new OkHttpClient();

                            // Build URL with query parameters dynamically
                            HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/product/changestatus")
                                    .newBuilder();
                            urlBuilder.addQueryParameter("status", "1");
                            urlBuilder.addQueryParameter("product_id", product.getId());

                            // Convert to URL string
                            String url = urlBuilder.build().toString();

                            // Create request
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();


                            try {

                                Response response = okHttpClient.newCall(request).execute();
                                String responsetext = response.body().string();

                                Log.i("colors-log", responsetext);

                                ResponseDTO<String> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>(){}.getType());

                                if (responseDTO.isSuccess()) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Success!")
                                                    .setContentText("Your product was activated.")
                                                    .show();
                                        }
                                    });
                                } else {


                                }


                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }


                        }
                    }).start();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();

                            OkHttpClient okHttpClient = new OkHttpClient();

                            // Build URL with query parameters dynamically
                            HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/product/changestatus")
                                    .newBuilder();
                            urlBuilder.addQueryParameter("status", "2");
                            urlBuilder.addQueryParameter("product_id", product.getId());

                            // Convert to URL string
                            String url = urlBuilder.build().toString();

                            // Create request
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();

                            try {

                                Response response = okHttpClient.newCall(request).execute();
                                String responsetext = response.body().string();



                                ResponseDTO<String> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>(){}.getType());

                                if (responseDTO.isSuccess()) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Success!")
                                                    .setContentText("Your product was deactivated.")
                                                    .show();
                                        }
                                    });
                                } else {


                                }


                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }


                        }
                    }).start();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return  this.productArrayList.size();
    }
}