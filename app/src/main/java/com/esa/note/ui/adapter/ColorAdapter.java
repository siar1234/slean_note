package com.esa.note.ui.adapter;

import static com.esa.note.library.Public.AVAILABLE_UPDATE;
import static com.esa.note.library.Public.ICON_PENCIL;
import static com.esa.note.library.Public.NORMAL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.listener.ColorSelectorInterface;
import com.esa.note.objects.ColorObject;
import com.esa.note.theme.BottomSheetTheme;
import com.esa.note.theme.ColorApplier;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private final Context context;
    private final ColorSelectorInterface colorSelectorInterface;
    private final List<ColorObject> colorList;
    private BottomSheetTheme bottomSheetTheme;
    private int selectedColorPosition = 0;
    private int displayMode = NORMAL;

    public ColorAdapter(Context context, List<ColorObject> colorList, ColorSelectorInterface colorSelectorInterface) {
        this.context = context;
        this.colorSelectorInterface = colorSelectorInterface;
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColorViewHolder(LayoutInflater.from(context).inflate(R.layout.color_item, parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setColor(colorList.get(position));
        if (position == selectedColorPosition) {
            holder.setSelected();
        }
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displayMode == NORMAL) {
                    selectedColorPosition = position;
                    notifyDataSetChanged();
                    colorSelectorInterface.onColorSelected(colorList.get(position));
                }
                else {
                    if (colorList.get(position).getDescription() == NORMAL) {
                        colorSelectorInterface.onColorUpdate(colorList.get(position), position);
                    }
                }

            }
        });
        holder.background.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (displayMode == NORMAL) {
                    displayMode = AVAILABLE_UPDATE;
                    notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        View background;
        TextView colorView, title;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            colorView = itemView.findViewById(R.id.color);
            title = itemView.findViewById(R.id.title);
        }

        @SuppressLint("SetTextI18n")
        void setColor(ColorObject colorObject) {
            try {
                if (bottomSheetTheme != null) {

                    ColorApplier.applyColorToView(background, bottomSheetTheme.getBackground(), R.drawable.button_rounded);

                    title.setTextColor(Color.parseColor(bottomSheetTheme.getTextColor()));
                }
                ColorApplier.applyColorToView(colorView, colorObject.getColor(), R.drawable.button_rounded);
                title.setText(colorObject.getTitle());

                if (displayMode == NORMAL) {
                    colorView.setText("");
                }
                else if (displayMode == AVAILABLE_UPDATE) {
                    if (colorObject.getDescription() == NORMAL) {
                        colorView.setText(ICON_PENCIL);
                    }
                    else {
                        colorView.setText("");
                    }
                }
            }
            catch (Exception e) {
                title.setText("error");
            }

        }

        void setSelected() {
            if (bottomSheetTheme != null) {
                ColorApplier.applyColorToView(background, bottomSheetTheme.getIconColor(), R.drawable.button_rounded);
            }
        }
    }

    public void setSelectedColorPosition(int selectedColorPosition) {
        this.selectedColorPosition = selectedColorPosition;
    }

    public void setBottomSheetTheme(BottomSheetTheme bottomSheetTheme) {
        this.bottomSheetTheme = bottomSheetTheme;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }
}
