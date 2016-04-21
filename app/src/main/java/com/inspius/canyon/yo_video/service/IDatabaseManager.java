package com.inspius.canyon.yo_video.service;

/**
 * Created by Billy on 4/19/16.
 */


import com.inspius.canyon.yo_video.greendao.DBKeywordSearch;

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
     * Insert a user into the DB
     *
     * @param keyword to be inserted
     */
    DBKeywordSearch insertKeyword(DBKeywordSearch keyword);
    DBKeywordSearch insertKeyword(String keyword);

    /**
     * List all the users from the DB
     *
     * @return list of keywords
     */
    ArrayList<DBKeywordSearch> listKeyword();

    /**
     * Update a keyword from the DB
     *
     * @param keyword to be updated
     */
    void updateKeyword(DBKeywordSearch keyword);

    /**
     * Delete all Keywords with a certain email from the DB
     *
     * @param keyword of users to be deleted
     */
    List<DBKeywordSearch> getKeywordByName(String keyword);
    void deleteKeywordByName(String keyword);

    /**
     * Delete a Keyword with a certain id from the DB
     *
     * @param keywordId of users to be deleted
     */
    boolean deleteKeywordById(Long keywordId);

    /**
     * Delete all the keyword from the DB
     */
    void clearKeyword();
}
