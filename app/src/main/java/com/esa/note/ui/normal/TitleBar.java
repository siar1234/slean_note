package com.esa.note.ui.normal;

import static com.esa.note.library.Public.MOVE;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.SORT_ENTITIES;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.ActivityTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.root.AppUi;

public class TitleBar extends AppUi {
    private TextView folder_title;
    protected Button btn_show_menu, btn_ShowPopupMenu, btn_okOnLeft, btn_okOnRight, moveButton, deleteButton, btn_cancel;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        folder_title = backgroundView.findViewById(R.id.folder_title);
        btn_show_menu = backgroundView.findViewById(R.id.btn_show_menu);
        btn_ShowPopupMenu = backgroundView.findViewById(R.id.btn_ShowPopupMenu);
        btn_okOnLeft = backgroundView.findViewById(R.id.btn_ok);
        btn_okOnRight = backgroundView.findViewById(R.id.btn_okOnRight);
        moveButton = backgroundView.findViewById(R.id.moveButton);
        deleteButton = backgroundView.findViewById(R.id.deleteButton);
        btn_cancel = backgroundView.findViewById(R.id.btn_cancel);
    }

    public interface OnMenuButtonListener {
        void onShowMenu();
        void onPopupMenuShow();
        void onMoveToOther();
        void onDeleteClicked();
        void doneMoveToOther();
        void backPressed();
        void cancelMoveToOther();
    }

    public void setOnMenuButtonListener(OnMenuButtonListener onMenuButtonListener) {
        btn_show_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuButtonListener.onShowMenu();
            }
        });
        btn_ShowPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonListener.onPopupMenuShow();
            }
        });
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonListener.onMoveToOther();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonListener.onDeleteClicked();
            }
        });

        btn_okOnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonListener.backPressed();
            }
        });

        btn_okOnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonListener.doneMoveToOther();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonListener.cancelMoveToOther();
            }
        });
    }

    @Override
    public void readyAllEvent() {
        super.readyAllEvent();


    }

    public void setTitle(String string) {
        folder_title.setText(string);
    }

    public void setActionBarMode(int how) {
        if (how == SORT_ENTITIES) {

            btn_okOnRight.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);

            btn_ShowPopupMenu.setVisibility(View.GONE);
            btn_show_menu.setVisibility(View.GONE);

            btn_okOnLeft.setVisibility(View.VISIBLE);
            moveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);

            folder_title.setVisibility(View.GONE);
        } else if (how == NORMAL) {

            btn_okOnRight.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);

            btn_ShowPopupMenu.setVisibility(View.VISIBLE);
            btn_show_menu.setVisibility(View.VISIBLE);

            btn_okOnLeft.setVisibility(View.GONE);
            moveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

            folder_title.setVisibility(View.VISIBLE);
        } else if (how == MOVE) {
            btn_okOnRight.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);

            btn_ShowPopupMenu.setVisibility(View.GONE);
            btn_show_menu.setVisibility(View.GONE);

            btn_okOnLeft.setVisibility(View.GONE);
            moveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

            folder_title.setVisibility(View.GONE);
        }
    }

    public int actionBarMode() {
        if (btn_ShowPopupMenu.getVisibility() == View.GONE && btn_okOnLeft.getVisibility() == View.VISIBLE) {
            return SORT_ENTITIES;
        } else if (btn_ShowPopupMenu.getVisibility() == View.VISIBLE && btn_okOnLeft.getVisibility() == View.GONE) {
            return NORMAL;
        } else {
            return MOVE;
        }
    }

    public void applyTheme(ActivityTheme activityTheme) {
        int titleColor = Color.parseColor(activityTheme.getTextColor());
        int iconColor = Color.parseColor(activityTheme.getIconColor());

        ColorApplier.applyColorToView(backgroundView, activityTheme.getBackground());
        folder_title.setTextColor(titleColor);
        folder_title.setHighlightColor(Color.parseColor(activityTheme.getTextSelectionColor()));

        btn_cancel.setTextColor(iconColor);
        btn_okOnRight.setTextColor(iconColor);
        btn_okOnLeft.setTextColor(iconColor);
        btn_show_menu.setTextColor(iconColor);
        btn_ShowPopupMenu.setTextColor(iconColor);

        moveButton.setTextColor(iconColor);
        deleteButton.setTextColor(iconColor);
    }

}
