package k4n.eugenia.yandexapp;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import k4n.eugenia.yandexapp.data.YandexAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Eugenia Kan on 16.04.2017.
 */

public class App extends Application {
    private static YandexAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        configureApi();
    }

    /**
     * Создание и настройка класса для работы с Yandex API
     */
    private void configureApi() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YandexAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(YandexAPI.class);
    }

    public static YandexAPI getApi() {
        return api;
    }
}
