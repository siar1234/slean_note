package com.esa.note.ui.manager;

import static com.esa.note.library.Public.AVAILABLE_UPDATE;
import static com.esa.note.library.Public.NORMAL;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.esa.note.R;
import com.esa.note.internet.LoginUserModel;
import com.esa.note.library.ViewGestureListener;
import com.esa.note.listener.SliderInterface;
import com.esa.note.objects.AppAccount;
import com.esa.note.objects.Setting;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.adapter.SliderAdapter;
import com.esa.note.ui.dialog.AskDialog;
import com.esa.note.ui.dialog.ChooseTemplateDialog;
import com.esa.note.ui.dialog.LoginDialog;
import com.esa.note.ui.dialog.RegisterDialog;
import com.esa.note.ui.root.AppUi;

import java.util.ArrayList;
import java.util.List;

public class SettingUiManager extends AppUi {

    private  CheckBox isDefault_onlyShowTitle, isDefault_hideFavs;
    private  TextView settingTitle;
    private  TextView text1, text2, text3;
    private  Button addThemeButton, backButton, loginButton, registerButton;
    private  ViewPager2 themeSelector;
    private SliderAdapter sliderAdapter;
    private List<AppTheme> themeList = new ArrayList<>();
    private final ChooseTemplateDialog chooseTemplateDialog = new ChooseTemplateDialog();
    private final AskDialog deleteThemeDialog = new AskDialog();
    private final LoginDialog loginDialog = new LoginDialog();
    private final RegisterDialog registerDialog = new RegisterDialog();

    public boolean uiBackPressed() {
        if (chooseTemplateDialog.isShowing()) {
            chooseTemplateDialog.cancel();
            return true;
        }
        else if (deleteThemeDialog.isShowing()) {
            deleteThemeDialog.cancel();
            return true;
        }
        else if (sliderAdapter.getDisplayMode() == AVAILABLE_UPDATE) {
            sliderAdapter.setDisplayMode(NORMAL);
            sliderAdapter.notifyDataSetChanged();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);

        settingTitle = backgroundView.findViewById(R.id.settingTitle);
        backButton = backgroundView.findViewById(R.id.backButton);

        text1 = backgroundView. findViewById(R.id.text1);
        text2 = backgroundView.findViewById(R.id.text2);
        text3 = backgroundView.findViewById(R.id.text3);
        addThemeButton = backgroundView.findViewById(R.id.addThemeButton);

        isDefault_onlyShowTitle = backgroundView.findViewById(R.id.isDefault_onlyShowTitle);
        isDefault_hideFavs = backgroundView.findViewById(R.id.isDefault_hideFavs);

        themeSelector = backgroundView.findViewById(R.id.themeSelector);

        chooseTemplateDialog.findAllViewById(context);
        deleteThemeDialog.findAllViewById(context);
        deleteThemeDialog.setTitle(context.getResources().getString(R.string.ask_delete_theme));
        loginDialog.findAllViewById(context);
        registerDialog.findAllViewById(context);

        loginButton = backgroundView.findViewById(R.id.loginButton);
        registerButton= backgroundView.findViewById(R.id.registerButton);
    }

    @Override
    public void readyAllDialogs(Activity activity) {
        super.readyAllDialogs(activity);
        chooseTemplateDialog.readyDialog(activity);
        deleteThemeDialog.readyDialog(activity);
        loginDialog.readyDialog(activity);
        registerDialog.readyDialog(activity);
    }

    public interface EventListener {
        void toCreateTheme(AppTheme appTheme);
        void backPressed();
        void onThemeSelected(AppTheme appTheme);
        void deleteTheme();
        void onLogin(LoginUserModel loginUserModel);
        void onRegister(AppAccount appAccount);
    }

