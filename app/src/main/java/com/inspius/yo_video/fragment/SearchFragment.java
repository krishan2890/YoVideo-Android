package com.inspius.yo_video.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.adapter.GridVideoAdapter;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.greendao.DBKeywordSearch;
import com.inspius.yo_video.helper.AppUtils;
import com.inspius.yo_video.listener.AdapterVideoActionListener;
import com.inspius.yo_video.model.DataCategoryJSON;
import com.inspius.yo_video.model.VideoJSON;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.AppSession;
import com.inspius.yo_video.service.DatabaseManager;
import com.inspius.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import cuneyt.example.com.tagview.Models.TagClass;
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

    @Bind(R.id.btnSearch)
    Button btnSearch;
    BasicGridLayoutManager mGridLayoutManager;
    GridVideoAdapter mAdapter = null;
    private int columns = 2;
    int pageNumber;
    ArrayList<TagClass> tagList;
    String currentKeySearch;
    DataCategoryJSON dataCategory;
    Random random;
    String keyword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataCategory = AppSession.getInstance().getCategoryData();
    }

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
        edtKeyWord.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    pageNumber = 1;
                    callSearchData();
                    String keyword = edtKeyWord.getText().toString();
                    if (!TextUtils.isEmpty(keyword)) {
                        DBKeywordSearch dbKeywordSearch = DatabaseManager.getInstance().insertKeyword(keyword);
                        if (dbKeywordSearch != null)
                            prepareTags();
                    }
                }
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber = 1;
                callSearchData();
                String keyword = edtKeyWord.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    DBKeywordSearch dbKeywordSearch = DatabaseManager.getInstance().insertKeyword(keyword);
                    if (dbKeywordSearch != null)
                        prepareTags();
                }
            }
        });
        random = new Random();
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapter = new GridVideoAdapter(new ArrayList<VideoModel>());
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                callSearchData();
            }
        });
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                callSearchData();
            }
        });

        ultimateRecyclerView.setAdapter(mAdapter);
        startAnimLoading();
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
                mAdapter.clear();
                callSearchData();
            }
        });
        tagGroup.setOnTagDeleteListener(new OnTagDeleteListener() {

            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                view.remove(position);
                DatabaseManager.getInstance().deleteKeywordByName(tag.text);
                prepareTags();
            }
        });
    }

    private void callSearchData() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String keyword = edtKeyWord.getText().toString();

        if (TextUtils.isEmpty(keyword))
            return;

        currentKeySearch = keyword;

        tagslayout.setVisibility(View.GONE);
        ultimateRecyclerView.setVisibility(View.VISIBLE);

        if (pageNumber < 1)
            pageNumber = 1;
        RPC.requestSearchVideoByKeyWord(edtKeyWord.getText().toString(), pageNumber, AppConstant.LIMIT_PAGE_HOMES, new APIResponseListener() {
            @Override
            public void onError(String message) {

                //isResponseData = true;
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                // isResponseData = true;

                stopAnimLoading();
                if (ultimateRecyclerView == null)
                    return;

                ultimateRecyclerView.setRefreshing(false);


                List<VideoJSON> data = (List<VideoJSON>) results;
                if (data == null || data.isEmpty())
                    return;

                if (pageNumber == 1)
                    mAdapter.clear();

                pageNumber++;
                updateDataProduct(data);
            }
        });
    }

    private void prepareTags() {
        List<DBKeywordSearch> keywords = DatabaseManager.getInstance().listKeyword();
        tagList = new ArrayList<>();
        if (keywords != null) {
            for (DBKeywordSearch keyword : keywords) {
                tagList.add(new TagClass(String.valueOf(keyword.getId()), keyword.getKeyword()));
            }
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

        for (int i = 0; i < tagList.size(); i++) {
            if (TextUtils.isEmpty(text) || tagList.get(i).getName().toLowerCase().startsWith(text.toLowerCase())) {
                tag = new Tag(tagList.get(i).getName());
                tag.radius = 10f;
                tag.layoutColor = (Color.parseColor(tagList.get(i).getColor()));
//                if (i % 2 == 0) // you can set deletable or not
                tag.isDeletable = true;
                tags.add(tag);
            }
        }
        tagGroup.addTags(tags);

    }

    void startAnimLoading() {
        avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);
    }

    void updateDataProduct(List<VideoJSON> data) {
        List<VideoModel> listVideo = new ArrayList<>();
        for (VideoJSON productModel : data) {
            VideoModel vModel = new VideoModel(productModel);
            vModel.setCategoryName(AppUtils.getCategoryName(dataCategory, productModel.categoryId));
            listVideo.add(vModel);
        }

        mAdapter.insert(listVideo);
    }

    @Override
    public void onPlayVideoListener(int position, VideoModel model) {
        Intent intent = AppUtils.getIntentVideoDetail(mContext, (VideoModel) model, false);
        if (intent == null)
            return;

        startActivity(intent);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        Intent intent = AppUtils.getIntentVideoDetail(mContext, (VideoModel) model, false);
        if (intent == null)
            return;

        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_SEARCH_BY_KEYWORD);
    }
}
