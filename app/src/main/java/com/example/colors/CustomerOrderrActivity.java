package com.example.colors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Cart;
import model.CustomerOrder;

public class CustomerOrderrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_orderr);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<CustomerOrder> customerOrderArrayList = new ArrayList<>();
        customerOrderArrayList.add(new CustomerOrder("1","Modern Product","10","2400.00","2025-12-48","nimala Prerea","0766844125","Maradana","No 8, Colombo 08 , main street, maradana"));
        customerOrderArrayList.add(new CustomerOrder("1","Modern Product","10","2400.00","2025-12-48","nimala Prerea","0766844125","Maradana","No 8, Colombo 08 , main street, maradana"));
        customerOrderArrayList.add(new CustomerOrder("1","Modern Product","10","2400.00","2025-12-48","nimala Prerea","0766844125","Maradana","No 8, Colombo 08 , main street, maradana"));
        customerOrderArrayList.add(new CustomerOrder("1","Modern Product","10","2400.00","2025-12-48","nimala Prerea","0766844125","Maradana","No 8, Colombo 08 , main street, maradana"));
        customerOrderArrayList.add(new CustomerOrder("1","Modern Product","10","2400.00","2025-12-48","nimala Prerea","0766844125","Maradana","No 8, Colombo 08 , main street, maradana"));



        RecyclerView recyclerView = findViewById(R.id.CusOrderRcycleView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerOrderrActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        CustomerOrderAdapter customerOrderAdapter = new CustomerOrderAdapter(customerOrderArrayList);
        recyclerView.setAdapter(customerOrderAdapter);


    }
}


class  CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.CustomerOrderViewHolder>{

    static  class CustomerOrderViewHolder extends  RecyclerView.ViewHolder{

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView6;
        TextView textView7;
        TextView textView8;
        public CustomerOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView45);
            textView2 = itemView.findViewById(R.id.textView47);
            textView3 = itemView.findViewById(R.id.textView48);
            textView4 = itemView.findViewById(R.id.textView52);
            textView5 = itemView.findViewById(R.id.textView50);
            textView6 = itemView.findViewById(R.id.textView55);
            textView7 = itemView.findViewById(R.id.textView51);
            textView8 = itemView.findViewById(R.id.textView53);
        }
    }

    ArrayList<CustomerOrder> customerOrderArrayList;

    public CustomerOrderAdapter(ArrayList<CustomerOrder> customerOrderArrayList) {
        this.customerOrderArrayList = customerOrderArrayList;
    }

    @NonNull
    @Override
    public CustomerOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.customer_order_item,parent,false);
        CustomerOrderViewHolder customerOrderViewHolder = new CustomerOrderViewHolder(view);
        return customerOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderViewHolder holder, int position) {

        holder.textView1.setText(customerOrderArrayList.get(position).getName());
        holder.textView2.setText(customerOrderArrayList.get(position).getQty());
        holder.textView3.setText(customerOrderArrayList.get(position).getPrice());
        holder.textView4.setText(customerOrderArrayList.get(position).getDate());
        holder.textView5.setText(customerOrderArrayList.get(position).getCname());
        holder.textView6.setText(customerOrderArrayList.get(position).getCmobile());
        holder.textView7.setText(customerOrderArrayList.get(position).getCcity());
        holder.textView8.setText(customerOrderArrayList.get(position).getCaddrss());

    }

    @Override
    public int getItemCount() {
        return customerOrderArrayList.size();
    }
}