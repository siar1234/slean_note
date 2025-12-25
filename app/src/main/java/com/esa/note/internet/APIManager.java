package com.esa.note.internet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.esa.note.database.DatabaseHelper;
import com.esa.note.objects.Entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class APIManager {

    public static int ERROR_NOT_CONNECTED = 0;

    private final HttpService httpService;

    public APIManager(HttpService httpService) {
        this.httpService = httpService;
    }

    public void getUpdates(Context context) {
        Call<List<PostEntityModel>> call = httpService.getUpdates();
        List<Integer> idList = new ArrayList<>();
        call.enqueue(new Callback<List<PostEntityModel>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<PostEntityModel>> call, Response<List<PostEntityModel>> response) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);

                if (response.body() != null) {
                    for (PostEntityModel postEntityModel : response.body()) {
                        Entity entity = new Entity(postEntityModel);
                        databaseHelper.updateEntity(entity);
                        idList.add(postEntityModel.getId());
                    }

                }
                databaseHelper.close();

                Call<Boolean> call1 = httpService.deleteUpdates(idList);
                call1.enqueue(new Callback<Boolean>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Log.d("hey",response.body()+"");
                    }
                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<PostEntityModel>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

    }

    public void updateEntity(Entity entity, Callback<Boolean> callback) {
            Call<Boolean> call = httpService.updateEntity(new PostEntityModel(entity));
            call.enqueue(callback);
    }

    public void updateEntity(Entity entity, Context context) {
        Call<Boolean> call = httpService.updateEntity(new PostEntityModel(entity));
        call.enqueue(new Callback<Boolean>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body() != null) {
                    if (response.body() == false) {
                        Toast.makeText(context, "이게 뭐노?", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteEntity( Entity entity, Callback<Boolean> callback) {
        Call<Boolean> call = httpService.deleteEntity( new PostEntityModel(entity));
        call.enqueue(callback);
    }
    public void deleteEntity(Entity entity, Context context) {
        Call<Boolean> call = httpService.deleteEntity(new PostEntityModel(entity));
        call.enqueue(new Callback<Boolean>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body() != null) {
                    if (response.body() == false) {
                        Toast.makeText(context, "이게 뭐노?", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void insertEntity(Entity entity, Callback<Boolean> callback) {
        Call<Boolean> call = httpService.insertEntity( new PostEntityModel(entity));
        call.enqueue(callback);
    }
    public void insertEntity(Entity entity, Context context) {
        Call<Boolean> call = httpService.insertEntity(new PostEntityModel(entity));
        call.enqueue(new Callback<Boolean>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body() != null) {
                    if (response.body() == false) {
                        Toast.makeText(context, "이게 뭐노?", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(LoginUserModel loginUserModel, Callback<Boolean> callback) {
        Call<Boolean> call = httpService.login(loginUserModel);
        call.enqueue(callback);
    }

    public void autoLogin( Callback<Boolean> callback) {
        Call<Boolean> call = httpService.autoLogin();
        call.enqueue(callback);
    }

    public void register(Context context, AccountPostModel accountPostModel, Callback<Boolean> callback) {
        File file = new File(context.getDatabasePath("Note.db").getAbsolutePath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),  file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<Boolean> call = httpService.register(accountPostModel, filePart);
        call.enqueue(callback);
    }

//    public void openSession(Callback<Boolean> callback) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),  file);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//        Call<Boolean> call = httpService.register(accountPostModel, filePart);
//        call.enqueue(callback);
//    }
}
