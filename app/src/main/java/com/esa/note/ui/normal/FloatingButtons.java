package com.esa.note.ui.normal;

import static com.esa.note.library.Public.HIDE;
import static com.esa.note.library.Public.SHOW;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.bookshelf.HorizontalMoveListener;
import com.esa.note.library.Public;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.root.AppUi;

import java.util.Random;

public class FloatingButtons extends AppUi {

    public static final int DRAGGED_BUTTON_ADD = 1;
    public LinearLayout folderButtonView, noteButtonView, plusButtonView;
    public TextView noteButtonText, folderButtonText;
    private int StateOfPlusButton = HIDE;
    private float Width_PlusButtonView = 0, Width_folderButtonView = 0, Width_noteButtonView = 0, xDown = 0, defaultPosition,
            beforeMove = 0, alreadyHide_noteButton = 0, alreadyHide_folderButton = 0, xml_15dp = 0;

    private Button plusButton, noteButton, folderButton;
    private View restoringView;
    private ObjectAnimator returnAnimation, anim___btn_folder, anim___btn_note, anim___btn_add;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        folderButtonView = backgroundView.findViewById(R.id.folderButtonView);
        noteButtonView = backgroundView.findViewById(R.id.noteButtonView);
        plusButtonView = backgroundView.findViewById(R.id.plusButtonView);
        plusButton = backgroundView.findViewById(R.id.plusButton);
        noteButton = backgroundView.findViewById(R.id.noteButton);
        folderButton = backgroundView.findViewById(R.id.folderButton);
        noteButtonText = backgroundView.findViewById(R.id.noteButtonText);
        folderButtonText = backgroundView.findViewById(R.id.folderButtonText);
        restoringView = backgroundView.findViewById(R.id.restoringView);
        
        plusButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                defaultPosition = plusButton.getX();
                xml_15dp = plusButton.getWidth();
                xml_15dp = xml_15dp / 4;
                plusButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        plusButtonView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Width_PlusButtonView = plusButtonView.getWidth();
                plusButtonView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        noteButtonView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Width_noteButtonView = noteButtonView.getWidth();

                noteButtonView.setTranslationX(Width_noteButtonView);

