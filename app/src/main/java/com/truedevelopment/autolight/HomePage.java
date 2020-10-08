package com.truedevelopment.autolight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {

    Button userProfile, addDevice;
    String user_name,user_username,user_email,user_phone,user_password;

   private FirebaseRecyclerOptions<Product> options;
   private FirebaseRecyclerAdapter<Product, theViewholder> adapter;
   private RecyclerView recyclerView;
   DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        userProfile =  findViewById(R.id.user_profile);
        addDevice = findViewById(R.id.add_device);
        recyclerView= findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        user_name = intent.getStringExtra("name");
        user_username = intent.getStringExtra("username");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");
        ref = FirebaseDatabase.getInstance().getReference("Users").child(user_username).child("ProductsOwned");

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserIntent = new Intent(getApplicationContext(),UserProfile.class);

               UserIntent.putExtra("name",user_name);
               UserIntent.putExtra("username",user_username);
               UserIntent.putExtra("email",user_email);
               UserIntent.putExtra("phone",user_phone);
               UserIntent.putExtra("password",user_password);

                startActivity(UserIntent);
            }
        });

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deviceIntent = new Intent(getApplicationContext(),AddDevice.class);
                deviceIntent.putExtra("email",user_email);
                deviceIntent.putExtra("username",user_username);

                startActivity(deviceIntent);
            }
        });

         options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(ref,Product.class).build();
         adapter = new FirebaseRecyclerAdapter<Product, theViewholder>(options) {
             @Override
             protected void onBindViewHolder(@NonNull theViewholder holder, int position, @NonNull Product model) {

                 final String lastControlledBy = user_email;
                 final String name_username = user_username;

                 holder.devicename.setText(""+model.getnickname());
                 holder.deviceid.setText(""+model.getProductID());
                 final String productid = holder.deviceid.getText().toString();
                 final String productname = holder.devicename.getText().toString();
                 final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("devices").child(productid);
                 holder.On.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         ref.child("LED_STATUS").setValue("ON");
                         ref.child("lastControlledBy").setValue(lastControlledBy);
                     }
                 });
                 holder.Off.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         ref.child("LED_STATUS").setValue("OFF");
                         ref.child("lastControlledBy").setValue(lastControlledBy);
                     }
                 });

                 holder.popUpMenu.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                         popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                         popupMenu.show();
                         popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                             @Override
                             public boolean onMenuItemClick(MenuItem item) {
                                 switch (item.getItemId()){
                                     case R.id.menu_edit:
                                     {
                                         Intent editActivityIntent = new Intent(getApplicationContext(),edit_menu.class);

                                         editActivityIntent.putExtra("editname",productname);
                                         editActivityIntent.putExtra("productid",productid);
                                         editActivityIntent.putExtra("username",name_username);

                                         startActivity(editActivityIntent);
                                         break;
                                     }
                                 }
                                 return true;
                             }
                         });
                     }
                 });
             }

             @NonNull
             @Override
             public theViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                 View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
                 return new theViewholder(v);
             }
         };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}