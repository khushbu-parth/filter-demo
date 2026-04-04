package com.colorcallscreen.colorphone.callscreen.calltheme.custom;

import android.content.Context;
import android.os.Build;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


public abstract class WindowViewer {
    private Context context;
    private LayoutInflater inflater;
    protected WindowManager.LayoutParams params;
    private boolean shouldEnableDrag = false;
    protected View view;
    public WindowManager windowManager;

    protected int animationStyle() {
        return -1;
    }

    protected boolean dragEnable() {
        return false;
    }

    protected abstract void findViews(View view);

    protected int getWindowGravity() {
        return 48;
    }

    public abstract View initView(LayoutInflater layoutInflater);

    public void onWindowClick() {
    }

    protected abstract void onWindowCreated();

    
    public class Dragable implements View.OnTouchListener {
        private GestureDetector gestureDetector;
        private float initialTouchX;
        private float initialTouchY;
        private int initialX;
        private int initialY;
        private int lastAction;
        private WindowManager.LayoutParams params;

        public Dragable(GestureDetector gestureDetector, WindowManager.LayoutParams layoutParams) {
            this.gestureDetector = gestureDetector;
            this.params = layoutParams;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            GestureDetector gestureDetector = this.gestureDetector;
            if (gestureDetector != null && gestureDetector.onTouchEvent(motionEvent)) {
                WindowViewer.this.onWindowClick();
                return true;
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                this.initialX = this.params.x;
                this.initialY = this.params.y;
                this.initialTouchX = motionEvent.getRawX();
                this.initialTouchY = motionEvent.getRawY();
                this.lastAction = motionEvent.getAction();
                return true;
            } else if (action == 1) {
                WindowViewer windowViewer = WindowViewer.this;
                if (windowViewer.windowManager != null && windowViewer.view != null) {
                    this.lastAction = motionEvent.getAction();
                }
                return true;
            } else if (action != 2) {
                return WindowViewer.this.dragEnable();
            } else {
                if (view == null) {
                    return false;
                }
                try {
                    this.params.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                    this.params.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                    WindowViewer windowViewer2 = WindowViewer.this;
                    View view2 = windowViewer2.view;
                    if (view2 != null) {
                        windowViewer2.windowManager.updateViewLayout(view2, this.params);
                    }
                    return true;
                } catch (Exception unused) {
                    return false;
                }
            }
        }
    }

    public WindowViewer(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setUpView();
    }

    private void dragView(WindowManager.LayoutParams layoutParams) {
        if (dragEnable()) {
            this.view.setOnTouchListener(new Dragable(new GestureDetector(new GestureDetector.SimpleOnGestureListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer.1
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            }), layoutParams));
        }
    }

    public void finishWindow() {
        View view;
        WindowManager windowManager = this.windowManager;
        if (windowManager == null || (view = this.view) == null) {
            return;
        }
        windowManager.removeView(view);
        this.windowManager = null;
        this.view = null;
    }

    public Context getContext() {
        return this.context;
    }

    public View getView() {
        return this.view;
    }

    public void setAsWindow() {
        Context context = this.context;
        if (context == null || this.view == null) {
            return;
        }
        this.windowManager = (WindowManager) context.getSystemService("window");
        this.params = null;
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            this.params = new WindowManager.LayoutParams(-1, -2, 2038, 8, -3);
        } else if (i >= 23) {
            this.params = new WindowManager.LayoutParams(-1, -2, 2002, 8, -3);
        } else {
            this.params = new WindowManager.LayoutParams(-1, -2, 2003, 8, -3);
        }
        if (animationStyle() != -1) {
            this.params.windowAnimations = animationStyle();
        }
        WindowManager.LayoutParams layoutParams = this.params;
        layoutParams.flags = 32;
        layoutParams.gravity = getWindowGravity();
        this.windowManager.addView(this.view, this.params);
        dragView(this.params);
        onWindowCreated();
    }

    public void setEnableDrag(boolean z) {
        this.shouldEnableDrag = z;
    }

    public void setUpView() {
        View initView = initView(this.inflater);
        this.view = initView;
        findViews(initView);
    }
}
