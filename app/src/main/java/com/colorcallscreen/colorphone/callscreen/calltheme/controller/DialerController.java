package com.colorcallscreen.colorphone.callscreen.calltheme.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class DialerController extends DialerView {
    private View activityView;
    private LinearLayout bottomPart;
    private View dialerView;
    private boolean dialerVisibility;

    public DialerController(Context context, boolean z, View view) {
        super(context, z);
        this.dialerVisibility = false;
        this.activityView = view;
        this.dialerView = view.findViewById(R.id.dialerView);
        this.bottomPart = (LinearLayout) view.findViewById(R.id.bottomPart);
        findViews(this.activityView);
        onWindowCreated();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView, com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public void finishWindow() {
        hideDialer();
    }

    public void hideDialer() {
        if (this.dialerView != null) {
            YoYo.with(Techniques.SlideOutDown).withListener(new AnimatorListenerAdapter() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.controller.DialerController.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    DialerController.this.dialerView.setVisibility(8);
                    DialerController.this.bottomPart.setVisibility(0);
                    DialerController.this.dialerVisibility = false;
                }
            }).duration(200L).playOn(this.dialerView);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.windowview.DialerView
    public boolean isDialerVisible() {
        return this.dialerVisibility;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public void setAsWindow() {
        showDialer();
    }

    public void showDialer() {
        this.dialerView.setVisibility(0);
        this.bottomPart.setVisibility(4);
        YoYo.with(Techniques.SlideInUp).duration(200L).playOn(this.dialerView);
        this.dialerVisibility = true;
    }
}
