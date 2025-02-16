package com.example.colors.ui.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.colors.R;
import com.example.colors.databinding.FragmentCartBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import DTO.Cart_DTO;
import DTO.ResponseDTO;
import DTO.ResponseListDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
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
        RecyclerView recyclerView = view.findViewById(R.id.cartrecyclerView);
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
        return view;
    }

    private void loadCart() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            HttpUrl url = HttpUrl.parse("http://192.168.1.4:8080/colors/cart/loadcart")
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
    }

    private void deleteCartItem(int itemId) {

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.4:8080/colors/cart/delete")
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
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.4:8080/colors/cart/update")
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
                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.4:8080/colors/cart/update")
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


