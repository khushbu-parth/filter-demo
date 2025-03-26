package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;


public class DBDownloadManager extends SQLiteOpenHelper {
    private static final String BOOKMARK_TABLE = "bookmark";
    private static final String DBNAME = "appshopinc.db";
    private static final String DOWNLOAD_QUE_TABLE = "queDownloadFileDetails";
    private static final String DOWNLOAD_TABLE = "downloadFileDetails";
    private static final String KEY_CURRENT_SIZE = "key_current_size";
    private static final String KEY_DOWNLOAD_ID = "key_down_id";
    private static final String KEY_DOWNLOAD_PERCENT = "key_percent";
    private static final String KEY_DOWNLOAD_PROGRESS = "key_progress";
    private static final String KEY_DOWNLOAD_SPEED = "key_speed";
    private static final String KEY_FAVICON = "key_favicon";
    private static final String KEY_FILE_LOCATION = "key_file_location";
    private static final String KEY_ICON = "key_icon";
    private static final String KEY_IS_IN_PAUSE = "key_is_in_pause";
    private static final String KEY_LAST_MODIFICATION = "key_last_modification";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_TOTAL_SIZE = "key_total_size";
    private static final String KEY_URL = "key_url";
    private static final int VERSION = 1;
    private static final String _id = "_id";
    private static DBDownloadManager instance;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DBDownloadManager(Context context) {
        super(context, DBNAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public static synchronized DBDownloadManager getInstance(Context context) {
        DBDownloadManager dBDownloadManager;
        Class cls = DBDownloadManager.class;
        synchronized (cls) {
            synchronized (cls) {
                if (instance == null) {
                    instance = new DBDownloadManager(context);
                }
                dBDownloadManager = instance;
            }
        }
        return dBDownloadManager;
    }

    public void addUpdateDownloadData(int i, DownloadingItem downloadingItem, boolean z) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, downloadingItem.getName());
            contentValues.put(KEY_ICON, downloadingItem.getIcon());
            contentValues.put(KEY_URL, downloadingItem.getUrl());
            contentValues.put(KEY_DOWNLOAD_ID, Integer.valueOf(downloadingItem.getDownloadId()));
            contentValues.put(KEY_TOTAL_SIZE, Double.valueOf(downloadingItem.getTotalSize()));
            contentValues.put(KEY_CURRENT_SIZE, Double.valueOf(downloadingItem.getCurrentSize()));
            contentValues.put(KEY_DOWNLOAD_PERCENT, Integer.valueOf(downloadingItem.getPercent()));
            contentValues.put(KEY_DOWNLOAD_PROGRESS, Integer.valueOf(downloadingItem.getProgress()));
            contentValues.put(KEY_DOWNLOAD_SPEED, Integer.valueOf(downloadingItem.getSpeed()));
            contentValues.put(KEY_IS_IN_PAUSE, Integer.valueOf(downloadingItem.getIsInPause()));
            contentValues.put(KEY_FILE_LOCATION, downloadingItem.getLocalFilePath());
            contentValues.put(KEY_LAST_MODIFICATION, downloadingItem.getLastModification());
            if (z) {
                writableDatabase.update(DOWNLOAD_TABLE, contentValues, "key_down_id=?", new String[]{String.valueOf(i)});
                return;
            }
            writableDatabase.insert(DOWNLOAD_TABLE, (String) null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DownloadingItem db_retrieve(int i) {
        DownloadingItem downloadingItem;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        DownloadingItem downloadingItem2 = new DownloadingItem();
        try {
            if (!readableDatabase.isOpen()) {
                readableDatabase = getReadableDatabase();
            }
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloadFileDetails WHERE _id=?", new String[]{String.valueOf(i)});
            if (rawQuery != null && rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                do {
                    downloadingItem = new DownloadingItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_ICON)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_ID)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_TOTAL_SIZE)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_CURRENT_SIZE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PERCENT)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PROGRESS)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_SPEED)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_IS_IN_PAUSE)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_LOCATION)), rawQuery.getString(rawQuery.getColumnIndex(KEY_LAST_MODIFICATION)));
                    try {
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                            return downloadingItem;
                        } catch (Exception e2) {
                            e = e2;
                            downloadingItem2 = downloadingItem;
                            e.printStackTrace();
                            return downloadingItem2;
                        }
                    }
                } while (rawQuery.moveToNext());
                downloadingItem2 = downloadingItem;
            }
            if (rawQuery != null) {
                if (!rawQuery.isClosed()) {
                    rawQuery.close();
                }
            }
            return downloadingItem2;
        } catch (Exception e3) {
            e3.printStackTrace();
            return downloadingItem2;
        }
    }

    public void deleteDownloadData(int i) {
        try {
            getWritableDatabase().delete(DOWNLOAD_TABLE, "key_down_id=?", new String[]{String.valueOf(i)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete_query(int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.delete(DOWNLOAD_TABLE, "_id=?", new String[]{String.valueOf(i)});
            writableDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
            writableDatabase.close();
        }
    }

    public List<DownloadingItem> getActiveDownloadingData() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloadFileDetails WHERE key_down_id!=0 AND key_is_in_pause==0", (String[]) null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    rawQuery.moveToFirst();
                    do {
                        arrayList.add(new DownloadingItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_ICON)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_ID)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_TOTAL_SIZE)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_CURRENT_SIZE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PERCENT)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PROGRESS)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_SPEED)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_IS_IN_PAUSE)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_LOCATION)), rawQuery.getString(rawQuery.getColumnIndex(KEY_LAST_MODIFICATION))));
                    } while (rawQuery.moveToNext());
                    rawQuery.close();
                    return arrayList;
                }
            }
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public int getCurrentDownloadingCount() {
        try {
            Cursor rawQuery = getReadableDatabase().rawQuery("select count(*) from downloadFileDetails where key_down_id!=0 and key_is_in_pause==0", (String[]) null);
            rawQuery.moveToFirst();
            int i = rawQuery.getInt(0);
            rawQuery.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<DownloadingItem> getDownloadedData() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloadFileDetails WHERE key_down_id=0 ORDER BY key_last_modification DESC ", (String[]) null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    rawQuery.moveToFirst();
                    do {
                        arrayList.add(new DownloadingItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_ICON)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_ID)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_TOTAL_SIZE)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_CURRENT_SIZE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PERCENT)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PROGRESS)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_SPEED)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_IS_IN_PAUSE)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_LOCATION)), rawQuery.getString(rawQuery.getColumnIndex(KEY_LAST_MODIFICATION))));
                    } while (rawQuery.moveToNext());
                    rawQuery.close();
                    return arrayList;
                }
            }
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public List<DownloadingItem> getDownloadingData() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloadFileDetails WHERE key_down_id!=0 ORDER BY _id DESC ", (String[]) null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    rawQuery.moveToFirst();
                    do {
                        arrayList.add(new DownloadingItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_ICON)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_ID)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_TOTAL_SIZE)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_CURRENT_SIZE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PERCENT)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PROGRESS)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_SPEED)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_IS_IN_PAUSE)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_LOCATION)), rawQuery.getString(rawQuery.getColumnIndex(KEY_LAST_MODIFICATION))));
                    } while (rawQuery.moveToNext());
                    rawQuery.close();
                    return arrayList;
                }
            }
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public DownloadingItem getDownloadingDataById(int i) {
        DownloadingItem downloadingItem;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        DownloadingItem downloadingItem2 = new DownloadingItem();
        try {
            if (!readableDatabase.isOpen()) {
                readableDatabase = getReadableDatabase();
            }
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloadFileDetails WHERE key_down_id=?", new String[]{String.valueOf(i)});
            if (rawQuery != null && rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                do {
                    downloadingItem = new DownloadingItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_ICON)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_ID)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_TOTAL_SIZE)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_CURRENT_SIZE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PERCENT)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PROGRESS)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_SPEED)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_IS_IN_PAUSE)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_LOCATION)), rawQuery.getString(rawQuery.getColumnIndex(KEY_LAST_MODIFICATION)));
                    try {
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                            return downloadingItem;
                        } catch (Exception e2) {
                            e = e2;
                            downloadingItem2 = downloadingItem;
                            e.printStackTrace();
                            return downloadingItem2;
                        }
                    }
                } while (rawQuery.moveToNext());
                downloadingItem2 = downloadingItem;
            }
            if (rawQuery != null) {
                if (!rawQuery.isClosed()) {
                    rawQuery.close();
                }
            }
            return downloadingItem2;
        } catch (Exception e3) {
            e3.printStackTrace();
            return downloadingItem2;
        }
    }

    public List<DownloadingItem> getQuedDownloadData() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloadFileDetails WHERE key_is_in_pause=2", (String[]) null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    rawQuery.moveToFirst();
                    do {
                        arrayList.add(new DownloadingItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_ICON)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_ID)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_TOTAL_SIZE)), (double) rawQuery.getInt(rawQuery.getColumnIndex(KEY_CURRENT_SIZE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PERCENT)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_PROGRESS)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DOWNLOAD_SPEED)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_IS_IN_PAUSE)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_LOCATION)), rawQuery.getString(rawQuery.getColumnIndex(KEY_LAST_MODIFICATION))));
                    } while (rawQuery.moveToNext());
                    rawQuery.close();
                    return arrayList;
                }
            }
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public boolean hasObject(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloadFileDetails WHERE key_name=?", new String[]{str});
        boolean moveToFirst = rawQuery.moveToFirst();
        rawQuery.close();
        readableDatabase.close();
        return !moveToFirst;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE if not exists downloadFileDetails ( _id INTEGER PRIMARY KEY AUTOINCREMENT, key_name TEXT, key_icon TEXT, key_url TEXT, key_down_id NUMERIC, key_total_size NUMERIC, key_current_size NUMERIC, key_percent NUMERIC, key_progress NUMERIC, key_speed NUMERIC, key_is_in_pause TEXT, key_file_location TEXT, key_last_modification TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE if not exists queDownloadFileDetails ( _id INTEGER PRIMARY KEY AUTOINCREMENT, key_name TEXT, key_icon TEXT, key_url TEXT, key_down_id NUMERIC, key_total_size NUMERIC, key_current_size NUMERIC, key_percent NUMERIC, key_progress NUMERIC, key_speed NUMERIC, key_is_in_pause TEXT, key_file_location TEXT, key_last_modification TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE bookmark ( _id INTEGER PRIMARY KEY AUTOINCREMENT, key_name TEXT, key_url TEXT, key_favicon TEXT)");
    }
}
