package com.inspius.canyon.yo_video.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.greendao.DBVideoDownload;
import com.inspius.canyon.yo_video.helper.ImageUtil;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DownloadListAdapter extends UltimateViewAdapter<DownloadListAdapter.HolderGirdCell> {
    private List<DBVideoDownload> mItems;
    AdapterActionListener listener;

    private DisplayImageOptions options;

    public DownloadListAdapter() {
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

    public void setAdapterActionListener(AdapterActionListener listener) {
        this.listener = listener;
    }

    @Override
    public HolderGirdCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_download, parent, false);
        HolderGirdCell vh = new HolderGirdCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderGirdCell holder, final int position) {
        final DBVideoDownload model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });

            //ImageLoader.getInstance().displayImage(model.getImage(), holder.imvThumbnail, options, animateFirstListener);

            Glide.with(GlobalApplication.getAppContext()).load(model.getPath()).placeholder(R.drawable.img_product_default).into(holder.imvThumbnail);
        }
    }

    public void add(List<DBVideoDownload> listData) {
        mItems.addAll(listData);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterItemCount() {
        return mItems.size();
    }

    @Override
    public HolderGirdCell newFooterHolder(View view) {
        return null;
    }

    @Override
    public HolderGirdCell newHeaderHolder(View view) {
        return null;
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

        @Bind(R.id.tvnName)
        TextView tvnName;

        public HolderGirdCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public DBVideoDownload getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}