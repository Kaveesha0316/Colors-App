package com.example.colors.ui.my_product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.colors.BuildConfig;
import com.example.colors.CustomerOrderrActivity;
import com.example.colors.R;
import com.example.colors.databinding.FragmentCartBinding;
import com.example.colors.myProductActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DTO.CategoryProfitDTO;
import DTO.ProfitResponseDTO;
import DTO.ResponseDTO;
import DTO.ResponseListDTO;
import DTO.User_DTO;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class my_productFragment extends Fragment {

    User_DTO user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_product, container, false);


        if (isNetworkAvailable()) {


            Button button = view.findViewById(R.id.button7);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), myProductActivity.class));
                }
            });

            Button button2 = view.findViewById(R.id.button5);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), CustomerOrderrActivity.class));
                }
            });
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.colors.userprefs", Context.MODE_PRIVATE);
            String userjson = sharedPreferences.getString("userData", null);

            if (userjson != null) {
                Gson gson = new Gson();

                user = gson.fromJson(userjson, User_DTO.class);

            }

            loadBarchart(view);
            loadPieChart(view);

            TextView textView1 = view.findViewById(R.id.textView33);
            TextView textView2 = view.findViewById(R.id.textView39);
            TextView textView3 = view.findViewById(R.id.textView41);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();

                    OkHttpClient okHttpClient = new OkHttpClient();

                    // Build URL with query parameters dynamically
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/orderdb/totalqty")
                            .newBuilder();

                    urlBuilder.addQueryParameter("user_id", String.valueOf(user.getId()));

                    // Convert to URL string
                    String url = urlBuilder.build().toString();

                    // Create request
                    Request request = new Request.Builder()
                            .url(url)
                            .build();


                    try {

                        Response response = okHttpClient.newCall(request).execute();
                        String responsetext = response.body().string();

                        Log.i("colors-log", responsetext);

                        ResponseDTO<String> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>() {
                        }.getType());

                        if (responseDTO.isSuccess()) {
                            String qty = responseDTO.getContent();

                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView1.setText(qty);
                                }
                            });

                        } else {


                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }
            }).start();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();

                    OkHttpClient okHttpClient = new OkHttpClient();

                    // Build URL with query parameters dynamically
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/orderdb/totalprofit")
                            .newBuilder();

                    urlBuilder.addQueryParameter("user_id", String.valueOf(user.getId()));

                    // Convert to URL string
                    String url = urlBuilder.build().toString();

                    // Create request
                    Request request = new Request.Builder()
                            .url(url)
                            .build();


                    try {

                        Response response = okHttpClient.newCall(request).execute();
                        String responsetext = response.body().string();

                        Log.i("colors-log", responsetext);

                        ResponseDTO<String> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>() {
                        }.getType());

                        if (responseDTO.isSuccess()) {
                            String profit = responseDTO.getContent();

                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView2.setText("Rs." + profit + "0");
                                }
                            });

                        } else {


                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();

                    OkHttpClient okHttpClient = new OkHttpClient();

                    // Build URL with query parameters dynamically
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/orderdb/bestproduct")
                            .newBuilder();

                    urlBuilder.addQueryParameter("user_id", String.valueOf(user.getId()));

                    // Convert to URL string
                    String url = urlBuilder.build().toString();

                    // Create request
                    Request request = new Request.Builder()
                            .url(url)
                            .build();


                    try {

                        Response response = okHttpClient.newCall(request).execute();
                        String responsetext = response.body().string();

                        Log.i("colors-log", responsetext);

                        ResponseDTO<String> responseDTO = gson.fromJson(responsetext, new TypeToken<ResponseDTO<String>>() {
                        }.getType());

                        if (responseDTO.isSuccess()) {
                            String name = responseDTO.getContent();

                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView3.setText(name);
                                }
                            });

                        } else {


                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }
            }).start();

        }

        return view;
    }

    private boolean isNetworkAvailable() {
        // Implement network check here
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public  void loadBarchart(View view){
        BarChart barChart = view.findViewById(R.id.barchart);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();

                        OkHttpClient okHttpClient = new OkHttpClient();

                        // Build URL with query parameters dynamically
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/orderdb/barchart")
                                .newBuilder();

                        urlBuilder.addQueryParameter("user_id",String.valueOf(user.getId()));

                        // Convert to URL string
                        String url = urlBuilder.build().toString();

                        // Create request
                        Request request = new Request.Builder()
                                .url(url)
                                .build();


                        try {

                            Response response = okHttpClient.newCall(request).execute();
                            String responsetext = response.body().string();

                            Log.i("colors-log", responsetext);

                            ResponseListDTO<ProfitResponseDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<ProfitResponseDTO>>(){}.getType());

                            if (responseDTO.isSuccess()) {
                                List<ProfitResponseDTO> responseDTOS  = responseDTO.getContent();
                                ((Activity) getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateBarChart(barChart, responseDTOS);
                                    }
                                });

                            } else {


                            }


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                    }
                }).start();




    }

    private void updateBarChart(BarChart barChart, List<ProfitResponseDTO> profitDataList) {
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<String> dateLabels = new ArrayList<>();

        for (int i = 0; i < profitDataList.size(); i++) {
            ProfitResponseDTO data = profitDataList.get(i);
            barEntryArrayList.add(new BarEntry(i, data.getProfit())); // Add profit value
            dateLabels.add(data.getDate()); // Add date label
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Profit (Rs.)");

        // 🎨 Define different colors for bars
        ArrayList<Integer> colorList = new ArrayList<>();
        int[] colors = new int[]{
                ContextCompat.getColor(getContext(), R.color.red),
                ContextCompat.getColor(getContext(), R.color.yellow),
                ContextCompat.getColor(getContext(), R.color.olive_green),
                ContextCompat.getColor(getContext(), R.color.red),
                ContextCompat.getColor(getContext(), R.color.yellow)
        };

        for (int i = 0; i < barEntryArrayList.size(); i++) {
            colorList.add(colors[i % colors.length]); // Assign colors in a repeating pattern
        }

        barDataSet.setColors(colorList);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        // Customize X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels)); // Set date labels
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(barEntryArrayList.size());

        // Customize Y-Axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextSize(12f);
        leftAxis.setTypeface(Typeface.DEFAULT_BOLD);
        barChart.getAxisRight().setEnabled(false); // Hide right Y-Axis

        // Set chart properties
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDescription(null);
        barChart.setTouchEnabled(true);
        barChart.setFitBars(true);

        // Animate chart
        barChart.animateY(1500, Easing.EaseInOutBounce);
        barChart.setData(barData);
        barChart.invalidate();
    }


    public  void  loadPieChart(View view){
        PieChart pieChart = view.findViewById(R.id.pieChart);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                OkHttpClient okHttpClient = new OkHttpClient();

                // Build URL with query parameters dynamically
                HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/orderdb/piechart")
                        .newBuilder();

                urlBuilder.addQueryParameter("user_id",String.valueOf(user.getId()));

                // Convert to URL string
                String url = urlBuilder.build().toString();

                // Create request
                Request request = new Request.Builder()
                        .url(url)
                        .build();


                try {

                    Response response = okHttpClient.newCall(request).execute();
                    String responsetext = response.body().string();

                    Log.i("colors-log", responsetext);

                    ResponseListDTO<CategoryProfitDTO> responseDTO =  gson.fromJson(responsetext, new TypeToken<ResponseListDTO<CategoryProfitDTO>>(){}.getType());

                    if (responseDTO.isSuccess()) {
                        List<CategoryProfitDTO> responseDTOS  = responseDTO.getContent();
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 updatePieChart(pieChart, responseDTOS);
                            }
                        });

                    } else {


                    }


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        }).start();


    }

    private void updatePieChart(PieChart pieChart, List<CategoryProfitDTO> categoryProfitList) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for (CategoryProfitDTO data : categoryProfitList) {
            pieEntries.add(new PieEntry(data.getProfit(), data.getCategory()));
        }

        // Modern Color Palette
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(ContextCompat.getColor(getContext(), R.color.terracotta));
        colorList.add(ContextCompat.getColor(getContext(), R.color.olive_green));
        colorList.add(ContextCompat.getColor(getContext(), R.color.dark_brown));
        colorList.add(ContextCompat.getColor(getContext(), R.color.terracotta));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Profit By Category");
        pieDataSet.setColors(colorList);
        pieDataSet.setSliceSpace(4f); // Add spacing between slices
        pieDataSet.setSelectionShift(10f); // Enlarge selected slice

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(14f);
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieData.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white)); // White text for better contrast

        // Modern Pie Chart Look
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(true); // Show category names
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);

        pieChart.setUsePercentValues(true); // Show values in %
        pieChart.setHoleRadius(10f); // Small hole for modern look
        pieChart.setTransparentCircleRadius(10f);

        // Enable & Customize Legend (Category Labels)
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setTextSize(14f);
        legend.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        legend.setDrawInside(false);
        legend.setEnabled(true); // Show legend
        pieChart.getLegend().setEnabled(false);

        Description description = new Description();
        description.setText("");
        description.setTextSize(16f);  // Increase the text size (adjust the value as needed)
        description.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Set text color if needed

        pieChart.setDescription(description);

        // Animate the Chart
        pieChart.animateY(1500, Easing.EaseInOutQuad);
        pieChart.invalidate();
    }


}