package com.truedevelopment.autolight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddDevice extends AppCompatActivity {

    String email,user;
    Button addDevice;
    TextInputLayout productID,productKey;
    TextView usertv;
    DatabaseReference reference;
    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        Intent deviceIntent = getIntent();

        email = deviceIntent.getStringExtra("email");
        user = deviceIntent.getStringExtra("username");
        addDevice = findViewById(R.id.btnAdd);
        productID = findViewById(R.id.productId);
        productKey = findViewById(R.id.productKey);
        usertv = findViewById(R.id.tvusername);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        ref = FirebaseDatabase.getInstance().getReference().child("Products");

        usertv.setText(user);

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingDevice(v);
            }
        });

    }

    private void addingDevice(View view) {

       validateDevice();
    }




    private void validateDevice(){
       final String product_ID = productID.getEditText().getText().toString();
       final String product_key = productKey.getEditText().getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");

        Query checkDevice = ref.orderByChild("productKey").equalTo(product_ID);

        checkDevice.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    productID.setError(null);
                    productID.setErrorEnabled(false);

                    String productKeyFromDb = snapshot.child(product_ID).child("productKey").getValue(String.class);

                    if(productKeyFromDb.equals(product_key)){
                        productKey.setError(null);
                        productKey.setErrorEnabled(false);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                        reference.child(user).child("ProductsOwned").setValue(product_ID);
                    }
                    else{
                        productKey.setError("Wrong Key Entered! Please Check");
                        productKey.requestFocus();
                    }

                }
                else{
                    productID.setError("No such product exists");
                    productID.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}