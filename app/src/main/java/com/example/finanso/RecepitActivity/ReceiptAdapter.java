package com.example.finanso.RecepitActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.ListActivity.ReadAllHistoriaResponse;
import com.example.finanso.R;

import java.util.ArrayList;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {
    Context context;
    private ArrayList<ReadAllReceiptResponse> lista_paragony;
    private int layoutExample;
    private String listId;
    private OnReceiptClickListener mOnReceiptClickListener;

    public interface OnReceiptClickListener {
        void onLongClick(int position);
        void onItemClick(int position);
    }

    public  class ReceiptViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        private RelativeLayout receiptRelativeLayoutPozycja;
        private TextView receiptIdEditText;
        private TextView receiptDataEditText;
        private TextView receiptOpisEditText;

        OnReceiptClickListener onReceiptClickListener;

        public ReceiptViewHolder(@NonNull View itemView, OnReceiptClickListener onReceiptClickListener) {
            super(itemView);
            // mImageView=itemView.findViewById(R.id.imageView);
            receiptRelativeLayoutPozycja =itemView.findViewById(R.id.pozycjaLinia);
            receiptIdEditText =itemView.findViewById(R.id.textViewIdRekord);
            receiptOpisEditText =itemView.findViewById(R.id.textViewOpisRekord);
            receiptDataEditText =itemView.findViewById(R.id.textViewDataRekord);
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
    public ReceiptAdapter(Context context, ArrayList <ReadAllReceiptResponse> lista_paragony, OnReceiptClickListener onReceiptClickListener){
        this.context =context;
        this.mOnReceiptClickListener = onReceiptClickListener;
        this.lista_paragony = lista_paragony;
        layoutExample = R.layout.adapter_item_paragony;

    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutExample,parent,false);
        ReceiptViewHolder cvh = new ReceiptViewHolder(v, mOnReceiptClickListener);


        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {

        ReadAllReceiptResponse rowRARR = lista_paragony.get(position);
        listId =rowRARR.id;
        holder.receiptIdEditText.setText(rowRARR.id+ ".");
        if(rowRARR.czygwarancja.equals("tak")) {
            holder.receiptDataEditText.setText("gwarancja obowiązuje do: " + rowRARR.data);
        }
        else if (rowRARR.czygwarancja.equals("nie"))
        {
            holder.receiptDataEditText.setText("brak gwarancji");

        }
        else
        {
           holder.receiptDataEditText.setText("błąd");

       }
        holder.receiptOpisEditText.setText(rowRARR.opis);
        String getteXt = (String) holder.receiptDataEditText.getText();
    }

    @Override
    public int getItemCount() {
        return lista_paragony.size();
    }
/*

    public void filterList(ArrayList<ReadAllHistoriaResponse>filteredList){
        lista_kategoria =filteredList;
        notifyDataSetChanged();
    }
*/
public void filterList(ArrayList<ReadAllReceiptResponse>filteredList){
    lista_paragony =filteredList;
    notifyDataSetChanged();
}

}
