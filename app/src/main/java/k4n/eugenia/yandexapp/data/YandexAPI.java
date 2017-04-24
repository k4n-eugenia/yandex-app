package k4n.eugenia.yandexapp.data;

import k4n.eugenia.yandexapp.BuildConfig;
import k4n.eugenia.yandexapp.data.models.Languages;
import k4n.eugenia.yandexapp.data.models.TranslateResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Eugenia Kan on 16.04.2017.
 */

public interface YandexAPI {
    String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";

    @GET("getLangs?key=" + BuildConfig.YANDEX_API_KEY)
    Call<Languages> getLanguages(@Query("ui") String ui);

    @POST("translate?key=" + BuildConfig.YANDEX_API_KEY)
    @FormUrlEncoded
    Call<TranslateResult> translate(@Query("lang") String lang, @Field("text") String text);
}
