package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.web;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

public class ObservableWebView extends WebView implements Scrollable {
    private List<ObservableScrollViewCallbacks> mCallbackCollection;
    private ObservableScrollViewCallbacks mCallbacks;
    private boolean mDragging;
    private boolean mFirstScroll;
    private boolean mIntercepted;
    private MotionEvent mPrevMoveEvent;
    private int mPrevScrollY;
    private ScrollState mScrollState;
    private int mScrollY;
    private ViewGroup mTouchInterceptionViewGroup;

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (byte) 0);
            }

            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int a;
        int b;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.a = parcel.readInt();
            this.b = parcel.readInt();
        }

        SavedState(Parcel parcel, byte b2) {
            this(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.a);
            parcel.writeInt(this.b);
        }
    }

    public ObservableWebView(Context context) {
        super(context);
    }

    public ObservableWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ObservableWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private void dispatchOnDownMotionEvent() {
        ObservableScrollViewCallbacks observableScrollViewCallbacks = this.mCallbacks;
        if (observableScrollViewCallbacks != null) {
            observableScrollViewCallbacks.onDownMotionEvent();
        }
        if (this.mCallbackCollection != null) {
            for (int i = 0; i < this.mCallbackCollection.size(); i++) {
                this.mCallbackCollection.get(i).onDownMotionEvent();
            }
        }
    }

    private void dispatchOnScrollChanged(int i, boolean z, boolean z2) {
        ObservableScrollViewCallbacks observableScrollViewCallbacks = this.mCallbacks;
        if (observableScrollViewCallbacks != null) {
            observableScrollViewCallbacks.onScrollChanged(i, z, z2);
        }
        if (this.mCallbackCollection != null) {
            for (int i2 = 0; i2 < this.mCallbackCollection.size(); i2++) {
                this.mCallbackCollection.get(i2).onScrollChanged(i, z, z2);
            }
        }
    }

    private void dispatchOnUpOrCancelMotionEvent(ScrollState scrollState) {
        ObservableScrollViewCallbacks observableScrollViewCallbacks = this.mCallbacks;
        if (observableScrollViewCallbacks != null) {
            observableScrollViewCallbacks.onUpOrCancelMotionEvent(scrollState);
        }
        if (this.mCallbackCollection != null) {
            for (int i = 0; i < this.mCallbackCollection.size(); i++) {
                this.mCallbackCollection.get(i).onUpOrCancelMotionEvent(scrollState);
            }
        }
    }

    private boolean hasNoCallbacks() {
        return this.mCallbacks == null && this.mCallbackCollection == null;
    }

    public void addScrollViewCallbacks(ObservableScrollViewCallbacks observableScrollViewCallbacks) {
        if (this.mCallbackCollection == null) {
            this.mCallbackCollection = new ArrayList();
        }
        this.mCallbackCollection.add(observableScrollViewCallbacks);
    }

    public void clearScrollViewCallbacks() {
        List<ObservableScrollViewCallbacks> list = this.mCallbackCollection;
        if (list != null) {
            list.clear();
        }
    }

    public int getCurrentScrollY() {
        return this.mScrollY;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (hasNoCallbacks()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (motionEvent.getActionMasked() == 0) {
            this.mDragging = true;
            this.mFirstScroll = true;
            dispatchOnDownMotionEvent();
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        this.mPrevScrollY = savedState.a;
        this.mScrollY = savedState.b;
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.a = this.mPrevScrollY;
        savedState.b = this.mScrollY;
        return savedState;
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if (!hasNoCallbacks()) {
            this.mScrollY = i2;
            dispatchOnScrollChanged(i2, this.mFirstScroll, this.mDragging);
            if (this.mFirstScroll) {
                this.mFirstScroll = false;
            }
            int i5 = this.mPrevScrollY;
            this.mScrollState = i5 < i2 ? ScrollState.UP : i2 < i5 ? ScrollState.DOWN : ScrollState.STOP;
            this.mPrevScrollY = i2;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (hasNoCallbacks()) {
            return super.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                if (this.mPrevMoveEvent == null) {
                    this.mPrevMoveEvent = motionEvent;
                }
                float y = motionEvent.getY() - this.mPrevMoveEvent.getY();
                this.mPrevMoveEvent = MotionEvent.obtainNoHistory(motionEvent);
                float currentScrollY = ((float) getCurrentScrollY()) - y;
                float f = 0.0f;
                if (currentScrollY <= 0.0f) {
                    if (this.mIntercepted) {
                        return false;
                    }
                    ViewGroup viewGroup = this.mTouchInterceptionViewGroup;
                    if (viewGroup == null) {
                        viewGroup = (ViewGroup) getParent();
                    }
                    float f2 = 0.0f;
                    View view = this;
                    while (view != null && view != viewGroup) {
                        f += (float) (view.getLeft() - view.getScrollX());
                        f2 += (float) (view.getTop() - view.getScrollY());
                        view = (View) view.getParent();
                    }
                    final MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
                    obtainNoHistory.offsetLocation(f, f2);
                    if (!viewGroup.onInterceptTouchEvent(obtainNoHistory)) {
                        return super.onTouchEvent(motionEvent);
                    }
                    this.mIntercepted = true;
                    obtainNoHistory.setAction(0);
                    final ViewGroup finalViewGroup = viewGroup;
                    post(new Runnable() {
                        public void run() {
                            finalViewGroup.dispatchTouchEvent(obtainNoHistory);
                        }
                    });
                    return false;
                }
            }
            return super.onTouchEvent(motionEvent);
        }
        this.mIntercepted = false;
        this.mDragging = false;
        dispatchOnUpOrCancelMotionEvent(this.mScrollState);
        return super.onTouchEvent(motionEvent);
    }


    public void removeScrollViewCallbacks(ObservableScrollViewCallbacks observableScrollViewCallbacks) {
        List<ObservableScrollViewCallbacks> list = this.mCallbackCollection;
        if (list != null) {
            list.remove(observableScrollViewCallbacks);
        }
    }

    public void scrollVerticallyTo(int i) {
        scrollTo(0, i);
    }

    public void setScrollViewCallbacks(ObservableScrollViewCallbacks observableScrollViewCallbacks) {
        this.mCallbacks = observableScrollViewCallbacks;
    }

    public void setTouchInterceptionViewGroup(ViewGroup viewGroup) {
        this.mTouchInterceptionViewGroup = viewGroup;
    }
}
