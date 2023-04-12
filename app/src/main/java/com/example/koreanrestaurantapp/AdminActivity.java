package com.example.koreanrestaurantapp;

import static android.app.PendingIntent.getActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanrestaurantapp.Common.Common;
import com.example.koreanrestaurantapp.Interface.ItemClickListener;
import com.example.koreanrestaurantapp.ViewHolder.MenuViewHolder;
import com.example.koreanrestaurantapp.ViewHolder.MenuViewHolderAd;
import com.example.koreanrestaurantapp.databinding.ActivityAdminBinding;
import com.example.koreanrestaurantapp.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolderAd> adapter;

    //storage
    FirebaseStorage storage;
    StorageReference storageReference;
    EditText edtNameCategory;
    ImageView imagecategory;
    Button btnSelect, btnUpload;
//
    Uri saveUri;Category newCategory;
    private final int pickImageRequest=71;
//
//    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);

        //firebase
        database= FirebaseDatabase.getInstance();
        category= database.getReference("Category");
        storage= FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        FloatingActionButton fab= (FloatingActionButton)  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog();
                Intent add_category= new Intent(AdminActivity.this,AddCategory.class);
                startActivity(add_category);
            }
        });

        recycler_menu=(RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();
    }

   /* private void showDialog() {
        AlertDialog.Builder alertDialog=  new AlertDialog.Builder(AdminActivity.this);
        alertDialog.setTitle("Add new Category");

        LayoutInflater inflater= this.getLayoutInflater();
        View add_menu_layer=inflater.inflate(R.layout.add_new_category_admin,null);

        edtNameCategory=findViewById(R.id.edtNameCategory);
        btnSelect= findViewById(R.id.selectbtn);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btnUpload= findViewById(R.id.uploadbtn);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layer);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //firebase
                if( newCategory != null){
                    category.push().setValue(newCategory);
                    Toast.makeText(AdminActivity.this, "New category", Toast.LENGTH_SHORT).show();
                }
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

    private void uploadImage() {
        if (saveUri != null){
            ProgressDialog dialog= new ProgressDialog(this);
            dialog.setMessage("Uploading");
            dialog.show();

            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder= storageReference.child("images"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText( AdminActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newCategory = new Category(edtNameCategory.getText().toString(),uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(AdminActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress= (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded"+progress+"%");
                }
            });
        }
    }

    private void selectImage() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,pickImageRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==pickImageRequest && requestCode==RESULT_OK && data!=null && data.getData()!=null){
            saveUri= data.getData();
            btnSelect.setText("Image Selected");
        }
    }
*/
    private void loadMenu() {

        adapter= new FirebaseRecyclerAdapter<Category, MenuViewHolderAd>(Category.class,R.layout.menu_item,MenuViewHolderAd.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolderAd menuViewHolderAd, Category category, int i) {
                menuViewHolderAd.txtMenuName.setText((category.getName()));
                Picasso.with(getBaseContext()).load(category.getImage()).into(menuViewHolderAd.imageView);
                menuViewHolderAd.setItemOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get Category and send to new Activity
                        Intent foodListAd= new Intent(AdminActivity.this,MenuAdmin.class);
                        foodListAd.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodListAd);
                    }
                });
            }

        };
        recycler_menu.setAdapter(adapter);
    }

    //Update - delete category
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        category.child(key).removeValue();
        Toast.makeText(this, "Delete this category", Toast.LENGTH_SHORT).show();

    }

    private void showUpdateDialog(String key,Category item) {
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(AdminActivity.this);
        alertDialog.setTitle("Update category");

        LayoutInflater inflater= this.getLayoutInflater();
        View update_cate= inflater.inflate(R.layout.activity_add_category,null);
        edtNameCategory=update_cate.findViewById(R.id.edtNameCategory1);
        btnSelect= update_cate.findViewById(R.id.selectbtn1);
        btnUpload= update_cate.findViewById(R.id.uploadbtn1);

        edtNameCategory.setText(item.getName());
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
            }
        });

        alertDialog.setView(update_cate);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setName(edtNameCategory.getText().toString());
                category.child(key).setValue(item);
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

    private void changeImage(Category item) {
        if (saveUri != null){
            ProgressDialog dialog= new ProgressDialog(this);
            dialog.setMessage("Uploading");
            dialog.show();

            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder= storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText( AdminActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            newCategory = new Category(edtNameCategory.getText().toString(),uri.toString());
                            item.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(AdminActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress= (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded"+progress+"%");
                }
            });
        }
    }
    private void selectImage() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && data!=null && data.getData()!=null){
            saveUri= data.getData();
            //imagecategory.setImageURI(saveUri);
            btnSelect.setText("Image Selected");
        }
    }
}