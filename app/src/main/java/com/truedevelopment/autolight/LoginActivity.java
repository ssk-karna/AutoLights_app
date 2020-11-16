package com.truedevelopment.autolight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button callSignUp,login,resend,forgot;
    TextView mWelcome, mSignIn;
    TextInputLayout mUserId, mPassword;
    ImageView logoImage;
    FirebaseAuth mAuth;
    FirebaseUser muser;

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
        resend = findViewById(R.id.btnResend);
        forgot = findViewById(R.id.btnForgot);
        mAuth = FirebaseAuth.getInstance();
        resend.setVisibility(View.GONE);
        keepLogin();
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
            login();

        }

    }

    public void getUserDetails(final String uid) {

        final String userEnteredUsername = mUserId.getEditText().getText().toString().trim();
        final String userEnteredPassword = mPassword.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        Log.d("TAG","get USer function" + uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String passwordFromDB = snapshot.child(uid).child("password").getValue(String.class);

                        SharedPreferences sp = getSharedPreferences("MyUserPrefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        String nameFromDB = snapshot.child(uid).child("name").getValue(String.class);
                        String usernameFromDB = snapshot.child(uid).child("username").getValue(String.class);
                        Log.d("TAG", "username is in login + "+usernameFromDB);
                        String emailFromDB = snapshot.child(uid).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(uid).child("phone").getValue(String.class);

                        editor.putString("name",nameFromDB);
                        editor.putString("username",usernameFromDB);
                        editor.putString("email",emailFromDB);
                        editor.putString("phone",phoneFromDB);
                        editor.putString("password",passwordFromDB);
                        editor.putString("uid",uid);
                        editor.commit();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void login(){
         String userEnteredUsername = mUserId.getEditText().getText().toString().trim();
         String userEnteredPassword = mPassword.getEditText().getText().toString().trim();


        mAuth.signInWithEmailAndPassword(userEnteredUsername,userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent newIntent = new Intent(LoginActivity.this, HomePage.class);
                   final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                   if (!user.isEmailVerified()){
                       resend.setVisibility(View.VISIBLE);
                       resend.setVisibility(View.VISIBLE);
                       Toast.makeText(LoginActivity.this,"Email is not Verified. Click resend to get verification link",Toast.LENGTH_LONG).show();
                       resend.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       Toast.makeText(LoginActivity.this,"Verification Email has been Sent",Toast.LENGTH_LONG).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(LoginActivity.this," Failed to send Verification Email "+e.getMessage(),Toast.LENGTH_LONG).show();
                                   }
                               });
                           }
                       });
                   }else {
                       getUserDetails(user.getUid());
                       Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                       Log.d("TAG", "uid in ifSuccessful + " + user.getUid());
                       startActivity(newIntent);
                   }
                }else{
                    Toast.makeText(LoginActivity.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void keepLogin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Intent newIntent = new Intent(LoginActivity.this, HomePage.class);
            startActivity(newIntent);
        }
        else{
            mUserId.requestFocus();
        }
    }
}