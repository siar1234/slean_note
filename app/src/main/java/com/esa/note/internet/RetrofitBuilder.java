package com.esa.note.internet;

import android.util.Log;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.annotations.NonNull;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class RetrofitBuilder {

    private static Retrofit retrofit = null;
    private static final List<Cookie> cookieList = new ArrayList<>();

//    public static Retrofit getClient() {
//        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .readTimeout(60 , TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        Gson gson = new GsonBuilder().setLenient().create();
//
//        CookieJar cookieJar = new CookieJar() {
//
//            final List<Cookie> cookieList = new ArrayList<>();
//            @EverythingIsNonNull
//            @Override
//            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                cookieList.clear();
//                cookieList.addAll(cookies);
//            }
//
//            @EverythingIsNonNull
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl url) {
//
//                return cookieList;
//            }
//        };
//
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl("http://192.168.35.150:5000/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(getUnsafeOkHttpClient().cookieJar(cookieJar).build())
//                    .build();
//        }
//        return retrofit;
//    }

    public static Retrofit getClient(String url) {
        CookieJar cookieJar = new CookieJar() {

            @EverythingIsNonNull
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieList.clear();
                cookieList.addAll(cookies);
//                Log.d("work!!", cookies.get(0).expiresAt()+"");
//                Log.d("work!!", cookies.get(0).domain()+"");
//                Log.d("work!!", cookies.get(0).path()+"");
//                Log.d("work!!", cookies.get(0).name()+"");
//                Log.d("work!!", cookies.get(0).value()+"");
//                Log.d("work!!", cookies.get(0).httpOnly()+"");
//                Log.d("work!!", cookies.get(0).secure()+"");
//                Log.d("work!!", cookies+"");
//                Log.d("work!!?", cookieList+"");
            }

            @NonNull
            @EverythingIsNonNull
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {

//                Cookie cookie= new Cookie.Builder()
//                        .domain("localhost")
//                        .path("/")
//                        .name(string)
//                        .value("fffffffffffffffff")
//                        .httpOnly()
//                        .secure()
//                        .build();
//                cookieList.add(cookie);
                Log.d("work!!?!!", cookieList+", "+ url);
                return cookieList;
            }
//        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .readTimeout(60 , TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//        OkHttpClient client = new OkHttpClient.Builder()
//                .followSslRedirects(true)
//                .followRedirects(true)
//                .build();
//        Gson gson = new GsonBuilder().setLenient().create();


        };
       // if (retrofit == null) {
            Log.d("hhhh", url);
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                   .addConverterFactory(GsonConverterFactory.create())
                    .client(getUnsafeOkHttpClient().cookieJar(cookieJar).build())
                    .build();
      //  }
        return retrofit;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)  {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.followSslRedirects(true);
            builder.followRedirects(true);

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
