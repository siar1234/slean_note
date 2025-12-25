package com.esa.note.database;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.Entities_in_FirstPage;
import static com.esa.note.library.Public.Every;
import static com.esa.note.library.Public.FAVORITES;
import static com.esa.note.library.Public.NORMAL;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import com.esa.note.library.Public;
import com.esa.note.objects.Entity;
import com.esa.note.theme.AppTheme;

import java.util.ArrayList;
import java.util.List;

public class ListCRUDHelper {

    private final Context context;

    public ListCRUDHelper(Context context) {
        this.context = context;
    }

    public static List<Entity> entitiesInFolder(Context context,  int parentsId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.loadEntitiesOfFolder(parentsId);
        List<Entity> entityList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                entity.readCursor(cursor);
                entityList.add(0, entity);
            }
            cursor.close();
        }

        databaseHelper.close();

        return entityList;
    }

    public static List<Entity> entityList(Context context, int how, Entity parent) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = null;
        if (how == Public.Entities_In_Folder) {
            cursor = databaseHelper.loadEntitiesOfFolder(parent.getId());
        }
        List<Entity> entityList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                entity.setId(cursor.getInt(0));
                entity.setTitle(cursor.getString(1));
                entity.setContent(cursor.getString(2));
                entity.setCreatedDate(cursor.getLong(3));
                entity.setModifiedDate(cursor.getLong(4));
                entity.setDescription(cursor.getInt(5));
                entity.setParent(cursor.getInt(6));
                entity.setPosition(cursor.getInt(7));
                entity.setPosition_favorite(cursor.getInt(8));
                entity.setPassword(cursor.getString(9));
                entity.setTextColor(cursor.getString(10));
                entity.setBackground(cursor.getString(11));

                entityList.add(0, entity);
            }
            cursor.close();
        }

        databaseHelper.close();

        return entityList;
    }

    public static List<Entity> entityList( Context context, int how) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = null;
        if (how == Public.Entities_in_FirstPage) {
            cursor = databaseHelper.load(Public.Entities_in_FirstPage);
        }
        else if (how == Public.Every) {
            cursor = databaseHelper.load(Public.Every);
        }
        else if (how == Public.EveryFolders) {
            cursor = databaseHelper.loadEveryFolders();
        }
        else if (how == Public.FAVORITES) {
            cursor = databaseHelper.loadFavorites();
        }
        else if (how == ADD) {
            cursor = databaseHelper.load(Public.ADD);
        }
        else if (how == DELETE) {
            cursor = databaseHelper.loadDeleted(Every);
        }
        List<Entity> entityList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                entity.setId(cursor.getInt(0));
                entity.setTitle(cursor.getString(1));
                entity.setContent(cursor.getString(2));
                entity.setCreatedDate(cursor.getLong(3));
                entity.setModifiedDate(cursor.getLong(4));
                entity.setDescription(cursor.getInt(5));
                entity.setParent(cursor.getInt(6));
                entity.setPosition(cursor.getInt(7));
                entity.setPosition_favorite(cursor.getInt(8));
                entity.setPassword(cursor.getString(9));
                entity.setTextColor(cursor.getString(10));
                entity.setBackground(cursor.getString(11));

                entityList.add(0, entity);
            }
            cursor.close();
        }

        databaseHelper.close();

        return entityList;
    }

    public static List<Entity> allNoteAndFolders(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.allNoteAndFolders();
        List<Entity> entityList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                entity.setId(cursor.getInt(0));
                entity.setTitle(cursor.getString(1));
                entity.setContent(cursor.getString(2));
                entity.setCreatedDate(cursor.getLong(3));
                entity.setModifiedDate(cursor.getLong(4));
                entity.setDescription(cursor.getInt(5));
                entity.setParent(cursor.getInt(6));
                entity.setPosition(cursor.getInt(7));
                entity.setPosition_favorite(cursor.getInt(8));
                entity.setPassword(cursor.getString(9));
                entity.setTextColor(cursor.getString(10));
                entity.setBackground(cursor.getString(11));

                entityList.add(0, entity);
            }
            cursor.close();
        }

        databaseHelper.close();

        return entityList;
    }

    public static void savePosition(Context context , List<Entity> entityList, int how) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                while (entityList.size() > position) {
                    if (how == FAVORITES) {
                        entityList.get(position).setPosition_favorite(position);
                    } else if (how == NORMAL) {
                        entityList.get(position).setPosition(position);
                    }
                    position++;
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                for (Entity entity : entityList) {
                    databaseHelper.updateEntity(entity);
                }
                databaseHelper.close();
            }
        }).start();

    }
    public static List<Entity> deletedEntityList(Context context , int how) {
        List<Entity> entityList = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = null;

        if (how == Entities_in_FirstPage) {
            cursor =  databaseHelper.loadDeleted(Entities_in_FirstPage);
        }
        else if (how == Every) {
            cursor =  databaseHelper.loadDeleted(Every);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                entity.setId(cursor.getInt(0));
                entity.setTitle(cursor.getString(1));
                entity.setContent(cursor.getString(2));
                entity.setCreatedDate(cursor.getLong(3));
                entity.setModifiedDate(cursor.getLong(4));

                entity.setDeletedDate(Long.parseLong(entity.getContent().substring(0, 12)));

                entity.setDescription(cursor.getInt(5));
                entity.setParent(cursor.getInt(6));
                entity.setPosition(cursor.getInt(7));
                entity.setPosition_favorite(cursor.getInt(8));
                entity.setPassword(cursor.getString(9));
                entity.setTextColor(cursor.getString(10));
                entity.setBackground(cursor.getString(11));

                entity.print("from  trash activity onCreate,   ");

                entityList.add(0, entity);
            }
            cursor.close();
        }
        databaseHelper.close();

        return entityList;
    }
    public static List<AppTheme> allThemeList(Context context) {
        List<AppTheme> list = new ArrayList<>();

        list.add(AppTheme.defaultLightTheme(context));
        list.add(AppTheme.defaultDarkTheme(context));

        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        Cursor cursor = databaseHelper.loadAllThemes();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                AppTheme appTheme = new AppTheme();
                appTheme.setDefaultLightTheme(context);
                appTheme.setId(cursor.getInt(0));
                appTheme.setTitle(cursor.getString(1));
                appTheme.read(cursor.getString(2));
                appTheme.setDescription(NORMAL);

                list.add(appTheme);
            }
            cursor.close();
        }

        databaseHelper.close();

        return list;
    }

    public interface MoveToTrashListener {
        void onMoveToTrashFinished(List<Entity> entityList, List<Entity> favList);
    }

    public static void moveToTrash(Activity activity, List<Entity> entityList, MoveToTrashListener moveToTrashListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List <Entity> favList = new ArrayList<>();
                for (Entity entity : entityList) {
                    moveToTrash(activity, entity);
                    if (entity.isFavorite()) {
                        favList.add(entity);
                    }
                    if (entity.isFolder()) {
                        for (Entity child : entity.getPage().getList()) {
                            moveToTrash(activity, child);
                        }
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        moveToTrashListener.onMoveToTrashFinished(entityList ,favList);
                    }
                });
            }
        }).start();


    }
    private static void moveToTrash(Context context , Entity entity) {
        entity.setDeleted();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.updateEntity(entity);
        databaseHelper.close();
    }
}
