package com.example.finanso.RecepitActivity;


import static com.google.android.material.internal.ContextUtils.getActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import com.example.finanso.R;
import com.example.finanso.SQLite.SqLiteManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParagonyActivity  extends AppCompatActivity implements ReceiptAdapter.OnReceiptClickListener {

    private DrawerLayout drawer;
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
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 77;
    private CheckBox radioBurronGwarancja;
    private EditText dateE, opisParagony;
    private Button buttoPrzejdzDoZdjecia;
    private Button dodajB;
    private File imagePath;
    private File imagesFolder;

    private DatePickerDialog picker;
    public int czyPopupDodaj;
    String gwarancja = "nie";
    private String zdjecieS;
    private String kategorieA[] = {"Wybierz kategorię", "Rachunki", "Spożywcze", "Prezenty", "Chemia", "Remont"};
    private ImageButton dodajZdjecieParagonu;
    private Button dodajWpisParagonu;
    private boolean photoFile;
    public Uri imageUri = null;
    private Uri image_uri = null;
    private Button dodajUprawnienieZdjeciaParagonu;
    private int position;
    private String imageUrl;
    private ImageView imageParagon;
    private boolean boolFoto;
    private View parent;

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

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        //  mAdapter=new ExampleAdapter(exampleList, this,3);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        drawer = findViewById(R.id.drawer_layout_list);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.paragony_menu, menu);
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
             //   boolFoto=false;
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
 /*   private void createDialogImage(String imageUrl) {
        File imgFile = new File(imageUrl);
        View view = View.inflate(this,R.layout.popup_show_image,null);
        final View paragonyImageView = getLayoutInflater().inflate(R.layout.popup_show_image, null);
        imageParagon = (ImageView) paragonyImageView.findViewById(R.id.ImageViewParagon);
        parent =  findViewById(R.id.parentParagony);

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageParagon = (ImageView) paragonyImageView.findViewById(R.id.ImageViewParagon);
            imageParagon.setImageBitmap(myBitmap);
            *//*dialogBuild.setView(paragonyImageView);
            dialog.setContentView(R.layout.popup_show_image);
            dialog = dialogBuild.create();
            dialog.show();*//*
        } else {
            Toast.makeText(ParagonyActivity.this, "Problem ze zdjęciem", Toast.LENGTH_SHORT).show();
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(view,0,0,false);
       // popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        popupWindow.setFocusable(true);
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view,Gravity.NO_GRAVITY,location[0],location[1]+view.getHeight());

    }*/

    private void createDialogImage(String imageUrl) {
        dialogBuild = new AlertDialog.Builder(this);
        final View paragonyImageView = getLayoutInflater().inflate(R.layout.popup_show_image, null);
        File imgFile = new File(imageUrl);

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageParagon = (ImageView) paragonyImageView.findViewById(R.id.ImageViewParagon);
            imageParagon.setImageBitmap(myBitmap);
            dialogBuild.setView(paragonyImageView);
            dialog.setContentView(R.layout.popup_show_image);
            dialog = dialogBuild.create();
            dialog.show();
        } else {
            Toast.makeText(ParagonyActivity.this, "Problem ze zdjęciem", Toast.LENGTH_SHORT).show();
        }



        imageParagon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void createDialogOnLongPresss(int position) {
        dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(getActivity(this));
        View listOnLongPressPopupView = li.inflate(R.layout.popup_lista_onlongpress, null);

        Button buttonListDelete = (Button) listOnLongPressPopupView.findViewById(R.id.buttonDeleteListOnPress);
        Button buttonListEdit = (Button) listOnLongPressPopupView.findViewById(R.id.buttonEditListOnPress);

        dialogBuild.setView(listOnLongPressPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        dataEditPopup = lista_paragony.get(position);

        buttonListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB=new SqLiteManager(ParagonyActivity.this);
                myDB.deleteOneRowFromReceipt(dataEditPopup.id);
                dialog.dismiss();
                mAdapter.notifyDataSetChanged();
                finish();
                Intent intent = new Intent(ParagonyActivity.this, ParagonyActivity.class);
                startActivity(intent);
            }
        });


        buttonListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                createNewDialogEdit(dataEditPopup.id, dataEditPopup.opis, dataEditPopup.czygwarancja, dataEditPopup.data, dataEditPopup.zdjecie);
            }
        });

    }

    public void createNewDialog() {
        //pop up dodawanie rekordu
        // final View paragonyImageView = getLayoutInflater().inflate(R.layout.popup_show_image, null);
        imagePath = new File(imagesFolder + "/" + image_name);
        String zdjecieURL;
        zdjecieURL = imagePath.toString();
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
        opisParagony = (EditText) paragonyView.findViewById(R.id.OpisPopupParagony);
        radioBurronGwarancja = (CheckBox) paragonyView.findViewById(R.id.radioZGwarancja);
        dateE = (EditText) paragonyView.findViewById(R.id.dataE);
        dodajZdjecieParagonu = (ImageButton) paragonyView.findViewById(R.id.zdjęcieButtonParagon);
        dodajWpisParagonu = (Button) paragonyView.findViewById(R.id.dodajB);
        //  buttoPrzejdzDoZdjecia = (Button) paragonyView.findViewById(R.id.przejdzDoZdjecieButton);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        dateE.setText(currentDate);
        dialogBuild.setView(paragonyView);
        dialog = dialogBuild.create();
        //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                boolFoto = false;
            }
        });


        radioBurronGwarancja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBurronGwarancja.isChecked()) {
                    dateE.setVisibility(View.VISIBLE);
                } else {
                    dateE.setVisibility(View.INVISIBLE);
                }
            }
        });

        dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar kalendarz = Calendar.getInstance();
                int day = kalendarz.get(Calendar.DAY_OF_MONTH);
                int month = kalendarz.get(Calendar.MONTH);
                int year = kalendarz.get(Calendar.YEAR);

                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                picker = new DatePickerDialog(ParagonyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateE.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        dodajZdjecieParagonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    imagePath = openCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        dodajWpisParagonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBurronGwarancja.isChecked()) {
                    gwarancja = "tak";
                } else if (!radioBurronGwarancja.isChecked()) {
                    gwarancja = "nie";
                    Date d = new Date();
                    dateE.setText(DateFormat.format("MMMM d, yyyy ", d.getTime()));
                } else {
                    gwarancja = "error";
                    Date d = new Date();
                    dateE.setText(DateFormat.format("MMMM d, yyyy ", d.getTime()));
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

                } else if (imagesFolder + "/" + image_name == "null/null"||!boolFoto) {
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
            }
        });

    }

    public void createNewDialogEdit(String id, String opis, String czygwarancja, String data, String zdjecie) {
        //pop up edycja rekordu
        // final View paragonyImageView = getLayoutInflater().inflate(R.layout.popup_show_image, null);
        String zdjecieURL;
        zdjecieURL = zdjecie;

        dialogBuild = new AlertDialog.Builder(this);
        final View paragonyView = getLayoutInflater().inflate(R.layout.popup_paragony, null);
        buttoPrzejdzDoZdjecia = (Button) paragonyView.findViewById(R.id.wyswietlZdjecieButton);
        opisParagony = (EditText) paragonyView.findViewById(R.id.OpisPopupParagony);
        opisParagony.setText(opis);
        radioBurronGwarancja = (CheckBox) paragonyView.findViewById(R.id.radioZGwarancja);
        if (czygwarancja.equals("tak")) {
            radioBurronGwarancja.setChecked(true);
        } else if (czygwarancja.equals("nie")) {
            radioBurronGwarancja.setChecked(false);
        } else {
            radioBurronGwarancja.setChecked(false);
        }

        dateE = (EditText) paragonyView.findViewById(R.id.dataE);
        dateE.setText(data);

        dodajZdjecieParagonu = (ImageButton) paragonyView.findViewById(R.id.zdjęcieButtonParagon);
        if (zdjecieURL.equals("null/null")) {
            buttoPrzejdzDoZdjecia.setVisibility(View.GONE);
            dodajZdjecieParagonu.setVisibility(View.VISIBLE);
        } else {
            ///storage/emulated/0/picture/170322204240.jpg
            ///storage/emulated/0/picture/170322204240.jpg
            dodajZdjecieParagonu.setVisibility(View.GONE);
            buttoPrzejdzDoZdjecia.setVisibility(View.VISIBLE);
        }
        dodajWpisParagonu = (Button) paragonyView.findViewById(R.id.dodajB);


        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        //dateE.setText(currentDate);
        dialogBuild.setView(paragonyView);
        dialog = dialogBuild.create();
        //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dateE.setInputType(InputType.TYPE_NULL);

        if (radioBurronGwarancja.isChecked()) {
            dateE.setVisibility(View.VISIBLE);
        } else {
            dateE.setVisibility(View.INVISIBLE);
        }


        radioBurronGwarancja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBurronGwarancja.isChecked()) {
                    dateE.setVisibility(View.VISIBLE);
                } else {
                    dateE.setVisibility(View.INVISIBLE);
                }
            }
        });


        dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar kalendarz = Calendar.getInstance();
                int day = kalendarz.get(Calendar.DAY_OF_MONTH);
                int month = kalendarz.get(Calendar.MONTH);
                int year = kalendarz.get(Calendar.YEAR);

                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                picker = new DatePickerDialog(ParagonyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateE.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        dodajZdjecieParagonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

            }
        });
        buttoPrzejdzDoZdjecia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogImage(zdjecie);

            }
        });

        dodajWpisParagonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBurronGwarancja.isChecked()) {
                    gwarancja = "tak";
                } else if (!radioBurronGwarancja.isChecked()) {
                    gwarancja = "nie";
                    Date d = new Date();
                    dateE.setText(DateFormat.format("MMMM d, yyyy ", d.getTime()));
                } else {
                    gwarancja = "error";
                    Date d = new Date();
                    dateE.setText(DateFormat.format("MMMM d, yyyy ", d.getTime()));
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
                    dataPopup = currentDate.toString();

                }

                if ((opisParagony.getText().toString().trim().length() == 0||!boolFoto)&&zdjecieURL.equals("null/null")) {
                    Toast.makeText(ParagonyActivity.this, "Dodaj zdjęcie", Toast.LENGTH_SHORT).show();
                }
             /*   else if (zdjecieURL.equals("null/null")) {
                    Toast.makeText(ParagonyActivity.this, "Dodaj zdjęcie", Toast.LENGTH_SHORT).show();

                } */else {

                    myDBKat.updateReceiptData(id.trim(), opisParagony.getText().toString().trim(), gwarancja.trim(), dataPopup.trim());
                    dialog.dismiss();
                    zapiszParagonDoArray();
                    buildRecyclerView();
                    imagesFolder = null;
                    image_name = null;
                    dialog.dismiss();
                }

            }
        });

    }

    void zapiszParagonDoArray() {
        myDB = new SqLiteManager(ParagonyActivity.this);
        lista_paragony = new ArrayList<>();

        Cursor cursor = myDB.readAllParagony();
        if (cursor.getCount() == 0) {
            Toast.makeText(ParagonyActivity.this, "Brak danych.", Toast.LENGTH_SHORT).show();
        } else {
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
        // File file = new File(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/picture").getPath());
        imagesFolder = new File(contextCamera.getExternalFilesDir("picture"), "PHOTO_FINANSO");
        //  imagesFolder = new File(Environment.getExternalStorageDirectory().getPath(), "/picture");
        image_name = new SimpleDateFormat("ddMMyyHHmmss", Locale.US).format(new Date()) + ".jpg";

        //**  File image_file = new File(imagesFolder, image_name);
        Uri outputUri = Uri.fromFile(new File(imagesFolder + "/" + image_name));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }
        imageUri = Uri.fromFile(File.createTempFile("myImages", image_name));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        startActivityIfNeeded(intent, 1888);
        imagePath = new File(imagesFolder + "/" + image_name);
        return imagePath;
    }


//TODO
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(requestCode==100){
        if(grantResults.length>0&&grantResults[0] ==PackageManager.PERMISSION_GRANTED &&grantResults[1] ==PackageManager.PERMISSION_GRANTED){
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

    @Override
    public void onLongClick(int position) {
        this.position=position;
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

        if(resultCode==RESULT_OK){
            boolFoto = true;

        }
    }


}
