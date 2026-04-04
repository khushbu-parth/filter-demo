package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class SwipeUpHelper {
    Context context;
    private float dX;
    private float dY;
    GestureDetector gestureDetector;
    float move;
    float prevEventY;
    GestureDetector.SimpleOnGestureListener simpleOnGestureListener;
    View view;
    int[] xy;
    int threshold = 800;
    boolean isDrag = false;

    
    public interface SwipeCompeleteListner {
        void onSwipeUpComplete();
    }

    public SwipeUpHelper(Context context, View view) {
        int[] iArr = new int[2];
        this.xy = iArr;
        this.context = context;
        this.view = view;
        view.getLocationInWindow(iArr);
    }

    private void animate() {
        this.view.animate().y(-10.0f).setDuration(1000L).start();
    }

    public void fadeOutAnimation(View view) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        ofFloat.setDuration(1200L);
        ofFloat.start();
    }

    private int getThreshold() {
        return (this.context.getResources().getDisplayMetrics().heightPixels * 75) / 100;
    }

    public void slideAnimation(View view, float f) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationY", f);
        ofFloat.setDuration(300L);
        ofFloat.start();
    }

    public void start(final SwipeCompeleteListner swipeCompeleteListner) {
        this.simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.SwipeUpHelper.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (Math.abs(f2) > 1.0f && f2 > 0.0f) {
                    swipeCompeleteListner.onSwipeUpComplete();
                }
                return false;
            }
        };
        this.gestureDetector = new GestureDetector(this.context, this.simpleOnGestureListener);
        this.view.setOnTouchListener(new View.OnTouchListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.SwipeUpHelper.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SwipeUpHelper.this.gestureDetector.onTouchEvent(motionEvent);
                if (motionEvent.getAction() != 0) {
                    return false;
                }
                YoYo.with(Techniques.Pulse).playOn(view);
                return true;
            }
        });
    }

    public void swipeUp(final SwipeCompeleteListner swipeCompeleteListner) {
        this.threshold = getThreshold();
        this.view.setOnTouchListener(new View.OnTouchListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.SwipeUpHelper.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    SwipeUpHelper.this.dY = view.getY() - motionEvent.getRawY();
                    Log.d("swipe", "down: " + motionEvent.getRawY());
                    SwipeUpHelper.this.prevEventY = motionEvent.getRawY();
                    Utility.cancelBounce();
                } else if (action != 1) {
                    if (action == 2 && SwipeUpHelper.this.prevEventY > motionEvent.getRawY()) {
                        SwipeUpHelper swipeUpHelper = SwipeUpHelper.this;
                        swipeUpHelper.move = swipeUpHelper.dY + motionEvent.getRawY();
                        Log.d("swipe", "move: " + motionEvent.getRawY());
                        if (SwipeUpHelper.this.move > SwipeUpHelper.this.threshold) {
                            SwipeUpHelper.this.isDrag = true;
                            view.animate().y(SwipeUpHelper.this.move).setDuration(0L).start();
                        }
                    }
                } else if (SwipeUpHelper.this.move >= SwipeUpHelper.this.threshold || !SwipeUpHelper.this.isDrag) {
                    SwipeUpHelper swipeUpHelper2 = SwipeUpHelper.this;
                    swipeUpHelper2.slideAnimation(view, swipeUpHelper2.xy[1]);
                } else {
                    Log.d("swipe", "onTouch: complete");
                    SwipeUpHelper.this.fadeOutAnimation(view);
                    SwipeUpHelper swipeUpHelper3 = SwipeUpHelper.this;
                    swipeUpHelper3.slideAnimation(view, swipeUpHelper3.xy[1]);
                    swipeCompeleteListner.onSwipeUpComplete();
                }
                return true;
            }
        });
    }

    private int getThreshold(int i) {
        return (this.context.getResources().getDisplayMetrics().heightPixels * (100 - i)) / 100;
    }

    public void swipeUp(final SwipeCompeleteListner swipeCompeleteListner, int i) {
        this.threshold = getThreshold(i);
        this.view.setOnTouchListener(new View.OnTouchListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.SwipeUpHelper.4
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    SwipeUpHelper.this.dY = view.getY() - motionEvent.getRawY();
                    Log.d("swipe", "down: " + motionEvent.getRawY());
                    SwipeUpHelper.this.prevEventY = motionEvent.getRawY();
                    Utility.cancelBounce();
                } else if (action != 1) {
                    if (action == 2 && SwipeUpHelper.this.prevEventY > motionEvent.getRawY()) {
                        SwipeUpHelper swipeUpHelper = SwipeUpHelper.this;
                        swipeUpHelper.move = swipeUpHelper.dY + motionEvent.getRawY();
                        Log.d("swipe", "move: " + motionEvent.getRawY());
                        if (SwipeUpHelper.this.move > SwipeUpHelper.this.threshold) {
                            SwipeUpHelper.this.isDrag = true;
                            view.animate().y(SwipeUpHelper.this.move).setDuration(0L).start();
                        }
                    }
                } else if (SwipeUpHelper.this.move >= SwipeUpHelper.this.threshold || !SwipeUpHelper.this.isDrag) {
                    SwipeUpHelper swipeUpHelper2 = SwipeUpHelper.this;
                    swipeUpHelper2.slideAnimation(view, swipeUpHelper2.xy[1]);
                } else {
                    Log.d("swipe", "onTouch: complete");
                    SwipeUpHelper swipeUpHelper3 = SwipeUpHelper.this;
                    swipeUpHelper3.slideAnimation(view, swipeUpHelper3.xy[1]);
                    swipeCompeleteListner.onSwipeUpComplete();
                }
                return true;
            }
        });
    }
}
