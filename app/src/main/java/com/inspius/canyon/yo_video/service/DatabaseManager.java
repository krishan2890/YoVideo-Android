package com.inspius.canyon.yo_video.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.greendao.DBKeywordSearch;
import com.inspius.canyon.yo_video.greendao.DBKeywordSearchDao;
import com.inspius.canyon.yo_video.greendao.DBNotification;
import com.inspius.canyon.yo_video.greendao.DBNotificationDao;
import com.inspius.canyon.yo_video.greendao.DBRecentVideo;
import com.inspius.canyon.yo_video.greendao.DBRecentVideoDao;
import com.inspius.canyon.yo_video.greendao.DBVideoDownload;
import com.inspius.canyon.yo_video.greendao.DBVideoDownloadDao;
import com.inspius.canyon.yo_video.greendao.DBWishListVideo;
import com.inspius.canyon.yo_video.greendao.DaoMaster;
import com.inspius.canyon.yo_video.greendao.DaoSession;
import com.inspius.canyon.yo_video.greendao.DBWishListVideoDao;
import com.inspius.canyon.yo_video.helper.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Billy on 4/19/16.
 */
public class DatabaseManager implements IDatabaseManager, AsyncOperationListener {
    /**
     * Class tag. Used for debug.
     */
    private static final String TAG = DatabaseManager.class.getCanonicalName();
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager instance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context context;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    public DatabaseManager() {
        this.context = GlobalApplication.getAppContext();
        mHelper = new DaoMaster.DevOpenHelper(this.context, "-yovideo", null);
        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
    }

    public static IDatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();

