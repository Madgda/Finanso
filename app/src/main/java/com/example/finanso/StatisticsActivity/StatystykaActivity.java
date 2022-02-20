package com.example.finanso.StatisticsActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.ListActivity.ListAdapter;
import com.example.finanso.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StatystykaActivity  extends AppCompatActivity {

    private DrawerLayout drawer;
    private FloatingActionButton plus;
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem dodajLista;
    private PieChart pieChart;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statystyka);

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
        loadPieChartData();


        barChart = findViewById(R.id.barChart);
        loadBarChartData();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Analiza wydatków");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void loadBarChartData() {
        ArrayList<BarEntry> rekordyB = new ArrayList<>();
        rekordyB.add(new BarEntry(0,140f));
        rekordyB.add(new BarEntry(1,420f));
        rekordyB.add(new BarEntry(2,330f));
        rekordyB.add(new BarEntry(3,550f));
        rekordyB.add(new BarEntry(4,920f));
       rekordyB.add(new BarEntry(5,160f));
        rekordyB.add(new BarEntry(6,213f));
        BarDataSet dataSetB = new BarDataSet(rekordyB,"Suma wydatków");

        ArrayList<String> dniWyswietlane = new ArrayList<>();
        dniWyswietlane.add("Pon");
        dniWyswietlane.add("Wt");
        dniWyswietlane.add("Śr");
        dniWyswietlane.add("Czw");
        dniWyswietlane.add("Pt");
        dniWyswietlane.add("Sb");
        dniWyswietlane.add("Nd");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dniWyswietlane.get((int)value);
            }
        };
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(formatter);

        dataSetB.setColor(getApplicationContext().getResources().getColor(R.color.BlueDarker));
        dataSetB.setBarBorderColor(Color.BLUE);
        dataSetB.setValueTextColor(Color.BLACK);
        dataSetB.setValueTextSize(10f);
        dataSetB.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData data = new BarData(dataSetB);
        data.setBarWidth(0.45f);
        barChart.getDescription().setEnabled(false);
        barChart.setData(data);

       // barChart.setTouchEnabled(true);
        //barChart.setDoubleTapToZoomEnabled(true);
   //     barChart.setDragEnabled(true);
     //   barChart.setScaleEnabled(true);



        //barChart.animateY(1400, Easing.EaseInOutQuad);
    }
    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("TOP 6 KATEGORII");
        pieChart.setCenterTextSize(10);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    private void loadPieChartData() {
        ArrayList<PieEntry> rekordyP = new ArrayList<>();
        rekordyP.add(new PieEntry(0.3f,"Spożywcze"));
        rekordyP.add(new PieEntry(0.35f,"Rachunki"));
        rekordyP.add(new PieEntry(0.1f,"Prezenty"));
        rekordyP.add(new PieEntry(0.1f,"Chemia"));
        rekordyP.add(new PieEntry(0.1f,"Remont"));
        rekordyP.add(new PieEntry(0.15f,"Inne"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for(int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }
            PieDataSet dataSet = new PieDataSet(rekordyP,"Kategoria");
            dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_statystyka,menu);
        menu.findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>wyszukaj daty</font>"));
        menu.findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>sortuj wg </font>"));
        menu.findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>pokaż</font>"));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuDodajLista:


                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
