package com.example.colors.ui.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.colors.AddProductActivity;
import com.example.colors.BuildConfig;
import com.example.colors.InvoiceActivityMainActivity;
import com.example.colors.R;
import com.example.colors.databinding.FragmentCartBinding;
import com.example.colors.ui.home.HomeFragment;
import com.example.colors.ui.profile.DashboardViewModel;
import com.example.colors.ui.profile.profileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import DTO.Cart_DTO;
import DTO.ResponseDTO;
import DTO.ResponseListDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;
import model.Cart;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    User_DTO user;
    Double total;
    ArrayList<Cart> cartArrayList;
    CartAdapter cartAdapter;
    TextView textView21, textView23;
    private  static  final String TAG = "payhare demo";

//    private TextView textView;
SweetAlertDialog progressDialog;

    private  final ActivityResultLauncher<Intent> payHareLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    Intent data = result.getData();
                    if (data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)){
                        Serializable serializable = data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);


                        if(serializable instanceof PHResponse){
                            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) serializable;
                            String msg = response.isSuccess() ?"Payment success"+response.getData():"Payment Faild"+response;
                            Log.i(TAG,msg);
//                            textView.setText(msg);
                            progressDialog.dismissWithAnimation();
                            removeperchesproduct();
                        }
                    }
                }else if (result.getResultCode() == Activity.RESULT_CANCELED){
//                    textView.setText("user canceld the request");
                    progressDialog.dismissWithAnimation();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
        String userjson = sharedPreferences.getString("userData", null);
        if (userjson != null) {
            user = new Gson().fromJson(userjson, User_DTO.class);
        }

        textView21 = view.findViewById(R.id.textView21);
        textView23 = view.findViewById(R.id.textView23);


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();



//        RecyclerView recyclerView = view.findViewById(R.id.cartrecyclerView);

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        cartArrayList = new ArrayList<>();
//        cartAdapter = new CartAdapter(getContext(), cartArrayList, this);
//        recyclerView.setAdapter(cartAdapter);
//
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                Cart item = cartArrayList.get(position);
//                cartArrayList.remove(position);
//                cartAdapter.notifyItemRemoved(position);
//                deleteCartItem(item.getId());
//                updateTotal();
//            }
//        }).attachToRecyclerView(recyclerView);
//
//        loadCart();
//
//        Button button = view.findViewById(R.id.button3);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (user.getAddress().equals("")&&user.getCity().equals("")){
//                  new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                      .setTitleText("Warning!")
//                          .setContentText("Please update yor address and city")
//                             .show();
//                    profileFragment fragmentB = new profileFragment();
//
//
//                    FragmentManager fragmentManager = getParentFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//
//                    fragmentTransaction.replace(R.id.fragment_container, fragmentB);
//
//
//                    fragmentTransaction.addToBackStack(null);
//
//
//                    fragmentTransaction.commit();
//                }else{
//                    new Thread(() -> {
//                        OkHttpClient client = new OkHttpClient();
//                        HttpUrl url = HttpUrl.parse("http://192.168.1.4:8080/colors/cart/loadcart")
//                                .newBuilder()
//                                .addQueryParameter("userId", String.valueOf(user.getId()))
//                                .build();
//                        Request request = new Request.Builder().url(url.toString()).build();
//                        try {
//                            Response response = client.newCall(request).execute();
//
//                            ResponseListDTO<Cart_DTO> responseDTO = new Gson().fromJson(response.body().string(), new TypeToken<ResponseListDTO<Cart_DTO>>() {}.getType());
//                            if (responseDTO.isSuccess()) {
//                                List<Cart_DTO> cartDtoList = responseDTO.getContent();
//                                StringBuilder name = new StringBuilder();
//
//                                for (Cart_DTO cartDto : cartDtoList) {
//                                    name.append(cartDto.getProduct_name())
//                                            .append(" x ")
//                                            .append(cartDto.getQty())
//                                            .append(", ");
//                                }
//
//// Remove trailing comma and space if not empty
//                                if (name.length() > 0) {
//                                    name.setLength(name.length() - 2);
//                                }
//
//                                String result = name.toString();
//                                Log.i("colors-log",result);
//                                getActivity().runOnUiThread(() -> {
//                                    initiatePayment(result);
//
//
//                                });
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }).start();
//
//                }
//
//            }
//        });
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        firestore.collection("notifications")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w("FirestoreListener", "Listen failed.", error);
//                            return;
//                        }
//
//                        if (value != null && !value.isEmpty()) {
//                            for (DocumentSnapshot doc : value.getDocuments()) {
//                                String notificationMessage = doc.getString("message");
//                                Log.d("FirestoreListener", "New Notification: " + notificationMessage);
//                            }
//                        } else {
//                            Log.d("FirestoreListener", "No notifications found.");
//                        }
//                    }
//                });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
//        String userjson = sharedPreferences.getString("userData", null);
//        if (userjson != null) {
//            user = new Gson().fromJson(userjson, User_DTO.class);
//        }
//
//        textView21 = getActivity().findViewById(R.id.textView21);
//        textView23 = getActivity().findViewById(R.id.textView23);
        RecyclerView recyclerView = getActivity().findViewById(R.id.cartrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartArrayList = new ArrayList<>();
        cartAdapter = new CartAdapter(getContext(), cartArrayList, this);
        recyclerView.setAdapter(cartAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Cart item = cartArrayList.get(position);
                cartArrayList.remove(position);
                cartAdapter.notifyItemRemoved(position);
                deleteCartItem(item.getId());
                updateTotal();
            }
        }).attachToRecyclerView(recyclerView);

        loadCart();

        Button button = getActivity().findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.getAddress() == null &&user.getCity() == null){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning!")
                            .setContentText("Please update yor address and city")
                            .show();
                    profileFragment fragmentB = new profileFragment();


                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                    fragmentTransaction.replace(R.id.fragment_container, fragmentB);


                    fragmentTransaction.addToBackStack(null);


                    fragmentTransaction.commit();
                }else{
                    new Thread(() -> {
                        OkHttpClient client = new OkHttpClient();
                        HttpUrl url = HttpUrl.parse(BuildConfig.URL+"/cart/loadcart")
                                .newBuilder()
                                .addQueryParameter("userId", String.valueOf(user.getId()))
                                .build();
                        Request request = new Request.Builder().url(url.toString()).build();
                        try {
                            Response response = client.newCall(request).execute();

                            ResponseListDTO<Cart_DTO> responseDTO = new Gson().fromJson(response.body().string(), new TypeToken<ResponseListDTO<Cart_DTO>>() {}.getType());
                            if (responseDTO.isSuccess()) {
                                List<Cart_DTO> cartDtoList = responseDTO.getContent();
                                StringBuilder name = new StringBuilder();

                                for (Cart_DTO cartDto : cartDtoList) {
                                    name.append(cartDto.getProduct_name())
                                            .append(" x ")
                                            .append(cartDto.getQty())
                                            .append(", ");
                                }

// Remove trailing comma and space if not empty
                                if (name.length() > 0) {
                                    name.setLength(name.length() - 2);
                                }

                                String result = name.toString();
                                Log.i("colors-log",result);
                                getActivity().runOnUiThread(() -> {
                                    initiatePayment(result);


                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                }

            }
        });
    }


    ;
    Random random = new Random();
    int randomNum = 100000 + random.nextInt(900000); // Generates a 6-digit random number
    String orderId = "ORD" + randomNum;
    public void removeperchesproduct(){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/cart/removeperchesproduct")
                    .newBuilder();

            urlBuilder.addQueryParameter("userId", String.valueOf(user.getId()));
            urlBuilder.addQueryParameter("total", String.valueOf(total));
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();

                ResponseDTO<String> responseDTO = new Gson().fromJson(response.body().string(), new TypeToken<ResponseDTO<String>>() {}.getType());
                if (responseDTO.isSuccess()) {

                    firebaseInsert();

                    Intent intent = new Intent(requireContext(), InvoiceActivityMainActivity.class);
                    intent.putExtra("ORDER_ID", orderId);
                    intent.putExtra("TOTAL_AMOUNT", total);
                    intent.putExtra("DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    intent.putExtra("ITEM",cartArrayList);
                    startActivity(intent);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void  firebaseInsert(){


        HashMap<String,Object> document = new HashMap<>();
        document.put("buyer",String.valueOf(user.getId()));
        document.put("order",orderId);
        document.put("total",String.valueOf(total));
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("buyer").add(document)
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

        firestore.collection("buyer")
                .whereEqualTo("buyer",String.valueOf(user.getId()))
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

    private  void initiatePayment(String name){
        InitRequest req = new InitRequest();
        req.setMerchantId("1221290");       // Merchant ID
        req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
        req.setAmount(total);             // Final Amount to be charged
        req.setOrderId("230000123");        // Unique Reference ID
        req.setItemsDescription(name);  // Item description title
        req.setCustom1("This is the custom message 1");
        req.setCustom2("This is the custom message 2");
        req.getCustomer().setFirstName("Saman");
        req.getCustomer().setLastName("Perera");
        req.getCustomer().setEmail("samanp@gmail.com");
        req.getCustomer().setPhone("+94771234567");
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

//Optional Params

        req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, name, 1, 1000.0));

//        req.setNotifyUrl(“https://your-callback-url”);

        Intent intent = new Intent(getContext(), PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);

        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);

        payHareLauncher.launch(intent);

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("Loading...");
        progressDialog.setCancelable(false); // Prevent dialog from being dismissed by tapping outside
        progressDialog.show(); // Show the progress dialog


    }

    private void loadCart() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            HttpUrl url = HttpUrl.parse(BuildConfig.URL+"/cart/loadcart")
                    .newBuilder()
                    .addQueryParameter("userId", String.valueOf(user.getId()))
                    .build();
            Request request = new Request.Builder().url(url.toString()).build();
            try {
                Response response = client.newCall(request).execute();

                ResponseListDTO<Cart_DTO> responseDTO = new Gson().fromJson(response.body().string(), new TypeToken<ResponseListDTO<Cart_DTO>>() {}.getType());
                if (responseDTO.isSuccess()) {
                    getActivity().runOnUiThread(() -> {
                        cartArrayList.clear();
                        for (Cart_DTO product : responseDTO.getContent()) {
                            cartArrayList.add(new Cart(product.getId(), String.valueOf(product.getQty()), product.getProduct_name(), product.getProduct_price(), product.getProduct_imagepath(), product.getProduct_Id()));
                        }
                        cartAdapter.notifyDataSetChanged();
                        updateTotal();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void updateTotal() {
        double subTotal = 0.0;
        for (Cart cart : cartArrayList) {
            subTotal += cart.getProduct_price() * Integer.parseInt(cart.getQty());
        }
        textView21.setText("Rs." + subTotal);
        textView23.setText("Rs." + subTotal);
        total = subTotal;
    }

    private void deleteCartItem(int itemId) {

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/cart/delete")
                    .newBuilder();

            urlBuilder.addQueryParameter("cartItemId", String.valueOf(itemId));
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();

                ResponseDTO<String> responseDTO = new Gson().fromJson(response.body().string(), new TypeToken<ResponseDTO<String>>() {}.getType());
                if (responseDTO.isSuccess()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Product was Removed.")
                                    .show();
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void notification() {
        // Use getActivity() to access the context of the fragment's parent activity
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "C1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Notification notification = new NotificationCompat.Builder(getContext(), "C1")
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Order Placed!")  // Title of the notification
                .setContentText("Your order #" + orderId + " has been placed successfully. Total: RS." + total+"0")  // Content with Order ID and Total Amount
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Priority
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 1000, 500})
                .build();

        notificationManager.notify(1, notification);
    }


}

class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder> {
    private Context context;
    private ArrayList<Cart> cartlist;
    private CartFragment cartFragment;
    User_DTO user;

    class cartViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;
        ImageView imageView;
        ImageButton imageButtonup, imageButtondown;

        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.order_product_name);
            textView2 = itemView.findViewById(R.id.order_product_price);
            textView3 = itemView.findViewById(R.id.text_quantity);
            imageView = itemView.findViewById(R.id.cart_product_image);
            imageButtonup = itemView.findViewById(R.id.imageButton2);
            imageButtondown = itemView.findViewById(R.id.imageButton6);
        }
    }

    public CartAdapter(Context context, ArrayList<Cart> cartlist, CartFragment cartFragment) {
        this.context = context;
        this.cartlist = cartlist;
        this.cartFragment = cartFragment;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new cartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart item = cartlist.get(position);
        holder.textView1.setText(item.getProduct_name());
        holder.textView2.setText("Rs." + item.getProduct_price());
        holder.textView3.setText(item.getQty());
        Glide.with(context).load(item.getProduct_imagepath()).into(holder.imageView);

        holder.imageButtonup.setOnClickListener(v -> {

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
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/cart/update")
                            .newBuilder();
                    urlBuilder.addQueryParameter("qty", "1");
                    urlBuilder.addQueryParameter("product_id", String.valueOf(item.getProduct_Id()));
                    urlBuilder.addQueryParameter("user_id",String.valueOf(user.getId()));
                    urlBuilder.addQueryParameter("status","up");

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
                            if (message.equals("Updated")){
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        item.setQty(String.valueOf(Integer.parseInt(item.getQty()) + 1));
                                        notifyItemChanged(position);
                                        cartFragment.updateTotal();
//                                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                                                .setTitleText("Success!")
//                                                .setContentText("Product Updated.")
//                                                .show();
                                    }
                                });
                            }else {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Warning!")
                                                .setContentText("Product stock is low")
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
        });
        holder.imageButtondown.setOnClickListener(v -> {
            int qty = Integer.parseInt(item.getQty());
            if (qty > 1) {
                item.setQty(String.valueOf(Integer.parseInt(item.getQty()) - 1));
                notifyItemChanged(position);
                cartFragment.updateTotal();

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
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/cart/update")
                                .newBuilder();
                        urlBuilder.addQueryParameter("qty", "1");
                        urlBuilder.addQueryParameter("product_id", String.valueOf(item.getProduct_Id()));
                        urlBuilder.addQueryParameter("user_id",String.valueOf(user.getId()));
                        urlBuilder.addQueryParameter("status","down");

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
                                if (message.equals("Updated")){
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

//                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                                                    .setTitleText("Success!")
//                                                    .setContentText("Product Updated.")
//                                                    .show();
                                        }
                                    });
                                }else {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Warning!")
                                                    .setContentText("Product stock is low")
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
    @Override public int getItemCount() { return cartlist.size(); }
}


