package com.example.finanso.CategoryActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanso.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    private ArrayList<ReadAllCategoryResponse> lista_kategoria;
    private int layoutExample;
    private String listId;
    private OnCategoryClickListener mOnCategoryClickListener;

    public interface OnCategoryClickListener {
        void onLongClick(int position);
        void onItemClick(int position);
    }

    public  class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        private RelativeLayout categoryRelativeLayoutPozycja;
        private TextView categoryIdEditText;
        private TextView categorySzczegolEditText;
        private TextView categoryOpisEditText;

        OnCategoryClickListener onCategoryClickListener;

        public CategoryViewHolder(@NonNull View itemView, OnCategoryClickListener onCategoryClickListener) {
            super(itemView);
            // mImageView=itemView.findViewById(R.id.imageView);
            categoryRelativeLayoutPozycja =itemView.findViewById(R.id.pozycjaLinia);
            categoryIdEditText =itemView.findViewById(R.id.textViewIdRekord);
            categoryOpisEditText =itemView.findViewById(R.id.textViewOpisRekord);
            categorySzczegolEditText =itemView.findViewById(R.id.textSzczegolOpisRekord);
            this.onCategoryClickListener = mOnCategoryClickListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }


        @Override
        public boolean onLongClick(View view) {
            onCategoryClickListener.onLongClick(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            onCategoryClickListener.onItemClick(getAdapterPosition());
        }
    }
    public CategoryAdapter(Context context, ArrayList <ReadAllCategoryResponse> lista_kategoria, OnCategoryClickListener onCategoryClickListener){
        this.context =context;
        this.mOnCategoryClickListener = onCategoryClickListener;
        this.lista_kategoria = lista_kategoria;
                layoutExample = R.layout.adapter_item_kategorie;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutExample,parent,false);
        CategoryViewHolder cvh = new CategoryViewHolder(v, mOnCategoryClickListener);


        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        ReadAllCategoryResponse rowRACR = lista_kategoria.get(position);
        listId =rowRACR.id;
       holder.categoryIdEditText.setText(rowRACR.id+ ".");
        holder.categorySzczegolEditText.setText(rowRACR.opis);
        holder.categoryOpisEditText.setText(rowRACR.nazwa);
        holder.categoryRelativeLayoutPozycja.setBackgroundColor(Color.parseColor(rowRACR.kolor));
    }

    @Override
    public int getItemCount() {
        return lista_kategoria.size();
    }
/*

    public void filterList(ArrayList<ReadAllHistoriaResponse>filteredList){
        lista_kategoria =filteredList;
        notifyDataSetChanged();
    }
*/


}
