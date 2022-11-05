package com.example.harvest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    Animation fade, fade2;
    ImageView apple, pumpkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent iHome = new Intent(SplashActivity.this, MainActivity.class);

        fade = AnimationUtils.loadAnimation(this, R.anim.image_animation);
        fade2 = AnimationUtils.loadAnimation(this, R.anim.image_animation2);

        apple = findViewById(R.id.imageApple);
        pumpkin = findViewById((R.id.imagePumpkin));

        apple.setAnimation(fade);
        pumpkin.setAnimation(fade2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(iHome);
            }
        }, 4500);
    }
}