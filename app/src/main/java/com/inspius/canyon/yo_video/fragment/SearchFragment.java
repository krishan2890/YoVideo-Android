package com.inspius.canyon.yo_video.fragment;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.GridVideoAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.service.AppSession;
import com.inspius.canyon.yo_video.helper.AppUtils;
import com.inspius.canyon.yo_video.helper.EndlessRecyclerOnScrollListener;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.canyon.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;
import cuneyt.example.com.tagview.Models.TagClass;
import cuneyt.example.com.tagview.Tag.Constants;
import cuneyt.example.com.tagview.Tag.OnTagClickListener;
import cuneyt.example.com.tagview.Tag.OnTagDeleteListener;
import cuneyt.example.com.tagview.Tag.Tag;
import cuneyt.example.com.tagview.Tag.TagView;

/**
 * Created by Billy on 12/1/15.
 */
public class SearchFragment extends BaseMainFragment implements AdapterVideoActionListener {
    public static final String TAG = SearchFragment.class.getSimpleName();

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.edtKeyWord)
    EditText edtKeyWord;

    @Bind(R.id.tag_group)
    TagView tagGroup;

    @Bind(R.id.tags_layout)
    ScrollView tagslayout;

    BasicGridLayoutManager mGridLayoutManager;
    GridVideoAdapter mAdapter = null;
    private int columns = 2;
    int pageNumber;
    ArrayList<TagClass> tagList;
    String currentKeySearch;
    DataCategoryJSON dataCategory;
    Random random;
    boolean isResponseData;
    List<VideoModel> data;
    String keyword;
    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityInterface.updateHeaderTitle(getString(R.string.search));
        mActivityInterface.setVisibleHeaderMenu(false);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    @Override
    public void onInitView() {
        stopAnimLoading();
        random = new Random();
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapter = new GridVideoAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);

        ultimateRecyclerView.setAdapter(mAdapter);

        ultimateRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                requestGetDataProduct();
            }
        });

        // tag view

        prepareTags();
        setTags("");

        edtKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagslayout.setVisibility(View.VISIBLE);
                setTags(s);
                if (currentKeySearch != null && s.toString().equalsIgnoreCase(currentKeySearch))
                    tagslayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                edtKeyWord.setText(tag.text);
                edtKeyWord.setSelection(tag.text.length());//to set cursor position
            }
        });
        tagGroup.setOnTagDeleteListener(new OnTagDeleteListener() {

            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                view.remove(position);
            }
        });

        // Data Search
        RPC.requestGetCategories(new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                dataCategory= (DataCategoryJSON) results;
            }
        });
    }

    private void prepareTags() {
        tagList = new ArrayList<>();
        JSONArray jsonArray = null;
        JSONObject temp;
        try {
            jsonArray = new JSONArray(Constants.COUNTRIES);
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getJSONObject(i);
                tagList.add(new TagClass(temp.getString("code"), temp.getString("name")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setTags(CharSequence cs) {
        String text = cs.toString();
        ArrayList<Tag> tags = new ArrayList<>();
        Tag tag;
        /**
         * counter for prevent frozen effect
         * if the tags number is greather than 20 some device will a bit frozen
         */
        int counter = 0;

        /**
         * for empty edittext
         */
        if (TextUtils.isEmpty(text)) {
            counter = 20;
            if (counter > tagList.size())
                counter = tagList.size();

            for (int k = 0; k < counter; k++) {
                int i = random.nextInt(tagList.size());
                tag = new Tag(tagList.get(i).getName());
                tag.radius = 10f;
                tag.layoutColor = (Color.parseColor(tagList.get(i).getColor()));
                if (i % 2 == 0) // you can set deletable or not
                    tag.isDeletable = true;
                tags.add(tag);
            }
        } else {
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).getName().toLowerCase().startsWith(text.toLowerCase())) {
                    tag = new Tag(tagList.get(i).getName());
                    tag.radius = 10f;
                    tag.layoutColor = (Color.parseColor(tagList.get(i).getColor()));
                    if (i % 2 == 0) // you can set deletable or not
                        tag.isDeletable = true;
                    tags.add(tag);
                    counter++;
                    /**
                     * if you don't want show all tags. You can set a limit.
                     if (counter == 10)
                     break;
                     */

                }
            }
        }

        tagGroup.addTags(tags);
    }

    void startAnimLoading() {
        avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        avloadingIndicatorView.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSearch)
    void doSearch() {
        keyword = edtKeyWord.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_key_word));
            return;
        }
        tagslayout.setVisibility(View.GONE);
        currentKeySearch = keyword;
        mActivityInterface.hideKeyBoard();
        mAdapter.clear();
        pageNumber = 1;
        startAnimLoading();
        isResponseData = false;

      requestGetDataProduct();
    }

    void requestGetDataProduct() {
        if (isResponseData)
            return;

        isResponseData = false;
        RPC.requestSearchVideoByKeyWord(keyword, new APIResponseListener() {
            @Override
            public void onError(String message) {
                isResponseData = true;
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                isResponseData = true;

                stopAnimLoading();

                List<VideoJSON> data = (List<VideoJSON>) results;
                if (data == null || data.isEmpty())
                    return;

                pageNumber++;
                updateDataProduct(data);
            }
        });
    }

    void updateDataProduct(List<VideoJSON> data) {
        List<VideoModel> listVideo = new ArrayList<>();
        for (VideoJSON productModel : data) {
            VideoModel vModel = new VideoModel(productModel);
            vModel.setCategoryName(AppUtils.getCategoryName(dataCategory, productModel.categoryId));
            listVideo.add(vModel);
        }

        mAdapter.add(listVideo);
    }

    @Override
    public void onPlayVideoListener(int position, VideoModel model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(model, true), true);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance((VideoModel) model, false), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RPC.cancelRequestByTag(AppConstant.RELATIVE_URL_LIST_VIDEOS);
        RPC.cancelRequestByTag(AppConstant.RELATIVE_URL_MORE_VIDEOS);
    }
}
