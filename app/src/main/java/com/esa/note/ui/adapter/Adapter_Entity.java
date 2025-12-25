package com.esa.note.ui.adapter;

import static com.esa.note.library.Public.FAVORITE;
import static com.esa.note.library.Public.LINEAR;
import static com.esa.note.library.Public.NOT_FAVORITE;
import static com.esa.note.library.Public.star_empty;
import static com.esa.note.library.Public.star_filled;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.listener.EntityGestureListener;
import com.esa.note.objects.Entity;
import com.esa.note.theme.FolderTheme;
import com.esa.note.theme.NoteTheme;

import java.util.List;

public class Adapter_Entity extends RecyclerView.Adapter<NoteAndFolderViewHolder> {

    private final Context context;
    private final List<Entity> entityList;
    private final EntityGestureListener entityGestureListener;
    protected ItemTouchHelper itemTouchHelper;
    protected boolean moving = false;
    protected int displayMode = LINEAR;
    private NoteTheme noteTheme;
    private FolderTheme folderTheme;

    public Adapter_Entity(Context context, List<Entity> entityList, EntityGestureListener entityGestureListener) {
        this.context = context;
        this.entityList = entityList;
        this.entityGestureListener = entityGestureListener;
    }

    @NonNull
    @Override
    public NoteAndFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteAndFolderViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull  NoteAndFolderViewHolder holder,  int position) {
        holder.display(entityList.get(holder.getAdapterPosition()), displayMode, moving);
        holder.applyTheme(entityList.get(holder.getAdapterPosition()), noteTheme, folderTheme);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moving == false) {
                    entityGestureListener.ClickedEntity(entityList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            }
        });

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                entityGestureListener.onSortEntityMode();
                    moving = true;
                notifyDataSetChanged();
                return true;
            }
        });

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entityGestureListener.ClickedDetail(entityList.get(holder.getAdapterPosition()));
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entityGestureListener.CLickedEdit(entityList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });

        holder.btn_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                itemTouchHelper.startDrag(holder);
                return false;
            }
        });

        holder.btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.btn_fav.getText().toString().equals(star_empty)) {
                    holder.btn_fav.setText(star_filled);
                    entityGestureListener.ChangedFavState(entityList.get(holder.getAdapterPosition()), FAVORITE);
                }
                else {
                    holder. btn_fav.setText(star_empty);

                    entityGestureListener.ChangedFavState(entityList.get(holder.getAdapterPosition()), NOT_FAVORITE);
                }

            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    entityGestureListener.entitySelected(entityList.get(holder.getAdapterPosition()));
                }
                else {
                    entityGestureListener.entityDeSelected(entityList.get(holder.getAdapterPosition()));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    public void backToDefault() {
            moving = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (moving == true) {
            return super.getItemViewType(position);
        }
        else {
            return position;
        }
    }

    public void setFolderTheme(FolderTheme folderTheme) {
        this.folderTheme = folderTheme;
    }

    public void setNoteTheme(NoteTheme noteTheme) {
        this.noteTheme = noteTheme;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }
}
