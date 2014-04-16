package im.amomo.volley;

import android.graphics.Bitmap;

/**
 * Created by GoogolMo on 10/23/13.
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements OkImageLoader.ImageCache {

    public static final int DEFAULT_BITMAP_CACHE_PERCENT = 30;

    /**
     * 按比例获得堆栈中内存大小
     *
     * @param percent 比例
     * @return
     */
    public static int getMemorySize(int percent) {
        return (int) (Runtime.getRuntime().maxMemory() * (percent / 100f));
    }

    public BitmapLruCache() {
        super(getMemorySize(DEFAULT_BITMAP_CACHE_PERCENT));
    }

    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
