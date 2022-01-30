package com.example.finanso;

import static android.media.CamcorderProfile.get;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListActivity extends AppCompatActivity implements ExampleAdapter.OnNoteListener {

    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private AlertDialog.Builder dialogBuild;
    private AlertDialog dialog;
    private EditText liczbaE,opisE,dateE,opisSzczegolE;
    private Button dodajB;
    private DatePickerDialog picker;
    private Spinner kategoriaS;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<ExampleItem> exampleList = new ArrayList<>();
    private MenuItem dodajLista;
    private TextView mItemInfo;
    public int czyPopupDodaj;
    public SqLiteManager myDB;
    //ArrayList<String> lista_id,lista_kwota,lista_opis,lista_szczegol,lista_data,lista_kategoria;

    private String kategorieA[]={"Wybierz kategorię","Rachunki","Spożywcze","Prezenty","Chemia","Remont"};
    private Button buttonOnPressEdit,buttonOnPressDelete;
    // DBHelper DB;
    private static ListActivity instance = null;
    private ArrayList<ReadAllHistoriaResponse> lista_historia;
    private String rowId;
    private int position;
    private String pressed;
    private TextView infoTextOnClick;
    private Button buttonListDelete;
    private Button buttonListEdit;

    public ListActivity(){
        myDB=new SqLiteManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        this.instance = this;


        //DB= new DBHelper(this);
        mRecyclerView =findViewById(R.id.recycler_view);
        mItemInfo= findViewById(R.id.textViewInfoKlikAdapter);

        zapiszListeDoArray();
      //  mAdapter = new ExampleAdapter(ListActivity.this,lista_historia,1);
        buildRecyclerView();
       // przeladuj();

        Intent intent = getIntent();
        czyPopupDodaj=intent.getIntExtra(MainActivity.CZY_POPUP_LISTA,0);

        switch (czyPopupDodaj){
            case 1:
                createDialogAddNewRecord();
                czyPopupDodaj=0;
                break;

            case 0:
                break;
            default:

        }



        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Finanso");
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

        //exampleList.add(lista_id,lista_kwota,lista_opis,lista_szczegol,lista_data,lista_kategoria);
       // exampleList.add(new ExampleItem("#ff9d9c","Prezenty świąteczne","2021.12.05","-6566.77"));
        //exampleList.add(new ExampleItem("#a3bce1","Wypłata","2021.12.01","6776.99"));
        /*exampleList.add(new ExampleItem("#6C4D70","Zakupy spożywcze","2021.11.13","-144.32"));
        exampleList.add(new ExampleItem("#ff9d9c","Prezenty świąteczne","2021.11.05","-6566.77"));
        exampleList.add(new ExampleItem("#a3bce1","Wypłata","2021.11.01","6776.99"));
*/
     /*        mRecyclerView =findViewById(R.id.recycler_view);

       mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(this);
        mAdapter=new ExampleAdapter(exampleList, this,1);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
*/
        drawer = findViewById(R.id.drawer_layout_list);
      //  plus =findViewById(R.id.plus);
    //    plus.setOnClickListener(new View.OnClickListener(){
/*
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(ListActivity.this,MainActivity.class);
              startActivity(intent);
            }
        });
        //Intent intent = getIntent();
*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

 /*   @Override
    protected void onResume() {
        super.onResume();
        przeladuj();
        //DB= new DBHelper(this);
         Toast.makeText(ListActivity.this,"SIE WZIEŁO SIE PRZEŁADOWAŁO SIE!! ",Toast.LENGTH_SHORT).show();

    }*/

    private void filter(String text){
        ArrayList<ExampleItem> filteredList = new ArrayList<>();

        for(ExampleItem item : exampleList){
            if(item.getText1().toLowerCase().contains(text.toLowerCase())||item.getText2().toLowerCase().contains(text.toLowerCase())||item.getText3().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
}


   // public void dashGo(View view) {
    //    setContentView(R.layout.activity_main);
    //}


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_lista,menu);
        menu.findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>wyszukaj daty</font>"));
        menu.findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>sortuj wg </font>"));
        menu.findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>pokaż</font>"));

        /*
        menu.getItem(R.id.menuDodajLista).setOnMenuItemClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                createNewDialog();
            }
        });
*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                int id = item.getItemId();

                if(id==R.id.menuDodajLista)
                {
                    createDialogAddNewRecord();
                    return true;
                }
                return super.onOptionsItemSelected(item);
    }


    public void createDialogAddNewRecord(){
        //pop up dodawanie rekordu
        dialogBuild = new AlertDialog.Builder(this);
        final View listaPopupView=getLayoutInflater().inflate(R.layout.popup_lista,null);
        liczbaE = (EditText) listaPopupView.findViewById(R.id.kwotaE);
        opisE = (EditText) listaPopupView.findViewById(R.id.opisE);
        opisSzczegolE = (EditText) listaPopupView.findViewById(R.id.opisDlugiE);
        dateE = (EditText) listaPopupView.findViewById(R.id.dataE);
        dodajB = (Button) listaPopupView.findViewById(R.id.dodajB);
        kategoriaS = (Spinner) listaPopupView.findViewById(R.id.kategoriaS);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        //kategoriaS.setPrompt("Wybierz kategorię");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, kategorieA);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        kategoriaS.setAdapter(spinnerArrayAdapter);

        dateE.setText(currentDate);


        dialogBuild.setView(listaPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);
        dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar kalendarz = Calendar.getInstance();
                int day= kalendarz.get(Calendar.DAY_OF_MONTH);
                int month= kalendarz.get(Calendar.MONTH);
                int year= kalendarz.get(Calendar.YEAR);

                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                picker=new DatePickerDialog(ListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateE.setText(day+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        dodajB.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                 myDB=new SqLiteManager(ListActivity.this);
              //  if(liczbaE.getText()<> "" && opisE.getText()<> "" && opisSzczegolE.getText()<> "" && dateE.getText() <> "") {
                if(liczbaE.getText().toString().trim().length() > 0&& opisE.getText().toString().trim().length() > 0 && opisSzczegolE.getText().toString().trim().length() > 0 && dateE.getText().toString().trim().length() > 0) {
                    myDB.addWpis(liczbaE.getText().toString().trim(), opisE.getText().toString().trim(), opisSzczegolE.getText().toString().trim(), dateE.getText().toString().trim(), 1);
                    dialog.dismiss();
                    zapiszListeDoArray();
                    buildRecyclerView();
                  //  Intent intent = new Intent(ListActivity.this, ListActivity.class);
                //    startActivity(intent);
                }
                else{
                    Toast.makeText(ListActivity.this,"BŁĄD",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
/*    void przeladuj() {
        abc();

    }*/
    void zapiszListeDoArray()
    {
        this.pressed=pressed;
        myDB =new SqLiteManager(ListActivity.instance);
        lista_historia =new ArrayList<>();
      /*  lista_id = new ArrayList<>();
        lista_kwota = new ArrayList<>();
        lista_opis = new ArrayList<>();
        lista_szczegol = new ArrayList<>();
        lista_data = new ArrayList<>();
        lista_kategoria = new ArrayList<>();
*/
        Cursor cursor = myDB.readAllHistoria();
        if(cursor.getCount()==0){
            Toast.makeText(ListActivity.instance,"Brak danych.",Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                ReadAllHistoriaResponse readAHR = new ReadAllHistoriaResponse();
                readAHR.id=cursor.getString(0);
                readAHR.kwota=cursor.getString(1);
                readAHR.opis=cursor.getString(2);
                readAHR.szczegol_opis=cursor.getString(3);
                readAHR.data=cursor.getString(4);
                readAHR.kategoria_id=cursor.getString(5);
                lista_historia.add(readAHR);
                /*lista_id.add(cursor.getString(0));
                lista_kwota.add(cursor.getString(1));
                lista_opis.add(cursor.getString(2));
                lista_szczegol.add(cursor.getString(3));
                lista_data.add(cursor.getString(4));
                lista_kategoria.add(cursor.getString(5));*/
            }
        }
    }
/*
    public void showInfoHelper(int position_) {
        this.position = position_;
    }*/

/*
    void wczytajZBazy(ExampleAdapter mAdapter_){
        this.mAdapter=mAdapter_;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.instance));

    }
    void abc(){
         mAdapter = new ExampleAdapter(ListActivity.instance,lista_historia,1);
        wczytajZBazy(mAdapter);
     }
*/
/*
            listaInfoEditText.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    listaInfoEditText.setVisibility(View.GONE);
                }
            }, 2000);
        }
        else
        {
            listaInfoEditText.setVisibility(View.GONE);
        }
    }*/
public void removeItem(int position){
    lista_historia.remove(position);
    mAdapter.notifyItemRemoved(position);
}
     public void buildRecyclerView(){
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
         mAdapter = new ExampleAdapter(ListActivity.this,lista_historia,1,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



     }

    /*public void setItmInfo(){
        lista_historia.set(5,);

   } */

    /*@Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

*/

   /* private void setOnClickListener() {
        listener = new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        };
    }
*/
    /*void notifyChanged(){
         mAdapter.notifyDataSetChanged();
      }
 */

    /*void deleteListRecord(String rowId_) {
        this.rowId = rowId_;
        //  Toast.makeText(this,"BŁĄD: "+ListActivity.this,Toast.LENGTH_SHORT).show();
        // myDB=new SqLiteManager(this);
        //         myDB=new SqLiteManager(this);
        // myDB =new SqLiteManager(ListActivity.this);
        if (myDB == null) {
            Toast.makeText(this, "Error usuwania, brak zmiennej myDB ", Toast.LENGTH_SHORT).show();
        } else {
            myDB.deleteOneRow(rowId);
        }
    }*/
/*

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(ListActivity.this, "KLIKNIETO A ONCLICK ZADZIALAL!", Toast.LENGTH_SHORT).show();

    }
*/
    public void createDialogOnLongPress(){
        /*this.rowId=rowId;
        this.opisValue=opisValue;
        this.position = position;
        */
        dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater li= LayoutInflater.from(getActivity(this));
        View listOnLongPressPopupView=li.inflate(R.layout.popup_lista_onlongpress,null);

        buttonListDelete = (Button) listOnLongPressPopupView.findViewById(R.id.buttonDeleteListOnPress);
        buttonListEdit = (Button) listOnLongPressPopupView.findViewById(R.id.buttonEditListOnPress);

        dialogBuild.setView(listOnLongPressPopupView);
        dialog = dialogBuild.create();
        dialog.show();

      /*  buttonListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqLiteManager myDB=new SqLiteManager(context);
                myDB.deleteOneRow(rowId);
                myDB.close();
                listActivity.zapiszListeDoArray();
                //   listActivity.przeladuj();
                mAdapter.notifyDataSetChanged();
                notifyItemRemoved(position);
                dialog.dismiss();


                //  notifyItemRemoved(position);
                //notifyItemRangeChanged(position, getItemCount());
                // listActivity.finish();
                //listActivity.startActivity(listActivity.getIntent());
            }
        });
*/

/*
        buttonListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                createNewEditDialog(rowId,kwotaValue,opisValue,szczegolOpisValue,dataValue);
            }
        });
*/
    }

    @Override
    public void onLongClick(int position) {
        createDialogOnLongPress();
    }

    @Override
    public void onItemClick(int position) {

        infoTextOnClick = findViewById(R.id.infoShowHelpOnClick);
        infoTextOnClick.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                infoTextOnClick.setVisibility(View.GONE);
            }
        }, 1500);

    }


}



