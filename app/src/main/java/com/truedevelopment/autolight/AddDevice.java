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
    TextView usertv,iduserEntered,idDb;




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
        iduserEntered = findViewById(R.id.tvuserenteredID);
        idDb = findViewById(R.id.tvDbid);


        usertv.setText(user);

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingDevice(v);
            }
        });


    }

    public void addingDevice(View v) {

        if(!validateProductID() | !validateProductKey()){
            return;
        }
        else {

            checkingDevice();
        }
    }

    private Boolean validateProductID(){
        String val = productID.getEditText().getText().toString();
            // 8-20 characters long

        if(val.isEmpty()){
            productID.setError("Field Cannot be Empty");
            return false;
        }
        else
        {
            productID.setError(null);
            productID.setErrorEnabled(false);
            return true;

        }
    }

    private Boolean validateProductKey(){
        String val = productKey.getEditText().getText().toString();


        if(val.isEmpty()){
            productKey.setError("Field Cannot be Empty");
            return false;
        }
        else
        {
            productKey.setError(null);
            productKey.setErrorEnabled(false);
            return true;

        }
    }

    private void checkingDevice() {

        final String userEnteredProductID = productID.getEditText().getText().toString().trim();
        final String userEnteredProductKey = productKey.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("devices");

        Query checkUser = reference.orderByChild("productID").equalTo(userEnteredProductID);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    productID.setError(null);
                    productID.setErrorEnabled(false);


                    String productKeyFromDB = snapshot.child(userEnteredProductID).child("productKey").getValue(String.class);

                    if(productKeyFromDB.equals(userEnteredProductKey)){

                        productKey.setError(null);
                        productKey.setErrorEnabled(false);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                        reference.child(user).child("ProductsOwned").setValue(userEnteredProductID);


                    }
                    else{
                        productKey.setError("Wrong Key Entered!");
                        productKey.requestFocus();
                    }
                }else {
                    productID.setError("No Such Product Exists!");
                    productID.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    private void validateDevice(){
//       final String product_ID = productID.getEditText().getText().toString().trim();
//       final String product_key = productKey.getEditText().getText().toString().trim();
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");
//
//
//        Query checkDevice = ref.orderByChild("productKey").equalTo(product_ID);
//
//        checkDevice.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//
//                    productID.setError(null);
//                    productID.setErrorEnabled(false);
//
//                    String productKeyFromDb = snapshot.child(product_ID).child("productKey").getValue(String.class);
//
//                    if(productKeyFromDb.equals(product_key)){
//                        productKey.setError(null);
//                        productKey.setErrorEnabled(false);
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
//                        reference.child(user).child("ProductsOwned").setValue(product_ID);
//                    }
//                    else{
//                        productKey.setError("Wrong Key Entered! Please Check");
//                        productKey.requestFocus();
//                    }
//
//                }
//                else{
//                    productID.setError("No such product exists");
//                    productID.requestFocus();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
//    }
//
}