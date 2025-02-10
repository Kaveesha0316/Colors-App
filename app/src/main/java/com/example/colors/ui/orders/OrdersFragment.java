package com.example.colors.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colors.R;

import java.util.ArrayList;

import model.Order;

public class OrdersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ArrayList<Order> orderArrayList = new ArrayList<>();
        orderArrayList.add(new Order("Test 1","10","2510.00","2024-08-24","Delivered"));
        orderArrayList.add(new Order("Test 2","8","1000.00","2024-08-25","Shiped"));
        orderArrayList.add(new Order("Test 6","45","100.00","2024-08-12","Pecked"));
        orderArrayList.add(new Order("Test 4","20","240.00","2024-08-27","Return"));
        RecyclerView recyclerView = view.findViewById(R.id.order_recylcle_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

       OrderAdapter orderAdapter = new OrderAdapter(orderArrayList);
       recyclerView.setAdapter(orderAdapter);



        return view;
    }
}

class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.orderViewHolder>{

    static  class orderViewHolder extends  RecyclerView.ViewHolder{

        TextView textView1 ;
        TextView textView2 ;
        TextView textView3 ;
        TextView textView4 ;
        Button button1 ;
        public orderViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.order_product_name);
            textView2 = itemView.findViewById(R.id.order_product_qty);
            textView3 = itemView.findViewById(R.id.order_product_price);
            button1 = itemView.findViewById(R.id.order_status);
            textView4 = itemView.findViewById(R.id.order_product_date);
        }
    }
     ArrayList<Order> orderArrayList;

    public OrderAdapter(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_item,parent,false);
        orderViewHolder orderViewHolder = new orderViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull orderViewHolder holder, int position) {

        holder.textView1.setText(orderArrayList.get(position).getNamae());
        holder.textView2.setText(orderArrayList.get(position).getQty());
        holder.textView3.setText(orderArrayList.get(position).getPrice());
        holder.textView4.setText(orderArrayList.get(position).getDate());
        holder.button1.setText(orderArrayList.get(position).getStatus());


    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}