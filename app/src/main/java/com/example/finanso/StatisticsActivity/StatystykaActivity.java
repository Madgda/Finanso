package com.example.finanso.StatisticsActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.ListActivity.ListAdapter;
import com.example.finanso.R;
import com.example.finanso.SQLite.SqLiteManager;
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
   // private BarChart barChart;
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
    public SqLiteManager myDB;
    private ArrayList<String> lista_suma;
    private TextView wydatkiText, wplywyText;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<String> lista_data,lista_BarChartExpenses;
    public  ArrayList<BarEntry> rekordyC = new ArrayList<>();
    public  ArrayList<String> dniWyswietlane= new ArrayList<>();
    public  BarChart barChart;
    public float aXisEntries[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDB = new SqLiteManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statystyka);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
        loadPieChartData();


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
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        String output = sdf.format(c.getTime());
        dataDoEdit.setText(output);
        selectButton(dzienButton);
        widok=0;
        //statystyki
        getIncome();
        getExpense();
        barChart = findViewById(R.id.barChart);
        //barChart.set
        changeBarChartData();
      //  loadBarChartData();


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            switch (widok){
                case 0:
                    naxtOrPreviousDay(1);
                    dayChangeNextDate();
                    changeBarChartData();
                    break;
                case 1:
                    nextOrPreviousWeek(1);
                    weekChangeNextDate();
                    changeBarChartData();
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

                getIncome();
                getExpense();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            switch (widok){
                case 0:
                    naxtOrPreviousDay(0);
                    dayChangeNextDate();
                    changeBarChartData();
                    break;
                case 1:
                    nextOrPreviousWeek(0);
                    weekChangeNextDate();
                    changeBarChartData();
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

                getIncome();
                getExpense();
            }
        });

        dzienButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                dayChangeNextDate();
                widok=0;
                selectButton(dzienButton);
                getIncome();
                getExpense();
                changeBarChartData();

            }


        });
        tydzienButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                weekChangeNextDate();
                widok=1;
                selectButton(tydzienButton);
                getIncome();
                getExpense();
                changeBarChartData();

            }
        });
        miesiacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                monthChangeNextDate();
                widok=2;
                selectButton(miesiacButton);
                getIncome();
                getExpense();
            }
        });
        kwartalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                quarterChangeNextDate();
                widok=3;
                selectButton(kwartalButton);
                getIncome();
                getExpense();
            }
        });
        rokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckButtonsAll();
                yearChangeNextDate();
                widok=4;
                selectButton(rokButton);
                getIncome();
                getExpense();
            }
        });
    }



    private void getExpense() {
        String dataOd = dataOdEdit.getText().toString();
        //String dataDo = dataDoEdit.getText().toString();
        String dt = dataDoEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
                c.add(Calendar.DATE, -1);
        String dataDo = sdf.format(c.getTime());

        wydatkiText = findViewById(R.id.wartoscWydatki);
        //lista_suma = new ArrayList<>();
        myDB = new SqLiteManager(StatystykaActivity.this);
        lista_suma = new ArrayList<>();

        Cursor cursor = myDB.readSumOfExpensesForStatistics(dataOd,dataDo);
        if (cursor.getCount() == 0) {
            Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if(cursor.getString(0)!=null) {
                    lista_suma.add(cursor.getString(0));
                }
                else{
                    wydatkiText.setText("0.00 zł");
                }
            }
        }
        if(lista_suma.size()!=0){
        wydatkiText.setText(lista_suma.get(0)+" zł");
        }
        else{
            wydatkiText.setText("0.00 zł");
        }
    }
    private void getIncome() {
        String dataOd = dataOdEdit.getText().toString();

        String dt = dataDoEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        String dataDo = sdf.format(c.getTime());

        wplywyText = findViewById(R.id.wartoscWplywy);
        //lista_suma = new ArrayList<>();
       myDB = new SqLiteManager(StatystykaActivity.this);
            lista_suma = new ArrayList<>();

            Cursor cursor = myDB.readSumOfIncomeForStatistics(dataOd,dataDo);
            if (cursor.getCount() == 0) {
                Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    if(cursor.getString(0)!=null) {
                        lista_suma.add(cursor.getString(0));
                    }
                    else{
                        wplywyText.setText("0.00 zł");
                    }
                }
            }
        if(lista_suma.size()!=0){
            wplywyText.setText(lista_suma.get(0)+" zł");
        }
        else{
            wplywyText.setText("0.00 zł");
        }
    }

    private void loadBarChartData () {
        /*    ArrayList<BarEntry> rekordyB = new ArrayList<>();
            rekordyB.add(new BarEntry(0, 140f));
            rekordyB.add(new BarEntry(1, 420f));
            rekordyB.add(new BarEntry(2, 330f));
         *//*   rekordyB.add(new BarEntry(3, 550f));*//*
          *//*  rekordyB.add(new BarEntry(4, 920f));
            rekordyB.add(new BarEntry(5, 160f));
            rekordyB.add(new BarEntry(6, 213f));
            rekordyB.add(new BarEntry(7, 420f));
            rekordyB.add(new BarEntry(8, 330f));
            rekordyB.add(new BarEntry(9, 550f));
            rekordyB.add(new BarEntry(10, 920f));
            rekordyB.add(new BarEntry(11, 160f));
            rekordyB.add(new BarEntry(12, 213f));
            rekordyB.add(new BarEntry(13, 213f));*/
            /*ArrayList<BarEntry> rekordyA = new ArrayList<>();
            rekordyA.add(new BarEntry(0,150f));
            rekordyA.add(new BarEntry(1, 450f));
            rekordyA.add(new BarEntry(2, 350f));
            *//*rekordyA.add(new BarEntry(3, 0));*//*
          *//*  rekordyA.add(new BarEntry(3, 590f));
            rekordyA.add(new BarEntry(4, 950f));
            rekordyA.add(new BarEntry(5, 150f));
            rekordyA.add(new BarEntry(6, 253f));
            rekordyA.add(new BarEntry(7, 420f));
            rekordyA.add(new BarEntry(8, 330f));
            rekordyA.add(new BarEntry(9, 550f));
            rekordyA.add(new BarEntry(10, 920f));
            rekordyA.add(new BarEntry(11, 160f));
            rekordyA.add(new BarEntry(12, 213f));
            rekordyA.add(new BarEntry(13, 213f));*/
      /*      BarDataSet dataSetB = new BarDataSet(rekordyB, "Suma wydatków");
        dataSetB.setColor(getApplicationContext().getResources().getColor(R.color.greyGreen));

        BarDataSet dataSetA = new BarDataSet(rekordyA, "Suma wpływów");
        dataSetA.setColor(getApplicationContext().getResources().getColor(R.color.greyRed));
*/

        BarDataSet dataSetC = new BarDataSet(rekordyC, "Suma wpływów");
        dataSetC.setColor(getApplicationContext().getResources().getColor(R.color.LightPink));


 //        dniWyswietlane.add("Czw");
        if(dniWyswietlane.size()==0){
            dniWyswietlane.add("Pon");
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
       // xAxis.mEntries=new float[]{};
        aXisEntries=xAxis.mEntries;

        ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if(value>=dniWyswietlane.size()){
                        return null;

                    }
                    else {
                        return dniWyswietlane.get((int) value);
                    }
                }
            };
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(formatter);


       /*     dataSetB.setBarBorderColor(Color.GREEN);
            dataSetB.setValueTextColor(Color.BLACK);
            dataSetB.setValueTextSize(10f);
            dataSetB.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSetA.setBarBorderColor(Color.RED);
            dataSetA.setValueTextColor(Color.BLACK);
            dataSetA.setValueTextSize(10f);
          dataSetA.setAxisDependency(YAxis.AxisDependency.RIGHT);*/
        dataSetC.setBarBorderColor(Color.RED);
            dataSetC.setValueTextColor(Color.BLACK);
        dataSetC.setValueTextSize(10f);
        dataSetC.setAxisDependency(YAxis.AxisDependency.RIGHT);
          //  BarData data = new BarData(dataSetB,dataSetA);
            BarData data = new BarData(dataSetC);
            data.setBarWidth(0.45f);
            //data.groupBars(-0.5f, 0.06f, 0.02f);
            barChart.getDescription().setEnabled(false);
            barChart.setData(data);

            // barChart.setTouchEnabled(true);
            barChart.setDoubleTapToZoomEnabled(false);
            //     barChart.setDragEnabled(true);
            //   barChart.setScaleEnabled(true);


            //barChart.animateY(1400, Easing.EaseInOutQuad);
        }
        private void setupPieChart () {
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
        private void selectButton (Button button){
            Button tempButton = button;
            uncheckButtonsAll();
            button.setBackgroundColor(getColor(R.color.BlueDarkest));
            button.setTextColor(getColor(R.color.grey));

        }
        private void loadPieChartData () {
            ArrayList<PieEntry> rekordyP = new ArrayList<>();
            rekordyP.add(new PieEntry(0.3f, "Spożywcze"));
            rekordyP.add(new PieEntry(0.35f, "Rachunki"));
            rekordyP.add(new PieEntry(0.1f, "Prezenty"));
            rekordyP.add(new PieEntry(0.1f, "Chemia"));
            rekordyP.add(new PieEntry(0.1f, "Remont"));
            rekordyP.add(new PieEntry(0.15f, "Inne"));

            ArrayList<Integer> colors = new ArrayList<>();
            for (int color : ColorTemplate.MATERIAL_COLORS) {
                colors.add(color);
            }

            for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(color);
            }
            PieDataSet dataSet = new PieDataSet(rekordyP, "Kategoria");
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
        private void uncheckButtonsAll () {
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

        private void naxtOrPreviousDay (Integer way)
        //way=0 previous
        //way=1 next
        {
            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (way) {
                case 0:
                    c.add(Calendar.DATE, -1);
                    break;

                case 1:
                    c.add(Calendar.DATE, 1);
                    break;

                default:
                    break;
            }
            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);
        }

        private void nextOrPreviousWeek (Integer way)
        //way=0 previous
        //way=1 next
        {
            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (way) {
                case 0:
                    c.add(Calendar.WEEK_OF_MONTH, -1);
                    break;

                case 1:
                    c.add(Calendar.WEEK_OF_MONTH, 1);
                    break;

                default:
                    break;
            }
            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);
        }
        private void nextOrPreviousMonth (Integer way)
        //way=0 previous
        //way=1 next
        {

            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (way) {
                case 0:
                    c.add(Calendar.MONTH, -1);
                    break;

                case 1:
                    c.add(Calendar.MONTH, 1);
                    break;

                default:
                    break;
            }
            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);
        }
        private void nextOrPreviousQuarter (Integer way)
        //way=0 previous
        //way=1 next
        {

            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (way) {
                case 0:
                    c.add(Calendar.MONTH, -3);
                    break;

                case 1:
                    c.add(Calendar.MONTH, 3);
                    break;

                default:
                    break;
            }
            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);
        }
        private void nextOrPreviousYear (Integer way)
        //way=0 previous
        //way=1 next
        {

            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (way) {
                case 0:
                    c.add(Calendar.YEAR, -1);
                    break;

                case 1:
                    c.add(Calendar.YEAR, 1);
                    break;

                default:
                    break;
            }
            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);
        }

        private void dayChangeNextDate () {
            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, 1);
            String output = sdf.format(c.getTime());
            dataDoEdit.setText(output);
        }
        private void weekChangeNextDate () {
            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.set(Calendar.DAY_OF_WEEK, 2);

            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);

            c.add(Calendar.WEEK_OF_MONTH, 1);
            output = sdf.format(c.getTime());
            dataDoEdit.setText(output);
        }

        private void monthChangeNextDate () {
            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.set(Calendar.DAY_OF_MONTH, 1);

            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);

            c.add(Calendar.MONTH, 1);
            output = sdf.format(c.getTime());
            dataDoEdit.setText(output);
        }

        private void quarterChangeNextDate () {
            String dt = dataOdEdit.getText().toString(); // Start date
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

            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);

            c.add(Calendar.MONTH, 3);
            output = sdf.format(c.getTime());
            dataDoEdit.setText(output);
        }
        private void yearChangeNextDate () {
            String dt = dataOdEdit.getText().toString(); // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.set(Calendar.DAY_OF_YEAR, 1);

            String output = sdf.format(c.getTime());
            dataOdEdit.setText(output);

            c.add(Calendar.YEAR, 1);
            output = sdf.format(c.getTime());
            dataDoEdit.setText(output);
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_toolbar_statystyka, menu);
            wybierzDateMenuItem = menu.findItem(R.id.item2);
            ustawDzisDataMenuItem = menu.findItem(R.id.item3);
            wybierzDateMenuItem.setTitle(Html.fromHtml("<font color='#000000'>Wybierz datę początkową</font>"));
            ustawDzisDataMenuItem.setTitle(Html.fromHtml("<font color='#000000'>Ustaw dzisiejszą date</font>"));

            ustawDzisDataMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    dataOdEdit.setText(currentDate);
                    dayChangeNextDate();
                    widok = 0;
                    getIncome();
                    getExpense();
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
                            if (day < 10 && month < 9) {
                                dataOdEdit.setText(year + "-0" +(month + 1) + "-0" +day);
                            } else if (day < 10) {
                                dataOdEdit.setText(year + "-" +(month + 1) + "-0" +day);
                            } else if (month < 9) {
                                dataOdEdit.setText(year + "-0" +(month + 1) + "-" +day);                            } else if (month < 9) {
                            } else {
                                dataOdEdit.setText(year + "-" +(month + 1) + "-" +day);
                            }
                            dayChangeNextDate();
                        }
                    }, year, month, day);
                    picker.show();
                    widok = 0;
                    selectButton(dzienButton);

                    return false;
                }
            });

            return true;
        }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){

            switch (item.getItemId()) {

                case R.id.menuDodajLista:


                    return true;

                default:
                    return super.onOptionsItemSelected(item);

            }
        }

        private void changeBarChartData(){
        String dataFind;
            String dataOd = dataOdEdit.getText().toString();
            String dataDo = dataDoEdit.getText().toString();
            //String dataDo = dataDoEdit.getText().toString();
            //ArrayList<String> dniWyswietlane = new ArrayList<>();
            dataDo=getPreviousDay(dataDo);
            dataFind=dataOd;
            Double suma=0.00;
            Double kwotaIteracja=0.00;
            myDB = new SqLiteManager(StatystykaActivity.this);
            lista_BarChartExpenses = new ArrayList<>();
            lista_suma = new ArrayList<>();
            lista_data = new ArrayList<>();
            lista_BarChartExpenses.clear();
            rekordyC.clear();
            dniWyswietlane.clear();
            //barChart.clearValues();
            barChart.clear();
            barChart.notifyDataSetChanged();
            barChart.invalidate();

            Cursor cursor = myDB.readDataForBarChartStatistics(dataOd,dataDo);
            aXisEntries=new float[]{};

            switch (widok){
                case 0:
                      if (cursor.getCount() == 0) {
                          Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
                       //   rekordyC.add(new BarEntry(0,0));


                      } else {
                          while (cursor.moveToNext()) {
                              if(cursor.getString(0)!=null) {
                                  lista_suma.add(cursor.getString(0));
                                   lista_data.add(cursor.getString(1));
                               }
                               else{
                              //    wydatkiText.setText("0.00 zł");
                              }
                           }
                       }
                          for(int i=0;i<lista_data.size();i++){
                              String dataT=lista_data.get(i);
                              if(dataT.equals(dataFind)){
                                  kwotaIteracja=Double.valueOf(lista_suma.get(i));
                                  suma= suma+kwotaIteracja;
                              }
                          }
                       rekordyC.add(new BarEntry(0,Float.valueOf(String.valueOf(suma))));

                          String dayOfWeekName= getWeekName(dataOd);
                    dniWyswietlane.add(dayOfWeekName);
                    break;
                case 1:
                      if (cursor.getCount() == 0) {
                          Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
                          rekordyC.add(new BarEntry(0,0));
                          rekordyC.add(new BarEntry(1,0));
                          rekordyC.add(new BarEntry(2,0));
                          rekordyC.add(new BarEntry(3,0));
                          rekordyC.add(new BarEntry(4,0));
                          rekordyC.add(new BarEntry(5,0));
                          rekordyC.add(new BarEntry(6,0));
                      } else {
                          while (cursor.moveToNext()) {
                              if(cursor.getString(0)!=null) {
                                  lista_suma.add(cursor.getString(0));
                                   lista_data.add(cursor.getString(1));
                               }
                               else{
                              //    wydatkiText.setText("0.00 zł");
                              }
                           }
                       }
                      dataDo= getNextDay(dataDo);
                      Integer iteration=0;
                            while(!dataFind.equals(dataDo) && iteration<7) {
                                suma=0.0;
                                for (int i = 0; i < lista_data.size(); i++) {
                                    String dataT = lista_data.get(i);
                                    if (dataT.equals(dataFind)) {
                                        kwotaIteracja = Double.valueOf(lista_suma.get(i));
                                        suma =  kwotaIteracja;
                                    }
                                }
                                if (suma.equals(0.00) || suma.equals(0) || suma.equals(0.0)){
                                    rekordyC.add(new BarEntry(iteration,0));
                                }
                                else{
                                    rekordyC.add(new BarEntry(iteration,Float.valueOf(String.valueOf(suma))));
                                }
                                dataFind=getNextDay(dataFind);
                                iteration++;
                            }
                    dniWyswietlane.add("Pon");
                    dniWyswietlane.add("Wt");
                    dniWyswietlane.add("Śr");
                    dniWyswietlane.add("Czw");
                    dniWyswietlane.add("Pt");
                    dniWyswietlane.add("Sb");
                    dniWyswietlane.add("Nd");
                break;

            }
            loadBarChartData();
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }

    private String getWeekName(String dataIn) {
        Calendar k = Calendar.getInstance();
        try {
            //c= sdf.parse(dataIn);
            k.setTime(sdf.parse(dataIn));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(StatystykaActivity.this, "Błąd daty!", Toast.LENGTH_SHORT).show();
        }
        //c=Calendar.getInstance()
        int dayOfWeek = k.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekName="";
        switch(dayOfWeek){
            case 2:
                dayOfWeekName="Pn";
                break;
            case 3:
                dayOfWeekName="Wt";
                break;
            case 4:
                dayOfWeekName="Śr";
                break;
            case 5:
                dayOfWeekName="Cz";
                break;
            case 6:
                dayOfWeekName="Pt";
                break;
            case 7:
                dayOfWeekName="Sb";
                break;
            case 1:
                dayOfWeekName="Nd";
                break;
        }
        return dayOfWeekName;
    }

    private String getNextDay(String dataBefore){
            String dataAfter="";
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dataBefore));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, 1);
             dataAfter = sdf.format(c.getTime());
            return dataAfter;
        }
        private String getPreviousDay(String dataBefore){
            String dataAfter="";
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dataBefore));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, -1);
             dataAfter = sdf.format(c.getTime());
            return dataAfter;
        }
    }

