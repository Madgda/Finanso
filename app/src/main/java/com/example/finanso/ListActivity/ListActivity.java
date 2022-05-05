package com.example.finanso.ListActivity;

import static android.content.ContentValues.TAG;
import static com.google.android.material.internal.ContextUtils.getActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.MainActivity.MainActivity;
import com.example.finanso.R;
import com.example.finanso.SQLite.SqLiteManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListActivity extends AppCompatActivity implements ListAdapter.OnlistClickListener {

    private RecyclerView mRecyclerView;
    private AlertDialog.Builder dialogBuild;
    private AlertDialog dialog;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private EditText liczbaE, opisE, dateE, opisSzczegolE;
    private DatePickerDialog picker;
    private Spinner kategoriaS;
    private ListAdapter mAdapter;
    public int czyPopupDodaj;
    public SqLiteManager myDB;
    @SuppressLint("StaticFieldLeak")
    private static ListActivity instance = null;
    private ArrayList<ReadAllHistoriaResponse> lista_historia;
    private ReadAllHistoriaResponse dataEditPopup;
    private ArrayList<String> kategoriaArray;
    private RadioButton wplywCheck, wydatekCheck;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public ListActivity() {
        myDB = new SqLiteManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        instance = this;


        //DB= new DBHelper(this);
        mRecyclerView = findViewById(R.id.recycler_view);

        zapiszListeDoArray();
        zapiszKategorieDoArray();
        //  mAdapter = new ExampleAdapter(ListActivity.this,lista_historia,1);
        buildRecyclerView();
        // przeladuj();

        Intent intent = getIntent();
        czyPopupDodaj = intent.getIntExtra(MainActivity.CZY_POPUP_LISTA, 0);

        switch (czyPopupDodaj) {
            case 1:
                createDialogAddNewRecord();
                czyPopupDodaj = 0;
                break;

            case 0:
                break;
            default:

        }


        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Historia transakcji");
        setSupportActionBar(toolbar);

        EditText editText = findViewById((R.id.loopaView));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }


    private void filter(String text) {
        ArrayList<ReadAllHistoriaResponse> filteredList = new ArrayList<>();

        for (ReadAllHistoriaResponse item : lista_historia) {
            if (item.opis.toLowerCase().contains(text.toLowerCase()) || item.szczegol_opis.toLowerCase().contains(text.toLowerCase()) || item.kwota.toLowerCase().contains(text.toLowerCase())) {
                {
                    filteredList.add(item);
                }
            }
            mAdapter.filterList(filteredList);
        }
    }
    //


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_lista, menu);

        /*
         */ /*
           menu.getItem(R.id.menuDodajLista).setOnMenuItemClickListener(new View.OnClickListener(){

               @Override
               public void onClick(View view) {
                   createNewDialog();
               }
           });*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuDodajLista) {
            createDialogAddNewRecord();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("SetTextI18n")
    public void createDialogAddNewRecord() {
        //pop up dodawanie rekordu

        dialogBuild = new AlertDialog.Builder(this);
        final View listaPopupView = getLayoutInflater().inflate(R.layout.popup_lista, null);
        liczbaE = listaPopupView.findViewById(R.id.kwotaE);
        opisE = listaPopupView.findViewById(R.id.opisE);
        opisSzczegolE = listaPopupView.findViewById(R.id.opisDlugiE);
        dateE = listaPopupView.findViewById(R.id.dataE);
        Button dodajB = listaPopupView.findViewById(R.id.dodajB);
        kategoriaS = listaPopupView.findViewById(R.id.kategoriaS);
        wplywCheck = listaPopupView.findViewById(R.id.radioWplyw);
        wydatekCheck = listaPopupView.findViewById(R.id.radioWydatek);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //kategorieA=lista_kategorie.nazwa
        //kategoriaS.setPrompt("Wybierz kategorię");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriaArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        kategoriaS.setAdapter(spinnerArrayAdapter);

        dateE.setText(currentDate);


        dialogBuild.setView(listaPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);
        dateE.setOnClickListener(view -> {
            final Calendar kalendarz = Calendar.getInstance();
            int day = kalendarz.get(Calendar.DAY_OF_MONTH);
            int month = kalendarz.get(Calendar.MONTH);
            int year = kalendarz.get(Calendar.YEAR);

            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);
            picker = new DatePickerDialog(ListActivity.this, (datePicker, year1, month1, day1) -> {
                if (day1 < 10 && month1 < 9) {
                    dateE.setText(year1 + "-0" + (month1 + 1) + "-0" + day1);
                } else if (day1 < 10) {
                    dateE.setText(year1 + "-" + (month1 + 1) + "-0" + day1);
                } else if (month1 < 9) {
                    dateE.setText(year1 + "-0" + (month1 + 1) + "-" + day1);
                } else if (month1 < 9) {
                } else {
                    dateE.setText(year1 + "-" + (month1 + 1) + "-" + day1);
                }
            }, year, month, day);
            picker.show();
        });

        dodajB.setOnClickListener(view -> {
            String kategoriaDoBazy = kategoriaS.getSelectedItem().toString();
            int countDot = kategoriaDoBazy.indexOf(".");
            String czyWplywString;
            if (wplywCheck.isChecked()) {
                czyWplywString = "tak";
            } else if (wydatekCheck.isChecked()) {
                czyWplywString = "nie";
            } else {
                czyWplywString = "błąd";
            }
            double w;
            String kwotaString = liczbaE.getText().toString().replaceAll(",", ".");
            try {
                w = Double.parseDouble(kwotaString);
            } catch (NumberFormatException e) {
                w = 0.00; // your default value
            }
            Double kwotaPoPrzecinku = w;
            df.setRoundingMode(RoundingMode.UP);

            kategoriaDoBazy = kategoriaDoBazy.substring(0, countDot);
            myDB = new SqLiteManager(ListActivity.this);
            if (liczbaE.getText().toString().trim().length() > 0 && opisE.getText().toString().trim().length() > 0 && dateE.getText().toString().trim().length() > 0) {
                myDB.addWpis(df.format(kwotaPoPrzecinku).trim(), opisE.getText().toString().trim(), opisSzczegolE.getText().toString().trim(), dateE.getText().toString().trim(), kategoriaDoBazy, czyWplywString.trim());
                dialog.dismiss();
                zapiszListeDoArray();
                buildRecyclerView();
            } else {
                Toast.makeText(ListActivity.this, "Proszę uzupełnić wymagane pola: kwota, opis, data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void zapiszListeDoArray() {
        myDB = new SqLiteManager(ListActivity.this);
        lista_historia = new ArrayList<>();

        Cursor cursor = myDB.readAllHistoryWithKategorie();
        if (cursor.getCount() == 0) {
            // Toast.makeText(ListActivity.instance, "Brak danych.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                ReadAllHistoriaResponse readAHR = new ReadAllHistoriaResponse();
                readAHR.id = cursor.getString(0);
                readAHR.kwota = cursor.getString(1);
                readAHR.opis = cursor.getString(2);
                readAHR.szczegol_opis = cursor.getString(3);
                readAHR.data = cursor.getString(4);
                readAHR.kategoria_id = cursor.getString(7);
                readAHR.kategoria_nazwa = cursor.getString(9);
                readAHR.kategoria_kolor = cursor.getString(8);
                readAHR.czyWplyw = cursor.getString(6);
                lista_historia.add(readAHR);
            }
        }
    }

    void zapiszKategorieDoArray() {
        myDB = new SqLiteManager(ListActivity.instance);
        ArrayList<String> lista_kategorie_id = new ArrayList<>();
        ArrayList<String> lista_kategorie_kolor = new ArrayList<>();
        ArrayList<String> lista_kategorie_nazwa = new ArrayList<>();
        ArrayList<String> lista_kategorie_opis = new ArrayList<>();
        kategoriaArray = new ArrayList<>();

        Cursor cursor = myDB.readAllKategorie();
        if (cursor.getCount() == 0) {
            //  Toast.makeText(ListActivity.instance, "Brak danych.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                lista_kategorie_id.add(cursor.getString(0));
                lista_kategorie_kolor.add(cursor.getString(1));
                lista_kategorie_nazwa.add(cursor.getString(2));
                lista_kategorie_opis.add(cursor.getString(3));
                kategoriaArray.add(cursor.getString(0) + ". " + cursor.getString(2));

            }
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ListAdapter(ListActivity.this, lista_historia, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // lista_historia.set(position,)

    }

    /*public void setItmInfo(){
        lista_historia.set(5,);

   } */

    @SuppressLint("NotifyDataSetChanged")
    public void createDialogOnLongPress(Integer position) {
        dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(getActivity(this));
        View listOnLongPressPopupView = li.inflate(R.layout.popup_lista_onlongpress, null);

        Button buttonListDelete = listOnLongPressPopupView.findViewById(R.id.buttonDeleteListOnPress);
        Button buttonListEdit = listOnLongPressPopupView.findViewById(R.id.buttonEditListOnPress);

        dialogBuild.setView(listOnLongPressPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dataEditPopup = lista_historia.get(position);
        //Toast.makeText(ListActivity.this, dataEditPopup.id+" - "+ dataEditPopup.kwota+" - "+ dataEditPopup.opis+" - "+ dataEditPopup.data+" - "+ dataEditPopup.szczegol_opis, Toast.LENGTH_SHORT).show();

        buttonListDelete.setOnClickListener(view -> {


            myDB = new SqLiteManager(ListActivity.this);
            myDB.deleteOneRowFromList(dataEditPopup.id);
            dialog.dismiss();
            mAdapter.notifyDataSetChanged();
            finish();
            Intent intent = new Intent(ListActivity.this, ListActivity.class);
            startActivity(intent);
            //  Intent intent = new Intent(ListActivity.this(, ListActivity.class);

        });


        buttonListEdit.setOnClickListener(view -> {
            dialog.dismiss();
            createEditRecordDialog(dataEditPopup.id, dataEditPopup.kwota, dataEditPopup.opis, dataEditPopup.szczegol_opis, dataEditPopup.data, dataEditPopup.kategoria_id, dataEditPopup.kategoria_nazwa, dataEditPopup.czyWplyw);
        });

    }


    @SuppressLint("NotifyDataSetChanged")
    public void createEditRecordDialog(String rowId, String kwotaValue, String
            opisValue, String szczegolOpisValue, String dataValue, String kategoria_id, String
                                               kategoria_nazwa, String czyWplyw) {
        dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(getActivity(this));
        View listEditPopupView = li.inflate(R.layout.popup_lista_edit, null);

        liczbaE = listEditPopupView.findViewById(R.id.kwotaE);
        liczbaE.setText(kwotaValue);
        opisE = listEditPopupView.findViewById(R.id.opisE);
        opisE.setText(opisValue);
        opisSzczegolE = listEditPopupView.findViewById(R.id.opisDlugiE);
        opisSzczegolE.setText(szczegolOpisValue);
        dateE = listEditPopupView.findViewById(R.id.dataE);
        dateE.setText(dataValue);
        Button zapiszB = listEditPopupView.findViewById(R.id.zapiszB);
        kategoriaS = listEditPopupView.findViewById(R.id.kategoriaS);
        wplywCheck = listEditPopupView.findViewById(R.id.radioWplyw);
        wydatekCheck = listEditPopupView.findViewById(R.id.radioWydatek);

        if (czyWplyw.equals("tak")) {
            wydatekCheck.setChecked(false);
            wplywCheck.setChecked(true);

        } else if (czyWplyw.equals("nie")) {
            wydatekCheck.setChecked(true);
            wplywCheck.setChecked(false);

        } else {
            wydatekCheck.setChecked(false);
            wplywCheck.setChecked(false);
        }

        //  String kategorieA[] = {"Wybierz kategorię", "Rachunki", "Spożywcze", "Prezenty", "Chemia", "Remont"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriaArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        kategoriaS.setAdapter(spinnerArrayAdapter);
        int pozycjaKategoria;
        //  Log.v(TAG, kategoria_id+". "+kategoria_nazwa);
        pozycjaKategoria = kategoriaArray.indexOf(kategoria_id + ". " + kategoria_nazwa);
        if (pozycjaKategoria > 0) {
            kategoriaS.setSelection(pozycjaKategoria);
        }

        Log.v(TAG, Integer.toString(pozycjaKategoria));

        dialogBuild.setView(listEditPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);
        dateE.setOnClickListener(view -> {
            final Calendar kalendarz = Calendar.getInstance();
            try {
                kalendarz.setTime(sdf.parse(dataValue));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int day = kalendarz.get(Calendar.DAY_OF_MONTH);
            int month = kalendarz.get(Calendar.MONTH);
            int year = kalendarz.get(Calendar.YEAR);

            @SuppressLint("SetTextI18n") DatePickerDialog picker = new DatePickerDialog(ListActivity.this, (datePicker, year1, month1, day1) -> {
                if (day1 < 10 && month1 < 9) {
                    dateE.setText(year1 + "-0" + (month1 + 1) + "-0" + day1);
                } else if (day1 < 10) {
                    dateE.setText(year1 + "-" + (month1 + 1) + "-0" + day1);
                } else if (month1 < 9) {
                    dateE.setText(year1 + "-0" + (month1 + 1) + "-" + day1);
                } else if (month1 < 9) {
                } else {
                    dateE.setText(year1 + "-" + (month1 + 1) + "-" + day1);
                }
            }, year, month, day);
            picker.show();
        });
        zapiszB.setOnClickListener(view -> {
            String czyWplywString;
            if (wplywCheck.isChecked()) {
                czyWplywString = "tak";
            } else if (wydatekCheck.isChecked()) {
                czyWplywString = "nie";
            } else {
                czyWplywString = "błąd";
            }
            String kwotaString = liczbaE.getText().toString().replaceAll(",", ".");
            double w;
            try {
                w = Double.parseDouble(kwotaString);
            } catch (NumberFormatException e) {
                w = 0.00;
            }
            Double kwotaPoPrzecinku = w;
            df.setRoundingMode(RoundingMode.UP);

            String kategoriaDoBazy = kategoriaS.getSelectedItem().toString();
            int countDot = kategoriaDoBazy.indexOf(".");
            kategoriaDoBazy = kategoriaDoBazy.substring(0, countDot);

            SqLiteManager myDB = new SqLiteManager(ListActivity.this);
            if (liczbaE.getText().toString().trim().length() > 0 && opisE.getText().toString().trim().length() > 0 && dateE.getText().toString().trim().length() > 0) {
                myDB.updateListData(rowId, df.format(kwotaPoPrzecinku).trim(), opisE.getText().toString().trim(), opisSzczegolE.getText().toString().trim(), dateE.getText().toString().trim(), kategoriaDoBazy, czyWplywString.trim());
                dialog.dismiss();
                mAdapter.notifyDataSetChanged();
                zapiszListeDoArray();
                buildRecyclerView();
            } else {
                Toast.makeText(ListActivity.this, "BŁĄD", Toast.LENGTH_SHORT).show();
            }

        });


    }


    @Override
    public void onLongClick(int position_) {
        createDialogOnLongPress(position_);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(ListActivity.this, "Przytrzymaj jedną z pozycji by usunąć lub edytować ", Toast.LENGTH_SHORT).show();


    }

}



