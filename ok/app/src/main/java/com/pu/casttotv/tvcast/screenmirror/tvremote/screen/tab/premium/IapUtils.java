package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium;

import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Const;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;

/* loaded from: classes4.dex */
public class IapUtils {
    private static IapUtils purchaseUtil;

    public static String getKeyFlashSale() {
        return "purchase_key_flash_sale";
    }

    public static String getKeyMonth() {
        return "month_pack";
    }

    public static String getKeyOnetime() {
        return "all_pack";
    }

    public static String getKeySaleOnetime() {
        return "all_pack_sale";
    }

    public static String getKeyWeek() {
        return "week_pack_change_year";
    }

    public static String getSaleMonth() {
        return "month_pack_sale";
    }

    public static String getSaleMonthHigh() {
        return "month_high_pack_sale";
    }

    public static String getSaleOneTimeHigh() {
        return "subs_yearly_high";
    }

    public static String getSaleWeek() {
        return "week_pack_sale";
    }

    public static IapUtils getInstance() {
        if (purchaseUtil == null) {
            purchaseUtil = new IapUtils();
        }
        return purchaseUtil;
    }

    public static boolean isIapAll() {
        try {
            return ((Boolean) SharedPrefsUtil.getInstance().get(Const.KEY_IAP_ALL, Boolean.class)).booleanValue();
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public void setIapAll(boolean z) {
        try {
            SharedPrefsUtil.getInstance().put(Const.KEY_IAP_ALL, Boolean.valueOf(z));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean isPaymentMirror() {
        try {
            return ((Boolean) SharedPrefsUtil.getInstance().get(Const.KEY_IAP_SCREEN_MIRROR, Boolean.class)).booleanValue();
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
