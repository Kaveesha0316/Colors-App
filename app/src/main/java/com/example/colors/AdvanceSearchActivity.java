package com.example.colors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Product;

public class AdvanceSearchActivity extends AppCompatActivity {

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

        RecyclerView recyclerView = findViewById(R.id.advancerecycleview);

        ArrayList<Product> productList =   new ArrayList<>();
        productList.add(new Product("U001","Product Title","Rs.2500.00"));
        productList.add(new Product("U002","kamal","250"));
        productList.add(new Product("U003","nimal","542"));
        productList.add(new Product("U004","sumal","250"));
        productList.add(new Product("U005","namal","140"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdvanceSearchActivity.this,2);

        recyclerView.setLayoutManager(gridLayoutManager);

        SaerchProductAdapter saerchProductAdapter = new SaerchProductAdapter(AdvanceSearchActivity.this,productList);
        recyclerView.setAdapter(saerchProductAdapter);


        Spinner spinner = findViewById(R.id.spinner);
        String sort[] = new String[]{"Sort by","Price DESC","Price ASC","Latest","Oldest"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AdvanceSearchActivity.this, android.R.layout.simple_spinner_item,sort);
        spinner.setAdapter(arrayAdapter);


            LinearLayout horizontalLayout = findViewById(R.id.advanceSeachhorizontalLayout);

// Example data
            String[] itemTitles = {"Pottery", "Wooden", "Art","Jewelry","Decors", "Seasonal"};
            int[] itemImages = {R.drawable.pot2, R.drawable.wood, R.drawable.art,R.drawable.jualary,R.drawable.decor,R.drawable.seasonal};

// Add items to the HorizontalScrollView
            for (int i = 0; i < itemTitles.length; i++) {
                // Inflate the item layout
                View itemView = getLayoutInflater().inflate(R.layout.category_item, horizontalLayout, false);

                // Set data
                ImageView itemImage = itemView.findViewById(R.id.imageView4);
                TextView itemTitle = itemView.findViewById(R.id.textView12);

                itemImage.setImageResource(itemImages[i]);
                itemTitle.setText(itemTitles[i]);

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
                priceIndicator.setText("Rs. " + progress); // Adjust this based on your price range
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
                priceIndicator.setText("Rs. " + price);
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
                priceIndicator2.setText("Rs. " + progress); // Adjust this based on your price range
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
                priceIndicator2.setText("Rs. " + price);
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




        }
}


class  SaerchProductAdapter extends RecyclerView.Adapter<SaerchProductAdapter.SaerchProductViewHolder>{

    class SaerchProductViewHolder extends RecyclerView.ViewHolder{

        //        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public Button Button1;
        public CardView cardView;
        public SaerchProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.productTitle);
            textView2 = itemView.findViewById(R.id.productPrice);
//            imageView = itemView.findViewById(R.id.productImage);
            Button1 = itemView.findViewById(R.id.addToCartButton);
            cardView = itemView.findViewById(R.id.product_card_view);
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