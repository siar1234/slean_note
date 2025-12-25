package com.esa.note.ui.adapter;

import static com.esa.note.library.Public.folder;
import static com.esa.note.library.Public.note;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.objects.Entity;
import com.esa.note.listener.EntityGestureListener;
import com.esa.note.theme.AppTheme;

import java.util.List;

public class Adapter_Favorite extends RecyclerView.Adapter<Adapter_Favorite.FavViewHolder> {

    protected boolean moving = false;

    public Adapter_Favorite(Context context, List<Entity> favList, EntityGestureListener entityGestureListener, ItemTouchHelper itemTouchHelper) {
        this.context = context;
        this.favList = favList;
        this.entityGestureListener = entityGestureListener;
        this.itemTouchHelper = itemTouchHelper;
    }

    public Adapter_Favorite(Context context, List<Entity> favList, EntityGestureListener entityGestureListener) {
        this.context = context;
        this.favList = favList;
        this.entityGestureListener = entityGestureListener;
    }

    protected Context context;
    protected List<Entity> favList;
    protected EntityGestureListener entityGestureListener;
    protected ItemTouchHelper itemTouchHelper;

    private AppTheme appTheme;

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavViewHolder(LayoutInflater.from(context).inflate(R.layout.favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.displayItem(favList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moving == false) {
                    entityGestureListener.ClickedEntity(favList.get(position), position);
                }
            }
        });
        holder.btn_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                itemTouchHelper.startDrag(holder);
                return false;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (moving == false) {
            return position;
        }
        else {
            return super.getItemViewType(position);
        }
    }

    public void setMovingMode() {
            moving = true;

        notifyDataSetChanged();
    }
    public void setNotMovingMode() {
        moving = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        TextView icon_fav, title_fav;
        Button btn_move;
        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_fav = itemView.findViewById(R.id.icon_fav);
            title_fav = itemView.findViewById(R.id.title_fav);
            btn_move = itemView.findViewById(R.id.btn_move);
        }
        void displayItem(Entity entity) {
            if (entity.isNote()) {
                icon_fav.setText(note);
            }
            else {
                icon_fav.setText(folder);
            }
            title_fav.setText(entity.getTitle());

            if (moving == true) {
                btn_move.setVisibility(View.VISIBLE);
            }
            else {
                btn_move.setVisibility(View.GONE);
            }

            if (appTheme != null) {

                int iconColor = Color.parseColor(appTheme.getMainActivityTheme().getFavIconColor());
                int textColor = Color.parseColor(appTheme.getMainActivityTheme().getFavTextColor());

                icon_fav.setTextColor(iconColor);
                btn_move.setTextColor(iconColor);

                title_fav.setTextColor(textColor);
            }
        }
    }

    public void setTheme(AppTheme appTheme) {
        this.appTheme = appTheme;
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }
}