    public void readyAllEvent(EventListener listener) {
        chooseTemplateDialog.setOnEventListener(new ChooseTemplateDialog.onEventListener() {
            @Override
            public void onTemplateSelected(AppTheme appTheme) {
                chooseTemplateDialog.cancel();
                listener.toCreateTheme(appTheme);
            }

            @Override
            public void onCanceled() {
                chooseTemplateDialog.cancel();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.backPressed();
            }
        });
        addThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTemplateDialog.show();
            }
        });

        themeSelector.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                listener.onThemeSelected(themeList.get(position));
                applyTheme(themeList.get(position));
            }
        });

        deleteThemeDialog.setOnEventListener(new AskDialog.onEventListener() {
            @Override
            public void onYesClick() {
                listener.deleteTheme();
            }

            @Override
            public void onNoClick() {
                deleteThemeDialog.cancel();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.show();
            }
        });

        loginDialog.setLoginListener(new LoginDialog.LoginListener() {
            @Override
            public void onLogin(String id, String password) {
                LoginUserModel loginUserModel = new LoginUserModel();
                loginUserModel.setId(id);
                loginUserModel.setPassword(password);
                listener.onLogin( loginUserModel);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog.show();
            }
        });

        registerDialog.setRegisterListener(new RegisterDialog.RegisterListener() {
            @Override
            public void onRegister(AppAccount appAccount) {
                listener.onRegister(appAccount);
            }
        });
    }

    public void setSettingUpdateListener(Setting setting) {
        ViewGestureListener.set(isDefault_onlyShowTitle).setOnCheckedListener(new ViewGestureListener.OnCheckedListener() {
            @Override
            public void checked() {
                setting.setShowTitleOnlyIsDefault(true);
            }

            @Override
            public void unChecked() {
                setting.setShowTitleOnlyIsDefault(false);
            }
        });
        ViewGestureListener.set(isDefault_hideFavs).setOnCheckedListener(new ViewGestureListener.OnCheckedListener() {
            @Override
            public void checked() {
                setting.setHideFavs(true);
            }

            @Override
            public void unChecked() {
                setting.setHideFavs(false);
            }
        });
    }

    public void displayThemes(SliderInterface sliderInterface) {
        sliderAdapter = new SliderAdapter(context, themeList, sliderInterface);
        themeSelector.setAdapter(sliderAdapter);
    }

    public void applySetting(Setting setting) {
        isDefault_onlyShowTitle.setChecked(setting.isShowTitleOnlyIsDefault());
        isDefault_hideFavs.setChecked(setting.isHideFavs());
    }

    public void focusOnTheme(int position) {
        themeSelector.setCurrentItem(position);
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        if (sliderAdapter != null) {
            sliderAdapter.setTheme(appTheme);
        }

        ColorApplier.applyColorToView(backgroundView, appTheme.getSettingActivityTheme().getBackground());

        int textColor = Color.parseColor(appTheme.getSettingActivityTheme().getTextColor());
        int iconColor = Color.parseColor(appTheme.getSettingActivityTheme().getIconColor());
        int checkboxColor = Color.parseColor(appTheme.getSettingActivityTheme().getCheckBoxColor());
        int textHighLightColor = Color.parseColor(appTheme.getSettingActivityTheme().getTextSelectionColor());

        text1.setTextColor(textColor);
        text1.setHighlightColor(textHighLightColor);
        text2.setTextColor(textColor);
        text2.setHighlightColor(textHighLightColor);
        text3.setTextColor(textColor);
        text3.setHighlightColor(textHighLightColor);

        settingTitle.setTextColor(textColor);

        backButton.setTextColor(iconColor);

        ColorApplier.applyCheckboxColor(isDefault_onlyShowTitle, checkboxColor);
        ColorApplier.applyCheckboxColor(isDefault_hideFavs, checkboxColor);

        chooseTemplateDialog.applyTheme(appTheme.getSettingActivityTheme().getDialogTheme());
    }

    public void addThemeOnSlider(AppTheme appTheme) {
        themeList.add(appTheme);
        sliderAdapter.notifyItemInserted(themeList.size() - 1);
    }
    public void updateThemeOnSlider(AppTheme appTheme, int position) {
        themeList.remove(position);
        themeList.add(position, appTheme);
        sliderAdapter.setDisplayMode(NORMAL);
        sliderAdapter.notifyDataSetChanged();
    }
    public void removeThemeOnSlider(int position) {
        themeList.remove(position);
        sliderAdapter.notifyItemRemoved(position);

        sliderAdapter.setDisplayMode(NORMAL);
        sliderAdapter.notifyDataSetChanged();
    }

    public void setThemeList(List<AppTheme> themeList) {
        this.themeList = themeList;
    }

    public AskDialog getDeleteThemeDialog() {
        return deleteThemeDialog;
    }
}
