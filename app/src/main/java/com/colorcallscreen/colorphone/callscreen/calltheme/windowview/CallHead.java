package com.colorcallscreen.colorphone.callscreen.calltheme.windowview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PermissionCenter;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public class CallHead extends WindowViewer {
    TextView backtocall;
    CardView callheadopt;
    private ImageView circle;
    private Context context;
    TextView endCall;
    private OnTapListener listener;
    private WindowManager.LayoutParams params;
    private LinearLayout revealEffectLayout;


    public interface OnTapListener {
        void onDeclineCall();

        void onReturnToCall();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected void onWindowCreated() {
    }


    public class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        private SingleTapConfirm() {
        }
    }

    public CallHead(Context context) {
        super(context);
        this.context = context;
    }

    public void moveWithReveal(final AnimatorListenerAdapter animatorListenerAdapter) {
        WindowManager.LayoutParams layoutParams;
        try {
            if (this.revealEffectLayout == null || (layoutParams = this.params) == null) {
                return;
            }
            layoutParams.height = -1;
            layoutParams.width = -1;
            this.windowManager.updateViewLayout(this.view, layoutParams);
            this.revealEffectLayout.post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.1
                @Override 
                public void run() {
                    CallHead.this.circle.setVisibility(8);
                    Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(CallHead.this.revealEffectLayout, (CallHead.this.revealEffectLayout.getLeft() + CallHead.this.revealEffectLayout.getRight()) / 2, CallHead.this.revealEffectLayout.getTop(), 0.0f, Math.max(CallHead.this.revealEffectLayout.getWidth(), CallHead.this.revealEffectLayout.getHeight()));
                    createCircularReveal.addListener(animatorListenerAdapter);
                    CallHead.this.revealEffectLayout.setBackgroundColor(CallHead.this.context.getResources().getColor(R.color.reveal_color));
                    createCircularReveal.start();
                }
            });
        } catch (Exception unused) {
        }
    }

    private void showCallHead(Context context) {
        if (PermissionCenter.isOverlayPermissionEnabled(context)) {
            if (this.windowManager != null || getView() != null) {
                finishWindow();
            }
            this.windowManager = (WindowManager) context.getSystemService("window");
            int i = Build.VERSION.SDK_INT;
            if (i >= 26) {
                this.params = new WindowManager.LayoutParams(-2, -2, 2038, 8, -3);
            } else if (i >= 23) {
                this.params = new WindowManager.LayoutParams(-2, -2, 2002, 8, -3);
            } else {
                this.params = new WindowManager.LayoutParams(-2, -2, 2003, 8, -3);
            }
            WindowManager.LayoutParams layoutParams = this.params;
            layoutParams.gravity = GravityCompat.START;
            layoutParams.x = 0;
            layoutParams.y = 100;
            final GestureDetector gestureDetector = new GestureDetector(context, new SingleTapConfirm());
            if (getView() == null) {
                setUpView();
            }
            if (getView() != null) {
                getView().setOnTouchListener(new View.OnTouchListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.2
                    private float initialTouchX;
                    private float initialTouchY;
                    private int initialX;
                    private int initialY;
                    private int lastAction;

                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (gestureDetector.onTouchEvent(motionEvent)) {
                            if (CallHead.this.callheadopt.getVisibility() == 0) {
                                CallHead.this.callheadopt.setVisibility(8);
                            } else {
                                CallHead.this.callheadopt.setVisibility(0);
                            }
                            return true;
                        }
                        int action = motionEvent.getAction();
                        if (action == 0) {
                            this.initialX = CallHead.this.params.x;
                            this.initialY = CallHead.this.params.y;
                            this.initialTouchX = motionEvent.getRawX();
                            this.initialTouchY = motionEvent.getRawY();
                            this.lastAction = motionEvent.getAction();
                            return true;
                        } else if (action == 1) {
                            if (CallHead.this.windowManager != null && CallHead.this.getView() != null) {
                                this.lastAction = motionEvent.getAction();
                            }
                            return true;
                        } else if (action != 2) {
                            return false;
                        } else {
                            if (CallHead.this.windowManager != null && CallHead.this.getView() != null) {
                                CallHead.this.params.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                                CallHead.this.windowManager.updateViewLayout(CallHead.this.getView(), CallHead.this.params);
                                this.lastAction = motionEvent.getAction();
                            }
                            return true;
                        }
                    }
                });
            }
            try {
                if (this.params != null) {
                    this.windowManager.addView(getView(), this.params);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void cleanUp() {
        finishWindow();
        this.windowManager = null;
        this.view = null;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    protected void findViews(View view) {
        this.circle = (ImageView) view.findViewById(R.id.logo);
        this.revealEffectLayout = (LinearLayout) view.findViewById(R.id.revealEffectLayout);
        this.callheadopt = (CardView) view.findViewById(R.id.callheadopt);
        this.backtocall = (TextView) view.findViewById(R.id.backtocall);
        this.endCall = (TextView) view.findViewById(R.id.endCall);
        this.backtocall.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.3
            @Override 
            public void onClick(View view2) {
                if (CallHead.this.listener != null) {
                    CallHead.this.listener.onReturnToCall();
                }
                CallHead.this.moveWithReveal(new AnimatorListenerAdapter() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.3.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                    }
                });
            }
        });
        this.endCall.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHead.4
            @Override 
            public void onClick(View view2) {
                if (CallHead.this.listener != null) {
                    CallHead.this.listener.onDeclineCall();
                }
            }
        });
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public View initView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.head, (ViewGroup) null);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.WindowViewer
    public void setAsWindow() {
        showCallHead(this.context);
    }

    public void setTapListener(OnTapListener onTapListener) {
        this.listener = onTapListener;
    }
}
