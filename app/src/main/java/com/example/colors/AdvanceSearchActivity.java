package com.example.colors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import DTO.Product_DTO;
import DTO.ResponseListDTO;
import DTO.ReturnProductDTO;
import model.Product;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdvanceSearchActivity extends AppCompatActivity {

    String selectedName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advance_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        RecyclerView recyclerView = findViewById(R.id.advancerecycleview);
//
//        ArrayList<Product> productList =   new ArrayList<>();
//        productList.add(new Product("U001","Product Title","Rs.2500.00"));
//        productList.add(new Product("U002","kamal","250"));
//        productList.add(new Product("U003","nimal","542"));
//        productList.add(new Product("U004","sumal","250"));
//        productList.add(new Product("U005","namal","140"));
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdvanceSearchActivity.this,2);
//
//        recyclerView.setLayoutManager(gridLayoutManager);
//
//        SaerchProductAdapter saerchProductAdapter = new SaerchProductAdapter(AdvanceSearchActivity.this,productList);
//        recyclerView.setAdapter(saerchProductAdapter);

        loaddata("","","","","Latest");


        Spinner spinner = findViewById(R.id.spinner);
        String sort[] = new String[]{"Sort by","Price DESC","Price ASC","Latest","Oldest"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AdvanceSearchActivity.this, android.R.layout.simple_spinner_item,sort);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();

                if (selectedValue.equals("Price DESC")){
                    loaddata("","","","","Price DESC");
                }else if (selectedValue.equals("Price ASC")){
                    loaddata("","","","","Price ASC");
                }else if (selectedValue.equals("Latest")){
                    loaddata("","","","","Latest");
                }else if (selectedValue.equals("Oldest")){
                    loaddata("","","","","Oldest");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        LinearLayout horizontalLayout = findViewById(R.id.advanceSeachhorizontalLayout);




// Example data
        String[] itemTitles = {"All","Pottery", "Wooden", "Art", "Jewelry", "Decors", "Seasonal"};
        int[] itemImages = {R.drawable.tool,R.drawable.pot2, R.drawable.wood, R.drawable.art, R.drawable.jualary, R.drawable.decor, R.drawable.seasonal};

// Variable to track the selected item
        final View[] selectedItem = {null};

// Add items to the HorizontalScrollView
        for (int i = 0; i < itemTitles.length; i++) {
            // Inflate the item layout
            View itemView = getLayoutInflater().inflate(R.layout.category_item, horizontalLayout, false);

            // Set data
            ImageView itemImage = itemView.findViewById(R.id.imageView4);
            TextView itemTitle = itemView.findViewById(R.id.textView12);

            itemImage.setImageResource(itemImages[i]);
            itemTitle.setText(itemTitles[i]);

            // Set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                // Reset background of previous selection
                if (selectedItem[0] != null) {
                    selectedItem[0].setBackgroundColor(Color.TRANSPARENT);
                }

                // Highlight the selected item
                view.setBackgroundColor(Color.LTGRAY);  // Change this to your preferred selection color
                selectedItem[0] = view;

                // Get selected item's name
                 selectedName = itemTitle.getText().toString();
                if (selectedName.equals("All")){
                    loaddata("","","","","Latest");
                }
                    loaddata("",selectedName,"","","Latest");
                }
            });

            // Add the item to the LinearLayout
            horizontalLayout.addView(itemView);
        }


        SeekBar seekBar = findViewById(R.id.seekBar);
            seekBar.setProgress(50);
        TextView priceIndicator = findViewById(R.id.priceIndicator);
        priceIndicator.setText("Rs. 500");

        SeekBar seekBar2 = findViewById(R.id.seekBar2);
        seekBar2.setProgress(50);
        TextView priceIndicator2 = findViewById(R.id.priceIndicator2);
        priceIndicator2.setText("Rs. 5000");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the price indicator
                priceIndicator.setText("Rs." + progress); // Adjust this based on your price range
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Handle start tracking
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Handle stop tracking
            }
        });

        int minPrice = 0;
        int maxPrice = 1000;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int price = minPrice + (progress * (maxPrice - minPrice)) / seekBar.getMax();
                priceIndicator.setText("Rs." + price);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Handle start tracking
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Handle stop tracking
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the price indicator
                priceIndicator2.setText("Rs." + progress); // Adjust this based on your price range
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Handle start tracking
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Handle stop tracking
            }
        });

        int minPrice2 = 1000;
        int maxPrice2 = 10000;

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int price = minPrice + (progress * (maxPrice2 - minPrice2)) / seekBar.getMax();
                priceIndicator2.setText("Rs." + price);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Handle start tracking
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Handle stop tracking
            }
        });

        Button button = findViewById(R.id.button10);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.SearchtextInputEditText);

                String price1WithoutPrefix = priceIndicator.getText().toString().replace("Rs.", "");
                String price2WithoutPrefix = priceIndicator2.getText().toString().replace("Rs.", "");

                String[] startArray = price1WithoutPrefix.split("\\.");
                String[] endArray = price2WithoutPrefix.split("\\.");

                String start = startArray[0];
                String end = endArray[0];

                String searchText = editText.getText().toString();

                Log.i("colors-log",searchText);
                Log.i("colors-log",start);
                Log.i("colors-log",end);

                loaddata(searchText,selectedName,start,end,"Latest");

            }
        });




        }
        public void loaddata(String serachText ,String category,String startPrice , String endPrice,String sort){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();

                    OkHttpClient okHttpClient = new OkHttpClient();


                    // Build URL with query parameters dynamically
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.4:8080/colors/home/advanceSearch")
                            .newBuilder();

//                    String category = urlBuilder.build().queryParameter("category");
//                    if (category == null || category.isEmpty()) {
//                        category = null; // Pass null to the repository
//                    }
                    urlBuilder.addQueryParameter("searchText", serachText);

                    urlBuilder.addQueryParameter("category", category);
                    if (startPrice.isEmpty()){
                        urlBuilder.addQueryParameter("startPrice", "1000");
                    }else {
                        urlBuilder.addQueryParameter("startPrice", startPrice);
                    }

                    if (endPrice.isEmpty()){
                        urlBuilder.addQueryParameter("endPrice", "6000");
                    }else {
                        urlBuilder.addQueryParameter("endPrice", endPrice);
                    }

                    urlBuilder.addQueryParameter("sort", sort);





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

                                    RecyclerView recyclerView = findViewById(R.id.advancerecycleview);


                                    recyclerView.setLayoutManager(new GridLayoutManager(AdvanceSearchActivity.this, 2)); // 2 columns

                                    SaerchProductAdapter saerchProductAdapter = new SaerchProductAdapter(AdvanceSearchActivity.this,productList);
                                    recyclerView.setAdapter(saerchProductAdapter);


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


class  SaerchProductAdapter extends RecyclerView.Adapter<SaerchProductAdapter.SaerchProductViewHolder>{

    class SaerchProductViewHolder extends RecyclerView.ViewHolder{

        //        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public Button Button1;
        public CardView cardView;

        public  ImageView productImage;
        public SaerchProductViewHolder(@NonNull View itemView) {
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
    public SaerchProductAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;

    }

    @NonNull
    @Override
    public SaerchProductAdapter.SaerchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_item,null);
        SaerchProductAdapter.SaerchProductViewHolder productViewHolder = new SaerchProductAdapter.SaerchProductViewHolder(view);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaerchProductAdapter.SaerchProductViewHolder holder, int position) {

        Product product = productArrayList.get(position);

        holder.textView1.setText(productArrayList.get(position).getName());
        holder.textView2.setText(productArrayList.get(position).getPrice());
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
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return  this.productArrayList.size();
    }
}