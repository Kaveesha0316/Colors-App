package com.example.colors.SqlLite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import model.Order;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "orders.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QTY = "qty";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_IMAGE_URL = "image_url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the orders table
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_QTY + " TEXT, "
                + COLUMN_PRICE + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_STATUS + " TEXT, "
                + COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if exists and create new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // Method to insert order into the database
    public void insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + TABLE_ORDERS + "("
                + COLUMN_NAME + ", "
                + COLUMN_QTY + ", "
                + COLUMN_PRICE + ", "
                + COLUMN_DATE + ", "
                + COLUMN_STATUS + ", "
                + COLUMN_IMAGE_URL + ") VALUES ('"
                + order.getNamae() + "', '"
                + order.getQty() + "', '"
                + order.getPrice() + "', '"
                + order.getDate() + "', '"
                + order.getStatus() + "', '"
                + order.getImageUrl() + "')";
        db.execSQL(insertQuery);
        db.close();
    }

    // Method to get all orders
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Order order = new Order(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_QTY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public void deleteAllOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ORDERS);
        db.close();
    }
}
