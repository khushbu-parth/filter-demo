package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewConfiguration;

/* loaded from: classes4.dex */
public final class AnimationUtils {
    public static void animateVisibility(final View view) {
        boolean z = view.getVisibility() == 0;
        float f = 0.0f;
        float f2 = z ? 1.0f : 0.0f;
        if (!z) {
            f = 1.0f;
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", f2, f);
        ofFloat.setDuration(ViewConfiguration.getDoubleTapTimeout());
        if (z) {
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.thntech.cast68.screen.tab.webcast.AnimationUtils.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    view.setVisibility(8);
                }
            });
        } else {
            view.setVisibility(0);
        }
        ofFloat.start();
    }
}
