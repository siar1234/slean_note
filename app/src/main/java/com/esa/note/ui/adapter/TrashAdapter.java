package com.esa.note.ui.adapter;

import static com.esa.note.library.Public.GRID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.listener.TrashInterface;
import com.esa.note.objects.Entity;
import com.esa.note.theme.TrashActivityTheme;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<NoteAndFolderViewHolder> {

    private final Context context;
    private final List<Entity> entityList;
    private final TrashInterface trashInterface;
    private TrashActivityTheme trashActivityTheme;
    private int displayMode = GRID;

    public TrashAdapter(Context context, List<Entity> entityList, TrashInterface trashInterface) {
        this.context = context;
        this.entityList = entityList;
        this.trashInterface = trashInterface;
    }

    @NonNull
    @Override
    public NoteAndFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteAndFolderViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAndFolderViewHolder holder, int position) {
        holder.item.setAlpha(1.0f);
        holder.display(entityList.get(holder.getAdapterPosition()), displayMode, false);
        holder.applyTheme(entityList.get(holder.getAdapterPosition()),
                trashActivityTheme.getNoteTheme(), trashActivityTheme.getFolderTheme());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.item.getAlpha() == 1.0) {
                    holder.item.setAlpha(0.5f);
                    trashInterface.onItemSelected(entityList.get(holder.getAdapterPosition()));
                }
                else {
                    holder.item.setAlpha(1.0f);
                    trashInterface.onItemDeselected(entityList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void applyTheme(TrashActivityTheme trashActivityTheme) {
        this.trashActivityTheme = trashActivityTheme;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }
}
