package im.amomo.volley.sample.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import im.amomo.volley.OkRequest;
import im.amomo.volley.sample.BaseRequest;
import im.amomo.volley.sample.R;
import im.amomo.volley.toolbox.OkVolley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by GoogolMo on 12/31/13.
 */
public class RequestFragment extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.title, new String[]{
                "Request GoogolMo's Profile", "post data to douban", "post"}));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == 0) {
            load();
        } else if (position == 1) {
            try {
                postData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (position == 2) {
            post();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListShown(true);
    }

    private void load() {

        OkRequest request = new BaseRequest(Request.Method.GET, "https://api.douban.com/v2/user/googolmo",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), jsonObject.optString("name"), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request.setTag("request");
        OkVolley.getInstance().getRequestQueue().add(request);

    }

    private void post() {

        BaseRequest request = new BaseRequest(Request.Method.POST, "http://10.0.2.24:5000/test1",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError) {
                            Toast.makeText(getActivity(),
                                    new String(((ServerError) error).networkResponse.data, Charset.defaultCharset()), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        request.form("text", "test " + SystemClock.elapsedRealtime());
        request.setTag("request");
        OkVolley.getInstance().getRequestQueue().add(request);
    }

    private void postData() throws IOException {

        AssetManager assetManager = getActivity().getAssets();
        InputStream in = assetManager.open("26391.jpg");

        BaseRequest request = new BaseRequest(Request.Method.POST, "http://10.0.2.24:5000/",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError) {
                            Toast.makeText(getActivity(),
                                    new String(((ServerError) error).networkResponse.data, Charset.defaultCharset()), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        request.part("text", "test " + SystemClock.elapsedRealtime());
        request.part("image", "image.jpeg", "image/jpeg", in);
        request.header(OkRequest.HEADER_AUTHORIZATION, String.format("Bearer %1$s", "1e32ab95edd560b45a9802c1c980221c"));
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
