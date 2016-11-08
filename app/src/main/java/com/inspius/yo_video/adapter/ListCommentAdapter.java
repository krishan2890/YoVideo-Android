package com.inspius.yo_video.adapter;

/**
 * Created by Billy on 11/7/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.helper.TimeUtils;
import com.inspius.yo_video.listener.AdapterActionListener;
import com.inspius.yo_video.model.CommentJSON;
import com.inspius.yo_video.widget.CalloutLink;
import com.inspius.yo_video.widget.Hashtag;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListCommentAdapter extends UltimateViewAdapter<ListCommentAdapter.HolderListCell> {
    private List<CommentJSON> mItems;
    AdapterActionListener listener;

    private Context mContext;
    private DisplayImageOptions options;

    public ListCommentAdapter(Context mContext) {
        this.mContext = mContext;
        this.mItems = new ArrayList<>();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_avatar)
                .showImageForEmptyUri(R.drawable.img_avatar)
                .showImageOnFail(R.drawable.img_avatar)
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
    public HolderListCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        HolderListCell vh = new HolderListCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderListCell holder, final int position) {
        final CommentJSON model = getItem(position);
        if (model != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });

            holder.tvnUserName.setText(model.user.username);
            holder.tvnTime.setText(TimeUtils.getDateTimeFormat(model.createAt));

            ImageLoader.getInstance().displayImage(model.user.avatar, holder.imvAvatar, options);

            String comments = model.commentText;
            ArrayList<int[]> hashtagSpans = getSpans(comments, '#');
            ArrayList<int[]> calloutSpans = getSpans(comments, '@');

            SpannableString commentsContent =
                    new SpannableString(comments);

            for (int i = 0; i < hashtagSpans.size(); i++) {
                int[] span = hashtagSpans.get(i);
                int hashTagStart = span[0];
                int hashTagEnd = span[1];

                commentsContent.setSpan(new Hashtag(mContext),
                        hashTagStart,
                        hashTagEnd, 0);
            }

            for (int i = 0; i < calloutSpans.size(); i++) {
                int[] span = calloutSpans.get(i);
                int calloutStart = span[0];
                int calloutEnd = span[1];

                commentsContent.setSpan(new CalloutLink(mContext),
                        calloutStart,
                        calloutEnd, 0);
            }

            holder.tvnContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tvnContent.setText(commentsContent);

        }
    }

    public ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<int[]>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return spans;
    }

    public void add(CommentJSON data) {
        mItems.add(0, data);
        notifyDataSetChanged();
    }

    public void add(List<CommentJSON> listData) {
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
    public HolderListCell newFooterHolder(View view) {
        return null;
    }

    @Override
    public HolderListCell newHeaderHolder(View view) {
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

    public class HolderListCell extends UltimateRecyclerviewViewHolder {
        @Bind(R.id.tvnContent)
        TextView tvnContent;

        @Bind(R.id.tvnUserName)
        TextView tvnUserName;

        @Bind(R.id.tvnTime)
        TextView tvnTime;

        @Bind(R.id.imvAvatar)
        ImageView imvAvatar;

        public HolderListCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public CommentJSON getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}