package com.example.colors.ui.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.colors.R;
import com.example.colors.databinding.FragmentCartBinding;

import java.util.ArrayList;

import model.Cart;


public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ArrayList<Cart> cartArrayList = new ArrayList<>();
        cartArrayList.add(new Cart("Test 1","Rs.25441.00"));
        cartArrayList.add(new Cart("Test 2","Rs.25441.00"));
        cartArrayList.add(new Cart("Test 3","Rs.25441.00"));

        RecyclerView recyclerView = view.findViewById(R.id.cartrecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartArrayList);
        recyclerView.setAdapter(cartAdapter);

        return view;
    }
}

class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder>{

    class cartViewHolder extends RecyclerView.ViewHolder{

        TextView textView1;
        TextView textView2;

        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.order_product_name);
            textView2 = itemView.findViewById(R.id.order_product_price);
        }
    }

    public ArrayList<Cart> cartlist;

    public CartAdapter(ArrayList<Cart> cartlist) {
        this.cartlist = cartlist;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item,parent,false);
        cartViewHolder cartViewHolder = new cartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, int position) {

        holder.textView1.setText(cartlist.get(position).getName());
        holder.textView2.setText(cartlist.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return this.cartlist.size();
    }
}

