package com.example.koreanrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.koreanrestaurantapp.Interface.ItemClickListener;
import com.example.koreanrestaurantapp.ViewHolder.FoodViewHolder;
import com.example.koreanrestaurantapp.ViewHolder.FoodViewHolderAd;
import com.example.koreanrestaurantapp.model.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MenuAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId="";
    FirebaseRecyclerAdapter<Food, FoodViewHolderAd> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        database= FirebaseDatabase.getInstance();
        foodList= database.getReference("Food");

        recyclerView=(RecyclerView) findViewById(R.id.ad_recycler_foods);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if( getIntent()!=null){
            categoryId= getIntent().getStringExtra("CategoryId");
            if(!categoryId.isEmpty()&& categoryId!=null){
                loadListFood(categoryId);
            }
        }
    }

    private void loadListFood(String categoryId){
        //like Select * from Foods where MenuId=
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolderAd>(
                Food.class,
                R.layout.food_item,
                FoodViewHolderAd.class,
                foodList.orderByChild("menuId").equalTo(categoryId)) {


            @Override
            protected void populateViewHolder(FoodViewHolderAd foodViewHolderAd, Food food, int i) {
                foodViewHolderAd.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolderAd.food_image);

                foodViewHolderAd.setItemOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MenuAdmin.this, "Click", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        //Set Adapte
        recyclerView.setAdapter(adapter);
    }
}