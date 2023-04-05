package com.example.koreanrestaurantapp.ViewHolder;


import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanrestaurantapp.Common.Common;
import com.example.koreanrestaurantapp.Interface.ItemClickListener;
import com.example.koreanrestaurantapp.R;

public class FoodViewHolderAd extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView food_name;
    public ImageView food_image;
    private ItemClickListener itemClickListener;
    public FoodViewHolderAd(@NonNull View itemView) {
        super(itemView);

        food_name= (TextView) itemView.findViewById(R.id.food_name);
        food_image=(ImageView) itemView.findViewById(R.id.food_image);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener((View.OnClickListener) this);
    }
    public void setItemOnClickListener(ItemClickListener itemClickListener){
        this.itemClickListener= itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Action:");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}

