package com.colorcallscreen.colorphone.callscreen.calltheme.Splash;

import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.role.RoleManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Toast;

import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_DEFAULT_APP = 1005;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONSnew = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        findViewById(R.id.btnApplied).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerReplacingDefaultDialer();
                if (Build.VERSION.SDK_INT < 23) {
                    callNext();
                } else if (!Settings.System.canWrite(PermissionActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } else if (Build.VERSION.SDK_INT == 33) {
                    if (checkAndRequestPermissions33()) {
                        callNext();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkAndRequestPermissions()) {
                            callNext();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void openPermissionSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(PermissionActivity.this)) {
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 102);
            }
        }
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(PermissionActivity.this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 103);
        }
    }


    private void offerReplacingDefaultDialer() {
        TelecomManager telecomManager = (TelecomManager) getSystemService("telecom");
        if (telecomManager.getDefaultDialerPackage().equals(getPackageName())) {
        } else if (Build.VERSION.SDK_INT >= 29) {
            RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
            if (roleManager.isRoleAvailable("android.app.role.DIALER") && !roleManager.isRoleHeld("android.app.role.DIALER")) {
                startActivityForResult(roleManager.createRequestRoleIntent("android.app.role.DIALER"), REQUEST_PERMISSIONS_DEFAULT_APP);
            }
        } else if (Build.VERSION.SDK_INT >= 23 && !getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
            Intent intent = new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER");
            intent.putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME", getPackageName());
            startActivityForResult(intent, REQUEST_PERMISSIONS_DEFAULT_APP);
        }
//        if (!Settings.System.canWrite(PermissionActivity.this)) {
//            openPermissionSetting();
//        }
    }
    private boolean checkAndRequestPermissions33() {
        int medialoc = ContextCompat.checkSelfPermission(this, ACCESS_MEDIA_LOCATION);
        int readcon = ContextCompat.checkSelfPermission(this, READ_CONTACTS);
        int readimages = ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES);
        int readaudio = ContextCompat.checkSelfPermission(this, READ_MEDIA_AUDIO);
        int noti = ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS);
        int readcalllog = ContextCompat.checkSelfPermission(this, READ_CALL_LOG);
        int writecalllog = ContextCompat.checkSelfPermission(this, WRITE_CALL_LOG);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (medialoc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_MEDIA_LOCATION);
        }
        if (readcon != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_CONTACTS);
        }
        if (readimages != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_MEDIA_IMAGES);
        }
        if (readaudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_MEDIA_AUDIO);
        }
        if (noti != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(POST_NOTIFICATIONS);
        }
        if (readcalllog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_CALL_LOG);
        }
        if (writecalllog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_CALL_LOG);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONSnew);
            return false;
        }
        return true;

    }


    private boolean checkAndRequestPermissions() {

        int writeStorage = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int reccon = ContextCompat.checkSelfPermission(this, READ_CONTACTS);
        int readcalllog = ContextCompat.checkSelfPermission(this, READ_CALL_LOG);
        int writecalllog = ContextCompat.checkSelfPermission(this, WRITE_CALL_LOG);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE);
        }if (reccon != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_CONTACTS);
        }if (readcalllog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_CALL_LOG);
        }if (writecalllog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_CALL_LOG);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_CALL_LOG, PackageManager.PERMISSION_GRANTED);
                perms.put(WRITE_CALL_LOG, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                            && perms.get(WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED


                    ) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CALL_LOG)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_CALL_LOG)

                        ) {

                            showDialogOK("Permission required for this app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            }
            case REQUEST_ID_MULTIPLE_PERMISSIONSnew:
                Map<String, Integer> perms = new HashMap<>();
                perms.put(ACCESS_MEDIA_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
                perms.put(POST_NOTIFICATIONS, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_CALL_LOG, PackageManager.PERMISSION_GRANTED);
                perms.put(WRITE_CALL_LOG, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && perms.get(POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                            && perms.get(WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED

                    ) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_MEDIA_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_IMAGES)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_AUDIO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, POST_NOTIFICATIONS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CALL_LOG)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_CALL_LOG)
                        ) {

                            showDialogOK("Permission required for this app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(PermissionActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    private void callNext() {
        startActivity(new Intent(PermissionActivity.this, MainActivity.class));
        getSharedPreferences(getPackageName(), 0).edit().putBoolean("apppermi", true).apply();

    }
}