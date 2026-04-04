package com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import java.util.ArrayList;
import java.util.HashMap;


public final class MeowBottomNavigation extends FrameLayout {
    private HashMap<Integer, View> _$_findViewCache;
    private boolean allowDraw;
    private int backgroundBottomColor;
    private BezierView bezierView;
    private boolean callListenerWhenIsSelected;
    private ArrayList<MeowBottomNavigationCell> cells;
    private int circleColor;
    private int countBackgroundColor;
    private int countTextColor;
    private Typeface countTypeface;
    private int defaultIconColor;
    private int heightCell;
    private boolean isAnimating;
    private LinearLayout ll_cells;
    private ArrayList<Model> models;
    private ClickListener onClickedListener;
    private ReselectListener onReselectListener;
    private ShowListener onShowListener;
    private int rippleColor;
    private int selectedIconColor;
    private int selectedId;
    private int shadowColor;

    
    public interface ClickListener {
        void onClickItem(Model model);
    }

    
    public interface ReselectListener {
        void onReselectItem(Model model);
    }

    
    public interface ShowListener {
        void onShowItem(Model model);
    }

    private static void ll_cells$annotations() {
    }

    public MeowBottomNavigation(Context context) {
        super(context);
        this.models = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.selectedId = -1;
        this.defaultIconColor = Color.parseColor("#757575");
        this.selectedIconColor = Color.parseColor("#2196f3");
        this.backgroundBottomColor = Color.parseColor("#ffffff");
        this.circleColor = Color.parseColor("#ffffff");
        this.shadowColor = -4539718;
        this.countTextColor = Color.parseColor("#ffffff");
        this.countBackgroundColor = Color.parseColor("#ff0000");
        this.rippleColor = Color.parseColor("#757575");
        this.heightCell = Utils.dip(getContext(), 90);
        initializeViews();
    }

    public MeowBottomNavigation(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.models = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.selectedId = -1;
        this.defaultIconColor = Color.parseColor("#757575");
        this.selectedIconColor = Color.parseColor("#2196f3");
        this.backgroundBottomColor = Color.parseColor("#ffffff");
        this.circleColor = Color.parseColor("#ffffff");
        this.shadowColor = -4539718;
        this.countTextColor = Color.parseColor("#ffffff");
        this.countBackgroundColor = Color.parseColor("#ff0000");
        this.rippleColor = Color.parseColor("#757575");
        this.heightCell = Utils.dip(getContext(), 90);
        setAttributeFromXml(context, attributeSet);
        initializeViews();
    }

    public MeowBottomNavigation(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.models = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.selectedId = -1;
        this.defaultIconColor = Color.parseColor("#757575");
        this.selectedIconColor = Color.parseColor("#2196f3");
        this.backgroundBottomColor = Color.parseColor("#ffffff");
        this.circleColor = Color.parseColor("#ffffff");
        this.shadowColor = -4539718;
        this.countTextColor = Color.parseColor("#ffffff");
        this.countBackgroundColor = Color.parseColor("#ff0000");
        this.rippleColor = Color.parseColor("#757575");
        this.heightCell = Utils.dip(getContext(), 90);
        setAttributeFromXml(context, attributeSet);
        initializeViews();
    }

    public final ArrayList<Model> getModels() {
        return this.models;
    }

    public final void setModels(ArrayList<Model> arrayList) {
        this.models = arrayList;
    }

    public final ArrayList<MeowBottomNavigationCell> getCells() {
        return this.cells;
    }

    public final int getDefaultIconColor() {
        return this.defaultIconColor;
    }

    public final void setDefaultIconColor(int i) {
        this.defaultIconColor = i;
        updateAllIfAllowDraw();
    }

    public final int getSelectedIconColor() {
        return this.selectedIconColor;
    }

    public final void setSelectedIconColor(int i) {
        this.selectedIconColor = i;
        updateAllIfAllowDraw();
    }

    public final int getBackgroundBottomColor() {
        return this.backgroundBottomColor;
    }

