package com.example.koreanrestaurantapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koreanrestaurantapp.Common.Common;
import com.example.koreanrestaurantapp.Database.Database;
import com.example.koreanrestaurantapp.ViewHolder.CartAdapter;
import com.example.koreanrestaurantapp.model.Order;
import com.example.koreanrestaurantapp.model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView txtTotal;
    Button btnOrderPlace;
    List<Order> cart= new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database= FirebaseDatabase.getInstance();
        request= database.getReference("Requests");
        recyclerView=(RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotal=(TextView) findViewById(R.id.txtTotal);
        btnOrderPlace=(Button) findViewById(R.id.btnPlaceOrder);
        btnOrderPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RequestOrder requestOrder= new RequestOrder(
//                        Common.currentUser.getPhone(),
//                        Common.currentUser.getName(),
//                        address,
//                        txtTotal.getText().toString(),
//                        cart
//                );
                showAlertAdress();
            }

        });

        loadListFood();

    }
    private void showAlertAdress() {
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Your address");
        alertDialog.setMessage("Enter your address");

        final EditText edtAdress= new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAdress .setLayoutParams(lp);
        alertDialog.setView(edtAdress);
        alertDialog.setIcon(R.drawable.ic_cart);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //create new request
                Request requestOrder= new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAdress.getText().toString(),
                        txtTotal.getText().toString(),
                        cart
                );

                //submit to firebase
                request.child(String.valueOf(System.currentTimeMillis())).setValue(requestOrder);
                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText( Cart.this,"Thanks for ordering", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter= new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        int total=0;
        for(Order order:cart){
            total+= (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en","US");
            NumberFormat nfm= NumberFormat.getCurrencyInstance(locale);

            txtTotal.setText(nfm.format(total));
        }
    }
}