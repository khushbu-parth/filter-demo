package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class Constants {
    public static final String[] AUDIO_EXTENSIONS = {".mp3", ".m4a", ".wav"};
    public static final String[] BLOCKED_DOMAINS = {"youtube.com", "youtube.be"};
    public static final String[] CHECK_EXTENSIONS = {".mp4", ".3gp", ".mov", ".mpg", ".wmv", ".asf", ".mpeg", ".mp3", ".m4a", ".wav"};
    public static final String[] CHECK_VIDEO_EXTENSIONS = {".mp4", ".3gp", ".mov", ".mpg", ".wmv", ".asf", ".mpeg"};
    public static final String[] LINKS_OPENED_IN_EXTERNAL_BROWSER = {"target=external", "play.google.com/store", "youtube.com/watch"};
    public static final String[] LINKS_OPENED_IN_INTERNAL_WEBVIEW = {"target=webview", "target=internal", "target=blank", "target=_blank"};

    public static String EngineHome(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "https://google.com/" : "https://search.aol.com/" : "https://www.ask.com/" : "https://www.bing.com/" : "https://search.yahoo.com/web";
    }

    public static String SearchQueries(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "https://google.com/search?q=" : "https://search.aol.com/aol/search?q=" : "https://www.ask.com/web?q=" : "https://www.bing.com/search?q=" : "https://search.yahoo.com/search?p=";
    }

    public static String getFileNameFromURL(String str) {
        String replace = str.replace("%20", " ");
        try {
            String host = new URL(replace).getHost();
            if (host.length() > 0 && replace.endsWith(host)) {
                return "";
            }
            int lastIndexOf = replace.lastIndexOf(47) + 1;
            int length = replace.length();
            int lastIndexOf2 = replace.lastIndexOf(63);
            if (lastIndexOf2 == -1) {
                lastIndexOf2 = length;
            }
            int lastIndexOf3 = replace.lastIndexOf(35);
            if (lastIndexOf3 != -1) {
                length = lastIndexOf3;
            }
            return replace.substring(lastIndexOf, Math.min(lastIndexOf2, length));
        } catch (MalformedURLException unused) {
            return "";
        }
    }
}