    public final void setBackgroundBottomColor(int i) {
        this.backgroundBottomColor = i;
        updateAllIfAllowDraw();
    }

    public final int getCircleColor() {
        return this.circleColor;
    }

    public final void setCircleColor(int i) {
        this.circleColor = i;
        updateAllIfAllowDraw();
    }

    public final int getCountTextColor() {
        return this.countTextColor;
    }

    public final void setCountTextColor(int i) {
        this.countTextColor = i;
        updateAllIfAllowDraw();
    }

    public final int getCountBackgroundColor() {
        return this.countBackgroundColor;
    }

    public final void setCountBackgroundColor(int i) {
        this.countBackgroundColor = i;
        updateAllIfAllowDraw();
    }

    public final Typeface getCountTypeface() {
        return this.countTypeface;
    }

    public final void setCountTypeface(Typeface typeface) {
        this.countTypeface = typeface;
        updateAllIfAllowDraw();
    }


    @SuppressLint("ResourceType")
    private void setAttributeFromXml(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.MeowBottomNavigation, 0, 0);
        try {
            setDefaultIconColor(obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_defaultIconColor, this.defaultIconColor));
            setSelectedIconColor(obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_selectedIconColor, this.selectedIconColor));
            setBackgroundBottomColor(obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_backgroundBottomColor, this.backgroundBottomColor));
            setCircleColor(obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_circleColor, this.circleColor));
            setCountTextColor(obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_countTextColor, this.countTextColor));
            setCountBackgroundColor(obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_countBackgroundColor, this.countBackgroundColor));
            this.rippleColor = obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_rippleColor, this.rippleColor);
            this.shadowColor = obtainStyledAttributes.getColor(R.styleable.MeowBottomNavigation_mbn_shadowColor, this.shadowColor);
            String string = obtainStyledAttributes.getString(4);
            if (string != null && string.length() > 0) {
                setCountTypeface(Typeface.createFromAsset(context.getAssets(), string));
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void initializeViews() {
        this.ll_cells = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, this.heightCell);
        layoutParams.gravity = 80;
        this.ll_cells.setLayoutParams(layoutParams);
        this.ll_cells.setOrientation(0);
        this.ll_cells.setClipChildren(false);
        this.ll_cells.setClipToPadding(false);
        BezierView bezierView = new BezierView(getContext());
        this.bezierView = bezierView;
        bezierView.setLayoutParams(new LayoutParams(-1, this.heightCell));
        this.bezierView.setColor(this.backgroundBottomColor);
        this.bezierView.setShadowColor(this.shadowColor);
        addView(this.bezierView);
        addView(this.ll_cells);
        this.allowDraw = true;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.selectedId == -1) {
            this.bezierView.setBezierX((Build.VERSION.SDK_INT < 17 || getLayoutDirection() != 1) ? -Utils.dipf(getContext(), 72) : getMeasuredWidth() + Utils.dipf(getContext(), 72));
        }
        int i3 = this.selectedId;
        if (i3 != -1) {
            show(i3, false);
        }
    }

    public final void add(final Model model) {
        final MeowBottomNavigationCell meowBottomNavigationCell = new MeowBottomNavigationCell(getContext());
        meowBottomNavigationCell.setLayoutParams(new LinearLayout.LayoutParams(0, this.heightCell, 1.0f));
        meowBottomNavigationCell.setIcon(model.getIcon());
        meowBottomNavigationCell.setCount(model.getCount());
        meowBottomNavigationCell.setDefaultIconColor(this.defaultIconColor);
        meowBottomNavigationCell.setSelectedIconColor(this.selectedIconColor);
        meowBottomNavigationCell.setCircleColor(this.circleColor);
        meowBottomNavigationCell.setCountTextColor(this.countTextColor);
        meowBottomNavigationCell.setCountBackgroundColor(this.countBackgroundColor);
        meowBottomNavigationCell.setCountTypeface(this.countTypeface);
        meowBottomNavigationCell.setRippleColor(this.rippleColor);
        meowBottomNavigationCell.setOnClickListener(new ClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.MeowBottomNavigation.1
            @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.MeowBottomNavigation.ClickListener
            public void onClickItem(Model model2) {
                if (MeowBottomNavigation.this.isShowing(model.getId())) {
                    MeowBottomNavigation.this.onReselectListener.onReselectItem(model);
                }
                if (!meowBottomNavigationCell.isEnabledCell() && !MeowBottomNavigation.this.isAnimating) {
                    MeowBottomNavigation.show$default(MeowBottomNavigation.this, model.getId(), false, 2, null);
                    MeowBottomNavigation.this.onClickedListener.onClickItem(model);
                } else if (MeowBottomNavigation.this.callListenerWhenIsSelected) {
                    MeowBottomNavigation.this.onClickedListener.onClickItem(model);
                }
            }
        });
        meowBottomNavigationCell.disableCell();
        this.ll_cells.addView(meowBottomNavigationCell);
        this.cells.add(meowBottomNavigationCell);
        this.models.add(model);
    }

    private void updateAllIfAllowDraw() {
        if (this.allowDraw) {
            for (MeowBottomNavigationCell meowBottomNavigationCell : this.cells) {
                meowBottomNavigationCell.setDefaultIconColor(this.defaultIconColor);
                meowBottomNavigationCell.setSelectedIconColor(this.selectedIconColor);
                meowBottomNavigationCell.setCircleColor(this.circleColor);
                meowBottomNavigationCell.setCountTextColor(this.countTextColor);
                meowBottomNavigationCell.setCountBackgroundColor(this.countBackgroundColor);
                meowBottomNavigationCell.setCountTypeface(this.countTypeface);
            }
            this.bezierView.setColor(this.backgroundBottomColor);
        }
    }

    private void anim(final MeowBottomNavigationCell meowBottomNavigationCell, int i, boolean z) {
        this.isAnimating = true;
        int modelPosition = getModelPosition(i);
        int modelPosition2 = getModelPosition(this.selectedId);
        long abs = (Math.abs(modelPosition - (modelPosition2 < 0 ? 0 : modelPosition2)) * 100) + 150;
        long j = z ? abs : 1L;
        final FastOutSlowInInterpolator fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(j);
        ofFloat.setInterpolator(fastOutSlowInInterpolator);
        final float bezierX = this.bezierView.getBezierX();
        final long j2 = j;
        final long j3 = j;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.MeowBottomNavigation$MeowBottomNavigation$anim$$inlined$apply$lambda$1
            final long $animDuration$inlined;
            final FastOutSlowInInterpolator $animInterpolator$inlined;
            final float $beforeX;
            final MeowBottomNavigationCell $cell$inlined;
            final MeowBottomNavigation this$0;

            /* JADX INFO: Access modifiers changed from: package-private */
            {

                this.$beforeX = bezierX;
                this.this$0 = MeowBottomNavigation.this;
                this.$animDuration$inlined = j2;
                this.$animInterpolator$inlined = fastOutSlowInInterpolator;
                this.$cell$inlined = meowBottomNavigationCell;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                BezierView bezierView;
                BezierView bezierView2;
                float animatedFraction = valueAnimator.getAnimatedFraction();
                float x = this.$cell$inlined.getX() + (this.$cell$inlined.getMeasuredWidth() / 2);
                if (x > this.$beforeX) {
                    bezierView2 = this.this$0.bezierView;
                    float f = this.$beforeX;
                    bezierView2.setBezierX(((x - f) * animatedFraction) + f);
                } else {
                    bezierView = this.this$0.bezierView;
                    float f2 = this.$beforeX;
                    bezierView.setBezierX(f2 - ((f2 - x) * animatedFraction));
                }
                if (animatedFraction == 1.0f) {
                    this.this$0.isAnimating = false;
                }
            }
        });
        ofFloat.start();
        if (Math.abs(modelPosition - modelPosition2) > 1) {
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat2.setDuration(j3);
            ofFloat2.setInterpolator(fastOutSlowInInterpolator);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.navigation.MeowBottomNavigation$MeowBottomNavigation$anim$$inlined$apply$lambda$2
                final long $animDuration$inlined;
                final FastOutSlowInInterpolator $animInterpolator$inlined;
                final MeowBottomNavigation this$0;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.this$0 = MeowBottomNavigation.this;
                    this.$animDuration$inlined = j3;
                    this.$animInterpolator$inlined = fastOutSlowInInterpolator;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BezierView bezierView;
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    bezierView = this.this$0.bezierView;
                    bezierView.setProgress(animatedFraction * 2.0f);
                }
            });
            ofFloat2.start();
        }
        meowBottomNavigationCell.setFromLeft(modelPosition > modelPosition2);
        for (MeowBottomNavigationCell meowBottomNavigationCell2 : this.cells) {
            meowBottomNavigationCell2.setDuration(abs);
        }
    }

    static void anim$default(MeowBottomNavigation meowBottomNavigation, MeowBottomNavigationCell meowBottomNavigationCell, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = true;
        }
        meowBottomNavigation.anim(meowBottomNavigationCell, i, z);
    }

    public final void show(int i, boolean z) {
        int size = this.models.size();
        for (int i2 = 0; i2 < size; i2++) {
            Model model = this.models.get(i2);
            MeowBottomNavigationCell meowBottomNavigationCell = this.cells.get(i2);
            if (model.getId() == i) {
                anim(meowBottomNavigationCell, i, z);
                meowBottomNavigationCell.enableCell(z);
                this.onShowListener.onShowItem(model);
            } else {
                meowBottomNavigationCell.disableCell();
            }
        }
        this.selectedId = i;
    }

    public static void show$default(MeowBottomNavigation meowBottomNavigation, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = true;
        }
        meowBottomNavigation.show(i, z);
    }

    public final boolean isShowing(int i) {
        return this.selectedId == i;
    }

    public final Model getModelById(int i) {
        for (Model model : this.models) {
            if (model.getId() == i) {
                return model;
            }
        }
        return null;
    }

    public final MeowBottomNavigationCell getCellById(int i) {
        return this.cells.get(getModelPosition(i));
    }

    public final int getModelPosition(int i) {
        int size = this.models.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (this.models.get(i2).getId() == i) {
                return i2;
            }
        }
        return -1;
    }

    public final void setCount(int i, String str) {
        Model modelById = getModelById(i);
        if (modelById != null) {
            int modelPosition = getModelPosition(i);
            modelById.setCount(str);
            this.cells.get(modelPosition).setCount(str);
        }
    }

    public final void clearCount(int i) {
        Model modelById = getModelById(i);
        if (modelById != null) {
            int modelPosition = getModelPosition(i);
            modelById.setCount(MeowBottomNavigationCell.EMPTY_VALUE);
            this.cells.get(modelPosition).setCount(MeowBottomNavigationCell.EMPTY_VALUE);
        }
    }

    public final void clearAllCounts() {
        for (Model model : this.models) {
            clearCount(model.getId());
        }
    }

    public final void setOnShowListener(ShowListener showListener) {
        this.onShowListener = showListener;
    }

    public final void setOnClickMenuListener(ClickListener clickListener) {
        this.onClickedListener = clickListener;
    }

    public final void setOnReselectListener(ReselectListener reselectListener) {
        this.onReselectListener = reselectListener;
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

    public void _$_clearFindViewByIdCache() {
        HashMap<Integer, View> hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    
    public static class Model {
        private String count = MeowBottomNavigationCell.EMPTY_VALUE;
        private int icon;
        private int id;

        public final String getCount() {
            return this.count;
        }

        public final void setCount(String str) {
            this.count = str;
        }

        public final int getId() {
            return this.id;
        }

        public final void setId(int i) {
            this.id = i;
        }

        public final int getIcon() {
            return this.icon;
        }

        public final void setIcon(int i) {
            this.icon = i;
        }

        public Model(int i, int i2) {
            this.id = i;
            this.icon = i2;
        }
    }
}
