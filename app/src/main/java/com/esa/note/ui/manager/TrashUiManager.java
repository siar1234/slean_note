package com.esa.note.ui.manager;

import static com.esa.note.library.Public.EMPTY;
import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.NORMAL;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.esa.note.R;
import com.esa.note.listener.TrashInterface;
import com.esa.note.objects.Entity;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.adapter.TrashAdapter;
import com.esa.note.ui.dialog.AskDialog;
import com.esa.note.ui.root.AppUi;

import java.util.ArrayList;
import java.util.List;

public class TrashUiManager extends AppUi implements TrashInterface {

    private View btnSet,btn_restore , btn_delete;
    private TrashAdapter trashAdapter;
    private TextView text_restore, icon_restore, text_delete, icon_delete, title, description;
    private int howDelete;
    private RecyclerView listView;

    private Button backButton, emptyButton;
    private final AskDialog deleteDialog = new AskDialog();
    private final List<Entity> selectedItems = new ArrayList<>();
    public boolean uiBackPressed() {

        if (deleteDialog.isShowing()) {
            deleteDialog.cancel();
            return true;
        }
        else if (selectedItems.size() > 0) {
            onNothingSelected();
            selectedItems.clear();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);

        backButton = backgroundView.findViewById(R.id.backButton);
        emptyButton = backgroundView.findViewById(R.id.emptyButton);
        btn_restore = backgroundView.findViewById(R.id.btn_restore);
        btn_delete = backgroundView.findViewById(R.id.btn_delete);
        listView = backgroundView.findViewById(R.id.listView);
        btnSet = backgroundView.findViewById(R.id.btnSet);
        btnSet.setVisibility(View.GONE);

        text_restore = backgroundView.findViewById(R.id.text_restore);
        icon_restore = backgroundView.findViewById(R.id.icon_restore);

        text_delete = backgroundView.findViewById(R.id.text_delete);
        icon_delete = backgroundView.findViewById(R.id.icon_delete);

        title = backgroundView.findViewById(R.id.title);
        description = backgroundView.findViewById(R.id.description);

        deleteDialog.findAllViewById(context);
        deleteDialog.setTitle(context.getResources().getString(R.string.ask_delete));
    }

    @Override
    public void readyAllDialogs(Activity activity) {
        super.readyAllDialogs(activity);
        deleteDialog.readyDialog(activity);
    }

    public void displayTrashes(List<Entity> list) {
        trashAdapter = new TrashAdapter(context, list, this);
        trashAdapter.setDisplayMode(GRID);
        trashAdapter.notifyDataSetChanged();

        listView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        listView.setAdapter(trashAdapter);
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);

        ColorApplier.applyColorToView(backgroundView, appTheme.getTrashActivityTheme().getBackground());

        int textColor = Color.parseColor(appTheme.getTrashActivityTheme().getTextColor());
        int iconColor = Color.parseColor(appTheme.getTrashActivityTheme().getIconColor());
        int textHighLightColor = Color.parseColor(appTheme.getTrashActivityTheme().getTextSelectionColor());

        backButton.setTextColor(iconColor);

        emptyButton.setTextColor(textColor);

        text_restore.setTextColor(textColor);
        text_delete.setTextColor(textColor);

        icon_restore.setTextColor(iconColor);
        icon_delete.setTextColor(iconColor);

        title.setTextColor(textColor);
        title.setHighlightColor(textHighLightColor);
        description.setTextColor(textColor);
        description.setHighlightColor(textHighLightColor);

        deleteDialog.applyTheme(appTheme.getTrashActivityTheme().getDialogTheme());
        trashAdapter.applyTheme(appTheme.getTrashActivityTheme());
    }

    public void onNothingSelected() {
        btnSet.setVisibility(View.GONE);
        emptyButton.setVisibility(View.VISIBLE);
    }

    public void onSomethingSelected() {
        btnSet.setVisibility(View.VISIBLE);
        emptyButton.setVisibility(View.GONE);
    }

    public AskDialog getDeleteDialog() {
        return deleteDialog;
    }

    @Override
    public void onItemSelected(Entity entity) {
        selectedItems.add(entity);
        if (selectedItems.size() == 0) {
            onNothingSelected();
        }
        else {
            onSomethingSelected();
        }
    }

    @Override
    public void onItemDeselected(Entity entity) {
        selectedItems.remove(entity);
        if (selectedItems.size() == 0) {
            onNothingSelected();
        }
        else {
            onSomethingSelected();
        }
    }

    public interface UpdateListener {
        void onRestore(List<Entity> list);
        void onDelete(List<Entity> list);
        void onEmpty();
        void backPressed();
    }
    public void setUpdateListener(UpdateListener listener) {
        btn_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRestore(selectedItems);
            }
        });
        deleteDialog.setOnEventListener(new AskDialog.onEventListener() {
            @Override
            public void onYesClick() {
                deleteDialog.cancel();
                if (howDelete == EMPTY) {
                    listener.onEmpty();
                }
                else if (howDelete == NORMAL) {
                    listener.onDelete(selectedItems);
                }
            }

            @Override
            public void onNoClick() {
                deleteDialog.cancel();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.backPressed();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
                howDelete = NORMAL;
            }
        });

        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
                howDelete = EMPTY;
            }
        });
    }
    public void notifyDataSetChanged() {
        trashAdapter.notifyDataSetChanged();
    }
}
