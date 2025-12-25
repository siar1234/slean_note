package com.esa.note.ui.manager;

import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.EXPORT_TEXT_FILES;
import static com.esa.note.library.Public.FAVORITES;
import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.IMPORT_TEXT_FILES;
import static com.esa.note.library.Public.LINEAR;
import static com.esa.note.library.Public.MOVE;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.RESTORE;
import static com.esa.note.library.Public.SORT_ENTITIES;
import static com.esa.note.ui.dialog.ChooseFileTypeDialog.REQUEST_CODE_EXPORT;
import static com.esa.note.ui.dialog.EditFolderDialog.ADD_FOLDER;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import com.esa.note.R;
import com.esa.note.database.ListCRUDHelper;
import com.esa.note.listener.EntityGestureListener;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Page;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.dialog.AskDialog;
import com.esa.note.ui.dialog.ChooseFileTypeDialog;
import com.esa.note.ui.dialog.DetailDialog;
import com.esa.note.ui.dialog.EditFolderDialog;
import com.esa.note.ui.dialog.FilePathDialog;
import com.esa.note.ui.normal.FloatingButtons;
import com.esa.note.ui.normal.NavMenu;
import com.esa.note.ui.normal.PopupMenu;
import com.esa.note.ui.normal.TitleBar;
import com.esa.note.ui.root.AppUi;

import java.util.ArrayList;
import java.util.List;

public class MainUiManager extends AppUi {

    public FrameLayout mainFrame, background;

    protected DrawerLayout drawerLayout;

    private View backgroundView;

    private final PopupMenu popupMenu = new PopupMenu();
    private final NavMenu navMenu = new NavMenu();
    private final FloatingButtons floatingButtons = new FloatingButtons();
    private final EditFolderDialog editFolderDialog = new EditFolderDialog();
    private final DetailDialog detailDialog = new DetailDialog();
    private final AskDialog deleteDialog = new AskDialog();
    private final TitleBar titleBar = new TitleBar();
    private final FilePathDialog filePathDialog = new FilePathDialog();
    private final List<Entity> folderStack = new ArrayList<>();
    private final ChooseFileTypeDialog chooseFileTypeDialog = new ChooseFileTypeDialog();
    private Page firstPage;
    private List<Page> everyPages = new ArrayList<>();
    private IntentListener listener;

    public interface IntentListener {
        void toCreateNote();
        void toOpenTrash();
        void toUpdateSetting();
        void toMoveToOther();
        void doneMoveToOther();
        void cancelMoveToOther();
        void showLinearView();
        void showGridView();
        void onDelete();
        void toExportDatabase();
        void toExportTextFile(String path);
        void toImportDatabase();
        void toImportTextFile(String path);
        void saveFolder(int requestCode, Entity folder);
        void saveEntitiesPosition(List<Entity> list , int how);
        void setNotFavorite(Entity entity);
    }

