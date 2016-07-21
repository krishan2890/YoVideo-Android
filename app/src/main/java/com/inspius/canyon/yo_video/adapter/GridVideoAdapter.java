package com.inspius.canyon.yo_video.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.listener.AnimateFirstDisplayListener;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateGridLayoutAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GridVideoAdapter extends UltimateGridLayoutAdapter<VideoModel, GridVideoAdapter.HolderGirdCell> {
    private List<VideoModel> mItems;
    AdapterVideoActionListener listener;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public GridVideoAdapter(List<VideoModel> items) {
        super(items);
        this.mItems = items;

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

    public void setAdapterActionListener(AdapterVideoActionListener listener) {
        this.listener = listener;
    }

//    public void insert(List<VideoModel> data) {
//        insertInternal(data, mItems);
//    }
//
//    public void insert(VideoModel model, int position) {
//        insertInternal(mItems, model, position);
//    }
//
//    public void remove(int position) {
//        removeInternal(mItems, position);
//    }

    public void clear() {
        removeAll();
//        for (int i = getAdapterItemCount() - 1; i > 0; i--)
//            removeAt(i);
    }

    @Override
    protected void bindNormal(GridVideoAdapter.HolderGirdCell holder, VideoModel videoModel, final int position) {
        final VideoModel model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.getTitle());
            holder.tvnView.setText(model.getViewNumber());
            holder.tvnCategory.setText(model.getCategoryName());
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

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.item_grid_video;
    }

    @Override
    protected GridVideoAdapter.HolderGirdCell newViewHolder(View view) {
        return new HolderGirdCell(view, true);
    }

    @Override
    protected void withBindHolder(GridVideoAdapter.HolderGirdCell holder, VideoModel data, int position) {

    }

    public VideoModel getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
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
}