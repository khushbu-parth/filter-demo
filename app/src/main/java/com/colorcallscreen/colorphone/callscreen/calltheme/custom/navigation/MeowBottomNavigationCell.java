package com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;

import java.util.HashMap;


public final class MeowBottomNavigationCell extends RelativeLayout {
    public static final String EMPTY_VALUE = "empty";
    private HashMap<Integer, View> _$_findViewCache;
    private boolean allowDraw;
    private int circleColor;
    public View containerView;
    private String count;
    private int countBackgroundColor;
    private int countTextColor;
    private Typeface countTypeface;
    private int defaultIconColor;
    private long duration;
    private int icon;
    private int iconSize;
    private boolean isEnabledCell;
    private boolean isFromLeft;
    private MeowBottomNavigation.ClickListener onClickListener;
    private float progress;
    private int rippleColor;
    private int selectedIconColor;

    private void setAttributeFromXml(Context context, AttributeSet attributeSet) {
    }

    public MeowBottomNavigationCell(Context context) {
        super(context);
        this.count = EMPTY_VALUE;
        this.iconSize = Utils.dip(getContext(), 48);
        initializeView();
    }

    public MeowBottomNavigationCell(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.count = EMPTY_VALUE;
        this.iconSize = Utils.dip(getContext(), 48);
        setAttributeFromXml(context, attributeSet);
        initializeView();
    }

    public MeowBottomNavigationCell(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.count = EMPTY_VALUE;
        this.iconSize = Utils.dip(getContext(), 48);
        setAttributeFromXml(context, attributeSet);
        initializeView();
    }

    public final int getDefaultIconColor() {
        return this.defaultIconColor;
    }

    public final void setDefaultIconColor(int i) {
        this.defaultIconColor = i;
        if (this.allowDraw) {
            ((CellImageView) _$_findCachedViewById(R.id.iv)).setColor(!this.isEnabledCell ? this.defaultIconColor : this.selectedIconColor);
        }
    }

    public final int getSelectedIconColor() {
        return this.selectedIconColor;
    }

    public final void setSelectedIconColor(int i) {
        this.selectedIconColor = i;
        if (this.allowDraw) {
            ((CellImageView) _$_findCachedViewById(R.id.iv)).setColor(this.isEnabledCell ? this.selectedIconColor : this.defaultIconColor);
        }
    }

    public final int getCircleColor() {
        return this.circleColor;
    }

    public final void setCircleColor(int i) {
        this.circleColor = i;
        if (this.allowDraw) {
            setEnabledCell(this.isEnabledCell);
        }
    }

    public final int getIcon() {
        return this.icon;
    }

    public final void setIcon(int i) {
        this.icon = i;
        if (this.allowDraw) {
            ((CellImageView) _$_findCachedViewById(R.id.iv)).setResource(i);
        }
    }

    public final String getCount() {
        return this.count;
    }

    public final void setCount(String str) {
        this.count = str;
        if (this.allowDraw) {
            if (str != null && str.equals(EMPTY_VALUE)) {
                ((TextView) _$_findCachedViewById(R.id.tv_count)).setText("");
                ((TextView) _$_findCachedViewById(R.id.tv_count)).setVisibility(4);
                return;
            }
            String str2 = this.count;
            if (str2 != null && str2.length() >= 3) {
                this.count.substring(0, 1);
            }
            ((TextView) _$_findCachedViewById(R.id.tv_count)).setText(this.count);
            ((TextView) _$_findCachedViewById(R.id.tv_count)).setVisibility(0);
            String str3 = this.count;
            float f = (str3 == null || str3.length() != 0) ? 1.0f : 0.5f;
            ((TextView) _$_findCachedViewById(R.id.tv_count)).setScaleX(f);
            ((TextView) _$_findCachedViewById(R.id.tv_count)).setScaleY(f);
        }
    }

