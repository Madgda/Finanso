package com.example.finanso.StatisticsActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.ListActivity.ListActivity;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class StatystykaActivity  extends AppCompatActivity {

    private DrawerLayout drawer;
    private FloatingActionButton plus;
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem dodajLista;
    private PieChart pieChart;
    private BarChart barChart;
    public Button dzienButton;
    public Button tydzienButton;
    private Button miesiacButton;
    private Button kwartalButton;
    private Button rokButton;
    private TextView dataOdEdit;
    private TextView dataDoEdit;
    private Integer widok;//0-dzień, 1-tydzień, 2-miesiąc, 3-kwartał, 4-rok
    private Button nextButton;
    private Button backButton;
    private MenuItem wybierzDateMenuItem;
    private MenuItem ustawDzisDataMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statystyka);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

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
        backButton= findViewById(R.id.backButton);
        nextButton= findViewById(R.id.nextButton);
        dzienButton= findViewById(R.id.dzienButton);
        tydzienButton= findViewById(R.id.tydzienButton);
        miesiacButton= findViewById(R.id.miesiacButton);
        kwartalButton= findViewById(R.id.kwartalButton);
        rokButton= findViewById(R.id.rokButton);
        dataOdEdit= findViewById(R.id.dataOdText);
        dataDoEdit= findViewById(R.id.dataDoText);

        dataOdEdit.setText(currentDate);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf1.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        String output = sdf1.format(c.getTime());
        dataDoEdit.setText(output);
        selectButton(dzienButton);
        widok=0;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            switch (widok){
                case 0:
                    naxtOrPreviousDay(1);
                    dayChangeNextDate();
                    break;
                case 1:
                    nextOrPreviousWeek(1);
                    weekChangeNextDate();
                    break;
                case 2:
                    nextOrPreviousMonth(1);
                    monthChangeNextDate();
                    break;
                case 3:
                    nextOrPreviousQuarter(1);
                    quarterChangeNextDate();
                    break;
                case 4:
                    nextOrPreviousYear(1);
                    yearChangeNextDate();
                    break;

            }


            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            switch (widok){
                case 0:
                    naxtOrPreviousDay(0);
                    dayChangeNextDate();
                    break;
                case 1:
                    nextOrPreviousWeek(0);
                    weekChangeNextDate();
                    break;
                case 2:
                    nextOrPreviousMonth(0);
                    monthChangeNextDate();
                    break;
                case 3:
                    nextOrPreviousQuarter(0);
                    quarterChangeNextDate();
                    break;
                case 4:
                    nextOrPreviousYear(0);
                    yearChangeNextDate();
                    break;

            }


            }
        });

        dzienButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                dayChangeNextDate();
                widok=0;
                selectButton(dzienButton);
            }


        });
        tydzienButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                weekChangeNextDate();
                widok=1;
                selectButton(tydzienButton);
            }
        });
        miesiacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                monthChangeNextDate();
                widok=2;
                selectButton(miesiacButton);
            }
        });
        kwartalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                quarterChangeNextDate();
                widok=3;
                selectButton(kwartalButton);
            }
        });
        rokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                yearChangeNextDate();
                widok=4;
                selectButton(rokButton);
            }
        });
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
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setCenterText("TOP 6 KATEGORII");
        //pieChart.setCenterTextSize(10);
        pieChart.getDescription().setEnabled(true);
        pieChart.getDescription().setTextColor(Color.BLACK);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }
    private void selectButton(Button button) {
        Button tempButton = button;
        uncheckButtonsAll();
        button.setBackgroundColor(getColor(R.color.BlueDarkest));
        button.setTextColor(getColor(R.color.grey));

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
    private void uncheckButtonsAll(){
        dzienButton.setBackgroundColor(getColor(R.color.BlueDarker));
        tydzienButton.setBackgroundColor(getColor(R.color.BlueDarker));
        miesiacButton.setBackgroundColor(getColor(R.color.BlueDarker));
        kwartalButton.setBackgroundColor(getColor(R.color.BlueDarker));
        rokButton.setBackgroundColor(getColor(R.color.BlueDarker));
        dzienButton.setTextColor(getColor(R.color.white));
        tydzienButton.setTextColor(getColor(R.color.white));
        miesiacButton.setTextColor(getColor(R.color.white));
        kwartalButton.setTextColor(getColor(R.color.white));
        rokButton.setTextColor(getColor(R.color.white));
    }

    private void naxtOrPreviousDay(Integer way)
    //way=0 previous
    //way=1 next
    {
        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch(way){
            case 0:
                c.add(Calendar.DATE, -1);
                break;

            case 1:
                c.add(Calendar.DATE, 1);
                break;

            default:
                break;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);
    }

    private void nextOrPreviousWeek(Integer way)
    //way=0 previous
    //way=1 next
    {
        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch(way){
            case 0:
                c.add(Calendar.WEEK_OF_MONTH, -1);
                break;

            case 1:
                c.add(Calendar.WEEK_OF_MONTH, 1);
                break;

            default:
                break;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);
    }
    private void nextOrPreviousMonth(Integer way)
    //way=0 previous
    //way=1 next
    {

        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch(way){
            case 0:
                c.add(Calendar.MONTH, -1);
                break;

            case 1:
                c.add(Calendar.MONTH, 1);
                break;

            default:
                break;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);
    }
 private void nextOrPreviousQuarter(Integer way)
    //way=0 previous
    //way=1 next
    {

        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch(way){
            case 0:
                c.add(Calendar.MONTH, -3);
                break;

            case 1:
                c.add(Calendar.MONTH, 3);
                break;

            default:
                break;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);
    }
 private void nextOrPreviousYear(Integer way)
    //way=0 previous
    //way=1 next
    {

        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch(way){
            case 0:
                c.add(Calendar.YEAR, -1);
                break;

            case 1:
                c.add(Calendar.YEAR, 1);
                break;

            default:
                break;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);
    }

        private void dayChangeNextDate(){
        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        String output = sdf1.format(c.getTime());
        dataDoEdit.setText(output);
    }
    private void weekChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");

        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);

        c.add(Calendar.WEEK_OF_MONTH, 1);
        output = sdf1.format(c.getTime());
        dataDoEdit.setText(output);
    }

    private void monthChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");

        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);

        c.add(Calendar.MONTH, 1);
        output = sdf1.format(c.getTime());
        dataDoEdit.setText(output);
    }

    private void quarterChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.set(Calendar.DAY_OF_MONTH, 1);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3)
            c.set(Calendar.MONTH, 0);
        else if (currentMonth >= 4 && currentMonth <= 6)
            c.set(Calendar.MONTH, 3);
        else if (currentMonth >= 7 && currentMonth <= 9)
            c.set(Calendar.MONTH, 6);
        else if (currentMonth >= 10 && currentMonth <= 12)
            c.set(Calendar.MONTH, 9);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);

        c.add(Calendar.MONTH, 3);
        output = sdf1.format(c.getTime());
        dataDoEdit.setText(output);
    }
    private void yearChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.set(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");

        String output = sdf1.format(c.getTime());
        dataOdEdit.setText(output);

        c.add(Calendar.YEAR, 1);
        output = sdf1.format(c.getTime());
        dataDoEdit.setText(output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_statystyka,menu);
         wybierzDateMenuItem = menu.findItem(R.id.item2);
         ustawDzisDataMenuItem = menu.findItem(R.id.item3);
        wybierzDateMenuItem.setTitle(Html.fromHtml("<font color='#000000'>Wybierz datę początkową</font>"));
        ustawDzisDataMenuItem.setTitle(Html.fromHtml("<font color='#000000'>Ustaw dzisiejszą date</font>"));

        ustawDzisDataMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
                dataOdEdit.setText(currentDate);
                dayChangeNextDate();
                widok=0;
                selectButton(dzienButton);
                return false;
            }
        });
        wybierzDateMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                        final Calendar kalendarz = Calendar.getInstance();
                        int day = kalendarz.get(Calendar.DAY_OF_MONTH);
                        int month = kalendarz.get(Calendar.MONTH);
                        int year = kalendarz.get(Calendar.YEAR);

                        Locale locale = getResources().getConfiguration().locale;
                        Locale.setDefault(locale);
                        DatePickerDialog picker = new DatePickerDialog(StatystykaActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if(day<10&&month<9){
                                    dataOdEdit.setText("0"+day + "." + "0"+(month + 1) + "." + year);
                                }
                                else if(day<10){
                                    dataOdEdit.setText("0"+day + "." + (month + 1) + "." + year);
                                }
                                else if(month<9) {
                                    dataOdEdit.setText(day + "." +"0"+(month + 1) + "." + year);
                                }
                                else {
                                    dataOdEdit.setText(day + "." + (month + 1) + "." + year);
                                }
                                dayChangeNextDate();
                            }
                        }, year, month, day);
                        picker.show();
                widok=0;
                selectButton(dzienButton);

                return false;
            }
        });

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
