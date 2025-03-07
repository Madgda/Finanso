package com.example.finanso.RecepitActivity;


import static android.text.format.DateFormat.format;
import static com.google.android.material.internal.ContextUtils.getActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.R;
import com.example.finanso.SQLite.SqLiteManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ParagonyActivity extends AppCompatActivity implements ReceiptAdapter.OnReceiptClickListener {

    private RecyclerView mRecyclerView;
    private ReceiptAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public SqLiteManager myDB;
    public String dataPopup;
    public String image_name;
    private ArrayList<ReadAllReceiptResponse> lista_paragony;
    private AlertDialog.Builder dialogBuild;
    private ReadAllReceiptResponse dataEditPopup;
    private AlertDialog dialog;
    private CheckBox radioBurronGwarancja;
    private EditText dateE, opisParagony;
    private File imagePath;
    private File imagesFolder;
    private DatePickerDialog picker;
    String gwarancja = "nie";
    private String zdjecieS;
    private ImageButton dodajZdjecieParagonu;
    private Button dodajWpisParagonu;
    public Uri imageUri = null;
    private ImageView imageParagon;
    private boolean boolFoto;

    public ParagonyActivity() {
        myDB = new SqLiteManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragony);

        mRecyclerView = findViewById(R.id.recycler_view);

        zapiszParagonDoArray();
        buildRecyclerView();

        /*      OnBackPressedCallback callback = new OnBackPressedCallback(true *//* enabled by default *//*) {
            @Override
            public void handleOnBackPressed() {
                boolFoto = false;
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
*/
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Twoje paragony");
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

                filter(s.toString());

            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        //  mAdapter=new ExampleAdapter(exampleList, this,3);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void filter(String text) {
        ArrayList<ReadAllReceiptResponse> filteredList = new ArrayList<>();

        for (ReadAllReceiptResponse item : lista_paragony) {
            if (item.opis.toLowerCase().contains(text.toLowerCase()) || item.data.toLowerCase().contains(text.toLowerCase())) {
                {
                    filteredList.add(item);
                }
            }
            mAdapter.filterList(filteredList);
        }
    }

    private void dodajParagonButtonOnClick() {
//kliknięcie przycisku paragony
        Button dodajParagonButton = findViewById(R.id.dodajParagon);
        dodajParagonButton.setOnClickListener(v -> {
            //   boolFoto=false;
            createNewDialog();
        });
    }

    private void createDialogImage(String imageUrl) {
        dialogBuild = new AlertDialog.Builder(this);
        final View paragonyImageView = getLayoutInflater().inflate(R.layout.popup_show_image, null);
        File imgFile = new File(imageUrl);

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageParagon = paragonyImageView.findViewById(R.id.ImageViewParagon);
            imageParagon.setImageBitmap(myBitmap);
            dialogBuild.setView(paragonyImageView);
            dialog.setContentView(R.layout.popup_show_image);
            dialog = dialogBuild.create();
            dialog.show();
        } else {
            Toast.makeText(ParagonyActivity.this, "Problem ze zdjęciem", Toast.LENGTH_SHORT).show();
        }


        imageParagon.setOnClickListener(view -> dialog.dismiss());

    }

    @SuppressLint("NotifyDataSetChanged")
    private void createDialogOnLongPresss(int position) {
        dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(getActivity(this));
        View listOnLongPressPopupView = li.inflate(R.layout.popup_lista_onlongpress, null);

        Button buttonListDelete = listOnLongPressPopupView.findViewById(R.id.buttonDeleteListOnPress);
        Button buttonListEdit = listOnLongPressPopupView.findViewById(R.id.buttonEditListOnPress);

        dialogBuild.setView(listOnLongPressPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dataEditPopup = lista_paragony.get(position);

        buttonListDelete.setOnClickListener(view -> {
            myDB = new SqLiteManager(ParagonyActivity.this);
            myDB.deleteOneRowFromReceipt(dataEditPopup.id);
            dialog.dismiss();
            mAdapter.notifyDataSetChanged();
            finish();
            Intent intent = new Intent(ParagonyActivity.this, ParagonyActivity.class);
            startActivity(intent);
        });


        buttonListEdit.setOnClickListener(view -> {
            dialog.dismiss();
            createNewDialogEdit(dataEditPopup.id, dataEditPopup.opis, dataEditPopup.czygwarancja, dataEditPopup.data, dataEditPopup.zdjecie);
        });

    }

    @SuppressLint("SetTextI18n")
    public void createNewDialog() {
        //pop up dodawanie rekordu
        imagePath = new File(imagesFolder + "/" + image_name);
        if (ContextCompat.checkSelfPermission(ParagonyActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ParagonyActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        if (ContextCompat.checkSelfPermission(ParagonyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ParagonyActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);
        }
        dialogBuild = new AlertDialog.Builder(this);
        final View paragonyView = getLayoutInflater().inflate(R.layout.popup_paragony, null);
        opisParagony = paragonyView.findViewById(R.id.OpisPopupParagony);
        radioBurronGwarancja = paragonyView.findViewById(R.id.radioZGwarancja);
        dateE = paragonyView.findViewById(R.id.dataE);
        dodajZdjecieParagonu = paragonyView.findViewById(R.id.zdjęcieButtonParagon);
        dodajWpisParagonu = paragonyView.findViewById(R.id.dodajB);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        dateE.setText(currentDate);
        dialogBuild.setView(paragonyView);
        dialog = dialogBuild.create();
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);

        dialog.setOnDismissListener(dialogInterface -> boolFoto = false);


        radioBurronGwarancja.setOnClickListener(view -> {
            if (radioBurronGwarancja.isChecked()) {
                dateE.setVisibility(View.VISIBLE);
            } else {
                dateE.setVisibility(View.INVISIBLE);
            }
        });

        dateE.setOnClickListener(view -> {
            final Calendar kalendarz = Calendar.getInstance();
            int day = kalendarz.get(Calendar.DAY_OF_MONTH);
            int month = kalendarz.get(Calendar.MONTH);
            int year = kalendarz.get(Calendar.YEAR);

            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);
            picker = new DatePickerDialog(ParagonyActivity.this,
                    (datePicker, year1, month1, day1) -> dateE.setText(day1 + "/" + (month1 + 1) + "/" + year1),
                    year,
                    month,
                    day);
            picker.show();
        });


        dodajZdjecieParagonu.setOnClickListener(view -> {
            try {
                imagePath = openCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        dodajWpisParagonu.setOnClickListener(view -> {
            if (radioBurronGwarancja.isChecked()) {
                gwarancja = "tak";
            } else if (!radioBurronGwarancja.isChecked()) {
                gwarancja = "nie";
                Date d = new Date();
                dateE.setText(format("MMMM d, yyyy ", d.getTime()));
            } else {
                gwarancja = "error";
                Date d = new Date();
                dateE.setText(format("MMMM d, yyyy ", d.getTime()));
            }

            zdjecieS = "";

            if (gwarancja.equals("nie")) {
                dataPopup = "";
            } else if (gwarancja.equals("tak")) {
                dataPopup = dateE.getText().toString().trim();
            } else {
                dataPopup = currentDate;
            }


            if (imagePath == null) {
                zdjecieS = "";
            } else {
                zdjecieS = String.valueOf(imagePath);
            }


            SqLiteManager myDBKat = new SqLiteManager(ParagonyActivity.this);

            if (opisParagony.getText().toString().trim().length() == 0) {
                Toast.makeText(ParagonyActivity.this, "Nieprawidłowy opis", Toast.LENGTH_SHORT).show();

            } else if ((imagesFolder + "/" + image_name).equals("null/null") || !boolFoto) {
                Toast.makeText(ParagonyActivity.this, "Dodaj zdjęcie", Toast.LENGTH_SHORT).show();

            } else {
                myDBKat.addParagon(opisParagony.getText().toString().trim(), gwarancja.trim(), dataPopup.trim(), zdjecieS.trim());
                dialog.dismiss();
                zapiszParagonDoArray();
                buildRecyclerView();
                imagesFolder = null;
                image_name = null;
                dialog.dismiss();
            }
            imagePath = null;
        });

    }

    @SuppressLint("SetTextI18n")
    public void createNewDialogEdit(String id, String opis, String czygwarancja, String data, String zdjecie) {
        String zdjecieURL;
        zdjecieURL = zdjecie;

        dialogBuild = new AlertDialog.Builder(this);
        final View paragonyView = getLayoutInflater().inflate(R.layout.popup_paragony, null);
        Button buttoPrzejdzDoZdjecia = paragonyView.findViewById(R.id.wyswietlZdjecieButton);
        TextView textViewPopupParagony = paragonyView.findViewById(R.id.testPopupParagon);
        opisParagony = paragonyView.findViewById(R.id.OpisPopupParagony);
        opisParagony.setText(opis);
        radioBurronGwarancja = paragonyView.findViewById(R.id.radioZGwarancja);
        if (czygwarancja.equals("tak")) {
            radioBurronGwarancja.setChecked(true);
        } else if (czygwarancja.equals("nie")) {
            radioBurronGwarancja.setChecked(false);
        } else {
            radioBurronGwarancja.setChecked(false);
        }

        dateE = paragonyView.findViewById(R.id.dataE);
        dateE.setText(data);

        dodajZdjecieParagonu = paragonyView.findViewById(R.id.zdjęcieButtonParagon);
        if (zdjecieURL.equals("null/null")) {
            buttoPrzejdzDoZdjecia.setVisibility(View.GONE);
            dodajZdjecieParagonu.setVisibility(View.VISIBLE);
        } else {
            dodajZdjecieParagonu.setVisibility(View.GONE);
            buttoPrzejdzDoZdjecia.setVisibility(View.VISIBLE);
            textViewPopupParagony.setText("Szczegóły paragonu:");
        }
        dodajWpisParagonu = paragonyView.findViewById(R.id.dodajB);


        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        //dateE.setText(currentDate);
        dialogBuild.setView(paragonyView);
        dialog = dialogBuild.create();
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);

        if (radioBurronGwarancja.isChecked()) {
            dateE.setVisibility(View.VISIBLE);
        } else {
            dateE.setVisibility(View.INVISIBLE);
        }


        radioBurronGwarancja.setOnClickListener(view -> {
            if (radioBurronGwarancja.isChecked()) {
                dateE.setVisibility(View.VISIBLE);
            } else {
                dateE.setVisibility(View.INVISIBLE);
            }
        });


        dateE.setOnClickListener(view -> {
            final Calendar kalendarz = Calendar.getInstance();
            int day = kalendarz.get(Calendar.DAY_OF_MONTH);
            int month = kalendarz.get(Calendar.MONTH);
            int year = kalendarz.get(Calendar.YEAR);
            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);
            picker = new DatePickerDialog(ParagonyActivity.this, (datePicker, year1, month1, day1) -> dateE.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
            picker.show();
        });


        dodajZdjecieParagonu.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(ParagonyActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ParagonyActivity.this, new String[]{
                        Manifest.permission.CAMERA
                }, 100);
            }
            if (ContextCompat.checkSelfPermission(ParagonyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ParagonyActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 101);
            }
            try {
                imagePath = openCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        buttoPrzejdzDoZdjecia.setOnClickListener(view -> {
            dialog.dismiss();
            createDialogImage(zdjecie);

        });

        dodajWpisParagonu.setOnClickListener(view -> {
            if (radioBurronGwarancja.isChecked()) {
                gwarancja = "tak";
            } else if (!radioBurronGwarancja.isChecked()) {
                gwarancja = "nie";
                Date d = new Date();
                dateE.setText(format("MMMM d, yyyy ", d.getTime()));
            } else {
                gwarancja = "error";
                Date d = new Date();
                dateE.setText(format("MMMM d, yyyy ", d.getTime()));
            }

            zdjecieS = "";
            if (imagePath == null) {
                zdjecieS = "";
            } else {
                zdjecieS = String.valueOf(imagePath);
            }
            SqLiteManager myDBKat = new SqLiteManager(ParagonyActivity.this);

            if (gwarancja.equals("nie")) {
                dataPopup = "";
            } else if (gwarancja.equals("tak")) {
                dataPopup = dateE.getText().toString().trim();
            } else {
                dataPopup = currentDate;

            }

            if ((opisParagony.getText().toString().trim().length() == 0 || !boolFoto) && zdjecieURL.equals("null/null")) {
                Toast.makeText(ParagonyActivity.this, "Dodaj zdjęcie", Toast.LENGTH_SHORT).show();
            } else {

                myDBKat.updateReceiptData(id.trim(), opisParagony.getText().toString().trim(), gwarancja.trim(), dataPopup.trim());
                dialog.dismiss();
                zapiszParagonDoArray();
                buildRecyclerView();
                imagesFolder = null;
                image_name = null;
                dialog.dismiss();
            }

        });

    }

    void zapiszParagonDoArray() {
        myDB = new SqLiteManager(ParagonyActivity.this);
        lista_paragony = new ArrayList<>();

        Cursor cursor = myDB.readAllParagony();
        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                ReadAllReceiptResponse readARR = new ReadAllReceiptResponse();
                readARR.id = cursor.getString(0);
                readARR.opis = cursor.getString(1);
                readARR.czygwarancja = cursor.getString(2);
                readARR.data = cursor.getString(3);
                readARR.zdjecie = cursor.getString(4);
                lista_paragony.add(readARR);
            }
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ReceiptAdapter(ParagonyActivity.this, lista_paragony, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private File openCamera() throws IOException {
        boolFoto = false;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagesFolder = null;
        image_name = null;
        Context contextCamera = this.getApplicationContext();
        imagesFolder = new File(contextCamera.getExternalFilesDir("picture"), "PHOTO_FINANSO");
        image_name = new SimpleDateFormat("ddMMyyHHmmss", Locale.US).format(new Date()) + ".jpg";
        Uri outputUri = Uri.fromFile(new File(imagesFolder + "/" + image_name));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        imageUri = Uri.fromFile(File.createTempFile("myImages", image_name));
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityIfNeeded(intent, 1888);
        imagePath = new File(imagesFolder + "/" + image_name);
        return imagePath;
    }


    @Override
    public void onLongClick(int position) {
        createDialogOnLongPresss(position);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(ParagonyActivity.this, "Przytrzymaj jedną z pozycji by usunąć lub edytować ", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  String sciezka= toString(imageUri);

        if (resultCode == RESULT_OK) {
            boolFoto = true;

        }
    }


}
