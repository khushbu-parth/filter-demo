package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

public class CornerTransform implements Transformation<Bitmap> {
    private boolean exceptLeftBottom;
    private boolean exceptLeftTop;
    private boolean exceptRightBotoom;
    private boolean exceptRightTop;
    private BitmapPool mBitmapPool;
    private float radius;

    public CornerTransform(Context context, float f) {
        this.mBitmapPool = Glide.get(context).getBitmapPool();
        this.radius = f;
    }

    public void setExceptCorner(boolean z, boolean z2, boolean z3, boolean z4) {
        this.exceptLeftTop = z;
        this.exceptRightTop = z2;
        this.exceptLeftBottom = z3;
        this.exceptRightBotoom = z4;
    }

    @NonNull
    @NotNull
    @Override
    public Resource<Bitmap> transform(@NonNull @NotNull Context context, @NonNull @NotNull Resource<Bitmap> resource, int i, int i2) {
        int height;
        int i3;
        Bitmap mo264get = resource.get();
        if (i > i2) {
            float f = i2;
            float f2 = i;
            height = mo264get.getWidth();
            i3 = (int) (mo264get.getWidth() * (f / f2));
            if (i3 > mo264get.getHeight()) {
                i3 = mo264get.getHeight();
                height = (int) (mo264get.getHeight() * (f2 / f));
            }
        } else if (i < i2) {
            float f3 = i;
            float f4 = i2;
            int height2 = mo264get.getHeight();
            int height3 = (int) (mo264get.getHeight() * (f3 / f4));
            if (height3 > mo264get.getWidth()) {
                height = mo264get.getWidth();
                i3 = (int) (mo264get.getWidth() * (f4 / f3));
            } else {
                height = height3;
                i3 = height2;
            }
        } else {
            height = mo264get.getHeight();
            i3 = height;
        }
        this.radius *= i3 / i2;
        Bitmap bitmap = this.mBitmapPool.get(height, i3, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(height, i3, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(mo264get, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int width = (mo264get.getWidth() - height) / 2;
        int height4 = (mo264get.getHeight() - i3) / 2;
        if (width != 0 || height4 != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height4);
            bitmapShader.setLocalMatrix(matrix);
        }
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight());
        float f5 = this.radius;
        canvas.drawRoundRect(rectF, f5, f5, paint);
        if (this.exceptLeftTop) {
            float f6 = this.radius;
            canvas.drawRect(0.0f, 0.0f, f6, f6, paint);
        }
        if (this.exceptRightTop) {
            float f7 = this.radius;
            canvas.drawRect(canvas.getWidth() - f7, 0.0f, f7, f7, paint);
        }
        if (this.exceptLeftBottom) {
            float f8 = this.radius;
            canvas.drawRect(0.0f, canvas.getHeight() - f8, f8, canvas.getHeight(), paint);
        }
        if (this.exceptRightBotoom) {
            canvas.drawRect(canvas.getWidth() - this.radius, canvas.getHeight() - this.radius, canvas.getWidth(), canvas.getHeight(), paint);
        }
        return BitmapResource.obtain(bitmap, this.mBitmapPool);
    }

    @Override
    public void updateDiskCacheKey(@NonNull @NotNull MessageDigest messageDigest) {

    }
}
