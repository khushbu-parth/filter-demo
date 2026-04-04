package com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;


public final class BezierView extends View {
    private float bezierInnerHeight;
    private float bezierInnerWidth;
    private float bezierX;
    private int color;
    private float height;
    private PointF[] innerArray;
    private Paint mainPaint;
    private Path mainPath;
    private PointF[] outerArray;
    private float progress;
    private PointF[] progressArray;
    private int shadowColor;
    private float shadowHeight;
    private Paint shadowPaint;
    private Path shadowPath;
    private float width;

    public BezierView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initializeViews();
    }

    public BezierView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initializeViews();
    }

    public BezierView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initializeViews();
    }

    public BezierView(Context context) {
        super(context);
        initializeViews();
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int i) {
        this.color = i;
        Paint paint = this.mainPaint;
        if (paint != null) {
            paint.setColor(i);
        }
        invalidate();
    }

    public final int getShadowColor() {
        return this.shadowColor;
    }

    public final void setShadowColor(int i) {
        this.shadowColor = i;
        Paint paint = this.shadowPaint;
        if (paint != null) {
            paint.setShadowLayer(Utils.dipf(getContext(), 4), 0.0f, 0.0f, this.shadowColor);
        }
        invalidate();
    }

    public final float getBezierX() {
        return this.bezierX;
    }

    public final void setBezierX(float f) {
        if (f != this.bezierX) {
            this.bezierX = f;
            invalidate();
        }
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        if (f != this.progress) {
            this.progress = f;
            PointF[] pointFArr = this.progressArray;
            if (pointFArr == null) {
                return;
            }
            pointFArr[1].x = this.bezierX - (this.bezierInnerWidth / 2.0f);
            this.progressArray[2].x = this.bezierX - (this.bezierInnerWidth / 4.0f);
            this.progressArray[3].x = this.bezierX - (this.bezierInnerWidth / 4.0f);
            this.progressArray[4].x = this.bezierX;
            this.progressArray[5].x = this.bezierX + (this.bezierInnerWidth / 4.0f);
            this.progressArray[6].x = this.bezierX + (this.bezierInnerWidth / 4.0f);
            this.progressArray[7].x = this.bezierX + (this.bezierInnerWidth / 2.0f);
            for (int i = 2; i <= 6; i++) {
                if (this.progress <= 1.0f) {
                    this.progressArray[i].y = calculate(this.innerArray[i].y, this.outerArray[i].y);
                } else {
                    this.progressArray[i].y = calculate(this.outerArray[i].y, this.innerArray[i].y);
                }
            }
            if (this.progress == 2.0f) {
                this.progress = 0.0f;
            }
            invalidate();
        }
    }

    private void initializeViews() {
        this.shadowHeight = Utils.dipf(getContext(), 25);
        setWillNotDraw(false);
        this.mainPath = new Path();
        this.shadowPath = new Path();
        this.outerArray = new PointF[11];
        this.innerArray = new PointF[11];
        this.progressArray = new PointF[11];
        for (int i = 0; i < 11; i++) {
            this.outerArray[i] = new PointF();
            this.innerArray[i] = new PointF();
            this.progressArray[i] = new PointF();
        }
        Paint paint = new Paint(1);
        this.mainPaint = paint;
        paint.setStrokeWidth(0.0f);
        this.mainPaint.setAntiAlias(true);
        this.mainPaint.setStyle(Paint.Style.FILL);
        this.mainPaint.setColor(this.color);
        Paint paint2 = new Paint(1);
        this.shadowPaint = paint2;
        paint2.setAntiAlias(true);
        this.shadowPaint.setShadowLayer(Utils.dipf(getContext(), 4), 0.0f, 0.0f, this.shadowColor);
        setColor(this.color);
        setShadowColor(this.shadowColor);
        setLayerType(1, this.shadowPaint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.width = MeasureSpec.getSize(i);
        this.height = MeasureSpec.getSize(i2);
        float dipf = Utils.dipf(getContext(), 40);
        float dipf2 = Utils.dipf(getContext(), 15);
        this.bezierInnerWidth = Utils.dipf(getContext(), 124);
        this.bezierInnerHeight = Utils.dipf(getContext(), -10);
        float f = this.shadowHeight;
        PointF[] pointFArr = this.outerArray;
        if (pointFArr == null) {
            return;
        }
        float f2 = dipf2 + f;
        pointFArr[0] = new PointF(0.0f, f2);
        float f3 = dipf / 2.0f;
        this.outerArray[1] = new PointF(this.bezierX - f3, f2);
        float f4 = dipf / 4.0f;
        this.outerArray[2] = new PointF(this.bezierX - f4, f2);
        this.outerArray[3] = new PointF(this.bezierX - f4, f);
        this.outerArray[4] = new PointF(this.bezierX, f);
        this.outerArray[5] = new PointF(this.bezierX + f4, f);
        this.outerArray[6] = new PointF(this.bezierX + f4, f2);
        this.outerArray[7] = new PointF(this.bezierX + f3, f2);
        this.outerArray[8] = new PointF(this.width, f2);
        this.outerArray[9] = new PointF(this.width, this.height);
        this.outerArray[10] = new PointF(0.0f, this.height);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = this.mainPath;
        if (path == null) {
            return;
        }
        path.reset();
        if (this.shadowPaint == null) {
            return;
        }
        this.shadowPath.reset();
        if (this.progress == 0.0f) {
            drawInner(canvas, true);
            drawInner(canvas, false);
            return;
        }
        drawProgress(canvas, true);
        drawProgress(canvas, false);
    }

    private void drawInner(Canvas canvas, boolean z) {
        PointF[] pointFArr;
        Paint paint = z ? this.shadowPaint : this.mainPaint;
        Path path = z ? this.shadowPath : this.mainPath;
        calculateInner();
        if (path == null || (pointFArr = this.innerArray) == null) {
            return;
        }
        path.lineTo(pointFArr[0].x, this.innerArray[0].y);
        path.lineTo(this.innerArray[1].x, this.innerArray[1].y);
        path.cubicTo(this.innerArray[2].x, this.innerArray[2].y, this.innerArray[3].x, this.innerArray[3].y, this.innerArray[4].x, this.innerArray[4].y);
        path.cubicTo(this.innerArray[5].x, this.innerArray[5].y, this.innerArray[6].x, this.innerArray[6].y, this.innerArray[7].x, this.innerArray[7].y);
        path.lineTo(this.innerArray[8].x, this.innerArray[8].y);
        path.lineTo(this.innerArray[9].x, this.innerArray[9].y);
        path.lineTo(this.innerArray[10].x, this.innerArray[10].y);
        this.progressArray = (PointF[]) this.innerArray.clone();
        canvas.drawPath(path, paint);
    }

    private void calculateInner() {
        float f = this.shadowHeight;
        this.innerArray[0] = new PointF(0.0f, this.bezierInnerHeight + f);
        this.innerArray[1] = new PointF(this.bezierX - (this.bezierInnerWidth / 2.0f), this.bezierInnerHeight + f);
        this.innerArray[2] = new PointF(this.bezierX - (this.bezierInnerWidth / 4.0f), this.bezierInnerHeight + f);
        this.innerArray[3] = new PointF(this.bezierX - (this.bezierInnerWidth / 4.0f), this.height - f);
        this.innerArray[4] = new PointF(this.bezierX, this.height - f);
        this.innerArray[5] = new PointF(this.bezierX + (this.bezierInnerWidth / 4.0f), this.height - f);
        this.innerArray[6] = new PointF(this.bezierX + (this.bezierInnerWidth / 4.0f), this.bezierInnerHeight + f);
        this.innerArray[7] = new PointF(this.bezierX + (this.bezierInnerWidth / 2.0f), this.bezierInnerHeight + f);
        this.innerArray[8] = new PointF(this.width, this.bezierInnerHeight + f);
        this.innerArray[9] = new PointF(this.width, this.height);
        this.innerArray[10] = new PointF(0.0f, this.height);
    }

    private void drawProgress(Canvas canvas, boolean z) {
        Paint paint = z ? this.shadowPaint : this.mainPaint;
        Path path = z ? this.shadowPath : this.mainPath;
        path.lineTo(this.progressArray[0].x, this.progressArray[0].y);
        path.lineTo(this.progressArray[1].x, this.progressArray[1].y);
        path.cubicTo(this.progressArray[2].x, this.progressArray[2].y, this.progressArray[3].x, this.progressArray[3].y, this.progressArray[4].x, this.progressArray[4].y);
        path.cubicTo(this.progressArray[5].x, this.progressArray[5].y, this.progressArray[6].x, this.progressArray[6].y, this.progressArray[7].x, this.progressArray[7].y);
        path.lineTo(this.progressArray[8].x, this.progressArray[8].y);
        path.lineTo(this.progressArray[9].x, this.progressArray[9].y);
        path.lineTo(this.progressArray[10].x, this.progressArray[10].y);
        canvas.drawPath(path, paint);
    }

    private float calculate(float f, float f2) {
        float f3 = this.progress;
        if (f3 > 1.0f) {
            f3 -= 1.0f;
        }
        if (f3 >= 0.9f && f3 <= 1.0f) {
            calculateInner();
        }
        return (f3 * (f2 - f)) + f;
    }
}
