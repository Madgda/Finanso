package com.example.finanso;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class ExampleItem {
    private int mColor1;
    private int mColorPrice;
    private String mText1;
    private String mText2;
    private String mText3;
    private String stringColor;
    ExampleItem context = this;
    //ListActivity mListActivity;

    public ExampleItem(String color1,String text1, String text2, String text3)
    {
        stringColor=color1;
        mText1=text1;
        mText2=text2;
        mText3=text3;

    }

   /* public int getImageColor()
    {
        //colors1= getResources().getColorStateList(mColor1);
        return  mColor1;//.getColor(context, mColor1);
    }    */
    public Boolean checkIfMoreThanZero()
    {
        return Double.parseDouble(mText3)>=0;
    }
    /*
    public int getPriceColor()
    {

        //ResourcesCompat.getColor(R.color.BlueDarker);

        if(Double.parseDouble(mText3)>=0){
        mColorPrice=mListActivity.getColor(R.color.greyGreen);
        }
        else
        {
            mColorPrice= mListActivity.getColor(R.color.BlueDarker);

        }

        return mColorPrice;
    }

     */
    public int getColor1(){
        return Color.parseColor(stringColor);
    }
    public String getText1(){
        return mText1;
    }
    public String getText2(){
        return mText2;
    }
    public String getText3(){
        return mText3;
    }

}
