package im.amomo.volley.sample.retrofitrequest;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import im.amomo.volley.GsonGetRequest;
import im.amomo.volley.sample.model.PersonalDes;
import im.amomo.volley.sample.model.PersonalDesDeserializer;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-09
 * Time: 14:22
 */
public class ApiRequest {

    public static GsonGetRequest<PersonalDes> personalDesGsonGetRequest(Response.Listener<PersonalDes> listener,
            Response.ErrorListener errorListener) {
        final String url = "https://api.douban.com/v2/user/ailurus";

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(PersonalDes.class, new PersonalDesDeserializer())
                .create();

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<PersonalDes>() {
                        }.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }
}
