package com.esa.note.ui.bottomsheet;

import static com.esa.note.library.Public.HIDE;
import static com.esa.note.library.Public.MIDDLE;
import static com.esa.note.library.Public.TOP;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.esa.note.R;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.BottomSheetTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.root.AppUi;

public class BottomSheet extends AppUi {

    protected View background_popup, handleView, emptyView;
    private float yDown = 0, parentsHeight = 0;

    protected int background, iconColor, textColor, handleColor, checkboxColor;

    protected boolean isOnlyMoveBottomToTop = false;

    public void setPopupBackground(View view) {
        background_popup = view;
    }

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        handleView = backgroundView.findViewById(R.id.handle_input_detail);
        //backgroundView.setVisibility(View.INVISIBLE);
        backgroundView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentsHeight = backgroundView.getHeight();
                backgroundView.setTranslationY(parentsHeight);
                backgroundView.setVisibility(View.VISIBLE);
                backgroundView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    @Override
    public void readyAllEvent() {
        super.readyAllEvent();
        handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:

                        yDown = event.getY();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        float movedY = event.getY();

                        float distanceY = movedY - yDown;

                        if (backgroundView.getY() + distanceY >= 0) {
                           // Log.d("hey", backgroundView.getY() + distanceY+"");
                            backgroundView.setY(backgroundView.getY() + distanceY);
                        }

                        break;

                    case MotionEvent.ACTION_UP:

                        float newPosition = backgroundView.getY();

                        if (newPosition < parentsHeight / 4) {
                            animate(TOP);
                        } else if (newPosition > parentsHeight / 1.5) {
                            animate(HIDE);
                        } else {
                            if (isOnlyMoveBottomToTop == false) {
                                animate(MIDDLE);
                            }
                            else {
                                animate(HIDE);
                            }
                        }
                        break;

                }
                return false;
            }
        });
    }

    public void animate(int how) {

        background_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(HIDE);
            }
        });

        ObjectAnimator anim___input_detail;
        ObjectAnimator anim___background_popup;

        if (how == MIDDLE) {
            anim___input_detail = ObjectAnimator.ofFloat(backgroundView, "translationY", parentsHeight / 2);
            anim___input_detail.setInterpolator(new DecelerateInterpolator(2));
            anim___input_detail.setDuration(500);
            anim___input_detail.start();

            anim___background_popup = ObjectAnimator.ofFloat(background_popup, "alpha", 1);
            anim___background_popup.setInterpolator(new DecelerateInterpolator(2));
            anim___background_popup.setDuration(500);
            anim___background_popup.start();

            background_popup.setClickable(true);
            background_popup.setFocusable(true);

            if (emptyView != null) {
                if (emptyView.getHeight() == 0) {
                    ViewGroup.LayoutParams layoutParams = emptyView.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = Math.round(parentsHeight / 2);
                    emptyView.setLayoutParams(layoutParams);
                }
                emptyView.setVisibility(View.VISIBLE);
            }
        } else if (how == TOP) {
            anim___input_detail = ObjectAnimator.ofFloat(backgroundView, "translationY", 0);
            anim___input_detail.setInterpolator(new DecelerateInterpolator(2));
            anim___input_detail.setDuration(500);
            anim___input_detail.start();

            anim___background_popup = ObjectAnimator.ofFloat(background_popup, "alpha", 1);
            anim___background_popup.setInterpolator(new DecelerateInterpolator(2));
            anim___background_popup.setDuration(500);
            anim___background_popup.start();

            background_popup.setClickable(true);
            background_popup.setFocusable(true);

            if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }

        } else if (how == HIDE) {
            anim___background_popup = ObjectAnimator.ofFloat(background_popup, "alpha", 0);
            anim___background_popup.setInterpolator(new DecelerateInterpolator(2));
            anim___background_popup.setDuration(500);
            anim___background_popup.start();

            anim___input_detail = ObjectAnimator.ofFloat(backgroundView, "translationY", parentsHeight);
            anim___input_detail.setInterpolator(new DecelerateInterpolator(2));
            anim___input_detail.setDuration(500);
            anim___input_detail.start();

            background_popup.setClickable(false);
            background_popup.setFocusable(false);
        }
    }

    public boolean isShowing() {
        if (backgroundView.getY() != parentsHeight) {
            return true;
        }
        else {
            return false;
        }
    }

    public void applyTheme(BottomSheetTheme bottomSheetTheme) {
        background = Color.parseColor(bottomSheetTheme.getBackground());
        handleColor = Color.parseColor(bottomSheetTheme.getHandleColor());
        textColor = Color.parseColor(bottomSheetTheme.getTextColor());
        iconColor = Color.parseColor(bottomSheetTheme.getIconColor());
        checkboxColor = Color.parseColor(bottomSheetTheme.getCheckboxColor());

        ColorApplier.applyColorToView(
                handleView.findViewById(R.id.view),
                bottomSheetTheme.getHandleColor() ,
                R.drawable.view_rounded_gray
        );
        ColorApplier.applyColorToView(
                backgroundView,
                bottomSheetTheme.getBackground(),
                R.drawable.view_rounded_top
        );
    }

    //commentStart
    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        String message = "You write wrong function";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.d("error", message);

    }
    //commentEnd

    public View getBackgroundView() {
        return backgroundView;
    }
}
