package com.truedevelopment.autolight;

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
}