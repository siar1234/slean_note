package com.esa.note.ui.adapter;

import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.listener.EditThemeInterface;
import com.esa.note.objects.ThemeData;
import com.esa.note.theme.ColorApplier;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.CustomViewHolder> {

    private final Context context;
    private final List<ThemeData> themeDataList;
    private final EditThemeInterface editThemeInterface;
    private int white = Color.parseColor(TOTAL_WHITE);
    private int black = Color.parseColor(TOTAL_BLACK);

    public ThemeAdapter(Context context, List<ThemeData> themeDataList, EditThemeInterface editThemeInterface) {
        this.context = context;
        this.themeDataList = themeDataList;
        this.editThemeInterface = editThemeInterface;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_theme_data, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.display(themeDataList.get(position));
        if (holder.color != null) {
            holder.color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editThemeInterface.toThemeColorChange(themeDataList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            });
        }
        if (holder.switchButton != null) {
            holder.switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.switchButton.isChecked()) {
                        holder.switchButton.setTrackTintList(ColorStateList.valueOf(black));
                        holder.switchButton.setThumbTintList(ColorStateList.valueOf(white));
                        themeDataList.get(holder.getAdapterPosition()).setColor(TOTAL_WHITE);
                        editThemeInterface.onThemeColorChanged(themeDataList.get(holder.getAdapterPosition()));
                    }
                    else {
                        holder.switchButton.setTrackTintList(ColorStateList.valueOf(white));
                        holder.switchButton.setThumbTintList(ColorStateList.valueOf(black));
                        themeDataList.get(holder.getAdapterPosition()).setColor(TOTAL_BLACK);
                        editThemeInterface.onThemeColorChanged(themeDataList.get(holder.getAdapterPosition()));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return themeDataList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        View color, colorSelection, only2ColorSelection;
        View backgroundView;
        SwitchCompat switchButton;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundView = itemView.findViewById(R.id.backgroundView);
            colorSelection = backgroundView.findViewById(R.id.colorSelection);
            only2ColorSelection = backgroundView.findViewById(R.id.only2ColorSelection);
        }

        void display(ThemeData themeData) {

            if (themeData.isOnlyBlackAndWhite()) {
                title = only2ColorSelection.findViewById(R.id.title);
                switchButton = only2ColorSelection.findViewById(R.id.switchButton);

                only2ColorSelection.setVisibility(View.VISIBLE);
                colorSelection.setVisibility(View.GONE);

                int black = Color.parseColor(TOTAL_BLACK);
                int white = Color.parseColor(TOTAL_WHITE);
                if (themeData.getColor().equals(TOTAL_BLACK)) {
                    switchButton.setTrackTintList(ColorStateList.valueOf(white));
                    switchButton.setThumbTintList(ColorStateList.valueOf(black));
                }
                else {
                    switchButton.setTrackTintList(ColorStateList.valueOf(black));
                    switchButton.setThumbTintList(ColorStateList.valueOf(white));
                }
            }
            else {
                title = colorSelection.findViewById(R.id.title);
                color = colorSelection.findViewById(R.id.color);
                ColorApplier.applyColorToView(color, themeData.getColor(), R.drawable.button_rounded);

                only2ColorSelection.setVisibility(View.GONE);
                colorSelection.setVisibility(View.VISIBLE);
            }
            title.setText(themeData.getTitle());

        }
    }
}
