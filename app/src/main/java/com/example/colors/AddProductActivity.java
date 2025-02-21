package com.example.colors;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import DTO.ResponseDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddProductActivity extends AppCompatActivity {

    User_DTO user;
    private List<Uri> imageUris;
    private Uri ProductImageUri1;
    private Uri ProductImageUri2;
    private Uri ProductImageUri3;
    private String uploadProductImageUrl1;
    private String uploadProductImageUrl2;
    private String uploadProductImageUrl3;
    SweetAlertDialog progressDialog;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Spinner spinner;

    String selectedValue;
    String productId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
        String userjson = sharedPreferences.getString("userData",null);

        if (userjson != null) {
            Gson gson = new Gson();

            user = gson.fromJson(userjson, User_DTO.class);
        }

         editText1 = findViewById(R.id.editTextTextPname);
         editText2 = findViewById(R.id.editTextText2Pprice);
         editText3 = findViewById(R.id.editTextTextPqty);
         editText4 = findViewById(R.id.editTextTextMultiLine);


        Button button = findViewById(R.id.button8);
        imageUris = new ArrayList<>();

        Intent intent = getIntent();

         spinner = findViewById(R.id.spinner2);
        String sort[] = new String[]{"Select","Pottery","Wooden","Art","Jewelry","Decors","Seasonal"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item,sort);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedValue = adapterView.getItemAtPosition(i).toString();
                Log.i("colors-log",selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (intent != null) {

            if(Objects.equals(intent.getStringExtra("isUpdate"), "true")){
                TextView textView34 = findViewById(R.id.textView34);
                textView34.setText("Update Product");

                ImageView  imageView1 = findViewById(R.id.addproductimageView1);
                ImageView imageView2 = findViewById(R.id.addproductimageView2);
                ImageView imageView3 = findViewById(R.id.addproductimageView3);

                editText1.setText(intent.getStringExtra("productName"));
                editText2.setText(intent.getStringExtra("productPrice"));
                editText2.setEnabled(false);
                editText3.setText(intent.getStringExtra("productQty"));
                editText4.setText(intent.getStringExtra("productDescription"));
                productId = intent.getStringExtra("productId");

                spinner = findViewById(R.id.spinner2);
                spinner.setSelection(Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("productCategory"))));
                spinner.setEnabled(false);
                selectedValue = String.valueOf(spinner.getSelectedItem());

                Glide.with(AddProductActivity.this)
                        .load(intent.getStringExtra("productimg1"))
                        .placeholder(R.drawable.loading) // Optional: Placeholder image
                        .error(R.drawable.mark) // Optional: Error image
                        .into(imageView1);

//                String urlString1 = intent.getStringExtra("productimg1");
//                Uri uri1 = Uri.parse(urlString1);
//                imageUris.add(0,uri1);
//
//                String urlString2 = intent.getStringExtra("productimg2");
//                Uri uri2 = Uri.parse(urlString2);
//                imageUris.add(1,uri2);
//
//                String urlString3 = intent.getStringExtra("productimg3");
//                Uri uri3 = Uri.parse(urlString3);
//                imageUris.add(2,uri3);

                Glide.with(AddProductActivity.this)
                        .load(intent.getStringExtra("productimg2"))
                        .placeholder(R.drawable.loading) // Optional: Placeholder image
                        .error(R.drawable.mark) // Optional: Error image
                        .into(imageView2);

                Glide.with(AddProductActivity.this)
                        .load(intent.getStringExtra("productimg3"))
                        .placeholder(R.drawable.loading) // Optional: Placeholder image
                        .error(R.drawable.mark) // Optional: Error image
                        .into(imageView3);

                button.setText("Update product");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageUris.isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please select product image ")
                                    .show();
                        }else if (editText1.getText().toString().isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please enter product name")
                                    .show();
                        }else if (editText3.getText().toString().isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please enter product qty")
                                    .show();
                        }else if (editText4.getText().toString().isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please enter product descriptin")
                                    .show();
                        }else{
                            uploadImageToFirebase(imageUris);


                        }
                    }
                });

            }else {

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageUris.isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please select product image ")
                                    .show();
                        }else if (editText1.getText().toString().isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please enter product name")
                                    .show();
                        }else if (editText2.getText().toString().isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please enter product price")
                                    .show();
                        }else if (editText3.getText().toString().isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please enter product qty")
                                    .show();
                        }else if (editText4.getText().toString().isEmpty()){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please enter product descriptin")
                                    .show();
                        }else if (selectedValue.equals("Select")){
                            new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning!")
                                    .setContentText("Please select category")
                                    .show();
                        }else{
                            uploadImageToFirebase(imageUris);


                        }
                    }
                });


            }

        }










        ImageView  imageView1 = findViewById(R.id.addproductimageView1);
        ImageView imageView2 = findViewById(R.id.addproductimageView2);
        ImageView imageView3 = findViewById(R.id.addproductimageView3);

        ActivityResultLauncher<String> productImagePickerLauncher1 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView1.setImageURI(uri);
                        imageUris.add(0,uri);
                    }
                }
        );
         imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productImagePickerLauncher1.launch("image/*");
            }
        });

        ActivityResultLauncher<String> productImagePickerLauncher2 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView2.setImageURI(uri);
                        imageUris.add(1,uri);
                    }
                }
        );

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productImagePickerLauncher2.launch("image/*");
            }
        });

        ActivityResultLauncher<String> productImagePickerLauncher3 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView3.setImageURI(uri);
                        imageUris.add(2,uri);
                    }
                }
        );

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productImagePickerLauncher3.launch("image/*");
            }
        });


    }

    public void  firebaseInsert(){


        HashMap<String,Object> document = new HashMap<>();
        document.put("name", editText1.getText().toString());
        document.put("user",String.valueOf(user.getId()));
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("product").add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("colors-log", "onSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("colors-log", "error");
                    }
                });

        firestore.collection("product")
                .whereEqualTo("user",String.valueOf(user.getId()))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("colors-log", "Listen failed.", error);
                            return;
                        }

                        if (snapshots != null) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {

                                if (dc.getType().equals(DocumentChange.Type.ADDED)){
                                    Log.d("colors-log", "notifed ");
                                    notification();
                                }

                            }
                        }
                    }
                });

    }

    public void notification() {
        // Use getActivity() to access the context of the fragment's parent activity
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "C1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Notification notification = new NotificationCompat.Builder(AddProductActivity.this, "C1")
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Product Published")  // Title of the notification
                .setContentText("Your New Product has been Added successfully")  // Content with Order ID and Total Amount
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Priority
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 1000, 500})
                .build();

        notificationManager.notify(1, notification);
    }

    private void uploadImageToFirebase(List<Uri> productImages) {

        Log.i("Firebase", "Product image URL: " + productImages);
// Create a SweetAlert progress dialog
        progressDialog = new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("Loading...");
        progressDialog.setCancelable(false); // Prevent dialog from being dismissed by tapping outside
        progressDialog.show(); // Show the progress dialog

        FirebaseStorage storage = FirebaseStorage.getInstance();
        List<String> uploadedUrls = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(0);
        int totalFiles = productImages.size();

        for (Uri productImageUri : productImages) {
            StorageReference storageRef = storage.getReference().child("productImages/" + System.currentTimeMillis() + ".jpg");

            storageRef.putFile(productImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Save the image URL in the map with a specific key for product images
                                    uploadedUrls.add(uri.toString());
                                    Log.i("Firebase", "Product image URL: " + uri.toString());

                                    // Check if all files are uploaded
                                    if (uploadCount.incrementAndGet() == totalFiles) {
//                                        sendImagesToBackend(uploadedUrls);

                                        if (uploadedUrls.size() == 3){
                                            String im0 = uploadedUrls.get(0);
                                            String im1 = uploadedUrls.get(1);
                                            String im2 = uploadedUrls.get(2);

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Integer id = user.getId();
                                                    Gson gson = new Gson();
                                                    JsonObject product = new JsonObject();
                                                    product.addProperty("Reg_product_User_id", id.toString());
                                                    product.addProperty("Reg_product_name", editText1.getText().toString());
                                                    product.addProperty("Reg_product_price", editText2.getText().toString());
                                                    product.addProperty("Reg_product_qty", editText3.getText().toString());
                                                    product.addProperty("Reg_product_description", editText4.getText().toString());
                                                    product.addProperty("Reg_product_category", selectedValue);
                                                    product.addProperty("Reg_product_image_path1", im0);
                                                    product.addProperty("Reg_product_image_path2", im1);
                                                    product.addProperty("Reg_product_image_path3", im2);
                                                    product.addProperty("Reg_product_product_id", productId);

                                                    OkHttpClient okHttpClient = new OkHttpClient();

                                                    RequestBody requestBody = RequestBody.create(gson.toJson(product), MediaType.get("application/json"));

                                                    Request request = new Request.Builder()
                                                            .url(BuildConfig.URL+"/product/add")
                                                            .post(requestBody)
                                                            .build();

                                                    try {
                                                        Response response = okHttpClient.newCall(request).execute();
                                                        String responsetext = response.body().string();

                                                        ResponseDTO<String> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>() {}.getType());

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismissWithAnimation(); // Dismiss the progress dialog

                                                                if (responseDTO.isSuccess()) {
                                                                    // Show success dialog
                                                                    new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                            .setTitleText("Success!")
                                                                            .setContentText("successful.")
                                                                            .show();

                                                                    firebaseInsert();

                                                                    editText1.setText("");
                                                                    editText1.requestFocus();
                                                                    editText2.setText("");
                                                                    editText3.setText("");
                                                                    editText4.setText("");

                                                                    ImageView  imageView1 = findViewById(R.id.addproductimageView1);
                                                                    imageView1.setImageResource(R.drawable.add);
                                                                    ImageView imageView2 = findViewById(R.id.addproductimageView2);
                                                                    imageView2.setImageResource(R.drawable.add);
                                                                    ImageView imageView3 = findViewById(R.id.addproductimageView3);
                                                                    imageView3.setImageResource(R.drawable.add);

                                                                    Spinner spinner = findViewById(R.id.spinner2);
                                                                    spinner.setSelection(0);

                                                                } else {
                                                                    // Show error dialog
                                                                    new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Error!")
                                                                            .setContentText("Something went wrong. Please try again.")
                                                                            .show();
                                                                }
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismissWithAnimation(); // Dismiss the progress dialog

                                                                // Show error dialog for exceptions
                                                                new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Error!")
                                                                        .setContentText("An error occurred. Please try again.")
                                                                        .show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }).start();


                                        }else if (uploadedUrls.size() == 2){
                                            String im0 = uploadedUrls.get(0);
                                            String im1 = uploadedUrls.get(1);
                                            String im2 = "";

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Integer id = user.getId();
                                                    Gson gson = new Gson();
                                                    JsonObject product = new JsonObject();
                                                    product.addProperty("Reg_product_User_id", id.toString());
                                                    product.addProperty("Reg_product_name", editText1.getText().toString());
                                                    product.addProperty("Reg_product_price", editText2.getText().toString());
                                                    product.addProperty("Reg_product_qty", editText3.getText().toString());
                                                    product.addProperty("Reg_product_description", editText4.getText().toString());
                                                    product.addProperty("Reg_product_category", selectedValue);
                                                    product.addProperty("Reg_product_image_path1", im0);
                                                    product.addProperty("Reg_product_image_path2", im1);
                                                    product.addProperty("Reg_product_image_path3", im2);
                                                    product.addProperty("Reg_product_product_id", productId);

                                                    OkHttpClient okHttpClient = new OkHttpClient();

                                                    RequestBody requestBody = RequestBody.create(gson.toJson(product), MediaType.get("application/json"));

                                                    Request request = new Request.Builder()
                                                            .url(BuildConfig.URL+"/product/add")
                                                            .post(requestBody)
                                                            .build();

                                                    try {
                                                        Response response = okHttpClient.newCall(request).execute();
                                                        String responsetext = response.body().string();

                                                        ResponseDTO<String> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>() {}.getType());

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismissWithAnimation(); // Dismiss the progress dialog

                                                                if (responseDTO.isSuccess()) {
                                                                    // Show success dialog
                                                                    new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                            .setTitleText("Success!")
                                                                            .setContentText("successful.")
                                                                            .show();

                                                                    editText1.setText("");
                                                                    editText1.requestFocus();
                                                                    editText2.setText("");
                                                                    editText3.setText("");
                                                                    editText4.setText("");

                                                                    ImageView  imageView1 = findViewById(R.id.addproductimageView1);
                                                                    imageView1.setImageResource(R.drawable.add);
                                                                    ImageView imageView2 = findViewById(R.id.addproductimageView2);
                                                                    imageView2.setImageResource(R.drawable.add);
                                                                    ImageView imageView3 = findViewById(R.id.addproductimageView3);
                                                                    imageView3.setImageResource(R.drawable.add);

                                                                    Spinner spinner = findViewById(R.id.spinner2);
                                                                    spinner.setSelection(0);

                                                                } else {
                                                                    // Show error dialog
                                                                    new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Error!")
                                                                            .setContentText("Something went wrong. Please try again.")
                                                                            .show();
                                                                }
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismissWithAnimation(); // Dismiss the progress dialog

                                                                // Show error dialog for exceptions
                                                                new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Error!")
                                                                        .setContentText("An error occurred. Please try again.")
                                                                        .show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }).start();


                                        }else {
                                            String im0 = uploadedUrls.get(0);
                                            String im1 = "";
                                            String im2 = "";

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Integer id = user.getId();
                                                    Gson gson = new Gson();
                                                    JsonObject product = new JsonObject();
                                                    product.addProperty("Reg_product_User_id", id.toString());
                                                    product.addProperty("Reg_product_name", editText1.getText().toString());
                                                    product.addProperty("Reg_product_price", editText2.getText().toString());
                                                    product.addProperty("Reg_product_qty", editText3.getText().toString());
                                                    product.addProperty("Reg_product_description", editText4.getText().toString());
                                                    product.addProperty("Reg_product_category", selectedValue);
                                                    product.addProperty("Reg_product_image_path1", im0);
                                                    product.addProperty("Reg_product_image_path2", im1);
                                                    product.addProperty("Reg_product_image_path3", im2);
                                                    product.addProperty("Reg_product_product_id", productId);

                                                    OkHttpClient okHttpClient = new OkHttpClient();

                                                    RequestBody requestBody = RequestBody.create(gson.toJson(product), MediaType.get("application/json"));

                                                    Request request = new Request.Builder()
                                                            .url(BuildConfig.URL+"product/add")
                                                            .post(requestBody)
                                                            .build();

                                                    try {
                                                        Response response = okHttpClient.newCall(request).execute();
                                                        String responsetext = response.body().string();

                                                        ResponseDTO<String> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>() {}.getType());

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismissWithAnimation(); // Dismiss the progress dialog

                                                                if (responseDTO.isSuccess()) {
                                                                    // Show success dialog
                                                                    new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                            .setTitleText("Success!")
                                                                            .setContentText("successful.")
                                                                            .show();

                                                                    editText1.setText("");
                                                                    editText1.requestFocus();
                                                                    editText2.setText("");
                                                                    editText3.setText("");
                                                                    editText4.setText("");

                                                                    ImageView  imageView1 = findViewById(R.id.addproductimageView1);
                                                                    imageView1.setImageResource(R.drawable.add);
                                                                    ImageView imageView2 = findViewById(R.id.addproductimageView2);
                                                                    imageView2.setImageResource(R.drawable.add);
                                                                    ImageView imageView3 = findViewById(R.id.addproductimageView3);
                                                                    imageView3.setImageResource(R.drawable.add);

                                                                    Spinner spinner = findViewById(R.id.spinner2);
                                                                    spinner.setSelection(0);

                                                                } else {
                                                                    // Show error dialog
                                                                    new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Error!")
                                                                            .setContentText("Something went wrong. Please try again.")
                                                                            .show();
                                                                }
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismissWithAnimation(); // Dismiss the progress dialog

                                                                // Show error dialog for exceptions
                                                                new SweetAlertDialog(AddProductActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Error!")
                                                                        .setContentText("An error occurred. Please try again.")
                                                                        .show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }).start();


                                        }






                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Upload failed for product image", e);
                        }
                    });
        }



    }


}