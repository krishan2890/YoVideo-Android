package com.inspius.canyon.yo_video.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.greendao.DaoMaster;
import com.inspius.canyon.yo_video.greendao.DaoSession;
import com.inspius.canyon.yo_video.greendao.WishList;
import com.inspius.canyon.yo_video.greendao.WishListDao;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Billy on 1/11/16.
 */
public class WishListManager {
    private static WishListManager mInstance;
    private Cursor cursor;
    private WishListDao wishListDao;

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;
    List<WishList> dataWishList;

    public static synchronized WishListManager getInstance() {
        if (mInstance == null)
            mInstance = new WishListManager();

        return mInstance;
    }

    public WishListManager() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(GlobalApplication.getAppContext(), "wish-list-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        wishListDao = daoSession.getWishListDao();
        cursor = db.query(wishListDao.getTablename(), wishListDao.getAllColumns(), null, null, null, null, null);

        reloadWishListData();
    }

    public WishList addVideo(VideoModel model) {
        WishList note = new WishList(null, model.getVideoId(), model.getTitle());
        long id = daoSession.insert(note);
        cursor.requery();

        dataWishList.add(0, note);

        note.setId(id);
        return note;
    }

    public void deleteVideo(WishList model) {
        wishListDao.deleteByKey(model.getId());
        cursor.requery();

        dataWishList.remove(model);
    }

    private List<WishList> reloadWishListData() {
        QueryBuilder qb = wishListDao.queryBuilder();
        dataWishList = qb.list();

        return dataWishList;
    }

    public void getWishListDetail(APIResponseListener listener) {
        if (dataWishList == null || dataWishList.isEmpty()) {
            listener.onSuccess(new ArrayList<VideoJSON>());
            return;
        }

        List<Long> listId = new ArrayList<>();
        for (WishList model : dataWishList)
            listId.add(model.getVideoId());

        RPC.requestGetVideoWishListInfo(listId, listener);
    }

    public WishList exitWishList(long id) {
        if (dataWishList == null || dataWishList.isEmpty())
            return null;

        for (WishList model : dataWishList)
            if (id == model.getVideoId())
                return model;

        return null;
    }

    public void cancelRequestWishList() {
        RPC.cancelRequestByTag(AppConstant.RELATIVE_URL_LIST_VIDEOS);
    }
}
