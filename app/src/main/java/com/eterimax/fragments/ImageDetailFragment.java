package com.eterimax.fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.eterimax.R;
import com.eterimax.activities.ImageDetailActivity;
import com.eterimax.activities.ImageListActivity;
import com.eterimax.pojos.Image;
import com.eterimax.singletons.MyVolley;
import com.google.gson.Gson;

/**
 * A fragment representing a single Image detail screen.
 * This fragment is either contained in a {@link ImageListActivity}
 * in two-pane mode (on tablets) or a {@link ImageDetailActivity}
 * on handsets.
 */
public class ImageDetailFragment extends Fragment {
    /**
     * The dummy content this fragment is presenting.
     */
    private Image mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ImageListActivity.ARG_ITEM)) {

            mItem = new Gson().fromJson(getArguments().getString(ImageListActivity.ARG_ITEM), Image.class);

            Activity activity = this.getActivity();

            NetworkImageView headerImage = (NetworkImageView) activity.findViewById(R.id.image);

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
                headerImage.setImageUrl(mItem.toString(), MyVolley.getInstance(activity).getImageLoader());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.image_detail)).setText(mItem.getImageId());
        }

        return rootView;
    }
}
