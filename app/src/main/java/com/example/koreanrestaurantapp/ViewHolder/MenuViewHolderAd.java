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


public class MenuViewHolderAd  extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener  {

    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public MenuViewHolderAd(@NonNull View itemView) {
        super(itemView);

        txtMenuName= (TextView) itemView.findViewById(R.id.menu_name);
        imageView=(ImageView) itemView.findViewById(R.id.menu_image);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener((View.OnClickListener) this);

    }

    public void setItemOnClickListener(ItemClickListener itemClickListener){
        this.itemClickListener= itemClickListener;
    }

    public  void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(),false);

    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Action:");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }

}
