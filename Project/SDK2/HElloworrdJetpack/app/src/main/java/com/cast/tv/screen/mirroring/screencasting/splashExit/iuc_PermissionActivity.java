package com.cast.tv.screen.mirroring.screencasting.splashExit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetStateChangeReceiver;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.library.info.CastTvAppManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class iuc_PermissionActivity extends AppCompatActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONSnew = 2;
    private ImageView allowPermission, startCast;
    private boolean blnStart = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        NetStateChangeReceiver.registerReceiver(this);

        CastTvAppManager.getInstance(this).showFirstNativeAds(
                this,
                (ViewGroup) findViewById(R.id.fl_native),
                (ImageView) findViewById(R.id.native_space_img),
                1
        );


        allowPermission = findViewById(R.id.allowPermissionImage);

        allowPermission.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 23) {
                    callnext();
                } else if (Build.VERSION.SDK_INT == 33) {
                    if (checkAndRequestPermissions33()) {
                        callnext();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkAndRequestPermissions()) {
                            callnext();
                        }
                    }
                }
            }
        });

    }

    private void callnext() {
//        Intent intent;
//        if (AppManager.startScreen == 1) {
//            intent = new Intent(this, InfoActivity.class);
//        } else {
//            intent = new Intent(this, InfoActivity.class);
//        }
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);


        CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
            startActivity(new Intent(iuc_PermissionActivity.this, InfoActivity.class));
            finish();
        });


    }

    public void mPermission() {
        Dexter.withContext(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION",
                        "android.permission.RECORD_AUDIO").
                withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            callnext();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void callnextactivity() {
        startActivity(new Intent(iuc_PermissionActivity.this, First_Activity.class));
        finish();
    }

    private boolean checkAndRequestPermissions33() {
        int medialoc = ContextCompat.checkSelfPermission(this, ACCESS_MEDIA_LOCATION);
        int readimages = ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES);
        int readaudio = ContextCompat.checkSelfPermission(this, READ_MEDIA_AUDIO);
        int readvideo = ContextCompat.checkSelfPermission(this, READ_MEDIA_VIDEO);
        int recordaudio = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);
        int accloc = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        int fineloc = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (medialoc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_MEDIA_LOCATION);
        }
        if (readimages != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_MEDIA_IMAGES);
        }
        if (readaudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_MEDIA_AUDIO);
        }
        if (readvideo != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_MEDIA_VIDEO);
        }
        if (recordaudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(RECORD_AUDIO);
        }
        if (accloc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_COARSE_LOCATION);
        }
        if (fineloc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONSnew);
            return false;
        }
        return true;
    }

    private boolean checkAndRequestPermissions() {

        int writeStorage = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int fin_loc = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int cross = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        int recaudi = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
        }
        if (fin_loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
        }
        if (cross != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_COARSE_LOCATION);
        }
        if (recaudi != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(RECORD_AUDIO);
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
                // Initialize the map with both permissions

                perms.put(WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

                    ) {
                        if (blnStart == true) {
                            callnextactivity();
                        }
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, RECORD_AUDIO)


                        ) {

                            showDialogOK("Permission required for this app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            // proceed with logic by disabling the related features or quit the app.
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
                perms.put(READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_MEDIA_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_MEDIA_VIDEO, PackageManager.PERMISSION_GRANTED);
                perms.put(RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && perms.get(READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                            && perms.get(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && perms.get(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                    ) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_MEDIA_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_IMAGES)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_AUDIO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_VIDEO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, RECORD_AUDIO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)
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
        new AlertDialog.Builder(iuc_PermissionActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}