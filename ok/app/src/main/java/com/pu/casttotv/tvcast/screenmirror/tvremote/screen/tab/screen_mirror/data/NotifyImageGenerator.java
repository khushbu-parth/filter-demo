package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: classes4.dex */
public final class NotifyImageGenerator {
    private static Context mContext;
    private byte[] mCurrentDefaultScreen;
    private int mCurrentScreenSizeX;

    public NotifyImageGenerator(Context context) {
        mContext = context;
    }

    public void addDefaultScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                if (NotifyImageGenerator.this.mCurrentScreenSizeX != MyApplication.getAppData().getScreenSize().x) {
                    NotifyImageGenerator.this.mCurrentDefaultScreen = null;
                }
                if (NotifyImageGenerator.this.mCurrentDefaultScreen == null) {
                    NotifyImageGenerator.this.mCurrentDefaultScreen = NotifyImageGenerator.generateImage(NotifyImageGenerator.mContext.getString(R.string.image_generator_press), NotifyImageGenerator.mContext.getString(R.string.main_activity_start_stream).toUpperCase(), NotifyImageGenerator.mContext.getString(R.string.image_generator_on_device));
                    NotifyImageGenerator.this.mCurrentScreenSizeX = MyApplication.getAppData().getScreenSize().x;
                }
                if (NotifyImageGenerator.this.mCurrentDefaultScreen != null) {
                    MyApplication.getAppData().getImageQueue().add(NotifyImageGenerator.this.mCurrentDefaultScreen);
                }
            }
        }, 500L);
    }

    public static Bitmap getBitmapFromAsset() {
        try {
            return BitmapFactory.decodeStream(mContext.getAssets().open("mirror/image_mirror_web.png"));
        } catch (IOException unused) {
            return null;
        }
    }

    public byte[] getClientNotifyImage(String str) {
        if ("MESSAGE_ACTION_HTTP_RESTART".equals(str)) {
            return generateImage(mContext.getString(R.string.image_generator_settings_changed), "", mContext.getString(R.string.image_generator_go_to_new_address));
        }
        if (!"MESSAGE_ACTION_PIN_UPDATE".equals(str)) {
            return null;
        }
        return generateImage(mContext.getString(R.string.image_generator_settings_changed), "", mContext.getString(R.string.image_generator_reload_this_page));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] generateImage(String str, String str2, String str3) {
        Bitmap createBitmap = Bitmap.createBitmap(MyApplication.getAppData().getScreenSize().x, MyApplication.getAppData().getScreenSize().y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawRGB(255, 255, 255);
        new Rect();
        Paint paint = new Paint(1);
        canvas.drawBitmap(getBitmapFromAsset(), (createBitmap.getWidth() - 720.0f) / 2.0f, (createBitmap.getHeight() - 699.0f) / 2.0f, paint);
        byte[] bArr = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            createBitmap.compress(Bitmap.CompressFormat.JPEG, MyApplication.getAppPreference().getJpegQuality(), byteArrayOutputStream);
            bArr = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (IOException unused) {
        }
        createBitmap.recycle();
        return bArr;
    }
}
