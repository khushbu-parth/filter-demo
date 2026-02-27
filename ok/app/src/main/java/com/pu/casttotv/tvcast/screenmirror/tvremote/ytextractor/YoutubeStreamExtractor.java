package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model.PlayerResponse;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model.Response;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model.StreamingData;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model.YTMedia;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model.YTSubtitles;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model.YoutubeMeta;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils.HTTPUtility;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils.LogUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils.RegexUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils.Utils;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.cookie.SM;

public class YoutubeStreamExtractor extends AsyncTask<String, Void, Void> {
    private ExtractorException Ex;
    Map<String, String> Headers = new HashMap();
    String PlayerBaseRegex = "(?<=PLAYER_JS_URL\":\").*?(?=\")";
    List<YTMedia> adaptiveMedia = new ArrayList();
    Handler han = new Handler(Looper.getMainLooper());
    ExtractorListner listener;
    List<YTMedia> muxedMedia = new ArrayList();
    PlayerResponse playerResponse;
    List<String> reasonUnavialable = Arrays.asList("This video is unavailable on this device.", "Content Warning", "who has blocked it on copyright grounds.");
    String regexFindReason = "(?<=(class=\"message\">)).*?(?=<)";
    String regexPageLink = "(http|https)://(www\\.|m.|)youtube\\.com/watch\\?v=(.+?)( |\\z|&)";
    String regexPlayerJson1 = "(?<=ytplayer.config\\s=).*?((\\}(\n|)\\}(\n|))|(\\}))(?=;)";
    String regexPlayerJson2 = "(?<=ytInitialPlayerResponse\\s=).*?(\\}(\\]|\\})\\})(?=;)";
    String regexUrl = "(?<=url=).*";
    String regexYtshortLink = "(http|https)://(www\\.|)youtu.be/.*";
    private Response response;
    private int selectedRegrexPlayerJson = 0;
    List<YTSubtitles> subtitle = new ArrayList();
    private YoutubeMeta ytmeta;

    public interface ExtractorListner {
        void onExtractionDone(List<YTMedia> list, List<YTMedia> list2, List<YTSubtitles> list3, YoutubeMeta youtubeMeta);

        void onExtractionGoesWrong(ExtractorException extractorException);
    }

    public YoutubeStreamExtractor(ExtractorListner extractorListner) {
        this.listener = extractorListner;
        this.Headers.put("Accept-Language", "en");
    }

    public YoutubeStreamExtractor setHeaders(Map<String, String> map) {
        this.Headers = map;
        return this;
    }

    public YoutubeStreamExtractor useDefaultLogin() {
        this.Headers.put(SM.COOKIE, Utils.loginCookie);
        return setHeaders(this.Headers);
    }

    public Map<String, String> getHeaders() {
        return this.Headers;
    }

    public void Extract(String str) {
        execute(str);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void r5) {
        ExtractorException extractorException = this.Ex;
        if (extractorException != null) {
            this.listener.onExtractionGoesWrong(extractorException);
        } else {
            this.listener.onExtractionDone(this.adaptiveMedia, this.muxedMedia, this.subtitle, this.ytmeta);
        }
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        this.Ex = null;
        this.adaptiveMedia.clear();
        this.muxedMedia.clear();
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        ExtractorException extractorException = this.Ex;
        if (extractorException != null) {
            this.listener.onExtractionGoesWrong(extractorException);
        }
    }

    /* access modifiers changed from: protected */
    public Void doInBackground(String[] strArr) {
        String extractVideoID = Utils.extractVideoID(strArr[0]);
        try {
            String downloadPageSource = HTTPUtility.downloadPageSource("https://www.youtube.com/watch?v=" + extractVideoID + "&has_verified=1&bpctr=9999999999", this.Headers);
            String parsePlayerConfig = parsePlayerConfig(downloadPageSource);
            int i = this.selectedRegrexPlayerJson;
            if (i == 1) {
                PlayerResponse parseJson1 = parseJson1(parsePlayerConfig);
                this.playerResponse = parseJson1;
                parseJson1.setPlayerJs(RegexUtils.matchGroup(this.PlayerBaseRegex, downloadPageSource));
            } else if (i == 2) {
                PlayerResponse parseJson2 = parseJson2(parsePlayerConfig);
                this.playerResponse = parseJson2;
                parseJson2.setPlayerJs(RegexUtils.matchGroup(this.PlayerBaseRegex, downloadPageSource));
            }
            this.ytmeta = this.playerResponse.getVideoDetails();
            this.subtitle = this.playerResponse.getCaptions() != null ? this.playerResponse.getCaptions().getPlayerCaptionsTracklistRenderer().getCaptionTracks() : null;
            if (this.playerResponse.getVideoDetails().getisLive()) {
                parseLiveUrls(this.playerResponse.getStreamingData());
            } else {
                StreamingData streamingData = this.playerResponse.getStreamingData();
                LogUtils.log("sizea= " + streamingData.getAdaptiveFormats().length);
                LogUtils.log("sizem= " + streamingData.getFormats().length);
                this.adaptiveMedia = parseUrls(streamingData.getAdaptiveFormats());
                this.muxedMedia = parseUrls(streamingData.getFormats());
                LogUtils.log("sizeXa= " + this.adaptiveMedia.size());
                LogUtils.log("sizeXm= " + this.muxedMedia.size());
            }
        } catch (Exception e2) {
            LogUtils.log(Arrays.toString(e2.getStackTrace()));
            this.Ex = new ExtractorException("Error While getting Youtube Data:" + e2.getMessage());
            cancel(true);
        }
        return null;
    }

