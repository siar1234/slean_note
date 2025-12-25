package com.esa.note.ui.normal;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.root.AppUi;

public class PopupMenu extends AppUi {

    private Button  btn_showLinearView, btn_showGridView;
    private TextView sortByModifiedDate;
    private TextView sortByCreatedDate;
    private TextView sortByReverse;

    private int disabledIconColor, defaultIconColor;

    public boolean isShowing = false;

    public Animation HidePopupMenuAnim, ShowPopupMenuAnim;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        btn_showGridView = backgroundView.findViewById(R.id.showGrid);
        btn_showLinearView =  backgroundView.findViewById(R.id.showLinear);
        sortByModifiedDate =  backgroundView.findViewById(R.id.sortByModifiedDate);
        sortByCreatedDate =  backgroundView.findViewById(R.id.sortByCreatedDate);
        sortByReverse =  backgroundView.findViewById(R.id.sortByReverse);

        if (Build.VERSION.SDK_INT >= 21) {
            backgroundView.setElevation(20);
        }
        HidePopupMenuAnim = AnimationUtils.loadAnimation(backgroundView.getContext(), R.anim.anim_hide___menu_sort);
        HidePopupMenuAnim.setDuration(500);
        HidePopupMenuAnim.setInterpolator(new DecelerateInterpolator(2));
        HidePopupMenuAnim.setFillAfter(true);

        ShowPopupMenuAnim = AnimationUtils.loadAnimation(backgroundView.getContext(), R.anim.anim_show___menu_sort);
        ShowPopupMenuAnim.setDuration(500);
        ShowPopupMenuAnim.setInterpolator(new DecelerateInterpolator(2));
        ShowPopupMenuAnim.setFillAfter(true);

        HidePopupMenuAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_showGridView.setVisibility(View.GONE);
                btn_showLinearView.setVisibility(View.GONE);
                sortByCreatedDate.setVisibility(View.GONE);
                sortByModifiedDate.setVisibility(View.GONE);
                sortByReverse.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public interface AllViewClickListener {
        void sortByCreatedDate();
        void sortByModifiedDate();
        void sortReverse();
        void showLinearView();
        void showGridView();
    }

    public void setAllViewClickListener(AllViewClickListener listener) {
        sortByCreatedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sortByCreatedDate();
            }
        });

        sortByModifiedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sortByModifiedDate();
            }
        });

        sortByReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sortReverse();
            }
        });

        btn_showLinearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyLinearViewIsShown();
                listener.showLinearView();
            }
        });

        btn_showGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyGridViewIsShown();
                listener.showGridView();
            }
        });
    }

    public void show() {
        if (backgroundView.getVisibility() == View.GONE) {
            backgroundView.setVisibility(View.VISIBLE);
        }
        backgroundView.startAnimation(ShowPopupMenuAnim);
        btn_showGridView.setVisibility(View.VISIBLE);
        btn_showLinearView.setVisibility(View.VISIBLE);
        sortByCreatedDate.setVisibility(View.VISIBLE);
        sortByModifiedDate.setVisibility(View.VISIBLE);
        sortByReverse.setVisibility(View.VISIBLE);
        isShowing = true;
    }

    public void hide() {
        backgroundView.startAnimation(HidePopupMenuAnim);
        isShowing = false;
    }

    public void applyGridViewIsShown() {
            btn_showLinearView.setTextColor(disabledIconColor);
            btn_showGridView.setTextColor(defaultIconColor);
    }
    public void applyLinearViewIsShown() {
            btn_showLinearView.setTextColor(defaultIconColor);
            btn_showGridView.setTextColor(disabledIconColor);
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        int textColor =  Color.parseColor(appTheme.getMainActivityTheme().getPopupMenuTextColor());
        disabledIconColor = Color.parseColor(appTheme.getMainActivityTheme().getPopupMenuDisabledIconColor());
        defaultIconColor = Color.parseColor(appTheme.getMainActivityTheme().getPopupMenuIconColor());
        ColorApplier.applyColorToView(backgroundView, appTheme.getMainActivityTheme().getPopupMenuBackground());

        sortByCreatedDate.setTextColor(textColor);
        sortByReverse.setTextColor(textColor);
        sortByModifiedDate.setTextColor(textColor);
    }

}
