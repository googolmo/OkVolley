package im.amomo.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.volley.VolleyError;

/**
 * Created by GoogolMo on 11/6/13.
 */
public class OkNetworkImageView extends ImageView {
    /**
     * The URL of the network image to load
     */
    private String mUrl;
    /**
     * Resource ID of the image to be used as a placeholder until the network image is loaded.
     */
    private int mDefaultImageId;

    /**
     * Resource ID of the image to be used if the network response fails.
     */
    private int mErrorImageId;

    /**
     * Local copy of the ImageLoader.
     */
    private im.amomo.volley.OkImageLoader mImageLoader;

    private boolean mNeedLoadImage = true;

    /**
     * Current ImageContainer. (either in-flight or finished)
     */
    private im.amomo.volley.OkImageLoader.ImageContainer mImageContainer;

    private OnImageLoadListener mOnImageLoadListener;


    /**
     * {@inheritDoc}
     */
    public OkNetworkImageView(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    public OkNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     */
    public OkNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置展示URL
     *
     * @param url         图片URL
     * @param imageLoader 图片展示loader
     * @param listener    监听器
     */
    public void setImageUrl(String url, im.amomo.volley.OkImageLoader imageLoader, OnImageLoadListener listener) {
        this.setImageUrl(url, imageLoader, listener, new SimpleBitmapDisplayer());
    }


    /**
     * 设置展示URL
     *
     * @param url             图片URL
     * @param imageLoader     图片展示loader
     * @param listener        监听器
     * @param bitmapDisplayer 图片展示
     */
    public void setImageUrl(String url, im.amomo.volley.OkImageLoader imageLoader, OnImageLoadListener listener,
                            BitmapDisplayer bitmapDisplayer) {
        mNeedLoadImage = true;
        mOnImageLoadListener = listener;
        mUrl = url;
        mImageLoader = imageLoader;
        mBitmapDisplayer = bitmapDisplayer;
        // The URL has potentially changed. See if we need to load it.
        if (mOnImageLoadListener != null) {
            mOnImageLoadListener.preLoad(this);
        }
        loadImageIfNecessary(false);
    }

    /**
     * 设置展示URL
     *
     * @param url         图片URL
     * @param imageLoader 图片展示loader
     */
    public void setImageUrl(String url, im.amomo.volley.OkImageLoader imageLoader) {
        this.setImageUrl(url, imageLoader, null);
    }

    public interface OnImageLoadListener {
        public void preLoad(ImageView imageView);

        public Bitmap onLoad(Bitmap bitmap, ImageView imageView);

        public void loadComplete(Bitmap bitmap, ImageView imageView);

        public void loadFailed(VolleyError error, ImageView imageView);
    }

    /**
     * 设置图片展示监听
     *
     * @param listener 监听器
     */
    public void setOnImageLoadListener(OnImageLoadListener listener) {
        this.mOnImageLoadListener = listener;
    }

    private BitmapDisplayer mBitmapDisplayer;

    /**
     * 设置展示器
     *
     * @param bitmapDisplayer
     */
    public void setBitmapDisplayer(BitmapDisplayer bitmapDisplayer) {
        this.mBitmapDisplayer = bitmapDisplayer;
    }

    /**
     * Sets the default image resource ID to be used for this view until the attempt to load it
     * completes.
     */
    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }

    /**
     * Sets the error image resource ID to be used for this view in the event that the image
     * requested fails to load.
     */
    public void setErrorImageResId(int errorImage) {
        mErrorImageId = errorImage;
    }

    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        int width = getWidth();
        int height = getHeight();

        boolean isFullyWrapContent = getLayoutParams() != null
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
        // if the view's bounds aren't known yet, and this is not a wrap-content/wrap-content
        // view, hold off on loading the image.
        if (width == 0 && height == 0 && !isFullyWrapContent) {
            return;
        }

