package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ConfereceCallAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;


public class ShowAllConference extends AppCompatActivity {
    AdLoad adLoad;
    RecyclerView rvConference;


    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_show_all_conference);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ShowAllConference.1
            @Override 
            public void onClick(View view) {
                ShowAllConference.this.onBackPressed();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvConference);
        this.rvConference = recyclerView;
        recyclerView.setAdapter(new ConfereceCallAdapter(this, (List) new Gson().fromJson(getIntent().getStringExtra("callModel"), new TypeToken<List<CallModel>>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ShowAllConference.2
        }.getType())));
    }
}
