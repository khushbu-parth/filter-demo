package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.squareup.okhttp.HttpUrl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import okio.BufferedSource;
import okio.Okio;

public class AdBlocker {
    private static final Set<String> AD_HOSTS = new HashSet();
    private static final String AD_HOSTS_FILE = "ads_urls.rtf";

    public static WebResourceResponse createEmptyResource() {
        return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
    }

    public static void init(final Context context) {
        new AsyncTask<Void, Void, Void>() {

            public Void doInBackground(Void... voidArr) {
                AdBlocker.loadFromAssets(context);
                return null;
            }
        }.execute(new Void[0]);
    }

    public static boolean isAd(String str) {
        HttpUrl parse = HttpUrl.parse(str);
        return isAdHost(parse != null ? parse.host() : "");
    }

    private static boolean isAdHost(String str) {
        int indexOf;
        int i;
        if (TextUtils.isEmpty(str) || (indexOf = str.indexOf(".")) < 0) {
            return false;
        }
        if (AD_HOSTS.contains(str) || ((i = indexOf + 1) < str.length() && isAdHost(str.substring(i)))) {
            return true;
        }
        return false;
    }


    public static void loadFromAssets(Context context) {
        try {
            InputStream open = context.getAssets().open(AD_HOSTS_FILE);
            BufferedSource buffer = Okio.buffer(Okio.source(open));
            while (true) {
                String readUtf8Line = buffer.readUtf8Line();
                if (readUtf8Line != null) {
                    AD_HOSTS.add(readUtf8Line);
                } else {
                    buffer.close();
                    open.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
