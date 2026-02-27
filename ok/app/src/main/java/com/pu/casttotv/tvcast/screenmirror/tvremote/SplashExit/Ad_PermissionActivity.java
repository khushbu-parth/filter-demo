package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;


public class Ad_PermissionActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_permission);

        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_MIDEUM);

        findViewById(R.id.tv_allow_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    gotonext();
                } else {
                    askPermission();
                }
            }
        });
    }

    private void gotonext() {
        AdsManager.CallInterstitialAdLoad(Ad_PermissionActivity.this, 0, new MyCallback() {
            @Override
            public void callbackCall() {
                if (AdsManager.CountryScreen == 1) {
                    Intent intent = new Intent(Ad_PermissionActivity.this, Ad_CountryActivity.class);
                    startActivity(intent);
                } else if (AdsManager.LanguageScreen == 1) {
                    Intent intent = new Intent(Ad_PermissionActivity.this, Ad_LanguageActivity.class);
                    startActivity(intent);
                } else if (AdsManager.SwipeScreen == 1) {
                    Intent intent = new Intent(Ad_PermissionActivity.this, Ad_SwipeScreenActivity.class);
                    startActivity(intent);
                } else if (AdsManager.AppPurchaseScreen == 1) {
                    Intent intent = new Intent(Ad_PermissionActivity.this, Ad_AppPurchaseActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Ad_PermissionActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        AdsManager.CallInterstitialAdLoad(this, 1, new MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO);
            int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES);
            int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO);
            return result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;
        }
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            }, 68);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 68);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 68) {
            if (grantResults.length > 0) {
                boolean locationAccepted;
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                    locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED;
                } else {
                    locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                }
                if (locationAccepted)
                    gotonext();
                else {
                    Toast.makeText(this, "Permission Denied, You cannot access storage data.", Toast.LENGTH_SHORT).show();
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showMessageOKCancel("You need to allow access to both the permissions",
                                (dialog, which) -> {
                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                                        requestPermissions(new String[]{
                                                Manifest.permission.READ_MEDIA_AUDIO,
                                                Manifest.permission.READ_MEDIA_IMAGES,
                                                Manifest.permission.READ_MEDIA_VIDEO
                                        }, 68);
                                        return;
                                    }
                                    requestPermissions(new String[]{
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    }, 68);
                                });
                        return;
                    }
                }
            }
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Ad_PermissionActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}