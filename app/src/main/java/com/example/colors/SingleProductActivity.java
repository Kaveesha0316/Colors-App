package com.example.colors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Objects;

import DTO.ResponseDTO;
import DTO.ResponseListDTO;
import DTO.ReturnProductDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
import model.Product;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SingleProductActivity extends AppCompatActivity {

    String productId;
    String category;
    Integer qty = 1;
    User_DTO user;

    Integer avqty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_single_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         TextView productName = findViewById(R.id.sinlge_product_title);
        TextView productPrice = findViewById(R.id.sinlge_product_price);
        TextView offer = findViewById(R.id.sinlge_product_price2);
        TextView sinlge_product_description = findViewById(R.id.sinlge_product_description);
        TextView textView25 = findViewById(R.id.textView25);
        TextView text_quantity = findViewById(R.id.text_quantity);
        text_quantity.setText(String.valueOf(qty));
        ImageView mainImage = findViewById(R.id.sinlge_main_product_image);
        ImageView thumb1 = findViewById(R.id.thumb1);
        ImageView thumb2 = findViewById(R.id.thumb2);
        ImageView thumb3 = findViewById(R.id.thumb3);
        Button button = findViewById(R.id.btn_add_to_cart);

        ImageButton imageButtonup = findViewById(R.id.imageButton2);
        ImageButton imageButtondown = findViewById(R.id.imageButton6);

        imageButtonup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty +=1;
                text_quantity.setText(String.valueOf(qty));
            }
        });
        imageButtondown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (qty <= 1){
                    text_quantity.setText("1");

                }else {
                    qty -=1;
                    text_quantity.setText(String.valueOf(qty));
                }
            }
        });

      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if (qty <= avqty){



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

                      // Build URL with query parameters dynamically
                      HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/cart/add")
                              .newBuilder();
                      urlBuilder.addQueryParameter("qty", String.valueOf(qty));
                      urlBuilder.addQueryParameter("product_id", productId);

                      urlBuilder.addQueryParameter("user_id",String.valueOf(user.getId()));

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
                              String message = responseDTO.getMessage();
                              if (message.equals("add")){
                                  runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          new SweetAlertDialog(SingleProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                  .setTitleText("Success!")
                                                  .setContentText("Product added to your cart.")
                                                  .show();
                                      }
                                  });
                              }else {
                                  runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          new SweetAlertDialog(SingleProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                  .setTitleText("Success!")
                                                  .setContentText("Product quantity updated.")
                                                  .show();
                                      }
                                  });
                              }
                          } else {


                          }


                      } catch (Exception e) {
                          throw new RuntimeException(e);
                      }


                  }
              }).start();
              }else {
                  new SweetAlertDialog(SingleProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                          .setTitleText("Warning!")
                          .setContentText("Only "+String.valueOf(avqty)+" items are available. Please choose a quantity of "+String.valueOf(avqty)+" or less. ")
                          .show();
              }
          }
      });

        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
            productName.setText(intent.getStringExtra("productName"));
            productPrice.setText("Rs."+intent.getStringExtra("productPrice")+"0");
            Double pp = Double.parseDouble(intent.getStringExtra("productPrice"));
            Double tt = pp+(pp*10/100);
            offer.setText("Rs."+tt+"0");
            sinlge_product_description.setText(intent.getStringExtra("description"));
            category = intent.getStringExtra("category");

            String img1 =intent.getStringExtra("imgpath1");
            String img2 =intent.getStringExtra("imgpath2");
            String img3 =intent.getStringExtra("imgpath3");
            avqty = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("qty")));

            if (avqty == 0) {
                textView25.setText("Out of Stock");
                textView25.setTextColor(getColor(R.color.red));
                button.setEnabled(false);
            } else if (avqty > 0 && avqty <= 5) { // Corrected condition
                textView25.setText("Low Stock");
                textView25.setTextColor(getColor(R.color.yellow));
            } else if (avqty > 5) { // Corrected condition
                textView25.setText("In Stock");
                textView25.setTextColor(getColor(R.color.olive_green));
            }

            Glide.with(SingleProductActivity.this)
                    .load(img1)
                    .placeholder(R.drawable.loading) // Optional: Placeholder image
                    .error(R.drawable.mark) // Optional: Error image
                    .into(mainImage);
            Glide.with(SingleProductActivity.this)
                    .load(img1)
                    .placeholder(R.drawable.loading) // Optional: Placeholder image
                    .error(R.drawable.mark) // Optional: Error image
                    .into(thumb1);
            Glide.with(SingleProductActivity.this)
                    .load(img2)
                    .placeholder(R.drawable.loading) // Optional: Placeholder image
                    .error(R.drawable.mark) // Optional: Error image
                    .into(thumb2);
            Glide.with(SingleProductActivity.this)
                    .load(img3)
                    .placeholder(R.drawable.loading) // Optional: Placeholder image
                    .error(R.drawable.mark) // Optional: Error image
                    .into(thumb3);


            thumb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Glide.with(SingleProductActivity.this)
                            .load(img1)
                            .placeholder(R.drawable.loading) // Optional: Placeholder image
                            .error(R.drawable.mark) // Optional: Error image
                            .into(mainImage);

                }
            });

            thumb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Glide.with(SingleProductActivity.this)
                            .load(img2)
                            .placeholder(R.drawable.loading) // Optional: Placeholder image
                            .error(R.drawable.mark) // Optional: Error image
                            .into(mainImage);

                }
            });

            thumb3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Glide.with(SingleProductActivity.this)
                            .load(img3)
                            .placeholder(R.drawable.loading) // Optional: Placeholder image
                            .error(R.drawable.mark) // Optional: Error image
                            .into(mainImage);

                }
            });

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                OkHttpClient okHttpClient = new OkHttpClient();

                // Build URL with query parameters dynamically
                HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/product/loadrelatedproduct")
                        .newBuilder();
                urlBuilder.addQueryParameter("category", category);
                urlBuilder.addQueryParameter("product_id", productId);

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

                    ResponseListDTO<ReturnProductDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<ReturnProductDTO>>(){}.getType());

                    if (responseDTO.isSuccess()) {

                        List<ReturnProductDTO> product_dtoList = responseDTO.getContent();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Product> productList =   new ArrayList<>();

                                for (ReturnProductDTO product:product_dtoList){
                                    productList.add(new Product(String.valueOf(product.getId()),product.getName(),String.valueOf(product.getPrice()),product.getQty(),product.getStatus(),product.getDescription(),product.getCategory(),product.getImgpath1(),product.getImgpath2(),product.getImgpath3()));

                                }


                                RecyclerView recyclerView = findViewById(R.id.related_product_recycleView);


                                recyclerView.setLayoutManager(new GridLayoutManager(SingleProductActivity.this, 2)); // 2 columns

                                ProductAdapter productAdapter = new ProductAdapter(SingleProductActivity.this,productList);
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
}



