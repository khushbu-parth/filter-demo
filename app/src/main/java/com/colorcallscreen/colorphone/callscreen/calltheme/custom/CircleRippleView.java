package com.colorcallscreen.colorphone.callscreen.calltheme.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import java.util.ArrayList;
import java.util.Iterator;


public class CircleRippleView extends RelativeLayout {
    private static final float DEFAULT_SCALE = 6.0f;
    private boolean animationRunning;
    private ArrayList<Animator> animatorList;
    private AnimatorSet animatorSet;
    private Paint paint;
    private int rippleAmount;
    private int rippleColor;
    private int rippleDelay;
    private int rippleDurationTime;
    private LayoutParams rippleParams;
    private float rippleRadius;
    private float rippleScale;
    private float rippleStrokeWidth;
    private int rippleType;
    private ArrayList<RippleView> rippleViewList;


    public class RippleView extends View {
        public RippleView(Context context) {
            super(context);
            setVisibility(4);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float min = Math.min(getWidth(), getHeight()) / 2;
            canvas.drawCircle(min, min, min - CircleRippleView.this.rippleStrokeWidth, CircleRippleView.this.paint);
        }
    }

    public CircleRippleView(Context context) {
        super(context);
        this.animationRunning = false;
        this.rippleViewList = new ArrayList<>();
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (isInEditMode()) {
            return;
        }
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RippleBackground);
            this.rippleColor = obtainStyledAttributes.getColor(0, getResources().getColor(R.color.rippelColor));
            this.rippleStrokeWidth = obtainStyledAttributes.getDimension(5, getResources().getDimension(R.dimen.rippleStrokeWidth));
            this.rippleRadius = obtainStyledAttributes.getDimension(2, getResources().getDimension(R.dimen.rippleRadius));
            this.rippleDurationTime = obtainStyledAttributes.getInt(1, PathInterpolatorCompat.MAX_NUM_POINTS);
            this.rippleAmount = obtainStyledAttributes.getInt(3, 6);
            this.rippleScale = obtainStyledAttributes.getFloat(4, DEFAULT_SCALE);
            this.rippleType = obtainStyledAttributes.getInt(6, 0);
            obtainStyledAttributes.recycle();
            this.rippleDelay = this.rippleDurationTime / this.rippleAmount;
            Paint paint = new Paint();
            this.paint = paint;
            paint.setAntiAlias(true);
            if (this.rippleType == 0) {
                this.rippleStrokeWidth = 0.0f;
                this.paint.setStyle(Paint.Style.FILL);
            } else {
                this.paint.setStyle(Paint.Style.STROKE);
            }
            this.paint.setColor(this.rippleColor);
            float f = this.rippleRadius;
            float f2 = this.rippleStrokeWidth;
            LayoutParams layoutParams = new LayoutParams((int) ((f + f2) * 2.0f), (int) ((f + f2) * 2.0f));
            this.rippleParams = layoutParams;
            layoutParams.addRule(13, -1);
            AnimatorSet animatorSet = new AnimatorSet();
            this.animatorSet = animatorSet;
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            this.animatorList = new ArrayList<>();
            for (int i = 0; i < this.rippleAmount; i++) {
                RippleView rippleView = new RippleView(getContext());
                addView(rippleView, this.rippleParams);
                this.rippleViewList.add(rippleView);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, this.rippleScale);
                ofFloat.setRepeatCount(-1);
                ofFloat.setRepeatMode(1);
                ofFloat.setStartDelay(this.rippleDelay * i);
                ofFloat.setDuration(this.rippleDurationTime);
                this.animatorList.add(ofFloat);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, this.rippleScale);
                ofFloat2.setRepeatCount(-1);
                ofFloat2.setRepeatMode(1);
                ofFloat2.setStartDelay(this.rippleDelay * i);
                ofFloat2.setDuration(this.rippleDurationTime);
                this.animatorList.add(ofFloat2);
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0.0f);
                ofFloat3.setRepeatCount(-1);
                ofFloat3.setRepeatMode(1);
                ofFloat3.setStartDelay(this.rippleDelay * i);
                ofFloat3.setDuration(this.rippleDurationTime);
                this.animatorList.add(ofFloat3);
            }
            this.animatorSet.playTogether(this.animatorList);
            return;
        }
        throw new IllegalArgumentException("Attributes should be provided to this view,");
    }

    public boolean isRippleAnimationRunning() {
        return this.animationRunning;
    }

    public void startRippleAnimation() {
        if (isRippleAnimationRunning()) {
            return;
        }
        Iterator<RippleView> it = this.rippleViewList.iterator();
        while (it.hasNext()) {
            it.next().setVisibility(0);
        }
        this.animatorSet.start();
        this.animationRunning = true;
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            this.animatorSet.end();
            this.animationRunning = false;
        }
    }

    public CircleRippleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.animationRunning = false;
        this.rippleViewList = new ArrayList<>();
        init(context, attributeSet);
    }

    public CircleRippleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.animationRunning = false;
        this.rippleViewList = new ArrayList<>();
        init(context, attributeSet);
    }
}
