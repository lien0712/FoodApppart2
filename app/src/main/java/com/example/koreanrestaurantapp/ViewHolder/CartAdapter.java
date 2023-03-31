package com.example.koreanrestaurantapp.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanrestaurantapp.Interface.ItemClickListener;
import com.example.koreanrestaurantapp.R;
import com.example.koreanrestaurantapp.model.Order;
import com.google.android.material.internal.TextDrawableHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cart_name, txt_price;
    public ImageView img_cart_count;
    private ItemClickListener itemClickListener;


    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name=(TextView) itemView.findViewById(R.id.cart_item_name);
        txt_price=(TextView) itemView.findViewById(R.id.cart_item_price);
        img_cart_count=(ImageView) itemView.findViewById(R.id.cart_item_count);

    }

    @Override
    public void onClick(View view) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    public List<Order> listData= new ArrayList<>();
    private Context context;

    int priceOfFood, quantityOfFood;


    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Locale locale= new Locale("en","US");
        NumberFormat nfm=NumberFormat.getCurrencyInstance(locale);
        Order listA= listData.get(position);
        String list=listData.get(position).getPrice();
        priceOfFood=Integer.parseInt(listData.get(position).getPrice());
        quantityOfFood=Integer.parseInt(listData.get(position).getQuantity());
        int price=priceOfFood*quantityOfFood;
        //int price=(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(nfm.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());
        //holder.img_cart_count.setImageDrawable(listData.get());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
