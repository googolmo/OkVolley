package im.amomo.volley.sample;

import android.app.Application;
import com.android.volley.VolleyLog;
import im.amomo.volley.toolbox.OkVolley;

/**
 * Created by GoogolMo on 12/31/13.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkVolley.getInstance().init(this)
                .setUserAgent(OkVolley.generateUserAgent(this))
                .trustAllCerts();

        VolleyLog.DEBUG = BuildConfig.DEBUG;
    }
}
