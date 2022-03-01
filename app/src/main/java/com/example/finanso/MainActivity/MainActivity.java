package com.example.finanso.MainActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.Html;
import android.view.MenuInflater;
import android.widget.Button;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


import com.example.finanso.CategoryActivity.CategoryActivity;
import com.example.finanso.ListActivity.ListActivity;
import com.example.finanso.RecepitActivity.ParagonyActivity;
import com.example.finanso.R;
import com.example.finanso.StatisticsActivity.StatystykaActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
private DrawerLayout drawer;
private FloatingActionButton actionButton;
private int czyPopupDodaje ;
    public static final String CZY_POPUP_LISTA="com.example.application.example.CZY_POPUP_LISTA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plusPrzyciskAkcja();
        toolbarSet();
        buttonListClick();
        buttonKategorieClick();
        buttonParagonyClick();
        buttonStatystykaClick();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }
    }

    private void buttonListClick() {
//kliknięcie przycisku lista
        Button plus = (Button) findViewById(R.id.imageButton1);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonKategorieClick() {
//kliknięcie przycisku kategorie
        Button kategoriaB = (Button) findViewById(R.id.imageButton3);
        kategoriaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonParagonyClick() {
//kliknięcie przycisku paragony
        Button paragonyB = (Button) findViewById(R.id.imageButton4);
        paragonyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ParagonyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonStatystykaClick() {
//kliknięcie przycisku statystyka
            Button statystykaB = (Button) findViewById(R.id.imageButton2);
            statystykaB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, StatystykaActivity.class);
                    startActivity(intent);
                }
            });

        }


    private void toolbarSet() {
//toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void plusPrzyciskAkcja() {

        actionButton =findViewById(R.id.plusB);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    czyPopupDodaje=1;
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putExtra(CZY_POPUP_LISTA,czyPopupDodaje);
                    startActivity(intent);
               // createNewDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.opcje_menu,menu);
        menu.findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>wyszukaj daty</font>"));
        menu.findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>sortuj wg </font>"));
        menu.findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>pokaż</font>"));
        menu.findItem(R.id.item5).setTitle(Html.fromHtml("<font color='#000000'>ceny miedzy</font>"));

        return true;
    }

   public void onBackPress (){
       if(drawer.isDrawerOpen(GravityCompat.START)){
           drawer.closeDrawer(GravityCompat.START);
       }
       else{
           super.onBackPressed();
       }
   }

}