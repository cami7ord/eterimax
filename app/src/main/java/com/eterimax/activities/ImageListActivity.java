package com.eterimax.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.eterimax.R;
import com.eterimax.pojos.Image;
import com.eterimax.singletons.MyVolley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Images. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ImageDetailActivity} representing
 * item details.
 */

public class ImageListActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    /**
     * The argument representing the item ID.
     */
    public static final String ARG_ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.image_list);
        downloadImages();

    }

    private void downloadImages() {

        showProgressDialog();

        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=f08c2e99273a9d8c85ffe004223cfb4f&format=json&nojsoncallback=1";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success res", response.toString());
                        setupRecyclerView(mRecyclerView);
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error res", error.getLocalizedMessage());
                    }
                });

        // Access the RequestQueue through a singleton class.
        MyVolley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);

        List<Image> list = new ArrayList<>();
        list.add(new Image.Builder("https://pbs.twimg.com/profile_images/768009219790503937/o2Dn7hDK.jpg").build());

        RecyclerView.Adapter mAdapter = new SimpleItemRecyclerViewAdapter(list);
        recyclerView.setAdapter(mAdapter);

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Image> mValues;

        public SimpleItemRecyclerViewAdapter(List<Image> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            Glide.with(ImageListActivity.this).load(mValues.get(position).getImageUrl()).into(holder.mImageView);

            /*holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ImageDetailActivity.class);
                    intent.putExtra(ARG_ITEM_ID, holder.mItem.getImageUrl());

                    context.startActivity(intent);
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public Image mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.flickr_image);
            }
        }
    }
}
