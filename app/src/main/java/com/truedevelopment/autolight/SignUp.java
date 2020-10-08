package com.truedevelopment.autolight;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

  FirebaseDatabase rootNode;
  DatabaseReference reference;

  TextInputLayout regName, regMail,regPhone, regPassword,regUserName;
  Button register,backToLogin;
  ImageView logoImg;
  TextView mWelcomeText, mSignInText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //FirebaseApp.initializeApp(this);

        regName = findViewById(R.id.name);
        regUserName = findViewById(R.id.userName);
        regMail = findViewById(R.id.tvusername);
        regPassword = findViewById(R.id.passwordSignUp);
        register = findViewById(R.id.btnRegister);
        regPhone = findViewById(R.id.phone);
        backToLogin = findViewById(R.id.btnLoginPage);
        mSignInText = findViewById(R.id.textSignInText);
        mWelcomeText = findViewById(R.id.textWelcomeText);
        logoImg = findViewById(R.id.logoImg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this,LoginActivity.class);

                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(logoImg,"logo_image");
                pairs[1] = new Pair<View,String>(backToLogin,"trans_new_user");
                pairs[2] = new Pair<View,String>(register,"trans_login");
                pairs[3] = new Pair<View,String>(mWelcomeText,"trans_welcome");
                pairs[4] = new Pair<View,String>(mSignInText,"trans_signinText");
                pairs[5] = new Pair<View,String>(regMail,"trans_email");
                pairs[6] = new Pair<View,String>(regPassword,"trans_password");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);

                startActivity(intent,options.toBundle());


            }
        });


    }

    private Boolean validateName(){
        String val = regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Field Cannot be Empty");
            return false;
        }

        else
        {
            regName.setError(null);
            return true;

        }
    }

    private Boolean validateEmail(){
        String val = regMail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";

        if(val.isEmpty()){
            regMail.setError("Field Cannot be Empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            regMail.setError("Invalid Email");
            return false;
        }
        else
        {
            regMail.setError(null);
            regMail.setErrorEnabled(false);
            return true;

        }
    }

    private Boolean validatePhone(){
        String val = regPhone.getEditText().getText().toString();

        if(val.isEmpty()){
            regPhone.setError("Field Cannot be Empty");
            return false;
        }
        else
        {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;

        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();
        String passwordPattern = "^"            //Starts with String
                +"(?=.*[0-9])"                  //at least one digit
                + "(?=.*[a-z])(?=.*[A-Z])"      //at least one small and one capital alphabet
                + "(?=.*[@#$%^&+=])"            // at least one special character
                + "(?=\\S+$).{8,20}$";          // 8-20 characters long

        if(val.isEmpty()){
            regPassword.setError("Field Cannot be Empty");
            return false;
        }
        else if(!val.matches(passwordPattern)){
            regPassword.setError("Password too weak");
            return false;
        }
        else
        {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;

        }
    }

    private Boolean validateUserName(){
        String val = regUserName.getEditText().getText().toString();
        String noWhiteSpace =  "(?=\\S+$)";    // 8-20 characters long

        if(val.isEmpty()){
            regUserName.setError("Field Cannot be Empty");
            return false;
        }
        else if(val.matches(noWhiteSpace)){
            regUserName.setError("No white Spaces allowed!");
            return false;
        }
        else if(val.length()>=15){
            regUserName.setError("Too Long. Less than 15 Characters allowed!");
            return false;
        }
        else
        {
            regUserName.setError(null);
            regUserName.setErrorEnabled(false);
            return true;

        }
    }

    public void registerUser(View view){

        rootNode = FirebaseDatabase.getInstance();
        reference  = rootNode.getReference().child("Users");

        if(!validateName() | !validateEmail() | !validatePhone() | !validatePassword() | !validateUserName()){
            return;
        }

        String name = regName.getEditText().getText().toString();
        String email = regMail.getEditText().getText().toString();
        String phone = regPhone.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();
        String username = regUserName.getEditText().getText().toString();

        UserHelperClass helperClass = new UserHelperClass(name,username,email,phone,password);

        reference.child(username).setValue(helperClass);
        reference.child(username).child("ProductsOwned").setValue("0");

        Intent logInIntent = new Intent(SignUp.this,LoginActivity.class);
        startActivity(logInIntent);

    }


}