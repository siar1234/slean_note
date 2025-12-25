package com.esa.note.ui.normal;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.library.ViewGestureListener;
import com.esa.note.objects.Entity;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.dialog.DetailDialog;
import com.esa.note.ui.root.AppUi;

public class ReadNote extends AppUi {

//    private int doubleClickFlag = 0;
//    private final long CLICK_DELAY = 2500;
//    private float getEventX, getEventY, xml_40dp;

    private TextView show_title, show_content;

    private Button btn_edit, btn_back, btn_detail;

    private ScrollView scrollView;
    private final DetailDialog detailDialog = new DetailDialog();
    private Entity entity;

    //private int offset = -1;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        show_title = backgroundView.findViewById(R.id.show_title);
        show_content = backgroundView.findViewById(R.id.show_content);

        btn_edit = backgroundView.findViewById(R.id.btn_edit);
        btn_back = backgroundView.findViewById(R.id.btn_back_show);
        btn_detail = backgroundView.findViewById(R.id.btn_detail_show);

        scrollView = backgroundView.findViewById(R.id.scrollView);

        detailDialog.findAllViewById(context);

    }

    @Override
    public void readyAllDialogs(Activity activity) {
        super.readyAllDialogs(activity);
        detailDialog.readyDialog(activity);
    }

    public interface EventListener {
        void onStartNoteUpdate();
        void onBackPressed();
        void onStartNoteUpdate(int titleIndex, int contentIndex);
    }

    public void readyAllEvent(EventListener listener) {

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStartNoteUpdate();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackPressed();
            }
        });

        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog.setText(entity.getDetail());
                detailDialog.show();
            }
        });

        ViewGestureListener.set(show_title).setOnDoubleClickListener(new ViewGestureListener.OnDoubleClickListener() {
            @Override
            public void onDoubleClick() {
                listener.onStartNoteUpdate(-1, 1);
            }
        });
        ViewGestureListener.set(show_content).setOnDoubleClickListener(new ViewGestureListener.OnDoubleClickListener() {
            @Override
            public void onDoubleClick() {
                listener.onStartNoteUpdate(-1, 1);
            }
        });
        ViewGestureListener.set(scrollView).setOnDoubleClickListener(new ViewGestureListener.OnDoubleClickListener() {
            @Override
            public void onDoubleClick() {
                listener.onStartNoteUpdate(-1, 1);
            }
        });
//        show_title.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//                    doubleClickFlag++;
//                    Handler handler = new Handler();
//                    Runnable clickRunnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            doubleClickFlag = 0;
//                        }
//                    };
//                    if (doubleClickFlag == 1) {
//                        getEventX = event.getX();
//                        getEventY = event.getY();
//                        handler.postDelayed(clickRunnable, CLICK_DELAY);
//                    } else if (doubleClickFlag == 2) {
//                        if (getEventX - 500 <= event.getX() && getEventX + 500 >= event.getX()
//                                && getEventY - 500 <= event.getY() && getEventY + 500 >= event.getY()) {
//                            doubleClickFlag = 0;
//                            editNoteUiManager.setNoteWriteMode(entity);
//
//                            try {
//
////                                editNoteActivity.setSelection_title(offset);
//
//                                InputMethodManager inputMethodManager = (InputMethodManager) editNoteActivity.getSystemService(INPUT_METHOD_SERVICE);
//                                inputMethodManager.showSoftInput(editNoteActivity.getCurrentFocus(), 0);
//                            } catch (Exception e) {
////                                editNoteActivity.setSelection_title(0);
//                            }
//                        }
//                    } else if (doubleClickFlag == 0) {
//                        getEventX = event.getX();
//                        getEventY = event.getY();
//                    }
//                }
//                if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
//                    doubleClickFlag = 0;
//                }
//                return false;
//            }
//        });
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
//                    doubleClickFlag = 0;
//                }
//                return false;
//            }
//        });
    }
    public boolean isShowing() {
        if (backgroundView.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public void setTitleAndContent(Entity entity) {
        show_title.setText(entity.getTitle());
        show_content.setText(entity.getContent());
    }

    public void show() {
        backgroundView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        backgroundView.setVisibility(View.GONE);
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);

        int textColor = Color.parseColor(appTheme.getEditNoteActivityTheme().getTextColor());
        int iconColor =  Color.parseColor(appTheme.getEditNoteActivityTheme().getIconColor());
        int textHighLightColor = Color.parseColor(appTheme.getEditNoteActivityTheme().getTextSelectionColor());

        show_title.setTextColor(textColor);
        show_title.setHighlightColor(textHighLightColor);
        show_content.setTextColor(textColor);
        show_content.setHighlightColor(textHighLightColor);

        btn_back.setTextColor(iconColor);
        btn_detail.setTextColor(iconColor);
        btn_edit.setTextColor(iconColor);

        ColorApplier.applyColorToView(backgroundView, appTheme.getEditNoteActivityTheme().getBackground());

        detailDialog.applyTheme(appTheme.getEditNoteActivityTheme().getDialogTheme());
    }

    public void setAndApplyEntity(Entity entity) {
        this.entity = entity;
        if (entity.getTitle() == null) {
            show_title.setVisibility(View.GONE);
        }
        else {
            show_title.setText(entity.getTitle());
        }
        if (entity.getContent() != null) {
            show_content.setVisibility(View.VISIBLE);
            show_content.setText(entity.getContent());
        }

    }

    public int getScrollY() {
        return scrollView.getScrollY();
    }
}
