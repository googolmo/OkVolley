package im.amomo.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.VolleyLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by GoogolMo on 10/22/13.
 */
public class OkHttpStack implements OkStack {

    private final OkHttpClient mClient;

    private final UrlRewriter mUrlRewriter;

    /**
     * An interface for transforming URLs before use.
     */
    public interface UrlRewriter {
        /**
         * Returns a URL to use instead of the provided one, or null to indicate
         * this URL should not be used at all.
         */
        public String rewriteUrl(String originalUrl);
    }


    public OkHttpStack(OkHttpClient client) {
        this(null, client);
    }

    public OkHttpStack(UrlRewriter urlRewriter, OkHttpClient client) {
        this.mUrlRewriter = urlRewriter;
        this.mClient = client;
    }

    /**
     * perform the request
     *
     * @param request           request
     * @param additionalHeaders headers
     * @return http response
     * @throws java.io.IOException
     * @throws com.android.volley.AuthFailureError
     */
    @Override
    public Response performRequest(Request<?> request,
                                   Map<String, String> additionalHeaders) throws IOException, AuthFailureError {


        String url = request.getUrl();
        HashMap<String, String> map = new HashMap<String, String>();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        if (mUrlRewriter != null) {
            String rewritten = mUrlRewriter.rewriteUrl(url);
            if (rewritten == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = rewritten;
        }

        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(url);

        for (String headerName : map.keySet()) {
            builder.header(headerName, map.get(headerName));
//            connection.addRequestProperty(headerName, map.get(headerName));
            if (VolleyLog.DEBUG) {
                // print header message
                VolleyLog.d("RequestHeader: %1$s:%2$s", headerName, map.get(headerName));
            }
        }
        setConnectionParametersForRequest(builder, request);
        // Initialize HttpResponse with data from the okhttp.
        Response okHttpResponse = mClient.newCall(builder.build()).execute();

        int responseCode = okHttpResponse.code();
        if (responseCode == -1) {
            // -1 is returned by getResponseCode() if the response code could not be retrieved.
            // Signal to the caller that something was wrong with the connection.
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        return okHttpResponse;
    }

    /* package */
    static void setConnectionParametersForRequest(okhttp3.Request.Builder builder,
                                                  Request<?> request) throws IOException, AuthFailureError {

        byte[] postBody = null;
        if (VolleyLog.DEBUG) {
            VolleyLog.d("request.method = %1$s", request.getMethod());
        }
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST:
                // This is the deprecated way that needs to be handled for backwards compatibility.
                // If the request's post body is null, then the assumption is that the request is
                // GET.  Otherwise, it is assumed that the request is a POST.
                postBody = request.getBody();
                if (postBody != null) {
                    // Prepare output. There is no need to set Content-Length explicitly,
                    // since this is handled by HttpURLConnection using the size of the prepared
                    // output stream.
                    builder.post(RequestBody
                            .create(MediaType.parse(request.getBodyContentType()), postBody));
                    if (VolleyLog.DEBUG) {
                        VolleyLog.d("RequestHeader: %1$s:%2$s", OkRequest.HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    }
                } else {
                    builder.get();
                }
                break;
            case Method.GET:
                // Not necessary to set the request method because connection defaults to GET but
                // being explicit here.
                builder.get();
                break;
            case Method.DELETE:
                builder.delete();
                break;
            case Method.POST:
                postBody = request.getBody();
                if (postBody == null) {
                    builder.post(RequestBody.create(MediaType.parse(request.getBodyContentType()), ""));
                } else {
                    builder.post(RequestBody.create(MediaType.parse(request.getBodyContentType()), postBody));
                }
                if (VolleyLog.DEBUG) {
                    VolleyLog.d("RequestHeader: %1$s:%2$s", OkRequest.HEADER_CONTENT_TYPE, request.getBodyContentType());
                }
                break;
            case Method.PUT:
                postBody = request.getBody();
                if (postBody == null) {
                    builder.put(RequestBody.create(MediaType.parse(request.getBodyContentType()), ""));
                } else {
                    builder.put(RequestBody.create(MediaType.parse(request.getBodyContentType()), postBody));
                }
                if (VolleyLog.DEBUG) {
                    VolleyLog.d("RequestHeader: %1$s:%2$s", OkRequest.HEADER_CONTENT_TYPE, request.getBodyContentType());
                }
                break;
            case Method.HEAD:
                builder.head();
                break;
            case Method.PATCH:
                postBody = request.getBody();
                if (postBody == null) {
                    builder.patch(RequestBody.create(MediaType.parse(request.getBodyContentType()), ""));
                } else {
                    builder.patch(RequestBody.create(MediaType.parse(request.getBodyContentType()), postBody));
                }
                if (VolleyLog.DEBUG) {
                    VolleyLog.d("RequestHeader: %1$s:%2$s", OkRequest.HEADER_CONTENT_TYPE, request.getBodyContentType());
                }
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }


    }

//    /**
//     * set request trust all certs include untrusts
//     *
//     * @return this http stact
//     */
//    public OkHttpStack trustAllCerts() {
//        this.mClient.setSslSocketFactory(getTrustedFactory());
//        return this;
//    }
//
//    /**
//     * set request trust all hosts include hosts with untrusts
//     *
//     * @return
//     */
//    public OkHttpStack trustAllHosts() {
//        this.mClient.setHostnameVerifier(getTrustedVerifier());
//        return this;
//    }
//
//    /**
//     * set custom host name verifier
//     *
//     * @param verifier verifier
//     * @return this http stack
//     */
//    public OkHttpStack setHostnameVerifier(HostnameVerifier verifier) {
//        this.mClient.setHostnameVerifier(verifier);
//        return this;
//    }
//
//    private static SSLSocketFactory TRUSTED_FACTORY;
//    private static HostnameVerifier TRUSTED_VERIFIER;
//
//    private static SSLSocketFactory getTrustedFactory() {
//        if (TRUSTED_FACTORY == null) {
//            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                public void checkClientTrusted(X509Certificate[] chain, String authType) {
//                    // Intentionally left blank
//                }
//
//                public void checkServerTrusted(X509Certificate[] chain, String authType) {
//                    // Intentionally left blank
//                }
//            }};
//            try {
//                SSLContext context = SSLContext.getInstance("TLS");
//                context.init(null, trustAllCerts, new SecureRandom());
//                TRUSTED_FACTORY = context.getSocketFactory();
//            } catch (GeneralSecurityException e) {
//                IOException ioException = new IOException(
//                        "Security exception configuring SSL context");
//                ioException.initCause(e);
//            }
//        }
//        return TRUSTED_FACTORY;
//    }
//
//    private static HostnameVerifier getTrustedVerifier() {
//        if (TRUSTED_VERIFIER == null)
//            TRUSTED_VERIFIER = new HostnameVerifier() {
//
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            };
//
//        return TRUSTED_VERIFIER;
//    }


}
