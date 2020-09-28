package com.truedevelopment.autolight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button callSignUp,login;
    TextView mWelcome, mSignIn;
    TextInputLayout mUserId, mPassword;
    ImageView logoImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.btnSignUp);
        login = findViewById(R.id.btnLogin);
        mWelcome = findViewById(R.id.welcomeText);
        mSignIn = findViewById(R.id.signInText);
        mUserId = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        logoImage = findViewById(R.id.logoImage);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,SignUp.class);

                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(logoImage,"logo_image");
                pairs[1] = new Pair<View,String>(callSignUp,"trans_new_user");
                pairs[2] = new Pair<View,String>(login,"trans_login");
                pairs[3] = new Pair<View,String>(mWelcome,"trans_welcome");
                pairs[4] = new Pair<View,String>(mSignIn,"trans_signinText");
                pairs[5] = new Pair<View,String>(mUserId,"trans_email");
                pairs[6] = new Pair<View,String>(mPassword,"trans_password");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);

                startActivity(intent,options.toBundle());



            }
        });

    }

    private Boolean validateUserName(){
        String val = mUserId.getEditText().getText().toString();
        String noWhiteSpace =  "(?=\\S+$)";    // 8-20 characters long

        if(val.isEmpty()){
            mUserId.setError("Field Cannot be Empty");
            return false;
        }
        else
        {
            mUserId.setError(null);
            mUserId.setErrorEnabled(false);
            return true;

        }
    }

    private Boolean validatePassword(){
        String val = mPassword.getEditText().getText().toString();


        if(val.isEmpty()){
            mPassword.setError("Field Cannot be Empty");
            return false;
        }
        else
        {
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;

        }
    }

    public void loginUser(View view){

        if(!validateUserName() | !validatePassword()){
            return;
        }

        else{
            isUser();
        }

    }

    private void isUser() {

        final String userEnteredUsername = mUserId.getEditText().getText().toString().trim();
        final String userEnteredPassword = mPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    mUserId.setError(null);
                    mUserId.setErrorEnabled(false);


                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if(passwordFromDB.equals(userEnteredPassword)){

                        mPassword.setError(null);
                        mPassword.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(userEnteredUsername).child("phone").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),HomePage.class);

                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("username",usernameFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("phone",phoneFromDB);
                        intent.putExtra("password",passwordFromDB);


                        //Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    }
                    else{
                        mPassword.setError("Wrong Password!");
                        mPassword.requestFocus();
                    }
                }else {
                    mUserId.setError("No Such User Exists!");
                    mUserId.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}