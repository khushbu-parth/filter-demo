package com.colorcallscreen.colorphone.callscreen.calltheme.service.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.core.content.ContextCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.UserImageTable;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.UserPhoneTable;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.helper.DatabaseHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;


public class UsersImageHandler {
    private static DatabaseHelper databaseHelper;

    private static void storeUpdateImageToServer(byte[] bArr, String str, Context context) {
    }

    private static DatabaseHelper databaseHelper(Context context) {
        DatabaseHelper databaseHelper2 = databaseHelper;
        if (databaseHelper2 != null) {
            return databaseHelper2;
        }
        DatabaseHelper databaseHelper3 = new DatabaseHelper(context);
        databaseHelper = databaseHelper3;
        return databaseHelper3;
    }

    public static void handleImageForNameAndNotification(String str, StatusBarNotification statusBarNotification, Context context) {
        List<String> contactNumberFromName;
        try {
            if (ContextCompat.checkSelfPermission(context, BoloPermission.READ_CONTACTS) == 0 && (contactNumberFromName = ContactsHandler.contactNumberFromName(str, context)) != null && !contactNumberFromName.isEmpty() && statusBarNotification.getNotification().largeIcon != null) {
                UserImageTable userImageTable = null;
                for (String str2 : contactNumberFromName) {
                    String replaceAll = str2.replaceAll("[^\\d.]", "");
                    UserPhoneTable userPhoneForPhoneNumber = userPhoneForPhoneNumber(replaceAll, context);
                    if (userPhoneForPhoneNumber == null) {
                        UserPhoneTable storePhoneNumbers = storePhoneNumbers(replaceAll, userImageTable, statusBarNotification.getNotification().largeIcon, context, str2);
                        if (storePhoneNumbers != null) {
                            userImageTable = storePhoneNumbers.getUserImage();
                        }
                    } else if (userPhoneForPhoneNumber.getUserImage() == null || userPhoneForPhoneNumber.getUserImage().getImage() == null || userPhoneForPhoneNumber.getUserImage().getCreatedOn().getTime() < Utility.dateAfterTime(-5, 10).getTime()) {
                        userImageTable = storeCreateUserImage(statusBarNotification.getNotification().largeIcon, userPhoneForPhoneNumber.getUserImage(), context);
                        if (userPhoneForPhoneNumber.getUserImage() == null) {
                            userPhoneForPhoneNumber.setUserImage(userImageTable);
                            try {
                                databaseHelper(context).getUserPhoneDao().createOrUpdate(userPhoneForPhoneNumber);
                                storeUpdateImageToServer(userPhoneForPhoneNumber.getUserImage().getImage(), str2, context);
                            } catch (Exception unused) {
                            }
                        }
                    }
                }
            }
        } catch (Exception unused2) {
        }
        databaseHelper = null;
    }

    private static UserImageTable storeCreateUserImage(Bitmap bitmap, UserImageTable userImageTable, Context context) {
        try {
            Dao<UserImageTable, Integer> userImageDao = databaseHelper(context).getUserImageDao();
            if (userImageTable == null) {
                userImageTable = new UserImageTable();
            }
            userImageTable.setImage(Utility.bitmapToByte(bitmap));
            userImageDao.createOrUpdate(userImageTable);
            return userImageTable;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static UserPhoneTable storePhoneNumbers(String str, UserImageTable userImageTable, Bitmap bitmap, Context context, String str2) throws SQLException {
        if (userImageTable == null) {
            try {
                userImageTable = storeCreateUserImage(bitmap, userImageTable, context);
            } catch (Exception unused) {
                return null;
            }
        }
        UserPhoneTable userPhoneTable = new UserPhoneTable();
        userPhoneTable.setPhoneNumber(str);
        userPhoneTable.setUserImage(userImageTable);
        databaseHelper(context).getUserPhoneDao().createOrUpdate(userPhoneTable);
        storeUpdateImageToServer(userPhoneTable.getUserImage().getImage(), str2, context);
        return userPhoneTable;
    }

    public static Bitmap userImageForPhoneNumber(String str, Context context) {
        Bitmap contactPhotoFromNumber;
        BoloApplication application = BoloApplication.getApplication();
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        UserPhoneTable userPhoneForPhoneNumber = userPhoneForPhoneNumber(str.replaceAll("[^\\d.]", ""), application);
        if (userPhoneForPhoneNumber != null && userPhoneForPhoneNumber.getUserImage() != null && userPhoneForPhoneNumber.getUserImage().getImage() != null) {
            return BitmapFactory.decodeByteArray(userPhoneForPhoneNumber.getUserImage().getImage(), 0, userPhoneForPhoneNumber.getUserImage().getImage().length);
        }
        if (ContextCompat.checkSelfPermission(application, BoloPermission.READ_CONTACTS) != 0 || (contactPhotoFromNumber = ContactsHandler.contactPhotoFromNumber(str, application)) == null) {
            return null;
        }
        return contactPhotoFromNumber;
    }

    private static UserPhoneTable userPhoneForPhoneNumber(String str, Context context) {
        try {
            if (str.length() > 8) {
                str = str.substring(str.length() - 8);
            }
            Dao<UserPhoneTable, Integer> userPhoneDao = new DatabaseHelper(context).getUserPhoneDao();
            UserPhoneTable queryForFirst = userPhoneDao.queryForFirst(userPhoneDao.queryBuilder().where().like("phoneNumber", "%" + str + "%").prepare());
            Log.e("", "heee");
            return queryForFirst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
