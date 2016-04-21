package com.inspius.canyon.yo_video.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


import com.inspius.canyon.yo_video.greendao.DBKeywordSearch;
import com.inspius.canyon.yo_video.greendao.DBKeywordSearchDao;
import com.inspius.canyon.yo_video.greendao.DaoMaster;
import com.inspius.canyon.yo_video.greendao.DaoSession;
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

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link Context}.
     */
    public DatabaseManager(final Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(this.context, "-database", null);
        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
    }

    /**
     * @param context The Android {@link Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {

        if (instance == null) {
            instance = new DatabaseManager(context);
        }

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
//            asyncSession.deleteAll(DBUserDetails.class);
//            asyncSession.deleteAll(DBPhoneNumber.class);
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
            if (userDao.loadAll().size() > 1000)
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
}
