package com.example.colors.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aemerse.slider.ImageCarousel;
import com.aemerse.slider.model.CarouselItem;
import com.example.colors.AdvanceSearchActivity;
import com.example.colors.R;
import com.example.colors.SingleProductActivity;
import com.example.colors.databinding.FragmentHomeBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import DTO.Product_DTO;
import DTO.ResponseDTO;
import DTO.ResponseListDTO;
import DTO.User_DTO;
import model.Product;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupImageCarousel();
        setCategoryItem();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                OkHttpClient okHttpClient = new OkHttpClient();

                // Build URL with query parameters dynamically
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.2:8080/colors/home/loadproduct")
                        .newBuilder();
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

                             Log.i("colors-log", responsetext);

                    ResponseListDTO<Product_DTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<Product_DTO>>(){}.getType());

                    if (responseDTO.isSuccess()) {

                        List<Product_DTO> product_dtoList = responseDTO.getContent();



                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayList<Product> productList =   new ArrayList<>();

                                            for (Product_DTO product:product_dtoList){
                                                productList.add(new Product(String.valueOf(product.getId()),product.getName(),String.valueOf(product.getPrice())));

                                            }


                                            RecyclerView recyclerView = view.findViewById(R.id.productrecyclerView);


                                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

                                            ProductAdapter productAdapter = new ProductAdapter(getContext(),productList);
                                            recyclerView.setAdapter(productAdapter);


                                            ImageButton imageButton = view.findViewById(R.id.advancesearchimageButton);
                                            imageButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(getContext(), AdvanceSearchActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                    } else {


                    }


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        }).start();

        TextInputEditText searchEditText = view.findViewById(R.id.SearchtextInputEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
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
                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.2:8080/colors/home/loadproduct")
                                .newBuilder();
                        urlBuilder.addQueryParameter("searchText", searchEditText.getText().toString());

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

                            ResponseListDTO<Product_DTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<Product_DTO>>(){}.getType());

                            if (responseDTO.isSuccess()) {

                                List<Product_DTO> product_dtoList = responseDTO.getContent();



                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<Product> productList =   new ArrayList<>();

                                        for (Product_DTO product:product_dtoList){
                                            productList.add(new Product(String.valueOf(product.getId()),product.getName(),String.valueOf(product.getPrice())));

                                        }


                                        RecyclerView recyclerView = view.findViewById(R.id.productrecyclerView);


                                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

                                        ProductAdapter productAdapter = new ProductAdapter(getContext(),productList);
                                        recyclerView.setAdapter(productAdapter);


                                        ImageButton imageButton = view.findViewById(R.id.advancesearchimageButton);
                                        imageButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getContext(), AdvanceSearchActivity.class);
                                                startActivity(intent);
                                            }
                                        });
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

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });




        return view;
    }

    private void setupImageCarousel() {
        ImageCarousel carousel = binding.carousel;
        carousel.registerLifecycle(getLifecycle());
        carousel.setAutoPlay(true);
        carousel.setAutoPlayDelay(3000);
        carousel.setShowNavigationButtons(false);



        List<CarouselItem> carouselItems = new ArrayList<>();
        carouselItems.add(new CarouselItem(R.drawable.cr1));
        carouselItems.add(new CarouselItem(R.drawable.cr6));
        carouselItems.add(new CarouselItem(R.drawable.cr5));

        carousel.setData(carouselItems);
    }

    private  void setCategoryItem(){
        LinearLayout horizontalLayout = binding.horizontalLayout;

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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
            textView1 = itemView.findViewById(R.id.myproductTitle);
            textView2 = itemView.findViewById(R.id.myproductpft);
//            imageView = itemView.findViewById(R.id.productImage);
            Button1 = itemView.findViewById(R.id.UpdateButton);
            cardView = itemView.findViewById(R.id.Myproduct_card_view);
        }
    }

    public  ArrayList<Product> productArrayList;
    private Context context;
    public ProductAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_item,null);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

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



