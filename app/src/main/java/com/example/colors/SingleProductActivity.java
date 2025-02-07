package com.example.colors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

import model.Product;

public class SingleProductActivity extends AppCompatActivity {

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

        Intent intent = getIntent();
        if (intent != null) {
            productName.setText(intent.getStringExtra("productName"));
            productPrice.setText(intent.getStringExtra("productPrice"));
        }

        ImageView mainImage = findViewById(R.id.sinlge_main_product_image);
        ImageView thumb1 = findViewById(R.id.thumb1);
        ImageView thumb2 = findViewById(R.id.thumb2);
        ImageView thumb3 = findViewById(R.id.thumb3);

        thumb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainImage.setImageResource(R.drawable.signin_img2);
            }
        });

        thumb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainImage.setImageResource(R.drawable.signin_img3);
            }
        });

        thumb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainImage.setImageResource(R.drawable.signup_img2);
            }
        });

        ArrayList<Product> productList =   new ArrayList<>();
        productList.add(new Product("U001","Product Title","Rs.2500.00"));
        productList.add(new Product("U002","kamal","250"));
        productList.add(new Product("U003","nimal","542"));
        productList.add(new Product("U004","sumal","250"));
        productList.add(new Product("U005","namal","140"));

        RecyclerView recyclerView = findViewById(R.id.related_product_recycleView);

//        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(SingleProductActivity.this, 2)); // 2 columns

       ProductAdapter productAdapter = new ProductAdapter(SingleProductActivity.this,productList);
        recyclerView.setAdapter(productAdapter);
    }
}



class  ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    class ProductViewHolder extends RecyclerView.ViewHolder{

        //        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public Button Button1;
        public CardView cardView;
        public ProductViewHolder(@NonNull View itemView) {
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