        // if the URL to be loaded in this view is empty, cancel any old requests and clear the
        // currently loaded image.
        if (TextUtils.isEmpty(mUrl)) {
            if (mImageContainer != null) {
                mImageContainer.cancelRequest();
                mImageContainer = null;
            }
            setImageBitmap(null);
            return;
        }

        // if there was an old request in this view, check if it needs to be canceled.
        if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
            if (mImageContainer.getRequestUrl().equals(mUrl)) {
                // if the request is from the same URL, return.
                return;
            } else {
                // if there is a pre-existing request, cancel it if it's fetching a different URL.
                mImageContainer.cancelRequest();
                setImageBitmap(null);
            }
        }

        // The pre-existing content of this view didn't match the current URL. Load the new image
        // from the network.
        im.amomo.volley.OkImageLoader.ImageContainer newContainer = mImageLoader.get(mUrl,
                new im.amomo.volley.OkImageLoader.ImageListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mOnImageLoadListener != null) {
                            mOnImageLoadListener.loadFailed(error, OkNetworkImageView.this);
                        }
                        if (mErrorImageId != 0) {
                            if (getContext() != null) {
                                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), mDefaultImageId);
                                if (mBitmapDisplayer != null) {
                                    mBitmapDisplayer.display(bitmap, OkNetworkImageView.this);
                                } else {
                                    setImageBitmap(bitmap);
                                }
                            }

                        }
                    }

                    @Override
                    public void onResponse(final im.amomo.volley.OkImageLoader.ImageContainer response, boolean isImmediate) {
                        // If this was an immediate response that was delivered inside of a layout
                        // pass do not set the image immediately as it will trigger a requestLayout
                        // inside of a layout. Instead, defer setting the image by posting back to
                        // the main thread.
                        if (isImmediate && isInLayoutPass) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    onResponse(response, false);
                                }
                            });
                            return;
                        }

                        if (response.getBitmap() != null) {
                            Bitmap bitmap = null;
                            if (mOnImageLoadListener != null) {
                                bitmap = mOnImageLoadListener.onLoad(response.getBitmap(), OkNetworkImageView.this);
                            }
                            if (bitmap == null) {
                                bitmap = response.getBitmap();
                            }
                            if (mBitmapDisplayer != null) {
                                mBitmapDisplayer.display(bitmap, OkNetworkImageView.this);
                            } else {
                                setImageBitmap(bitmap);
                            }

                            if (mOnImageLoadListener != null) {
                                mOnImageLoadListener.loadComplete(bitmap, OkNetworkImageView.this);
                            }
                        } else if (mDefaultImageId != 0) {
                            if (getContext() != null) {
                                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), mDefaultImageId);
                                if (mBitmapDisplayer != null) {
                                    mBitmapDisplayer.display(bitmap, OkNetworkImageView.this);
                                } else {
                                    setImageBitmap(bitmap);
                                }
                            }

                        }
                    }
                });

        // update the ImageContainer to be the new bitmap container.
        mImageContainer = newContainer;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mNeedLoadImage) {
            loadImageIfNecessary(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mImageContainer != null) {
            // If the view was bound to an image request, cancel it and clear
            // out the image from the view.
            mImageContainer.cancelRequest();
            setImageBitmap(null);
            // also clear out the container so we can reload the image if necessary.
            mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        mNeedLoadImage = false;
        super.setImageResource(resId);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mNeedLoadImage = false;
        super.setImageBitmap(bm);
    }

    /**
     * @param bm        bitmap
     * @param displayer 图片显示器
     */
    public void setImageBitmap(Bitmap bm, BitmapDisplayer displayer) {
        mNeedLoadImage = false;
        displayer.display(bm, this);
    }

    public class SimpleBitmapDisplayer implements BitmapDisplayer {

        @Override
        public void display(Bitmap bitmap, ImageView imageView) {
            imageView.setImageBitmap(bitmap);
        }
    }

}
