package com.esa.note.bookshelf;

import android.animation.ObjectAnimator;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

public abstract class HorizontalMoveListener implements View.OnTouchListener {

    private float xDown = 0, defaultX = 0, xml_5dp = 0;
    protected float distance = 0;
    private ObjectAnimator objectAnimator;
    public int animationDuration = 750;

    public HorizontalMoveListener(View view) {
        xml_5dp = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 5,
                view.getContext().getResources().getDisplayMetrics() );
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onCreate(view);
                view.setVisibility(View.VISIBLE);
                defaultX = view.getX();
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                onDragStart();
                xDown = motionEvent.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float movedX = motionEvent.getX();

                distance = movedX - xDown;
                float position = distance + view.getX();
                if (canMove(position)) {
                    onMove(view, position);
                }
                break;
            case MotionEvent.ACTION_UP:
                float newPosition = view.getX();
                if (view.getX() < defaultX + 5 && view.getX() > defaultX - 5) {
                    onClick(view);
                }

                if (newPosition > defaultX / 2 || distance > xml_5dp ) {
                returnToDefault(view);
                }
                else {
                move(view);
                }
                break;
        }
        return false;
    }

    public abstract void onCreate(View view);

    public abstract boolean canMove(float position);

    public abstract void onClick(View view);

    public void onDragStart() {

    }

    public void onMove(View view, float position) {
        view.setX(position);
    }

    public void returnToDefault(View view) {
        objectAnimator = ObjectAnimator.ofFloat(view, "translationX", defaultX);
        objectAnimator.setInterpolator(new DecelerateInterpolator(2));
        objectAnimator.setDuration(animationDuration);
        objectAnimator.start();

    }

    public void move(View view) {
        objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0);
        objectAnimator.setInterpolator(new DecelerateInterpolator(2));
        objectAnimator.setDuration(animationDuration);
        objectAnimator.start();

    }

    public ObjectAnimator getAnimator() {
        return objectAnimator;
    }
}