    private PlayerResponse parseJson1(String str) throws Exception {
        this.response = (Response) new GsonBuilder().serializeNulls().create().fromJson(new JsonParser().parse(str), Response.class);
        return (PlayerResponse) new GsonBuilder().serializeNulls().create().fromJson(this.response.getArgs().getPlayerResponse(), PlayerResponse.class);
    }

    private PlayerResponse parseJson2(String str) throws Exception {
        return (PlayerResponse) new GsonBuilder().serializeNulls().create().fromJson(new JsonParser().parse(str), PlayerResponse.class);
    }

    private String parsePlayerConfig(String str) throws ExtractorException {
        if (Utils.isListContain(this.reasonUnavialable, RegexUtils.matchGroup(this.regexFindReason, str))) {
            throw new ExtractorException(RegexUtils.matchGroup(this.regexFindReason, str));
        } else if (RegexUtils.matchGroup(this.regexPlayerJson1, str) != null) {
            this.selectedRegrexPlayerJson = 1;
            return RegexUtils.matchGroup(this.regexPlayerJson1, str);
        } else if (RegexUtils.matchGroup(this.regexPlayerJson2, str) != null) {
            this.selectedRegrexPlayerJson = 2;
            return RegexUtils.matchGroup(this.regexPlayerJson2, str);
        } else {
            this.selectedRegrexPlayerJson = 3;
            throw new ExtractorException("This Video is unavialable");
        }
    }

    private List<YTMedia> parseUrls(YTMedia[] yTMediaArr) {
        String str;
        YTMedia[] yTMediaArr2 = yTMediaArr;
        String str2 = "url=";
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < yTMediaArr2.length) {
            try {
                YTMedia yTMedia = yTMediaArr2[i];
                LogUtils.log(yTMedia.getSignatureCipher() != null ? yTMedia.getSignatureCipher() : "null cip");
                if (yTMedia.useCipher()) {
                    String[] split = yTMedia.getSignatureCipher().split("&");
                    int length = split.length;
                    String str3 = "";
                    String str4 = str3;
                    int i2 = 0;
                    while (i2 < length) {
                        String str5 = split[i2];
                        if (str5.startsWith("s=")) {
                            str4 = CipherManager.dechiperSig(URLDecoder.decode(str5.replace("s=", "")), this.playerResponse.getPlayerJs());
                        }
                        if (str5.startsWith(str2)) {
                            str3 = URLDecoder.decode(str5.replace(str2, ""));
                            String[] split2 = str3.split("&");
                            int length2 = split2.length;
                            int i3 = 0;
                            while (i3 < length2) {
                                String str6 = split2[i3];
                                if (str6.startsWith("s=")) {
                                    str4 = CipherManager.dechiperSig(URLDecoder.decode(str6.replace("s=", "")), this.playerResponse.getPlayerJs());
                                }
                                i3++;
                                str2 = str2;
                            }
                        }
                        i2++;
                        str2 = str2;
                    }
                    str = str2;
                    yTMedia.setUrl(str3 + "&sig=" + str4);
                    arrayList.add(yTMedia);
                } else {
                    str = str2;
                    arrayList.add(yTMedia);
                }
                i++;
                yTMediaArr2 = yTMediaArr;
                str2 = str;
            } catch (Exception e2) {
                this.Ex = new ExtractorException(e2.getMessage());
                cancel(true);
            }
        }
        return arrayList;
    }

    private void parseLiveUrls(StreamingData streamingData) throws Exception {
        if (streamingData.getHlsManifestUrl() != null) {
            for (String str : RegexUtils.getAllMatches("(#EXT-X-STREAM-INF).*?(index.m3u8)", HTTPUtility.downloadPageSource(streamingData.getHlsManifestUrl()))) {
                YTMedia yTMedia = new YTMedia();
                String[] split = RegexUtils.matchGroup("(#).*?(?=https)", str).split(",");
                yTMedia.setUrl(RegexUtils.matchGroup("(https:).*?(index.m3u8)", str));
                for (String str2 : split) {
                    if (str2.startsWith("BANDWIDTH")) {
                        yTMedia.setBitrate(Integer.valueOf(str2.replace("BANDWIDTH=", "")).intValue());
                    }
                    if (str2.startsWith("CODECS")) {
                        yTMedia.setMimeType(str2.replace("CODECS=", "").replace("\"", ""));
                    }
                    if (str2.startsWith("FRAME-RATE")) {
                        yTMedia.setFps(Integer.valueOf(str2.replace("FRAME-RATE=", "")).intValue());
                    }
                    if (str2.startsWith("RESOLUTION")) {
                        String[] split2 = str2.replace("RESOLUTION=", "").split("x");
                        yTMedia.setWidth(Integer.valueOf(split2[0]).intValue());
                        yTMedia.setHeight(Integer.valueOf(split2[1]).intValue());
                        yTMedia.setQualityLabel(split2[1] + "p");
                    }
                }
                LogUtils.log(yTMedia.getUrl());
                this.muxedMedia.add(yTMedia);
            }
            return;
        }
        throw new ExtractorException("No link for hls video");
    }
}
