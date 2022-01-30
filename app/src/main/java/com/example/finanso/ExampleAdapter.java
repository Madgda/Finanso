package com.example.finanso;

import static com.google.android.material.internal.ContextUtils.getActivity;
import static java.lang.Double.parseDouble;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearLayoutManager;
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
    static ListActivity listActivity = new ListActivity();
    private Button buttonListDelete, buttonListEdit;
    String rowId = "-1" ;
    private EditText liczbaE;
    private EditText opisE;
    private EditText opisSzczegolE;
    private EditText dateE;
    private Button zapiszB;
    private Spinner kategoriaS;
    private String opisValue;
    private String kwotaValue;
    private String dataValue;
    private String szczegolOpisValue;
    private String listId;
    private Integer position;
    private OnNoteListener mOnNoteListener;

    public interface OnNoteListener {
            void onLongClick(int position);
            void onItemClick(int position);
    }

    public  class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        private RelativeLayout listaRelativeLayoutPozycja;
        private TextView listaOpisEditText;
        private TextView listaDataEditText;
        private TextView listaSzczegolOpisEditText;
        private TextView listaKwotaEditText;
        private TextView listaInfoEditText;
        OnNoteListener onNoteListener;

        public ExampleViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
           // mImageView=itemView.findViewById(R.id.imageView);
            listaRelativeLayoutPozycja =itemView.findViewById(R.id.pozycjaLinia);
            listaOpisEditText =itemView.findViewById(R.id.textViewOpisRekord);
            listaDataEditText =itemView.findViewById(R.id.textViewDataRekord);
            listaKwotaEditText =itemView.findViewById(R.id.textViewCenaRekord);
            listaSzczegolOpisEditText =itemView.findViewById(R.id.textViewSzczegolRekord);
            listaInfoEditText =itemView.findViewById(R.id.textViewInfoKlikAdapter);
            //   listaZlEditText =itemView.findViewById(R.id.textViewZlWRekord);
            this.onNoteListener = mOnNoteListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public boolean onLongClick(View view) {
            onNoteListener.onLongClick(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onItemClick(getAdapterPosition());
        }
    }
        public ExampleAdapter(Context context,ArrayList <ReadAllHistoriaResponse> lista_historia, int rodzajExampleItem, OnNoteListener onNoteListener){
            this.context =context;
            this.mOnNoteListener = onNoteListener;
            this.lista_historia = lista_historia;

            this.rodzajExampleItem = rodzajExampleItem;
                switch (rodzajExampleItem) {

                case 1: {
                layoutExample = R.layout.adapter_item_lista;
                    }

     }

}

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutExample,parent,false);
    ExampleViewHolder evh = new ExampleViewHolder(v,mOnNoteListener);
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
        listId =rowRAHR.id;
        holder.listaOpisEditText.setText(rowRAHR.opis);
        holder.listaKwotaEditText.setText(rowRAHR.kwota+" zł");
        holder.listaKwotaEditText.setTextColor(priceColor);
        holder.listaSzczegolOpisEditText.setText(rowRAHR.szczegol_opis);
        holder.listaDataEditText.setText(rowRAHR.data);

     /*   if (rowRAHR.pressed=="0")
        {
            holder.listaInfoEditText.setVisibility(View.GONE);
        }
        else{
            holder.listaInfoEditText.setVisibility(View.VISIBLE);
        }*/
/*


        holder.listaRelativeLayoutPozycja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = holder.getAdapterPosition();
                notifyDataSetChanged();
            }

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
*/

//TODO POPUP ONLONG CLICK
  /*      holder.listaRelativeLayoutPozycja.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Toast.makeText(context,"Dlaczego trzymasz tu ten palec?Puść!!!",Toast.LENGTH_SHORT).show();
                rowId= String.valueOf(listId);
                opisValue = holder.listaOpisEditText.getText().toString();
                kwotaValue = rowRAHR.kwota.toString();
                dataValue = holder.listaDataEditText.getText().toString();
                szczegolOpisValue = holder.listaSzczegolOpisEditText.getText().toString();
                int positionDialog= holder.getAdapterPosition();
                createNewDialog(positionDialog, rowId, kwotaValue, opisValue, szczegolOpisValue, dataValue);
                return false;
            }
        });*/
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
    public void createNewDialog(Integer position, String rowId, String kwotaValue,String opisValue,String szczegolOpisValue, String dataValue){
        this.rowId=rowId;
        this.opisValue=opisValue;
        this.position = position;
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
                SqLiteManager myDB=new SqLiteManager(context);
                myDB.deleteOneRow(rowId);
                myDB.close();
                listActivity.zapiszListeDoArray();
             //   listActivity.przeladuj();
                notifyDataSetChanged();
                notifyItemRemoved(position);
                dialog.dismiss();


                //  notifyItemRemoved(position);
                //notifyItemRangeChanged(position, getItemCount());
               // listActivity.finish();
                //listActivity.startActivity(listActivity.getIntent());
            }
        });


        buttonListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                createNewEditDialog(rowId,kwotaValue,opisValue,szczegolOpisValue,dataValue);
            }
        });
    }

    public void infoTextShow(Integer position) {
        lista_historia.get(position);

    }
/*    private void deleteFromArray(Integer position) {
        this.position=position;
        lista_historia.remove(position);
    }*/



    public void createNewEditDialog(String rowId, String kwotaValue,String opisValue,String szczegolOpisValue, String dataValue) {
        this.rowId=rowId;
        this.opisValue=opisValue;
        dialogBuild = new AlertDialog.Builder(context);
        LayoutInflater li= LayoutInflater.from(getActivity(context));
        View listEditPopupView=li.inflate(R.layout.popup_lista_edit,null);

        liczbaE = (EditText) listEditPopupView.findViewById(R.id.kwotaE);
        liczbaE.setText(kwotaValue);
        opisE = (EditText) listEditPopupView.findViewById(R.id.opisE);
        opisE.setText(opisValue);
        opisSzczegolE = (EditText) listEditPopupView.findViewById(R.id.opisDlugiE);
        opisSzczegolE.setText(szczegolOpisValue);
        dateE = (EditText) listEditPopupView.findViewById(R.id.dataE);
        dateE.setText(dataValue);
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
               SqLiteManager myDB=new SqLiteManager(context);
                      if(liczbaE.getText().toString().trim().length() > 0&& opisE.getText().toString().trim().length() > 0 && opisSzczegolE.getText().toString().trim().length() > 0 && dateE.getText().toString().trim().length() > 0) {
                          myDB.updateData(rowId,liczbaE.getText().toString().trim(), opisE.getText().toString().trim(), opisSzczegolE.getText().toString().trim(), dateE.getText().toString().trim(), 1);
                          dialog.dismiss();
                          notifyDataSetChanged();
                           Intent intent = new Intent(context, listActivity.getClass());
                            listActivity.startActivity(intent);
                             }
                           else{
                          Toast.makeText(context,"BŁĄD",Toast.LENGTH_SHORT).show();
                           }

            }
        });


    }



}
