package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.AppPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PermissionCenter;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.notification.BoloNotificationListenerService;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.flipboard.bottomsheet.BottomSheetLayout;


public class ReqPermissionActivity extends BaseActivity {
    private LinearLayout accessibility_lay;
    private ImageView answer_phone;
    private LinearLayout answer_phone_lay;
    private BottomSheetLayout bottomSheetLayout;
    private Button button;
    private LinearLayout call_log_lay;
    private ImageView contact;
    private LinearLayout contact_lay;
    private ImageView default_app;
    private LinearLayout default_app_lay;
    private TextView info;
    private BoloPermission permission;
    private ImageView pop_up;
    private LinearLayout pop_up_lay;
    private ImageView read_calllog;
    private ImageView read_state;
    private LinearLayout read_state_layout;
    private ImageView record;
    private LinearLayout record_lay;
    private ImageView storage;
    private LinearLayout storage_lay;
    private View storage_line;
    private boolean isAllPermissionGranted = false;
    private boolean isDefaultAppVisible = false;
    private int ENABLED = R.drawable.enabled_circle;
    private int DISABLED = R.drawable.disabled_circle;
    int check = 0;
    private boolean isWorkDone = false;
    private boolean isStartLogged = false;

    private void changeColor(ImageView imageView, int i) {
        imageView.setImageResource(i);
    }

    private void check(String str) {
        if (!this.permission.isPermissionGranted(str)) {
            if (this.permission.shouldRelational(str)) {
                this.permission.groupPermission(this.isDefaultAppVisible);
                return;
            } else if (!BoloPermission.Utils.checkPermissionPreference(str)) {
                this.permission.groupPermission(this.isDefaultAppVisible);
                BoloPermission.Utils.updatePermissionPreference(str);
                return;
            } else {
                showDeviceSetting();
                return;
            }
        }
        this.check = 0;
    }

    private void checkGrantedPermission() {
        if (!this.isDefaultAppVisible) {
            changeColor(this.default_app, this.ENABLED);
        } else {
            changeColor(this.default_app, this.DISABLED);
        }
        for (String str : BoloPermission.permissionList(this.isDefaultAppVisible)) {
            if (this.permission.isPermissionGranted(str)) {
                if (str.equals(BoloPermission.RECORD_AUDIO)) {
                    changeColor(this.record, this.ENABLED);
                    this.record_lay.setVisibility(8);
                } else if (str.equals(BoloPermission.READ_CONTACTS)) {
                    changeColor(this.contact, this.ENABLED);
                    this.contact_lay.setVisibility(8);
                } else if (str.equals(BoloPermission.WRITE_CONTACTS)) {
                    changeColor(this.contact, this.ENABLED);
                    this.contact_lay.setVisibility(8);
                } else if (str.equals(BoloPermission.WRITE_EXTERNAL_STORAGE)) {
                    changeColor(this.storage, this.ENABLED);
                    hideRowLayout(this.storage_lay, false);
                } else if (str.equals(BoloPermission.READ_PHONE_STATE)) {
                    changeColor(this.read_state, this.ENABLED);
                    this.read_state_layout.setVisibility(8);
                } else if (str.equals(BoloPermission.ANSWER_PHONE_CALLS)) {
                    changeColor(this.answer_phone, this.ENABLED);
                    this.answer_phone_lay.setVisibility(8);
                } else if (str.equals(BoloPermission.READ_CALL_LOG)) {
                    changeColor(this.read_calllog, this.ENABLED);
                    this.call_log_lay.setVisibility(8);
                } else if (str.equals(BoloPermission.WRITE_CALL_LOG)) {
                    changeColor(this.read_calllog, this.ENABLED);
                    this.call_log_lay.setVisibility(8);
                }
            } else if (str.equals(BoloPermission.RECORD_AUDIO)) {
                changeColor(this.record, this.DISABLED);
                this.record_lay.setVisibility(8);
            } else if (str.equals(BoloPermission.READ_CONTACTS)) {
                changeColor(this.contact, this.DISABLED);
                this.contact_lay.setVisibility(0);
            } else if (str.equals(BoloPermission.WRITE_CONTACTS)) {
                changeColor(this.contact, this.DISABLED);
                this.contact_lay.setVisibility(0);
            } else if (str.equals(BoloPermission.WRITE_EXTERNAL_STORAGE)) {
                changeColor(this.storage, this.DISABLED);
            } else if (str.equals(BoloPermission.READ_PHONE_STATE)) {
                changeColor(this.read_state, this.DISABLED);
                this.read_state_layout.setVisibility(0);
            } else if (str.equals(BoloPermission.ANSWER_PHONE_CALLS)) {
                changeColor(this.answer_phone, this.DISABLED);
                this.answer_phone_lay.setVisibility(0);
            } else if (str.equals(BoloPermission.READ_CALL_LOG)) {
                changeColor(this.read_calllog, this.DISABLED);
                this.call_log_lay.setVisibility(8);
            } else if (str.equals(BoloPermission.WRITE_CALL_LOG)) {
                changeColor(this.read_calllog, this.DISABLED);
                this.call_log_lay.setVisibility(8);
            }
        }
    }

