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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.eterimax.R;
import com.eterimax.activities.ImageDetailActivity;
import com.eterimax.activities.ImageListActivity;
import com.eterimax.pojos.Image;
import com.eterimax.singletons.MyVolley;
import com.eterimax.utilities.Utilities;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
            ((TextView) rootView.findViewById(R.id.image_description)).setText(mItem.getDescription());

            downloadUserLocation((TextView) rootView.findViewById(R.id.user_location));
        }

        return rootView;
    }

    private void downloadUserLocation(final TextView v) {
        String url = "https://api.flickr.com/services/rest/?method=flickr.people.getInfo" +
                "&api_key=f08c2e99273a9d8c85ffe004223cfb4f&format=json&nojsoncallback=1" +
                "&user_id=" + mItem.getOwnerId();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success res", response.toString());
                        try {
                            v.setText(response.getJSONObject("person").getJSONObject("location").getString("_content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error res", error.getLocalizedMessage());
                    }
                });
        // Access the RequestQueue through a singleton class.
        MyVolley.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }
}
