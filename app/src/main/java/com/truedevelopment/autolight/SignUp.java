package com.truedevelopment.autolight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

  FirebaseDatabase rootNode;
  DatabaseReference reference;

  TextInputLayout regName, regMail,regPhone, regPassword;
  Button register,backToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //FirebaseApp.initializeApp(this);

        regName = findViewById(R.id.name);
        regMail = findViewById(R.id.email);
        regPassword = findViewById(R.id.passwordSignUp);
        register = findViewById(R.id.btnRegister);
        regPhone = findViewById(R.id.phone);
        backToLogin = findViewById(R.id.btnLoginPage);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
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
            return true;

        }
    }

    public void registerUser(View view){

        rootNode = FirebaseDatabase.getInstance();
        reference  = rootNode.getReference().child("Users");

        if(!validateName() | !validateEmail() | !validatePhone() | !validatePassword()){
            return;
        }

        String name = regName.getEditText().getText().toString();
        String email = regMail.getEditText().getText().toString();
        String phone = regPhone.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        UserHelperClass helperClass = new UserHelperClass(name,email,phone,password);

        reference.child(phone).setValue(helperClass);

    }
}