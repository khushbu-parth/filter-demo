package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public class TranslucentActivity extends Activity {
    private RelativeLayout relLay;

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.translucent_activity);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().setLayout(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight() - 100);
        this.relLay = (RelativeLayout) findViewById(R.id.translayRel);
        TextView textView = (TextView) findViewById(R.id.text_trans);
        this.relLay.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.TranslucentActivity.1
            @Override 
            public void onClick(View view) {
                TranslucentActivity.this.finish();
            }
        });
        String stringExtra = getIntent().getStringExtra("autostart");
        if (stringExtra != null) {
            textView.setText(stringExtra);
        }
    }
}
