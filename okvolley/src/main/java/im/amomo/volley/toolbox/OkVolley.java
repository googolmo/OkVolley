package im.amomo.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import im.amomo.volley.OkHttpStack;
import im.amomo.volley.OkNetwork;
import im.amomo.volley.OkRequest;
import okhttp3.OkHttpClient;

/**
 * Created by GoogolMo on 10/22/13.
 */
public class OkVolley {

    private RequestQueue mRequestQueue;
//    private static Network InstanceNetwork;
//    private static OkHttpStack OkHttpStack;

    private static final String VERSION = "OkVolley/1.0";
    /** Number of network request dispatcher threads to start. */
    private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";

    private static OkVolley sInstance;

    private Map<String, String> mRequestHeaders;
    private OkHttpClient mHttpClient;
    private Context mContext;
    private Cache mCache;
    private int mThreadPoolSize;


    private OkVolley(Context context, Map<String, String> headers, OkHttpClient client, Cache cache
            , int threadPoolSize) {
        this.mContext = context;
        this.mRequestHeaders = headers;
        this.mHttpClient = client;
        this.mCache = cache;
        this.mThreadPoolSize = threadPoolSize;
    }

    public static OkVolley getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("Please call initByBuilder(Builder) first!");
        }
        return sInstance;
    }

    public synchronized static void initByBuilder(Builder builder) {
        if (sInstance == null) {
            sInstance = builder.build();
        }
    }

    public static class Builder {

        private Context context;

        private final Map<String, String> defaultHeaders;
        private String defaultUserAgent;
        private String defaultCharset;
        private OkHttpClient defaultHttpClient;
        private String defaultCacheDir;
        private int defaultThreadPoolSize = DEFAULT_NETWORK_THREAD_POOL_SIZE;

        public Builder(Context context) {
            this.context = context;
            defaultHeaders = new HashMap<>();
        }

        public Builder headers(Map<String, String> headers) {
            defaultHeaders.putAll(headers);
            return this;
        }

        public Builder header(String key, String value) {
            defaultHeaders.put(key, value);
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.defaultUserAgent = userAgent;
            return this;
        }

        public Builder charset(String charset) {
            this.defaultCharset = charset;
            return this;
        }

        public Builder httpClient(OkHttpClient client) {
            this.defaultHttpClient = client;
            return this;
        }

        public Builder cacheDir(String cacheDir) {
            this.defaultCacheDir = cacheDir;
            return this;
        }

        public Builder threadPoolSize(int size) {
            this.defaultThreadPoolSize = size;
            return this;
        }

        public OkVolley build() {
            if (TextUtils.isEmpty(defaultUserAgent)) {
                defaultUserAgent = generateUserAgent(context);
            }
            defaultHeaders.put(OkRequest.HEADER_USER_AGENT, defaultUserAgent);
            defaultHeaders.put(OkRequest.HEADER_ACCEPT_CHARSET, TextUtils.isEmpty(defaultUserAgent)
                    ? OkRequest.CHARSET_UTF8: defaultCharset);
            if (defaultHttpClient == null) {
                defaultHttpClient = new OkHttpClient.Builder()
                        .build();
            }

            if (TextUtils.isEmpty(defaultCacheDir)) {
                defaultCacheDir = DEFAULT_CACHE_DIR;
            }
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir == null) {
                cacheDir = context.getCacheDir();
            }
            cacheDir = new File(cacheDir, defaultCacheDir);
            Cache cache = new DiskBasedCache(cacheDir);

            return new OkVolley(context, defaultHeaders, defaultHttpClient, cache, defaultThreadPoolSize);
        }
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
        if (mRequestQueue == null) {
            mRequestQueue = newRequestQueue();
            mRequestQueue.start();
        }
        return mRequestQueue;
    }


    private RequestQueue newRequestQueue() {

        OkHttpStack stack = new OkHttpStack(mHttpClient);
        OkNetwork network = new OkNetwork(stack);

        RequestQueue queue = new RequestQueue(mCache, network, mThreadPoolSize);

        return queue;
    }
}
