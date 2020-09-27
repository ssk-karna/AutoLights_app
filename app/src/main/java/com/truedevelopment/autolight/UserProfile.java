package com.truedevelopment.autolight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {

    TextInputLayout email, fulllName, phone,password;
    TextView fullNameLabel, userNameLabel;
    String user_name,user_username,user_email,user_phone,user_password;
    Button update;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fulllName = findViewById(R.id.userFullName);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.userPhone);
        password = findViewById(R.id.userPassword);
        fullNameLabel = findViewById(R.id.textFullName);
        userNameLabel = findViewById(R.id.textUsername);
        update = findViewById(R.id.btnUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(v);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        showAllUserData();


    }

    private void showAllUserData() {
        Intent intent = getIntent();
         user_name = intent.getStringExtra("name");
         user_username = intent.getStringExtra("username");
         user_email = intent.getStringExtra("email");
         user_phone = intent.getStringExtra("phone");
         user_password = intent.getStringExtra("password");

      fullNameLabel.setText(user_name);
      userNameLabel.setText(user_username);
      fulllName.getEditText().setText(user_name);
      email.getEditText().setText(user_email);
      phone.getEditText().setText(user_phone);
      password.getEditText().setText(user_password);

    }

    public void update(View view){

        if(isNameChanged() || isPasswordChanged()){
            Toast.makeText(this, "Data has been Updated", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Data is same and cannot be Updated", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isPasswordChanged() {

        if(!user_password.equals( password.getEditText().getText().toString())) {
            user_password = password.getEditText().getText().toString();
            reference.child(user_username).child("password").setValue(password.getEditText().getText().toString());
            return true;
        }
            else{
                return false;
            }

    }


    private boolean isNameChanged() {

        if(!user_name.equals( fulllName.getEditText().getText().toString())) {
            user_name = fulllName.getEditText().getText().toString();
            reference.child(user_username).child("name").setValue(fulllName.getEditText().getText().toString());
            return true;
        }
            else{
                return false;
            }

    }
}