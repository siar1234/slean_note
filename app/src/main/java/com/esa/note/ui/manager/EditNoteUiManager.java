package com.esa.note.ui.manager;

import static com.esa.note.activity.EditNoteActivity.CREATE_NOTE;
import static com.esa.note.activity.EditNoteActivity.LAUNCH_FROM_SHORTCUT;
import static com.esa.note.activity.EditNoteActivity.READ_AND_UPDATE_NOTE;
import static com.esa.note.library.Public.FAVORITE;
import static com.esa.note.library.Public.HIDE;
import static com.esa.note.library.Public.HIDE_CONTENT;
import static com.esa.note.library.Public.IN_FOLDER;
import static com.esa.note.library.Public.MIDDLE;
import static com.esa.note.library.Public.NOTE;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.esa.note.R;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Setting;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.bottomsheet.EditDetail;
import com.esa.note.ui.normal.ReadNote;
import com.esa.note.ui.normal.WriteNote;
import com.esa.note.ui.root.AppUi;

public class EditNoteUiManager extends AppUi {

    private final WriteNote writeNote = new WriteNote();
    private final ReadNote readNote = new ReadNote();
    private final EditDetail editDetail = new EditDetail();
    private boolean isNoteUpdated = false;
    private Entity entity = new Entity();
    private int activityMode = CREATE_NOTE;

    private int parentsId = 0;
    private boolean isInFolder = false;

    public void setParentsId(int parentsId) {
        this.parentsId = parentsId;
    }

    public void setInFolder(boolean inFolder) {
        isInFolder = inFolder;
    }

    public void setActivityMode(int activityMode) {
        this.activityMode = activityMode;
    }
    private EventListener listener;

    public boolean isNeedBackPressedAction() {
        if (editDetail.isShowing()) {
            editDetail.animate(HIDE);
            return true;
        }
        else if (writeNote.getNotUpdateDialog().isShowing()) {
            writeNote.getNotUpdateDialog().cancel();
            return true;
        }
        else if (writeNote.getNotSaveDialog().isShowing()) {
            writeNote.getNotSaveDialog().cancel();
            return true;
        }
        else if (activityMode == CREATE_NOTE) {
            if (!writeNote.getTitle().trim().isEmpty() && activityMode == CREATE_NOTE
                    || !writeNote.getContent().trim().isEmpty() && activityMode == CREATE_NOTE) {
                writeNote.getNotSaveDialog().show();
                return true;
            }
            else {
                return false;
            }

        }
        else if (activityMode == READ_AND_UPDATE_NOTE && writeNote.isShowing()
                || activityMode == LAUNCH_FROM_SHORTCUT  && writeNote.isShowing()) {
            if (!entity.getTitle().equals(writeNote.getTitle()) || !entity.getContent().equals(writeNote.getContent())) {
                writeNote.getNotUpdateDialog().show();
            } else {
                setNoteReadMode(entity);
            }
            return true;
        }
        else if (isNoteUpdated) {
            if (listener != null) {
                listener.finishWithUpdatedNote(entity);
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);

        readNote.findAllViewById( backgroundView.findViewById(R.id.view_note) );
        writeNote.findAllViewById(backgroundView.findViewById(R.id.edit_note));

        editDetail.findAllViewById( backgroundView.findViewById(R.id.input_detail) );
        editDetail.setPopupBackground(backgroundView.findViewById(R.id.background_popup));
    }

    public interface EventListener {
        void onSaveNote(Entity entity);
        void canceledCreateNote();
        void finishWithUpdatedNote(Entity entity);
        void chooseImage();
        void connectWithOther();
    }

    public void readyAllEvent(EventListener listener) {
        this.listener = listener;

        readNote.readyAllEvent(new ReadNote.EventListener() {

            @Override
            public void onStartNoteUpdate() {
                setNoteWriteMode(entity);
            }

            @Override
            public void onBackPressed() {
                isNeedBackPressedAction();
            }

            @Override
            public void onStartNoteUpdate(int titleIndex, int contentIndex) {
                setNoteWriteMode(entity);
//                if (titleIndex == -1) {
//                    writeNote.setSelection_title(contentIndex);
//                }
//                else {
//                    writeNote.setSelection_title(titleIndex);
//                }

            }
        });

        writeNote.readyAllEvent(new WriteNote.EventListener() {
            @Override
            public void chooseImage() {
                listener.chooseImage();
            }

            @Override
            public void onSave(Entity entity) {
                entity.setCreatedDate(editDetail.getDatePicker_created().getDate());
                entity.setModifiedDate(editDetail.getDatePicker_modified().getDate());

                int description = NOTE;

                if (editDetail.getCheck_hideContent().isChecked() == true) {
                    description += HIDE_CONTENT;
                }

                if (isInFolder) {
                    description += IN_FOLDER;
                }

                if (entity.isFavorite()) {
                    description += FAVORITE;
                }

                entity.setDescription(description);

                if (activityMode == CREATE_NOTE) {
                    entity.setParent(parentsId);
                    entity.setPosition(0);
                    entity.setPosition_favorite(0);
                }
                listener.onSaveNote(entity);
            }

            @Override
            public void onBackPressed() {
                isNeedBackPressedAction();
            }

            @Override
            public void showEditDetailBottomSheet() {
                editDetail.animate(MIDDLE);
            }

            @Override
            public void canceledCreateNote() {
                listener.canceledCreateNote();
            }

            @Override
            public void canceledUpdateNote() {
                setNoteReadMode(entity);
            }

            @Override
            public void connectWithOther() {
                listener.connectWithOther();
            }
        });
        editDetail.readyAllEvent();
    }

    @Override
    public void readyAllDialogs(Activity activity) {
        super.readyAllDialogs(activity);
        writeNote.readyAllDialogs(activity);
        readNote.readyAllDialogs(activity);
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        writeNote.applyTheme(appTheme);
        readNote.applyTheme(appTheme);
        editDetail.applyTheme(appTheme.getEditNoteActivityTheme().getBottomSheetTheme());
    }

    public void setNoteWriteMode(Entity entity) {
        this.entity = entity;
        writeNote.setEntity(entity);
        writeNote.show();
        readNote.hide();

        Log.d("hey", readNote.getScrollY()+"");
        writeNote.scrollTo(readNote.getScrollY());

        if (entity.isOnlyShowTitle()) {
            editDetail.setCheck_hideContent();
        }
        editDetail.setCreatedDate(entity.getCreatedDate());
        editDetail.setModifiedDate(entity.getModifiedDate());
    }

    public void setNoteReadMode(Entity entity) {
        this.entity = entity;
        readNote.setAndApplyEntity(entity);

        writeNote.hide();
        readNote.show();

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) backgroundView.getContext()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(writeNote.getContentInput().getWindowToken(), 0);
        }catch (Exception e) {
            e.printStackTrace();
            InputMethodManager inputMethodManager = (InputMethodManager) backgroundView.getContext()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(writeNote.getContentInput().getWindowToken(), 0);
        }

    }

    public void applySetting(Setting setting) {
        if (setting.isShowTitleOnlyIsDefault()) {
            editDetail.setCheck_hideContent();
        }
    }

    public void setNoteUpdated(boolean noteUpdated) {
        isNoteUpdated = noteUpdated;
    }
}
