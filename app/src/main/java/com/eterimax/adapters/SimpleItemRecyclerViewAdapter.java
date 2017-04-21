package com.eterimax.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.eterimax.R;
import com.eterimax.activities.BaseActivity;
import com.eterimax.activities.ImageDetailActivity;
import com.eterimax.activities.ImageListActivity;
import com.eterimax.pojos.Image;
import com.eterimax.singletons.MyVolley;
import com.google.gson.Gson;

import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final BaseActivity mContext;
    private final List<Image> mValues;

    public SimpleItemRecyclerViewAdapter(BaseActivity context, List<Image> items) {
        mContext = context;
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
        holder.mImageView.setImageUrl(mValues.get(position).toString(),
                MyVolley.getInstance(mContext).getImageLoader());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ImageDetailActivity.class);
                intent.putExtra(ImageListActivity.ARG_ITEM, new Gson().toJson(holder.mItem));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mContext);
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final NetworkImageView mImageView;
        private Image mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (NetworkImageView) view.findViewById(R.id.flickr_image);
        }
    }
}
