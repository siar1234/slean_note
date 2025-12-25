package com.esa.note.ui.bottomsheet;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.AVAILABLE_UPDATE;
import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.HIDE;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.TOP;
import static com.esa.note.library.Public.UPDATE;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.database.ColorListCRUDHelper;
import com.esa.note.listener.ColorSelectorInterface;
import com.esa.note.objects.ColorObject;
import com.esa.note.objects.ThemeData;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.BottomSheetTheme;
import com.esa.note.ui.adapter.ColorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SelectColorBottomSheet extends BottomSheet {

    private TextView addColorButton;
    private RecyclerView colorSelector;
    private  ColorAdapter colorAdapter;
    private List<ColorObject> colorList = new ArrayList<>();
    private boolean onlyBlackAndWhite = false;
    private int position = 0, id = 0;
    private ColorObject updatedColor = new ColorObject();

    Disposable disposable;

    public SelectColorBottomSheet() {
        isOnlyMoveBottomToTop = true;
    }

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        addColorButton = backgroundView.findViewById(R.id.addColorButton);
        colorSelector = backgroundView.findViewById(R.id.colorSelector);
    }

    public void displayColors(List<ColorObject> list, ColorSelectorInterface colorSelectorInterface) {
        colorSelector.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        colorList = list;
        colorAdapter = new ColorAdapter(context, list, colorSelectorInterface);
        colorSelector.setAdapter(colorAdapter);
    }

    public interface EventListener {
        void addColor();
    }

    public void readyAllEvent(EventListener listener) {
        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addColor();
            }
        });
    }

    public void show(ThemeData themeData) {
        animate(TOP);
        position = 0;
        for (ColorObject color : colorList) {
            if (color.getColor().equals(themeData.getColor())) {
                colorAdapter.setSelectedColorPosition(position);
                colorAdapter.notifyDataSetChanged();
                colorSelector.smoothScrollToPosition(position);
                break;
            }
            else {
                position++;
            }
        }
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);

    }


    @Override
    public void animate(int how) {
        super.animate(how);

        if (how == HIDE) {
            if (colorAdapter.getDisplayMode() == AVAILABLE_UPDATE) {
                colorAdapter.setDisplayMode(NORMAL);
                colorAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateListView(int request) {
        disposable = Observable.fromCallable(new Callable<List<ColorObject>>() {
            @Override
            public List<ColorObject> call() {
                if (request == ADD) {
                    return ColorListCRUDHelper.colorListOrderedById(context);
                } else {
                    return new ArrayList<>();
                }
            }
        }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ColorObject>>() {
            @Override
            public void accept(List<ColorObject> list) {
                if (request == ADD) {
                    ColorObject createdColor = list.get(0);
                    createdColor.setDescription(NORMAL);

                    colorList.add(0, createdColor);
                    colorAdapter.notifyItemInserted(0);
                }
                else if (request == UPDATE) {
                    updatedColor.setDescription(NORMAL);
                    colorList.remove(position);
                    colorList.add(position, updatedColor);
                    colorAdapter.notifyItemChanged(position);
                }
                else if (request == DELETE) {
                    colorList.remove(position);
                    colorAdapter.notifyItemRemoved(position);
                }
            }
        });
    }

    public void setUpdatedColor(ColorObject updatedColor) {
        this.updatedColor = updatedColor;
    }

    public ColorAdapter getAdapter() {
        return colorAdapter;
    }

    @Override
    public void applyTheme(BottomSheetTheme bottomSheetTheme) {
        super.applyTheme(bottomSheetTheme);
        addColorButton.setTextColor(iconColor);

        colorAdapter.setBottomSheetTheme(bottomSheetTheme);
        colorAdapter.notifyDataSetChanged();

    }
}
