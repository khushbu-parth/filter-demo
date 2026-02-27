package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.google.android.gms.cast.HlsSegmentFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public abstract class ContentSearch extends Thread {
    private Context context;
    private int numLinksInspected = 0;
    private String page;
    private String title;
    private String url;

    public abstract void onAudioFound(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2);

    public abstract void onFinishedInspectingURL(boolean z);

    public abstract void onImageFound(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2);

    public abstract void onStartInspectingURL();

    public abstract void onVideoFound(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2);

    public ContentSearch(Context context, String str, String str2, String str3) {
        this.context = context;
        this.url = str;
        this.page = str2;
        this.title = str3;
    }

    private String getUrlWithoutParameters(String str) throws URISyntaxException {
        URI uri = new URI(str);
        return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment()).toString();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean z;
        boolean z2;
        boolean z3;
        String headerField;
        String str;
        String lowerCase = this.url.toLowerCase();
        String[] stringArray = this.context.getResources().getStringArray(R.array.videourl_filters);
        String[] stringArray2 = this.context.getResources().getStringArray(R.array.imageurl_filters);
        String[] stringArray3 = this.context.getResources().getStringArray(R.array.audiourl_filters);
        int length = stringArray.length;
        boolean z4 = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            }
            if (lowerCase.contains(stringArray[i])) {
                try {
                    String urlWithoutParameters = getUrlWithoutParameters(lowerCase);
                    if (!urlWithoutParameters.endsWith(".js") && !urlWithoutParameters.endsWith(".css") && !urlWithoutParameters.endsWith(".svg") && !urlWithoutParameters.endsWith(".ts")) {
                        z = true;
                        break;
                    }
                } catch (URISyntaxException e2) {
                    e2.printStackTrace();
                }
            }
            i++;
        }
        boolean z5 = (!z || !URLAddFilter.IsContainsAdURL(this.context, this.url)) ? z : false;
        int length2 = stringArray2.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length2) {
                z2 = false;
                break;
            } else if (lowerCase.contains(stringArray2[i2])) {
                z2 = true;
                break;
            } else {
                i2++;
            }
        }
        int length3 = stringArray3.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length3) {
                z3 = false;
                break;
            } else if (lowerCase.contains(stringArray3[i3])) {
                z3 = true;
                break;
            } else {
                i3++;
            }
        }
        if (z2) {
            addImageToList(this.url, this.page, this.title, "image");
        }
        if (z3) {
            addAudioToList(this.url, this.page, this.title, "audio");
        }
        if (z5) {
            this.numLinksInspected++;
            onStartInspectingURL();
            URLConnection uRLConnection = null;
            try {
                uRLConnection = new URL(this.url).openConnection();
                uRLConnection.connect();
            } catch (IOException e3) {
                e3.printStackTrace();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            if (uRLConnection != null && (headerField = uRLConnection.getHeaderField("content-type")) != null) {
                String lowerCase2 = headerField.toLowerCase();
                if (lowerCase2.contains("video") || lowerCase2.contains("audio")) {
                    addVideoToList(uRLConnection, this.page, this.title, lowerCase2);
                } else if (lowerCase2.equals("application/octet-stream") || lowerCase2.equals("application/mp4") || lowerCase2.equals("video/mp4")) {
                    addVideoToList(uRLConnection, this.page, this.title, lowerCase2);
                } else if (lowerCase2.equals("application/x-mpegurl") || lowerCase2.equals("application/vnd.apple.mpegurl") || lowerCase2.equals("application/x-mpegURL; charset=UTF-8")) {
                    try {
                        str = new URL(this.page).getHost();
                    } catch (MalformedURLException e5) {
                        e5.printStackTrace();
                        str = "";
                    }
                    if (!str.contains("twitter.com") && !str.contains("metacafe.com") && !str.contains("myspace.com")) {
                        addVideosToListFromM3U8_Direct(uRLConnection, this.page, this.title);
                    }
                    addVideosToListFromM3U8(uRLConnection, this.page, this.title);
                } else if (lowerCase2.equals("binary/octet-stream")) {
                    addVideosToListFromM3U8_Direct(uRLConnection, this.page, this.title);
                }
            }
            int i4 = this.numLinksInspected - 1;
            this.numLinksInspected = i4;
            if (i4 <= 0) {
                z4 = true;
            }
            onFinishedInspectingURL(z4);
        }
    }

    private void addImageToList(String str, String str2, String str3, String str4) {
        onImageFound(null, "image", str, str3, str2, false, null, false);
    }

    private void addAudioToList(String str, String str2, String str3, String str4) {
        onAudioFound(null, str3, str, "image", str2, false, null, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x01a1 A[Catch: IOException | JSONException -> 0x025b, TryCatch #0 {IOException | JSONException -> 0x025b, blocks: (B:3:0x0016, B:5:0x0022, B:6:0x002a, B:9:0x0040, B:13:0x004f, B:22:0x0073, B:24:0x0079, B:26:0x008a, B:29:0x0093, B:68:0x017f, B:70:0x0198, B:72:0x01a1, B:73:0x01a6, B:75:0x01b8, B:77:0x01be, B:78:0x01f1, B:80:0x01f8, B:90:0x024a, B:81:0x020e, B:83:0x0217, B:84:0x0229, B:86:0x022f, B:31:0x00a3, B:33:0x00a9, B:35:0x00b1, B:36:0x00bd, B:38:0x00c5, B:41:0x00ce, B:43:0x00dc, B:44:0x00f1, B:45:0x011e, B:49:0x0140, B:66:0x0171, B:54:0x0151, B:57:0x015c, B:18:0x005b, B:20:0x0061), top: B:94:0x0016 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void addVideoToList(URLConnection uRLConnection, String str, String str2, String str3) {
        String str4;
        String str5 = null;
        String str6;
        CharSequence charSequence;
        int lastIndexOf;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12 = "youtube.com";
        uRLConnection.getURL().toString();
        try {
            String headerField = uRLConnection.getHeaderField("content-length");
            String headerField2 = uRLConnection.getHeaderField("Location");
            if (headerField2 == null) {
                headerField2 = uRLConnection.getURL().toString();
            }
            String str13 = headerField2;
            String host = new URL(str).getHost();
            if (host.contains("twitter.com") && str3.equals("video/mp2t")) {
                return;
            }
            if (str2 == null) {
                str5 = str3.contains("audio") ? "audio" : "video";
            } else if (str3.contains("audio")) {
                str5 = "[AUDIO ONLY]" + str2;
            } else {
                str4 = str2;
                if (!host.contains(str12) || new URL(str13).getHost().contains("googlevideo.com")) {
                    str6 = "[AUDIO ONLY]";
                    charSequence = "audio";
                } else {
                    if (host.contains("dailymotion.com")) {
                        str11 = str13.replaceAll("(frag\\()+(\\d+)+(\\))", "FRAGMENT");
                        str10 = "dailymotion.com";
                    } else if (host.contains("vimeo.com") && str13.endsWith("m4s")) {
                        str11 = str13.replaceAll("(segment-)+(\\d+)", "SEGMENT");
                        str10 = "vimeo.com";
                    } else {
                        if (host.contains("facebook.com") && str13.contains("bytestart")) {
                            int lastIndexOf2 = str13.lastIndexOf("&bytestart");
                            int indexOf = str13.indexOf("fbcdn");
                            if (lastIndexOf2 > 0) {
                                str13 = "https://video.xx." + str13.substring(indexOf, lastIndexOf2);
                            }
                            URLConnection openConnection = new URL(str13).openConnection();
                            openConnection.connect();
                            str10 = null;
                            String headerField3 = openConnection.getHeaderField("content-length");
                            str3.hashCode();
                            str6 = "[AUDIO ONLY]";
                            charSequence = "audio";
                            onVideoFound(headerField3, "mp4", str13, str4, str, false, null, false);
                            str11 = str13;
                            str3.hashCode();
                            onVideoFound(null, "mp4", str11, str4, str, true, str10, false);
                            str12 = str10;
                        }
                        str6 = "[AUDIO ONLY]";
                        charSequence = "audio";
                        if (host.contains("instagram.com")) {
                            try {
                                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(str13, new HashMap());
                                mediaMetadataRetriever.release();
                            } catch (RuntimeException unused) {
                            }
                        }
                        str10 = null;
                        char c = 65535;
                        int hashCode = str3.hashCode();
                        if (hashCode == -1662384007) {
                            if (str3.equals("video/mp2t")) {
                                c = 2;
                            }
                        } else if (hashCode == -1662095187 && str3.equals("video/webm")) {
                            c = 1;
                        }
                        onVideoFound(headerField, (c == 0 || c == 1 || c != 2) ? "mp4" : HlsSegmentFormat.TS, str13, str4, str, false, null, false);
                        str11 = str13;
                        str3.hashCode();
                        onVideoFound(null, "mp4", str11, str4, str, true, str10, false);
                        str12 = str10;
                    }
                    str6 = "[AUDIO ONLY]";
                    charSequence = "audio";
                    str3.hashCode();
                    onVideoFound(null, "mp4", str11, str4, str, true, str10, false);
                    str12 = str10;
                }
                lastIndexOf = str13.lastIndexOf("&range");
                if (lastIndexOf > 0) {
                    str13 = str13.substring(0, lastIndexOf);
                }
                URLConnection openConnection2 = new URL(str13).openConnection();
                openConnection2.connect();
                String headerField4 = openConnection2.getHeaderField("content-length");
                if (str12 != null || !host.contains(str12)) {
                    str7 = null;
                    str8 = str4;
                } else {
                    InputStreamReader inputStreamReader = new InputStreamReader(new URL("http://www.youtube.com/oembed?url=" + str + "&format=json").openStream(), Charset.defaultCharset());
                    StringBuilder sb = new StringBuilder();
                    char[] cArr = new char[1024];
                    String str14 = str4;
                    while (true) {
                        int read = inputStreamReader.read(cArr);
                        if (read == -1) {
                            break;
                        }
                        sb.append(cArr, 0, read);
                        if (!str3.contains("video")) {
                            str14 = "[VIDEO ONLY]" + str14;
                        } else if (str3.contains(charSequence)) {
                            StringBuilder sb2 = new StringBuilder();
                            str9 = str6;
                            sb2.append(str9);
                            sb2.append(str14);
                            str14 = sb2.toString();
                            str6 = str9;
                        }
                        str9 = str6;
                        str6 = str9;
                    }
                    String string = new JSONObject(sb.toString()).getString("title");
                    str3.contains("video");
                    str8 = string;
                    str7 = str12;
                }
                str3.hashCode();
                onVideoFound(headerField4, "mp4", str13, str8, str, false, str7, false);
            }
            str4 = str5;
            if (!host.contains(str12)) {
            }
            str6 = "[AUDIO ONLY]";
            charSequence = "audio";
            lastIndexOf = str13.lastIndexOf("&range");
            if (lastIndexOf > 0) {
            }
            URLConnection openConnection22 = new URL(str13).openConnection();
            openConnection22.connect();
            String headerField42 = openConnection22.getHeaderField("content-length");
            if (str12 != null) {
            }
            str7 = null;
            str8 = str4;
            str3.hashCode();
            onVideoFound(headerField42, "mp4", str13, str8, str, false, str7, false);
        } catch (IOException | JSONException unused2) {
        }
    }

    private void addVideosToListFromM3U8_Direct(URLConnection uRLConnection, String str, String str2) {
        uRLConnection.getURL().toString();
        String headerField = uRLConnection.getHeaderField("Location");
        if (headerField == null) {
            headerField = uRLConnection.getURL().toString();
        }
        onVideoFound(null, "m3u8", headerField, "Video", str, true, null, false);
    }

    private void addVideosToListFromM3U8(URLConnection uRLConnection, String str, String str2) {
        uRLConnection.getURL().toString();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            String host = new URL(str).getHost();
            if (!host.contains("twitter.com") && !host.contains("metacafe.com") && !host.contains("myspace.com")) {
                return;
            }
            if (str2 == null) {
                str2 = "video";
            }
            if (!host.contains("twitter.com")) {
                if (host.contains("metacafe.com")) {
                    String url = uRLConnection.getURL().toString();
                    url.substring(0, url.lastIndexOf("/") + 1);
                } else if (host.contains("myspace.com")) {
                    onVideoFound(null, HlsSegmentFormat.TS, uRLConnection.getURL().toString(), str2, str, true, "myspace.com", false);
                    return;
                } else {
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            return;
                        }
                        if (readLine.endsWith(".m3u8")) {
                            onVideoFound(null, null, ((String) null) + readLine, str2, str, true, null, false);
                        }
                    }
                }
            }
            while (true) {
                bufferedReader.readLine();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
