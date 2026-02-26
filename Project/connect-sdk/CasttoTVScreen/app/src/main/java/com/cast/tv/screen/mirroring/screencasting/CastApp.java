package com.cast.tv.screen.mirroring.screencasting;

import android.content.Context;
import androidx.multidex.MultiDex;
import com.cast.tv.screen.mirroring.screencasting.Contract.Contracts;
import com.cast.tv.screen.mirroring.screencasting.Contract.SPContracts;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.SPUtils;
import com.cast.tv.screen.mirroring.screencasting.Utils.TimeUtil;


public class CastApp extends com.library.info.MyApplication {
    public static boolean DEBUG = false;
    public static String TAG = "CastApp";
    public static Context mContext = null;
    public static int mIntoRCNum = 1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initUseDate();
        initUmeng();
        initMaxAd();
    }

    private void initUmeng() {
        if (!((Boolean) SPUtils.get(mContext, SPContracts.VERSION_REPORT_AGAIN, false)).booleanValue()) {
            SPUtils.put(mContext, SPContracts.REPORT_EVENT_IDS, "");
            SPUtils.put(mContext, SPContracts.VERSION_REPORT_AGAIN, true);
        }
    }

    private void initMaxAd() {
        MaxRewardUtil.init();
    }

    private void initUseDate() {
        int intValue = ((Integer) SPUtils.get(mContext, Contracts.USE_APP_DATE, 0)).intValue();
        int obtainCurDateInt = TimeUtil.obtainCurDateInt();
        L.i("XXX", "ud: " + intValue + ", cd: " + obtainCurDateInt);
        if (intValue <= 0) {
            SPUtils.put(mContext, Contracts.USE_APP_DATE, Integer.valueOf(obtainCurDateInt));
            SPUtils.put(mContext, Contracts.USER_APP_NUM, 1);
            SPUtils.put(mContext, Contracts.CAST_FILE_NUM, 3);
        } else if (intValue == obtainCurDateInt) {
        } else {
            SPUtils.put(mContext, Contracts.USE_APP_DATE, Integer.valueOf(obtainCurDateInt));
            SPUtils.put(mContext, Contracts.CLICK_SCREEN_MIRROR, 0);
            SPUtils.put(mContext, Contracts.CLICK_PAV_NUM, 0);
            SPUtils.put(mContext, Contracts.CLICK_RC_NUM, 0);
            int intValue2 = ((Integer) SPUtils.get(mContext, Contracts.USER_APP_NUM, 0)).intValue() + 1;
            SPUtils.put(mContext, Contracts.USER_APP_NUM, Integer.valueOf(intValue2));
            if (intValue2 >= 8) {
                SPUtils.put(mContext, Contracts.CAST_FILE_NUM, 1);
                if (intValue2 >= 10) {
                    mIntoRCNum = 2;
                }
            } else {
                SPUtils.put(mContext, Contracts.CAST_FILE_NUM, 2);
            }
            L.i("XXX", "useNum: " + intValue2);
        }
    }
}
