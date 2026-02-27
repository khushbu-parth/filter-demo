package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes4.dex */
public class Utils {
    public static String removeBackSlash(String str) {
        return str.replaceAll("\\\\", "");
    }

    public static String formatFileSize(long j) {
        double d = j;
        double d2 = d / 1024.0d;
        double d3 = d2 / 1024.0d;
        double d4 = d3 / 1024.0d;
        double d5 = d4 / 1024.0d;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (d5 > 1.0d) {
            return decimalFormat.format(d5).concat(" TB");
        }
        if (d4 > 1.0d) {
            return decimalFormat.format(d4).concat(" GB");
        }
        if (d3 > 1.0d) {
            return decimalFormat.format(d3).concat(" MB");
        }
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" KB");
        }
        return decimalFormat.format(d).concat(" Bytes");
    }

    public static void disableSSLCertificateChecking() {
        TrustManager[] trustManagerArr = {new X509TrustManager() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.Utils.1
            @Override // javax.net.ssl.X509TrustManager
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            @Override // javax.net.ssl.X509TrustManager
            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            @Override // javax.net.ssl.X509TrustManager
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, trustManagerArr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.Utils.2
                @Override // javax.net.ssl.HostnameVerifier
                public boolean verify(String str, SSLSession sSLSession) {
                    return true;
                }
            });
        } catch (KeyManagementException | NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
    }

    public static int getTypeBrowser(String str) {
        if (str.contains("facebook")) {
            return 0;
        }
        if (str.contains("tiktok")) {
            return 1;
        }
        if (str.contains("buzzvideo")) {
            return 2;
        }
        if (str.contains("google")) {
            return 3;
        }
        if (str.contains("twitter")) {
            return 4;
        }
        if (str.contains("instagram")) {
            return 5;
        }
        if (str.contains("imdb")) {
            return 6;
        }
        if (str.contains("twitch")) {
            return 7;
        }
        if (str.contains("fox")) {
            return 8;
        }
        if (str.contains("livestream")) {
            return 9;
        }
        if (str.contains("espn")) {
            return 10;
        }
        return str.contains("dailymotion") ? 11 : 3;
    }
}
