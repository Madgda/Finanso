package com.example.finanso;

import static com.google.android.material.internal.ContextUtils.getActivity;
import static java.lang.Double.parseDouble;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    Context context;
    private ArrayList<ExampleItem> mExampleList;
    private ArrayList<ReadAllHistoriaResponse> lista_historia;
    private AppCompatActivity mListActivity;
    private int layoutExample;
    private AlertDialog dialog;
    private int rodzajExampleItem;
    private AlertDialog.Builder dialogBuild;
    private int row_index=-1;
    private ListActivity listActivity = new ListActivity();
    private Button buttonListDelete, buttonListEdit;
    String rowId = "-1" ;
    private EditText liczbaE;
    private EditText opisE;
    private EditText opisSzczegolE;
    private EditText dateE;
    private Button zapiszB;
    private Spinner kategoriaS;


    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout listaRelativeLayoutPozycja;
        private TextView listaOpisEditText;
        private TextView listaDataEditText;
        private TextView listaKwotaEditText;

        TextView listaInfoEditText;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
           // mImageView=itemView.findViewById(R.id.imageView);
            listaRelativeLayoutPozycja =itemView.findViewById(R.id.pozycjaLinia);
            listaOpisEditText =itemView.findViewById(R.id.textViewOpisRekord);
            listaDataEditText =itemView.findViewById(R.id.textViewDataRekord);
            listaKwotaEditText =itemView.findViewById(R.id.textViewCenaRekord);
            listaInfoEditText =itemView.findViewById(R.id.textViewInfoKlikAdapter);
            //   listaZlEditText =itemView.findViewById(R.id.textViewZlWRekord);
        }


    }
 ExampleAdapter(Context context,ArrayList <ReadAllHistoriaResponse> lista_historia, int rodzajExampleItem){
        this.context =context;
        this.lista_historia = lista_historia;

        /*this.lista_id=lista_id;
        this.lista_kwota=lista_kwota;
        this.lista_opis=lista_opis;
        this.lista_szczegol=lista_szczegol;
        this.lista_data=lista_data;
        this.lista_kategoria=lista_kategoria;*/
     this.rodzajExampleItem = rodzajExampleItem;
     switch (rodzajExampleItem) {

         case 1: {
             layoutExample = R.layout.adapter_item_lista;
         }

     }

}

/*    public ExampleAdapter(ArrayList<ExampleItem>exampleItems, AppCompatActivity listActivity, int rodzajExampleItem) {
        mExampleList = exampleItems;
        this.rodzajExampleItem = rodzajExampleItem;
        mListActivity = listActivity;
        switch (rodzajExampleItem) {

            case 1: {
                layoutExample = R.layout.adapter_item_lista;
            }

        }
    }*/
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutExample,parent,false);
    ExampleViewHolder evh = new ExampleViewHolder(v);
    return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
       //currentItem = (ExampleAdapter) lista_id.get(position);
        String kwota;

        ReadAllHistoriaResponse rowRAHR = lista_historia.get(position);
        //ReadAllHistoriaResponse rowRAHR = new ReadAllHistoriaResponse();

        kwota= rowRAHR.kwota;

        int priceColor;
             if(Double.parseDouble(kwota)>=0)
        {
            priceColor = context.getColor(R.color.greyGreen);
        }
        else {
                 priceColor = context.getColor(R.color.greyRed);
             }

        holder.listaOpisEditText.setText(rowRAHR.opis);
        holder.listaKwotaEditText.setText(rowRAHR.kwota+" zł");
        holder.listaKwotaEditText.setTextColor(priceColor);
      // holder.listaInfoEditText.setTextColor(priceColor);
    //   holder.listaInfoEditText.setText("TROLOLOLOLO");
        holder.listaDataEditText.setText(rowRAHR.data);
       /* holder.listaKwotaEditText.setText((String.valueOf(lista_kwota.get(position)))+"zł");
        holder.listaKwotaEditText.setTextColor(priceColor);
        holder.listaDataEditText.setText(String.valueOf(lista_data.get(position)));*/
        //SwipeListener swipeListener = new SwipeListener(context);
       // swipeListener= new SwipeListener(listaRelativeLayoutPozycja);
       holder.listaRelativeLayoutPozycja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = holder.getAdapterPosition();
                notifyDataSetChanged();
            }


               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                    }
                }, 3000);*//*
              //  holder.listaInfoEditText.setVisibility(View.GONE);
          */

        });
       if(row_index==position)
       {
           holder.listaInfoEditText.setVisibility(View.VISIBLE);
           Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               public void run() {
                   holder.listaInfoEditText.setVisibility(View.GONE);
               }
           }, 2000);
       }
       else
       {
           holder.listaInfoEditText.setVisibility(View.GONE);
       }

        holder.listaRelativeLayoutPozycja.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context,"Dlaczego trzymasz tu ten palec?Puść!!!",Toast.LENGTH_SHORT).show();
                rowId= String.valueOf(holder.getAdapterPosition());
                createNewDialog(rowId);
                return false;
            }
        });
