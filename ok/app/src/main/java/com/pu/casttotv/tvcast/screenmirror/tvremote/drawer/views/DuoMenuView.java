package com.pu.casttotv.tvcast.screenmirror.tvremote.drawer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.Toolbar;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;


/**
 * Created by PSD on 13-04-17.
 */

public class DuoMenuView extends RelativeLayout {
    private static final String TAG_FOOTER = "footer";
    private static final String TAG_HEADER = "header";
    @DrawableRes
    private static final int DEFAULT_DRAWABLE_ATTRIBUTE_VALUE = 0b11111111111111110010101111001111;
    @LayoutRes
    private static final int DEFAULT_LAYOUT_ATTRIBUTE_VALUE = 0b11111111111111110010101111010000;
    @DrawableRes
    private int mBackgroundDrawableId;
    @LayoutRes
    private int mHeaderViewId;
    @LayoutRes
    private int mFooterViewId;

    private OnMenuClickListener mOnMenuClickListener;
    private DataSetObserver mDataSetObserver;
    private MenuViewHolder mMenuViewHolder;
    private LayoutInflater mLayoutInflater;
    private Adapter mAdapter;
    private ImageView ivProfile;
    private TextView txtFullName;

    public DuoMenuView(Context context) {
        this(context, null);
    }

    public DuoMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public DuoMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(attrs);
        initialize();
    }

    private void readAttributes(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.DuoMenuView);

        try {
            mBackgroundDrawableId = typedArray.getResourceId(R.styleable.DuoMenuView_background, DEFAULT_DRAWABLE_ATTRIBUTE_VALUE);
            mHeaderViewId = typedArray.getResourceId(R.styleable.DuoMenuView_header, DEFAULT_LAYOUT_ATTRIBUTE_VALUE);
            mFooterViewId = typedArray.getResourceId(R.styleable.DuoMenuView_footer, DEFAULT_LAYOUT_ATTRIBUTE_VALUE);
        } finally {
            typedArray.recycle();
        }
    }


    private void initialize() {
        ViewGroup rootView = (ViewGroup) inflate(getContext(), R.layout.duo_view_menu, this);

        mMenuViewHolder = new MenuViewHolder(rootView);
        mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                postInvalidate();
                requestLayout();
            }
        };


    }

    private int getPrimaryColor() {
        TypedArray typedArray = getContext().obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        return color;
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }

    public View getHeaderView() {
        return findViewWithTag(TAG_HEADER);
    }

    public void setHeaderView(@LayoutRes int headerViewId) {
        mHeaderViewId = headerViewId;
//        handleHeader();
    }

    public View getFooterView() {
        return findViewWithTag(TAG_FOOTER);
    }

    public void setFooterView(@LayoutRes int footerViewId) {
        mFooterViewId = footerViewId;
//        handleFooter();
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) mAdapter.unregisterDataSetObserver(mDataSetObserver);
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataSetObserver);
//        handleOptions();
    }

    private void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof Toolbar) {
                    setViewAndChildrenEnabled(child, true);
                } else {
                    setViewAndChildrenEnabled(child, enabled);
                }
            }
        }
    }


    /**
     * Listener that listens to menu click events.
     */
    public interface OnMenuClickListener {

        public void onClick(String type);
    }

    /**
     * Holds the views in this menu
     */
    private class MenuViewHolder {

        LinearLayout ll_applayout, ll_screen_cast, ll_photo_cast, ll_video_cast, ll_music_cast, ll_youtube_cast, ll_google_cast, ll_vimeo_cast, ll_setting_cast, ll_sliderad;

        MenuViewHolder(ViewGroup rootView) {

            ll_applayout = rootView.findViewById(R.id.ll_applayout);
            ll_applayout.setOnClickListener(v -> mOnMenuClickListener.onClick("0"));

            ll_screen_cast = rootView.findViewById(R.id.ll_screen_cast);
            ll_screen_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("1"));

            ll_photo_cast = rootView.findViewById(R.id.ll_photo_cast);
            ll_photo_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("2"));

            ll_video_cast = rootView.findViewById(R.id.ll_video_cast);
            ll_video_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("3"));

            ll_music_cast = rootView.findViewById(R.id.ll_music_cast);
            ll_music_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("4"));

            ll_youtube_cast = rootView.findViewById(R.id.ll_youtube_cast);
            ll_youtube_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("5"));

            ll_google_cast = rootView.findViewById(R.id.ll_google_cast);
            ll_google_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("6"));

            ll_vimeo_cast = rootView.findViewById(R.id.ll_vimeo_cast);
            ll_vimeo_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("7"));

            ll_setting_cast = rootView.findViewById(R.id.ll_setting_cast);
            ll_setting_cast.setOnClickListener(v -> mOnMenuClickListener.onClick("8"));

            ll_sliderad = rootView.findViewById(R.id.ll_sliderad);
            ll_sliderad.setOnClickListener(v -> mOnMenuClickListener.onClick("9"));


        }

    }
}
