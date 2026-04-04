package com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;


class Utils {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    Utils() {
    }

    private static float getDP(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float dipf(Context context, float f) {
        return f * getDP(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float dipf(Context context, int i) {
        return i * getDP(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int dip(Context context, int i) {
        return (int) (i * getDP(context));
    }

    static int dip(Context context, float f) {
        return (int) (f * getDP(context));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Drawable getDrawableCompat(Context context, int i) {
        return ContextCompat.getDrawable(context, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Drawable changeColorDrawableVector(Context context, int i, int i2) {
        if (context == null) {
            return null;
        }
        VectorDrawableCompat create = VectorDrawableCompat.create(context.getResources(), i, null);
        create.mutate();
        if (i2 != -2) {
            create.setColorFilter(i2, PorterDuff.Mode.SRC_IN);
        }
        return create;
    }

    public static Drawable changeColorDrawableRes(Context context, int i, int i2) {
        if (context == null) {
            return null;
        }
        Drawable drawable = ContextCompat.getDrawable(context, i);
        drawable.mutate();
        if (i2 != -2) {
            drawable.setColorFilter(i2, PorterDuff.Mode.SRC_IN);
        }
        return drawable;
    }
}
