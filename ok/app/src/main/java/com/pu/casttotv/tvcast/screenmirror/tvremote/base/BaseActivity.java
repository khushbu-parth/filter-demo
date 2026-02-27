package com.pu.casttotv.tvcast.screenmirror.tvremote.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;


public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onFinish() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void gotoActivity(Class cls) {
        startActivity(new Intent(this, cls));
        Utils.nextScreen(this);
    }

}
