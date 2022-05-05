package com.example.finanso.StatisticsActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "deprecation", "UnnecessaryLocalVariable", "DuplicateCondition"})
public class StatystykaActivity extends AppCompatActivity {

    private PieChart pieChart;
    public Button dzienButton;
    public Button tydzienButton;
    private Button miesiacButton;
    private Button kwartalButton;
    private Button rokButton;
    private TextView dataOdEdit;
    private TextView dataDoEdit;
    private Integer widok;//0-dzień, 1-tydzień, 2-miesiąc, 3-kwartał, 4-rok
    public SqLiteManager myDB;
    private ArrayList<String> lista_suma;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<String> lista_data;
    public ArrayList<BarEntry> rekordyB = new ArrayList<>();
    public ArrayList<BarEntry> rekordyA = new ArrayList<>();
    public ArrayList<PieEntry> rekordyP = new ArrayList<>();
    public ArrayList<String> dniWyswietlane = new ArrayList<>();
    public BarChart barChart;
    public float[] aXisEntries;


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

        Button backButton = findViewById(R.id.backButton);
        Button nextButton = findViewById(R.id.nextButton);
        dzienButton = findViewById(R.id.dzienButton);
        tydzienButton = findViewById(R.id.tydzienButton);
        miesiacButton = findViewById(R.id.miesiacButton);
        kwartalButton = findViewById(R.id.kwartalButton);
        rokButton = findViewById(R.id.rokButton);
        dataOdEdit = findViewById(R.id.dataOdText);
        dataDoEdit = findViewById(R.id.dataDoText);

        dataOdEdit.setText(currentDate);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(currentDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        String output = sdf.format(c.getTime());
        dataDoEdit.setText(output);
        selectButton(dzienButton);
        widok = 0;
        //statystyki
        getIncome();
        getExpense();
        barChart = findViewById(R.id.barChart);
        //barChart.set
        changeBarChartData();
        changePieChartData();
        //  loadBarChartData();


        nextButton.setOnClickListener(view -> {
            switch (widok) {
                case 0:
                    naxtOrPreviousDay(1);
                    dayChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 1:
                    nextOrPreviousWeek(1);
                    weekChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 2:
                    nextOrPreviousMonth(1);
                    monthChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 3:
                    nextOrPreviousQuarter(1);
                    quarterChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 4:
                    nextOrPreviousYear(1);
                    yearChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;

            }

            getIncome();
            getExpense();
        });
        backButton.setOnClickListener(view -> {
            switch (widok) {
                case 0:
                    naxtOrPreviousDay(0);
                    dayChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 1:
                    nextOrPreviousWeek(0);
                    weekChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 2:
                    nextOrPreviousMonth(0);
                    monthChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 3:
                    nextOrPreviousQuarter(0);
                    quarterChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;
                case 4:
                    nextOrPreviousYear(0);
                    yearChangeNextDate();
                    changeBarChartData();
                    changePieChartData();
                    break;

            }

            getIncome();
            getExpense();
        });

        dzienButton.setOnClickListener(view -> {
            uncheckButtonsAll();
            dayChangeNextDate();
            widok = 0;
            selectButton(dzienButton);
            getIncome();
            getExpense();
            changeBarChartData();
            changePieChartData();

        });
        tydzienButton.setOnClickListener(view -> {
            uncheckButtonsAll();
            weekChangeNextDate();
            widok = 1;
            selectButton(tydzienButton);
            getIncome();
            getExpense();
            changeBarChartData();
            changePieChartData();

        });
        miesiacButton.setOnClickListener(view -> {
            uncheckButtonsAll();
            monthChangeNextDate();
            widok = 2;
            selectButton(miesiacButton);
            getIncome();
            getExpense();
            changeBarChartData();
            changePieChartData();
        });
        kwartalButton.setOnClickListener(view -> {
            uncheckButtonsAll();
            quarterChangeNextDate();
            widok = 3;
            selectButton(kwartalButton);
            getIncome();
            getExpense();
            changeBarChartData();
            changePieChartData();
        });
        rokButton.setOnClickListener(view -> {
            uncheckButtonsAll();
            yearChangeNextDate();
            widok = 4;
            selectButton(rokButton);
            getIncome();
            getExpense();
            changeBarChartData();
            changePieChartData();
        });
    }


