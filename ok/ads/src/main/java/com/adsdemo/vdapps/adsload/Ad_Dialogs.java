package com.adsdemo.vdapps.adsload;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsdemo.vdapps.R;
import com.adsdemo.vdapps.adsload.interfaces.InternetStutas;
import com.adsdemo.vdapps.adsload.interfaces.getDialogListner;
import com.willy.ratingbar.ScaleRatingBar;

public class Ad_Dialogs {

    private static boolean aBoolean;

    public static void gotoRedirectDialog(Activity activity) {

        Dialog redirectDialog = new Dialog(activity);
        redirectDialog.requestWindowFeature(1);
        redirectDialog.setCancelable(false);
        redirectDialog.setContentView(R.layout.ad_redirect_dialog);
        TextView btnRedirect = redirectDialog.findViewById(R.id.btnRedirect);
        if (!redirectDialog.isShowing()) {
            redirectDialog.show();
        }

        btnRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + AdsManager.app_newPackageName);
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(myAppLinkToMarket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Dialog gotoInternetDialog(Activity activity, final InternetStutas internetStutas) {

        Dialog dialog2 = new Dialog(activity, R.style.MyAlertDialogTheme);
        dialog2.requestWindowFeature(1);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.ad_no_internet_dialog);

        LinearLayout btnRetry = dialog2.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AdsManager.isInternetAvailable(activity)) {
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    internetStutas.chackInternet(true);
                }
            }
        });

        if (!dialog2.isShowing()) {
            dialog2.show();
        }
        return dialog2;
    }

    public static void showUpdateDialog(final String url, final Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        View view = context.getLayoutInflater().inflate(R.layout.ad_installnewappdialog, null);
        dialog.setContentView(view);
        ImageView update = view.findViewById(R.id.update);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    context.startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return;
    }

    public static void setExitDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity, R.style.MyAlertDialogTheme);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.ad_exit_dialog);
        dialog.setCancelable(true);
        dialog.show();
        AdsManager.CallNativeAdLoad(activity, dialog.findViewById(R.id.native_container), AdsManager.NATIVE_BIG);

        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void isDevMode(Activity context, int result, getDialogListner dialogListner) {

        if (Integer.valueOf(android.os.Build.VERSION.SDK) == 16) {
            aBoolean = android.provider.Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        } else if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 17) {
            aBoolean = android.provider.Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        } else {
            aBoolean = false;
        }
        aBoolean = false;
        Dialog dialog2 = new Dialog(context, R.style.MyAlertDialogTheme);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.ad_developer_option_dialog);
        TextView ivDeveloperOff = dialog2.findViewById(R.id.ivDeveloperOff);
        ivDeveloperOff.setOnClickListener(v -> {
            context.startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS), result);
            dialog2.dismiss();
        });
        if (aBoolean) {
            dialog2.show();
        } else {
            dialogListner.onCallBack();
        }
    }

    public static void setRateDialog(Activity activity) {
        int clicks = Ad_Globals.getRateClick(activity, "RateClick");
        if (AdsManager.RateDialog == 0) {
            return;
        } else {
            if (clicks == AdsManager.RateDialog) {
                Ad_Globals.setRateClick(activity, 0, "RateClick");
                final Dialog dialog = new Dialog(activity, R.style.MyAlertDialogTheme);
                dialog.requestWindowFeature(1);
                dialog.setContentView(R.layout.ad_rate_dialog);
                dialog.setCancelable(true);
                dialog.show();
                SharedPreferences sharedPreferences2 = activity.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences2.edit();
                ScaleRatingBar rotationratingbar_main = (ScaleRatingBar) dialog.findViewById(R.id.rotationratingbar_main);
                TextView tvNever = dialog.findViewById(R.id.tv_never);
                TextView tvLatter = dialog.findViewById(R.id.tv_later);
                TextView tvRate = dialog.findViewById(R.id.tv_rate);

                tvNever.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view) {
                        myEdit.putInt("chackRate", 100);
                        myEdit.commit();
                        dialog.dismiss();
                    }
                });

                tvLatter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view) {
                        myEdit.putInt("chackRate", 0);
                        myEdit.commit();
                        dialog.dismiss();
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
            } else {
                int newclick = clicks + 1;
                Ad_Globals.setRateClick(activity, newclick, "RateClick");
            }
        }
    }
}
