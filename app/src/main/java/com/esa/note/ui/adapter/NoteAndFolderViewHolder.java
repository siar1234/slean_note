package com.esa.note.ui.adapter;

import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.HIDE;
import static com.esa.note.library.Public.folder;
import static com.esa.note.library.Public.note;
import static com.esa.note.library.Public.star_empty;
import static com.esa.note.library.Public.star_filled;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.objects.Entity;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.FolderTheme;
import com.esa.note.theme.NoteTheme;

public class NoteAndFolderViewHolder extends RecyclerView.ViewHolder {
    FrameLayout item;
    TextView title, content, icon;
    Button btn_fav, btn_edit, btn_detail, btn_move;
    CheckBox checkBox;
    View backgroundView, grid_note, grid_folder, linearItem;

    public NoteAndFolderViewHolder(@NonNull View itemView) {
        super(itemView);
        item = (itemView).findViewById(R.id.item);
        grid_note = itemView.findViewById(R.id.grid_note);
        grid_folder = itemView.findViewById(R.id.grid_folder);
        linearItem = itemView.findViewById(R.id.linearItem);
        icon = linearItem.findViewById(R.id.icon);
    }

    void display(Entity entity, int displayMode, boolean moving) {
        if (entity.getDescription() == HIDE) {
            itemView.setVisibility(View.GONE);
        }
        if (displayMode == GRID) {
            if (entity.isNote()) {
                backgroundView = grid_note;
                grid_note.setVisibility(View.VISIBLE);
                grid_folder.setVisibility(View.GONE);
            }
            else {
                backgroundView = grid_folder;
                grid_folder.setVisibility(View.VISIBLE);
                grid_note.setVisibility(View.GONE);

            }
            linearItem.setVisibility(View.GONE);
        }
        else {
            backgroundView = linearItem;
            linearItem.setVisibility(View.VISIBLE);
            grid_folder.setVisibility(View.GONE);
            grid_note.setVisibility(View.GONE);

         //   icon = linearItem.findViewById(R.id.icon);
            if (entity.isNote()) {
                icon.setText(note);
            }
            else {
                icon.setText(folder);
            }
        }

        content = grid_note.findViewById(R.id.content);

        btn_edit = backgroundView.findViewById(R.id.btn_edit);

        title = backgroundView.findViewById(R.id.title);

        btn_fav = backgroundView.findViewById(R.id.btn_fav);
        btn_detail= backgroundView.findViewById(R.id.btn_detail);
        btn_edit = backgroundView.findViewById(R.id.btn_edit);
        btn_move = backgroundView.findViewById(R.id.btn_move);
        checkBox = backgroundView.findViewById(R.id.checkbox);

        if (!entity.getTitle().isEmpty()) {
            title.setVisibility(View.VISIBLE);
            title.setText(entity.getTitle());
//                        title.setTextColor(Color.parseColor(entity.getTextColor().substring(0, 9)));
        }
        else {
            title.setVisibility(View.GONE);
        }
        if (entity.isNote()) {

            if(entity.getContent() != null) {
                if (!entity.getContent().isEmpty()) {
                    content.setVisibility(View.VISIBLE);
                    if (entity.isDeleted()) {
                        try {
                            content.setText(entity.getContent().substring(12));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        content.setText(entity.getContent());
                    }
                }else {
                    content.setVisibility(View.GONE);
                }
            }
            else {
                content.setVisibility(View.GONE);
            }

            if (entity.isOnlyShowTitle()) {
                content.setVisibility(View.GONE);
            }
        }
        if (entity.isFavorite()) {
            btn_fav.setText(star_filled);
        }
        else {
            btn_fav.setText(star_empty);
        }
        if (moving == true) {
            btn_fav.setVisibility(View.GONE);
            btn_detail.setVisibility(View.GONE);

            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
            btn_move.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        }
        else {
            btn_fav.setVisibility(View.VISIBLE);
            btn_detail.setVisibility(View.VISIBLE);

            checkBox.setVisibility(View.GONE);
            btn_move.setVisibility(View.GONE);
            btn_edit.setVisibility(View.GONE);
        }
    }

    void applyTheme(Entity entity, NoteTheme noteTheme, FolderTheme folderTheme) {
        if (entity.isNote()) {
            applyNoteTheme(noteTheme);
        }
        else {
            applyFolderTheme(folderTheme);
        }
    }

    private void applyNoteTheme(NoteTheme noteTheme) {
        if (noteTheme != null) {
            applyLinearBackground(noteTheme.getBackground());
            applyNoteGridBackground(noteTheme);
            int textColor =  Color.parseColor(noteTheme.getTextColor());
            int iconColor = Color.parseColor(noteTheme.getIconColor());
            applyValuesColor(textColor, iconColor);
        }
    }
    private void applyFolderTheme(FolderTheme folderTheme) {
        applyLinearBackground(folderTheme.getBackground());
        applyFolderGridBackground(folderTheme);
        int textColor =  Color.parseColor(folderTheme.getTextColor());
        int iconColor = Color.parseColor(folderTheme.getIconColor());
        applyValuesColor(textColor, iconColor);
    }
    private void applyLinearBackground(String color) {
        ColorApplier.applyColorToView(
                linearItem,
                color,
                R.drawable.view_rounded );
    }
    private  void applyNoteGridBackground(NoteTheme noteTheme) {
        ColorApplier.applyColorToView(
                grid_note,
                noteTheme.getBackground(),
                R.drawable.view_rounded );
    }
    private void applyFolderGridBackground(FolderTheme folderTheme) {
        ColorApplier.applyColorToView(
                grid_folder.findViewById(R.id.parent1),
                folderTheme.getBackground(),
                R.drawable.view_rounded_top );
        ColorApplier.applyColorToView(
                grid_folder.findViewById(R.id.parent2),
                folderTheme.getBackground(),
                R.drawable.view_rounded_top_right_bottom );
    }
    private void applyValuesColor(int textColor, int iconColor) {
        title.setTextColor(textColor);
            content.setTextColor(textColor);

        btn_fav.setTextColor(iconColor);
        btn_detail.setTextColor(iconColor);
        btn_move.setTextColor(iconColor);
        btn_edit.setTextColor(iconColor);
        icon.setTextColor(iconColor);

        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(iconColor));
        } else {
            checkBox.setButtonTintList(ColorStateList.valueOf(iconColor));
        }
    }
}
