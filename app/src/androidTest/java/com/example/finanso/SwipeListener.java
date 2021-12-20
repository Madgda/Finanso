package com.example.finanso;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SwipeListener implements View.OnTouchListener {
    GestureDetector gestureDetector;

     SwipeListener(View view){
        int prog =100;
        int prog2 =100;

        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
            return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                float xDiff = e2.getX() -e1.getX();
                float yDiff = e2.getY()- e1.getY();
                try{
                    if(Math.abs(xDiff)>Math.abs(yDiff)) {
                        //x wieksze od y
                        if (Math.abs(xDiff) > prog && Math.abs(velocityX) > prog2) {
                            //róznica x wieksza od prog
                            //roznica velocity x wieksza od prog2
                            if (xDiff > 0) {
                                //w prawo
                            } else {
                                //w lewo
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                    }
                    catch (Exception exception){
                        exception.printStackTrace();
                    }
                    return false;
            }
        };
        gestureDetector = new GestureDetector(listener);
        view.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }
}
