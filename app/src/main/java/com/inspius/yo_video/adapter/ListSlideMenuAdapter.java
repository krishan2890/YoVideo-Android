package com.inspius.yo_video.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.listener.AdapterActionListener;
import com.inspius.yo_video.model.SlideMenuModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListSlideMenuAdapter extends UltimateViewAdapter<ListSlideMenuAdapter.ViewHolder> {
    private List<SlideMenuModel> slideMenuList;
    AdapterActionListener listener;

    public ListSlideMenuAdapter(List<SlideMenuModel> stringList, AdapterActionListener listener) {
        this.slideMenuList = stringList;
        this.listener = listener;
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slide_menu, parent, false);
        return new ViewHolder(v, true);
    }

    @Override
    public int getAdapterItemCount() {
        return slideMenuList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SlideMenuModel slideMenuModel = getItem(position);
        if (slideMenuModel != null) {
            holder.tvnMenuTitle.setText(slideMenuModel.title);
//            holder.imvMenuIcon.setImageResource(slideMenuModel.icon);
            ImageLoader.getInstance().displayImage(slideMenuModel.icon, holder.imvMenuIcon);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, slideMenuModel);
                }
            });

            if (slideMenuModel.isActived)
                holder.onItemSelected();
            else holder.onItemClear();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public SlideMenuModel getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < slideMenuList.size())
            return slideMenuList.get(position);
        else return new SlideMenuModel();
    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        @Bind(R.id.tvnMenuTitle)
        TextView tvnMenuTitle;

        @Bind(R.id.imvMenuIcon)
        ImageView imvMenuIcon;

        @Bind(R.id.viewLine)
        View viewLine;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#2b343d"));
            viewLine.setBackgroundColor(Color.parseColor("#cc3333"));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.TRANSPARENT);
            viewLine.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}