package im.amomo.volley.sample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import im.amomo.volley.OkNetworkImageView;
import im.amomo.volley.sample.R;
import im.amomo.volley.toolbox.OkVolley;

/**
 * Created by GoogolMo on 12/31/13.
 */
public class ImageFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OkNetworkImageView imageView = (OkNetworkImageView) view.findViewById(R.id.image);
        imageView.setImageUrl("http://img3.douban.com/img/celebrity/large/26391.jpg", OkVolley.getInstance().getImageLoader());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


}
