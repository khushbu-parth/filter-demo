package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;


public class CallUIUtils {
    private static ValueAnimator scaleDown;

    
    public enum RevealDir {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER
    }

    
    static class AnonymousClass1 {
        static final int[] revealDirections;

        AnonymousClass1() {
        }

        static {
            int[] iArr = new int[RevealDir.values().length];
            revealDirections = iArr;
            try {
                iArr[RevealDir.TOP_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                revealDirections[RevealDir.TOP_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                revealDirections[RevealDir.BOTTOM_LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                revealDirections[RevealDir.BOTTOM_RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    
    public static class Dragger {
        private Context context;
        private float dX;
        private float dY;
        private View[] targetViews;
        private View view;
        int[] initXY = new int[2];
        int[] dragXy = new int[2];
        int[] acceptXY = new int[2];
        int[] declineXY = new int[2];
        private boolean getAllInitalViews = false;
        private boolean eventSuccess = false;
        private DragEvent dragEvent = DragEvent.DEFAULT;
        Rect rect = new Rect();
        int[] location = new int[2];

        
        public enum DragEvent {
            ANSWER,
            DECLINE,
            MESSAGE,
            SPEAKER,
            WHATSAPP,
            BLOCK,
            DEFAULT
        }

        
        public interface DragEventListener {
            void onDragComplete(DragEvent dragEvent);

            @Deprecated
            void onDragIncomplete();

            void onDraging(DragEvent dragEvent);

            @Deprecated
            void onNegativeDrag();

            @Deprecated
            void onPositiveDrag();
        }

        public Dragger(View view, View[] viewArr, Context context) {
            this.view = view;
            this.targetViews = viewArr;
            this.context = context;
            view.getLocationInWindow(this.initXY);
        }

        public boolean isViewInBounds(View view, float f, float f2) {
            view.getDrawingRect(this.rect);
            view.getLocationOnScreen(this.location);
            Rect rect = this.rect;
            int[] iArr = this.location;
            rect.offset(iArr[0], iArr[1]);
            return this.rect.contains((int) f, (int) f2);
        }

        public void startDragging(final DragEventListener dragEventListener) {
            this.view.setOnTouchListener(new View.OnTouchListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallUIUtils.Dragger.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == 0) {
                        Dragger.this.dX = view.getX() - motionEvent.getRawX();
                        Dragger.this.dY = view.getY() - motionEvent.getRawY();
                        if (!Dragger.this.getAllInitalViews) {
                            view.getLocationInWindow(Dragger.this.initXY);
                            Dragger.this.targetViews[0].getLocationInWindow(Dragger.this.acceptXY);
                            Dragger.this.targetViews[1].getLocationInWindow(Dragger.this.declineXY);
                            Log.d("drag", "target: X:" + Dragger.this.declineXY[0] + ", Y:" + Dragger.this.declineXY[1]);
                            Dragger.this.getAllInitalViews = true;
                        }
                        return true;
                    }
                    if (action != 1) {
                        if (action == 2) {
                            view.animate().x(motionEvent.getRawX() + Dragger.this.dX).y(motionEvent.getRawY() + Dragger.this.dY).setDuration(0L).start();
                            view.getLocationInWindow(Dragger.this.dragXy);
                            if (Dragger.this.initXY[1] > Dragger.this.dragXy[1]) {
                                Log.d("DragTest", "onTouch: Postive");
                                DragEventListener dragEventListener2 = dragEventListener;
                                if (dragEventListener2 != null) {
                                    dragEventListener2.onPositiveDrag();
                                }
                            } else {
                                Log.d("DragTest", "onTouch: Negative");
                                DragEventListener dragEventListener3 = dragEventListener;
                                if (dragEventListener3 != null) {
                                    dragEventListener3.onNegativeDrag();
                                }
                            }
                            view.getX();
                            int width = view.getWidth() / 2;
                            view.getY();
                            int height = view.getHeight() / 2;
                            Dragger.this.targetViews[0].getX();
                            int width2 = Dragger.this.targetViews[0].getWidth() / 2;
                            Dragger.this.targetViews[0].getY();
                            int height2 = Dragger.this.targetViews[0].getHeight() / 2;
                            Dragger dragger = Dragger.this;
                            if (dragger.isViewInBounds(dragger.targetViews[0], motionEvent.getRawX(), motionEvent.getRawY())) {
                                Dragger.this.dragEvent = DragEvent.ANSWER;
                                Dragger.this.eventSuccess = true;
                                Utility.vibrate(Dragger.this.context);
                            } else {
                                Dragger dragger2 = Dragger.this;
                                if (dragger2.isViewInBounds(dragger2.targetViews[1], motionEvent.getRawX(), motionEvent.getRawY())) {
                                    Dragger.this.dragEvent = DragEvent.DECLINE;
                                    Dragger.this.eventSuccess = true;
                                    Utility.vibrate(Dragger.this.context);
                                } else {
                                    Dragger dragger3 = Dragger.this;
                                    if (dragger3.isViewInBounds(dragger3.targetViews[2], motionEvent.getRawX(), motionEvent.getRawY())) {
                                        Dragger.this.dragEvent = DragEvent.MESSAGE;
                                        Dragger.this.eventSuccess = true;
                                        Utility.vibrate(Dragger.this.context);
                                    } else {
                                        Dragger dragger4 = Dragger.this;
                                        if (dragger4.isViewInBounds(dragger4.targetViews[3], motionEvent.getRawX(), motionEvent.getRawY())) {
                                            Dragger.this.dragEvent = DragEvent.SPEAKER;
                                            Dragger.this.eventSuccess = true;
                                            Utility.vibrate(Dragger.this.context);
                                        } else {
                                            Dragger dragger5 = Dragger.this;
                                            if (dragger5.isViewInBounds(dragger5.targetViews[4], motionEvent.getRawX(), motionEvent.getRawY())) {
                                                Dragger.this.dragEvent = DragEvent.WHATSAPP;
                                                Dragger.this.eventSuccess = true;
                                                if (!Utility.hasWhatsappInstalled(Dragger.this.context)) {
                                                    Dragger.this.eventSuccess = false;
                                                }
                                                Utility.vibrate(Dragger.this.context);
                                            } else {
                                                Dragger dragger6 = Dragger.this;
                                                if (dragger6.isViewInBounds(dragger6.targetViews[5], motionEvent.getRawX(), motionEvent.getRawY())) {
                                                    Dragger.this.dragEvent = DragEvent.BLOCK;
                                                    Dragger.this.eventSuccess = true;
                                                    Utility.vibrate(Dragger.this.context);
                                                } else {
                                                    Dragger dragger7 = Dragger.this;
                                                    if (dragger7.isViewInBounds(dragger7.targetViews[6], motionEvent.getRawX(), motionEvent.getRawY())) {
                                                        Dragger.this.dragEvent = DragEvent.ANSWER;
                                                        Dragger.this.eventSuccess = true;
                                                        Utility.vibrate(Dragger.this.context);
                                                    } else {
                                                        Dragger dragger8 = Dragger.this;
                                                        if (!dragger8.isViewInBounds(dragger8.targetViews[7], motionEvent.getRawX(), motionEvent.getRawY())) {
                                                            Dragger.this.eventSuccess = false;
                                                            Dragger.this.dragEvent = DragEvent.DEFAULT;
                                                        } else {
                                                            Dragger.this.dragEvent = DragEvent.DECLINE;
                                                            Dragger.this.eventSuccess = true;
                                                            Utility.vibrate(Dragger.this.context);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            DragEventListener dragEventListener4 = dragEventListener;
                            if (dragEventListener4 != null) {
                                dragEventListener4.onDraging(Dragger.this.dragEvent);
                            }
                        }
                    } else if (Dragger.this.eventSuccess) {
                        if (Dragger.this.dragEvent == DragEvent.BLOCK) {
                            view.animate().x(Dragger.this.initXY[0]).y(Dragger.this.initXY[1]).setDuration(100L).start();
                        }
                        DragEventListener dragEventListener5 = dragEventListener;
                        if (dragEventListener5 != null) {
                            dragEventListener5.onDragComplete(Dragger.this.dragEvent);
                        }
                    } else {
                        view.animate().x(Dragger.this.initXY[0]).y(Dragger.this.initXY[1]).setDuration(100L).start();
                        DragEventListener dragEventListener6 = dragEventListener;
                        if (dragEventListener6 != null) {
                            dragEventListener6.onDragIncomplete();
                        }
                    }
                    return false;
                }
            });
        }
    }

    public static void circularRevealEffect(View view, AnimatorListenerAdapter animatorListenerAdapter, int i, RevealDir revealDir) {
        int left = (view.getLeft() + view.getRight()) / 2;
        int top = view.getTop();
        int i2 = AnonymousClass1.revealDirections[revealDir.ordinal()];
        if (i2 == 1) {
            left = view.getLeft();
            top = view.getTop();
        } else if (i2 == 2) {
            left = view.getRight();
            top = view.getTop();
        } else if (i2 == 3) {
            left = view.getLeft();
            top = view.getBottom();
        } else if (i2 == 4) {
            left = view.getRight();
            top = view.getBottom();
        }
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view, left, top, 0.0f, Math.max(view.getWidth(), view.getHeight()));
        if (animatorListenerAdapter != null) {
            createCircularReveal.addListener(animatorListenerAdapter);
        }
        view.setBackgroundColor(i);
        createCircularReveal.start();
    }

    public static void zoomGlowEffect(View view, int i) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat("scaleX", 1.2f), PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown = ofPropertyValuesHolder;
        ofPropertyValuesHolder.setDuration(800L);
        scaleDown.setRepeatCount(i);
        scaleDown.setRepeatMode(2);
        scaleDown.start();
    }

    public static void zoomGlowEffectEnd() {
        ValueAnimator valueAnimator = scaleDown;
        if (valueAnimator != null) {
            valueAnimator.end();
            scaleDown = null;
        }
    }
}
