package com.example.finanso.CategoryActivity;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.MainActivity.MainActivity;
import com.example.finanso.R;
import com.example.finanso.SQLite.SqLiteManager;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView mRecyclerView;
    private AlertDialog.Builder dialogBuild;
    private AlertDialog dialog;
    private CategoryAdapter mAdapter;
    public int czyPopupDodaj;
    public SqLiteManager myDB;
    private ArrayList<ReadAllCategoryResponse> lista_kategorie;
    private String pickedColor = "#7E8288";
    private ReadAllCategoryResponse dataEditPopup;
    private ImageButton dodajKolorKategorii;
    private EditText nazwaKategorie, opisKategorie;
    private Button zapiszEdycjeKategorii;

    public CategoryActivity() {
        myDB = new SqLiteManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorie);

        mRecyclerView = findViewById(R.id.recycler_view);

        zapiszKategorieDoArray();
        buildRecyclerView();
        // przeladuj();

        Intent intent = getIntent();
        czyPopupDodaj = intent.getIntExtra(MainActivity.CZY_POPUP_LISTA, 0);

        switch (czyPopupDodaj) {
            case 1:
                createNewDialog();
                czyPopupDodaj = 0;
                break;

            case 0:
                break;
            default:

        }


        Toolbar toolbar = findViewById(R.id.toolbarCategory);
        toolbar.setTitle("Ustawienia kategorii");
        setSupportActionBar(toolbar);

    }
/*

//TODO

    private void filter(String text){
        ArrayList<ReadAllHistoriaResponse> filteredList = new ArrayList<>();

        for(ReadAllHistoriaResponse item : exampleList){
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
}
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_kategorie, menu);
        //  menu.getItem(R.id.menuDodajKategorie)

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
        //po kliknieciu plusik
        pickedColor = "#7E8288";
        dialogBuild = new AlertDialog.Builder(this);
        final View kategoriePopupView = getLayoutInflater().inflate(R.layout.popup_kategorie, null);
        nazwaKategorie = kategoriePopupView.findViewById(R.id.NazwaPopupKategorie);
        opisKategorie = kategoriePopupView.findViewById(R.id.OpisPopupKategorie);
        dodajKolorKategorii = kategoriePopupView.findViewById(R.id.kolorKategorieButton);
        zapiszEdycjeKategorii = kategoriePopupView.findViewById(R.id.dodajButtonKategorie);
        //  dodajKolorKategorii.set(Integer.parseInt(pickedColor));
        dialogBuild.setView(kategoriePopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dodajKolorKategorii.setOnClickListener(view -> {
            final ColorPicker colorPicker = new ColorPicker(CategoryActivity.this);
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
        });

        zapiszEdycjeKategorii.setOnClickListener(view -> {
            // dialog.dismiss();
            SqLiteManager myDBKat = new SqLiteManager(CategoryActivity.this);

            if (nazwaKategorie.getText().toString().trim().length() > 0 && opisKategorie.getText().toString().trim().length() > 0) {
                myDBKat.addKategoria(pickedColor.trim(), nazwaKategorie.getText().toString().trim(), opisKategorie.getText().toString().trim());
                dialog.dismiss();
                zapiszKategorieDoArray();
                buildRecyclerView();
            } else {
                Toast.makeText(CategoryActivity.this, "Uzupełnij nazwę i opis", Toast.LENGTH_SHORT).show();
            }
        });

    }


    void zapiszKategorieDoArray() {
        myDB = new SqLiteManager(CategoryActivity.this);
        lista_kategorie = new ArrayList<>();

        Cursor cursor = myDB.readAllKategorie();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ReadAllCategoryResponse readAHR = new ReadAllCategoryResponse();
                readAHR.id = cursor.getString(0);
                readAHR.kolor = cursor.getString(1);
                readAHR.nazwa = cursor.getString(2);
                readAHR.opis = cursor.getString(3);
                lista_kategorie.add(readAHR);
            }
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CategoryAdapter(CategoryActivity.this, lista_kategorie, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public void createEditCategoryDialog(String rowId, String kolor, String nazwa, String opis) {
        //po kliknięciu edytuj
        dialogBuild = new AlertDialog.Builder(this);
        final View kategoriePopupView = getLayoutInflater().inflate(R.layout.popup_kategorie, null);
        nazwaKategorie = kategoriePopupView.findViewById(R.id.NazwaPopupKategorie);
        nazwaKategorie.setText(nazwa);
        opisKategorie = kategoriePopupView.findViewById(R.id.OpisPopupKategorie);
        TextView textOpisKategorie = kategoriePopupView.findViewById(R.id.kategoriaOpisText);
        opisKategorie.setText(opis);
        dodajKolorKategorii = kategoriePopupView.findViewById(R.id.kolorKategorieButton);
        dodajKolorKategorii.setBackgroundColor(Color.parseColor(kolor));
        zapiszEdycjeKategorii = kategoriePopupView.findViewById(R.id.dodajButtonKategorie);
        //  dodajKolorKategorii.set(Integer.parseInt(pickedColor));
        dialogBuild.setView(kategoriePopupView);
        dialog = dialogBuild.create();
        dialog.show();
        pickedColor = kolor;
        zapiszEdycjeKategorii.setText("Zapisz");
        textOpisKategorie.setText("Edytuj kategorię");
        dodajKolorKategorii.setOnClickListener(view -> {
            final ColorPicker colorPicker = new ColorPicker(CategoryActivity.this);
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
                    .setColumns(5)
                    .setTitle("Wybierz kolor")
                    .setDefaultColorButton(Color.parseColor(kolor))
                    .show();
        });

        zapiszEdycjeKategorii.setOnClickListener(view -> {
            SqLiteManager myDB = new SqLiteManager(CategoryActivity.this);
            if (nazwaKategorie.getText().toString().trim().length() > 0 && opisKategorie.getText().toString().trim().length() > 0) {
                myDB.updateCategoryData(rowId, pickedColor.trim(), nazwaKategorie.getText().toString().trim(), opisKategorie.getText().toString().trim());
                dialog.dismiss();
                mAdapter.notifyDataSetChanged();
                zapiszKategorieDoArray();
                buildRecyclerView();
            } else {
                Toast.makeText(CategoryActivity.this, "BŁĄD", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void createDialogOnLongPress(Integer position) {
        //popup edit i delete
        dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(getActivity(this));
        View listOnLongPressPopupView = li.inflate(R.layout.popup_lista_onlongpress, null);

        Button buttonListDelete = listOnLongPressPopupView.findViewById(R.id.buttonDeleteListOnPress);
        Button buttonListEdit = listOnLongPressPopupView.findViewById(R.id.buttonEditListOnPress);

        dialogBuild.setView(listOnLongPressPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dataEditPopup = lista_kategorie.get(position);

        buttonListDelete.setOnClickListener(view -> {


            myDB = new SqLiteManager(CategoryActivity.this);
            myDB.deleteOneRowFromCategory(dataEditPopup.id);
            dialog.dismiss();
            mAdapter.notifyDataSetChanged();
            finish();
            Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
            startActivity(intent);
        });


        buttonListEdit.setOnClickListener(view -> {
            dialog.dismiss();
            createEditCategoryDialog(dataEditPopup.id, dataEditPopup.kolor, dataEditPopup.nazwa, dataEditPopup.opis);
        });

    }

    @Override
    public void onLongClick(int position_) {
        createDialogOnLongPress(position_);
    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(CategoryActivity.this, "Przytrzymaj jedną z pozycji by usunąć lub edytować ", Toast.LENGTH_SHORT).show();

    }

}
