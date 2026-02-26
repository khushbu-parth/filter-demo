package com.cast.tv.screen.mirroring.screencasting.Weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;

public class MetaballView extends View {
    private final int ITEM_COUNT;
    private final int ITEM_DIVIDER;
    private final float SCALE_RATE;
    private final ArrayList<Circle> circlePaths;
    private final float handle_len_rate;
    private final Paint paint;
    private final float radius;
    private float mInterpolatedTime;
    private float maxLength;

    public MetaballView(Context context) {
        super(context);
        this.paint = new Paint();
        this.handle_len_rate = 2.0f;
        this.radius = 25.0f;
        this.ITEM_COUNT = 6;
        this.ITEM_DIVIDER = 50;
        this.SCALE_RATE = 0.3f;
        this.circlePaths = new ArrayList<>();
        init();
    }

    public MetaballView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint();
        this.handle_len_rate = 2.0f;
        this.radius = 25.0f;
        this.ITEM_COUNT = 6;
        this.ITEM_DIVIDER = 50;
        this.SCALE_RATE = 0.3f;
        this.circlePaths = new ArrayList<>();
        init();
    }

    public MetaballView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.paint = new Paint();
        this.handle_len_rate = 2.0f;
        this.radius = 25.0f;
        this.ITEM_COUNT = 6;
        this.ITEM_DIVIDER = 50;
        this.SCALE_RATE = 0.3f;
        this.circlePaths = new ArrayList<>();
        init();
    }

    private void init() {
        this.paint.setColor(-10076171);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);
        Circle circle = new Circle();
        circle.center = new float[]{75.0f, 32.5f};
        circle.radius = 18.75f;
        this.circlePaths.add(circle);
        for (int i = 1; i < 6; i++) {
            Circle circle2 = new Circle();
            circle2.center = new float[]{i * 100.0f, 32.5f};
            circle2.radius = 25.0f;
            this.circlePaths.add(circle2);
        }
        this.maxLength = 600.0f;
    }

    private float[] getVector(float f, float f2) {
        double d = f;
        double d2 = f2;
        return new float[]{(float) (Math.cos(d) * d2), (float) (Math.sin(d) * d2)};
    }

    private void metaball(Canvas canvas, int i, int i2, float f, float f2, float f3) {
        float f4 = 0;
        float[] fArr;
        float f5;
        Circle circle = this.circlePaths.get(i2);
        Circle circle2 = this.circlePaths.get(i);
        RectF rectF = new RectF();
        rectF.left = circle.center[0] - circle.radius;
        rectF.top = circle.center[1] - circle.radius;
        rectF.right = rectF.left + (circle.radius * 2.0f);
        rectF.bottom = rectF.top + (circle.radius * 2.0f);
        RectF rectF2 = new RectF();
        rectF2.left = circle2.center[0] - circle2.radius;
        rectF2.top = circle2.center[1] - circle2.radius;
        rectF2.right = rectF2.left + (circle2.radius * 2.0f);
        rectF2.bottom = rectF2.top + (circle2.radius * 2.0f);
        float[] fArr2 = {rectF.centerX(), rectF.centerY()};
        float[] fArr3 = {rectF2.centerX(), rectF2.centerY()};
        float distance = getDistance(fArr2, fArr3);
        float width = rectF.width() / 2.0f;
        float width2 = rectF2.width() / 2.0f;
        int i3 = (distance > f3 ? 1 : (distance == f3 ? 0 : -1));
        if (i3 > 0) {
            canvas.drawCircle(rectF2.centerX(), rectF2.centerY(), circle2.radius, this.paint);
        } else {
            width2 *= ((1.0f - (distance / f3)) * 0.3f) + 1.0f;
            canvas.drawCircle(rectF2.centerX(), rectF2.centerY(), width2, this.paint);
        }
        float f6 = 0.0f;
        if (width == 0.0f || width2 == 0.0f || i3 > 0) {
            return;
        }
        if (distance <= Math.abs(width - width2)) {
            return;
        }
        float f7 = width + width2;
        if (distance < f7) {
            float f8 = width * width;
            float f9 = distance * distance;
            float f10 = width2 * width2;
            fArr = fArr3;
            f5 = (float) Math.acos(((f10 + f9) - f8) / ((width2 * 2.0f) * distance));
            f6 = (float) Math.acos(((f8 + f9) - f10) / ((width * 2.0f) * distance));
        } else {
            fArr = fArr3;
            f5 = 0.0f;
        }
        float[] fArr4 = {fArr[0] - fArr2[0], fArr[1] - fArr2[1]};
        double d = fArr4[1];
        float f11 = fArr4[0];
        float[] fArr5 = fArr;
        float atan2 = (float) Math.atan2(d, f11);
        float acos = (float) Math.acos(f4 / distance);
        float f12 = (acos - f6) * f;
        float f13 = atan2 + f6 + f12;
        float f14 = (atan2 - f6) - f12;
        double d2 = atan2;
        double d3 = f5;
        double d4 = ((3.141592653589793d - d3) - acos) * f;
        float f15 = (float) (((d2 + 3.141592653589793d) - d3) - d4);
        float f16 = (float) ((d2 - 3.141592653589793d) + d3 + d4);
        float[] vector = getVector(f13, width);
        float[] vector2 = getVector(f14, width);
        float[] vector3 = getVector(f15, width2);
        float[] vector4 = getVector(f16, width2);
        float[] fArr6 = {vector[0] + fArr2[0], vector[1] + fArr2[1]};
        float[] fArr7 = {vector2[0] + fArr2[0], vector2[1] + fArr2[1]};
        float[] fArr8 = {vector3[0] + fArr5[0], vector3[1] + fArr5[1]};
        float[] fArr9 = {vector4[0] + fArr5[0], vector4[1] + fArr5[1]};
        float min = Math.min(f * f2, getLength(new float[]{fArr6[0] - fArr8[0], fArr6[1] - fArr8[1]}) / f7) * Math.min(1.0f, (distance * 2.0f) / f7);
        float f17 = width * min;
        float f18 = width2 * min;
        float[] vector5 = getVector(f13 - 1.5707964f, f17);
        float[] vector6 = getVector(f15 + 1.5707964f, f18);
        float[] vector7 = getVector(f16 - 1.5707964f, f18);
        float[] vector8 = getVector(f14 + 1.5707964f, f17);
        Path path = new Path();
        path.moveTo(fArr6[0], fArr6[1]);
        path.cubicTo(fArr6[0] + vector5[0], fArr6[1] + vector5[1], fArr8[0] + vector6[0], fArr8[1] + vector6[1], fArr8[0], fArr8[1]);
        path.lineTo(fArr9[0], fArr9[1]);
        path.cubicTo(fArr9[0] + vector7[0], fArr9[1] + vector7[1], fArr7[0] + vector8[0], fArr7[1] + vector8[1], fArr7[0], fArr7[1]);
        path.lineTo(fArr6[0], fArr6[1]);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private float getLength(float[] fArr) {
        return (float) Math.sqrt((fArr[0] * fArr[0]) + (fArr[1] * fArr[1]));
    }

    private float getDistance(float[] fArr, float[] fArr2) {
        float f = fArr[0] - fArr2[0];
        float f2 = fArr[1] - fArr2[1];
        return (float) Math.sqrt((f * f) + (f2 * f2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Circle circle = this.circlePaths.get(0);
        circle.center[0] = this.maxLength * this.mInterpolatedTime;
        RectF rectF = new RectF();
        rectF.left = circle.center[0] - circle.radius;
        rectF.top = circle.center[1] - circle.radius;
        rectF.right = rectF.left + (circle.radius * 2.0f);
        rectF.bottom = rectF.top + (circle.radius * 2.0f);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), circle.radius, this.paint);
        int size = this.circlePaths.size();
        for (int i = 1; i < size; i++) {
            metaball(canvas, i, 0, 0.6f, 2.0f, 100.0f);
        }
    }

    @Override
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(resolveSizeAndState(600, i, 0), resolveSizeAndState(70, i2, 0));
    }

    private void stopAnimation() {
        clearAnimation();
        postInvalidate();
    }

    private void startAnimation() {
        MoveAnimation moveAnimation = new MoveAnimation();
        moveAnimation.setDuration(2500L);
        moveAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        moveAnimation.setRepeatCount(-1);
        moveAnimation.setRepeatMode(2);
        startAnimation(moveAnimation);
    }

    @Override
    protected void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 8 || i == 4) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    public static class Circle {
        float[] center;
        float radius;

        private Circle() {
        }
    }

    public class MoveAnimation extends Animation {
        private MoveAnimation() {
        }

        @Override
        protected void applyTransformation(float f, Transformation transformation) {
            super.applyTransformation(f, transformation);
            MetaballView.this.mInterpolatedTime = f;
            MetaballView.this.invalidate();
        }
    }
}
