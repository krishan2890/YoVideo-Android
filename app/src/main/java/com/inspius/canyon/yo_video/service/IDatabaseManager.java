package com.inspius.canyon.yo_video.service;

/**
 * Created by Billy on 4/19/16.
 */


import com.inspius.canyon.yo_video.greendao.DBKeywordSearch;
import com.inspius.canyon.yo_video.greendao.DBNotification;
import com.inspius.canyon.yo_video.greendao.DBRecentVideo;
import com.inspius.canyon.yo_video.greendao.NewWishList;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author Octa
 */
public interface IDatabaseManager {

    /**
     * Closing available connections
     */
    void closeDbConnections();

    /**
     * Delete all tables and content from our database
     */
    void dropDatabase();

    /**
     * Keyword
     *
     * @param keyword
     * @return
     */
    DBKeywordSearch insertKeyword(DBKeywordSearch keyword);

    DBKeywordSearch insertKeyword(String keyword);

    ArrayList<DBKeywordSearch> listKeyword();

    void updateKeyword(DBKeywordSearch keyword);

    List<DBKeywordSearch> getKeywordByName(String keyword);

    void deleteKeywordByName(String keyword);

    boolean deleteKeywordById(Long keywordId);

    void clearKeyword();

    /**
     * WishList
     *
     * @param userID
     * @return
     */
    List<NewWishList> listVideoAtWishList(int userID);

    boolean deleteVideoAtWishList(Long id);

    void deleteVideoAtWishListByVideoId(Long id);

    void deleteVideoAtWishList(NewWishList course);

    NewWishList insertVideoToWishList(NewWishList course);

    boolean existVideoAtWithList(Long id);

    /**
     * Notifications
     */

    List<DBNotification> listNotification(int page);

    void updateNotification(DBNotification notification);

    DBNotification insertNotification(DBNotification notification);

    long getTotalNotificationNotView();

    DBNotification getNotificationByID(long id);

    /**
     * Recent Videos
     */
    void deleteVideoAtRecentListByVideoId(int id);
    DBRecentVideo insertVideoToRecentList(DBRecentVideo video);
    List<DBRecentVideo> listVideoAtRecent(int userID);
    boolean deleteVideoAtRecentList(Long id);
}
