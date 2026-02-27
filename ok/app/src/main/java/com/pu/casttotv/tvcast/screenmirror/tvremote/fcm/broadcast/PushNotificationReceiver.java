package com.pu.casttotv.tvcast.screenmirror.tvremote.fcm.broadcast;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.gson.Gson;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ModelSaleAll;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium.IapUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;

import java.util.Random;

public class PushNotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String str;
        if (!IapUtils.isIapAll()) {
            ModelSaleAll supperSale = SharedPrefsUtil.getInstance().getSupperSale();
            StringBuilder sb = new StringBuilder();
            sb.append(new Gson().toJson(supperSale));
            String str2 = "";
            sb.append(str2);
            if (supperSale == null || supperSale.getListNotification() == null || supperSale.getListNotification().size() <= 0) {
                str = str2;
            } else {
                int randomNumberInRange = getRandomNumberInRange(0, supperSale.getListNotification().size() - 1);
                String title = supperSale.getListNotification().get(randomNumberInRange).getTitle();
                str = supperSale.getListNotification().get(randomNumberInRange).getDes();
                str2 = title;
            }
            if (str2.isEmpty()) {
                str2 = "Glitter Cast Remote Premium";
                str = "For a limited time we’re offering an extra 50% off all sale items. Act now, once they’re gone they’re gone!";
            }
            Uri defaultUri = RingtoneManager.getDefaultUri(2);
            long currentTimeMillis = System.currentTimeMillis();
            if (Build.VERSION.SDK_INT >= 26) {
                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel("ID", "Cast tv", 3);
                notificationChannel.setDescription("Cast tv");
//                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
//                notificationManager.createNotificationChannel(notificationChannel);
//                Intent intent2 = new Intent(context, SalePremiumActivity.class);
//                intent2.setFlags(268468224);
//                notificationManager.notify(5, new NotificationCompat.Builder(context, "ID").setContentTitle(str2).setContentText(str).setSound(defaultUri).setAutoCancel(true).setWhen(currentTimeMillis).setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)).setStyle(new NotificationCompat.BigTextStyle().bigText(str)).setContentIntent(PendingIntent.getActivity(context, 0, intent2, 0)).build());
                return;
            }
//            Intent intent3 = new Intent(context, SalePremiumActivity.class);
//            intent3.setFlags(67108864);
//            PendingIntent activity = PendingIntent.getActivity(context, 0, intent3, 134217728);
//            ((NotificationManager) context.getSystemService("notification")).notify(5, new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(str2).setContentText(str).setSound(defaultUri).setAutoCancel(true).setWhen(currentTimeMillis).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)).setStyle(new NotificationCompat.BigTextStyle().bigText(str)).setContentIntent(activity).build());
        }
    }

    private int getRandomNumberInRange(int i, int i2) {
        if (i < i2) {
            return new Random().nextInt((i2 - i) + 1) + i;
        }
        throw new IllegalArgumentException("max must be greater than min");
    }
}
