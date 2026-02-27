package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv;

import android.util.Base64;
import com.tananaev.adblib.AdbBase64;

public class AndroidBase64 implements AdbBase64 {
    @Override // com.tananaev.adblib.AdbBase64
    public String encodeToString(byte[] bArr) {
        return Base64.encodeToString(bArr, 2);
    }
}
