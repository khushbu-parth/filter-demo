package com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component;

import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallLogModel;

import java.util.List;


public interface CallLogsView {
    void onCallLogLoaded(List<CallLogModel> list);
}
