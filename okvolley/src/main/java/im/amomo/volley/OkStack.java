package im.amomo.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by GoogolMo on 6/4/14.
 */
public interface OkStack {
    Response performRequest(Request<?> request, Map<String, String> map) throws IOException, AuthFailureError;
}