                noteButtonView.setVisibility(View.VISIBLE);
                noteButtonView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        noteButtonText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                alreadyHide_noteButton = (Width_noteButtonView - noteButtonText.getWidth()) / 4;
                noteButtonText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        folderButtonView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Width_folderButtonView = folderButtonView.getWidth();
                folderButtonView.setTranslationX(Width_folderButtonView);
                folderButtonView.setVisibility(View.VISIBLE);
                folderButtonView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        folderButtonText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                alreadyHide_folderButton = (Width_folderButtonView - folderButtonText.getWidth()) / 6;
                folderButtonText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {

            plusButton.setElevation(15);
            noteButton.setElevation(15);
            folderButton.setElevation(15);

        }
    }

    public interface OnButtonClickListener {
        void onNoteClick();
        void onFolderClick();
    }
    private boolean sleeping = true;
    public void setOnButtonClickListener(OnButtonClickListener listener) {

        ReturnToFirst_plusButton();
        jumpButton(0);

        Handler handler = new Handler(Looper.getMainLooper());

        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                    jumpButton(0);
                    handler.postDelayed(this, 5000);

            }
        };

        handler.postDelayed(runnableCode, 20000);
        plusButton.setOnTouchListener(new HorizontalMoveListener(plusButton) {
            @Override
            public void onDragStart() {
                returnAnimation.end();
            }

            @Override
            public void onCreate(View view) {

            }

            @Override
            public boolean canMove(float position) {
                return position > 0;
            }

            @Override
            public void onClick(View view) {
                if (StateOfPlusButton == Public.HIDE) {
                    Show__btn_note___btn_folder();

                    StateOfPlusButton = SHOW;
                } else if (StateOfPlusButton == SHOW) {
                    Hide__btn_note___btn_folder(Public.NORMAL);

                    StateOfPlusButton = Public.HIDE;
                }
            }

            @Override
            public void onMove(View view, float position) {
                super.onMove(view, position);
                if (StateOfPlusButton == SHOW) {
                    if (position > Width_PlusButtonView / 2) {
                        anim___btn_folder = ObjectAnimator.ofFloat(folderButtonView, "translationX", alreadyHide_folderButton);
                        anim___btn_note = ObjectAnimator.ofFloat(noteButtonView, "translationX", alreadyHide_noteButton);
                        anim___btn_folder.setInterpolator(new DecelerateInterpolator(2));
                        anim___btn_note.setInterpolator(new DecelerateInterpolator(2));

                        anim___btn_folder.setDuration(1000);
                        anim___btn_folder.start();

                        anim___btn_note.setDuration(750);
                        anim___btn_note.start();
                    } else {
                        Show__btn_note___btn_folder();
                    }
                }
            }
        });
        plusButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        returnAnimation.end();

                        xDown = event.getX();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        //Log.d("floating button", "move");

                        float movedX = event.getX();

                        float distanceX = movedX - xDown;
                        if (plusButton.getX() + distanceX > 0) {
                            plusButton.setX(plusButton.getX() + distanceX);
                        }

                        if (StateOfPlusButton == SHOW) {
                            if (plusButton.getX() > Width_PlusButtonView / 2) {
                                anim___btn_folder = ObjectAnimator.ofFloat(folderButtonView, "translationX", alreadyHide_folderButton);
                                anim___btn_note = ObjectAnimator.ofFloat(noteButtonView, "translationX", alreadyHide_noteButton);
                                anim___btn_folder.setInterpolator(new DecelerateInterpolator(2));
                                anim___btn_note.setInterpolator(new DecelerateInterpolator(2));

                                anim___btn_folder.setDuration(1000);
                                anim___btn_folder.start();

                                anim___btn_note.setDuration(750);
                                anim___btn_note.start();
                            } else {
                                Show__btn_note___btn_folder();
                            }
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        if (plusButton.getX() < defaultPosition + 5 && plusButton.getX() > defaultPosition - 5) {
                            handler.removeCallbacks(runnableCode);
                            handler.postDelayed(runnableCode, 20000);
                            if (StateOfPlusButton == Public.HIDE) {

                                Show__btn_note___btn_folder();

                                StateOfPlusButton = SHOW;
                            } else if (StateOfPlusButton == SHOW) {
                                Hide__btn_note___btn_folder(Public.NORMAL);

                                StateOfPlusButton = Public.HIDE;
                            }
                        }

                        if (plusButton.getX() > Width_PlusButtonView / 2) {
                            returnAnimation = ObjectAnimator.ofFloat(plusButton, "translationX", Width_PlusButtonView);
                            returnAnimation.setDuration(750);
                            returnAnimation.setInterpolator(new DecelerateInterpolator(2));
                            returnAnimation.start();
                            Hide__btn_note___btn_folder(DRAGGED_BUTTON_ADD);

                            restoringView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    switch (event.getActionMasked()) {

                                        case MotionEvent.ACTION_DOWN:
                                            beforeMove = event.getX();
                                            break;
                                        case MotionEvent.ACTION_MOVE:
                                            float movedX = event.getX();

                                            float distanceX = movedX - beforeMove;

                                            if (-distanceX > xml_15dp) {
                                                ReturnToFirst_plusButton();
                                                StateOfPlusButton = Public.HIDE;
                                            }

                                            break;

                                        case MotionEvent.ACTION_UP:
                                            break;

                                    }

                                    return false;
                                }
                            });

                        } else {
                            ReturnToFirst_plusButton();
                        }
                        break;

                }
                return false;
            }
        });
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNoteClick();
            }
        });

        folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFolderClick();
            }
        });
    }

    @Override
    public void readyAllEvent() {
        super.readyAllEvent();
    }
    AnimatorSet animatorSet = new AnimatorSet();
    public void jumpButton(int number) {
        Random random = new Random();

        // -50부터 -100까지의 랜덤 정수 생성
        int randomValue = random.nextInt(51) - 100;
        ObjectAnimator jump = ObjectAnimator.ofFloat(plusButton, "translationY", 0, randomValue);
            // jump.setInterpolator(new DecelerateInterpolator(2));
            jump.setDuration(500);
            jump.start();

            ObjectAnimator landing = ObjectAnimator.ofFloat(plusButton, "translationY", randomValue, 0);
            //  landing.setInterpolator(new DecelerateInterpolator(2));
            landing.setDuration(1000);
            landing.start();

            jump.setRepeatCount(0);
            jump.setRepeatCount(0);

            animatorSet.play(jump).before(landing);
            animatorSet.setInterpolator(new OvershootInterpolator(4));

// 애니메이션 실행
            animatorSet.start();

    }

    private void ReturnToFirst_plusButton() {
        returnAnimation = ObjectAnimator.ofFloat(plusButton, "translationX", 0f);
        returnAnimation.setDuration(750);
        returnAnimation.setInterpolator(new DecelerateInterpolator(2));
        returnAnimation.start();
    }

    private void Show__btn_note___btn_folder() {
        anim___btn_folder = ObjectAnimator.ofFloat(folderButtonView, "translationX", 0);
        anim___btn_note = ObjectAnimator.ofFloat(noteButtonView, "translationX", 0);
        anim___btn_add = ObjectAnimator.ofFloat(plusButton, "rotation", -45);
        anim___btn_folder.setInterpolator(new DecelerateInterpolator(2));
        anim___btn_note.setInterpolator(new DecelerateInterpolator(2));

        anim___btn_folder.setDuration(1000);
        anim___btn_note.setDuration(750);

        anim___btn_add.setInterpolator(new DecelerateInterpolator(2));
        anim___btn_add.setDuration(750);

        anim___btn_folder.start();
        anim___btn_note.start();
        anim___btn_add.start();
    }

    private void Hide__btn_note___btn_folder(int how) {
        anim___btn_folder = ObjectAnimator.ofFloat(folderButtonView, "translationX", Width_folderButtonView);
        anim___btn_note = ObjectAnimator.ofFloat(noteButtonView, "translationX", Width_noteButtonView);
        anim___btn_add = ObjectAnimator.ofFloat(plusButton, "rotation", 0);
        anim___btn_folder.setInterpolator(new DecelerateInterpolator(2));

        if (how == Public.NORMAL) {
            anim___btn_note.setInterpolator(new DecelerateInterpolator(1));

        } else if (how == DRAGGED_BUTTON_ADD) {
            anim___btn_note.setInterpolator(new DecelerateInterpolator(2));
        }

        anim___btn_folder.setDuration(1000);
        anim___btn_note.setDuration(750);

        anim___btn_add.setInterpolator(new DecelerateInterpolator(2));
        anim___btn_add.setDuration(750);

        anim___btn_folder.start();
        anim___btn_note.start();
        anim___btn_add.start();

    }
    public boolean isButtonsHiding() {
        if (backgroundView.getVisibility() == View.GONE) {
            return true;
        }
        else {
            return false;
        }
    }

    public void hideEveryButtons() {
        backgroundView.setVisibility(View.GONE);
    }
    public void showEveryButtons() {
        backgroundView.setVisibility(View.VISIBLE);
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        String background = appTheme.getMainActivityTheme().getFloatingBtnColor();
        int iconColor = Color.parseColor(appTheme.getMainActivityTheme().getFloatingBtnIconColor());
        int textColor = Color.parseColor(appTheme.getMainActivityTheme().getFloatingBtnTextColor());

        ColorApplier.applyColorToView( plusButton, background, R.drawable.button_rounded);
        ColorApplier.applyColorToView( noteButton, background, R.drawable.button_rounded);
        ColorApplier.applyColorToView(folderButton, background, R.drawable.button_rounded);

        plusButton.setTextColor(iconColor);
        noteButton.setTextColor(iconColor);
        folderButton.setTextColor(iconColor);

        folderButtonText.setTextColor(textColor);
        noteButtonText.setTextColor(textColor);
    }
}

