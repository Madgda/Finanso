package com.example.finanso.ListActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    Context context;
    private ArrayList<ReadAllHistoriaResponse> lista_historia;
    private final int layoutExample;

    private final OnlistClickListener mOnlistClickListener;

    public interface OnlistClickListener {
        void onLongClick(int position);

        void onItemClick(int position);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private final TextView listaOpisEditText;
        private final TextView listaDataEditText;
        private final TextView listaSzczegolOpisEditText;
        private final TextView listaKwotaEditText;
        OnlistClickListener onlistClickListener;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            // mImageView=itemView.findViewById(R.id.imageView);
            listaOpisEditText = itemView.findViewById(R.id.textViewOpisRekord);
            listaDataEditText = itemView.findViewById(R.id.textViewDataRekord);
            listaKwotaEditText = itemView.findViewById(R.id.textViewIdRekord);
            listaSzczegolOpisEditText = itemView.findViewById(R.id.textViewSzczegolRekord);
            //   listaZlEditText =itemView.findViewById(R.id.textViewZlWRekord);
            this.onlistClickListener = mOnlistClickListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public boolean onLongClick(View view) {
            onlistClickListener.onLongClick(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            onlistClickListener.onItemClick(getAdapterPosition());
        }
    }

    public ListAdapter(Context context, ArrayList<ReadAllHistoriaResponse> lista_historia, OnlistClickListener onlistClickListener) {
        this.context = context;
        this.mOnlistClickListener = onlistClickListener;
        this.lista_historia = lista_historia;
        layoutExample = R.layout.adapter_item_lista;

    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutExample, parent, false);
        return new ListViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        String czyWplyw;

        ReadAllHistoriaResponse rowRAHR = lista_historia.get(position);
        czyWplyw = rowRAHR.czyWplyw;

        int priceColor;
        if (czyWplyw.equals("tak")) {
            priceColor = context.getColor(R.color.greyGreen);
        } else if (czyWplyw.equals("nie")) {
            priceColor = context.getColor(R.color.greyRed);
        } else {
            priceColor = context.getColor(R.color.black);
        }
        holder.listaOpisEditText.setText(rowRAHR.opis);
        holder.listaKwotaEditText.setText(rowRAHR.kwota + " zł");
        holder.listaKwotaEditText.setTextColor(priceColor);
        holder.listaSzczegolOpisEditText.setText(rowRAHR.szczegol_opis);
        holder.listaDataEditText.setText(rowRAHR.data);
        holder.listaOpisEditText.setTextColor(Color.parseColor(rowRAHR.kategoria_kolor));
    }

    @Override
    public int getItemCount() {
        //return mExampleList.size();
        return lista_historia.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ReadAllHistoriaResponse> filteredList) {
        lista_historia = filteredList;
        notifyDataSetChanged();
    }


}


