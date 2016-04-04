package com.inspius.canyon.yo_video.adapter;

import android.graphics.Bitmap;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.listener.AnimateFirstDisplayListener;
import com.inspius.canyon.yo_video.model.CategoryJSON;
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

public class GridAllCategoryAdapter extends UltimateViewAdapter<GridAllCategoryAdapter.HolderGirdCell> {
    private List<CategoryJSON> mItems;
    AdapterActionListener listener;

    private DisplayImageOptions optionsThumbnail;
    private DisplayImageOptions optionsIcon;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public GridAllCategoryAdapter() {
        this.mItems = new ArrayList<>();

        optionsThumbnail = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.category_default)
                .showImageForEmptyUri(R.drawable.category_default)
                .showImageOnFail(R.drawable.category_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        optionsIcon = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_menu_categories)
                .showImageForEmptyUri(R.drawable.icon_menu_categories)
                .showImageOnFail(R.drawable.icon_menu_categories)
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
                .inflate(R.layout.item_grid_category, parent, false);
        HolderGirdCell vh = new HolderGirdCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderGirdCell holder, final int position) {
        final CategoryJSON model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.name);
            float[] direction = new float[]{0.0f, -1.0f, 0.5f};
            MaskFilter filter = new EmbossMaskFilter(direction, 0.8f, 15f, 1f);

            holder.tvnName.getPaint().setMaskFilter(filter);

            ImageLoader.getInstance().displayImage(model.image, holder.imvThumbnail, optionsThumbnail, animateFirstListener);
            ImageLoader.getInstance().displayImage(model.icon, holder.imvIcon, optionsIcon, animateFirstListener);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });

        }
    }


    public void add(List<CategoryJSON> listData) {
        mItems.addAll(listData);
        notifyDataSetChanged();
    }


    public void clear() {
        clear(mItems);
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

        @Bind(R.id.imvIcon)
        ImageView imvIcon;

        @Bind(R.id.tvnName)
        TextView tvnName;

        public HolderGirdCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public CategoryJSON getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}