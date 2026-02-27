package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;

/* loaded from: classes4.dex */
public class AutoCompleteAdapter extends ArrayAdapter<String> {
    private ArrayList<String> data;
    private Context mContext;

    public AutoCompleteAdapter(Context context, int i) {
        super(context, i);
        this.data = new ArrayList<>();
        this.mContext = context;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public int getCount() {
        return this.data.size();
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public String getItem(int i) {
        try {
            return this.data.get(i);
        } catch (Exception unused) {
            return "";
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.thntech.cast68.screen.tab.webcast.AutoCompleteAdapter.1
            /* JADX WARN: Code restructure failed: missing block: B:42:0x00d3, code lost:
                r1.close();
             */
            /* JADX WARN: Code restructure failed: missing block: B:44:0x00d7, code lost:
                r7 = move-exception;
             */
            /* JADX WARN: Code restructure failed: missing block: B:45:0x00d8, code lost:
                r7.printStackTrace();
             */
            @Override // android.widget.Filter
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    InputStream inputStream = null;
                    try {
                        try {
                            if (!charSequence.toString().startsWith("http://") && !charSequence.toString().startsWith("https://") && !charSequence.toString().equals(AutoCompleteAdapter.this.mContext.getResources().getString(R.string.home))) {
                                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://suggestqueries.google.com/complete/search?client=firefox&q=" + charSequence.toString()).openConnection();
                                try {
                                    try {
                                        inputStream = httpURLConnection.getInputStream();
                                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8192);
                                        StringBuilder sb = new StringBuilder();
                                        while (true) {
                                            String readLine = bufferedReader.readLine();
                                            if (readLine == null) {
                                                break;
                                            }
                                            sb.append(readLine);
                                        }
                                        JSONArray jSONArray = new JSONArray(sb.toString()).getJSONArray(1);
                                        ArrayList arrayList = new ArrayList();
                                        for (int i = 0; i < jSONArray.length(); i++) {
                                            arrayList.add(jSONArray.getString(i));
                                        }
                                        filterResults.values = arrayList;
                                        filterResults.count = arrayList.size();
                                        AutoCompleteAdapter.this.data = arrayList;
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                        if (inputStream != null) {
                                            try {
                                                inputStream.close();
                                            } catch (Exception e3) {
                                                e3.printStackTrace();
                                            }
                                        }
                                        return filterResults;
                                    }
                                } catch (Throwable th) {
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (Exception e4) {
                                            e4.printStackTrace();
                                        }
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                    throw th;
                                }
                            }
                        } catch (Exception unused) {
                        }
                    } catch (Throwable th2) {
                        try {
                            throw th2;
                        } catch (Throwable th3) {
                            th3.printStackTrace();
                        }
                    }
                }
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults == null || filterResults.count <= 0) {
                    AutoCompleteAdapter.this.notifyDataSetInvalidated();
                } else {
                    AutoCompleteAdapter.this.notifyDataSetChanged();
                }
            }
        };
    }
}
