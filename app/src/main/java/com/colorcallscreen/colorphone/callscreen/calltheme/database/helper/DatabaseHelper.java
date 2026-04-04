package com.colorcallscreen.colorphone.callscreen.calltheme.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.BlockListTable;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.SpeakUpKeywords;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.UserImageTable;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.UserPhoneTable;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "boloCaller.db";
    private Context context;
    private Dao<BlockListTable, Integer> mBlockListDao;
    private Dao<SpeakUpKeywords, Integer> mSpeakUpKeywordsDao;
    private Dao<UserPhoneTable, Integer> mUserDao;
    private Dao<UserImageTable, Integer> mUserImageDao;

    @Override // com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
        this.mUserDao = null;
        this.mUserImageDao = null;
        this.mSpeakUpKeywordsDao = null;
        this.mBlockListDao = null;
        this.context = context;
    }

    @Override // com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper, android.database.sqlite.SQLiteOpenHelper, java.lang.AutoCloseable
    public void close() {
        this.mUserDao = null;
        this.context = null;
        this.mUserImageDao = null;
        super.close();
    }

    public Dao<BlockListTable, Integer> getBlockListDao() throws SQLException {
        if (this.mBlockListDao == null) {
            this.mBlockListDao = getDao(BlockListTable.class);
        }
        return this.mBlockListDao;
    }

    public Dao<SpeakUpKeywords, Integer> getSpeakUpKeywordsDao() throws SQLException {
        if (this.mSpeakUpKeywordsDao == null) {
            this.mSpeakUpKeywordsDao = getDao(SpeakUpKeywords.class);
        }
        return this.mSpeakUpKeywordsDao;
    }

    public Dao<UserImageTable, Integer> getUserImageDao() throws SQLException {
        if (this.mUserImageDao == null) {
            this.mUserImageDao = getDao(UserImageTable.class);
        }
        return this.mUserImageDao;
    }

    public Dao<UserPhoneTable, Integer> getUserPhoneDao() throws SQLException {
        if (this.mUserDao == null) {
            this.mUserDao = getDao(UserPhoneTable.class);
        }
        return this.mUserDao;
    }

    @Override // com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, UserImageTable.class);
            TableUtils.createTableIfNotExists(connectionSource, UserPhoneTable.class);
            TableUtils.createTableIfNotExists(connectionSource, SpeakUpKeywords.class);
            TableUtils.createTableIfNotExists(connectionSource, BlockListTable.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
