package im.amomo.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.Volley;
import im.amomo.volley.OkHttpStack;
import im.amomo.volley.OkNetwork;
import im.amomo.volley.OkRequest;
import im.amomo.volley.OkStack;

import javax.net.ssl.HostnameVerifier;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GoogolMo on 10/22/13.
 */
public class OkVolley extends Volley {

    private static RequestQueue InstanceRequestQueue;
    private static Cache InstanceCache;
    private static Network InstanceNetwork;
    private static OkHttpStack OkHttpStack;

    private static final String VERSION = "OkVolley/1.0";

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";

    private static OkVolley Instance;
    private String mUserAgent;

    private Map<String, String> mRequestHeaders;

    private Context mContext;

    public static OkVolley getInstance() {
        if (Instance == null) {
            Instance = new OkVolley();
        }
        return Instance;
    }

    public OkVolley() {
    }

    /**
     * init method
     *
     * @param context Context
     * @return this Volley Object
     */
    public OkVolley init(Context context) {
        this.mContext = context;
        InstanceRequestQueue = newRequestQueue(context);
        mUserAgent = generateUserAgent(context);
        mRequestHeaders = new HashMap<String, String>();
        mRequestHeaders.put(OkRequest.HEADER_USER_AGENT, mUserAgent);
        mRequestHeaders.put(OkRequest.HEADER_ACCEPT_CHARSET, OkRequest.CHARSET_UTF8);
        return this;
    }

    /**
     * set default all user-agent
     *
     * @param userAgent user-agent
     * @return this Volley Object
     */
    public OkVolley setUserAgent(String userAgent) {
        this.mUserAgent = userAgent;
        return this;
    }

    /**
     * build the default User-Agent
     *
     * @param context
     * @return
     */
    public static String generateUserAgent(Context context) {
        StringBuilder ua = new StringBuilder("api-client/");
        ua.append(VERSION);

        String packageName = context.getApplicationContext().getPackageName();

        ua.append(" ");
        ua.append(packageName);

        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pi != null) {
            ua.append("/");
            ua.append(pi.versionName);
            ua.append("(");
            ua.append(pi.versionCode);
            ua.append(")");
        }
        ua.append(" Android/");
        ua.append(Build.VERSION.SDK_INT);

        try {
            ua.append(" ");
            ua.append(Build.PRODUCT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ua.append(" ");
            ua.append(Build.MANUFACTURER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ua.append(" ");
            ua.append(Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ua.toString();
    }

    /**
     * set trusted verifier
     *
     * @param verifier HostnameVerifier
     * @return this Volley Object
     */
    public OkVolley setHostnameTrustedVerifier(HostnameVerifier verifier) {
        OkHttpStack.setHostnameVerifier(verifier);
        return this;
    }

    /**
     * trust all certs
     *
     * @return this Volley Object
     */
    public OkVolley trustAllCerts() {
        OkHttpStack.trustAllCerts();
        return this;
    }

    /**
     * get the default request headers
     *
     * @return the default request headers
     */
    public Map<String, String> getDefaultHeaders() {
        return this.mRequestHeaders;
    }

    /**
     * get the default request queue
     *
     * @return default {@link com.android.volley.RequestQueue}
     */
    public RequestQueue getRequestQueue() {
        if (InstanceRequestQueue == null) {
            InstanceRequestQueue = newRequestQueue(mContext);
        }
        return InstanceRequestQueue;
    }

    /**
     * getRquest queue static
     *
     * @param context
     * @return {@link com.android.volley.RequestQueue}
     */
    @Deprecated
    public static RequestQueue getRequestQueue(Context context) {
        if (InstanceRequestQueue == null) {
            InstanceRequestQueue = newRequestQueue(context);
        }
        return InstanceRequestQueue;
    }

    public static RequestQueue newRequestQueue(Context context) {

        if (InstanceNetwork == null) {
            InstanceNetwork = new OkNetwork(getDefaultHttpStack());
        }

        if (InstanceCache == null) {
            File cache = context.getExternalCacheDir();
            if (cache == null) {
                cache = context.getCacheDir();
            }
            File cacheDir = new File(cache, DEFAULT_CACHE_DIR);
            InstanceCache = new DiskBasedCache(cacheDir);
        }

        RequestQueue queue = new RequestQueue(InstanceCache, InstanceNetwork);
        queue.start();

        return queue;
    }

    protected static OkStack getDefaultHttpStack() {
        if (OkHttpStack == null) {
            OkHttpStack = new OkHttpStack();
        }
        return OkHttpStack;
    }
}
