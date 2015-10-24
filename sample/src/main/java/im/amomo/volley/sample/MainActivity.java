package im.amomo.volley.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import im.amomo.volley.GsonGetRequest;
import im.amomo.volley.OkRequest;
import im.amomo.volley.sample.model.PersonalDes;
import im.amomo.volley.sample.okvolleyrequest.BaseRequest;
import im.amomo.volley.sample.retrofitrequest.ApiRequest;
import im.amomo.volley.sample.retrofitrequest.OkVolleyApiUtils;
import im.amomo.volley.toolbox.OkVolley;
import retrofit.Callback;
import retrofit.RetrofitError;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-10
 * Time: 10:42
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        Button btGetProfile = (Button) findViewById(R.id.bt_get_profile);
        tvProfile = (TextView) findViewById(R.id.tv_profile);

        btGetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load();
                //getPersonName();
                //getPersonNameWithRetrofit();
                getPersonNameWithRetrofitRxJava();
            }
        });
    }

    private void getPersonNameWithRetrofitRxJava() {
        OkVolleyApiUtils
                .getInstance()
                .getOkVolleyApi()
                .getPersonDes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<PersonalDes>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(PersonalDes personalDes) {
                                Log.d("PersonalDes", personalDes.toString());
                                Toast.makeText(MainActivity.this, personalDes.getName(), Toast.LENGTH_SHORT).show();
                                tvProfile.setText(personalDes.toString());
                            }
                        }
                );
    }

    private void getPersonNameWithRetrofitCallback() {
        OkVolleyApiUtils.getInstance().getOkVolleyApi().getPersonDes(new Callback<PersonalDes>() {

            @Override
            public void success(PersonalDes personalDes, retrofit.client.Response response) {
                Log.d("PersonalDes", personalDes.toString());
                Toast.makeText(MainActivity.this, personalDes.getName(), Toast.LENGTH_SHORT).show();
                tvProfile.setText(personalDes.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPersonName() {
        final GsonGetRequest gsonGetRequest =
                ApiRequest.personalDesGsonGetRequest(
                        new Response.Listener<PersonalDes>() {
                            @Override
                            public void onResponse(PersonalDes personalDes) {
                                Toast.makeText(MainActivity.this, personalDes.getName(), Toast.LENGTH_SHORT).show();
                                tvProfile.setText(personalDes.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        gsonGetRequest.setTag("request");
        OkVolley.getInstance().getRequestQueue().add(gsonGetRequest);
    }

    private void load() {
        OkRequest request = new BaseRequest(Request.Method.GET, "https://api.douban.com/v2/user/ailurus",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Response", jsonObject.toString());
                        Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, jsonObject.optString("name"), Toast.LENGTH_SHORT).show();
                        tvProfile.setText(jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request.setTag("request");
        OkVolley.getInstance().getRequestQueue().add(request);
    }

    @Override
    public void onDestroy() {
        OkVolley.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return request.getTag() != null && request.getTag().equals("request");
            }
        });
        super.onDestroy();
    }
}
