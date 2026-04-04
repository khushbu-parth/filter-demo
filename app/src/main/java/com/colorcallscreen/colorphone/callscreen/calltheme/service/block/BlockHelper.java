package com.colorcallscreen.colorphone.callscreen.calltheme.service.block;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BlockedNumberContract;
import android.widget.Toast;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ContactModel_Favorites;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;
import com.j256.ormlite.field.FieldType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class BlockHelper {
    public static void addToBlockList(String str, Context context) {
        BoloApplication application = BoloApplication.getApplication();
        ContentValues contentValues = new ContentValues();
        contentValues.put("original_number", str);
        context.getContentResolver().insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, contentValues);
        try {
            Toast.makeText(application, str + " " + application.getResources().getString(R.string.added_to_block_list), 0).show();
        } catch (Exception unused) {
        }
    }

    public static void removeFromBlockList(String str, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("original_number", str);
        context.getContentResolver().delete(context.getContentResolver().insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, contentValues), null, null);
        try {
            Toast.makeText(context, context.getResources().getString(R.string.remove_to_block_list), 0).show();
        } catch (Exception unused) {
        }
    }

    public static boolean isPhoneNumberInBlockedList(String str, Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(listOfAllBlockedList(context));
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((ContactModel_Favorites) it.next()).getNumber().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static List<ContactModel_Favorites> listOfAllBlockedList(Context context) {
        ArrayList arrayList = new ArrayList();
        Cursor query = context.getContentResolver().query(BlockedNumberContract.BlockedNumbers.CONTENT_URI, new String[]{FieldType.FOREIGN_ID_FIELD_SUFFIX, "original_number", "e164_number"}, null, null, null);
        if ((query != null ? query.getCount() : 0) > 0) {
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex("original_number"));
                String string2 = query.getString(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX));
                query.getString(query.getColumnIndex("e164_number"));
                if (string != null && !string.isEmpty()) {
                    String contactNameFromNumber = ContactsHandler.contactNameFromNumber(string, context);
                    ContactModel_Favorites contactModel_Favorites = new ContactModel_Favorites();
                    contactModel_Favorites.setId(string2);
                    contactModel_Favorites.setName(contactNameFromNumber);
                    contactModel_Favorites.setNumber(string);
                    arrayList.add(contactModel_Favorites);
                }
            }
        }
        Collections.reverse(arrayList);
        if (query != null) {
            query.close();
        }
        return arrayList;
    }
}
