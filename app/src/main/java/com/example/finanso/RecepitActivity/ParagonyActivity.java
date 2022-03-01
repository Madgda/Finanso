package com.example.finanso.RecepitActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.ListActivity.ListAdapter;
import com.example.finanso.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ParagonyActivity  extends AppCompatActivity {

    private DrawerLayout drawer;
    private FloatingActionButton plus;
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem dodajLista;
    private AlertDialog.Builder dialogBuild;
    private AlertDialog dialog;
    private CheckBox radioBurronGwarancja;
    private EditText dateE,opisParagony;
    private Button dodajB;
    private DatePickerDialog picker;
    public int czyPopupDodaj;
    private String kategorieA[]={"Wybierz kategorię","Rachunki","Spożywcze","Prezenty","Chemia","Remont"};
    private ImageButton dodajZdjecieParagonu;
    private Button dodajWpisParagonu;
    private boolean photoFile;
    private Button dodajUprawnienieZdjeciaParagonu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragony);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Finanso");
        setSupportActionBar(toolbar);
        dodajParagonButtonOnClick();

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
/*
                filter(s.toString());
*/
            }
        });

     /*   exampleList.add(new ExampleItem("#FFFFFF","Toster. Gwarancja do 22.10.2023","22.11.2021","1"));
        exampleList.add(new ExampleItem("#FFFFFF","Lodówka. Gwarancja do 21.11.2021","12.10.2021","2"));
        exampleList.add(new ExampleItem("#FFFFFF","Czajnik. Gwarancja do 10.10.2022","02.10.2021","3"));
        exampleList.add(new ExampleItem("#FFFFFF","Toster. Gwarancja do 22.10.2023","22.11.2021","1"));
        exampleList.add(new ExampleItem("#FFFFFF","Lodówka. Gwarancja do 21.11.2021","12.10.2021","2"));
        exampleList.add(new ExampleItem("#FFFFFF","Czajnik. Gwarancja do 10.10.2022","02.10.2021","3"));
        exampleList.add(new ExampleItem("#FFFFFF","Toster. Gwarancja do 22.10.2023","22.11.2021","1"));
        exampleList.add(new ExampleItem("#FFFFFF","Lodówka. Gwarancja do 21.11.2021","12.10.2021","2"));
        exampleList.add(new ExampleItem("#FFFFFF","Czajnik. Gwarancja do 10.10.2022","02.10.2021","3"));
*/

      //  exampleList.add(new ExampleItem(R.drawable.person,"linia jeden","liniadwa","0"));
      /*  exampleList.add(new ExampleItem(R.drawable.menu,"linia jeden2","liniadwa2","0"));
        exampleList.add(new ExampleItem(R.drawable.settings,"linia jeden3","liniadwa3","0"));
        exampleList.add(new ExampleItem(R.drawable.person,"linia jeden","liniadwa","0"));
        exampleList.add(new ExampleItem(R.drawable.menu,"linia jeden2","liniadwa2","0"));
        exampleList.add(new ExampleItem(R.drawable.settings,"linia jeden3","liniadwa3","0"));
        exampleList.add(new ExampleItem(R.drawable.person,"linia jeden","liniadwa","0"));
        exampleList.add(new ExampleItem(R.drawable.menu,"linia jeden2","liniadwa2","0"));
        exampleList.add(new ExampleItem(R.drawable.settings,"linia jeden3","liniadwa3","0"));
        exampleList.add(new ExampleItem(R.drawable.person,"linia jeden","liniadwa","0"));
        exampleList.add(new ExampleItem(R.drawable.menu,"linia jeden2","liniadwa2","0"));
        exampleList.add(new ExampleItem(R.drawable.settings,"linia jeden3","liniadwa3","0"));*/

        mRecyclerView =findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
      //  mAdapter=new ExampleAdapter(exampleList, this,3);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        drawer = findViewById(R.id.drawer_layout_list);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.paragony_menu,menu);
        menu.findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>wyszukaj daty</font>"));
        menu.findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>sortuj wg </font>"));
        menu.findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>pokaż</font>"));
        return true;
    }


    private void dodajParagonButtonOnClick() {
//kliknięcie przycisku paragony
        Button dodajParagonButton = (Button) findViewById(R.id.dodajParagon);
        dodajParagonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog();
            }
        });
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
/*    private void filter(String text){
        ArrayList<ExampleItem> filteredList = new ArrayList<>();

        for(ExampleItem item : exampleList){
            if(item.getText1().toLowerCase().contains(text.toLowerCase())||item.getText2().toLowerCase().contains(text.toLowerCase())||item.getText3().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }*/
    public void createNewDialog(){
        //pop up dodawanie rekordu
        if(ContextCompat.checkSelfPermission(ParagonyActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ParagonyActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        dialogBuild = new AlertDialog.Builder(this);
        final View paragonyView=getLayoutInflater().inflate(R.layout.popup_paragony,null);
        opisParagony = (EditText) paragonyView.findViewById(R.id.OpisPopupParagony);
        radioBurronGwarancja = (CheckBox) paragonyView.findViewById(R.id.radioZGwarancja);
        dateE = (EditText) paragonyView.findViewById(R.id.dataE);
        dodajZdjecieParagonu = (ImageButton) paragonyView.findViewById(R.id.zdjęcieButtonParagon);
        dodajWpisParagonu = (Button) paragonyView.findViewById(R.id.dodajB);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());



        dateE.setText(currentDate);
        dialogBuild.setView(paragonyView);
        dialog = dialogBuild.create();
        //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);

        radioBurronGwarancja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioBurronGwarancja.isChecked())
                {
                    dateE.setVisibility(View.VISIBLE);
                }
                else
                {
                    dateE.setVisibility(View.INVISIBLE);
                }
            }
        });

        dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar kalendarz = Calendar.getInstance();
                int day= kalendarz.get(Calendar.DAY_OF_MONTH);
                int month= kalendarz.get(Calendar.MONTH);
                int year= kalendarz.get(Calendar.YEAR);

                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                picker=new DatePickerDialog(ParagonyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateE.setText(day+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });


        dodajZdjecieParagonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    openCamera();

            }
        });
        dodajWpisParagonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void openCamera() {

Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityIfNeeded(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
     /* Bundle extras = data.getExtras();
      Bitmap imageBitmap = (Bitmap) extras.get("data");*/
      //imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(requestCode==100){
        if(grantResults.length>0&&grantResults[0] ==PackageManager.PERMISSION_GRANTED &&grantResults[1] ==PackageManager.PERMISSION_GRANTED){
            openCamera();
        }
    }
    else{
        displayMessage(getBaseContext(),"Brak uprawnienia do robienia zdjęć");
    }

    }

    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
