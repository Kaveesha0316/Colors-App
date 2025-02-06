package com.example.colors;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView2 = findViewById(R.id.imageView2);
        Button button1 =  findViewById(R.id.signin_button);
        Button button2 =  findViewById(R.id.button4);
        ImageButton imageButton1 = findViewById(R.id.imageButton3);
        ImageButton imageButton2 = findViewById(R.id.imageButton4);
        ImageButton imageButton3 = findViewById(R.id.imageButton5);
        TextView textView1 = findViewById(R.id.textView5);
        TextView textView2 = findViewById(R.id.textView6);
        EditText editText1 = findViewById(R.id.edit_text1);
        EditText editText2 = findViewById(R.id.edit_text2);


        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        Animation slideUp = AnimationUtils.loadAnimation(this,R.anim.slide_up);

        imageView2.startAnimation(fadeIn);
        button1.startAnimation(slideUp);
        imageButton1.startAnimation(slideUp);
        imageButton2.startAnimation(slideUp);
        imageButton3.startAnimation(slideUp);
        button2.startAnimation(slideUp);
        textView1.startAnimation(slideUp);
        textView2.startAnimation(slideUp);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,HomeActivity2.class));


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });





    }
}