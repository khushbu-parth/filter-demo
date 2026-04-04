package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.SimChooserHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public class SimChooseActivity extends BaseActivity {
    private CallModel callModel;
    private SimChooserHandler simChooserHandler;

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.sim_choose_activity);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler != null) {
            CallModel callModel = callHandler.tempCallModelForActivity;
            this.callModel = callModel;
            if (callModel != null) {
                callHandler.tempCallModelForActivity = null;
            }
        }
        this.simChooserHandler = new SimChooserHandler(this, this.callModel);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SimChooseActivity.1
            @Override 
            public void onClick(View view) {
                SimChooseActivity.this.finishAndRemoveTask();
            }
        });
        ((TextView) findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SimChooseActivity.2
            @Override 
            public void onClick(View view) {
                SimChooseActivity.this.finishAndRemoveTask();
            }
        });
        if (this.simChooserHandler.simListAdapter != null) {
            recyclerView.setAdapter(this.simChooserHandler.simListAdapter);
        }
    }
}
