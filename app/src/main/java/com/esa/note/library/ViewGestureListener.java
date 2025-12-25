package com.esa.note.library;

import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;

public class ViewGestureListener {

    private View view;
    private CheckBox checkBox;
    private final long CLICK_DELAY = 2500;

    public void setView(View view) {
        this.view = view;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public static ViewGestureListener set(CheckBox checkBox) {
        ViewGestureListener viewGestureListener = new ViewGestureListener();
        viewGestureListener.setCheckBox(checkBox);
        return viewGestureListener;
    }

    public static ViewGestureListener set(View view) {
        ViewGestureListener viewGestureListener = new ViewGestureListener();
        viewGestureListener.setView(view);
        return viewGestureListener;
    }

    public interface OnDoubleClickListener {
        void onDoubleClick();
    }

    private int doubleClickFlag = 0;

    public void setOnDoubleClickListener(OnDoubleClickListener listener) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doubleClickFlag++;
                Handler handler = new Handler();
                Runnable clickRunnable = new Runnable() {
                    @Override
                    public void run() {
                        doubleClickFlag = 0;
                    }
                };
                if (doubleClickFlag == 1) {
//                        getEventX = event.getX();
//                        getEventY = event.getY();
                    handler.postDelayed(clickRunnable, CLICK_DELAY);
                } else if (doubleClickFlag == 2) {
                    listener.onDoubleClick();
                    doubleClickFlag = 0;
                }
            }
        });
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//                    doubleClickFlag++;
//                    Handler handler = new Handler();
//                    Runnable clickRunnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            doubleClickFlag = 0;
//                        }
//                    };
//                    if (doubleClickFlag == 1) {
////                        getEventX = event.getX();
////                        getEventY = event.getY();
//                        handler.postDelayed(clickRunnable, CLICK_DELAY);
//                    } else if (doubleClickFlag == 2) {
//                        listener.onDoubleClick();
//                        doubleClickFlag = 0;
//                    } else if (doubleClickFlag == 0) {
////                        getEventX = event.getX();
////                        getEventY = event.getY();
//                    }
//                }
//                if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
//                    doubleClickFlag = 0;
//                }
//                return false;
//            }
//        });
    }

    public interface OnCheckedListener{
        void checked();
        void unChecked();
    }
    public void setOnCheckedListener(OnCheckedListener listener) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    listener.checked();
                }
                else {
                    listener.unChecked();
                }
            }
        });
    }
}
