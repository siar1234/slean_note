package com.esa.note.objects;

public class ColorObject {

    private int id;
    private String title;
    private String color;
    private int description;

    public ColorObject() {

    }

    public ColorObject(String title, String color, int description) {
        this.title = title;
        this.color = color;
        this.description = description;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }
}