        return instance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public synchronized void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
            asyncSession.deleteAll(DBKeywordSearch.class);    // clear all elements from a table
            asyncSession.deleteAll(DBNotification.class);
            asyncSession.deleteAll(DBRecentVideo.class);
            asyncSession.deleteAll(DBWishListVideo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DBKeywordSearch insertKeyword(DBKeywordSearch keyword) {
        try {
            if (keyword != null) {
                openWritableDb();
                DBKeywordSearchDao userDao = daoSession.getDBKeywordSearchDao();
                userDao.insert(keyword);
                Logger.d(TAG, "Inserted keyword: " + keyword.getKeyword() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyword;
    }

    @Override
    public DBKeywordSearch insertKeyword(String keyword) {
        List<DBKeywordSearch> keywords = getKeywordByName(keyword);
        if (keywords == null || keywords.isEmpty()) {
            DBKeywordSearch keywordSearch = new DBKeywordSearch();
            keywordSearch.setKeyword(keyword);

            return insertKeyword(keywordSearch);
        }

        return null;
    }

    @Override
    public ArrayList<DBKeywordSearch> listKeyword() {
        List<DBKeywordSearch> keywords = null;
        try {
            openReadableDb();
            DBKeywordSearchDao userDao = daoSession.getDBKeywordSearchDao();
            if (userDao.loadAll().size() > 100)
                clearKeyword();

            QueryBuilder<DBKeywordSearch> queryBuilder = userDao.queryBuilder().orderDesc(DBKeywordSearchDao.Properties.Id).limit(20);
            keywords = queryBuilder.list();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keywords != null) {
            return new ArrayList<>(keywords);
        }
        return null;
    }

    @Override
    public void updateKeyword(DBKeywordSearch keyword) {
        try {
            if (keyword != null) {
                openWritableDb();
                daoSession.update(keyword);
                Logger.d(TAG, "Updated user: " + keyword.getKeyword() + " from the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DBKeywordSearch> getKeywordByName(String keyword) {
        List<DBKeywordSearch> keywords = null;
        try {
            DBKeywordSearchDao userDao = daoSession.getDBKeywordSearchDao();
            QueryBuilder<DBKeywordSearch> queryBuilder = userDao.queryBuilder().where(DBKeywordSearchDao.Properties.Keyword.eq(keyword));
            keywords = queryBuilder.list();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keywords != null) {
            return new ArrayList<>(keywords);
        }
        return null;
    }

    @Override
    public void deleteKeywordByName(String keyword) {
        try {
            openWritableDb();
            DBKeywordSearchDao userDao = daoSession.getDBKeywordSearchDao();
            QueryBuilder<DBKeywordSearch> queryBuilder = userDao.queryBuilder().where(DBKeywordSearchDao.Properties.Keyword.eq(keyword));
            List<DBKeywordSearch> userToDelete = queryBuilder.list();
            for (DBKeywordSearch user : userToDelete) {
                userDao.delete(user);
            }
            daoSession.clear();
            Logger.d(TAG, userToDelete.size() + " entry. " + "Deleted user: " + keyword + " from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteKeywordById(Long keywordId) {
        return false;
    }

    @Override
    public void clearKeyword() {
        try {
            openWritableDb();
            DBKeywordSearchDao userDao = daoSession.getDBKeywordSearchDao();
            userDao.deleteAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DBWishListVideo> listVideoAtWishList(int userID) {
        List<DBWishListVideo> data = null;
        try {
            openReadableDb();
            DBWishListVideoDao userDao = daoSession.getDBWishListVideoDao();

            QueryBuilder<DBWishListVideo> queryBuilder = userDao.queryBuilder().where(DBWishListVideoDao.Properties.UserID.eq(userID)).orderDesc(DBWishListVideoDao.Properties.Id);
            data = queryBuilder.list();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data != null) {
            return new ArrayList<>(data);
        }
        return null;
    }

    @Override
    public boolean deleteVideoAtWishList(Long id) {
        try {
            openWritableDb();
            DBWishListVideoDao userDao = daoSession.getDBWishListVideoDao();
            userDao.deleteByKey(id);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteVideoAtWishListByVideoId(Long id) {
        try {
            openWritableDb();
            DBWishListVideoDao userDao = daoSession.getDBWishListVideoDao();
            QueryBuilder<DBWishListVideo> queryBuilder = userDao.queryBuilder().where(DBWishListVideoDao.Properties.VideoId.eq(id));
            queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteVideoAtWishList(DBWishListVideo video) {
        try {
            openWritableDb();
            DBWishListVideoDao userDao = daoSession.getDBWishListVideoDao();
            userDao.delete(video);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DBWishListVideo insertVideoToWishList(DBWishListVideo video) {
        try {
            if (video != null) {
                openWritableDb();
                DBWishListVideoDao userDao = daoSession.getDBWishListVideoDao();
                video.setId(userDao.insert(video));
                Logger.d(TAG, "Inserted keyword: " + video.getName() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return video;
    }

    @Override
    public boolean existVideoAtWithList(Long id) {
        long count = 0;
        try {
            openReadableDb();
            DBWishListVideoDao userDao = daoSession.getDBWishListVideoDao();
            QueryBuilder<DBWishListVideo> queryBuilder = userDao.queryBuilder().where(DBWishListVideoDao.Properties.VideoId.eq(id));
            count = queryBuilder.count();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count > 0)
            return true;

        return false;
    }

    /**
     * Notifications
     *
     * @return
     */

    @Override
    public List<DBNotification> listNotification(int page) {
        List<DBNotification> notifications = null;
        int limit = 20;
        int offset = limit * page;
        try {
            openReadableDb();
            DBNotificationDao notificationDao = daoSession.getDBNotificationDao();
            QueryBuilder<DBNotification> queryBuilder = notificationDao.queryBuilder().orderDesc(DBNotificationDao.Properties.Id).limit(limit).offset(offset);
            notifications = queryBuilder.list();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (notifications == null)
            notifications = new ArrayList<>();
        return notifications;
    }

    @Override
    public void updateNotification(DBNotification notification) {
        try {
            if (notification != null) {
                openWritableDb();
                daoSession.update(notification);
                Logger.d(TAG, "Updated Notification: " + notification.getTitle() + " from the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DBNotification insertNotification(DBNotification notification) {
        try {
            if (notification != null) {
                openWritableDb();
                DBNotificationDao userDao = daoSession.getDBNotificationDao();
                long id = userDao.insert(notification);
                notification.setId(id);
                Log.d(TAG, "Inserted notification: " + notification.getTitle() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notification;
    }

    @Override
    public long getTotalNotificationNotView() {
        long total = 0;
        try {
            openReadableDb();
            DBNotificationDao notificationDao = daoSession.getDBNotificationDao();

            // QueryBuilder<DBNotification> queryBuilder = notificationDao.queryBuilder().where(DBNotificationDao.Properties.Status.eq(0));
            total = notificationDao.count();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    @Override
    public DBNotification getNotificationByID(long id) {
        DBNotification item = null;
        try {
            openReadableDb();
            DBNotificationDao notificationDao = daoSession.getDBNotificationDao();
            item = notificationDao.load(id);

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * Recent videos
     */

    @Override
    public void deleteVideoAtRecentListByVideoId(int id) {
        try {
            openWritableDb();
            DBRecentVideoDao userDao = daoSession.getDBRecentVideoDao();
            QueryBuilder<DBRecentVideo> queryBuilder = userDao.queryBuilder().where(DBRecentVideoDao.Properties.VideoId.eq(id));
            queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DBRecentVideo insertVideoToRecentList(DBRecentVideo video) {
        try {
            if (video != null) {
                openWritableDb();
                DBRecentVideoDao dbDao = daoSession.getDBRecentVideoDao();
                video.setId(dbDao.insert(video));
                Logger.d(TAG, "Inserted DBRecentVideo: " + video.getName() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return video;
    }

    @Override
    public List<DBRecentVideo> listVideoAtRecent(int userID) {
        List<DBRecentVideo> data = null;
        try {
            openReadableDb();
            DBRecentVideoDao dbDao = daoSession.getDBRecentVideoDao();

            QueryBuilder<DBRecentVideo> queryBuilder = dbDao.queryBuilder().where(DBRecentVideoDao.Properties.UserID.eq(userID)).orderDesc(DBRecentVideoDao.Properties.Id);
            data = queryBuilder.list();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data != null) {
            return new ArrayList<>(data);
        }
        return null;
    }

    @Override
    public boolean deleteVideoAtRecentList(Long id) {
        try {
            openWritableDb();
            DBRecentVideoDao userDao = daoSession.getDBRecentVideoDao();
            userDao.deleteByKey(id);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Download Videos
     */

    @Override
    public DBVideoDownload insertVideoToDownloadList(String path, String name) {
        DBVideoDownload video = new DBVideoDownload();
        video.setTitle(name);
        video.setPath(path);

        try {
            if (video != null) {
                openWritableDb();
                DBVideoDownloadDao dbDao = daoSession.getDBVideoDownloadDao();
                video.setId(dbDao.insert(video));
                Logger.d(TAG, "Inserted DBVideoDownload: " + video.getTitle() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return video;
    }

    @Override
    public List<DBVideoDownload> listVideoDownload(int page) {
        List<DBVideoDownload> notifications = null;
        int limit = 10;

        if (page < 1)
            page = 1;
        int offset = (page - 1) * limit;

        try {
            openReadableDb();
            DBVideoDownloadDao dbDao = daoSession.getDBVideoDownloadDao();
            QueryBuilder<DBVideoDownload> queryBuilder = dbDao.queryBuilder().orderDesc(DBVideoDownloadDao.Properties.Id).limit(limit).offset(offset);
            notifications = queryBuilder.list();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (notifications == null)
            notifications = new ArrayList<>();
        return notifications;
    }

    @Override
    public DBVideoDownload getVideoDownloadByVideoID(int videoID) {
        DBVideoDownload mVideo = null;
        try {
            openReadableDb();
            DBVideoDownloadDao dbDao = daoSession.getDBVideoDownloadDao();
            QueryBuilder<DBVideoDownload> queryBuilder = dbDao.queryBuilder().where(DBVideoDownloadDao.Properties.VideoId.eq(videoID)).limit(1);
            mVideo = queryBuilder.unique();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mVideo;
    }
}
