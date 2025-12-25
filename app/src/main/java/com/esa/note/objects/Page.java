package com.esa.note.objects;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.HIDE;
import static com.esa.note.library.Public.LINEAR;
import static com.esa.note.library.Public.UPDATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.esa.note.R;
import com.esa.note.listener.EntityGestureListener;
import com.esa.note.theme.FolderTheme;
import com.esa.note.theme.MainActivityTheme;
import com.esa.note.theme.NoteTheme;
import com.esa.note.theme.TrashActivityTheme;
import com.esa.note.ui.adapter.Adapter_Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Page implements Serializable {

    private int id, selectedPosition, displayMode = LINEAR;
    private Adapter_Entity gridAdapter, linearAdapter;
    private List<Entity> list = new ArrayList<>();
    private final List<Entity> selectedEntities = new ArrayList<>();
    private View gridView, linearView;
    private String title;

    public View showingView() {
        if (displayMode == GRID) {
            return gridView;
        }
        else {
            return linearView;
        }
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Adapter_Entity getGridAdapter() {
        return gridAdapter;
    }

    public Adapter_Entity getLinearAdapter() {
        return linearAdapter;
    }

    public View getGridView() {
        return gridView;
    }

    public View getLinearView() {
        return linearView;
    }

    public List<Entity> getList() {
        return list;
    }

    public void setList(List<Entity> list) {
        this.list = list;
    }

    public void sortByCreatedDate() {

        List<Entity> newList = new ArrayList<>();
        for (Entity newEntity : this.getList()) {
            if (newList.size() == 0) {
                newList.add(newEntity);
            }
            else {
                int position = 0;
                for (Entity oldEntity : newList) {
                    if (newEntity.getCreatedDate() <= oldEntity.getCreatedDate()) {
                        position++;
                    }
                }
                newList.add(position, newEntity);
            }
        }
        this.getList().clear();
        this.getList().addAll(newList);
        this.getGridAdapter().notifyDataSetChanged();
        this.getLinearAdapter().notifyDataSetChanged();

    }

    public void sortByModifiedDate() {
        List<Entity> newList = new ArrayList<>();
        for (Entity newEntity :  this.getList()) {
            if (newList.size() == 0) {
                newList.add(newEntity);
            }
            else {
                int position = 0;
                for (Entity oldEntity : newList) {
                    if (newEntity.getModifiedDate() <= oldEntity.getModifiedDate()) {
                        position++;
                    }
                }
                newList.add(position, newEntity);
            }
        }
        this.getList().clear();
        this.getList().addAll(newList);
        this.getGridAdapter().notifyDataSetChanged();
        this.getLinearAdapter().notifyDataSetChanged();

    }

    public void sortByReverse() {
        Collections.reverse(this.getList());
        this.getGridAdapter().notifyDataSetChanged();
        this.getLinearAdapter().notifyDataSetChanged();

    }

    public void hideSpecificEntities(List<Entity> listForHide) {
        for (Entity entity : list) {
            for (Entity toRemove : listForHide) {
                if (entity.getId() == toRemove.getId()) {
                    entity.setDescription(HIDE);
                }
            }
        }
        gridAdapter.notifyDataSetChanged();
        linearAdapter.notifyDataSetChanged();
    }

    @SuppressLint("InflateParams")
    public void createView(Context context, EntityGestureListener entityGestureListener) {
        gridView = LayoutInflater.from(context).inflate(R.layout.display_notes, null, false);
        linearView = LayoutInflater.from(context).inflate(R.layout.display_notes, null, false);

        RecyclerView gridListView = gridView.findViewById(R.id.view_notes);

        ItemTouchHelper gridTouchHelper = new ItemTouchHelper(simpleCallback(list, GRID));

        gridListView.setHasFixedSize(true);
        int orientation = context.getResources().getConfiguration().orientation;
        int spanCount = 2;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }
        gridListView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));


        gridAdapter = new Adapter_Entity(context, list, entityGestureListener);
        gridAdapter.setDisplayMode(GRID);
        gridAdapter.setItemTouchHelper(gridTouchHelper);
        gridAdapter.notifyDataSetChanged();
        gridListView.setAdapter(gridAdapter);

        gridTouchHelper.attachToRecyclerView(gridListView);

        RecyclerView linearListView = linearView.findViewById(R.id.view_notes);
        ItemTouchHelper linearTouchHelper = new ItemTouchHelper(simpleCallback(list, LINEAR));

        linearListView.setHasFixedSize(true);

        linearListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        linearAdapter =  new Adapter_Entity(context, list, entityGestureListener);
        linearAdapter.setDisplayMode(LINEAR);
        linearAdapter.setItemTouchHelper(linearTouchHelper);
        linearAdapter.notifyDataSetChanged();
        linearListView.setAdapter(linearAdapter);

        linearTouchHelper.attachToRecyclerView(linearListView);

    }

    public void updateView(int how ,  Entity entity) {
        if (how == ADD) {

            list.add(0, entity);
            gridAdapter.notifyItemInserted(0);
            linearAdapter.notifyItemInserted(0);
        }
        else if (how == UPDATE) {
            list.remove(selectedPosition);
            list.add(selectedPosition, entity);
            gridAdapter.notifyItemChanged(selectedPosition);
            linearAdapter.notifyItemChanged(selectedPosition);
        }
        else if (how == DELETE) {
            list.remove(selectedPosition);
            gridAdapter.notifyDataSetChanged();
            linearAdapter.notifyDataSetChanged();

        }
    }

    public void updateView(int how , List<Entity> entityList) {
        if (how == ADD) {
            list.addAll(0 , entityList);
        }
        else if (how == DELETE) {
            for (Entity entity : entityList) {
                int position = 0;
                for (Entity entity1 : list) {
                    if (entity.getId() == entity1.getId()) {
                        list.remove(position);
                        break;
                    }
                    else {
                        position++;
                    }
                }
            }
        }
        linearAdapter.notifyDataSetChanged();
        gridAdapter.notifyDataSetChanged();
    }

    public void updateMultiple(int how, List<Entity> entityList) {
        if (how == ADD) {
            list.addAll(0 , entityList);
        }
        else if (how == DELETE) {
            for (Entity entity : list) {
                for (Entity entity1 : entityList) {
                    if (entity.getId() == entity1.getId()) {
                        list.remove(entity);
                    }
                }
            }
        }
        linearAdapter.notifyDataSetChanged();
        gridAdapter.notifyDataSetChanged();
    }

    public ItemTouchHelper.SimpleCallback simpleCallback(List<Entity> entityList, int how) {
        int dragDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        if (how == GRID) {
            dragDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        }
        return new ItemTouchHelper.SimpleCallback(
                dragDirs, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int from = viewHolder.getAdapterPosition();
                Entity entity = entityList.get(from);

                int to = target.getAdapterPosition();

                entityList.remove(from);
                entityList.add(to, entity);
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(from, to);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        };
    }

    private void applyTheme(String background, NoteTheme noteTheme, FolderTheme folderTheme) {
        try {
            int color = Color.parseColor(background);
            linearView.setBackgroundColor(color);
            gridView.setBackgroundColor(color);

            gridAdapter.setNoteTheme(noteTheme);
            gridAdapter.setFolderTheme(folderTheme);
            gridAdapter.notifyDataSetChanged();
            linearAdapter.setNoteTheme(noteTheme);
            linearAdapter.setFolderTheme(folderTheme);
            linearAdapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void applyTheme(MainActivityTheme mainActivityTheme) {
        applyTheme(mainActivityTheme.getBackground(), mainActivityTheme.getNoteTheme(), mainActivityTheme.getFolderTheme());
    }
    public void applyTheme(TrashActivityTheme trashActivityTheme) {
        applyTheme(trashActivityTheme.getBackground(), trashActivityTheme.getNoteTheme(), trashActivityTheme.getFolderTheme());
    }

    public List<Entity> getSelectedEntities() {
        return selectedEntities;
    }

}
