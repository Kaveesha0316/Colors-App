package com.example.colors.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.colors.BuildConfig;
import com.example.colors.ContactActivity;
import com.example.colors.MainActivity;
import com.example.colors.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;

import DTO.ResponseDTO;
import DTO.User_DTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class profileFragment extends Fragment {
      User_DTO user;
    private Uri UserImageUri;

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;

      Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();


//        loadProfileImage();
        TextView textView6 = view.findViewById(R.id.textView16);
         editText1 = view.findViewById(R.id.edit_user_name);
         editText2 = view.findViewById(R.id.edit_user_password);
         editText3 = view.findViewById(R.id.edit_user_mobile);
         editText4 = view.findViewById(R.id.edit_user_address);
         editText5 = view.findViewById(R.id.edit_user_city);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
        String userjson = sharedPreferences.getString("userData",null);

        if (userjson != null){
            Gson gson = new Gson();

            user = gson.fromJson(userjson, User_DTO.class);

            ImageView imageView = view.findViewById(R.id.profile_picture);
            String imageUrl = user.getImage_path();


            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.loading) // Optional: Placeholder image
                    .error(R.drawable.user) // Optional: Error image
                    .into(imageView);

            textView6.setText(user.getEmail());
            editText1.setText(user.getName());
            editText2.setText(user.getPassword());
            editText3.setText(user.getMobile());
            editText4.setText(user.getAddress());
            editText5.setText(user.getCity());

        }


        ImageView imageView = view.findViewById(R.id.imageView9);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ContactActivity.class));
            }
        });

        /////////profile image/////////

        ImageView UserProfilImageView = view.findViewById(R.id.profile_picture);

        ActivityResultLauncher<String> nicFrontImagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        UserProfilImageView.setImageURI(uri);
                        UserImageUri = uri;
                    }
                }
        );

        UserProfilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nicFrontImagePickerLauncher.launch("image/*");
            }
        });

        Button logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE).edit();
                editor.clear(); // Clear all stored preferences
                editor.apply();

                // Redirect to login activity
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });




        Button button = view.findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Integer id = user.getId();
                if(editText1.getText().toString().isEmpty()){
                    showErrorDialog(getContext(), "Please enter your name.");
                }else if(editText2.getText().toString().isEmpty()){
                    showErrorDialog(getContext(), "Please enter your password.");
                }else if(editText3.getText().toString().isEmpty()){
                    showErrorDialog(getContext(), "Please enter your mobile.");
                }else if(editText4.getText().toString().isEmpty()){
                    showErrorDialog(getContext(), "Please enter your address.");
                }else if(editText5.getText().toString().isEmpty()){
                    showErrorDialog(getContext(), "Please enter your city.");
                }else if(UserImageUri == null){
                    showErrorDialog(getContext(), "Please select image.");
                }else {

                    uploadImageToFirebase(UserImageUri);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Gson gson = new Gson();
//                            JsonObject user = new JsonObject();
//                            user.addProperty("id", id);
//                            user.addProperty("name", editText1.getText().toString());
//                            user.addProperty("password", editText2.getText().toString());
//                            user.addProperty("mobile", editText3.getText().toString());
//                            user.addProperty("address", editText4.getText().toString());
//                            user.addProperty("city", editText5.getText().toString());
//
//
//                            OkHttpClient okHttpClient = new OkHttpClient();
//
//                            RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
//
//                            Request request = new Request.Builder()
//                                    .url("http://192.168.1.2:8080/colors/user/profileUpdate")
//                                    .post(requestBody)
//                                    .build();
//
//                            try {
//
//                                Response response = okHttpClient.newCall(request).execute();
//                                String responsetext = response.body().string();
//
//                                ResponseDTO<User_DTO> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<User_DTO>>(){}.getType());
//
//
//
//
//                                if (responseDTO.isSuccess()) {
//
//                                    User_DTO userDTO = responseDTO.getContent();
//
////                                    Log.i("colors-log", "User Name: " + userDTO.getName());
//                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    String userjson = gson.toJson(userDTO);
//                                    editor.putString("userData",userjson);
//                                    editor.apply();
//
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            showSuccessDialog(context, "Success");
//                                        }
//                                    });
//
//
//
//                                } else {
//                                    showErrorDialog(getContext(), "something went wrong");
//
//                                }
//
//
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }).start();
                }


            }
        });

        return view;
    }

    private void uploadImageToFirebase(Uri imageUri) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                Log.d("Firebase", "Image URL: " + imageUrl);
                                // Send imageUrl to backend or use it directly

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Integer id = user.getId();
                                        Gson gson = new Gson();
                                        JsonObject user = new JsonObject();
                                        user.addProperty("id", id);
                                        user.addProperty("name", editText1.getText().toString());
                                        user.addProperty("password", editText2.getText().toString());
                                        user.addProperty("mobile", editText3.getText().toString());
                                        user.addProperty("address", editText4.getText().toString());
                                        user.addProperty("city", editText5.getText().toString());
                                        user.addProperty("image_path", imageUrl);


                                        OkHttpClient okHttpClient = new OkHttpClient();

                                        RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));

                                        Request request = new Request.Builder()
                                                .url(BuildConfig.URL+"/user/profileUpdate")
                                                .post(requestBody)
                                                .build();

                                        try {

                                            Response response = okHttpClient.newCall(request).execute();
                                            String responsetext = response.body().string();

                                            ResponseDTO<User_DTO> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<User_DTO>>(){}.getType());




                                            if (responseDTO.isSuccess()) {

                                                User_DTO userDTO = responseDTO.getContent();

//                                    Log.i("colors-log", "User Name: " + userDTO.getName());
                                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                String userjson = gson.toJson(userDTO);
                                                editor.putString("userData",userjson);
                                                editor.apply();

                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        showSuccessDialog(context, "Success");
                                                    }
                                                });



                                            } else {
                                                showErrorDialog(getContext(), "something went wrong");

                                            }


                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }).start();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firebase", "Failed to get download URL", e);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Upload failed", e);
                    }
                });
    }

    public void showErrorDialog(Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_error, null);
        builder.setView(view);


        TextView tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        Button btnDismiss = view.findViewById(R.id.ErrorbtnDismiss);


        tvErrorMessage.setText(errorMessage);


        // Create and show dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Transparent background
        alertDialog.show();

        // Dismiss button click event
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    public void showSuccessDialog(Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_success, null);
        builder.setView(view);


        TextView tvErrorMessage = view.findViewById(R.id.tvSuccessMessage);
        Button btnDismiss = view.findViewById(R.id.sucessbtnDismiss);


        tvErrorMessage.setText(errorMessage);


        // Create and show dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Transparent background
        alertDialog.show();

        // Dismiss button click event
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

}

