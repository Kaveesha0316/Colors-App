package com.example.colors.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aemerse.slider.ImageCarousel;
import com.aemerse.slider.model.CarouselItem;
import com.example.colors.R;
import com.example.colors.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import model.product;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupImageCarousel();
        setCategoryItem();

        ArrayList<product> productList =   new ArrayList<>();
        productList.add(new product("U001","shan","250"));
        productList.add(new product("U002","kamal","250"));
        productList.add(new product("U003","nimal","542"));
        productList.add(new product("U004","sumal","250"));
        productList.add(new product("U005","namal","140"));

        RecyclerView recyclerView = view.findViewById(R.id.productrecyclerView);

//        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        ProductAdapter productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

//        recyclerView.setAdapter(new ProductAdapter(productList)); // Set your adapter


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
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.productTitle);
            textView2 = itemView.findViewById(R.id.productPrice);
//            imageView = itemView.findViewById(R.id.productImage);
            Button1 = itemView.findViewById(R.id.addToCartButton);
        }
    }

    public  ArrayList<product> productArrayList;
    public ProductAdapter(ArrayList<product> productArrayList) {
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

        holder.textView1.setText(productArrayList.get(position).getName());
        holder.textView2.setText(productArrayList.get(position).getPrice());
//        holder.imageView.setText(productArrayList.get(position).getId());

        holder.Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"mobile",Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return  this.productArrayList.size();
    }
}



