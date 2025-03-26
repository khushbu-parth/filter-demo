package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.Constants;

public final class DownloadFileUtility {
    private DownloadFileUtility() {
    }

    public static boolean check_audio_extension(String str) {
        String lowerCase = str.toLowerCase();
        for (String contains : Constants.AUDIO_EXTENSIONS) {
            if (lowerCase.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public static boolean check_domains(String str) {
        String lowerCase = str.toLowerCase();
        for (String contains : Constants.BLOCKED_DOMAINS) {
            if (lowerCase.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public static boolean check_extension(String str) {
        String lowerCase = str.toLowerCase();
        for (String contains : Constants.CHECK_EXTENSIONS) {
            if (lowerCase.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public static boolean check_video_extension(String str) {
        String lowerCase = str.toLowerCase();
        for (String contains : Constants.CHECK_VIDEO_EXTENSIONS) {
            if (lowerCase.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public static String isCheckUrl(String str) {
        if (str.matches(".*.3gp.*")) {
            return ".3gp";
        }
        if (str.matches(".*.mp4.*")) {
            return ".mp4";
        }
        if (str.matches(".*.flac.*")) {
            return ".flac";
        }
        if (str.matches(".*.wmv.*")) {
            return ".wmv";
        }
        if (str.matches(".*.mpg.*")) {
            return ".mpg";
        }
        if (str.matches(".*.flv.*")) {
            return ".flv";
        }
        if (str.matches(".*.mkv.*")) {
            return ".mkv";
        }
        if (str.matches(".*.swf.*")) {
            return ".swf";
        }
        if (str.matches(".*.mov.*")) {
            return ".mov";
        }
        if (str.matches(".*.mp3.*")) {
            return ".mp3";
        }
        if (str.matches(".*.m4a.*")) {
            return ".m4a";
        }
        return str.matches(".*.wav.*") ? ".wav" : "novideo";
    }
}
