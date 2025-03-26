package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.BookmarkItem;


public class DBBookmarkHelper extends SQLiteOpenHelper {
    private static final String BOOKMARK_TABLE = "bookmarktable";
    private static final String DBNAME = "bookmarks.db";
    private static final String KEY_DELETE = "key_delete";
    private static final String KEY_FAVICON = "key_favicon";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_TYPE = "key_type";
    private static final String KEY_URL = "key_url";
    private static final int VERSION = 1;
    private static final String _id = "_id";
    private static DBBookmarkHelper instance;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DBBookmarkHelper(Context context) {
        super(context, DBNAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public static synchronized DBBookmarkHelper getInstance(Context context) {
        DBBookmarkHelper dBBookmarkHelper;
        Class cls = DBBookmarkHelper.class;
        synchronized (cls) {
            synchronized (cls) {
                if (instance == null) {
                    instance = new DBBookmarkHelper(context);
                }
                dBBookmarkHelper = instance;
            }
        }
        return dBBookmarkHelper;
    }


    public void AddBookmark(BookmarkItem bookmarkItem) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, bookmarkItem.getName());
            contentValues.put(KEY_URL, bookmarkItem.getUrl());
            contentValues.put(KEY_FAVICON, bookmarkItem.getFavicon());
            contentValues.put(KEY_TYPE, Integer.valueOf(bookmarkItem.getType()));
            contentValues.put(KEY_DELETE, Integer.valueOf(bookmarkItem.getdelete()));
            writableDatabase.insert(BOOKMARK_TABLE, (String) null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteBookmark(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.delete(BOOKMARK_TABLE, "key_url=?", new String[]{String.valueOf(str)});
            writableDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
            writableDatabase.close();
        }
    }

    public void DeleteBookmarkById(int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.delete(BOOKMARK_TABLE, "_id=?", new String[]{String.valueOf(i)});
            writableDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
            writableDatabase.close();
        }
    }

    public BookmarkItem bm_retrieve(int i) {
        BookmarkItem bookmarkItem;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        BookmarkItem bookmarkItem2 = new BookmarkItem();
        try {
            if (!readableDatabase.isOpen()) {
                readableDatabase = getReadableDatabase();
            }
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM bookmarktable WHERE _id=?", new String[]{String.valueOf(i)});
            if (rawQuery != null && rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                do {
                    bookmarkItem = new BookmarkItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FAVICON)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_TYPE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DELETE)));
                    try {
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                            return bookmarkItem;
                        } catch (Exception e2) {
                            e = e2;
                            bookmarkItem2 = bookmarkItem;
                            e.printStackTrace();
                            return bookmarkItem2;
                        }
                    }
                } while (rawQuery.moveToNext());
                bookmarkItem2 = bookmarkItem;
            }
            if (rawQuery != null) {
                if (!rawQuery.isClosed()) {
                    rawQuery.close();
                }
            }
            return bookmarkItem2;
        } catch (Exception e3) {
            e3.printStackTrace();
            return bookmarkItem2;
        }
    }

    public boolean check_bookmark(String str) {
        try {
            Cursor rawQuery = getReadableDatabase().rawQuery("SELECT * FROM bookmarktable WHERE key_url=?", new String[]{str});
            if (rawQuery.getCount() > 0) {
                if (!rawQuery.isClosed()) {
                    rawQuery.close();
                }
                return true;
            }
            if (!rawQuery.isClosed()) {
                rawQuery.close();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BookmarkItem> getBookmarks() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM bookmarktable ORDER BY _id ASC ", (String[]) null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    rawQuery.moveToFirst();
                    do {
                        arrayList.add(new BookmarkItem(rawQuery.getInt(rawQuery.getColumnIndex("_id")), rawQuery.getString(rawQuery.getColumnIndex(KEY_NAME)), rawQuery.getString(rawQuery.getColumnIndex(KEY_URL)), rawQuery.getString(rawQuery.getColumnIndex(KEY_FAVICON)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_TYPE)), rawQuery.getInt(rawQuery.getColumnIndex(KEY_DELETE))));
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

    public int getTotalBookmarks() {
        try {
            Cursor rawQuery = getReadableDatabase().rawQuery("select count(*) from bookmarktable", (String[]) null);
            rawQuery.moveToFirst();
            int i = rawQuery.getInt(0);
            rawQuery.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE bookmarktable ( _id INTEGER PRIMARY KEY AUTOINCREMENT, key_name TEXT, key_url TEXT, key_favicon TEXT, key_type INT, key_delete INT)");
    }
}
