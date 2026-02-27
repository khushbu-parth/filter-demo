package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Globle;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adsdemo.vdapps.adsload.Ad_Dialogs;
import com.adsdemo.vdapps.adsload.AdsManager;

import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Ad_ExitActivity;
import com.willy.ratingbar.ScaleRatingBar;

public class Ad_Dialog {
    public static void setExitRateDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity, com.adsdemo.vdapps.R.style.MyAlertDialogTheme);
        dialog.requestWindowFeature(1);
        dialog.setContentView(com.adsdemo.vdapps.R.layout.ad_exit_rate_dialog);
        dialog.setCancelable(true);
        dialog.show();
        SharedPreferences sharedPreferences2 = activity.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences2.edit();
        ScaleRatingBar rotationratingbar_main = (ScaleRatingBar) dialog.findViewById(com.adsdemo.vdapps.R.id.rotationratingbar_main);

        TextView tvNever = dialog.findViewById(com.adsdemo.vdapps.R.id.tv_never);
        TextView tvLatter = dialog.findViewById(com.adsdemo.vdapps.R.id.tv_later);
        TextView tvRate = dialog.findViewById(com.adsdemo.vdapps.R.id.tv_rate);

        tvNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                myEdit.putInt("chackRate", 100);
                myEdit.commit();
                dialog.dismiss();
                if (AdsManager.ExitScreen == 1) {
                    activity.startActivity(new Intent(activity, Ad_ExitActivity.class));
                } else {
                    Ad_Dialogs.setExitDialog(activity);
                }
            }
        });

        tvLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                myEdit.putInt("chackRate", 0);
                myEdit.commit();
                dialog.dismiss();
                if (AdsManager.ExitScreen == 1) {
                    activity.startActivity(new Intent(activity, Ad_ExitActivity.class));
                } else {
                    Ad_Dialogs.setExitDialog(activity);
                }
            }
        });

        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {

                int rate = (int) rotationratingbar_main.getRating();
                if (rate == 0) {
                    Toast.makeText(activity, "Please Give Some Rate, Touch on Star.", Toast.LENGTH_SHORT).show();
                } else if (rate <= 3) {

                    myEdit.putInt("chackRate", 0);
                    myEdit.commit();
                    dialog.dismiss();
                    if (AdsManager.ExitScreen == 1) {
                        activity.startActivity(new Intent(activity, Ad_ExitActivity.class));
                    } else {
                        Ad_Dialogs.setExitDialog(activity);
                    }
                } else {
                    Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        activity.startActivity(myAppLinkToMarket);
                        myEdit.putInt("chackRate", 100);
                        myEdit.commit();
                        dialog.dismiss();
                    } catch (ActivityNotFoundException e) {
                        myEdit.putInt("chackRate", 100);
                        myEdit.commit();
                        dialog.dismiss();
                        Toast.makeText(activity, "Unable To Find Market App", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