    public void readyAllEvent(IntentListener listener) {
        this.listener = listener;
        deleteDialog.setOnEventListener(new AskDialog.onEventListener() {
            @Override
            public void onYesClick() {
                deleteDialog.cancel();
                listener.onDelete();
            }

            @Override
            public void onNoClick() {
                deleteDialog.cancel();
            }
        });
        filePathDialog.readyAllEvent(new FilePathDialog.EventListener() {
            @Override
            public void okClicked(String filePath, int requestCode) {
                if (requestCode == EXPORT_TEXT_FILES) {
                    listener.toExportTextFile(filePath);
                }
                else if (requestCode == IMPORT_TEXT_FILES) {
                    listener.toImportTextFile(filePath);
                }
            }
        });
        chooseFileTypeDialog.setSelectListener(new ChooseFileTypeDialog.SelectListener() {
            @Override
            public void databaseFileSelected(int requestCode) {
                if (requestCode == REQUEST_CODE_EXPORT) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        listener.toExportDatabase();
                    }
                    else {
                        filePathDialog.show();
                    }
                }
                else {
                    if (Build.VERSION.SDK_INT >= 19) {
                        listener.toImportDatabase();
                    }
                    else {
                        filePathDialog.show(RESTORE);
                        filePathDialog.okButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                             //   BackupHelper.importDatabase(mainActivity, "");
                                filePathDialog.cancel();
                            }
                        });
                    }
                }
            }

            @Override
            public void textFileSelected(int requestCode) {
                if (requestCode == REQUEST_CODE_EXPORT) {
                    filePathDialog.show(EXPORT_TEXT_FILES);
                }
                else {
                    filePathDialog.show(IMPORT_TEXT_FILES);
                }

            }
        });

        titleBar.setOnMenuButtonListener(new TitleBar.OnMenuButtonListener() {
            @Override
            public void onShowMenu() {
                openDrawer();
            }

            @Override
            public void onPopupMenuShow() {
                popupMenu.show();
            }

            @Override
            public void onMoveToOther() {
                listener.toMoveToOther();
            }

            @Override
            public void onDeleteClicked() {
                deleteDialog.show();
            }

            @Override
            public void doneMoveToOther() {
                listener.doneMoveToOther();
            }

            @Override
            public void backPressed() {
                isNeedBackPressedAction();
            }

            @Override
            public void cancelMoveToOther() {
                listener.cancelMoveToOther();
            }
        });
        popupMenu.setAllViewClickListener(new PopupMenu.AllViewClickListener() {
            @Override
            public void sortByCreatedDate() {
                if (currentPage() != null) {
                    currentPage().sortByCreatedDate();
                }

            }

            @Override
            public void sortByModifiedDate() {
                if (currentPage() != null) {
                    currentPage().sortByModifiedDate();
                }

            }

            @Override
            public void sortReverse() {
                if (currentPage() != null) {
                    currentPage().sortByReverse();
                }
            }

            @Override
            public void showLinearView() {
                if (currentPage() != null) {
                    if (currentPage().getDisplayMode() == GRID) {
                        changePage( currentPage().showingView(), currentPage().getLinearView());
                        currentPage().setDisplayMode(LINEAR);

                        listener.showLinearView();
                    }
                }
            }

            @Override
            public void showGridView() {
                if (currentPage() != null) {
                    if (currentPage().getDisplayMode() == LINEAR) {
                        changePage( currentPage().showingView(), currentPage().getGridView());
                        currentPage().setDisplayMode(GRID);

                        listener.showGridView();
                    }
                }

            }
        });
        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupMenu.isShowing) {
                    popupMenu.hide();
                }
                return false;
            }
        });
        floatingButtons.setOnButtonClickListener(new FloatingButtons.OnButtonClickListener() {
            @Override
            public void onNoteClick() {
                listener.toCreateNote();
            }

            @Override
            public void onFolderClick() {
                if (isShowingFirstPage()) {
                    editFolderDialog.show(ADD_FOLDER, new Entity());
                }
                else {
                    editFolderDialog.show(ADD_FOLDER, new Entity(), currentPage().getId());
                }
            }
        });
        navMenu.setAllViewClickListener(new NavMenu.AllViewClickListener() {

            @Override
            public void setNotFavorite(Entity entity) {
                listener.setNotFavorite(entity);
            }

            @Override
            public void saveFavoritesPosition(List<Entity> list) {

            }

            @Override
            public void onExport() {
//                chooseFileTypeDialog.setRequestCode(REQUEST_CODE_EXPORT);
//                chooseFileTypeDialog.show();
                if (Build.VERSION.SDK_INT >= 19) {
                    listener.toExportDatabase();
                }
                else {
                    filePathDialog.show();
                }
            }

            @Override
            public void onImport() {
                if (Build.VERSION.SDK_INT >= 19) {
                    listener.toImportDatabase();
                }
                else {
                    filePathDialog.show(RESTORE);
                    filePathDialog.okButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //   BackupHelper.importDatabase(mainActivity, "");
                            filePathDialog.cancel();
                        }
                    });
                }
