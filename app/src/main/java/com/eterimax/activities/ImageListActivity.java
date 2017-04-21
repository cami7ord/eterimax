package com.eterimax.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eterimax.BuildConfig;
import com.eterimax.R;
import com.eterimax.adapters.SimpleItemRecyclerViewAdapter;
import com.eterimax.pojos.Image;
import com.eterimax.singletons.MyVolley;
import com.eterimax.utilities.Utilities;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Images. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ImageDetailActivity} representing
 * item details.
 */

public class ImageListActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchView.OnFocusChangeListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private static final int GRID_SPAN = 3;
    private static final int PER_PAGE = 60;
    private int currentPage = 1;
    private boolean isLoading = false;
    private String query = "";
    private MenuItem searchItem;
    private List<Image> imageList;

    /**
     * The argument representing the selected item.
     */
    public static final String ARG_ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        imageList = new ArrayList<>(PER_PAGE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.image_list);

        if(Utilities.hasInternet(this)) {
            // Start our refresh background task
            fetchImages(currentPage);
        } else {
            findViewById(R.id.no_net_layout).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);

        return true;
    }

    private void fetchImages(final int page) {

        currentPage = page;
        isLoading = true;
        displayLoadingIndicator(true);

        String method = "flickr.photos.getRecent";
        if(!TextUtils.isEmpty(query))
            method = "flickr.photos.search";

        String url = BuildConfig.BASE_URL + method + getString(R.string.api_key) +
                "&per_page=" + PER_PAGE + "&page=" + currentPage + getString(R.string.image_extras) +
                "&text=" + query;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if(page <= 1) {
                            setupRecyclerView(response);
                        } else {
                            modifyRecyclerView(response);
                        }
                        isLoading = false;
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error res", error.getLocalizedMessage());
                        displayLoadingIndicator(false);
                        isLoading = false;
                    }
                });

        // Access the RequestQueue through a singleton class.
        MyVolley.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void displayLoadingIndicator(boolean loading) {
        // We make sure that the SwipeRefreshLayout is displaying it's refreshing indicator
        if(currentPage == 1) {
            if(loading) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                hideProgressDialog();
            }
        }  else {
            if(loading) {
                showProgressDialog();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                hideProgressDialog();
            }
        }
    }

    private void setupRecyclerView(@NonNull JSONObject response) {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, GRID_SPAN);
        mRecyclerView.setLayoutManager(mLayoutManager);

        imageList = parseResponse(response);

        mAdapter = new SimpleItemRecyclerViewAdapter(this, imageList);
        mRecyclerView.setAdapter(mAdapter);

        // Stop the refreshing indicator
        displayLoadingIndicator(false);

        setupPagination();
    }

    private void modifyRecyclerView(@NonNull JSONObject response) {
        imageList.addAll(parseResponse(response));
        mAdapter.notifyItemRangeInserted(imageList.size(), imageList.size() + PER_PAGE);
        // Stop the refreshing indicator
        displayLoadingIndicator(false);
    }

    private List<Image> parseResponse(JSONObject response) {

        List<Image> images = new ArrayList<>(PER_PAGE);

        try {

            JSONArray photoArray = response.getJSONObject("photos").getJSONArray("photo");
            JSONObject photoObject;

            int farm;
            String server, imageId, secret, ownerId;
            String ownerName, date, title, description;

            for(int i=0 ; i<photoArray.length() ; i++) {

                photoObject = photoArray.getJSONObject(i);

                farm = photoObject.getInt("farm");
                server = photoObject.getString("server");
                imageId = photoObject.getString("id");
                secret = photoObject.getString("secret");
                ownerId = photoObject.getString("owner");

                ownerName = photoObject.getString("ownername");
                date = photoObject.getString("datetaken");
                title = photoObject.getString("title");
                description = photoObject.getJSONObject("description").getString("_content");

                images.add(new Image.Builder(farm, server, imageId, secret, ownerId)
                        .ownerName(ownerName).date(date).title(title).description(description).build());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return images;
    }

    private void setupPagination() {
        setupRefresh();
        setupLoadMore();
    }

    private void setupRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query = "";
                fetchImages(1);
            }
        });
    }

    private void setupLoadMore() {
        Mugen.with(mRecyclerView, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                fetchImages(++currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        }).start();
    }

    @Override
    public boolean onQueryTextSubmit(String searchQuery) {
        query = searchQuery;
        fetchImages(1);
        invalidateOptionsMenu();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus) {
            MenuItemCompat.collapseActionView(searchItem);
        }
    }
}
