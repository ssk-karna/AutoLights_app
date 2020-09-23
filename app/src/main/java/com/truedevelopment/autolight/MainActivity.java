package com.truedevelopment.autolight;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private  static final int SPLASH_SCREEN = 3000;

    Animation topAnim, bottomAnim;
    ImageView splashLogo;
    TextView splashName, poweredBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        splashLogo = findViewById(R.id.logo_splash);
        splashName = findViewById(R.id.name_splash);
        poweredBy = findViewById(R.id.TechSA);

        splashName.setAnimation(topAnim);
        splashLogo.setAnimation(bottomAnim);
        poweredBy.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  = new Intent(MainActivity.this, LoginActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(splashLogo,"logo_image");

                ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);




    }
}