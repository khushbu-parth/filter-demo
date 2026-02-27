package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.view.ViewParent;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import me.relex.photodraweeview.Attacher;
import me.relex.photodraweeview.OnScaleChangeListener;

/* loaded from: classes4.dex */
class NonInterceptableAttacher extends Attacher {
    private OnScaleChangeListener scaleChangeListener;

    public NonInterceptableAttacher(DraweeView<GenericDraweeHierarchy> draweeView) {
        super(draweeView);
    }

    @Override // me.relex.photodraweeview.Attacher, me.relex.photodraweeview.OnScaleDragGestureListener
    public void onDrag(float f, float f2) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            getDrawMatrix().postTranslate(f, f2);
            checkMatrixAndInvalidate();
            ViewParent parent = draweeView.getParent();
            if (parent == null) {
                return;
            }
            if (getScale() == 1.0f) {
                parent.requestDisallowInterceptTouchEvent(false);
            } else {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    @Override // me.relex.photodraweeview.Attacher
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override // me.relex.photodraweeview.Attacher
    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.scaleChangeListener = onScaleChangeListener;
    }

    @Override // me.relex.photodraweeview.Attacher, me.relex.photodraweeview.OnScaleDragGestureListener
    public void onScale(float f, float f2, float f3) {
        super.onScale(f, f2, f3);
        OnScaleChangeListener onScaleChangeListener = this.scaleChangeListener;
        if (onScaleChangeListener != null) {
            onScaleChangeListener.onScaleChange(f, f2, f3);
        }
    }
}
