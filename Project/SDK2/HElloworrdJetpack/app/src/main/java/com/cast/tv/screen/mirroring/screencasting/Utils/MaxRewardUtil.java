package com.cast.tv.screen.mirroring.screencasting.Utils;

import com.cast.tv.screen.mirroring.screencasting.CastApp;
import com.cast.tv.screen.mirroring.screencasting.Contract.Contracts;

public class MaxRewardUtil {
    private static int mCastFileNum;

    private MaxRewardUtil() {
    }

    public static void init() {
        mCastFileNum = 10000;
    }

    public static void reduceCastNum() {
        int i = mCastFileNum;
        if (i > 0) {
            mCastFileNum = i - 1;
        }
        L.d("XXX", "mCastFileNum ---- >>> " + mCastFileNum);
        SPUtils.put(CastApp.mContext, Contracts.CAST_FILE_NUM, Integer.valueOf(mCastFileNum));
    }

    public static void rewardCastNum() {
        mCastFileNum++;
        L.d("XXX", "mCastFileNum ---- >>> " + mCastFileNum);
        SPUtils.put(CastApp.mContext, Contracts.CAST_FILE_NUM, Integer.valueOf(mCastFileNum));
    }

    public static boolean isShowRewardDialog() {
        return mCastFileNum <= 1;
    }

    public static int obtainCastFileNum() {
        L.d("XXX", "mCastFileNum ---- >>> " + mCastFileNum);
        return mCastFileNum;
    }
}
