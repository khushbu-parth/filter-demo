package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;


public class AskPopupActivity extends BaseActivity implements View.OnClickListener {
    AdLoad adLoad;
    private LinearLayout addContactBtn;
    private LinearLayout blockBtn;
    private LinearLayout callBtn;
    private TextView name;
    private String number;
    private TextView number_tv;
    private LinearLayout smsBtn;
    private LinearLayout whatsappBtn;

    private void hideWhatsappIconIfNotFound() {
        if (Utility.checkAppIsInstalledOrNot(this, VideoCallController.AppPackage.WHATSAPP) || Utility.checkAppIsInstalledOrNot(this, VideoCallController.AppPackage.WHATSAPP_BUSSINESS)) {
            return;
        }
        this.whatsappBtn.setVisibility(8);
    }

    @Override 
    public void onClick(View view) {

        int id = view.getId();
        if(id == R.id.addcontact_btn){
            BoloApplication application = BoloApplication.getApplication();
            String str = this.number;
            Helper.addToContact(application, str, str);
            finish();
        }else if (id == R.id.blockBtn){
            finish();
            BlockHelper.addToBlockList(this.number, this);
            Toast.makeText(BoloApplication.getApplication(), this.number + " " + getString(R.string.added_to_block_list), 0).show();
        }else if (id == R.id.callBtn){
            finish();
            CallLogUtils.makeCall(this, this.number);
        }else if (id == R.id.smsBtn){
            Helper.sendSms(BoloApplication.getApplication(), this.number);
            finish();
        }else if (id == R.id.whatsappBtn){
            Helper.sendWhatsAppMsg(BoloApplication.getApplication(), this.number, null);
            finish();
        }

    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String str;
        String contactNameFromNumber;
        super.onCreate(bundle);
        setContentView(R.layout.activity_ask_popup_new);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        this.name = (TextView) findViewById(R.id.unknown);
        if (getIntent() != null && getIntent().getData() != null) {
            try {
                Uri data = getIntent().getData();
                if (data.getScheme().equalsIgnoreCase("tel")) {
                    String schemeSpecificPart = data.getSchemeSpecificPart();
                    this.number = schemeSpecificPart;
                    if (schemeSpecificPart != null && !schemeSpecificPart.isEmpty() && (contactNameFromNumber = ContactsHandler.contactNameFromNumber(this.number, this)) != null && !contactNameFromNumber.isEmpty()) {
                        this.name.setText(contactNameFromNumber);
                    }
                }
            } catch (Exception unused) {
            }
        }
        try {
            if (getIntent() != null && ((str = this.number) == null || str.isEmpty())) {
                this.number = getIntent().getStringExtra("number");
            }
        } catch (Exception unused2) {
        }
        try {
            if (Build.VERSION.SDK_INT >= 27) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
            }
            getWindow().addFlags(524288);
        } catch (Exception unused3) {
        }
        findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.AskPopupActivity.1
            @Override 
            public void onClick(View view) {
                AskPopupActivity.this.finish();
            }
        });
        findViewById(R.id.rootLayout).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.AskPopupActivity.2
            @Override 
            public void onClick(View view) {
            }
        });
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.AskPopupActivity.3
            @Override 
            public void onClick(View view) {
                AskPopupActivity.this.finish();
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.blockBtn);
        this.blockBtn = linearLayout;
        linearLayout.setOnClickListener(this);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.whatsappBtn);
        this.whatsappBtn = linearLayout2;
        linearLayout2.setOnClickListener(this);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.smsBtn);
        this.smsBtn = linearLayout3;
        linearLayout3.setOnClickListener(this);
        LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.callBtn);
        this.callBtn = linearLayout4;
        linearLayout4.setOnClickListener(this);
        LinearLayout linearLayout5 = (LinearLayout) findViewById(R.id.addcontact_btn);
        this.addContactBtn = linearLayout5;
        linearLayout5.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.number_tv);
        this.number_tv = textView;
        String str2 = this.number;
        if (str2 != null) {
            textView.setText(str2);
        }
        hideWhatsappIconIfNotFound();
        CallHandler.askPopupActivity = this;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        CallHandler.askPopupActivity = null;
        finish();
    }
}
