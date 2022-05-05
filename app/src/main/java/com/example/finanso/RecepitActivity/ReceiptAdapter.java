package com.example.finanso.RecepitActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.R;

import java.util.ArrayList;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {
    Context context;
    private ArrayList<ReadAllReceiptResponse> lista_paragony;
    private final int layoutExample;
    private final OnReceiptClickListener mOnReceiptClickListener;

    public interface OnReceiptClickListener {
        void onLongClick(int position);

        void onItemClick(int position);
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private final TextView receiptIdEditText;
        private final TextView receiptDataEditText;
        private final TextView receiptOpisEditText;

        OnReceiptClickListener onReceiptClickListener;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            // mImageView=itemView.findViewById(R.id.imageView);
            receiptIdEditText = itemView.findViewById(R.id.textViewIdRekord);
            receiptOpisEditText = itemView.findViewById(R.id.textViewOpisRekord);
            receiptDataEditText = itemView.findViewById(R.id.textViewDataRekord);
            this.onReceiptClickListener = mOnReceiptClickListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }


        @Override
        public boolean onLongClick(View view) {
            onReceiptClickListener.onLongClick(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            onReceiptClickListener.onItemClick(getAdapterPosition());
        }
    }

    public ReceiptAdapter(Context context, ArrayList<ReadAllReceiptResponse> lista_paragony, OnReceiptClickListener onReceiptClickListener) {
        this.context = context;
        this.mOnReceiptClickListener = onReceiptClickListener;
        this.lista_paragony = lista_paragony;
        layoutExample = R.layout.adapter_item_paragony;

    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutExample, parent, false);


        return new ReceiptViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {

        ReadAllReceiptResponse rowRARR = lista_paragony.get(position);
        holder.receiptIdEditText.setText(rowRARR.id + ".");
        if (rowRARR.czygwarancja.equals("tak")) {
            holder.receiptDataEditText.setText("gwarancja obowiązuje do: " + rowRARR.data);
        } else if (rowRARR.czygwarancja.equals("nie")) {
            holder.receiptDataEditText.setText("brak gwarancji");

        } else {
            holder.receiptDataEditText.setText("błąd");

        }
        holder.receiptOpisEditText.setText(rowRARR.opis);
    }

    @Override
    public int getItemCount() {
        return lista_paragony.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ReadAllReceiptResponse> filteredList) {
        lista_paragony = filteredList;
        notifyDataSetChanged();
    }

}
