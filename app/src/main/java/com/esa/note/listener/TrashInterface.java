package com.esa.note.listener;

import com.esa.note.objects.Entity;

public interface TrashInterface {
    void onItemSelected(Entity entity);
    void onItemDeselected(Entity entity);
}