    @SuppressLint("SetTextI18n")
    private void getExpense() {
        String dataOd = dataOdEdit.getText().toString();
        //String dataDo = dataDoEdit.getText().toString();
        String dt = dataDoEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        String dataDo = sdf.format(c.getTime());

        TextView wydatkiText = findViewById(R.id.wartoscWydatki);
        //lista_suma = new ArrayList<>();
        myDB = new SqLiteManager(StatystykaActivity.this);
        lista_suma = new ArrayList<>();

        Cursor cursor = myDB.readSumOfExpensesForStatistics(dataOd, dataDo);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(0) != null) {
                    lista_suma.add(cursor.getString(0));
                } else {
                    wydatkiText.setText("0.00 zł");
                }
            }
        }
        if (lista_suma.size() != 0) {
            wydatkiText.setText(lista_suma.get(0) + " zł");
        } else {
            wydatkiText.setText("0.00 zł");
        }
    }

    @SuppressLint("SetTextI18n")
    private void getIncome() {
        String dataOd = dataOdEdit.getText().toString();

        String dt = dataDoEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        String dataDo = sdf.format(c.getTime());

        TextView wplywyText = findViewById(R.id.wartoscWplywy);
        //lista_suma = new ArrayList<>();
        myDB = new SqLiteManager(StatystykaActivity.this);
        lista_suma = new ArrayList<>();

        Cursor cursor = myDB.readSumOfIncomeForStatistics(dataOd, dataDo);
        if (cursor.getCount() == 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(0) != null) {
                    lista_suma.add(cursor.getString(0));
                } else {
                    wplywyText.setText("0.00 zł");
                }
            }
        }
        if (lista_suma.size() != 0) {
            wplywyText.setText(lista_suma.get(0) + " zł");
        } else {
            wplywyText.setText("0.00 zł");
        }
    }

    private void loadBarChartData() {
        BarDataSet dataSetB = new BarDataSet(rekordyB, "Wpływy");
        BarDataSet dataSetA = new BarDataSet(rekordyA, "Wydatki");
        dataSetB.setColor(getApplicationContext().getResources().getColor(R.color.greyGreen));
        dataSetA.setColor(getApplicationContext().getResources().getColor(R.color.greyRed));

        if (dniWyswietlane.size() == 0) {
            dniWyswietlane.add("Pon");
        }
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        aXisEntries = xAxis.mEntries;

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= dniWyswietlane.size()) {
                    return null;

                } else {
                    return dniWyswietlane.get((int) value);
                }
            }
        };
        xAxis.setTextSize(8f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(formatter);

        dataSetA.setValueTextColor(Color.BLACK);
        dataSetB.setValueTextColor(Color.BLACK);
        dataSetA.setValueTextSize(13f);
        dataSetB.setValueTextSize(13f);
        dataSetA.setDrawValues(false);
        dataSetB.setDrawValues(false);
        dataSetA.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSetB.setAxisDependency(YAxis.AxisDependency.RIGHT);
        BarData data = new BarData(dataSetB, dataSetA);
        //      BarData data = new BarData(dataSetC);
        data.setBarWidth(0.48f);
        data.groupBars(-0.5f, 0.05f, 0.00f);
        barChart.getDescription().setEnabled(false);
        barChart.setData(data);
        barChart.setDoubleTapToZoomEnabled(false);
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        //pieChart.getDescription().setTextColor(Color.BLACK);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    private void selectButton(Button button) {
        uncheckButtonsAll();
        button.setBackgroundColor(getColor(R.color.BlueDarkest));
        button.setTextColor(getColor(R.color.grey));

    }

    private void loadPieChartData() {
        //  ArrayList<PieEntry> rekordyP = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }
        PieDataSet dataSet = new PieDataSet(rekordyP, "Top 6 Kategorii");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void uncheckButtonsAll() {
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
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void nextOrPreviousWeek(Integer way)
    //way=0 previous
    //way=1 next
    {
        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void nextOrPreviousMonth(Integer way)
    //way=0 previous
    //way=1 next
    {

        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void nextOrPreviousQuarter(Integer way)
    //way=0 previous
    //way=1 next
    {

        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void nextOrPreviousYear(Integer way)
    //way=0 previous
    //way=1 next
    {

        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void dayChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        String output = sdf.format(c.getTime());
        dataDoEdit.setText(output);
    }

    private void weekChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void monthChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void quarterChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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

    private void yearChangeNextDate() {
        String dt = dataOdEdit.getText().toString(); // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dt)));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_statystyka, menu);
        MenuItem wybierzDateMenuItem = menu.findItem(R.id.item2);
        MenuItem ustawDzisDataMenuItem = menu.findItem(R.id.item3);
        wybierzDateMenuItem.setTitle(Html.fromHtml("<font color='#000000'>Wybierz datę początkową</font>"));
        ustawDzisDataMenuItem.setTitle(Html.fromHtml("<font color='#000000'>Ustaw dzisiejszą date</font>"));

        ustawDzisDataMenuItem.setOnMenuItemClickListener(menuItem -> {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            dataOdEdit.setText(currentDate);
            dayChangeNextDate();
            widok = 0;
            selectButton(dzienButton);
            changeBarChartData();
            changePieChartData();

            return false;
        });
        wybierzDateMenuItem.setOnMenuItemClickListener(menuItem -> {

            final Calendar kalendarz = Calendar.getInstance();
            int day = kalendarz.get(Calendar.DAY_OF_MONTH);
            int month = kalendarz.get(Calendar.MONTH);
            int year = kalendarz.get(Calendar.YEAR);
            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);
            @SuppressLint("SetTextI18n") DatePickerDialog picker = new DatePickerDialog(StatystykaActivity.this, (datePicker, year1, month1, day1) -> {
                if (day1 < 10 && month1 < 9) {
                    dataOdEdit.setText(year1 + "-0" + (month1 + 1) + "-0" + day1);
                } else if (day1 < 10) {
                    dataOdEdit.setText(year1 + "-" + (month1 + 1) + "-0" + day1);
                } else if (month1 < 9) {
                    dataOdEdit.setText(year1 + "-0" + (month1 + 1) + "-" + day1);
                } else {
                    dataOdEdit.setText(year1 + "-" + (month1 + 1) + "-" + day1);
                }
                dayChangeNextDate();
            }, year, month, day);
            picker.show();
            widok = 0;
            selectButton(dzienButton);

            return false;
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuDodajLista) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeBarChartData() {
        String dataOd = dataOdEdit.getText().toString();
        String dataDo = dataDoEdit.getText().toString();
        dataDo = getPreviousDay(dataDo);
        myDB = new SqLiteManager(StatystykaActivity.this);
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();
        rekordyA.clear();
        rekordyB.clear();
        dniWyswietlane.clear();
        barChart.clear();
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        aXisEntries = new float[]{};

        switch (widok) {
            case 0://dzień
                dayBarChartExpenses(dataOd, dataDo);
                dayBarChartIncomes(dataOd, dataDo);
                String dayOfWeekName = getWeekName(dataOd);
                dniWyswietlane.add(dayOfWeekName);
                break;
            case 1://tydzień
                weekBarChartExpenses(dataOd, dataDo);
                weekBarChartIncomes(dataOd, dataDo);
                dniWyswietlane.add("Pon");
                dniWyswietlane.add("Wt");
                dniWyswietlane.add("Śr");
                dniWyswietlane.add("Czw");
                dniWyswietlane.add("Pt");
                dniWyswietlane.add("Sb");
                dniWyswietlane.add("Nd");
                break;

            case 2://miesiac
                monthBarChartExpenses(dataOd, dataDo);
                monthBarChartIncomes(dataOd, dataDo);
                addWeeksLabels();
                break;

            case 3://kwartał
                quarterBarChartExpenses(dataOd, dataDo);
                quarterBarChartIncomes(dataOd, dataDo);
                addXMonthNames(3);


                break;
            case 4://rok
                yearBarChartExpenses(dataOd, dataDo);
                yearBarChartIncomes(dataOd, dataDo);
                addXMonthNames(12);

                break;

        }
        loadBarChartData();
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    private void changePieChartData() {

        String dataOd = dataOdEdit.getText().toString();
        String dataDo = dataDoEdit.getText().toString();
        lista_suma = new ArrayList<>();
        ArrayList<String> lista_kategoria;
        rekordyP.clear();
        pieChart.clearValues();
        pieChart.clear();
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
        myDB = new SqLiteManager(StatystykaActivity.this);
        Cursor cursor = myDB.readDataForPieChartStatistics(dataOd, dataDo);
        double sumaAllKategorie = 0.00;
        double kwotaIteracja;
        lista_kategoria = new ArrayList<>();
        lista_suma = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(0) != null) {
                    lista_kategoria.add(cursor.getString(0));
                    lista_suma.add(cursor.getString(1));
                }
            }
        }
        for (int i = 0; i < lista_kategoria.size(); i++) {
            kwotaIteracja = Double.parseDouble(lista_suma.get(i));
            sumaAllKategorie = sumaAllKategorie + kwotaIteracja;
        }
        for (int i = 0; i < lista_kategoria.size(); i++) {
            kwotaIteracja = Double.parseDouble(lista_suma.get(i));
            float procent = ((float) kwotaIteracja / (float) sumaAllKategorie);
            rekordyP.add(new PieEntry(procent, lista_kategoria.get(i)));

        }
        if (lista_kategoria.size() == 0) {
            rekordyP.add(new PieEntry(1, "Brak danych na wybrany okres czasu"));
        }

        loadPieChartData();
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    private void yearBarChartExpenses(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readExpensesDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        String dataNextMonth;
        Double suma;
        double kwotaIteracja;
        int iteration;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            // Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            rekordyA.add(new BarEntry(0, 0));
            rekordyA.add(new BarEntry(1, 0));
            rekordyA.add(new BarEntry(2, 0));
            rekordyA.add(new BarEntry(3, 0));
            rekordyA.add(new BarEntry(4, 0));
            rekordyA.add(new BarEntry(5, 0));
            rekordyA.add(new BarEntry(6, 0));
            rekordyA.add(new BarEntry(7, 0));
            rekordyA.add(new BarEntry(8, 0));
            rekordyA.add(new BarEntry(9, 0));
            rekordyA.add(new BarEntry(10, 0));
            rekordyA.add(new BarEntry(11, 0));

        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }
            }
        }
        dataDo = getNextDay(dataDo);

        iteration = 0;
        // dataDo= getNextDay(dataDo);
        dataNextMonth = getNextMonth(dataFind);

        do {
            suma = 0.0;
            while (!dataFind.equals(dataNextMonth)) {
                for (int i = 0; i < lista_data.size(); i++) {
                    String dataT = lista_data.get(i);
                    if (dataT.equals(dataFind)) {
                        kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                        suma = suma + kwotaIteracja;
                    }
                }

                if (!dataFind.equals(dataDo)) {
                    dataFind = getNextDay(dataFind);
                }
            }


            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyA.add(new BarEntry(iteration, 0));
            } else {
                rekordyA.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            iteration++;
            if (!dataNextMonth.equals(dataDo)) {
                dataNextMonth = getNextMonth(dataNextMonth);
            }


        } while (!dataFind.equals(dataDo));
    }

    private void yearBarChartIncomes(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readIncomeDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        String dataNextMonth;
        Double suma;
        double kwotaIteracja;
        int iteration;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            //  Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            rekordyB.add(new BarEntry(0, 0));
            rekordyB.add(new BarEntry(1, 0));
            rekordyB.add(new BarEntry(2, 0));
            rekordyB.add(new BarEntry(3, 0));
            rekordyB.add(new BarEntry(4, 0));
            rekordyB.add(new BarEntry(5, 0));
            rekordyB.add(new BarEntry(6, 0));
            rekordyB.add(new BarEntry(7, 0));
            rekordyB.add(new BarEntry(8, 0));
            rekordyB.add(new BarEntry(9, 0));
            rekordyB.add(new BarEntry(10, 0));
            rekordyB.add(new BarEntry(11, 0));

        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }  //    wydatkiText.setText("0.00 zł");

            }
        }
        dataDo = getNextDay(dataDo);

        iteration = 0;
        // dataDo= getNextDay(dataDo);
        dataNextMonth = getNextMonth(dataFind);

        do {
            suma = 0.0;

            while (!dataFind.equals(dataNextMonth)) {
                for (int i = 0; i < lista_data.size(); i++) {
                    String dataT = lista_data.get(i);
                    if (dataT.equals(dataFind)) {
                        kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                        suma = suma + kwotaIteracja;
                    }
                }

                if (!dataFind.equals(dataDo)) {
                    dataFind = getNextDay(dataFind);
                }
            }


            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyB.add(new BarEntry(iteration, 0));
            } else {
                rekordyB.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            iteration++;
            if (!dataNextMonth.equals(dataDo)) {
                dataNextMonth = getNextMonth(dataNextMonth);
            }


        } while (!dataFind.equals(dataDo));
    }

    private void quarterBarChartExpenses(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readExpensesDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        String dataNextMonth;
        Double suma;
        double kwotaIteracja;
        int iteration;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            //  Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            rekordyA.add(new BarEntry(0, 0));
            rekordyA.add(new BarEntry(1, 0));
            rekordyA.add(new BarEntry(2, 0));

        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }  //    wydatkiText.setText("0.00 zł");

            }
        }
        dataDo = getNextDay(dataDo);
        iteration = 0;
        // dataDo= getNextDay(dataDo);
        dataNextMonth = getNextMonth(dataFind);

        do {
            suma = 0.0;
            //  dataFind = getNextDay(dataFind);
            while (!dataFind.equals(dataNextMonth)) {
                for (int i = 0; i < lista_data.size(); i++) {
                    String dataT = lista_data.get(i);
                    if (dataT.equals(dataFind)) {
                        kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                        suma = suma + kwotaIteracja;
                    }
                }

                if (!dataFind.equals(dataDo)) {
                    dataFind = getNextDay(dataFind);
                }
            }


            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyA.add(new BarEntry(iteration, 0));
            } else {
                rekordyA.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            iteration++;
            if (!dataNextMonth.equals(dataDo)) {
                dataNextMonth = getNextMonth(dataNextMonth);
            }


        } while (!dataFind.equals(dataDo));

    }

    private void quarterBarChartIncomes(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readIncomeDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        String dataNextMonth;
        Double suma;
        double kwotaIteracja;
        int iteration;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            //  Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            rekordyB.add(new BarEntry(0, 0));
            rekordyB.add(new BarEntry(1, 0));
            rekordyB.add(new BarEntry(2, 0));

        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }

            }
        }
        dataDo = getNextDay(dataDo);
        iteration = 0;
        // dataDo= getNextDay(dataDo);
        dataNextMonth = getNextMonth(dataFind);

        do {
            suma = 0.0;
            while (!dataFind.equals(dataNextMonth)) {
                for (int i = 0; i < lista_data.size(); i++) {
                    String dataT = lista_data.get(i);
                    if (dataT.equals(dataFind)) {
                        kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                        suma = suma + kwotaIteracja;
                    }
                }

                if (!dataFind.equals(dataDo)) {
                    dataFind = getNextDay(dataFind);
                }
            }


            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyB.add(new BarEntry(iteration, 0));
            } else {
                rekordyB.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            iteration++;
            if (!dataNextMonth.equals(dataDo)) {
                dataNextMonth = getNextMonth(dataNextMonth);
            }


        } while (!dataFind.equals(dataDo));

    }

    private void monthBarChartExpenses(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readExpensesDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        Double suma;
        double kwotaIteracja;
        int iteration;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            // Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            addWeeksLabels();
        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }  //    wydatkiText.setText("0.00 zł");

            }
        }
        dataDo = getNextDay(dataDo);
        iteration = 0;

        while (!dataFind.equals(dataDo)) {
            suma = 0.0;
            for (int j = 0; j < 7 && !dataFind.equals(dataDo); j++) {
                for (int i = 0; i < lista_data.size(); i++) {
                    String dataT = lista_data.get(i);
                    if (dataT.equals(dataFind)) {
                        kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                        suma = suma + kwotaIteracja;
                    }
                }

                dataFind = getNextDay(dataFind);

            }


            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyA.add(new BarEntry(iteration, 0));
            } else {
                rekordyA.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            iteration++;


        }
    }

    private void monthBarChartIncomes(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readIncomeDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        Double suma;
        double kwotaIteracja;
        int iteration;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            //    Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            addWeeksLabels();
        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }
            }
        }
        dataDo = getNextDay(dataDo);
        iteration = 0;
        while (!dataFind.equals(dataDo)) {
            suma = 0.0;
            for (int j = 0; j < 7 && !dataFind.equals(dataDo); j++) {
                for (int i = 0; i < lista_data.size(); i++) {
                    String dataT = lista_data.get(i);
                    if (dataT.equals(dataFind)) {
                        kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                        suma = suma + kwotaIteracja;
                    }
                }

                dataFind = getNextDay(dataFind);

            }


            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyB.add(new BarEntry(iteration, 0));
            } else {
                rekordyB.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            iteration++;


        }
    }

    private void weekBarChartExpenses(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readExpensesDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        Double suma;
        double kwotaIteracja;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            //Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            rekordyA.add(new BarEntry(0, 0));
            rekordyA.add(new BarEntry(1, 0));
            rekordyA.add(new BarEntry(2, 0));
            rekordyA.add(new BarEntry(3, 0));
            rekordyA.add(new BarEntry(4, 0));
            rekordyA.add(new BarEntry(5, 0));
            rekordyA.add(new BarEntry(6, 0));
        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }  //    wydatkiText.setText("0.00 zł");

            }
        }
        dataDo = getNextDay(dataDo);
        int iteration;
        iteration = 0;
        while (!dataFind.equals(dataDo) && iteration < 7) {
            suma = 0.0;
            for (int i = 0; i < lista_data.size(); i++) {
                String dataT = lista_data.get(i);
                if (dataT.equals(dataFind)) {
                    kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                    suma = kwotaIteracja;
                }
            }
            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyA.add(new BarEntry(iteration, 0));
            } else {
                rekordyA.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            dataFind = getNextDay(dataFind);
            iteration++;
        }
    }

    private void weekBarChartIncomes(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readIncomeDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        Double suma;
        double kwotaIteracja;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() == 0) {
            // Toast.makeText(StatystykaActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
            rekordyB.add(new BarEntry(0, 0));
            rekordyB.add(new BarEntry(1, 0));
            rekordyB.add(new BarEntry(2, 0));
            rekordyB.add(new BarEntry(3, 0));
            rekordyB.add(new BarEntry(4, 0));
            rekordyB.add(new BarEntry(5, 0));
            rekordyB.add(new BarEntry(6, 0));
        } else {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }  //    wydatkiText.setText("0.00 zł");

            }
        }
        dataDo = getNextDay(dataDo);
        int iteration;
        iteration = 0;
        while (!dataFind.equals(dataDo) && iteration < 7) {
            suma = 0.0;
            for (int i = 0; i < lista_data.size(); i++) {
                String dataT = lista_data.get(i);
                if (dataT.equals(dataFind)) {
                    kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                    suma = kwotaIteracja;
                }
            }
            if (suma.equals(0.00) || suma.equals(0.0)) {
                rekordyB.add(new BarEntry(iteration, 0));
            } else {
                rekordyB.add(new BarEntry(iteration, Float.parseFloat(String.valueOf(suma))));
            }
            dataFind = getNextDay(dataFind);
            iteration++;
        }
    }

    private void dayBarChartExpenses(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readExpensesDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        Double suma = 0.00;
        double kwotaIteracja;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() != 0) {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }
            }
        }
        for (int i = 0; i < lista_data.size(); i++) {
            String dataT = lista_data.get(i);
            if (dataT.equals(dataFind)) {
                kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                suma = suma + kwotaIteracja;
            }
        }
        rekordyA.add(new BarEntry(0, Float.parseFloat(String.valueOf(suma))));

    }

    private void dayBarChartIncomes(String dataOd, String dataDo) {
        Cursor cursorE = myDB.readIncomeDataForBarChartStatistics(dataOd, dataDo);
        String dataFind = dataOd;
        Double suma = 0.00;
        double kwotaIteracja;
        lista_suma = new ArrayList<>();
        lista_data = new ArrayList<>();

        if (cursorE.getCount() != 0) {
            while (cursorE.moveToNext()) {
                if (cursorE.getString(0) != null) {
                    lista_suma.add(cursorE.getString(0));
                    lista_data.add(cursorE.getString(1));
                }
            }
        }
        for (int i = 0; i < lista_data.size(); i++) {
            String dataT = lista_data.get(i);
            if (dataT.equals(dataFind)) {
                kwotaIteracja = Double.parseDouble(lista_suma.get(i));
                suma = suma + kwotaIteracja;
            }
        }
        rekordyB.add(new BarEntry(0, Float.parseFloat(String.valueOf(suma))));

    }


    private void addXMonthNames(Integer X) {
        String dataForMonth = dataOdEdit.getText().toString();

        for (int i = 0; i < X; i++) {
            String nameOfMonth = getMonthName(dataForMonth);
            dniWyswietlane.add(nameOfMonth);
            dataForMonth = getNextMonth(dataForMonth);

        }

    }

    private void addWeeksLabels() {
        String dataOd = dataOdEdit.getText().toString();
        String dataDo = dataDoEdit.getText().toString();
        String startWeek = dataOd;
        String endWeek = getNextWeek(dataOd);
        if (getYearNumber(endWeek) == getYearNumber(dataDo)) {//rok aktualny
            while (getDayOYearNumber(endWeek) <= getDayOYearNumber(dataDo)) {
                dniWyswietlane.add(getDayOMonthNumber(startWeek) + " - " + getDayOMonthNumber(getPreviousDay(endWeek)));
                startWeek = endWeek;
                endWeek = getNextWeek(endWeek);
            }
            if (getDayOYearNumber(endWeek) > getDayOYearNumber(dataDo)) {
                dniWyswietlane.add(getDayOMonthNumber(startWeek) + " - " + getDayOMonthNumber(getPreviousDay(dataDo)));
            }
        } else if (getYearNumber(endWeek) < getYearNumber(dataDo)) {  //rok poprzedni
            while ((getDayOYearNumber(endWeek) <= getDayOYearNumber(getPreviousDay(dataDo)) + 1) && getDayOYearNumber(endWeek) > getDayOYearNumber(dataOd)) {
                dniWyswietlane.add(getDayOMonthNumber(startWeek) + " - " + getDayOMonthNumber(getPreviousDay(endWeek)));
                startWeek = endWeek;
                endWeek = getNextWeek(endWeek);
            }
            if (getDayOYearNumber(endWeek) > getDayOYearNumber(dataDo)) {
                dniWyswietlane.add(getDayOMonthNumber(startWeek) + " - " + getDayOMonthNumber(getPreviousDay(dataDo)));
            }
        } else if (getYearNumber(endWeek) > getYearNumber(dataDo)) {//rok następny
            while (getDayOYearNumber(endWeek) <= getDayOYearNumber(getPreviousDay(dataDo)) + getYearsDays(dataDo)) {
                dniWyswietlane.add(getDayOMonthNumber(startWeek) + " - " + getDayOMonthNumber(getPreviousDay(endWeek)));
                startWeek = endWeek;
                endWeek = getNextWeek(endWeek);
            }
            if (getDayOYearNumber(endWeek) > getDayOYearNumber(dataDo) + getYearsDays(dataDo)) {
                dniWyswietlane.add(getDayOMonthNumber(startWeek) + " - " + getDayOMonthNumber(getPreviousDay(dataDo)));
            }
        }


    }

    private int getYearsDays(String dataIn) {
        int yearDays;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dataIn)));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(StatystykaActivity.this, "Błąd daty!", Toast.LENGTH_SHORT).show();
        }
        c.add(Calendar.YEAR, -1);
        yearDays = c.getActualMaximum(Calendar.DAY_OF_YEAR);

        return yearDays;
    }

    private int getDayOMonthNumber(String dataIn) {
        int weekdayNumber;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dataIn)));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(StatystykaActivity.this, "Błąd daty!", Toast.LENGTH_SHORT).show();
        }
        weekdayNumber = c.get(Calendar.DAY_OF_MONTH);

        return weekdayNumber;
    }

    private int getDayOYearNumber(String dataIn) {
        int weekdayNumber;
        Calendar c = Calendar.getInstance();
        try {
            //c= sdf.parse(dataIn);
            c.setTime(Objects.requireNonNull(sdf.parse(dataIn)));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(StatystykaActivity.this, "Błąd daty!", Toast.LENGTH_SHORT).show();
        }
        weekdayNumber = c.get(Calendar.DAY_OF_YEAR);

        return weekdayNumber;
    }

    private int getYearNumber(String dataIn) {
        int yearNumber;
        Calendar c = Calendar.getInstance();
        try {
            //c= sdf.parse(dataIn);
            c.setTime(Objects.requireNonNull(sdf.parse(dataIn)));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(StatystykaActivity.this, "Błąd daty!", Toast.LENGTH_SHORT).show();
        }

        yearNumber = c.get(Calendar.YEAR);

        return yearNumber;
    }

    private String getWeekName(String dataIn) {
        Calendar k = Calendar.getInstance();
        try {
            //c= sdf.parse(dataIn);
            k.setTime(Objects.requireNonNull(sdf.parse(dataIn)));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(StatystykaActivity.this, "Błąd daty!", Toast.LENGTH_SHORT).show();
        }
        //c=Calendar.getInstance()
        int dayOfWeek = k.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekName = "";
        switch (dayOfWeek) {
            case 2:
                dayOfWeekName = "Pn";
                break;
            case 3:
                dayOfWeekName = "Wt";
                break;
            case 4:
                dayOfWeekName = "Śr";
                break;
            case 5:
                dayOfWeekName = "Cz";
                break;
            case 6:
                dayOfWeekName = "Pt";
                break;
            case 7:
                dayOfWeekName = "Sb";
                break;
            case 1:
                dayOfWeekName = "Nd";
                break;
        }
        return dayOfWeekName;
    }

    private String getMonthName(String dataIn) {
        Calendar k = Calendar.getInstance();
        try {
            //c= sdf.parse(dataIn);
            k.setTime(Objects.requireNonNull(sdf.parse(dataIn)));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(StatystykaActivity.this, "Błąd daty!", Toast.LENGTH_SHORT).show();
        }
        //c=Calendar.getInstance()
        int whichMonth = k.get(Calendar.MONTH);
        String whichMonthName = "";
        switch (whichMonth) {
            case 0:
                whichMonthName = "Sty";
                break;
            case 1:
                whichMonthName = "Lut";
                break;
            case 2:
                whichMonthName = "Mar";
                break;
            case 3:
                whichMonthName = "Kwi";
                break;
            case 4:
                whichMonthName = "Maj";
                break;
            case 5:
                whichMonthName = "Cze";
                break;
            case 6:
                whichMonthName = "Lip";
                break;
            case 7:
                whichMonthName = "Sie";
                break;
            case 8:
                whichMonthName = "Wrz";
                break;
            case 9:
                whichMonthName = "Paź";
                break;
            case 10:
                whichMonthName = "List";
                break;
            case 11:
                whichMonthName = "Gru";
                break;
        }
        return whichMonthName;
    }

    private String getNextDay(String dataBefore) {
        String dataAfter;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dataBefore)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        dataAfter = sdf.format(c.getTime());
        return dataAfter;
    }

    private String getPreviousDay(String dataBefore) {
        String dataAfter;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dataBefore)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        dataAfter = sdf.format(c.getTime());
        return dataAfter;
    }

    private String getNextMonth(String dataBefore) {
        String dataAfter;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dataBefore)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, 1);
        dataAfter = sdf.format(c.getTime());
        return dataAfter;
    }

    private String getNextWeek(String dataBefore) {
        String dataAfter;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(dataBefore)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.WEEK_OF_MONTH, 1);
        dataAfter = sdf.format(c.getTime());
        return dataAfter;
    }
}

