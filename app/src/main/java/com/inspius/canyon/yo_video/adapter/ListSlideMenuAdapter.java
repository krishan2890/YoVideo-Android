package com.inspius.canyon.yo_video.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.model.SlideMenuModel;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
    public int getAdapterItemCount() {
        return slideMenuList.size();
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slide_menu, parent, false);
        ViewHolder vh = new ViewHolder(v, true);
        return vh;
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }

    @Override
    public long generateHeaderId(int position) {
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        super.onItemDismiss(position);
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

    public SlideMenuModel getItem(int position) {
        if (position < slideMenuList.size())
            return slideMenuList.get(position);
        else
            return null;
    }
}