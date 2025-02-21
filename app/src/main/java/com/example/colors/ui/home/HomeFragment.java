package com.example.colors.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.bumptech.glide.Glide;
import com.example.colors.AdvanceSearchActivity;
import com.example.colors.BuildConfig;
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
import DTO.ReturnProductDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
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

//        setupImageCarousel();
//        setCategoryItem();
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Gson gson = new Gson();
//
//                OkHttpClient okHttpClient = new OkHttpClient();
//
//                // Build URL with query parameters dynamically
//                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.4:8080/colors/home/loadproduct")
//                        .newBuilder();
//                urlBuilder.addQueryParameter("searchText", "");
//
//               // Convert to URL string
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
//                             Log.i("colors-log", responsetext);
//
//                    ResponseListDTO<ReturnProductDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<ReturnProductDTO>>(){}.getType());
//
//                    if (responseDTO.isSuccess()) {
//
//                        List<ReturnProductDTO> product_dtoList = responseDTO.getContent();
//
//
//
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ArrayList<Product> productList =   new ArrayList<>();
//
//                                            for (ReturnProductDTO product:product_dtoList){
//                                                productList.add(new Product(String.valueOf(product.getId()),product.getName(),String.valueOf(product.getPrice()),product.getQty(),product.getStatus(),product.getDescription(),product.getCategory(),product.getImgpath1(),product.getImgpath2(),product.getImgpath3()));
//
//                                            }
//
//
//                                            RecyclerView recyclerView = view.findViewById(R.id.productrecyclerView);
//
//
//                                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
//
//                                            ProductAdapter productAdapter = new ProductAdapter(getContext(),productList);
//                                            recyclerView.setAdapter(productAdapter);
//
//
//                                            ImageButton imageButton = view.findViewById(R.id.advancesearchimageButton);
//                                            imageButton.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    Intent intent = new Intent(getContext(), AdvanceSearchActivity.class);
//                                                    startActivity(intent);
//                                                }
//                                            });
//                                        }
//                                    });
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
//
//        TextInputEditText searchEditText = view.findViewById(R.id.SearchtextInputEditText);
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Gson gson = new Gson();
//
//                        OkHttpClient okHttpClient = new OkHttpClient();
//
//                        // Build URL with query parameters dynamically
//                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.4:8080/colors/home/loadproduct")
//                                .newBuilder();
//                        urlBuilder.addQueryParameter("searchText", searchEditText.getText().toString());
//
//                        // Convert to URL string
//                        String url = urlBuilder.build().toString();
//
//                        // Create request
//                        Request request = new Request.Builder()
//                                .url(url)
//                                .build();
//
//                        try {
//
//                            Response response = okHttpClient.newCall(request).execute();
//                            String responsetext = response.body().string();
//
//                            Log.i("colors-log", responsetext);
//
//                            ResponseListDTO<ReturnProductDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<ReturnProductDTO>>(){}.getType());
//
//                            if (responseDTO.isSuccess()) {
//
//                                List<ReturnProductDTO> product_dtoList = responseDTO.getContent();
//
//
//
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ArrayList<Product> productList =   new ArrayList<>();
//
//                                        for (ReturnProductDTO product:product_dtoList){
//                                            productList.add(new Product(String.valueOf(product.getId()),product.getName(),String.valueOf(product.getPrice()),product.getQty(),product.getStatus(),product.getDescription(),product.getCategory(),product.getImgpath1(),product.getImgpath2(),product.getImgpath3()));
//
//                                        }
//
//
//                                        RecyclerView recyclerView = view.findViewById(R.id.productrecyclerView);
//
//
//                                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
//
//                                        ProductAdapter productAdapter = new ProductAdapter(getContext(),productList);
//                                        recyclerView.setAdapter(productAdapter);
//
//
//                                        ImageButton imageButton = view.findViewById(R.id.advancesearchimageButton);
//                                        imageButton.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                Intent intent = new Intent(getContext(), AdvanceSearchActivity.class);
//                                                startActivity(intent);
//                                            }
//                                        });
//                                    }
//                                });
//                            } else {
//
//
//                            }
//
//
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//
//
//                    }
//
//                }).start();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupImageCarousel();
        setCategoryItem();
        if (isNetworkAvailable()) {





            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();

                    OkHttpClient okHttpClient = new OkHttpClient();

                    // Build URL with query parameters dynamically
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/home/loadproduct")
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

                        ResponseListDTO<ReturnProductDTO> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseListDTO<ReturnProductDTO>>() {
                        }.getType());

                        if (responseDTO.isSuccess()) {

                            List<ReturnProductDTO> product_dtoList = responseDTO.getContent();


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<Product> productList = new ArrayList<>();

                                    for (ReturnProductDTO product : product_dtoList) {
                                        productList.add(new Product(String.valueOf(product.getId()), product.getName(), String.valueOf(product.getPrice()), product.getQty(), product.getStatus(), product.getDescription(), product.getCategory(), product.getImgpath1(), product.getImgpath2(), product.getImgpath3()));

                                    }


                                    RecyclerView recyclerView = getActivity().findViewById(R.id.productrecyclerView);


                                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

                                    ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
                                    recyclerView.setAdapter(productAdapter);


                                    ImageButton imageButton = getActivity().findViewById(R.id.advancesearchimageButton);
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

            TextInputEditText searchEditText = getActivity().findViewById(R.id.SearchtextInputEditText);
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
                            HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/home/loadproduct")
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

                                ResponseListDTO<ReturnProductDTO> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseListDTO<ReturnProductDTO>>() {
                                }.getType());

                                if (responseDTO.isSuccess()) {

                                    List<ReturnProductDTO> product_dtoList = responseDTO.getContent();


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayList<Product> productList = new ArrayList<>();

                                            for (ReturnProductDTO product : product_dtoList) {
                                                productList.add(new Product(String.valueOf(product.getId()), product.getName(), String.valueOf(product.getPrice()), product.getQty(), product.getStatus(), product.getDescription(), product.getCategory(), product.getImgpath1(), product.getImgpath2(), product.getImgpath3()));

                                            }


                                            RecyclerView recyclerView = getActivity().findViewById(R.id.productrecyclerView);


                                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

                                            ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
                                            recyclerView.setAdapter(productAdapter);


                                            ImageButton imageButton = getActivity().findViewById(R.id.advancesearchimageButton);
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
        }
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

    private boolean isNetworkAvailable() {
        // Implement network check here
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
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
    public User_DTO user;
    class ProductViewHolder extends RecyclerView.ViewHolder{

//        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public Button Button1;
        public CardView cardView;

        public  ImageView productImage;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.myproductTitle);
            textView2 = itemView.findViewById(R.id.myproductpft);
//            imageView = itemView.findViewById(R.id.productImage);
            Button1 = itemView.findViewById(R.id.UpdateButton);
            cardView = itemView.findViewById(R.id.Myproduct_card_view);
            productImage = itemView.findViewById(R.id.myproductImage);
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
        holder.textView2.setText( "Rs."+productArrayList.get(position).getPrice());


        Glide.with(context)
                .load(productArrayList.get(position).getImgpath1())
                .placeholder(R.drawable.loading) // Optional: Placeholder image
                .error(R.drawable.mark) // Optional: Error image
                .into(holder.productImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra("productId", product.getId());
                intent.putExtra("productName", product.getName());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("qty", String.valueOf(product.getQty()));
                intent.putExtra("description", product.getDescription());
                intent.putExtra("category", product.getCategory());
                intent.putExtra("imgpath1", product.getImgpath1());
                intent.putExtra("imgpath2", product.getImgpath2());
                intent.putExtra("imgpath3", product.getImgpath3());
                context.startActivity(intent);
            }
        });
        if (product.getQty() == 0){
            holder.Button1.setEnabled(false);
            holder.Button1.setText("Out of stock");
            holder.Button1.setBackgroundColor(context.getColor(R.color.terracotta));
        }
        holder.Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/cart/add")
                                .newBuilder();
                        urlBuilder.addQueryParameter("qty", "1");
                        urlBuilder.addQueryParameter("product_id", product.getId());

                        urlBuilder.addQueryParameter("user_id",String.valueOf(user.getId()));

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
                                if (message.equals("add")){
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Success!")
                                                    .setContentText("Product added to your cart.")
                                                    .show();
                                        }
                                    });
                                }else {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Success!")
                                                    .setContentText("Product quantity updated.")
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



    @Override
    public int getItemCount() {
        return  this.productArrayList.size();
    }


}



