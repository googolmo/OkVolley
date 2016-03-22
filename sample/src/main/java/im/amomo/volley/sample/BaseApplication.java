package im.amomo.volley.sample;

import android.app.Application;
import com.android.volley.VolleyLog;
import im.amomo.volley.toolbox.OkVolley;
import im.amomo.volley.toolbox.OkVolley.Builder;

/**
 * Created by GoogolMo on 12/31/13.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        OkVolley.initByBuilder(new Builder(this));

        VolleyLog.DEBUG = BuildConfig.DEBUG;

    }


}
