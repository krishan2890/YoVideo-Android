package com.inspius.yo_video.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.inspius.yo_video.R;
import com.inspius.yo_video.adapter.ListCommentAdapter;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.helper.DialogUtil;
import com.inspius.yo_video.listener.AdapterActionListener;
import com.inspius.yo_video.model.CommentJSON;
import com.inspius.yo_video.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/15.
 */
public class CommentFragment extends BaseMainFragment implements AdapterActionListener {
    public static final String TAG = CommentFragment.class.getSimpleName();

    public static CommentFragment newInstance(VideoModel videoModel) {
        CommentFragment fragment = new CommentFragment();
        fragment.videoModel = videoModel;
        return fragment;
    }

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.edtCommentText)
    EditText edtCommentText;

    @Bind(R.id.linearLayoutNoComment)
    LinearLayout linearLayoutNoComment;

    private VideoModel videoModel;

    private LinearLayoutManager linearLayoutManager;
    private ListCommentAdapter mAdapter = null;

    int pageNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return R.layout.fragment_comment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityInterface.setVisibleHeaderMenu(false);
        mActivityInterface.setVisibleHeaderSearch(false);
        mActivityInterface.updateHeaderTitle("Comments");
    }

    @Override
    public void onInitView() {
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_category).colorResId(R.color.black_20).build());

        mAdapter = new ListCommentAdapter(mContext);
        mAdapter.setAdapterActionListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ultimateRecyclerView.reenableLoadmore();
                pageNumber = 1;
                requestGetComments();
            }
        });
        // setting load more Recycler View
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetComments();
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);

        startAnimLoading();

        pageNumber = 1;
        requestGetComments();
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        //mHostTabInterface.addFragment(VideoDetailFragment.newInstance((VideoModel) model, false), true);
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);
    }

    void requestGetComments() {
        linearLayoutNoComment.setVisibility(View.GONE);
        RPC.requestGetListComment(videoModel.getVideoId(), pageNumber, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                if (ultimateRecyclerView == null)
                    return;

                ultimateRecyclerView.setRefreshing(false);

                if (pageNumber <= 1)
                    mAdapter.clear();


                List<CommentJSON> data = (List<CommentJSON>) results;
                if (data == null || data.isEmpty()) {
                    ultimateRecyclerView.disableLoadmore();

                    if (pageNumber <= 1)
                        linearLayoutNoComment.setVisibility(View.VISIBLE);
                } else
                    mAdapter.add(data);
            }
        });
    }

    @OnClick(R.id.imvPostComment)
    void insertComment() {
        mActivityInterface.hideKeyBoard();
        linearLayoutNoComment.setVisibility(View.GONE);
        if (!mAccountDataManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));
            return;
        }

        String comment = edtCommentText.getText().toString();
        if (TextUtils.isEmpty(comment)) {

        } else {
            mActivityInterface.showLoading(getString(R.string.msg_loading));

            RPC.requestPostCommentVideo(videoModel.getVideoId(), comment, new APIResponseListener() {
                @Override
                public void onError(String message) {
                    mActivityInterface.hideLoading();

                    DialogUtil.showMessageBox(mContext, message);
                }

                @Override
                public void onSuccess(Object results) {
                    mActivityInterface.hideLoading();

                    CommentJSON commentJSON = (CommentJSON) results;
                    if (commentJSON != null)
                        mAdapter.add(commentJSON);

                    edtCommentText.setText("");
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_INSERT_COMMENT);
        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_GET_COMMENTS);
    }
}
