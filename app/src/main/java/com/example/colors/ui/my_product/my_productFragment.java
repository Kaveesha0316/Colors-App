package com.example.colors.ui.my_product;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;


public class my_productFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_product, container, false);

        loadBarchart(view);
        loadPieChart(view);
        Button button =  view.findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), myProductActivity.class));
            }
        });

        return view;
    }

    public  void loadBarchart(View view){
        BarChart barChart = view.findViewById(R.id.barchart);

        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        barEntryArrayList.add(new BarEntry(0, 25000));  // Date 1: Rs. 25,000
        barEntryArrayList.add(new BarEntry(1, 32000));  // Date 2: Rs. 32,000
        barEntryArrayList.add(new BarEntry(2, 18000));  // Date 3: Rs. 18,000
        barEntryArrayList.add(new BarEntry(3, 42000));  // Date 4: Rs. 42,000
        barEntryArrayList.add(new BarEntry(4, 37000));  // Date 5: Rs. 37,000

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList,"Profit (Rs.)");


        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(ContextCompat.getColor(getContext(), R.color.gray));
        colorList.add(ContextCompat.getColor(getContext(), R.color.gray));
        colorList.add(ContextCompat.getColor(getContext(), R.color.gray));
        colorList.add(ContextCompat.getColor(getContext(), R.color.gray));
        colorList.add(ContextCompat.getColor(getContext(), R.color.gray));
        barDataSet.setColors(colorList);
        barDataSet.setValueTextColor(R.color.black);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);


        barChart.getLegend().setTextColor(R.color.black);
        barChart.getLegend().setTextSize(12f);
        barData.setBarWidth(0.6f);


        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDescription(null);
        barChart.setTouchEnabled(true);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        String[] dateLabels = {"01 Jan", "02 Jan", "03 Jan", "04 Jan", "05 Jan"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(barEntryArrayList.size());
        barChart.getAxisLeft().setDrawGridLines(false);
        // Customize Y-Axis to Show Profit (Rs.)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextSize(12f);
        leftAxis.setTypeface(Typeface.DEFAULT_BOLD);



        // Hide Right Y-Axis
        barChart.getAxisRight().setEnabled(false);

        // Animate Chart
        barChart.animateY(1500, Easing.EaseInOutBounce);
        barChart.invalidate();

        barChart.setData(barData);

    }

    public  void  loadPieChart(View view){
        PieChart pieChart = view.findViewById(R.id.pieChart);

// Data with Labels
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(35, "Art"));
        pieEntries.add(new PieEntry(40, "Wood"));
        pieEntries.add(new PieEntry(25, "Potty"));
        pieEntries.add(new PieEntry(25, "Gualry"));

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
        pieData.setValueTextColor(ContextCompat.getColor(getContext(),R.color.white)); // White text for better contrast

/// Modern Pie Chart Look
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
        legend.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
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