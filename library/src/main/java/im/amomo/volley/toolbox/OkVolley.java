package im.amomo.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
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
    private static OkHttpStack okHttpStack;

    public static final int DEFAULT_BITMAP_CACHE_PERCENT = 30;

    private static final String VERSION = "OkVolley/1.0";

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";

    private static OkVolley Instance;
    private String mUserAgent;

    private Map<String, String> mRequestHeaders;

    private Context mContext;

    private int mBitmapCachePercent = DEFAULT_BITMAP_CACHE_PERCENT;

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
     * set imageload bitmap cache percent
     *
     * @param percent <100 >0
     * @return this Volley Object
     */
    public OkVolley setBitmapCachePercent(int percent) {
        if (percent > 80) {
            percent = 80;
        } else if (percent < 20) {
            percent = 20;
        }
        this.mBitmapCachePercent = percent;
        return this;
    }

    private im.amomo.volley.OkImageLoader mImageLoader;

    /**
     * get the imageloader
     *
     * @return imageloader
     */
    public im.amomo.volley.OkImageLoader getImageLoader() {
        if (mImageLoader == null) {

            Network network = new OkNetwork(getDefaultHttpStack());

            File cache = mContext.getExternalCacheDir();
            if (cache == null) {
                cache = mContext.getCacheDir();
            }
            File cacheDir = new File(cache, DEFAULT_CACHE_DIR);
            Cache diskBasedCache = new DiskBasedCache(cacheDir);

            RequestQueue imageQueue = new RequestQueue(diskBasedCache, network);

            im.amomo.volley.BitmapLruCache bitmapLruCache = new im.amomo.volley.BitmapLruCache(im.amomo.volley.BitmapLruCache
                    .getMemorySize(mBitmapCachePercent));
            mImageLoader = new im.amomo.volley.OkImageLoader(imageQueue, bitmapLruCache);
            mImageLoader.setBatchedResponseDelay(0);

            imageQueue.start();
        }
        return mImageLoader;
    }

    /**
     * 生成User-Agent
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
        okHttpStack.setHostnameVerifier(verifier);
        return this;
    }

    /**
     * trust all certs
     *
     * @return this Volley Object
     */
    public OkVolley trustAllCerts() {
        okHttpStack.trustAllCerts();
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
        if (okHttpStack == null) {
            okHttpStack = new OkHttpStack();
        }
        return okHttpStack;
    }
}
