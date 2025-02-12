package com.example.colors;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import DTO.ResponseDTO;
import model.Validation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();

                        if (editText1.getText().toString().isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warningmsg(findViewById(R.id.filledTextField),findViewById(R.id.edit_text1),"Please enter your name");

                                }
                            });
                        }else if (editText2.getText().toString().isEmpty()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warningmsg(findViewById(R.id.filledTextField2),findViewById(R.id.edit_text2),"Please enter your Email");

                                }
                            });

                        }else if (editText3.getText().toString().isEmpty()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warningmsg(findViewById(R.id.filledTextField3),findViewById(R.id.edit_text3),"Please enter your password");

                                }
                            });

                        }else if (editText4.getText().toString().isEmpty()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warningmsg(findViewById(R.id.filledTextField4),findViewById(R.id.edit_text14),"Please enter your mobile");

                                }
                            });
                        }else {


                            JsonObject user = new JsonObject();
                            user.addProperty("name", editText1.getText().toString());
                            user.addProperty("email", editText2.getText().toString());
                            user.addProperty("password", editText3.getText().toString());
                            user.addProperty("mobile", editText4.getText().toString());

                            OkHttpClient okHttpClient = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));

                            Request request = new Request.Builder()
                                    .url("http://192.168.1.2:8080/colors/user/signup")
                                    .post(requestBody)
                                    .build();

                            try {

                                Response response = okHttpClient.newCall(request).execute();
                                String responsetext = response.body().string();

                                ResponseDTO<String> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>(){}.getType());

                                Log.i("colors-log", String.valueOf(responsetext));

                                if (responseDTO.isSuccess()) {
                                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                } else {
                                    if ("Invalid email address".equals(responseDTO.getContent())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                warningmsg(findViewById(R.id.filledTextField2), findViewById(R.id.edit_text2), "Invalid email address");
                                            }
                                        });
                                    } else if ("Invalid password".equals(responseDTO.getContent())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                warningmsg(findViewById(R.id.filledTextField3), findViewById(R.id.edit_text3), "Invalid password");
                                            }
                                        });
                                    } else if ("Invalid mobile number".equals(responseDTO.getContent())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                warningmsg(findViewById(R.id.filledTextField4), findViewById(R.id.edit_text14), "Invalid mobile number");
                                            }
                                        });
                                    }else if ("Email already in use".equals(responseDTO.getContent())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                warningmsg(findViewById(R.id.filledTextField2), findViewById(R.id.edit_text2), "Email already in use");
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



        imageView.startAnimation(fadeIn);
        button1.startAnimation(slideUp);
        signup_button.startAnimation(slideUp);
        textView1.startAnimation(slideUp);
        textView2.startAnimation(slideUp);
        imageButton.startAnimation(slideUp);
        imageButton2.startAnimation(slideUp);
        imageButton3.startAnimation(slideUp);
    }
    public  void warningmsg(TextInputLayout textInputLayout , TextInputEditText editText ,String msg ){

        editText.startAnimation(AnimationUtils.loadAnimation(SignUpActivity.this,R.anim.shake));
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