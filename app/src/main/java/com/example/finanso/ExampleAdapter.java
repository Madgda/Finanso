package com.example.finanso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private AppCompatActivity mListActivity;
    private int layoutExample;
    private int rodzajExampleItem;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
           // mImageView=itemView.findViewById(R.id.imageView);
            mImageView=itemView.findViewById(R.id.pozycjaLinia);
            mTextView1=itemView.findViewById(R.id.textViewOpisRekord);
            mTextView2=itemView.findViewById(R.id.textViewDataRekord);
            mTextView3=itemView.findViewById(R.id.textViewCenaRekord);
            mTextView4=itemView.findViewById(R.id.textViewZlWRekord);
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
        ExampleItem currentItem =mExampleList.get(position);
        int priceColor;
       // String coloryy = "BlueDarker";
       //int color1 = R.color.BlueDarker;
        Context context = holder.mTextView3.getContext();
        if(currentItem.checkIfMoreThanZero())
        {
            priceColor=ContextCompat.getColor(mListActivity,R.color.greyGreen);//mListActivity.getColor(R.color.greyGreen);
        }
        else
        {
            priceColor=mListActivity.getColor(R.color.greyRed);

        }
       holder.mImageView.setBackgroundColor(currentItem.getColor1());
     holder.mTextView3.setTextColor(priceColor);
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<ExampleItem>filteredList){
        mExampleList =filteredList;
        notifyDataSetChanged();
    }


}
