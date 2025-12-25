package com.esa.note.widgets;

import static com.esa.note.activity.EditNoteActivity.READ_AND_UPDATE_NOTE;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import com.esa.note.R;
import com.esa.note.activity.EditNoteActivity;
import com.esa.note.database.DatabaseHelper;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Setting;

public class NoteWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int widgetId : appWidgetIds) {

            SharedPreferences sharedPreferences = context.getSharedPreferences("Widgets", Context.MODE_PRIVATE);
           int id = sharedPreferences.getInt(widgetId+"", 118);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_note);

            if (id == -1) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(widgetId+"", widgetId);
                editor.apply();


                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
            else {
                Entity entity = new Entity();
                DatabaseHelper databaseHelper  = new DatabaseHelper(context);
                Cursor cursor = databaseHelper.loadSpecificEntity(id);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
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
                    }
                    cursor.close();
                }
                databaseHelper.close();
                entity.createDetail(context);
                if (entity.isFolder()) {

                }
                else {
                    remoteViews.setTextViewText(R.id.title, entity.getTitle());
                    Setting setting = new Setting();
                    setting.loadAllSettings(context);

                    int textColor = Color.parseColor(setting.getTheme().getEditNoteActivityTheme().getTextColor());
                  //  remoteViews.setTextColor(R.id.title, textColor);
                    remoteViews.setTextViewText(R.id.content, entity.getContent());
                    Intent intent = new Intent(context, EditNoteActivity.class);


                    intent.putExtra("setting", setting);
                    intent.putExtra("howLoading", READ_AND_UPDATE_NOTE);
                    intent.putExtra("entity", entity);

                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    remoteViews.setOnClickPendingIntent(R.id.title, pendingIntent);
                    remoteViews.setOnClickPendingIntent(R.id.content, pendingIntent);


                    remoteViews.setInt(R.id.background, "setBackgroundColor",
                            Color.parseColor(setting.getTheme().getEditNoteActivityTheme().getBackground()) );

                }
            }
            appWidgetManager.updateAppWidget(widgetId, remoteViews);


        }

    }
}