/*        holder.listaRelativeLayoutPozycja.OnLongClickListener{(

            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(context,"Dlaczego trzymasz tu ten palec?Puść!!!",Toast.LENGTH_SHORT).show();
                createDialogOnPress();

                return false;
            }
       });*/
        //Context context = holder.listaKwotaEditText.getContext();



       // }
      // holder.mImageView.setBackgroundColor(currentItem.getColor1());
     //holder.mTextView3.setTextColor(priceColor);
       // holder.mTextView1.setText(currentItem.getText1());
        //holder.mTextView2.setText(currentItem.getText2());
        //holder.mTextView3.setText(currentItem.getText3());
    }

    private int checkIfMoreThanZero(String kwota) {
         int mColorPrice;
        if(parseDouble(kwota)>=0){
            mColorPrice=mListActivity.getColor(R.color.greyGreen);
        }
        else
        {
            mColorPrice= mListActivity.getColor(R.color.BlueDarker);

        }

        return mColorPrice;
    }

    @Override
    public int getItemCount() {
        //return mExampleList.size();
        return lista_historia.size();
    }

    public void filterList(ArrayList<ExampleItem>filteredList){
        mExampleList =filteredList;
        notifyDataSetChanged();
    }
    public void createNewDialog(String rowId){
        this.rowId=rowId;
        dialogBuild = new AlertDialog.Builder(context);
        LayoutInflater li= LayoutInflater.from(getActivity(context));
        View listOnLongPressPopupView=li.inflate(R.layout.popup_lista_onlongpress,null);

        buttonListDelete = (Button) listOnLongPressPopupView.findViewById(R.id.buttonDeleteListOnPress);
        buttonListEdit = (Button) listOnLongPressPopupView.findViewById(R.id.buttonEditListOnPress);

        dialogBuild.setView(listOnLongPressPopupView);
        dialog = dialogBuild.create();
        dialog.show();

        buttonListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listActivity.deleteListRecord(rowId);
                dialog.dismiss();
            }
        });

        buttonListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                createNewEditDialog();
            }
        });
    }

    public void createNewEditDialog() {
        this.rowId=rowId;
        dialogBuild = new AlertDialog.Builder(context);
        LayoutInflater li= LayoutInflater.from(getActivity(context));
        View listEditPopupView=li.inflate(R.layout.popup_lista_edit,null);

        liczbaE = (EditText) listEditPopupView.findViewById(R.id.kwotaE);
        opisE = (EditText) listEditPopupView.findViewById(R.id.opisE);
        opisSzczegolE = (EditText) listEditPopupView.findViewById(R.id.opisDlugiE);
        dateE = (EditText) listEditPopupView.findViewById(R.id.dataE);
        zapiszB = (Button) listEditPopupView.findViewById(R.id.zapiszB);
        kategoriaS = (Spinner) listEditPopupView.findViewById(R.id.kategoriaS);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

         String kategorieA[]={"Wybierz kategorię","Rachunki","Spożywcze","Prezenty","Chemia","Remont"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, kategorieA);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        kategoriaS.setAdapter(spinnerArrayAdapter);

        dateE.setText(currentDate);

        dialogBuild.setView(listEditPopupView);
        dialog = dialogBuild.create();
        dialog.show();


        dateE.setInputType(InputType.TYPE_NULL);
        dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar kalendarz = Calendar.getInstance();
                int day = kalendarz.get(Calendar.DAY_OF_MONTH);
                int month = kalendarz.get(Calendar.MONTH);
                int year = kalendarz.get(Calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateE.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
        zapiszB.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
               // SqLiteManager myDB=new SqLiteManager(context);
                //  if(liczbaE.getText()<> "" && opisE.getText()<> "" && opisSzczegolE.getText()<> "" && dateE.getText() <> "") {
             //   if(liczbaE.getText().toString().trim().length() > 0&& opisE.getText().toString().trim().length() > 0 && opisSzczegolE.getText().toString().trim().length() > 0 && dateE.getText().toString().trim().length() > 0) {
               //     myDB.addWpis(liczbaE.getText().toString().trim(), opisE.getText().toString().trim(), opisSzczegolE.getText().toString().trim(), dateE.getText().toString().trim(), 1);
                    dialog.dismiss();
                   // zapiszListeDoArray();
                    //abc();
                    //wczytajZBazy();
                    //  Intent intent = new Intent(ListActivity.this, ListActivity.class);
                    //    startActivity(intent);
             //   }
               // else{
                    //Toast.makeText(context,"BŁĄD",Toast.LENGTH_SHORT).show();
               // }
            }
        });


    }



}
