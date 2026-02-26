package com.lib.screening;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cast.tv.screen.mirroring.screencasting.R;
import com.lib.screening.listener.OnRequestMediaProjectionResultCallback;

public class RequestMediaProjectionActivity extends AppCompatActivity {
    static OnRequestMediaProjectionResultCallback resultCallback;
    private MediaProjectionManager mMediaProjectionManager;

    public static void safedk_Context_startActivity_97cb3195734cf5c9cc3418feeafa6dd6(Context p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    public static void safedk_RequestMediaProjectionActivity_startActivityForResult_09162f8e9c7458a500da31a1eb829fa5(RequestMediaProjectionActivity p0, Intent p1, int p2) {
        if (p1 == null) {
            return;
        }
        p0.startActivityForResult(p1, p2);
    }

    public static void start(Context context) {
        safedk_Context_startActivity_97cb3195734cf5c9cc3418feeafa6dd6(context, new Intent(context, RequestMediaProjectionActivity.class).addFlags(268435456));
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_request_media_projection);
        Window window = getWindow();
        window.setGravity(51);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        attributes.width = 1;
        attributes.height = 1;
        window.setAttributes(attributes);
        this.mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
    }

    @Override
    public void onStart() {
        super.onStart();
        requestMediaProjection();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1012) {
            MediaProjection mediaProjection = this.mMediaProjectionManager.getMediaProjection(i2, intent);
            if (mediaProjection == null) {
                Toast.makeText(this, "你拒绝了录屏操作！", 0).show();
            } else {
                OnRequestMediaProjectionResultCallback onRequestMediaProjectionResultCallback = resultCallback;
                if (onRequestMediaProjectionResultCallback != null) {
                    onRequestMediaProjectionResultCallback.onMediaProjectionResult(mediaProjection);
                }
            }
            finish();
        }
    }

    @Override
    public void onDestroy() {
        resultCallback = null;
        super.onDestroy();
    }

    private void requestMediaProjection() {
        safedk_RequestMediaProjectionActivity_startActivityForResult_09162f8e9c7458a500da31a1eb829fa5(this, this.mMediaProjectionManager.createScreenCaptureIntent(), 1012);
    }
}
