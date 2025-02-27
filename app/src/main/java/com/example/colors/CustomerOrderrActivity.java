package com.example.colors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import DTO.CustomerOrderDTO;
import DTO.MyOrderDTO;
import DTO.ResponseDTO;
import DTO.ResponseListDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
import model.Cart;
import model.CustomerOrder;
import model.Order;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomerOrderrActivity extends AppCompatActivity {

    User_DTO user;
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

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
        String userjson = sharedPreferences.getString("userData", null);
        if (userjson != null) {
            user = new Gson().fromJson(userjson, User_DTO.class);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                OkHttpClient okHttpClient = new OkHttpClient();

                // Build URL with query parameters dynamically
                HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/order/loadcus")
                        .newBuilder();
                urlBuilder.addQueryParameter("userId", String.valueOf(user.getId()));
                urlBuilder.addQueryParameter("searchText", "");

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

                    ResponseListDTO<CustomerOrderDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<CustomerOrderDTO>>(){}.getType());

                    if (responseDTO.isSuccess()) {

                        List<CustomerOrderDTO> product_dtoList = responseDTO.getContent();



                     runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<CustomerOrder> customerOrderArrayList = new ArrayList<>();

                                for (CustomerOrderDTO product:product_dtoList){
                                    customerOrderArrayList.add(new CustomerOrder(String.valueOf(product.getOrderId()),product.getName(),product.getQty(),product.getPrice(),product.getDate(),product.getCname(),product.getCmobile(),product.getCcity(),product.getCaddress(),product.getStatus(),product.getImageUrl(),product.getClocation()));

                                }


                                RecyclerView recyclerView = findViewById(R.id.CusOrderRcycleView);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerOrderrActivity.this);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);

                                CustomerOrderAdapter customerOrderAdapter = new CustomerOrderAdapter(CustomerOrderrActivity.this,customerOrderArrayList);
                                recyclerView.setAdapter(customerOrderAdapter);                            }
                        });
                    } else {


                    }


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        }).start();

        EditText editText2 = findViewById(R.id.advanceSearchtextInputEditText3);
        editText2.addTextChangedListener(new TextWatcher() {
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

                        // Build URL with query parameters dynamically
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/order/loadcus")
                                .newBuilder();
                        urlBuilder.addQueryParameter("userId", String.valueOf(user.getId()));
                        urlBuilder.addQueryParameter("searchText", editText2.getText().toString());

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

                            ResponseListDTO<CustomerOrderDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<CustomerOrderDTO>>(){}.getType());

                            if (responseDTO.isSuccess()) {

                                List<CustomerOrderDTO> product_dtoList = responseDTO.getContent();



                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<CustomerOrder> customerOrderArrayList = new ArrayList<>();

                                        for (CustomerOrderDTO product:product_dtoList){
                                            customerOrderArrayList.add(new CustomerOrder(String.valueOf(product.getOrderId()),product.getName(),product.getQty(),product.getPrice(),product.getDate(),product.getCname(),product.getCmobile(),product.getCcity(),product.getCaddress(),product.getStatus(),product.getImageUrl(),product.getClocation()));



                                        }


                                        RecyclerView recyclerView = findViewById(R.id.CusOrderRcycleView);

                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerOrderrActivity.this);
                                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                        recyclerView.setLayoutManager(linearLayoutManager);

                                        CustomerOrderAdapter customerOrderAdapter = new CustomerOrderAdapter(CustomerOrderrActivity.this,customerOrderArrayList);
                                        recyclerView.setAdapter(customerOrderAdapter);                            }
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


    }
    public void  firebaseInsert(String name,String sts){


        HashMap<String,Object> document = new HashMap<>();
        document.put("name", name);
        document.put("user",String.valueOf(user.getId()));
        document.put("status", sts);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("order").add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("colors-log", "onSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("colors-log", "error");
                    }
                });

        firestore.collection("order")
                .whereEqualTo("user",String.valueOf(user.getId()))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("colors-log", "Listen failed.", error);
                            return;
                        }

                        if (snapshots != null) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {

                                if (dc.getType().equals(DocumentChange.Type.ADDED)){
                                    Log.d("colors-log", "notifed ");
                                    notification();
                                }

                            }
                        }
                    }
                });

    }

    public void notification() {
        // Use getActivity() to access the context of the fragment's parent activity
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "C1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Notification notification = new NotificationCompat.Builder(CustomerOrderrActivity.this, "C1")
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Product fghfgh")  // Title of the notification
                .setContentText("Your New Product has been Added successfully")  // Content with Order ID and Total Amount
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Priority
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 1000, 500})
                .build();

        notificationManager.notify(1, notification);
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

        ImageButton location;

        ImageView imageView;
        Button button;
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
            imageView = itemView.findViewById(R.id.imageView7);
            location = itemView.findViewById(R.id.locationimageButton);
            button = itemView.findViewById(R.id.button9);
        }
    }

    ArrayList<CustomerOrder> customerOrderArrayList;
    Context context ;

    public CustomerOrderAdapter(Context context ,ArrayList<CustomerOrder> customerOrderArrayList) {
        this.context =context;
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
    public void onBindViewHolder(@NonNull CustomerOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {



        String sts = customerOrderArrayList.get(position).getStatus();


        holder.textView1.setText(customerOrderArrayList.get(position).getName());
        holder.textView2.setText("Qty:"+customerOrderArrayList.get(position).getQty());
        holder.textView3.setText("Rs."+customerOrderArrayList.get(position).getPrice()+"0");

        holder.textView5.setText(customerOrderArrayList.get(position).getCname());
        holder.textView6.setText(customerOrderArrayList.get(position).getCmobile());
        holder.textView7.setText(customerOrderArrayList.get(position).getCcity());
        holder.textView8.setText(customerOrderArrayList.get(position).getCaddrss());

        Glide.with(context)
                .load(customerOrderArrayList.get(position).getImage())
                .placeholder(R.drawable.loading) // Optional: Placeholder image
                .error(R.drawable.mark) // Optional: Error image
                .into(holder.imageView);

        CustomerOrder customerOrder2 = customerOrderArrayList.get(position);

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LocationactivityActivity.class);
                intent.putExtra("location",customerOrder2.getClocation());
                context.startActivity(intent);
            }
        });

        String inputDate = customerOrderArrayList.get(position).getDate();


        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");

        try {
            Date date = inputFormat.parse(inputDate);

            String formattedDate = outputFormat.format(date);

            // Set the formatted date to TextView
            holder.textView4.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textView4.setText(inputDate); // Fallback to original if parsing fails
        }

        holder.button.setText(sts);
        if (sts.equals("Processing")){
            holder.button.setBackgroundColor(context.getColor(R.color.yellow));
        }else if (sts.equals("Packed")){
            holder.button.setBackgroundColor(context.getColor(R.color.terracotta));
        }else if (sts.equals("Dispatch")){
            holder.button.setBackgroundColor(context.getColor(R.color.olive_green));
        }else if (sts.equals("Deleverd")){
            holder.button.setBackgroundColor(context.getColor(R.color.black));
        }

        final int currentPosition = position;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();

                        OkHttpClient okHttpClient = new OkHttpClient();
                        String orderId = customerOrderArrayList.get(currentPosition).getId();
                        String status = (String) holder.button.getText();
                        // Build URL with query parameters dynamically
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/order/changests")
                                .newBuilder();
                        urlBuilder.addQueryParameter("orderId",orderId);
                        urlBuilder.addQueryParameter("status",status);



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

                                if (message.equals("Packed")){
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.button.setText("Packed");
                                            holder.button.setBackgroundColor(context.getColor(R.color.terracotta));
                                        }
                                    });
                                }else if (message.equals("Processing")){
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.button.setText("Processing");
                                            holder.button.setBackgroundColor(context.getColor(R.color.olive_green));

                                        }
                                    });
                                }else if (message.equals("Dispatch")){
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.button.setText("Dispatch");
                                            holder.button.setBackgroundColor(context.getColor(R.color.olive_green));

                                        }
                                    });
                                }else {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.button.setText("Deleverd");
                                            holder.button.setBackgroundColor(context.getColor(R.color.black));

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
        return customerOrderArrayList.size();
    }
}