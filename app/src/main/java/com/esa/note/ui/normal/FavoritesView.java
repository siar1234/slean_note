package com.esa.note.ui.normal;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.FAVORITES;
import static com.esa.note.library.Public.ICON_PENCIL;
import static com.esa.note.library.Public.MOVE;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.UPDATE;
import static com.esa.note.library.Public.okIcon;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.database.ListCRUDHelper;
import com.esa.note.listener.EntityGestureListener;
import com.esa.note.objects.Entity;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.adapter.Adapter_Favorite;
import com.esa.note.ui.root.AppUi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesView extends AppUi {

    private RecyclerView view_favs;
    private List<Entity> list = new ArrayList<>();
    private Adapter_Favorite adapter;
    private View btn_showFavorites;
    private Button EditFavsButton;
    private TextView text_fav, icon_fav;
    private EventListener listener;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        view_favs = backgroundView.findViewById(R.id.view_favs);
        btn_showFavorites = backgroundView.findViewById(R.id.btn_showFavorites);
        EditFavsButton = backgroundView.findViewById(R.id.EditFavsButton);
        text_fav = backgroundView.findViewById(R.id.text_fav);
        icon_fav = backgroundView.findViewById(R.id.icon_fav);
    }

    public void setList(List<Entity> list) {
        this.list = list;
    }

    public interface EventListener {
        void saveFavoritesPosition(List<Entity> entityList);
        void setNotFavorite(Entity entity);
    }

    public void readyAllEvent(EventListener listener) {
        this.listener = listener;
        EditFavsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoritesViewMode() == NORMAL) {
                    changeFavoritesView(MOVE);
                } else {
                    changeFavoritesView(NORMAL);
                    listener.saveFavoritesPosition(list);
                    ListCRUDHelper.savePosition(backgroundView.getContext(), list, FAVORITES);
                }
            }
        });

        btn_showFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (view_favs.getVisibility() == View.VISIBLE) {
                        hide();
                        if (favoritesViewMode() == MOVE) {
                            changeFavoritesView(NORMAL);
                        }
                    } else {
                        show();
                    }
            }
        });
    }

    public void readyListView(EntityGestureListener entityGestureListener) {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
                        | ItemTouchHelper.END, 0) {

                    @Override
                    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                                @NonNull RecyclerView.ViewHolder viewHolder) {
                        int drag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                        int swipe = ItemTouchHelper.START | ItemTouchHelper.END;
                        return makeMovementFlags(drag, swipe);
                    }

                    @Override
                    public boolean isItemViewSwipeEnabled() {
                        return super.isItemViewSwipeEnabled();
                    }

                    @Override
                    public boolean isLongPressDragEnabled() {
                        return false;
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();

                        Collections.swap(list, from, to);
                        if (recyclerView.getAdapter() != null) {
                            recyclerView.getAdapter().notifyItemMoved(from, to);
                        }
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Entity entity = list.get(viewHolder.getAdapterPosition());
                        int get = viewHolder.getAdapterPosition();

                        list.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                        if (listener != null) {
                            listener.setNotFavorite(entity);
                        }
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);

        adapter = new Adapter_Favorite(backgroundView.getContext(), list , entityGestureListener);
        adapter.setItemTouchHelper(itemTouchHelper);

        view_favs.setLayoutManager(new LinearLayoutManager(backgroundView.getContext(), RecyclerView.VERTICAL, false));
        view_favs.setAdapter(adapter);

        itemTouchHelper.attachToRecyclerView(view_favs);
    }

    public int favoritesViewMode() {
        if (EditFavsButton.getText().equals(okIcon)) {
            return MOVE;
        }
        else {
            return NORMAL;
        }
    }

    public void changeFavoritesView(int how) {
        if (how == NORMAL) {
            EditFavsButton.setText(ICON_PENCIL);
            if (adapter != null) {
                adapter.setNotMovingMode();
            }
        }
        else if (how == MOVE){
            EditFavsButton.setText(okIcon);
            if (adapter != null) {
                adapter.setMovingMode();
            }

        }

    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        int textColor = Color.parseColor(appTheme.getMainActivityTheme().getNavMenuTextColor());
        int iconColor = Color.parseColor(appTheme.getMainActivityTheme().getNavMenuIconColor());
        icon_fav.setTextColor(iconColor);
        text_fav.setTextColor(textColor);
        EditFavsButton.setTextColor(iconColor);
        if (adapter != null) {
            adapter.setTheme(appTheme);
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isShowing() {
        if (view_favs.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public void show() {
        view_favs.setVisibility(View.VISIBLE);
        EditFavsButton.setVisibility(View.VISIBLE);
    }

    public void hide() {
        view_favs.setVisibility(View.GONE);
        EditFavsButton.setVisibility(View.GONE);
    }

    public void updateView(int how , int position, Entity entity) {
        if (how == ADD) {
            list.add(position, entity);
            adapter.notifyItemInserted(position);
        }
        else if (how == DELETE) {
            list.remove(position);
            adapter.notifyItemRemoved(position);
        }
        else if (how == UPDATE) {
            list.remove(position);
            list.add(position, entity);
            adapter.notifyItemChanged(position);
        }
    }

    public void updateView(int how , List<Entity> entityList) {
        if (how == ADD) {
            list.addAll(entityList);
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
        adapter.notifyDataSetChanged();
    }

    public List<Entity> getList() {
        return list;
    }
}
