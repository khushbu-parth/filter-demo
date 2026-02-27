package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes4.dex */
public class AdBlocker implements Serializable {
    private String easylistLastModified;
    private List<String> filters = new ArrayList();

    public void update(final Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences("settings", 0);
        final String format = new SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(new Date());
        if (!format.equals(sharedPreferences.getString("adFiltersLasUpdated", ""))) {
            new Thread() { // from class: com.thntech.cast68.screen.tab.webcast.AdBlocker.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    ArrayList arrayList = new ArrayList();
                    try {
                        URLConnection openConnection = new URL("https://easylist.to/easylist/easylist.txt").openConnection();
                        if (openConnection != null) {
                            InputStream inputStream = openConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            while (true) {
                                String readLine = bufferedReader.readLine();
                                if (readLine == null) {
                                    if (!arrayList.isEmpty()) {
                                        AdBlocker.this.filters = arrayList;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("updating ads filters complete. Total: ");
                                        sb.append(AdBlocker.this.filters.size());
                                    }
                                    FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "ad_filters.dat"));
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                    objectOutputStream.writeObject(AdBlocker.this);
                                    objectOutputStream.close();
                                    fileOutputStream.close();
                                } else if (readLine.contains("Last modified")) {
                                    if (!readLine.equals(AdBlocker.this.easylistLastModified)) {
                                        AdBlocker.this.easylistLastModified = readLine;
                                    } else {
                                        bufferedReader.close();
                                        inputStream.close();
                                        return;
                                    }
                                } else if (!readLine.startsWith("!") || !readLine.startsWith("[")) {
                                    arrayList.add(readLine);
                                }
                            }
                        } else {
                            sharedPreferences.edit().putString("adFiltersLasUpdated", format).apply();
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public boolean checkThroughFilters(String str) {
        if (str != null) {
            for (String str2 : this.filters) {
                if (str.contains(str2.replace("||", "//"))) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("checkThroughFilters: ");
                    sb.append(str2);
                    sb.append(" ");
                    sb.append(str);
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
