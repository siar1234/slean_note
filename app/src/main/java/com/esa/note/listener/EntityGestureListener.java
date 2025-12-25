package com.esa.note.listener;

import com.esa.note.objects.Entity;

public interface EntityGestureListener {

    void ClickedEntity(Entity entity, int position);

    void ClickedDetail(Entity entity);

    void CLickedEdit(Entity entity, int position);

    void ChangedFavState(Entity entity , int how);

//    void setFavorite(Entity entity);
//
//    void setNotFavorite(Entity entity);

    void entitySelected(Entity entity);

    void entityDeSelected(Entity entity);

    void onSortEntityMode();
}
