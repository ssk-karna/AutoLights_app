package com.truedevelopment.autolight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_menu extends AppCompatActivity {
String productid,devicename,username;
EditText updateName;
ImageView confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        updateName = findViewById(R.id.update_name);
        confirmButton = findViewById(R.id.confirm_edit);

        Intent editActivityIntent = getIntent();
        productid = editActivityIntent.getStringExtra("productid");
        devicename = editActivityIntent.getStringExtra("productname");
        username =  editActivityIntent.getStringExtra("username");

        updateName.setText(devicename);
        updateName.requestFocus();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser User= FirebaseAuth.getInstance().getCurrentUser();
                String updatedName = updateName.getText().toString();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUid()).child("ProductsOwned").child(productid);
                dbRef.child("nickname").setValue(updatedName);
                
            }
        });
    }
}