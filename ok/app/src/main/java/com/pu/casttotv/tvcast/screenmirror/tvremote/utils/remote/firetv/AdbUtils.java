package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv;

import com.tananaev.adblib.AdbCrypto;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class AdbUtils {
    public static AdbCrypto readCryptoConfig(File file) {
        File file2 = new File(file, "public.key");
        File file3 = new File(file, "private.key");
        if (file2.exists() && file3.exists()) {
            try {
                return AdbCrypto.loadAdbKeyPair(new AndroidBase64(), file3, file2);
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public static AdbCrypto writeNewCryptoConfig(File file) {
        File file2 = new File(file, "public.key");
        File file3 = new File(file, "private.key");
        try {
            AdbCrypto generateAdbKeyPair = AdbCrypto.generateAdbKeyPair(new AndroidBase64());
            generateAdbKeyPair.saveAdbKeyPair(file3, file2);
            return generateAdbKeyPair;
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean safeClose(Closeable closeable) {
        if (closeable == null) {
            return false;
        }
        try {
            closeable.close();
            return true;
        } catch (IOException unused) {
            return false;
        }
    }
}
