package com.mancj.materialsearchbar;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.lang.reflect.Field;

public class EditTextStyleHelper {
    private final Integer cursorColor;
    private final EditText editText;
    private final boolean handlesUseTintEnabled;
    private final Integer selectHandleLeftColor;
    private final Integer selectHandleLeftDrawable;
    private final Integer selectHandleMiddleColor;
    private final Integer selectHandleMiddleDrawable;
    private final Integer selectHandleRightColor;
    private final Integer selectHandleRightDrawable;

    public static void applyChanges(EditText editText2, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z) throws EditTextStyleChangeError {
        new Builder(editText2).setCursorColor(i).setSelectHandleLeftColor(i2).setSelectHandleRightColor(i3).setSelectHandleMiddleColor(i4).setSelectHandleLeftDrawable(i5).setSelectHandleRightDrawable(i6).setSelectHandleMiddleDrawable(i7).setHandleTintEnabled(z).build().apply();
    }

    private EditTextStyleHelper(Builder builder) {
        this.editText = builder.editText;
        this.cursorColor = builder.cursorColor;
        this.selectHandleLeftColor = builder.selectHandleLeftColor;
        this.selectHandleRightColor = builder.selectHandleRightColor;
        this.selectHandleMiddleColor = builder.selectHandleMiddleColor;
        this.selectHandleLeftDrawable = builder.selectHandleLeftDrawable;
        this.selectHandleRightDrawable = builder.selectHandleRightDrawable;
        this.selectHandleMiddleDrawable = builder.selectHandleMiddleDrawable;
        this.handlesUseTintEnabled = builder.handlesUseTintEnabled;
    }

    public void apply() throws EditTextStyleChangeError {
        try {
            Field declaredField = TextView.class.getDeclaredField("mEditor");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this.editText);
            if (this.cursorColor != null) {
                Field declaredField2 = TextView.class.getDeclaredField("mCursorDrawableRes");
                declaredField2.setAccessible(true);
                Drawable mutate = ContextCompat.getDrawable(this.editText.getContext(), declaredField2.getInt(this.editText)).mutate();
                mutate.setColorFilter(this.cursorColor.intValue(), PorterDuff.Mode.SRC_IN);
                Drawable[] drawableArr = {mutate, mutate};
                Field declaredField3 = obj.getClass().getDeclaredField("mCursorDrawable");
                declaredField3.setAccessible(true);
                declaredField3.set(obj, drawableArr);
            }
            String[] strArr = {"mTextSelectHandleLeftRes", "mTextSelectHandleRightRes", "mTextSelectHandleRes"};
            String[] strArr2 = {"mSelectHandleLeft", "mSelectHandleRight", "mSelectHandleCenter"};
            Integer[] numArr = {this.selectHandleLeftColor, this.selectHandleRightColor, this.selectHandleMiddleColor};
            Integer[] numArr2 = {this.selectHandleLeftDrawable, this.selectHandleRightDrawable, this.selectHandleMiddleDrawable};
            for (int i = 0; i < 3; i++) {
                Integer num = numArr[i];
                Integer num2 = numArr2[i];
                if (num != null) {
                    String str = strArr[i];
                    String str2 = strArr2[i];
                    TextView.class.getDeclaredField(str).setAccessible(true);
                    Drawable mutate2 = ContextCompat.getDrawable(this.editText.getContext(), num2.intValue()).mutate();
                    if (this.handlesUseTintEnabled) {
                        mutate2.setColorFilter(num.intValue(), PorterDuff.Mode.SRC_IN);
                    } else {
                        mutate2.clearColorFilter();
                    }
                    Field declaredField4 = obj.getClass().getDeclaredField(str2);
                    declaredField4.setAccessible(true);
                    declaredField4.set(obj, mutate2);
                }
            }
        } catch (Exception e) {
            throw new EditTextStyleChangeError("Error applying changes to " + this.editText, e);
        }
    }

    public static class Builder {
        Integer cursorColor;
        final EditText editText;
        boolean handlesUseTintEnabled;
        Integer selectHandleLeftColor;
        Integer selectHandleLeftDrawable;
        Integer selectHandleMiddleColor;
        Integer selectHandleMiddleDrawable;
        Integer selectHandleRightColor;
        Integer selectHandleRightDrawable;

        public Builder(EditText editText2) {
            this.editText = editText2;
        }

        public Builder setCursorColor(int i) {
            this.cursorColor = Integer.valueOf(i);
            return this;
        }

        public Builder setSelectHandleLeftColor(int i) {
            this.selectHandleLeftColor = Integer.valueOf(i);
            return this;
        }

        public Builder setSelectHandleRightColor(int i) {
            this.selectHandleRightColor = Integer.valueOf(i);
            return this;
        }

        public Builder setSelectHandleMiddleColor(int i) {
            this.selectHandleMiddleColor = Integer.valueOf(i);
            return this;
        }

        public Builder setSelectHandleLeftDrawable(int i) {
            this.selectHandleLeftDrawable = Integer.valueOf(i);
            return this;
        }

        public Builder setSelectHandleRightDrawable(int i) {
            this.selectHandleRightDrawable = Integer.valueOf(i);
            return this;
        }

        public Builder setSelectHandleMiddleDrawable(int i) {
            this.selectHandleMiddleDrawable = Integer.valueOf(i);
            return this;
        }

        public Builder setHandleTintEnabled(boolean z) {
            this.handlesUseTintEnabled = z;
            return this;
        }

        public EditTextStyleHelper build() {
            return new EditTextStyleHelper(this);
        }
    }

    public static class EditTextStyleChangeError extends Exception {
        public EditTextStyleChangeError(String str, Throwable th) {
            super(str, th);
        }
    }
}
