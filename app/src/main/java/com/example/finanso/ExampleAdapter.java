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


}
