package im.amomo.volley.sample.retrofitrequest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import im.amomo.volley.sample.model.PersonalDes;
import im.amomo.volley.sample.model.PersonalDesDeserializer;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-10
 * Time: 13:16
 */
public class OkVolleyApiUtils {

    private static OkVolleyApiUtils mInstance;
    private OkVolleyApi okVolleyApi;
    private static final String HOST = "https://api.douban.com";

    //构造方法
    private OkVolleyApiUtils() {
        //在构造方法中我们要通过实例化RestAdapter拿到我们的 OkVolleyApi
        //注: setRequestInterceptor()在这里是为了在请求头中加入设备信息, 方便我们后台的调试
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(PersonalDes.class, new PersonalDesDeserializer())
                .create();

        RestAdapter restAdapter =
                new RestAdapter.Builder()
                        .setConverter(new GsonConverter(gson))
                        .setRequestInterceptor(defaultInterceptor)
                        .setEndpoint(HOST)
                        .build();
        okVolleyApi = restAdapter.create(OkVolleyApi.class);


/*
        //get请求的缓存
        File httpCacheDirectory = new File(context.getCacheDir().getAbsolutePath(), "HttpCache");

        HttpResponseCache httpResponseCache = null;
        try {
            httpResponseCache = new HttpResponseCache(httpCacheDirectory, 10 * 1024);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Could not create http cache", e);
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setResponseCache(httpResponseCache);
        builder.setClient(new OkClient(okHttpClient));
*/

    }

    //单例
    public static OkVolleyApiUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkVolleyApiUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkVolleyApiUtils();
                }
            }
        }
        return mInstance;
    }

    public OkVolleyApi getOkVolleyApi() {
        return okVolleyApi;
    }

    //在这里我们还定义了一个RequestInterceptor, 作用是在请求头中拼入一些信息方便我们后台的调试
    //否则请求头中就只会出现okhttp 2.2.0的字样(Retrofit默认是直接使用OkhttpClient的)
    RequestInterceptor defaultInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("User-Agent", "some code here");
        }
    };

}
