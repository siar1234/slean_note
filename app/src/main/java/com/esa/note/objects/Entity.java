package com.esa.note.objects;

import static com.esa.note.library.Public.DELETED;
import static com.esa.note.library.Public.FAVORITE;
import static com.esa.note.library.Public.HIDE_CONTENT;
import static com.esa.note.library.Public.IN_FIRSTPAGE;
import static com.esa.note.library.Public.IN_FOLDER;
import static com.esa.note.library.Public.NOTE;
import static com.esa.note.library.Public.NOT_FAVORITE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.esa.note.R;
import com.esa.note.internet.PostEntityModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Entity implements Serializable {

    public Entity() {

    }
    public Entity(PostEntityModel postEntityModel) {
        id = postEntityModel.getId();
        title = postEntityModel.getTitle();
        content = postEntityModel.getContent();
        createdDate = postEntityModel.getCreatedDate();
        modifiedDate = postEntityModel.getModifiedDate();
        description = postEntityModel.getDescription();
        parent = postEntityModel.getParent();
        position = postEntityModel.getPosition();
        position_favorite = postEntityModel.getPosition_favorite();

    }

    @SerializedName("Id")
    private int id;

    @SerializedName("Title")
    private String title;

    @SerializedName("Content")
        private String content;

    @SerializedName("CreatedDate")
    private long createdDate;
    @SerializedName("ModifiedDate")
        private long modifiedDate;

        private long deletedDate;

    @SerializedName("Type")
    private int description;
    @SerializedName("Home")
    private int parent;

    @SerializedName("Position")
    private int position;
    @SerializedName("Position_Favorite")
    private int position_favorite;
    @SerializedName("Password")
    private String password;
    @SerializedName("TextColor")
    private String textColor;
    @SerializedName("Background")
    private String background;
    private String detail;

    private Page page;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public long getDeletedDate() {
        return deletedDate;
    }
    public void setDeletedDate(long deletedDate) {
        this.deletedDate = deletedDate;
    }
    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
//        Log.d("description", description+"");
        this.description = description;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public int getPosition_favorite() {
        return position_favorite;
    }

    public void setPosition_favorite(int position_favorite) {
        this.position_favorite = position_favorite;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void clone(Entity oldEntity) {
        this.setId(oldEntity.getId());
        this.setTitle(oldEntity.getTitle());
        this.setContent(oldEntity.getContent());
        this.setCreatedDate(oldEntity.getCreatedDate());
        this.setModifiedDate(oldEntity.getModifiedDate());
        this.setDescription(oldEntity.getDescription());
        this.setParent(oldEntity.getParent());
        this.setPosition(oldEntity.getPosition());
        this.setPosition_favorite(oldEntity.getPosition_favorite());
        this.setPassword(oldEntity.getPassword());
        this.setTextColor(oldEntity.getTextColor());
        this.setBackground(oldEntity.getBackground());
        this.setDetail(oldEntity.getDetail());
    }

    public boolean isOnlyShowTitle() {
        if (Entity.this.getDescription() == NOTE + HIDE_CONTENT + FAVORITE + IN_FIRSTPAGE ||
                Entity.this.getDescription() == NOTE + HIDE_CONTENT + FAVORITE + IN_FOLDER ||
                Entity.this.getDescription() ==  NOTE + HIDE_CONTENT + NOT_FAVORITE + IN_FOLDER  ||
                Entity.this.getDescription() == NOTE + HIDE_CONTENT + NOT_FAVORITE + IN_FIRSTPAGE) {
            return true;
        }
        else {
            return false;
        }
    }

    public void createDetail(Context context) {
        String detail;
        detail = context.getResources().getString(R.string.createdDate)
                +" "
                + DateTime.dateText(context, createdDate)
                +"\n"
                +context.getResources().getString(R.string.modifiedDate)
                +" "
                + DateTime.dateText(context, modifiedDate);
        this.setDetail(detail);
        //Log.d("detail", detail);
    }

    public boolean isNote() {
        if (description >= 10000) {
            if (description / 1000 == 11) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (description / 1000 == 1) {
                return true;
            }
            else {
                return false;
            }
        }

    }
    public boolean isFolder() {
        if (description >= 10000) {
            if (description / 1000 == 12) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (description / 1000 == 2) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    public void setDeleted() {
        content =
                Long.parseLong(new SimpleDateFormat("yyyyMMddkkmm", Locale.getDefault()).format(new Date()))
                        + content;
        description += 10000;
        Log.d("changed con", content);
    }

    public void setRestore() {
        description -= DELETED;
        content = content.substring(12);
        print("");
    }

    public boolean isFavorite() {
        if (this.getDescription() / 10 % 10 == 1) {

            return true;
        }
        else {
            return false;
        }
    }

    public void changeFavorite(int how) {
        if (how == FAVORITE) {
            this.setDescription(this.getDescription() + FAVORITE);
        }
        else if (how == NOT_FAVORITE) {
            this.setDescription(this.getDescription() - FAVORITE);

        }
    }
    public boolean isInFirstPage() {
        if (description % 10 == 0) {
            return true;
        }
        else {
            return false;
        }
    }
    public void setIfFirstPage(boolean should) {
        int old =  this.getDescription();
        if (should) {
            setDescription(old - 1);
        }
        else {
            setDescription(old + 1);
        }

    }
    public void print( String str) {
        Log.d(str+"entity", "------------------------------------------------------------");
        Log.d(str+"id", this.getId()+"");
        Log.d(str+"title", this.getTitle()+"");
        Log.d(str+"content", this.getContent()+"");
        Log.d(str+"createdDate", this.getCreatedDate()+"");
        Log.d(str+"modifiedDate", this.getModifiedDate()+"");

        String desc = "";
        if (isNote()) {
            desc += "Note_";
            if (isOnlyShowTitle()) {
                desc += "HideContent_";
            }
            else {
                desc += "ShowContent_";
            }
        }
        else {
            desc += "Folder_";
            if (isGridFolder()) {
                desc += "Grid_";
            }
            else {
                desc += "Linear_";
            }
        }
        if (isFavorite()) {
            desc += "Favorite_";
        }
        else {
            desc += "Normal_";
        }
        if (isInFirstPage()) {
            desc += "InFirstPage";
        }
        else {
            desc += "InFolder";
        }

        Log.d(str+"description to text", desc);
        Log.d(str+"description", this.getDescription()+"");
        Log.d(str+"parent", this.getParent()+"");
    }

    public boolean isGridFolder() {
        if (description / 100 % 10 == 2) {
            return true;
        }
        else {
            return false;
        }
    }

    public int findPosition(List<Entity> list) {
        int position = 0;
        for (Entity entity : list) {
            if (entity.getId() == this.getId()) {
                break;
            }
            else {
                position++;
            }
        }
        return position;
    }

    public void setLinear() {
        if (this.isGridFolder()) {
            description += 100;
        }
    }
    public void setGrid() {
        if (this.isGridFolder() == false) {
            description -= 100;
        }
    }

    public boolean shouldDelete() {
        try {
            long currentDate = DateTime.currentDate();

            if (currentDate > 1) {
                this.setDeletedDate(Long.parseLong(this.getContent().substring(0, 12)));

                String deletedDate = String.valueOf(this.getDeletedDate());

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmm");

                Calendar calendar = Calendar.getInstance();
                Date date = simpleDateFormat.parse(deletedDate);
                if (date != null) {
                    calendar.setTime(date);

                    calendar.add(Calendar.DATE, 30);

                    String string = simpleDateFormat.format(calendar.getTime());
                    long expectedDate = Long.parseLong(string);

                    this.print("from delete,  ");
                    Log.d("expected", expectedDate + "");
                    if (expectedDate < currentDate) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }


        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isDeleted() {
        if (description >= DELETED) {
            return true;
        }
        else {
            return false;
        }
    }

    public void readCursor(Cursor cursor) {
        try {
            this.setId(cursor.getInt(0));
            this.setTitle(cursor.getString(1));
            this.setContent(cursor.getString(2));
            this.setCreatedDate(cursor.getLong(3));
            this.setModifiedDate(cursor.getLong(4));
            this.setDescription(cursor.getInt(5));
            this.setParent(cursor.getInt(6));
            this.setPosition(cursor.getInt(7));
            this.setPosition_favorite(cursor.getInt(8));
            this.setPassword(cursor.getString(9));
            this.setTextColor(cursor.getString(10));
            this.setBackground(cursor.getString(11));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

 }