    public void checkUpdateAccesbilityPermissionGiven(final Activity activity) {
        activity.runOnUiThread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ReqPermissionActivity.1
            @Override 
            public void run() {
                new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ReqPermissionActivity.1.1
                    @Override 
                    public void run() {
                        if (!PermissionCenter.isAccessibilityEnabled(ReqPermissionActivity.this)) {
                            ReqPermissionActivity.this.checkUpdateAccesbilityPermissionGiven(activity);
                            return;
                        }
                        try {
                            activity.finishActivity(101);
                        } catch (Exception unused) {
                        }
                    }
                }, 100L);
            }
        });
    }

    private void goToHome(boolean z) {
        this.isWorkDone = true;
        startActivity(new Intent(this, MainActivity.class));
        if (z) {
            Utility.logUserProtiesAndSIMData();
        }
    }

    private void goToNextPermission() {
        changeColor(this.pop_up, this.ENABLED);
        hideRowLayout(this.pop_up_lay, true);
        startC(this.button);
    }

    private void handleVivoCase() {
        shouldSomePermissionHide(false);
        hideRowLayout(this.default_app_lay, false);
        this.isDefaultAppVisible = false;
        startC(this.button);
    }

    private void hideRowLayout(final View view, boolean z) {
        if (z) {
            YoYo.with(Techniques.FadeOutRight).duration(800L).playOn(view);
            new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ReqPermissionActivity.2
                @Override 
                public void run() {
                    view.setVisibility(8);
                }
            }, 800L);
            return;
        }
        view.setVisibility(8);
    }

    private void info() {
        this.info.setVisibility(0);
        this.info.setText(R.string.please_give_req_permission);
        this.button.setText(R.string.give_permission);
    }

    private boolean isEnableAccessibility() {
        return PermissionCenter.isAccessibilityEnabled(this);
    }

    private boolean isPopUpEnabled() {
        if (isOverlayNotFound()) {
            return true;
        }
        return PermissionCenter.isOverlayPermissionEnabled(this);
    }

    private void openAccesbility() {
        Helper.openAccessibilitySetting(this);
        checkUpdateAccesbilityPermissionGiven(this);
    }

    private void setBoloToDefaultApp() {
        if (!Utility.isAppDefaultSet(this)) {
            Utility.openDefaultAppDialog(this);
            this.isDefaultAppVisible = true;
            return;
        }
        this.isDefaultAppVisible = false;
    }

    private boolean shouldDefaultPopUpVisbile() {
        if (!Utility.isAppDefaultSet(this)) {
            this.isDefaultAppVisible = true;
        } else {
            this.isDefaultAppVisible = false;
        }
        return this.isDefaultAppVisible;
    }

    private void shouldSomePermissionHide(boolean z) {
        int i = z ? 8 : 0;
        this.storage_line.setVisibility(i);
        this.contact_lay.setVisibility(i);
        this.record_lay.setVisibility(8);
        this.read_state_layout.setVisibility(i);
        if (Build.VERSION.SDK_INT >= 26) {
            this.answer_phone_lay.setVisibility(i);
        }
    }

    private void showDeviceSetting() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.device_permission_view, (ViewGroup) this.bottomSheetLayout, false);
        ((Button) inflate.findViewById(R.id.btnApplied)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ReqPermissionActivity.3
            @Override 
            public void onClick(View view) {
                ReqPermissionActivity.this.permission.openApplicationSetting();
            }
        });
        ((ImageView) inflate.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ReqPermissionActivity.4
            @Override 
            public void onClick(View view) {
                ReqPermissionActivity.this.bottomSheetLayout.dismissSheet();
            }
        });
        this.bottomSheetLayout.showWithSheetView(inflate);
    }

    private void showEnableAccessibility() {
        if (!PermissionCenter.isAccessibilityEnabled(this)) {
            openAccesbility();
        } else {
            goToHome(true);
        }
    }

    public void startC(View view) {
        if (!this.isStartLogged) {
            Utility.logEventNew(Constants.PermissionCategory, "Permission_grant_start");
            this.isStartLogged = true;
        }
        if (this.isDefaultAppVisible && PreferenceUtils.getInstance().getBoolean(Constants.ENABLE_BOLO)) {
            setBoloToDefaultApp();
            if (isDefaultPackageNotFound()) {
                handleVivoCase();
            }
        } else if (!isPopUpEnabled()) {
            AppPermission.overlayPermission(this);
            if (isOverlayNotFound()) {
                goToNextPermission();
            }
        } else {
            try {
                if (!this.isAllPermissionGranted) {
                    if (this.check >= this.permission.getRequiredPermissionList(this.isDefaultAppVisible).size()) {
                        this.check = 0;
                    }
                    check(this.permission.getRequiredPermissionList(this.isDefaultAppVisible).get(this.check));
                    this.check++;
                    return;
                }
                showEnableAccessibility();
            } catch (Exception unused) {
            }
        }
    }

    public boolean isDefaultPackageNotFound() {
        return PreferenceUtils.getInstance().getBoolean(Utility.IS_DEFAULT_PACKAGE_NOT_FOUND);
    }

    public boolean isOverlayNotFound() {
        return PreferenceUtils.getInstance().getBoolean(Helper.OVERLAY_SETTING_NOT_FOUND);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == BoloPermission.DEVICE_SETTING_REQ) {
            this.isAllPermissionGranted = this.permission.isAllPermissionGranted(this.isDefaultAppVisible);
            checkGrantedPermission();
            if (this.isAllPermissionGranted && this.bottomSheetLayout.isSheetShowing()) {
                this.bottomSheetLayout.dismissSheet();
                this.info.setVisibility(8);
                showEnableAccessibility();
            }
        }
        if (i == 102 && isPopUpEnabled()) {
            changeColor(this.pop_up, this.ENABLED);
            hideRowLayout(this.pop_up_lay, true);
            startC(this.button);
        }
        if (i == 101) {
            if (BoloNotificationListenerService.checkAccessibility(this)) {
                goToHome(true);
            } else {
                info();
            }
        }
        if (i == 320 && !PreferenceUtils.getInstance().getBoolean(Utility.IS_DEFAULT_PACKAGE_NOT_FOUND)) {
            if (!Utility.isAppDefaultSet(this)) {
                changeColor(this.default_app, this.DISABLED);
            } else {
                changeColor(this.default_app, this.ENABLED);
                hideRowLayout(this.default_app_lay, true);
                this.isDefaultAppVisible = false;
                startC(this.button);
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_req_permission);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        this.permission = new BoloPermission(this);
        this.call_log_lay = (LinearLayout) findViewById(R.id.read_call_log_lay);
        this.record = (ImageView) findViewById(R.id.record_audio);
        this.contact = (ImageView) findViewById(R.id.read_contact);
        this.storage = (ImageView) findViewById(R.id.storage);
        this.answer_phone = (ImageView) findViewById(R.id.answer_phone);
        this.read_state = (ImageView) findViewById(R.id.read_state);
        this.bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.design_bottom_sheet);
        this.info = (TextView) findViewById(R.id.info);
        this.button = (Button) findViewById(R.id.btnApplied);
        this.default_app = (ImageView) findViewById(R.id.default_app);
        this.pop_up = (ImageView) findViewById(R.id.pop_up);
        this.read_calllog = (ImageView) findViewById(R.id.read_calllog);
        this.storage_line = findViewById(R.id.storage_line);
        this.contact_lay = (LinearLayout) findViewById(R.id.read_contact_lay);
        this.read_state_layout = (LinearLayout) findViewById(R.id.read_state_lay);
        this.record_lay = (LinearLayout) findViewById(R.id.record_audio_lay);
        this.storage_lay = (LinearLayout) findViewById(R.id.storage_lay);
        this.answer_phone_lay = (LinearLayout) findViewById(R.id.answer_phone_lay);
        this.default_app_lay = (LinearLayout) findViewById(R.id.default_app_lay);
        this.pop_up_lay = (LinearLayout) findViewById(R.id.pop_up_lay);
        this.accessibility_lay = (LinearLayout) findViewById(R.id.enable_accessibility_lay);
        boolean shouldDefaultPopUpVisbile = shouldDefaultPopUpVisbile();
        this.isDefaultAppVisible = shouldDefaultPopUpVisbile;
        if (this.permission.isAllPermissionGranted(shouldDefaultPopUpVisbile) && isEnableAccessibility() && isPopUpEnabled()) {
            if (PreferenceUtils.getInstance().getBoolean(Constants.IS_WELCOME_SCREEN_SHOWED)) {
                goToHome(false);
            } else {
                goToHome(false);
            }
        }
        if (isPopUpEnabled()) {
            this.pop_up_lay.setVisibility(8);
        } else {
            this.pop_up_lay.setVisibility(0);
        }
        if (isEnableAccessibility()) {
            this.accessibility_lay.setVisibility(8);
        } else {
            this.accessibility_lay.setVisibility(0);
        }
        if (this.isDefaultAppVisible && !isDefaultPackageNotFound()) {
            shouldSomePermissionHide(this.isDefaultAppVisible);
        } else {
            this.default_app_lay.setVisibility(8);
        }
        if (Build.VERSION.SDK_INT < 26) {
            this.answer_phone_lay.setVisibility(8);
        } else {
            this.read_state_layout.setVisibility(8);
        }
        this.isAllPermissionGranted = this.permission.isAllPermissionGranted(this.isDefaultAppVisible);
        checkGrantedPermission();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        this.check = 0;
        checkGrantedPermission();
        boolean isAllPermissionGranted = this.permission.isAllPermissionGranted(this.isDefaultAppVisible);
        this.isAllPermissionGranted = isAllPermissionGranted;
        if (!isAllPermissionGranted) {
            info();
        } else {
            showEnableAccessibility();
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.isWorkDone) {
            finish();
        }
    }
}
