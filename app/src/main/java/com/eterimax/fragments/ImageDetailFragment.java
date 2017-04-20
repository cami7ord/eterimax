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
import com.eterimax.utilities.Utilities;
import com.google.gson.Gson;

/**
 * A fragment representing a single Image detail screen.
 * This fragment is either contained in a {@link ImageListActivity}
 * in two-pane mode (on tablets) or a {@link ImageDetailActivity}
 * on handsets.
 */
public class ImageDetailFragment extends Fragment {

    private Image mItem;

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

        //((TextView) rootView.findViewById(R.id.user_name)).setText(mItem.getOwnerName());

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            String buddyIcon = "https://flickr.com/buddyicons/" + mItem.getOwnerId() + ".jpg";
            ((NetworkImageView) rootView.findViewById(R.id.buddy_icon)).
                    setImageUrl(buddyIcon, MyVolley.getInstance(getActivity()).getImageLoader());
            ((TextView) rootView.findViewById(R.id.user_name)).setText(mItem.getOwnerName());
            ((TextView) rootView.findViewById(R.id.image_title)).setText(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.image_date)).setText(Utilities.simpleServerDayFormat(mItem.getDate()));
            ((TextView) rootView.findViewById(R.id.image_description)).setText(mItem.getDescription());
        }

        return rootView;
    }
}
