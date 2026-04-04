package com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallLogModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;
import com.j256.ormlite.field.FieldType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class CallLogComponent {
    private static CallLogComponent instance;
    private CallLogsView callLogsView;
    private Context context;
    private boolean isInProcess = false;
    public List<String> listId = new ArrayList();

    public CallLogComponent(Context context, CallLogsView callLogsView) {
        this.context = context;
        if (callLogsView != null) {
            this.callLogsView = callLogsView;
        }
        if (instance == null) {
            instance = this;
        }
    }

    public Uri getUri(String str) {
        for (String str2 : this.listId) {
            if (str2.length() > 9 && str2.substring(str2.length() - 9, str2.length() - 6).equals(str)) {
                return Uri.parse(str2);
            }
        }
        return null;
    }

    public static CallLogComponent getInstance() {
        if (instance == null) {
            instance = new CallLogComponent(BoloApplication.getApplication(), null);
        }
        return instance;
    }

    public static void removeAllRecents(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme2);
        builder.setMessage(R.string.clear_history);
        builder.setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent.1.1

                    @Override 
                    public void run() {
                        final Cursor query = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
                        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent.1.1.1

                            @Override 
                            public void run() {
                                Cursor cursor = query;
                                if (cursor != null) {
                                    int columnIndex = cursor.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX);
                                    while (query.moveToNext()) {
                                        context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, "_id = ? ", new String[]{query.getString(columnIndex)});
                                    }
                                    query.close();
                                }
                                ((MainActivity) context).historyFragment.onClear();
                            }
                        });
                    }
                }).start();

            }
        });
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent$1 */
    
    public class AnonymousClass1 implements DialogInterface.OnClickListener {
        final /* synthetic */ Context val$context;

        AnonymousClass1(Context context) {
            this.val$context = context;
        }

        @Override 
        public void onClick(DialogInterface dialogInterface, int i) {
            new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent.1.1

                @Override 
                public void run() {
                    final Cursor query = AnonymousClass1.this.val$context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent.1.1.1

                        @Override 
                        public void run() {
                            Cursor cursor = query;
                            if (cursor != null) {
                                int columnIndex = cursor.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX);
                                while (query.moveToNext()) {
                                    AnonymousClass1.this.val$context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, "_id = ? ", new String[]{query.getString(columnIndex)});
                                }
                                query.close();
                            }
                            ((MainActivity) AnonymousClass1.this.val$context).historyFragment.onClear();
                        }
                    });
                }
            }).start();
        }
    }

    public void loadCallLogs(final String str, final long j, final long j2, final boolean z, final int i, final int i2) {
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent.2

            @Override 
            public void run() {
                final List<CallLogModel> queryForCallLogs = CallLogComponent.this.queryForCallLogs(str, j, j2, z, i, i2);
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent.2.1
                    @Override 
                    public void run() {
                        CallLogComponent.this.callLogsView.onCallLogLoaded(queryForCallLogs);
                    }
                });
            }
        }).start();
    }

    public List<CallLogModel> queryForCallLogs(String str, long j, long j2, boolean z, int i, int i2) {
        String str2;
        Cursor query;
        int i3;
        String str3;
        if (this.isInProcess) {
            return new ArrayList();
        }
        Bundle bundle = new Bundle();
        if (ContextCompat.checkSelfPermission(BoloApplication.getApplication(), BoloPermission.READ_CALL_LOG) != 0) {
            return new ArrayList();
        }
        this.isInProcess = true;
        HashSet hashSet = new HashSet();
        hashSet.addAll(new HashSet());
        String[] strArr = {FieldType.FOREIGN_ID_FIELD_SUFFIX, "number", "name", "type", "date", "is_read", "duration", "subscription_id", "photo_uri"};
        if (!z) {
            boolean z2 = false;
            if (i > -1) {
                String str4 = ")";
                if (Build.VERSION.SDK_INT >= 29) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putStringArray("android:query-arg-sort-columns", new String[]{"date"});
                    bundle2.putInt("android:query-arg-sort-direction", 1);
                    int i4 = i * 340;
                    Cursor query2 = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, bundle2, null);
                    ArrayList arrayList = new ArrayList();
                    if (query2 != null) {
                        int columnIndex = query2.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX);
                        int columnIndex2 = query2.getColumnIndex("number");
                        int columnIndex3 = query2.getColumnIndex("name");
                        int columnIndex4 = query2.getColumnIndex("type");
                        int columnIndex5 = query2.getColumnIndex("date");
                        int columnIndex6 = query2.getColumnIndex("is_read");
                        int columnIndex7 = query2.getColumnIndex("duration");
                        int columnIndex8 = query2.getColumnIndex("subscription_id");
                        int columnIndex9 = query2.getColumnIndex("photo_uri");
                        this.listId.clear();
                        if (i4 > -1) {
                            query2.move(i4);
                        }
                        while (query2.moveToNext()) {
                            String string = query2.getString(columnIndex3);
                            String string2 = query2.getString(columnIndex);
                            String string3 = query2.getString(columnIndex2);
                            int i5 = columnIndex;
                            String string4 = query2.getString(columnIndex4);
                            int i6 = columnIndex2;
                            String string5 = query2.getString(columnIndex5);
                            int i7 = columnIndex5;
                            int i8 = query2.getInt(columnIndex6);
                            int i9 = columnIndex6;
                            String string6 = query2.getString(columnIndex7);
                            int i10 = columnIndex7;
                            String string7 = query2.getString(columnIndex8);
                            int i11 = columnIndex8;
                            String string8 = query2.getString(columnIndex9);
                            Cursor cursor = query2;
                            if (string8 != null && !this.listId.contains(string8)) {
                                this.listId.add(string8);
                            }
                            CallLogModel callLogModel = new CallLogModel();
                            callLogModel.setCallId(string2);
                            callLogModel.setNumber(string3);
                            if (string != null && !string.isEmpty()) {
                                callLogModel.setName(string);
                            } else {
                                callLogModel.setName(string3);
                                callLogModel.searchForNameForNumber();
                            }
                            callLogModel.setDisplayName(callLogModel.getName());
                            callLogModel.setCallType(string4);
                            callLogModel.setDate(string5);
                            callLogModel.setIsRead(i8);
                            callLogModel.setDuration(string6);
                            callLogModel.setSimName(string7);
                            callLogModel.setImgUri(string8);
                            if (Integer.parseInt(string4) == i2) {
                                if (arrayList.size() > 0 && str == null) {
                                    CallLogModel callLogModel2 = (CallLogModel) arrayList.get(arrayList.size() - 1);
                                    if (callLogModel2.getNumber() != null) {
                                        i3 = columnIndex4;
                                        if (callLogModel2.getNumber().equals(callLogModel.getNumber())) {
                                            callLogModel2.setSameNumberCount(callLogModel2.getSameNumberCount() + 1);
                                            str3 = str4;
                                            callLogModel2.setDisplayName(callLogModel2.getName() + " (" + callLogModel2.getSameNumberCount() + str3);
                                            callLogModel2.setDate(callLogModel.getDate());
                                            callLogModel2.setCallType(string4);
                                            callLogModel2.setDate(string5);
                                            callLogModel2.setIsRead(i8);
                                            callLogModel2.setDuration(string6);
                                            callLogModel2.setSimName(string7);
                                            callLogModel2.setImgUri(string8);
                                            arrayList.set(arrayList.size() - 1, callLogModel2);
                                        } else {
                                            str3 = str4;
                                            arrayList.add(callLogModel);
                                        }
                                    } else {
                                        i3 = columnIndex4;
                                        str3 = str4;
                                    }
                                }
                                i3 = columnIndex4;
                                str3 = str4;
                                arrayList.add(callLogModel);
                            } else {
                                i3 = columnIndex4;
                                str3 = str4;
                                if (i2 == 0) {
                                    if (arrayList.size() > 0 && str == null) {
                                        CallLogModel callLogModel3 = (CallLogModel) arrayList.get(arrayList.size() - 1);
                                        if (callLogModel3.getNumber() != null) {
                                            if (callLogModel3.getNumber().equals(callLogModel.getNumber())) {
                                                callLogModel3.setSameNumberCount(callLogModel3.getSameNumberCount() + 1);
                                                callLogModel3.setDisplayName(callLogModel3.getName() + " (" + callLogModel3.getSameNumberCount() + str3);
                                                callLogModel3.setDate(callLogModel.getDate());
                                                callLogModel3.setCallType(String.valueOf(callLogModel3.getCallType()));
                                                callLogModel3.setDate(string5);
                                                callLogModel3.setIsRead(i8);
                                                callLogModel3.setDuration(string6);
                                                callLogModel3.setSimName(string7);
                                                callLogModel3.setImgUri(string8);
                                                arrayList.set(arrayList.size() - 1, callLogModel3);
                                            } else {
                                                arrayList.add(callLogModel);
                                            }
                                        }
                                    } else {
                                        arrayList.add(callLogModel);
                                    }
                                }
                            }
                            columnIndex = i5;
                            columnIndex2 = i6;
                            columnIndex5 = i7;
                            columnIndex6 = i9;
                            columnIndex7 = i10;
                            columnIndex8 = i11;
                            str4 = str3;
                            query2 = cursor;
                            columnIndex4 = i3;
                            z2 = false;
                        }
                    }
                    this.isInProcess = false;
                    return arrayList;
                }
                query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, null, null, "date DESC LIMIT " + (i * 340) + " , 340");
            } else if (str == null) {
                Log.e("notInData", "(" + TextUtils.join(",", hashSet) + ")");
                if (hashSet.size() == 0) {
                    if (Build.VERSION.SDK_INT >= 29) {
                        Bundle bundle3 = new Bundle();
                        bundle3.putStringArray("android:query-arg-sort-columns", new String[]{"date"});
                        bundle3.putInt("android:query-arg-sort-direction", 1);
                        this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, bundle3, null);
                        ArrayList arrayList2 = new ArrayList();
                        this.isInProcess = false;
                        return arrayList2;
                    }
                    query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, null, null, "date DESC LIMIT 150");
                } else if (Build.VERSION.SDK_INT >= 29) {
                    Bundle bundle4 = new Bundle();
                    bundle4.putStringArray("android:query-arg-sort-columns", new String[]{"date"});
                    bundle4.putInt("android:query-arg-sort-direction", 1);
                    query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, bundle4, null);
                } else {
                    query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, null, null, "date DESC ");
                }
            } else {
                String replace = str.replace(" ", "").replace("+", "").replace("-", "").replace("/", "");
                if (replace.length() > 7) {
                    replace = "%" + replace.substring(replace.length() - 7);
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("android:query-arg-sql-selection", "date BETWEEN ? AND ? AND REPLACE(REPLACE(REPLACE(REPLACE(number ,' ' , ''),'-',''),'+',''),'/','') like ?");
                    str2 = "subscription_id";
                    bundle5.putStringArray("android:query-arg-sql-selection-args", new String[]{String.valueOf(j), String.valueOf(j2), replace});
                    bundle5.putStringArray("android:query-arg-sort-columns", new String[]{"date"});
                    bundle5.putInt("android:query-arg-sort-direction", 1);
                    query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, bundle5, null);
                } else {
                    str2 = "subscription_id";
                    query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, "date BETWEEN ? AND ? AND REPLACE(REPLACE(REPLACE(REPLACE(number ,' ' , ''),'-',''),'+',''),'/','') like ?", new String[]{String.valueOf(j), String.valueOf(j2), replace}, "date DESC");
                }
                ArrayList arrayList3 = new ArrayList();
                if (query != null) {
                    int columnIndex10 = query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX);
                    int columnIndex11 = query.getColumnIndex("number");
                    int columnIndex12 = query.getColumnIndex("name");
                    int columnIndex13 = query.getColumnIndex("type");
                    int columnIndex14 = query.getColumnIndex("date");
                    int columnIndex15 = query.getColumnIndex("is_read");
                    int columnIndex16 = query.getColumnIndex("duration");
                    int columnIndex17 = query.getColumnIndex(str2);
                    int columnIndex18 = query.getColumnIndex("photo_uri");
                    while (query.moveToNext()) {
                        String string9 = query.getString(columnIndex10);
                        String string10 = query.getString(columnIndex11);
                        String string11 = query.getString(columnIndex12);
                        String string12 = query.getString(columnIndex13);
                        int i12 = columnIndex10;
                        String string13 = query.getString(columnIndex14);
                        int i13 = columnIndex11;
                        int i14 = query.getInt(columnIndex15);
                        int i15 = columnIndex12;
                        String string14 = query.getString(columnIndex16);
                        int i16 = columnIndex14;
                        String string15 = query.getString(columnIndex17);
                        int i17 = columnIndex13;
                        String string16 = query.getString(columnIndex18);
                        Cursor cursor2 = query;
                        CallLogModel callLogModel4 = new CallLogModel();
                        callLogModel4.setCallId(string9);
                        callLogModel4.setNumber(string10);
                        if (string11 != null && !string11.isEmpty()) {
                            callLogModel4.setName(string11);
                        } else {
                            callLogModel4.setName(string11);
                            callLogModel4.searchForNameForNumber();
                        }
                        callLogModel4.setDisplayName(callLogModel4.getName());
                        callLogModel4.setCallType(string12);
                        callLogModel4.setDate(string13);
                        callLogModel4.setIsRead(i14);
                        callLogModel4.setDuration(string14);
                        callLogModel4.setSimName(string15);
                        callLogModel4.setImgUri(string16);
                        if (Integer.parseInt(string12) == i2) {
                            arrayList3.add(callLogModel4);
                        } else if (i2 == 0) {
                            arrayList3.add(callLogModel4);
                        }
                        columnIndex10 = i12;
                        columnIndex11 = i13;
                        columnIndex12 = i15;
                        columnIndex14 = i16;
                        columnIndex13 = i17;
                        query = cursor2;
                    }
                }
                this.isInProcess = false;
                return arrayList3;
            }
        } else if (Build.VERSION.SDK_INT >= 29) {
            bundle.putString("android:query-arg-sql-selection", "date BETWEEN ? AND ? ");
            bundle.putStringArray("android:query-arg-sql-selection-args", new String[]{String.valueOf(j), String.valueOf(j2)});
            query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, bundle, null);
        } else {
            query = this.context.getContentResolver().query(CallLog.Calls.CONTENT_URI, strArr, "date BETWEEN ? AND ? ", new String[]{String.valueOf(j), String.valueOf(j2)}, "date ASC");
        }
        str2 = "subscription_id";
        ArrayList arrayList32 = new ArrayList();
        if (query != null) {
        }
        this.isInProcess = false;
        return arrayList32;
    }
}
