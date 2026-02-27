package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote.RemoteFragment;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv.FireTVManager;


import java.io.IOException;

public class DialogDisconnect extends Dialog {
    
    private final Context context;

    public DialogDisconnect(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_disconnect);

        TextView textView = (TextView) findViewById(R.id.tvNameDevice);
        Button button = (Button) findViewById(R.id.btnDisconnect);
        TextView textView2 = (TextView) findViewById(R.id.btnCancel);

        if (TVConnectUtils.getInstance().getDeviveName() != null) {
            textView.setText("Disconnect with " + TVConnectUtils.getInstance().getDeviveName());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                    try {
                        FireTVManager fireTVManager = RemoteFragment.fireTVManager;
                        if (fireTVManager != null) {
                            fireTVManager.disconnectTelevision();
                            try {
                                RemoteFragment.fireTVManager.getAdbConnection().close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                                StringBuilder sb = new StringBuilder();
                                sb.append("disconnect: ");
                                sb.append(e2.getMessage());
                            }
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                if (TVConnectUtils.getInstance() != null && TVConnectUtils.getInstance().isConnected()) {
                    TVConnectUtils.getInstance().disconnect();
                }
                DialogDisconnect.this.dismiss();
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View view) {
                DialogDisconnect.this.dismiss();
            }
        });

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
        getWindow().setLayout(-1, -2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);

    }

}
