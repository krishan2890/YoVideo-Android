package com.inspius.canyon.yo_video.adapter;

import android.graphics.Bitmap;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.listener.AnimateFirstDisplayListener;
import com.inspius.canyon.yo_video.model.CategoryJSON;
import com.marshalchen.ultimaterecyclerview.UltimateGridLayoutAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GridAllCategoryAdapter extends UltimateGridLayoutAdapter<CategoryJSON, GridAllCategoryAdapter.HolderGirdCell> {
    private List<CategoryJSON> mItems;
    AdapterActionListener listener;

    private DisplayImageOptions optionsThumbnail;
    private DisplayImageOptions optionsIcon;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public GridAllCategoryAdapter(List<CategoryJSON> items) {
        super(items);

        this.mItems = items;

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


    public void insert(CategoryJSON model) {
        insertInternal(mItems, model, getAdapterItemCount());
    }


    @Override
    protected void bindNormal(HolderGirdCell holder, CategoryJSON categoryJSON, final int position) {
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

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.item_grid_category;
    }

    @Override
    protected HolderGirdCell newViewHolder(View view) {
        return new HolderGirdCell(view, true);
    }

    @Override
    protected void withBindHolder(HolderGirdCell holder, CategoryJSON data, int position) {

    }

    public CategoryJSON getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
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
}