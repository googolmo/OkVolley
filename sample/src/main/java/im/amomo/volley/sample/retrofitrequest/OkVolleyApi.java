package im.amomo.volley.sample.retrofitrequest;

import im.amomo.volley.sample.model.PersonalDes;
import retrofit.Callback;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-10
 * Time: 13:10
 */
public interface OkVolleyApi {

    @GET("/v2/user/ailurus")
    void getPersonDes(Callback<PersonalDes> personalDesCallback);

    @GET("/v2/user/ailurus")
    Observable<PersonalDes> getPersonDes();
}
