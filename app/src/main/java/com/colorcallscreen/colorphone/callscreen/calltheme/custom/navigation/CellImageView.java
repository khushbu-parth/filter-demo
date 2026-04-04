package com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public final class CellImageView extends AppCompatImageView {
    private boolean actionBackgroundAlpha;
    private boolean allowDraw;
    private boolean changeSize;
    private int color;
    private ValueAnimator colorAnimator;
    private boolean fitImage;
    private boolean isBitmap;
    private int resource;
    private int size;
    private boolean useColor;

    public CellImageView(Context context) {
        super(context);
        this.useColor = true;
        this.size = Utils.dip(getContext(), 24);
        this.changeSize = true;
        initializeView();
    }

    public CellImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.useColor = true;
        this.size = Utils.dip(getContext(), 24);
        this.changeSize = true;
        setAttributeFromXml(context, attributeSet);
        initializeView();
    }

    public CellImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.useColor = true;
        this.size = Utils.dip(getContext(), 24);
        this.changeSize = true;
        setAttributeFromXml(context, attributeSet);
        initializeView();
    }

    public final boolean isBitmap() {
        return this.isBitmap;
    }

    public final void setBitmap(boolean z) {
        this.isBitmap = z;
        draw();
    }

    public final boolean getUseColor() {
        return this.useColor;
    }

    public final void setUseColor(boolean z) {
        this.useColor = z;
        draw();
    }

    public final int getResource() {
        return this.resource;
    }

    public final void setResource(int i) {
        this.resource = i;
        draw();
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int i) {
        this.color = i;
        draw();
    }

    public final int getSize() {
        return this.size;
    }

    public final void setSize(int i) {
        this.size = i;
        requestLayout();
    }

    private void setAttributeFromXml(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CellImageView, 0, 0);
        try {
            setBitmap(obtainStyledAttributes.getBoolean(R.styleable.CellImageView_meow_imageview_isBitmap, this.isBitmap));
            setUseColor(obtainStyledAttributes.getBoolean(R.styleable.CellImageView_meow_imageview_useColor, this.useColor));
            setResource(obtainStyledAttributes.getResourceId(R.styleable.CellImageView_meow_imageview_resource, this.resource));
            setColor(obtainStyledAttributes.getColor(R.styleable.CellImageView_meow_imageview_color, this.color));
            setSize(obtainStyledAttributes.getDimensionPixelSize(R.styleable.CellImageView_meow_imageview_size, this.size));
            this.actionBackgroundAlpha = obtainStyledAttributes.getBoolean(R.styleable.CellImageView_meow_imageview_actionBackgroundAlpha, this.actionBackgroundAlpha);
            this.changeSize = obtainStyledAttributes.getBoolean(R.styleable.CellImageView_meow_imageview_changeSize, this.changeSize);
            this.fitImage = obtainStyledAttributes.getBoolean(R.styleable.CellImageView_meow_imageview_fitImage, this.fitImage);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void initializeView() {
        this.allowDraw = true;
        draw();
    }

    private void draw() {
        Drawable changeColorDrawableRes;
        if (!this.allowDraw || this.resource == 0) {
            return;
        }
        if (this.isBitmap) {
            try {
                if (this.color == 0) {
                    changeColorDrawableRes = Utils.getDrawableCompat(getContext(), this.resource);
                } else {
                    changeColorDrawableRes = Utils.changeColorDrawableRes(getContext(), this.resource, this.color);
                }
                setImageDrawable(changeColorDrawableRes);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        boolean z = this.useColor;
        if (z && this.color == 0) {
            return;
        }
        try {
            setImageDrawable(Utils.changeColorDrawableVector(getContext(), this.resource, z ? this.color : -2));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public final void changeColorByAnim(int i, long j) {
        int i2 = this.color;
        if (i2 == 0) {
            setColor(i);
            return;
        }
        ValueAnimator valueAnimator = this.colorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.colorAnimator = ofFloat;
        if (ofFloat != null) {
            ofFloat.setDuration(j);
            ofFloat.setInterpolator(new FastOutSlowInInterpolator());
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.CellImageView$changeColorByAnim$$inlined$apply$lambda$1
                final long $d$inlined;
                final int $lastColor$inlined;
                final int $newColor$inlined;
                final CellImageView this$0;

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                }

                {
                    this.this$0 = CellImageView.this;
                    this.$d$inlined = j;
                    this.$newColor$inlined = i;
                    this.$lastColor$inlined = i2;
                }
            });
            ofFloat.start();
        }
    }

    public static void changeColorByAnim$default(CellImageView cellImageView, int i, long j, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            j = 250;
        }
        cellImageView.changeColorByAnim(i, j);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.fitImage) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                int size = MeasureSpec.getSize(i);
                setMeasuredDimension(size, (int) Math.ceil((size * drawable.getIntrinsicHeight()) / drawable.getIntrinsicWidth()));
                return;
            }
            super.onMeasure(i, i2);
        } else if (!this.isBitmap && this.changeSize) {
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.size, 1073741824);
            super.onMeasure(makeMeasureSpec, makeMeasureSpec);
        } else {
            super.onMeasure(i, i2);
        }
    }
}
