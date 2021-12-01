package com.example.finanso;

import static java.lang.Double.parseDouble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    Context context;
    private ArrayList<ExampleItem> mExampleList;
    private ArrayList lista_id,lista_kwota,lista_opis,lista_szczegol,lista_data,lista_kategoria;
    private AppCompatActivity mListActivity;
    private int layoutExample;
    private int rodzajExampleItem;

    public void odswiezListe() {
            notifyDataSetChanged();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout listaRelativeLayoutPozycja;
        public TextView listaOpisEditText;
        public TextView listaDataEditText;
        public TextView listaKwotaEditText;
        public TextView listaZlEditText;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
           // mImageView=itemView.findViewById(R.id.imageView);
            listaRelativeLayoutPozycja =itemView.findViewById(R.id.pozycjaLinia);
            listaOpisEditText =itemView.findViewById(R.id.textViewOpisRekord);
            listaDataEditText =itemView.findViewById(R.id.textViewDataRekord);
            listaKwotaEditText =itemView.findViewById(R.id.textViewCenaRekord);
         //   listaZlEditText =itemView.findViewById(R.id.textViewZlWRekord);
        }
    }
 ExampleAdapter(Context context,ArrayList lista_id,ArrayList lista_kwota,ArrayList lista_opis,ArrayList lista_szczegol,ArrayList lista_data,ArrayList lista_kategoria, int rodzajExampleItem){
        this.context =context;
        this.lista_id=lista_id;
        this.lista_kwota=lista_kwota;
        this.lista_opis=lista_opis;
        this.lista_szczegol=lista_szczegol;
        this.lista_data=lista_data;
        this.lista_kategoria=lista_kategoria;
     this.rodzajExampleItem = rodzajExampleItem;
     switch (rodzajExampleItem) {

         case 1: {
             layoutExample = R.layout.adapter_item_lista;
         }
         case 2: {
             layoutExample = R.layout.adapter_item_kategorie;
         }
         case 3: {
             layoutExample = R.layout.adapter_item_paragony;
         }

     }

}

    public ExampleAdapter(ArrayList<ExampleItem>exampleItems, AppCompatActivity listActivity, int rodzajExampleItem) {
        mExampleList = exampleItems;
        this.rodzajExampleItem = rodzajExampleItem;
        mListActivity = listActivity;
        switch (rodzajExampleItem) {

            case 1: {
                layoutExample = R.layout.adapter_item_lista;
            }
            case 2: {
                layoutExample = R.layout.adapter_item_kategorie;
            }
            case 3: {
                layoutExample = R.layout.adapter_item_paragony;
            }

        }
    }
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

        kwota= (String) lista_kwota.get(position);

        int priceColor;
             if(Double.parseDouble(kwota)>=0)
        {
            priceColor = context.getColor(R.color.greyGreen);
        }
        else {
                 priceColor = context.getColor(R.color.greyRed);
             }

        holder.listaOpisEditText.setText(String.valueOf(lista_opis.get(position)));
        holder.listaKwotaEditText.setText((String.valueOf(lista_kwota.get(position)))+"zł");
        holder.listaKwotaEditText.setTextColor(priceColor);
        holder.listaDataEditText.setText(String.valueOf(lista_data.get(position)));


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
        return lista_id.size();
    }

    public void filterList(ArrayList<ExampleItem>filteredList){
        mExampleList =filteredList;
        notifyDataSetChanged();
    }


}
