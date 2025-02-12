package com.example.colors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userData",null);

        if (userJson != null){
            startActivity(new Intent(MainActivity.this,HomeActivity2.class));
        }

        ImageView imageView = findViewById(R.id.imageView1);
        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        TextView textView1 = findViewById(R.id.textView);

        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        Animation slideUp = AnimationUtils.loadAnimation(this,R.anim.slide_up);

        imageView.startAnimation(fadeIn);


        button2.startAnimation(slideUp);
        button1.startAnimation(slideUp);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });




    }
}