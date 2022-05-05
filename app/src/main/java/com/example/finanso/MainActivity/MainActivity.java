package com.example.finanso.MainActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finanso.CategoryActivity.CategoryActivity;
import com.example.finanso.ListActivity.ListActivity;
import com.example.finanso.R;
import com.example.finanso.RecepitActivity.ParagonyActivity;
import com.example.finanso.StatisticsActivity.StatystykaActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private int czyPopupDodaje;
    public static final String CZY_POPUP_LISTA = "com.example.application.example.CZY_POPUP_LISTA";

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

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
    }

    private void buttonListClick() {
//kliknięcie przycisku lista
        Button plus = findViewById(R.id.imageButton1);
        plus.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }

    private void buttonKategorieClick() {
//kliknięcie przycisku kategorie
        Button kategoriaB = findViewById(R.id.imageButton3);
        kategoriaB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        });
    }

    private void buttonParagonyClick() {
//kliknięcie przycisku paragony
        Button paragonyB = findViewById(R.id.imageButton4);
        paragonyB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ParagonyActivity.class);
            startActivity(intent);
        });
    }

    private void buttonStatystykaClick() {
//kliknięcie przycisku statystyka
        Button statystykaB = findViewById(R.id.imageButton2);
        statystykaB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatystykaActivity.class);
            startActivity(intent);
        });

    }


    private void toolbarSet() {
//toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void plusPrzyciskAkcja() {

        FloatingActionButton actionButton = findViewById(R.id.plusB);
        actionButton.setOnClickListener(view -> {
            czyPopupDodaje = 1;
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra(CZY_POPUP_LISTA, czyPopupDodaje);
            startActivity(intent);
            // createNewDialog();
        });

    }


}