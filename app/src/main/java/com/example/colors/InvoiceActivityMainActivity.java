package com.example.colors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import model.Cart;

public class InvoiceActivityMainActivity extends AppCompatActivity {

    private String orderId;
    private double totalAmount;
    private String date;
    private ListView lvItems;
    private File invoiceFile;
    ArrayList<Cart> cartArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.invoice_activity_main);

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        // Get data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getStringExtra("ORDER_ID");
            totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0);
            date = intent.getStringExtra("DATE");
            cartArrayList = (ArrayList<Cart>) getIntent().getSerializableExtra("ITEM");

        }


        lvItems = findViewById(R.id.lvItems);

        // Load list into ListView
        CartAdapter adapter = new CartAdapter(this, cartArrayList);
        lvItems.setAdapter(adapter);

        // Initialize UI
        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
        TextView tvDate = findViewById(R.id.tvDate);
        Button btnDownloadInvoice = findViewById(R.id.btn_download_invoice);

        // Set text fields
        tvOrderId.setText(orderId);
        tvTotalAmount.setText("Rs." + totalAmount+"0");
        tvDate.setText(date);

        // Generate Invoice PDF
        generateInvoicePdf();

        // Button to open PDF
        btnDownloadInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPdf();
            }
        });
    }

    private void generateInvoicePdf() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(2);

        // Create PDF page (A4 size: 595 x 842)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int startX = 50; // Left margin
        int startY = 50; // Top margin
        int lineSpacing = 30; // Space between lines
        int tableStartY = 220; // Start position of the table

        // ** Draw Invoice Header **
        paint.setTextSize(22);
        paint.setFakeBoldText(true);
        canvas.drawText("INVOICE", 240, startY, paint);


        // ** Order Details **
        paint.setTextSize(16);
        paint.setFakeBoldText(false);
        startY += 50;
        canvas.drawText("Order ID: " + orderId, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Total Amount: Rs." + totalAmount+"0", startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText("DATE:" + date, startX, startY, paint);
        startY += lineSpacing;

        // ** Draw Table Header **
        paint.setTextSize(14);
        paint.setFakeBoldText(true);
        canvas.drawText("Item", startX, tableStartY, paint);
        canvas.drawText("Qty", startX + 250, tableStartY, paint);
        canvas.drawText("Price", startX + 350, tableStartY, paint);
        canvas.drawText("Total", startX + 450, tableStartY, paint);

        // Draw Line under Header
        canvas.drawLine(startX, tableStartY + 10, 540, tableStartY + 10, linePaint);

        // ** Draw Cart Items **
        paint.setFakeBoldText(false);
        int itemStartY = tableStartY + 40; // First row position
        for (Cart item : cartArrayList) {
            canvas.drawText(item.getProduct_name(), startX, itemStartY, paint);
            canvas.drawText(String.valueOf(item.getQty()), startX + 250, itemStartY, paint);
            canvas.drawText("Rs." + item.getProduct_price()+"0", startX + 350, itemStartY, paint);
            canvas.drawText("Rs." + (item.getProduct_price() * Double.parseDouble(item.getQty()))+"0", startX + 450, itemStartY, paint);
            itemStartY += lineSpacing;
        }

        // ** Draw Bottom Line **
        canvas.drawLine(startX, itemStartY, 540, itemStartY, linePaint);
        itemStartY += 40;

        // ** Draw Thank You Message **
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        canvas.drawText("Thank you for your purchase!", startX, itemStartY, paint);
        canvas.drawText("Colors (Pvt)Ltd", startX, itemStartY, paint);

        // Finish PDF page
        pdfDocument.finishPage(page);

        // Save PDF file
        invoiceFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "invoice_" + orderId + ".pdf");
        try {
            FileOutputStream fos = new FileOutputStream(invoiceFile);
            pdfDocument.writeTo(fos);
            fos.close();
            pdfDocument.close();
//            Toast.makeText(this, "Invoice saved: " + invoiceFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openPdf() {
        if (invoiceFile.exists()) {
            Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", invoiceFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invoice not found!", Toast.LENGTH_SHORT).show();
        }
    }
    public class CartAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Cart> cartItems;

        public CartAdapter(Context context, ArrayList<Cart> cartItems) {
            this.context = context;
            this.cartItems = cartItems;
        }

        @Override
        public int getCount() {
            return cartItems.size();
        }

        @Override
        public Object getItem(int position) {
            return cartItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            TextView text1 = convertView.findViewById(android.R.id.text1);
            TextView text2 = convertView.findViewById(android.R.id.text2);

            Cart item = cartItems.get(position);
            text1.setText(item.getProduct_name());
            text2.setText("Qty: " + item.getQty() + " * Rs." + item.getProduct_price()+"0");

            return convertView;
        }
    }

}

