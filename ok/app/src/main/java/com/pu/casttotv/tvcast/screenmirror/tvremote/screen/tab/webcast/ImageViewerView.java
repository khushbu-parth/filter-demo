package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/* loaded from: classes4.dex */
public class ImageViewerView extends RelativeLayout implements OnDismissListener, SwipeToDismissListener.OnViewMoveListener {
    private View backgroundView;
    private SwipeDirectionDetector.Direction direction;
    private SwipeDirectionDetector directionDetector;
    private ViewGroup dismissContainer;
    private GestureDetectorCompat gestureDetector;
    private boolean isOverlayWasClicked;
    private boolean isSwipeToDismissAllowed;
    private OnDismissListener onDismissListener;
    private View overlayView;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private MultiTouchViewPager pager;
    private ScaleGestureDetector scaleDetector;
    private SwipeToDismissListener swipeDismissListener;
    private boolean wasScaled;

    public void setCustomDraweeHierarchyBuilder(GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder) {
    }

    public void setCustomImageRequestBuilder(ImageRequestBuilder imageRequestBuilder) {
    }

    public ImageViewerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isSwipeToDismissAllowed = true;
        init();
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        findViewById(R.id.backgroundView).setBackgroundColor(i);
    }

    public void setOverlayView(View view) {
        this.overlayView = view;
        if (view != null) {
            this.dismissContainer.addView(view);
        }
    }

    public void setImageMargin(int i) {
        this.pager.setPageMargin(i);
    }

    public void setContainerPadding(int[] iArr) {
        this.pager.setPadding(iArr[0], iArr[1], iArr[2], iArr[3]);
    }

    private void init() {
        RelativeLayout.inflate(getContext(), R.layout.image_viewer, this);
        this.backgroundView = findViewById(R.id.backgroundView);
        this.pager = (MultiTouchViewPager) findViewById(R.id.pager);
        this.dismissContainer = (ViewGroup) findViewById(R.id.container);
        SwipeToDismissListener swipeToDismissListener = new SwipeToDismissListener(findViewById(R.id.dismissView), this, this);
        this.swipeDismissListener = swipeToDismissListener;
        this.dismissContainer.setOnTouchListener(swipeToDismissListener);
        this.directionDetector = new SwipeDirectionDetector(getContext()) { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.ImageViewerView.1
        };
        this.scaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener());
        this.gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.ImageViewerView.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                if (!ImageViewerView.this.pager.isScrolled()) {
                    return false;
                }
                ImageViewerView imageViewerView = ImageViewerView.this;
                imageViewerView.onClick(motionEvent, imageViewerView.isOverlayWasClicked);
                return false;
            }
        });
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        onUpDownEvent(motionEvent);
        if (this.direction == null && (this.scaleDetector.isInProgress() || motionEvent.getPointerCount() > 1)) {
            this.wasScaled = true;
            return this.pager.dispatchTouchEvent(motionEvent);
        }
        this.pager.getCurrentItem();
        throw null;
    }

    @Override // com.magicapps.casttotv.tv.screen.tab.webcast.OnDismissListener
    public void onDismiss() {
        OnDismissListener onDismissListener = this.onDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    @Override // com.magicapps.casttotv.tv.screen.tab.webcast.SwipeToDismissListener.OnViewMoveListener
    public void onViewMove(float f, int i) {
        float abs = 1.0f - (((1.0f / i) / 4.0f) * Math.abs(f));
        this.backgroundView.setAlpha(abs);
        View view = this.overlayView;
        if (view != null) {
            view.setAlpha(abs);
        }
    }

    public void setOnDismissListener(ImageViewer imageViewer) {
        this.onDismissListener = imageViewer;
    }

    public String getUrl() {
        this.pager.getCurrentItem();
        throw null;
    }

    public void setPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.pager.removeOnPageChangeListener(this.pageChangeListener);
        this.pageChangeListener = onPageChangeListener;
        this.pager.addOnPageChangeListener(onPageChangeListener);
        onPageChangeListener.onPageSelected(this.pager.getCurrentItem());
    }

    private void setStartPosition(int i) {
        this.pager.setCurrentItem(i);
    }

    private void onUpDownEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            onActionUp(motionEvent);
        }
        if (motionEvent.getAction() == 0) {
            onActionDown(motionEvent);
        }
        this.scaleDetector.onTouchEvent(motionEvent);
        this.gestureDetector.onTouchEvent(motionEvent);
    }

    private void onActionDown(MotionEvent motionEvent) {
        this.direction = null;
        this.wasScaled = false;
        this.pager.dispatchTouchEvent(motionEvent);
        this.swipeDismissListener.onTouch(this.dismissContainer, motionEvent);
        this.isOverlayWasClicked = dispatchOverlayTouch(motionEvent);
    }

    private void onActionUp(MotionEvent motionEvent) {
        this.swipeDismissListener.onTouch(this.dismissContainer, motionEvent);
        this.pager.dispatchTouchEvent(motionEvent);
        this.isOverlayWasClicked = dispatchOverlayTouch(motionEvent);
    }

    public void onClick(MotionEvent motionEvent, boolean z) {
        View view = this.overlayView;
        if (view == null || z) {
            return;
        }
        AnimationUtils.animateVisibility(view);
        super.dispatchTouchEvent(motionEvent);
    }

    @SuppressLint("WrongConstant")
    private boolean dispatchOverlayTouch(MotionEvent motionEvent) {
        View view = this.overlayView;
        return view != null && view.getVisibility() == 0 && this.overlayView.dispatchTouchEvent(motionEvent);
    }
}