    private void setIconSize(int i) {
        this.iconSize = i;
        if (this.allowDraw) {
            ((CellImageView) _$_findCachedViewById(R.id.iv)).setSize(i);
            ((CellImageView) _$_findCachedViewById(R.id.iv)).setPivotX(this.iconSize / 2.0f);
            ((CellImageView) _$_findCachedViewById(R.id.iv)).setPivotY(this.iconSize / 2.0f);
        }
    }

    public final int getCountTextColor() {
        return this.countTextColor;
    }

    public final void setCountTextColor(int i) {
        this.countTextColor = i;
        if (this.allowDraw) {
            ((TextView) _$_findCachedViewById(R.id.tv_count)).setTextColor(this.countTextColor);
        }
    }

    public final int getCountBackgroundColor() {
        return this.countBackgroundColor;
    }

    public final void setCountBackgroundColor(int i) {
        this.countBackgroundColor = i;
        if (this.allowDraw) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(this.countBackgroundColor);
            gradientDrawable.setShape(1);
            ViewCompat.setBackground(_$_findCachedViewById(R.id.tv_count), gradientDrawable);
        }
    }

    public final Typeface getCountTypeface() {
        return this.countTypeface;
    }

    public final void setCountTypeface(Typeface typeface) {
        this.countTypeface = typeface;
        if (!this.allowDraw || typeface == null) {
            return;
        }
        ((TextView) _$_findCachedViewById(R.id.tv_count)).setTypeface(this.countTypeface);
    }

    public final int getRippleColor() {
        return this.rippleColor;
    }

    public final void setRippleColor(int i) {
        this.rippleColor = i;
        if (this.allowDraw) {
            setEnabledCell(this.isEnabledCell);
        }
    }

    public final boolean isFromLeft() {
        return this.isFromLeft;
    }

    public final void setFromLeft(boolean z) {
        this.isFromLeft = z;
    }

    public final long getDuration() {
        return this.duration;
    }

    public final void setDuration(long j) {
        this.duration = j;
    }

    public void setProgress(float f) {
        this.progress = f;
        ((FrameLayout) _$_findCachedViewById(R.id.fl)).setY(((1.0f - this.progress) * Utils.dip(getContext(), 18)) + Utils.dip(getContext(), -2));
        ((CellImageView) _$_findCachedViewById(R.id.iv)).setColor(this.progress == 1.0f ? this.selectedIconColor : this.defaultIconColor);
        float f2 = ((1.0f - this.progress) * (-0.2f)) + 1.0f;
        ((CellImageView) _$_findCachedViewById(R.id.iv)).setScaleX(f2);
        ((CellImageView) _$_findCachedViewById(R.id.iv)).setScaleY(f2);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(this.circleColor);
        gradientDrawable.setShape(1);
        ViewCompat.setBackground(_$_findCachedViewById(R.id.v_circle), gradientDrawable);
        ViewCompat.setElevation(_$_findCachedViewById(R.id.v_circle), this.progress > 0.7f ? Utils.dipf(getContext(), this.progress * 4.0f) : 0.0f);
        int dip = Utils.dip(getContext(), 24);
        View _$_findCachedViewById = _$_findCachedViewById(R.id.v_circle);
        float f3 = 1.0f - this.progress;
        if (this.isFromLeft) {
            dip = -dip;
        }
        _$_findCachedViewById.setX((f3 * dip) + ((getMeasuredWidth() - Utils.dip(getContext(), 48)) / 2.0f));
        _$_findCachedViewById(R.id.v_circle).setY(((1.0f - this.progress) * getMeasuredHeight()) + Utils.dip(getContext(), 6));
    }

    public final boolean isEnabledCell() {
        return this.isEnabledCell;
    }

    public final void setEnabledCell(boolean z) {
        this.isEnabledCell = z;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(this.circleColor);
        gradientDrawable.setShape(1);
        if (Build.VERSION.SDK_INT >= 21 && !this.isEnabledCell) {
            ((FrameLayout) _$_findCachedViewById(R.id.fl)).setBackground(new RippleDrawable(ColorStateList.valueOf(this.rippleColor), null, gradientDrawable));
        } else {
            _$_findCachedViewById(R.id.fl).postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.MeowBottomNavigationCell.1
                @Override 
                public final void run() {
                    try {
                        MeowBottomNavigationCell.this._$_findCachedViewById(R.id.fl).setBackgroundColor(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 200L);
        }
    }

    public final MeowBottomNavigation.ClickListener getOnClickListener() {
        return this.onClickListener;
    }

    public final void setOnClickListener(MeowBottomNavigation.ClickListener clickListener) {
        this.onClickListener = clickListener;
        CellImageView cellImageView = (CellImageView) _$_findCachedViewById(R.id.iv);
        if (cellImageView != null) {
            cellImageView.setOnClickListener(new OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.MeowBottomNavigationCell.2

                @Override 
                public final void onClick(View view) {
                    MeowBottomNavigationCell.this.getOnClickListener().onClickItem(new MeowBottomNavigation.Model(0, 0));
                }
            });
        }
    }

    public View getContainerView() {
        return this.containerView;
    }

    public void setContainerView(View view) {
        this.containerView = view;
    }

    private void initializeView() {
        this.allowDraw = true;
        setContainerView(LayoutInflater.from(getContext()).inflate(R.layout.meow_navigation_cell, this));
        draw();
    }

    private void draw() {
        if (this.allowDraw) {
            setIcon(this.icon);
            setCount(this.count);
            setIconSize(this.iconSize);
            setCountTextColor(this.countTextColor);
            setCountBackgroundColor(this.countBackgroundColor);
            setCountTypeface(this.countTypeface);
            setRippleColor(this.rippleColor);
            setOnClickListener(this.onClickListener);
        }
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setProgress(this.progress);
    }

    public final void disableCell() {
        if (this.isEnabledCell) {
            animateProgress$default(this, false, false, 2, null);
        }
        setEnabledCell(false);
    }

    public final void enableCell(boolean z) {
        if (!this.isEnabledCell) {
            animateProgress(true, z);
        }
        setEnabledCell(true);
    }

    public static void enableCell$default(MeowBottomNavigationCell meowBottomNavigationCell, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        meowBottomNavigationCell.enableCell(z);
    }

    private void animateProgress(boolean z, boolean z2) {
        long j = z ? this.duration : 250L;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setStartDelay(z ? j / 4 : 0L);
        ofFloat.setDuration(z2 ? j : 1L);
        ofFloat.setInterpolator(new FastOutSlowInInterpolator());
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.MeowBottomNavigationCell$MeowBottomNavigationCell$animateProgress$$inlined$apply$lambda$1
            final long $d$inlined;
            final boolean $enableCell$inlined;
            final boolean $isAnimate$inlined;
            final MeowBottomNavigationCell this$0;

            {
                this.this$0 = MeowBottomNavigationCell.this;
                this.$enableCell$inlined = z;
                this.$d$inlined = j;
                this.$isAnimate$inlined = z2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                MeowBottomNavigationCell meowBottomNavigationCell = this.this$0;
                if (!this.$enableCell$inlined) {
                    animatedFraction = 1.0f - animatedFraction;
                }
                meowBottomNavigationCell.setProgress(animatedFraction);
            }
        });
        ofFloat.start();
    }

    static void animateProgress$default(MeowBottomNavigationCell meowBottomNavigationCell, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            z2 = true;
        }
        meowBottomNavigationCell.animateProgress(z, z2);
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap<>();
        }
        View view = this._$_findViewCache.get(Integer.valueOf(i));
        if (view == null) {
            View findViewById = findViewById(i);
            this._$_findViewCache.put(Integer.valueOf(i), findViewById);
            return findViewById;
        }
        return view;
    }
}
