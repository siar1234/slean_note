package com.esa.note.database;

import com.esa.note.objects.Entity;
import com.esa.note.ui.manager.MainUiManager;

import java.util.List;

public class EmptyInterface {
    public static MainUiManager.IntentListener emptyMainUiManagerInterface() {
        return new MainUiManager.IntentListener() {
            @Override
            public void toExportTextFile(String path) {

            }

            @Override
            public void toImportTextFile(String path) {

            }

            @Override
            public void toCreateNote() {

            }

            @Override
            public void toOpenTrash() {

            }

            @Override
            public void toUpdateSetting() {

            }

            @Override
            public void toMoveToOther() {

            }

            @Override
            public void doneMoveToOther() {

            }

            @Override
            public void cancelMoveToOther() {

            }

            @Override
            public void showLinearView() {

            }

            @Override
            public void showGridView() {

            }

            @Override
            public void onDelete() {

            }

            @Override
            public void toExportDatabase() {

            }

            @Override
            public void toImportDatabase() {

            }

            @Override
            public void saveFolder(int requestCode, Entity folder) {

            }

            @Override
            public void saveEntitiesPosition(List<Entity> list, int how) {

            }

            @Override
            public void setNotFavorite(Entity entity) {

            }
        };
    }
}
