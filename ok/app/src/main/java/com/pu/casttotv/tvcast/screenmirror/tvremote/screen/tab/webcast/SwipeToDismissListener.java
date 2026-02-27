package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/* loaded from: classes4.dex */
public class SwipeToDismissListener implements View.OnTouchListener {
    private OnDismissListener dismissListener;
    private OnViewMoveListener moveListener;
    private float startY;
    private final View swipeView;
    private boolean tracking = false;
    private int translationLimit;

    /* loaded from: classes4.dex */
    public interface OnViewMoveListener {
        void onViewMove(float f, int i);
    }

    public SwipeToDismissListener(View view, OnDismissListener onDismissListener, OnViewMoveListener onViewMoveListener) {
        this.swipeView = view;
        this.dismissListener = onDismissListener;
        this.moveListener = onViewMoveListener;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.translationLimit = view.getHeight() / 4;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (this.tracking) {
                        float y = motionEvent.getY() - this.startY;
                        this.swipeView.setTranslationY(y);
                        callMoveListener(y, this.translationLimit);
                    }
                    return true;
                } else if (action != 3) {
                    return false;
                }
            }
            if (this.tracking) {
                this.tracking = false;
                animateSwipeView(view.getHeight());
            }
            return true;
        }
        Rect rect = new Rect();
        this.swipeView.getHitRect(rect);
        if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            this.tracking = true;
        }
        this.startY = motionEvent.getY();
        return true;
    }

    private void animateSwipeView(int i) {
        float translationY = this.swipeView.getTranslationY();
        int i2 = this.translationLimit;
        if (translationY < (-i2)) {
            i = -i;
        } else if (translationY <= i2) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.swipeView, "translationY", translationY, 0.0f);
            ofFloat.setDuration(200L);
            ofFloat.setInterpolator(new AccelerateInterpolator());
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.thntech.cast68.screen.tab.webcast.SwipeToDismissListener.1
                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    SwipeToDismissListener.this.callDismissListener();
                }
            });
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.thntech.cast68.screen.tab.webcast.SwipeToDismissListener.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SwipeToDismissListener.this.callMoveListener(((Float) valueAnimator.getAnimatedValue()).floatValue(), SwipeToDismissListener.this.translationLimit);
                }
            });
            ofFloat.start();
        }
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.swipeView, "translationY", translationY, i);
        ofFloat2.setDuration(200L);
        ofFloat2.setInterpolator(new AccelerateInterpolator());
        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.thntech.cast68.screen.tab.webcast.SwipeToDismissListener.3
            @Override
            // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                SwipeToDismissListener.this.callDismissListener();
            }
        });
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.thntech.cast68.screen.tab.webcast.SwipeToDismissListener.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwipeToDismissListener.this.callMoveListener(((Float) valueAnimator.getAnimatedValue()).floatValue(), SwipeToDismissListener.this.translationLimit);
            }
        });
        ofFloat2.start();
    }

    public void callDismissListener() {
        OnDismissListener onDismissListener = this.dismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void callMoveListener(float f, int i) {
        OnViewMoveListener onViewMoveListener = this.moveListener;
        if (onViewMoveListener != null) {
            onViewMoveListener.onViewMove(f, i);
        }
    }
}
