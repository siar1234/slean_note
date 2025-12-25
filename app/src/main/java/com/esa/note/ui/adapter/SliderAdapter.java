package com.esa.note.ui.adapter;

import static com.esa.note.library.Public.AVAILABLE_UPDATE;
import static com.esa.note.library.Public.NORMAL;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.listener.SliderInterface;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private final Context context;
    private final List<AppTheme> themeList;
    private AppTheme theme;
    private int displayMode = NORMAL;
    private final SliderInterface sliderInterface;

    public SliderAdapter(Context context, List<AppTheme> themeList, SliderInterface sliderInterface) {
        this.context = context;
        this.themeList = themeList;
        this.sliderInterface = sliderInterface;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(context).inflate(R.layout.theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.createView(themeList.get(holder.getAdapterPosition()));
        if (holder.getAdapterPosition() != 0 && holder.getAdapterPosition() != 1) {
            holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (displayMode == NORMAL) {
                        displayMode = AVAILABLE_UPDATE;
                        notifyDataSetChanged();
                    }

                    return true;
                }
            });

                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (displayMode == AVAILABLE_UPDATE && themeList.get(holder.getAdapterPosition()).getDescription() == NORMAL) {
                            sliderInterface.onThemeModify(themeList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                        }
                    }
                });

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (displayMode == AVAILABLE_UPDATE && themeList.get(holder.getAdapterPosition()).getDescription() == NORMAL) {
                            sliderInterface.onDeleteTheme(themeList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                        }
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return themeList.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder{

        TextView thumbnail, title, editButton, deleteButton;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.editButton);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        void createView(AppTheme appTheme) {

            ColorApplier.applyColorToView(thumbnail, appTheme.getMainActivityTheme().getNoteTheme().getBackground(),
                    R.drawable.button_rounded);

            title.setText(appTheme.getTitle());

            if (theme != null) {
                int textColor = Color.parseColor(theme.getSettingActivityTheme().getTextColor());
                int iconColor = Color.parseColor(theme.getSettingActivityTheme().getIconColor());
                title.setTextColor(textColor);
                editButton.setTextColor(iconColor);
                deleteButton.setTextColor(iconColor);
            }

            if (displayMode == AVAILABLE_UPDATE) {
                if (appTheme.getDescription() == NORMAL) {
                    deleteButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                }
                else {
                    deleteButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.GONE);
                }
            }
            else {
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
            }
        }
    }

    public void setTheme(AppTheme appTheme) {
        this.theme = appTheme;
        notifyDataSetChanged();
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }
}
