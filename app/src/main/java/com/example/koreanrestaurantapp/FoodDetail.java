package com.example.koreanrestaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koreanrestaurantapp.Database.Database;
import com.example.koreanrestaurantapp.model.Food;
import com.example.koreanrestaurantapp.model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_description, food_increase_number;
    ImageView food_image,plusBtn,minusBtn;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;

    String foodId="";
    FirebaseDatabase database;
    DatabaseReference food;

    Food currentFood;
    int numberOrder=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database= FirebaseDatabase.getInstance();
        food=database.getReference("Food");

        food_increase_number= (TextView) findViewById(R.id.food_increase_number);
        food_increase_number.setText(String.valueOf(numberOrder));
        plusBtn=(ImageView) findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrder+=1;
                food_increase_number.setText(String.valueOf(numberOrder));
            }
        });
        minusBtn= findViewById(R.id.minusBtn);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrder-=1;
                food_increase_number.setText(String.valueOf(numberOrder));
            }
        });

        btnCart=(FloatingActionButton) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        String.valueOf(numberOrder),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetail.this,"Added to cast", Toast.LENGTH_SHORT).show();
            }
        });

        food_description= (TextView) findViewById(R.id.food_description);
        food_name=  (TextView) findViewById(R.id.food_name);
        food_price=  (TextView) findViewById(R.id.food_price);
        food_image=  (ImageView) findViewById(R.id.food_image);

        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapseActionView);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() !=null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()){
            getDetailFood(foodId);
        }


    }

    private void getDetailFood(String foodId) {
        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentFood = snapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}