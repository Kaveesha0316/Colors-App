package com.example.colors.ui.orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.colors.AdvanceSearchActivity;
import com.example.colors.BuildConfig;
import com.example.colors.R;
import com.example.colors.SqlLite.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DTO.MyOrderDTO;
import DTO.ResponseListDTO;
import DTO.ReturnProductDTO;
import DTO.User_DTO;
import model.Order;
import model.Product;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrdersFragment extends Fragment {

    User_DTO user;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
        String userjson = sharedPreferences.getString("userData", null);
        if (userjson != null) {
            user = new Gson().fromJson(userjson, User_DTO.class);
        }

        if (isNetworkAvailable()) {
            // If network is available, fetch data from API
            fetchOrdersFromApi(view);
        } else {
            // If offline, fetch orders from SQLite
            loadOrdersFromSQLite(view);
        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Gson gson = new Gson();
//
//                OkHttpClient okHttpClient = new OkHttpClient();
//
//                // Build URL with query parameters dynamically
//                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.4:8080/colors/order/load")
//                        .newBuilder();
//                urlBuilder.addQueryParameter("userId", String.valueOf(user.getId()));
//
//                // Convert to URL string
//                String url = urlBuilder.build().toString();
//
//                // Create request
//                Request request = new Request.Builder()
//                        .url(url)
//                        .build();
//
//                try {
//
//                    Response response = okHttpClient.newCall(request).execute();
//                    String responsetext = response.body().string();
//
//                    Log.i("colors-log-order", responsetext);
//
//                    ResponseListDTO<MyOrderDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<MyOrderDTO>>(){}.getType());
//
//                    if (responseDTO.isSuccess()) {
//
//                        List<MyOrderDTO> product_dtoList = responseDTO.getContent();
//
//
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ArrayList<Order> orderArrayList = new ArrayList<>();
//
//                                for (MyOrderDTO product:product_dtoList){
//                                    orderArrayList.add(new Order(product.getName(),product.getQty(),product.getPrice(),product.getDate(),product.getStatus(),product.getImageUrl()));
//
//
//                                }
//
//
//                                RecyclerView recyclerView = view.findViewById(R.id.order_recylcle_view);
//
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//
//                                recyclerView.setLayoutManager(linearLayoutManager);
//
//                                OrderAdapter orderAdapter = new OrderAdapter(getContext(),orderArrayList);
//                                recyclerView.setAdapter(orderAdapter);                            }
//                        });
//                    } else {
//
//
//                    }
//
//
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//
//
//            }
//        }).start();



        return view;
    }

    private boolean isNetworkAvailable() {
        // Implement network check here
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void fetchOrdersFromApi(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                // Build URL with query parameters dynamically
                HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/order/load")
                        .newBuilder();
                urlBuilder.addQueryParameter("userId", String.valueOf(user.getId()));

                // Convert to URL string
                String url = urlBuilder.build().toString();

                // Create request
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responsetext = response.body().string();
                    Log.i("colors-log-order", responsetext);

                    ResponseListDTO<MyOrderDTO> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseListDTO<MyOrderDTO>>(){}.getType());

                    if (responseDTO.isSuccess()) {
                        List<MyOrderDTO> product_dtoList = responseDTO.getContent();

                        databaseHelper.deleteAllOrders();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Order> orderArrayList = new ArrayList<>();
                                for (MyOrderDTO product : product_dtoList) {
                                    Order order = new Order(product.getName(), product.getQty(), product.getPrice(), product.getDate(), product.getStatus(), product.getImageUrl());
                                    databaseHelper.insertOrder(order); // Save order to SQLite
                                    orderArrayList.add(order);
                                }
                                displayOrders(view, orderArrayList); // Display orders
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadOrdersFromSQLite(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Order> orders = databaseHelper.getAllOrders(); // Load from SQLite
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayOrders(view, orders); // Display orders
                    }
                });
            }
        }).start();
    }

    private void displayOrders(View view, List<Order> orders) {
        RecyclerView recyclerView = view.findViewById(R.id.order_recylcle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        OrderAdapter orderAdapter = new OrderAdapter(getContext(), orders);
        recyclerView.setAdapter(orderAdapter);
    }
}

class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.orderViewHolder>{
Context context;
    static  class orderViewHolder extends  RecyclerView.ViewHolder{

        TextView textView1 ;
        TextView textView2 ;
        TextView textView3 ;
        TextView textView4 ;
        Button button1 ;
        ImageView imageView1;
        public orderViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.order_product_name);
            textView2 = itemView.findViewById(R.id.order_product_qty);
            textView3 = itemView.findViewById(R.id.order_product_price);
            button1 = itemView.findViewById(R.id.order_status);
            textView4 = itemView.findViewById(R.id.order_product_date);
            imageView1 = itemView.findViewById(R.id.cart_product_image);
        }
    }
     List<Order> orderArrayList;

    public OrderAdapter(Context context, List<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.context = context;
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
        holder.textView3.setText("Rs."+orderArrayList.get(position).getPrice()+"0");


        String inputDate = orderArrayList.get(position).getDate(); // "2025-02-15 12:50:28"

// Step 1: Define the input format (same as the string format)
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// Step 2: Define the desired output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");

        try {
            // Convert String to Date
            Date date = inputFormat.parse(inputDate);

            // Format Date to desired output
            String formattedDate = outputFormat.format(date);

            // Set the formatted date to TextView
            holder.textView4.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textView4.setText(inputDate); // Fallback to original if parsing fails
        }



        holder.button1.setText(orderArrayList.get(position).getStatus());

        if (orderArrayList.get(position).getStatus().equals("Processing")){
            holder.button1.setBackgroundColor(context.getColor(R.color.red));
        }else if (orderArrayList.get(position).getStatus().equals("Packed")){
            holder.button1.setBackgroundColor(context.getColor(R.color.olive_green));
        }else if (orderArrayList.get(position).getStatus().equals("Dispatch")){
            holder.button1.setBackgroundColor(context.getColor(R.color.yellow));
        }else {
            holder.button1.setBackgroundColor(context.getColor(R.color.black));
        }

        Glide.with(context)
                .load(orderArrayList.get(position).getImageUrl())
                .placeholder(R.drawable.loading) // Optional: Placeholder image
                .error(R.drawable.mark) // Optional: Error image
                .into(holder.imageView1);
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}