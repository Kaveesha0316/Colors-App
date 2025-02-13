package com.example.colors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import DTO.ResponseDTO;
import DTO.User_DTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();

                        if (editText1.getText().toString().isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warningmsg(findViewById(R.id.filledTextField),findViewById(R.id.edit_text1),"Please enter your Email");

                                }
                            });
                        }else if (editText2.getText().toString().isEmpty()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warningmsg(findViewById(R.id.filledTextField2),findViewById(R.id.edit_text2),"Please enter your Password");

                                }
                            });

                        }else {
                            JsonObject user = new JsonObject();
                            user.addProperty("email", editText1.getText().toString());
                            user.addProperty("password", editText2.getText().toString());


                            OkHttpClient okHttpClient = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));

                            Request request = new Request.Builder()
                                    .url("http://192.168.1.2:8080/colors/user/signin")
                                    .post(requestBody)
                                    .build();

                            try {

                                Response response = okHttpClient.newCall(request).execute();
                                String responsetext = response.body().string();

                                ResponseDTO<User_DTO> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<User_DTO>>(){}.getType());

                                Log.i("colors-log", responsetext);


                                if (responseDTO.isSuccess()) {

                                    User_DTO userDTO = responseDTO.getContent();

                                    Log.i("colors-log", "User Name: " + userDTO.getName());
                                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String userjson = gson.toJson(userDTO);
                                    editor.putString("userData",userjson);
                                    editor.apply();

                                    startActivity(new Intent(SignInActivity.this,HomeActivity2.class));
                                } else {
                                    if ("Invalid username or password or blocked".equals(responseDTO.getMessage())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                warningmsg(findViewById(R.id.filledTextField), findViewById(R.id.edit_text1), "Invalid email address or blocked");
                                               warningmsg(findViewById(R.id.filledTextField2), findViewById(R.id.edit_text2), "Invalid password or blocked");
                                            }
                                        });
                                    }
                                }


                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }).start();



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

    public  void warningmsg(TextInputLayout textInputLayout , TextInputEditText editText , String msg ){

        editText.startAnimation(AnimationUtils.loadAnimation(SignInActivity.this,R.anim.shake));
        textInputLayout.setError(msg);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Clear error when the EditText gains focus
                    textInputLayout.setError(null);
                }
            }
        });



    }
}
