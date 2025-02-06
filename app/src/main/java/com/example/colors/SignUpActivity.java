package com.example.colors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        Animation slideUp = AnimationUtils.loadAnimation(this,R.anim.slide_up);

        ImageView imageView = findViewById(R.id.imageView3);
        Button button1 = findViewById(R.id.button6);
        Button signup_button = findViewById(R.id.signup_button);
        TextView textView1 = findViewById(R.id.textView8);
        TextView textView2 = findViewById(R.id.textView9);
        ImageButton imageButton = findViewById(R.id.imageButton7);
        ImageButton imageButton2 = findViewById(R.id.imageButton8);
        ImageButton imageButton3 = findViewById(R.id.imageButton9);
        EditText editText1 = findViewById(R.id.edit_text1);
        EditText editText2 = findViewById(R.id.edit_text2);
        EditText editText3 = findViewById(R.id.edit_text3);
        EditText editText4 = findViewById(R.id.edit_text14);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,HomeActivity2.class));
            }
        });



        imageView.startAnimation(fadeIn);
        button1.startAnimation(slideUp);
        signup_button.startAnimation(slideUp);
        textView1.startAnimation(slideUp);
        textView2.startAnimation(slideUp);
        imageButton.startAnimation(slideUp);
        imageButton2.startAnimation(slideUp);
        imageButton3.startAnimation(slideUp);
    }
}