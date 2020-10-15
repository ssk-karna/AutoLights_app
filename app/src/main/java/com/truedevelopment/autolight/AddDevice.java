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
    TextInputLayout productID,productKey,nickname;
    TextView usertv,iduserEntered,idDb;

    DatabaseReference reference_user,reference_devices;
    int maxProductCount = 0;

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
        nickname = findViewById(R.id.nickname);
        usertv = findViewById(R.id.tvusername);


        reference_user = FirebaseDatabase.getInstance().getReference().child("Users");
        reference_devices = FirebaseDatabase.getInstance().getReference().child("devices");

        usertv.setText(user);

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingDevice(v);
                Intent homeIntent = new Intent(AddDevice.this, HomePage.class);
                startActivity(homeIntent);
            }
        });


    }

    public void addingDevice(View v) {

        if(!validateProductID() | !validateProductKey() | !validateNickname()){
            return;
        }
        else {

            checkingDevice();
        }
    }

    private Boolean validateProductID(){
        String val = productID.getEditText().getText().toString();

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
    private Boolean validateNickname(){
        String val = nickname.getEditText().getText().toString();

        if(val.isEmpty()){
            nickname.setError("Field Cannot be Empty");
            return false;
        }
        else
        {
            nickname.setError(null);
            nickname.setErrorEnabled(false);
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
        final String userEnteredNickname = nickname.getEditText().getText().toString().trim();

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

                        reference_user = FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("ProductsOwned");
                        reference_devices = FirebaseDatabase.getInstance().getReference().child("devices").child(userEnteredProductID).child("users");
                        final String username = user;

                        reference_devices.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){
                                    maxProductCount = (int) snapshot.getChildrenCount();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        reference_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.hasChild(userEnteredProductID)){
                                    reference_user.child(userEnteredProductID).child("ProductID").setValue(userEnteredProductID);
                                    reference_user.child(userEnteredProductID).child("nickname").setValue(userEnteredNickname);
                                    Toast.makeText(AddDevice.this, "Device added", Toast.LENGTH_SHORT).show();
                                    reference_devices.child(username).child("email").setValue(email);
                                }
                                else{
                                    Toast.makeText(AddDevice.this, "Device Already added", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


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

}