class  ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    User_DTO user;
    class ProductViewHolder extends RecyclerView.ViewHolder{

        //        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public Button Button1;
        public CardView cardView;
        public  ImageView productImage;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.myproductTitle);
            textView2 = itemView.findViewById(R.id.myproductpft);
//            imageView = itemView.findViewById(R.id.productImage);
            Button1 = itemView.findViewById(R.id.UpdateButton);
            cardView = itemView.findViewById(R.id.Myproduct_card_view);
            productImage = itemView.findViewById(R.id.myproductImage);
        }
    }

    public ArrayList<Product> productArrayList;
    private Context context;
    public ProductAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;

    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_item,null);
        ProductAdapter.ProductViewHolder productViewHolder = new ProductAdapter.ProductViewHolder(view);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {

        Product product = productArrayList.get(position);

        holder.textView1.setText(productArrayList.get(position).getName());
        holder.textView2.setText("Rs."+productArrayList.get(position).getPrice());
//        holder.imageView.setText(productArrayList.get(position).getId());

        Glide.with(context)
                .load(productArrayList.get(position).getImgpath1())
                .placeholder(R.drawable.loading) // Optional: Placeholder image
                .error(R.drawable.mark) // Optional: Error image
                .into(holder.productImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra("productId", product.getId());
                intent.putExtra("productName", product.getName());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("qty", String.valueOf(product.getQty()));
                intent.putExtra("description", product.getDescription());
                intent.putExtra("category", product.getCategory());
                intent.putExtra("imgpath1", product.getImgpath1());
                intent.putExtra("imgpath2", product.getImgpath2());
                intent.putExtra("imgpath3", product.getImgpath3());
                context.startActivity(intent);
            }
        });
        if (product.getQty() == 0){
            holder.Button1.setEnabled(false);
            holder.Button1.setText("Out of stock");
            holder.Button1.setBackgroundColor(context.getColor(R.color.terracotta));
        }
        holder.Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
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

                        // Build URL with query parameters dynamically
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/cart/add")
                                .newBuilder();
                        urlBuilder.addQueryParameter("qty", "1");
                        urlBuilder.addQueryParameter("product_id", product.getId());

                        urlBuilder.addQueryParameter("user_id",String.valueOf(user.getId()));

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
                                String message = responseDTO.getMessage();
                                if (message.equals("add")){
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Success!")
                                                    .setContentText("Product added to your cart.")
                                                    .show();
                                        }
                                    });
                                }else {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Success!")
                                                    .setContentText("Product quantity updated.")
                                                    .show();
                                        }
                                    });
                                }
                            } else {


                            }


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                    }
                }).start();

            }
        });
    }



    @Override
    public int getItemCount() {
        return  this.productArrayList.size();
    }
}