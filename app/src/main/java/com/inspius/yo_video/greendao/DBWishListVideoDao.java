package com.inspius.yo_video.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.inspius.yo_video.greendao.DBWishListVideo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBWISH_LIST_VIDEO".
*/
public class DBWishListVideoDao extends AbstractDao<DBWishListVideo, Long> {

    public static final String TABLENAME = "DBWISH_LIST_VIDEO";

    /**
     * Properties of entity DBWishListVideo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property VideoId = new Property(1, int.class, "videoId", false, "VIDEO_ID");
        public final static Property Category = new Property(2, String.class, "category", false, "CATEGORY");
        public final static Property Series = new Property(3, String.class, "series", false, "SERIES");
        public final static Property View = new Property(4, String.class, "view", false, "VIEW");
        public final static Property Image = new Property(5, String.class, "image", false, "IMAGE");
        public final static Property Link = new Property(6, String.class, "link", false, "LINK");
        public final static Property Name = new Property(7, String.class, "name", false, "NAME");
        public final static Property UserID = new Property(8, int.class, "userID", false, "USER_ID");
    };


    public DBWishListVideoDao(DaoConfig config) {
        super(config);
    }
    
    public DBWishListVideoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBWISH_LIST_VIDEO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"VIDEO_ID\" INTEGER NOT NULL ," + // 1: videoId
                "\"CATEGORY\" TEXT NOT NULL ," + // 2: category
                "\"SERIES\" TEXT NOT NULL ," + // 3: series
                "\"VIEW\" TEXT NOT NULL ," + // 4: view
                "\"IMAGE\" TEXT NOT NULL ," + // 5: image
                "\"LINK\" TEXT NOT NULL ," + // 6: link
                "\"NAME\" TEXT NOT NULL ," + // 7: name
                "\"USER_ID\" INTEGER NOT NULL );"); // 8: userID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBWISH_LIST_VIDEO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBWishListVideo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getVideoId());
        stmt.bindString(3, entity.getCategory());
        stmt.bindString(4, entity.getSeries());
        stmt.bindString(5, entity.getView());
        stmt.bindString(6, entity.getImage());
        stmt.bindString(7, entity.getLink());
        stmt.bindString(8, entity.getName());
        stmt.bindLong(9, entity.getUserID());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DBWishListVideo readEntity(Cursor cursor, int offset) {
        DBWishListVideo entity = new DBWishListVideo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // videoId
            cursor.getString(offset + 2), // category
            cursor.getString(offset + 3), // series
            cursor.getString(offset + 4), // view
            cursor.getString(offset + 5), // image
            cursor.getString(offset + 6), // link
            cursor.getString(offset + 7), // name
            cursor.getInt(offset + 8) // userID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBWishListVideo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setVideoId(cursor.getInt(offset + 1));
        entity.setCategory(cursor.getString(offset + 2));
        entity.setSeries(cursor.getString(offset + 3));
        entity.setView(cursor.getString(offset + 4));
        entity.setImage(cursor.getString(offset + 5));
        entity.setLink(cursor.getString(offset + 6));
        entity.setName(cursor.getString(offset + 7));
        entity.setUserID(cursor.getInt(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBWishListVideo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBWishListVideo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}