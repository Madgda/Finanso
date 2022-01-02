package com.example.finanso;

import static java.lang.String.format;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import petrov.kristiyan.colorpicker.ColorPicker;

public class KategorieActivity  extends AppCompatActivity {

    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<ExampleItem> exampleList = new ArrayList<>();
    private AlertDialog.Builder dialogBuild;
    private AlertDialog dialog;
    private CheckBox radioBurronGwarancja;
    private EditText opisKategorie, nazwaKategorie;
    private Button dodajB;
    private String pickedColor = "#7E8288";
    private DatePickerDialog picker;
    private ImageButton dodajKolorKategorii;
    private Button dodajWpisKategorii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorie);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Finanso");
        setSupportActionBar(toolbar);
        //  "Wybierz kategorię","Rachunki","Spożywcze","Prezenty","Chemia","Remont"

        exampleList.add(new ExampleItem("#6C4D70", "Spożywcze", "Zakupy spożywcze", "50"));
        exampleList.add(new ExampleItem("#56325A", "Prezenty", "Prezenty świąteczne, urodzinowe itp.", "30"));
        exampleList.add(new ExampleItem("#9beaf9", "Pensja", "Wypłata- wpływy", "20"));

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
      //  mAdapter = new ExampleAdapter(exampleList, this, 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        drawer = findViewById(R.id.drawer_layout_list);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void filter(String text) {
        ArrayList<ExampleItem> filteredList = new ArrayList<>();

        for (ExampleItem item : exampleList) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_kategorie, menu);
        menu.findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>wyszukaj daty</font>"));
        menu.findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>sortuj wg </font>"));
        menu.findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>pokaż</font>"));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuDodajKategorie) {
            createNewDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNewDialog() {
        dialogBuild = new AlertDialog.Builder(this);
        final View kategoriePopupView = getLayoutInflater().inflate(R.layout.popup_kategorie, null);
        nazwaKategorie = (EditText) kategoriePopupView.findViewById(R.id.NazwaPopupKategorie);
        opisKategorie = (EditText) kategoriePopupView.findViewById(R.id.OpisPopupKategorie);
        dodajKolorKategorii = (ImageButton) kategoriePopupView.findViewById(R.id.kolorKategorieButton);
        dodajWpisKategorii = (Button) kategoriePopupView.findViewById(R.id.dodajButtonKategorie);
        //  dodajKolorKategorii.set(Integer.parseInt(pickedColor));
        dialogBuild.setView(kategoriePopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dodajKolorKategorii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(KategorieActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        pickedColor = String.format("#%06X", 0xFFFFFF & color);
                        dodajKolorKategorii.setBackgroundColor(Color.parseColor(pickedColor));

                    }

                    @Override
                    public void onCancel() {
                        colorPicker.dismissDialog();
                    }
                })
                        // set the number of color columns
                        // you want  to show in dialog.
                        .setColumns(5)
                        // set a default color selected
                        // in the dialog
                        .setDefaultColorButton(Color.parseColor("#000000"))
                        .show();
            }
        });

        dodajWpisKategorii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // dialog.dismiss();
                SqLiteManager myDBKat=new SqLiteManager(KategorieActivity.this);

                if (nazwaKategorie.getText().toString().trim().length() > 0 && opisKategorie.getText().toString().trim().length() > 0) {
                    myDBKat.addKategoria(pickedColor.trim(),nazwaKategorie.getText().toString().trim(), opisKategorie.getText().toString().trim());
                    dialog.dismiss();
                } else {
                    Toast.makeText(KategorieActivity.this, "Uzupełnij nazwę i opis", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
