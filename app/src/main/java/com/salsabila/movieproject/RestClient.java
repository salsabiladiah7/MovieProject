package com.salsabila.movieproject;

import de.hdodenhof.circleimageview.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static MovieService service;

    public static MovieService getMovieService() {
        if (service == null) {
            String API_BASE_URL = "http://www.omdbapi.com";

//            Penambahan library logging untuk mempermudah debugging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            } else {
                loggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
            }

            OkHttpClient.Builder httpCli = new OkHttpClient.Builder();
            httpCli.addInterceptor(loggingInterceptor);

//            merubah url di interceptor karena API KEY berada di header.
//            sehingga url yang terdeteksi hanya www.omdbapi.com
            httpCli.addInterceptor(chain -> {
                Request request = chain.request();
                String newUrl = request.url().toString().replace("http://www.omdbapi.com", "http://www.omdbapi.com?apikey=2268147d&s=batman");
                return chain.proceed(request.newBuilder().url(newUrl).build());
            });

            Retrofit retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL).client(httpCli.build()).addConverterFactory(GsonConverterFactory.create()).build();

            service = retrofit.create(MovieService.class);
        }
        return service;
    }
}
