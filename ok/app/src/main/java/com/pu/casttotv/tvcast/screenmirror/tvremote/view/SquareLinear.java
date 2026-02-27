package com.pu.casttotv.tvcast.screenmirror.tvremote.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class SquareLinear extends LinearLayout {
    public SquareLinear(Context context) {
        super(context);
    }

    public SquareLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinear(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

    }
}
