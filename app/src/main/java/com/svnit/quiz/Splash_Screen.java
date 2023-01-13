package com.svnit.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    ImageView image;
    TextView text;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        image = findViewById(R.id.imageViewSplash);
        text = findViewById(R.id.textViewSplash);
        layout = findViewById(R.id.lay);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash);
        text.setAnimation(animation);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash_Screen.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        },5000);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Splash_Screen.this,LoginActivity.class);
                startActivity(i);
                finish();
                handler.removeCallbacksAndMessages(null);
            }
        });


    }
}