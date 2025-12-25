package com.esa.note.internet;

import android.util.Log;

import com.esa.note.objects.Entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostEntityModel {

    @SerializedName("Id")
    @Expose
    private final int id;

    @SerializedName("Title")
    @Expose
    private final String title;

    @SerializedName("Content")
    @Expose
    private final String content;

    @SerializedName("CreatedDate")
    @Expose
    private final long createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    private final long modifiedDate;

    @SerializedName("Type")
    @Expose
    private final int type;

    @SerializedName("Description")
    @Expose
    private int description;

    @SerializedName("Home")
    @Expose
    private final int home;

    @SerializedName("Parent")
    @Expose
    private int parent;

    @SerializedName("Position")
    @Expose
    private final int position;
    @SerializedName("Position_Favorite")
    @Expose
    private final int position_favorite;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("TextColor")
    @Expose
    private  String textColor;
    @SerializedName("Background")
    @Expose
    private  String background;

    public PostEntityModel(Entity entity) {
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        createdDate = entity.getCreatedDate();
        modifiedDate = entity.getModifiedDate();
        type = entity.getDescription();
        home = entity.getParent();
        position = entity.getPosition();
        position_favorite = entity.getPosition_favorite();
    }

    public void print(String string) {
        Log.d(string, "---------------------------------------------");
        Log.d(string+"id", this.getId()+"");
        Log.d("씨발 title", this.getTitle()+"");
        Log.d("씨발 content", this.getContent()+"");
        Log.d("씨발 createdDate", this.getCreatedDate()+"");
        Log.d("씨발 modifiedDate", this.getModifiedDate()+"");
        Log.d("씨발 description", this.getType()+"");
        Log.d("씨발 parent", this.getHome()+"");
        Log.d("씨발 position", this.getPosition()+"");
        Log.d("씨발 position_favorite", this.getPosition_favorite()+"");
        Log.d(string, "---------------------------------------------");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public int getType() {
        return type;
    }

    public int getHome() {
        return home;
    }

    public int getPosition() {
        return position;
    }

    public int getPosition_favorite() {
        return position_favorite;
    }

    public String getPassword() {
        return password;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getBackground() {
        return background;
    }

    public int getDescription() {
        return description;
    }

    public int getParent() {
        return parent;
    }
}
