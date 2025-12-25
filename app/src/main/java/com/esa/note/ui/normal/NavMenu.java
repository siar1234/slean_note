package com.esa.note.ui.normal;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.objects.Entity;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.root.AppUi;

import java.util.List;

public class NavMenu extends AppUi {

    private Button menu_back;

    private LinearLayout btn_backup, btn_restore, btn_trash, settingButton;
    private final FavoritesView favoritesView = new FavoritesView();

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);

        btn_backup = backgroundView.findViewById(R.id.btn_backup);
        btn_restore = backgroundView.findViewById(R.id.btn_restore);
        btn_trash = backgroundView.findViewById(R.id.btn_trash);
        settingButton = backgroundView.findViewById(R.id.settingButton);
        menu_back =  backgroundView.findViewById(R.id.menu_back);
//        backupLowAPIDialog = mainUiManager.getBackupLowAPIDialog();

        favoritesView.findAllViewById(backgroundView);
    }

    public interface AllViewClickListener {
        void onBackPressed();
        void onExport();
        void onImport();
        void onTrash();
        void onSetting();
        void setNotFavorite(Entity entity);
        void saveFavoritesPosition(List<Entity> list);
    }
    public void setAllViewClickListener(AllViewClickListener listener) {

        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackPressed();
            }
        });
        favoritesView.readyAllEvent(new FavoritesView.EventListener() {
            @Override
            public void saveFavoritesPosition(List<Entity> entityList) {
                listener.saveFavoritesPosition(entityList);
            }

            @Override
            public void setNotFavorite(Entity entity) {
                listener.setNotFavorite(entity);
            }
        });

        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onExport();
            }
        });

        btn_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImport();
            }
        });

        btn_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTrash();
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSetting();
            }
        });
    }

    public View parentView() {
        return this.backgroundView;
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        int textColor = Color.parseColor(appTheme.getMainActivityTheme().getNavMenuTextColor());
        int iconColor = Color.parseColor(appTheme.getMainActivityTheme().getNavMenuIconColor());

        ColorApplier.applyColorToView(backgroundView, appTheme.getMainActivityTheme().getNavMenuBackGround());

        TextView text_backup, text_restore, text_trash, text_setting;

        text_backup = backgroundView.findViewById(R.id.text_backup);
        text_restore = backgroundView.findViewById(R.id.text_restore);
        text_trash = backgroundView.findViewById(R.id.text_trash);
        text_setting = backgroundView.findViewById(R.id.text_setting);

        text_backup.setTextColor(textColor);
        text_restore.setTextColor(textColor);
        text_trash.setTextColor(textColor);
        text_setting.setTextColor(textColor);

        TextView icon_backup, icon_restore, icon_trash, icon_setting;

        icon_backup = backgroundView.findViewById(R.id.icon_backup);
        icon_restore = backgroundView.findViewById(R.id.icon_restore);
        icon_trash = backgroundView.findViewById(R.id.icon_trash);
        icon_setting = backgroundView.findViewById(R.id.icon_setting);

        icon_backup.setTextColor(iconColor);
        icon_restore.setTextColor(iconColor);
        icon_trash.setTextColor(iconColor);
        icon_setting.setTextColor(iconColor);

        menu_back.setTextColor(iconColor);

        favoritesView.applyTheme(appTheme);
    }

    public FavoritesView getFavoritesView() {
        return favoritesView;
    }
}