//                chooseFileTypeDialog.setRequestCode(REQUEST_CODE_IMPORT);
//                chooseFileTypeDialog.show();
            }

            @Override
            public void onTrash() {
                listener.toOpenTrash();
            }

            @Override
            public void onSetting() {
                listener.toUpdateSetting();
            }

            @Override
            public void onBackPressed() {
                drawerLayout.closeDrawer((navMenu.parentView()));
            }
        });
        editFolderDialog.setAllViewClickListener(new EditFolderDialog.AllViewClickListener() {
            @Override
            public void saveFolder(int requestCode, Entity folder) {
                listener.saveFolder(requestCode, folder);
            }
        });
    }

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        drawerLayout = (DrawerLayout) view;
        backgroundView = view;

        mainFrame = drawerLayout.findViewById(R.id.mainFrame);
        background = drawerLayout.findViewById(R.id.background);

        titleBar.findAllViewById( backgroundView.findViewById(R.id.actionBar_v2) );
        navMenu.findAllViewById( backgroundView.findViewById(R.id.menu) );
        popupMenu.findAllViewById( backgroundView.findViewById(R.id.menu_sort));
        floatingButtons.findAllViewById( backgroundView.findViewById(R.id.floatingButtonsView) );

        editFolderDialog.findAllViewById(context);
        filePathDialog.findAllViewById(context);
        deleteDialog.findAllViewById(context);
        deleteDialog.setTitle(context.getResources().getString(R.string.ask_delete));
        detailDialog.findAllViewById(context);
        chooseFileTypeDialog.findAllViewById(context);

    }

    public void readyAllDialogs(Activity activity) {
        editFolderDialog.readyDialog(activity);

        filePathDialog.readyDialog(activity);
        filePathDialog.readyAllEvent();

        detailDialog.readyDialog(activity);

        deleteDialog.readyDialog(activity);

        chooseFileTypeDialog.readyDialog(activity);
    }

    public void notifyMultipleItemRemoved(Context context , List<Entity> entityList , List<Entity> favList ) {
        navMenu.getFavoritesView().updateView(DELETE, favList);
        currentPage().updateView(DELETE, entityList);
        floatingButtons.showEveryButtons();
        titleBar.setActionBarMode(NORMAL);
        currentPage().getGridAdapter().backToDefault();
        currentPage().getLinearAdapter().backToDefault();
        ListCRUDHelper.savePosition(context, currentPage().getList(), NORMAL);
    }

    public FloatingButtons getFloatingButtons() {
        return floatingButtons;
    }

    public boolean isNeedBackPressedAction() {
        if (editFolderDialog.isShowing()) {
            editFolderDialog.cancel();
            return true;
        }
        if (Build.VERSION.SDK_INT < 19) {
            if (filePathDialog.isShowing()) {
                filePathDialog.cancel();
            }
            return true;
        }
        else if (detailDialog.isShowing()) {
            detailDialog.cancel();
            return true;
        }
        else if (deleteDialog.isShowing()) {
            deleteDialog.cancel();
            return true;
        }
        else if (navMenu.getFavoritesView().favoritesViewMode() == MOVE) {
            navMenu.getFavoritesView().changeFavoritesView(NORMAL);
            listener.saveEntitiesPosition(navMenu.getFavoritesView().getList(), FAVORITES);
            return true;
        }
        else if (floatingButtons.isButtonsHiding() && titleBar.actionBarMode() == SORT_ENTITIES) {
            floatingButtons.showEveryButtons();

            titleBar.setActionBarMode(NORMAL);

            currentPage().getGridAdapter().backToDefault();
            currentPage().getLinearAdapter().backToDefault();

            listener.saveEntitiesPosition(currentPage().getList(), NORMAL);
            return true;
        }
        else if (drawerLayout.isDrawerOpen(navMenu.parentView())) {
            drawerLayout.closeDrawer((navMenu.parentView()));
            return true;
        }
        else if (popupMenu.isShowing) {
            popupMenu.hide();
            return true;
        }
        else if (folderStack.size() > 0) {
            mainFrame.removeView(folderStack.get(0).getPage().showingView());
            folderStack.remove(0);
            titleBar.setTitle(currentPage().getTitle());
            if (currentPage().getDisplayMode() == LINEAR) {
                popupMenu.applyLinearViewIsShown();
            }
            else {
                popupMenu.applyGridViewIsShown();
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void openDrawer() {
        drawerLayout.openDrawer(navMenu.parentView());
    }

    public Page currentPage() {
        if (folderStack.size() == 0) {
            return firstPage;
        }
        else {
            return folderStack.get(0).getPage();
        }
    }

    public boolean isShowingFirstPage() {
        if (folderStack.size() > 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public NavMenu getNavMenu() {
        return navMenu;
    }

    public void changePage(View oldView , View newView) {
        mainFrame.removeView(oldView);
        mainFrame.addView(newView);
    }

    public FilePathDialog getBackupLowAPIDialog() {
        return filePathDialog;
    }

    public DetailDialog getDetailDialog() {
        return detailDialog;
    }

    public EditFolderDialog getEditFolderDialog() {
        return editFolderDialog;
    }

    public Page getFirstPage() {
        return firstPage;
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);

        for (Page page : everyPages) {
            page.applyTheme(appTheme.getMainActivityTheme());
        }

        backgroundView.setBackgroundColor(Color.parseColor(appTheme.getMainActivityTheme().getBackground()));
        titleBar.applyTheme(appTheme.getMainActivityTheme());
        deleteDialog.applyTheme(appTheme.getMainActivityTheme().getDialogTheme());

        detailDialog.applyTheme(appTheme.getMainActivityTheme().getDialogTheme());
        editFolderDialog.applyTheme(appTheme.getMainActivityTheme().getDialogTheme());

        floatingButtons.applyTheme(appTheme);

        navMenu.applyTheme(appTheme);
        chooseFileTypeDialog.applyTheme(appTheme.getMainActivityTheme().getDialogTheme());
        filePathDialog.applyTheme(appTheme.getMainActivityTheme().getDialogTheme());

        popupMenu.applyTheme(appTheme);
        if (currentPage() != null) {
            if (currentPage().getDisplayMode() == LINEAR) {
                popupMenu.applyLinearViewIsShown();
            }
            else {
                popupMenu.applyGridViewIsShown();
            }
        }
        else {
            popupMenu.applyLinearViewIsShown();
        }
    }

    public List<Entity> getFolderStack() {
        return folderStack;
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }


    public void applyPage(Page page) {
        try {
            mainFrame.addView(page.showingView());
            titleBar.setTitle(page.getTitle());
            if (page.getDisplayMode() == GRID) {
                popupMenu.applyGridViewIsShown();
            }
            else {
                popupMenu.applyLinearViewIsShown();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFirstPage(Page firstPage) {
        this.firstPage = firstPage;
    }

    public void setEveryPages(List<Page> everyPages) {
        this.everyPages = everyPages;
    }

    public void displayFavorites(List<Entity> list , EntityGestureListener entityGestureListener) {
        navMenu.getFavoritesView().setList(list);
        navMenu.getFavoritesView().readyListView(entityGestureListener);
    }

    public void applyActivityModeChanged(int activityMode) {
        if (activityMode == NORMAL) {
            floatingButtons.showEveryButtons();
            titleBar.setActionBarMode(NORMAL);
            currentPage().getGridAdapter().backToDefault();
            currentPage().getLinearAdapter().backToDefault();
        }
        else if (activityMode == MOVE) {
            floatingButtons.hideEveryButtons();
            titleBar.setActionBarMode(MOVE);
        }
    }

    public List<Page> getEveryPages() {
        return everyPages;
    }

    public void jumpPlusButton() {
        floatingButtons.jumpButton(0);
    }
}
