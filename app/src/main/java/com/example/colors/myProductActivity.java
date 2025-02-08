package com.example.colors;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

import model.MyProduct;

public class myProductActivity extends AppCompatActivity {

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
        ArrayList<MyProduct> productList =   new ArrayList<>();
        productList.add(new MyProduct("U001","Rs.2500.00","50","10","Rs.2500.00","1"));
        productList.add(new MyProduct("U001","Rs.2500.00","50","10","Rs.2500.00","1"));
        productList.add(new MyProduct("U001","Rs.2500.00","50","10","Rs.2500.00","1"));
        productList.add(new MyProduct("U001","Rs.2500.00","50","10","Rs.2500.00","1"));
        productList.add(new MyProduct("U001","Rs.2500.00","50","10","Rs.2500.00","1"));

        RecyclerView recyclerView = findViewById(R.id.myproductRecycleView);

        recyclerView.setLayoutManager(new GridLayoutManager(myProductActivity.this, 2)); // 2 columns

        MyProductAdapter productAdapter = new MyProductAdapter(myProductActivity.this,productList);
        recyclerView.setAdapter(productAdapter);
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
        holder.textView5.setText(productArrayList.get(position).getProfit());
//        holder.imageView.setText(productArrayList.get(position).getId());

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, SingleProductActivity.class);
//                intent.putExtra("productId", product.getId());
//                intent.putExtra("productName", product.getName());
//                intent.putExtra("productPrice", product.getPrice());
//                context.startActivity(intent);
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return  this.productArrayList.size();
    }
}