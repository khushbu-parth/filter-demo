package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import com.j256.ormlite.field.FieldType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ContactsHandler {
    public static String contactNameFromNumber(String str, Context context) {
        String str2;
        if (str != null) {
            try {
                if (str.trim().isEmpty()) {
                    return null;
                }
                Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name"}, null, null, null);
                str2 = "";
                if (query != null) {
                    str2 = query.moveToFirst() ? query.getString(0) : "";
                    query.close();
                }
                return str2;
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }

    public static List<String> contactNumberFromName(String str, Context context) {
        ArrayList arrayList;
        try {
            Cursor query = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"data1"}, "display_name ='" + str + "'", null, null);
            if (query.moveToFirst()) {
                arrayList = new ArrayList();
                if (query.getString(0) != null) {
                    arrayList.add(query.getString(0));
                }
            } else {
                arrayList = null;
            }
            while (query.moveToNext()) {
                if (query.getString(0) != null) {
                    arrayList.add(query.getString(0));
                }
            }
            return arrayList;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Bitmap contactPhotoFromNumber(String str, Context context) {
        InputStream openContactPhotoInputStream;
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name", FieldType.FOREIGN_ID_FIELD_SUFFIX}, null, null, null);
            String str2 = null;
            if (query != null) {
                while (query.moveToNext()) {
                    str2 = query.getString(query.getColumnIndexOrThrow(FieldType.FOREIGN_ID_FIELD_SUFFIX));
                }
                query.close();
            }
            if (str2 != null && (openContactPhotoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(str2).longValue()))) != null) {
                Bitmap decodeStream = BitmapFactory.decodeStream(openContactPhotoInputStream);
                try {
                    openContactPhotoInputStream.close();
                } catch (IOException unused) {
                }
                return decodeStream;
            }
        } catch (Exception unused2) {
        }
        return null;
    }
}
