package com.inspius.canyon.yo_video.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.greendao.NewWishList;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.listener.AnimateFirstDisplayListener;
import com.inspius.canyon.yo_video.listener.WishLishAdapterVideoActionListener;
import com.inspius.canyon.yo_video.model.CategoryJSON;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WishLishAdapter extends UltimateViewAdapter<WishLishAdapter.HolderGirdCell> {
    private List<NewWishList> mItems;
    WishLishAdapterVideoActionListener listener;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public WishLishAdapter() {
        this.mItems = new ArrayList<>();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_video_default)
                .showImageForEmptyUri(R.drawable.img_video_default)
                .showImageOnFail(R.drawable.img_video_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    public void setAdapterActionListener(WishLishAdapterVideoActionListener listener) {
        this.listener = listener;
    }

    @Override
    public HolderGirdCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_video, parent, false);
        HolderGirdCell vh = new HolderGirdCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderGirdCell holder, final int position) {
        final NewWishList model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.getName());
            holder.tvnView.setText(model.getView());
            holder.tvnCategory.setText(model.getCategoryname());
            holder.tvnSeries.setText(model.getSeries());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });

            holder.imvPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onPlayVideoListener(position, model);
                }
            });

            ImageLoader.getInstance().displayImage(model.getImage(), holder.imvThumbnail, options, animateFirstListener);

        }
    }


    public void add(List<NewWishList> listData) {
        mItems.addAll(listData);
        notifyDataSetChanged();
    }

    /*public void updateCategoryName(List<CategoryJSON> listCategory) {
        for (VideoModel video : mItems) {
            String categoryName = "";
            for (CategoryJSON category : listCategory) {
                if (video.getCategoryID() == category.id) {
                    categoryName = category.name;
                    break;
                }
            }

            if (TextUtils.isEmpty(categoryName))
                categoryName = "Other";

            video.setCategoryName(categoryName);
        }

        notifyDataSetChanged();
    }*/

    public void clear() {
//        clear(mItems);
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterItemCount() {
        return mItems.size();
    }

    @Override
    public HolderGirdCell getViewHolder(View view) {
        return new HolderGirdCell(view, false);
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    public class HolderGirdCell extends UltimateRecyclerviewViewHolder {
        @Bind(R.id.imvThumbnail)
        ImageView imvThumbnail;

        @Bind(R.id.imvPlay)
        ImageView imvPlay;

        @Bind(R.id.tvnName)
        TextView tvnName;

        @Bind(R.id.tvnSeries)
        TextView tvnSeries;

        @Bind(R.id.tvnView)
        TextView tvnView;

        @Bind(R.id.tvnCategory)
        TextView tvnCategory;

        public HolderGirdCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public NewWishList getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}