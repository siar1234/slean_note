package com.esa.note.activity;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.DEFAULT_DARK;
import static com.esa.note.library.Public.UPDATE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.esa.note.R;
import com.esa.note.database.DatabaseHelper;
import com.esa.note.database.ListCRUDHelper;
import com.esa.note.internet.APIManager;
import com.esa.note.internet.HttpService;
import com.esa.note.internet.LoginUserModel;
import com.esa.note.internet.RetrofitBuilder;
import com.esa.note.listener.SliderInterface;
import com.esa.note.objects.AppAccount;
import com.esa.note.objects.Setting;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.manager.SettingUiManager;
import com.esa.note.ui.normal.NativeUi;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;


public class SettingActivity extends AppCompatActivity implements SliderInterface {

    Setting setting;
    private AppTheme selectedTheme;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private final SettingUiManager settingUiManager = new SettingUiManager();
    private int themePosition = 0;
    private int requestCode;
    private String backendURL  = "http://192.168.35.157:5000/";
    private EditText urlInput, nameInput;


    @Override
    public void onBackPressed() {
        if (settingUiManager.uiBackPressed() == false) {
            setting.saveSetting(this);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            intent.putExtra("changedSettings", setting);
            intent.putExtra("backend", backendURL);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        nameInput = findViewById(R.id.nameInput);
       urlInput = findViewById(R.id.urlInput);
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setting.setUserName(nameInput.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        urlInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                backendURL = urlInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Button saveURL = findViewById(R.id.saveURL);
        saveURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting.setBackendURL(urlInput.getText().toString());
                DatabaseHelper databaseHelper = new DatabaseHelper(SettingActivity.this);
                databaseHelper.saveBackendURL(urlInput.getText().toString());
                databaseHelper.close();
            }
        });

        setting = (Setting) getIntent().getSerializableExtra("setting");
        nameInput.setText(setting.getUserName());
        urlInput.setText(setting.getBackendURL());

        selectedTheme = setting.getTheme();

        List<AppTheme> themeList = ListCRUDHelper.allThemeList(this);

        settingUiManager.findAllViewById( findViewById(R.id.background) );
        settingUiManager.readyAllDialogs(this);
        settingUiManager.setThemeList(themeList);
        settingUiManager.displayThemes(this);

        int position = 0;
        if (selectedTheme.getDescription() == DEFAULT_DARK) {
            position = 1;
        }
        else {
            for (int i = 0; i < themeList.size(); i++) {
                if (selectedTheme.getId() == themeList.get(i).getId()) {
                    position = i;
                    break;
                }
            }
        }
        settingUiManager.focusOnTheme(position);

        settingUiManager.applySetting(setting);

        settingUiManager.readyAllEvent(new SettingUiManager.EventListener() {
            @Override
            public void deleteTheme() {
                DatabaseHelper databaseHelper = new DatabaseHelper(SettingActivity.this) ;
                databaseHelper.deleteTheme(selectedTheme);
                databaseHelper.close();
                settingUiManager.removeThemeOnSlider(themePosition);
            }

            @Override
            public void toCreateTheme(AppTheme appTheme) {
                Intent intent = new Intent(SettingActivity.this, EditThemeActivity.class);
                intent.putExtra("appTheme", appTheme);
                intent.putExtra("howSaveTheme", ADD);
                requestCode = ADD;
                activityResultLauncher.launch(intent);
            }

            @Override
            public void backPressed() {
                onBackPressed();
            }

            @Override
            public void onThemeSelected(AppTheme appTheme) {
                setting.setTheme(appTheme);
                selectedTheme = appTheme;
                NativeUi.setAllBarColor(SettingActivity.this, selectedTheme.getSettingActivityTheme());
            }
            //@EverythingIsNonNull
            @Override
            public void onRegister(AppAccount appAccount) {

               // SettingActivity.this.getDatabasePath("Note.db").getAbsolutePath()
                    File file = new File("/storage/emulated/0/Download/ok.png");
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),  file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("d", file.getName(), requestBody);

                    HttpService httpService = RetrofitBuilder.getClient("http://10.0.2.2:5000/").create(HttpService.class);



//                    RequestBody requestBody = new MultipartBody.Builder()
//                            .setType(MultipartBody.FORM).addFormDataPart("d", )
                    Call<Boolean> call = httpService.callUploadApi(filePart);
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call,  Response<Boolean> response) {
                            Boolean success = response.body();
                            if (success != null) {
                                Log.d("res", success+"");
                                if (success == true) {
                                    Toast.makeText(SettingActivity.this, "yes", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(SettingActivity.this, "shit", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Log.d("res", "null");
                                Log.d("res", response.errorBody()+"");
                                Log.d("res", response.toString()+"");
                                Log.d("res", response.message()+"");
                                Log.d("res", response.isSuccessful()+"");
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(SettingActivity.this, "fuck, "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("onFail", t.getMessage());
                        }
                    });

            }

            @Override
            public void onLogin(LoginUserModel loginUserModel) {
                HttpService httpService = RetrofitBuilder.getClient(backendURL).create(HttpService.class);

                APIManager apiManager = new APIManager(httpService);

                apiManager.login(loginUserModel, new Callback<Boolean>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        List<String> CookieList = response.headers().values("Set-Cookie");
                        Log.d("hey", CookieList+"");
                        Boolean success = response.body();
                        if (success != null) {
                            Log.d("res", success+"");
                            if (success == true) {
                                Toast.makeText(SettingActivity.this, "hi", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(SettingActivity.this, "no", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(SettingActivity.this, "fuck, "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("onFail", t.getMessage());
                        // t.get
                    }
                });





            }
        });
        settingUiManager.setSettingUpdateListener(setting);

        NativeUi.setAllBarColor(this, selectedTheme.getSettingActivityTheme());
        settingUiManager.applyTheme(selectedTheme);
        //urlInput.setHintTextColor(Color.parseColor(selectedTheme.getSettingActivityTheme().getDialogTheme().getHintColor()));
      //  urlInput.setTextColor(Color.parseColor(selectedTheme.getSettingActivityTheme().getTextColor()));

        activityResultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                if (requestCode == ADD && resultCode == RESULT_OK) {
                    if (result.getData() != null) {
                        AppTheme appTheme = (AppTheme) result.getData().getSerializableExtra("appTheme");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseHelper databaseHelper = new DatabaseHelper(SettingActivity.this);
                                databaseHelper.addTheme(appTheme);
                                databaseHelper.close();
                                int id = (int) databaseHelper.lastInsertedId;
                                appTheme.setId(id);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        settingUiManager.addThemeOnSlider(appTheme);
                                    }
                                });
                            }
                        }).start();
                    }

                }
                else if (requestCode == UPDATE && resultCode == RESULT_OK) {
                    if (result.getData() != null) {
                        AppTheme updatedTheme = (AppTheme) result.getData().getSerializableExtra("appTheme");
                        settingUiManager.updateThemeOnSlider(updatedTheme, themePosition);

                        selectedTheme = updatedTheme;
                    }
                }
            }
        });

    }

    @Override
    public void onThemeModify(AppTheme appTheme, int position) {
        themePosition = position;
        Intent intent = new Intent(this.getApplicationContext(), EditThemeActivity.class);
        intent.putExtra("appTheme", appTheme);
        intent.putExtra("howSaveTheme", UPDATE);
        requestCode = UPDATE;
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onDeleteTheme(AppTheme appTheme, int position) {
        themePosition = position;
        selectedTheme = appTheme;
        settingUiManager.getDeleteThemeDialog().show();
    }
}