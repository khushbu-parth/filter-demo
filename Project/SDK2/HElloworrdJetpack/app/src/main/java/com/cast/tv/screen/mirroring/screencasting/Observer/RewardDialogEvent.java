package com.cast.tv.screen.mirroring.screencasting.Observer;

import org.greenrobot.eventbus.EventBus;

public class RewardDialogEvent {
    public int mViewType;

    public static void post(int i) {
        RewardDialogEvent rewardDialogEvent = new RewardDialogEvent();
        rewardDialogEvent.mViewType = i;
        EventBus.getDefault().post(rewardDialogEvent);
    }
}
