package com.truedevelopment.autolight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfile extends AppCompatActivity {

    TextInputLayout email, fulllName, phone,password;
    TextView fullNameLabel, userNameLabel;

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

        showAllUserData();
    }

    private void showAllUserData() {
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("name");
        String user_username = intent.getStringExtra("username");
        String user_email = intent.getStringExtra("email");
        String user_phone = intent.getStringExtra("phone");
        String user_password = intent.getStringExtra("password");

      fullNameLabel.setText(user_name);
      userNameLabel.setText(user_username);
      fulllName.getEditText().setText(user_name);
      email.getEditText().setText(user_email);
      phone.getEditText().setText(user_phone);
      password.getEditText().setText(user_password);
    }
}