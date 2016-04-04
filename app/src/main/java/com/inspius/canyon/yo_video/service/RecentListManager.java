package com.inspius.canyon.yo_video.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.greendao.DaoMaster;
import com.inspius.canyon.yo_video.greendao.DaoSession;
import com.inspius.canyon.yo_video.greendao.RecentVideos;
import com.inspius.canyon.yo_video.greendao.RecentVideosDao;
import com.inspius.canyon.yo_video.greendao.WishList;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Billy on 1/11/16.
 */
public class RecentListManager {
    private static RecentListManager mInstance;
    private Cursor cursor;
    private RecentVideosDao wishListDao;

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;
    List<RecentVideos> dataWishList;

    public static synchronized RecentListManager getInstance() {
        if (mInstance == null)
            mInstance = new RecentListManager();

        return mInstance;
    }

    public RecentListManager() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(GlobalApplication.getAppContext(), "wish-list-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        wishListDao = daoSession.getRecentVideosDao();
        cursor = db.query(wishListDao.getTablename(), wishListDao.getAllColumns(), null, null, null, null, null);

        reloadWishListData();
    }

    public RecentVideos addVideo(VideoModel model) {
        RecentVideos note = new RecentVideos(null, model.getVideoId(), model.getTitle());
        long id = daoSession.insert(note);
        cursor.requery();

        dataWishList.add(0, note);
        note.setId(id);

        int size = dataWishList.size();
        if (size > 10) {
            daoSession.delete(dataWishList.get(size - 1));
            dataWishList.remove(size - 1);
        }
        return note;
    }

    public void deleteVideo(WishList model) {
        wishListDao.deleteByKey(model.getId());
        cursor.requery();

        dataWishList.remove(model);
    }

    private List<RecentVideos> reloadWishListData() {
        QueryBuilder qb = wishListDao.queryBuilder();
        dataWishList = qb.list();

        return dataWishList;
    }

    public void getRecentVideosDetail(APIResponseListener listener) {
        if (dataWishList == null || dataWishList.isEmpty()) {
            listener.onSuccess(new ArrayList<VideoJSON>());
            return;
        }

        List<Long> listId = new ArrayList<>();
        for (RecentVideos model : dataWishList)
            listId.add(model.getVideoId());

        RPC.requestGetRecentVideoInfo(listId, listener);
    }

    public RecentVideos exitWishList(long id) {
        if (dataWishList == null || dataWishList.isEmpty())
            return null;

        for (RecentVideos model : dataWishList)
            if (id == model.getVideoId())
                return model;

        return null;
    }

    public void cancelRequestRecentVideos() {
        RPC.cancelRequestByTag(AppConstant.RELATIVE_URL_LIST_VIDEOS);
    }
}
