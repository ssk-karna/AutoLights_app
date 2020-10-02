package com.truedevelopment.autolight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {

    Button userProfile, addDevice;
    String user_name,user_username,user_email,user_phone,user_password;
    RecyclerView recview;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        userProfile =  findViewById(R.id.user_profile);
        addDevice = findViewById(R.id.add_device);
        recview = findViewById(R.id.recview);


        Intent intent = getIntent();
        user_name = intent.getStringExtra("name");
        user_username = intent.getStringExtra("username");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");

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

        recview.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(user_username).child("ProductsOwned"),Product.class)
                .build();

        adapter = new MyAdapter(options);
        recview.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}