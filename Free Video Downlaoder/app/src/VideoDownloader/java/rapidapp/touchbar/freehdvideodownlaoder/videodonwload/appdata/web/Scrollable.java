package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.web;

import android.view.ViewGroup;

public interface Scrollable {
    void addScrollViewCallbacks(ObservableScrollViewCallbacks observableScrollViewCallbacks);

    void clearScrollViewCallbacks();

    int getCurrentScrollY();

    void removeScrollViewCallbacks(ObservableScrollViewCallbacks observableScrollViewCallbacks);

    void scrollVerticallyTo(int i);

    @Deprecated
    void setScrollViewCallbacks(ObservableScrollViewCallbacks observableScrollViewCallbacks);

    void setTouchInterceptionViewGroup(ViewGroup viewGroup);
}
