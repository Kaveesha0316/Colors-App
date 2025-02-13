package com.example.colors;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Objects;

public class AddProductActivity extends AppCompatActivity {

    private Uri ProductImageUri1;
    private Uri ProductImageUri2;
    private Uri ProductImageUri3;


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

        EditText editText1 = findViewById(R.id.editTextTextPname);
        EditText editText2 = findViewById(R.id.editTextText2Pprice);
        EditText editText3 = findViewById(R.id.editTextTextPqty);
        Button button = findViewById(R.id.button8);

        Intent intent = getIntent();
        if (intent != null) {

            if(Objects.equals(intent.getStringExtra("isUpdate"), "true")){
                TextView textView34 = findViewById(R.id.textView34);
                textView34.setText("Update Product");

                editText1.setText(intent.getStringExtra("productName"));
                editText2.setText(intent.getStringExtra("productPrice"));
                editText3.setText(intent.getStringExtra("productQty"));

                button.setText("Update product");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       Toast.makeText(AddProductActivity.this,"Update",Toast.LENGTH_LONG).show();
                    }
                });

            }else {

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(AddProductActivity.this, "Inseart", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }






        Spinner spinner = findViewById(R.id.spinner2);
        String sort[] = new String[]{"Select","Pottery","Wooden","Art","Jewelry","Decors","Seasonal"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item,sort);
        spinner.setAdapter(arrayAdapter);



        ImageView  imageView1 = findViewById(R.id.addproductimageView1);
        ImageView imageView2 = findViewById(R.id.addproductimageView2);
        ImageView imageView3 = findViewById(R.id.addproductimageView3);

        ActivityResultLauncher<String> productImagePickerLauncher1 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView1.setImageURI(uri);
                        ProductImageUri1 = uri;
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
                        ProductImageUri2 = uri;
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
                        ProductImageUri3 = uri;
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


}