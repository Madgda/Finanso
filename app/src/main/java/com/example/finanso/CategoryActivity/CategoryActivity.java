package com.example.finanso.CategoryActivity;

import static com.google.android.material.internal.ContextUtils.getActivity;
import static java.lang.String.format;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.ListActivity.ListActivity;
import com.example.finanso.MainActivity.MainActivity;
import com.example.finanso.R;
import com.example.finanso.SQLite.SqLiteManager;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private AlertDialog.Builder dialogBuild;
    private AlertDialog dialog;
    private EditText liczbaE,opisE,dateE,opisSzczegolE;
    private Button dodajB;
    private DatePickerDialog picker;
    private Spinner kategoriaS;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem dodajLista;
    private TextView mItemInfo;
    public int czyPopupDodaj;
    public SqLiteManager myDB;
    private String kategorieA[]={"Wybierz kategorię","Rachunki","Spożywcze","Prezenty","Chemia","Remont"};
    private Button buttonOnPressEdit,buttonOnPressDelete;
    private static CategoryAdapter instance = null;
    private ArrayList<ReadAllCategoryResponse> lista_kategorie;
    private String rowId;
    private int position;
    private String pickedColor = "#7E8288";
    private String pressed;
    private TextView infoTextOnClick;
    private Button buttonListDelete;
    private Button buttonListEdit;
    private ReadAllCategoryResponse dataEditPopup;
    private String opisValue;
    private Button zapiszB;
    private ArrayList<String> kategoriaArray;
    private ImageButton dodajKolorKategorii;
    private EditText nazwaKategorie,opisKategorie;
    private Button zapiszEdycjeKategorii;

    public CategoryActivity(){
        myDB=new SqLiteManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorie);

        mRecyclerView =findViewById(R.id.recycler_view);

        zapiszKategorieDoArray();
        buildRecyclerView();
        // przeladuj();

        Intent intent = getIntent();
        czyPopupDodaj=intent.getIntExtra(MainActivity.CZY_POPUP_LISTA,0);

        switch (czyPopupDodaj){
            case 1:
                createNewDialog();
                czyPopupDodaj=0;
                break;

            case 0:
                break;
            default:

        }



        Toolbar toolbar = findViewById(R.id.toolbarCategory);
        toolbar.setTitle("Ustawienia kategorii");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_list);

    /*    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
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





/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_kategorie,menu);
        menu.findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>wyszukaj daty</font>"));
        menu.findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>sortuj wg </font>"));
        menu.findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>pokaż</font>"));

        */
/*
        menu.getItem(R.id.menuDodajLista).setOnMenuItemClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                createNewDialog();
            }
        });
*//*

        return true;
    }
*/



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
        nazwaKategorie = (EditText) kategoriePopupView.findViewById(R.id.NazwaPopupKategorie);
        opisKategorie = (EditText) kategoriePopupView.findViewById(R.id.OpisPopupKategorie);
        dodajKolorKategorii = (ImageButton) kategoriePopupView.findViewById(R.id.kolorKategorieButton);
        zapiszEdycjeKategorii = (Button) kategoriePopupView.findViewById(R.id.dodajButtonKategorie);
        //  dodajKolorKategorii.set(Integer.parseInt(pickedColor));
        dialogBuild.setView(kategoriePopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dodajKolorKategorii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        zapiszEdycjeKategorii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                SqLiteManager myDBKat=new SqLiteManager(CategoryActivity.this);

                if (nazwaKategorie.getText().toString().trim().length() > 0 && opisKategorie.getText().toString().trim().length() > 0) {
                    myDBKat.addKategoria(pickedColor.trim(),nazwaKategorie.getText().toString().trim(), opisKategorie.getText().toString().trim());
                    dialog.dismiss();
                    zapiszKategorieDoArray();
                    buildRecyclerView();
                } else {
                    Toast.makeText(CategoryActivity.this, "Uzupełnij nazwę i opis", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    void zapiszKategorieDoArray()
    {
        myDB=new SqLiteManager(CategoryActivity.this);
        lista_kategorie =new ArrayList<>();

        Cursor cursor = myDB.readAllKategorie();
        if(cursor.getCount()==0){
      //      Toast.makeText(CategoryActivity.this,"Brak danych.",Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                ReadAllCategoryResponse readAHR = new ReadAllCategoryResponse();
                readAHR.id=cursor.getString(0);
                readAHR.kolor=cursor.getString(1);
                readAHR.nazwa=cursor.getString(2);
                readAHR.opis=cursor.getString(3);
                lista_kategorie.add(readAHR);
            }
        }
    }

    public void buildRecyclerView(){
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CategoryAdapter(CategoryActivity.this,lista_kategorie,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
    public void createEditCategoryDialog(String rowId, String kolor, String nazwa, String opis) {
       //po kliknięciu edytuj
        dialogBuild = new AlertDialog.Builder(this);
        final View kategoriePopupView = getLayoutInflater().inflate(R.layout.popup_kategorie, null);
        nazwaKategorie = (EditText) kategoriePopupView.findViewById(R.id.NazwaPopupKategorie);
        nazwaKategorie.setText(nazwa);
        opisKategorie = (EditText) kategoriePopupView.findViewById(R.id.OpisPopupKategorie);
        opisKategorie.setText(opis);
        dodajKolorKategorii = (ImageButton) kategoriePopupView.findViewById(R.id.kolorKategorieButton);
        dodajKolorKategorii.setBackgroundColor(Color.parseColor(kolor));
        zapiszEdycjeKategorii = (Button) kategoriePopupView.findViewById(R.id.dodajButtonKategorie);
        //  dodajKolorKategorii.set(Integer.parseInt(pickedColor));
        dialogBuild.setView(kategoriePopupView);
        dialog = dialogBuild.create();
        dialog.show();
        pickedColor=kolor;
        dodajKolorKategorii.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
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
                        .setDefaultColorButton(Color.parseColor(kolor))
                        .show();
            }
        });

        zapiszEdycjeKategorii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
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
            }
        });
    }
    public void createDialogOnLongPress(Integer position){
        //popup edit i delete
        dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater li= LayoutInflater.from(getActivity(this));
        View listOnLongPressPopupView=li.inflate(R.layout.popup_lista_onlongpress,null);

        buttonListDelete = (Button) listOnLongPressPopupView.findViewById(R.id.buttonDeleteListOnPress);
        buttonListEdit = (Button) listOnLongPressPopupView.findViewById(R.id.buttonEditListOnPress);

        dialogBuild.setView(listOnLongPressPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dataEditPopup = lista_kategorie.get(position);

        buttonListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                myDB=new SqLiteManager(CategoryActivity.this);
                myDB.deleteOneRowFromCategory(dataEditPopup.id);
                dialog.dismiss();
                mAdapter.notifyDataSetChanged();
                finish();
                Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
                startActivity(intent);
              }
        });



        buttonListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                createEditCategoryDialog(dataEditPopup.id,dataEditPopup.kolor,dataEditPopup.nazwa,dataEditPopup.opis);
            }
        });

    }

    @Override
    public void onLongClick(int position_) {
        this.position=position_;
        createDialogOnLongPress(position);
    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(CategoryActivity.this, "Przytrzymaj jedną z pozycji by usunąć lub edytować ", Toast.LENGTH_SHORT).show();

    /*    infoTextOnClick = findViewById(R.id.infoShowHelpOnClickCategory);
        infoTextOnClick.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                infoTextOnClick.setVisibility(View.GONE);
            }
        }, 1500);*/

    }

}
