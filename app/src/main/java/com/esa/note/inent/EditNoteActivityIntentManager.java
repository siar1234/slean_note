package com.esa.note.inent;

import static android.app.Activity.RESULT_OK;
import static com.esa.note.library.Public.CHOOSE_IMAGE;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.esa.note.activity.EditNoteActivity;

public class EditNoteActivityIntentManager {

    private EditNoteActivity editNoteActivity;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int requestCode = 0;

    public interface IntentResultListener {
       void onChooseImage(Uri uri);
    }

    public void listen(EditNoteActivity editNoteActivity, IntentResultListener listener) {
        this.editNoteActivity = editNoteActivity;
        activityResultLauncher = editNoteActivity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {
                            if (result.getData() != null) {
                                Uri uri = result.getData().getData();
                                listener.onChooseImage(uri);

                            }
                        }
                    }
                });
    }

    public void launchToChooseImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        requestCode = CHOOSE_IMAGE;
        activityResultLauncher.launch(intent);

    }

}
