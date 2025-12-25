package com.esa.note.internet;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface HttpService {

    @Multipart
    @POST("user/file")
    Call<Boolean> callUploadApi(@Part MultipartBody.Part song);

    @Headers("Content-Type: application/json")
    @Multipart
    @POST("user/register")
    Call<Boolean> register(@Body AccountPostModel accountPostModel,  @Part MultipartBody.Part database);

    @POST("login")
    Call<Boolean> login(@Body LoginUserModel loginUserModel);

    @GET("user/autoLogin")
    Call<Boolean> autoLogin();

    @Headers("Content-Type: application/json")
    @PUT("entity/update/android")
    Call<Boolean> updateEntity(@Body PostEntityModel postEntityModel);

    @Headers("Content-Type: application/json")
    @DELETE("entity/delete/android")
    Call<Boolean> deleteEntity(@Body PostEntityModel postEntityModel);

    @Headers("Content-Type: application/json")
    @POST("entity/insert/android")
    Call<Boolean> insertEntity(@Body PostEntityModel postEntityModel);

    @Headers("Content-Type: application/json")
    @GET("entity/getUpdates/android")
    Call<List<PostEntityModel>> getUpdates();

    @Headers("Content-Type: application/json")
    @PUT("entity/deleteNoteTasks/android")
    Call<Boolean> deleteUpdates(@Body List<Integer> list);

    @Headers("Content-Type: application/json")
    @POST("openSession")
    Call<Boolean> openSession();

}
