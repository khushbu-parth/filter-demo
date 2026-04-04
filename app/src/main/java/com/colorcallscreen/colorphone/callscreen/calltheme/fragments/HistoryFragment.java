package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ViewPgHistoryAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;

import java.util.ArrayList;


public class HistoryFragment extends Fragment implements MainActivity.OnClearclickListener {
    CallLogsFragment callLogsFragment;
    private BroadcastReceiver callReceiver;
    HorizontalScrollView horScroll;
    IncomingCallFragment incomingCallFragment;
    MissedCallFragment missedCallFragment;
    OutgoingCallFragment outgoingCallFragment;
    AppCompatTextView txtAll;
    AppCompatTextView txtIncoming;
    AppCompatTextView txtMissed;
    AppCompatTextView txtOutgoing;
    ViewPager2 viewPager;
    private ViewPgHistoryAdapter viewPgHistoryAdapter;

    
    public interface HistoryUpdateListener {
        void notifyForDataSetChange();

        void onClearSearchView();

        void onContactAdded();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_history, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.callReceiver != null) {
                LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.callReceiver);
            }
        } catch (Exception unused) {
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.callReceiver == null) {
            this.callReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equalsIgnoreCase("NewContactAdded")) {
                        if (HistoryFragment.this.missedCallFragment != null) {
                            HistoryFragment.this.missedCallFragment.onContactAdded();
                        }
                        if (HistoryFragment.this.incomingCallFragment != null) {
                            HistoryFragment.this.incomingCallFragment.onContactAdded();
                        }
                        if (HistoryFragment.this.outgoingCallFragment != null) {
                            HistoryFragment.this.outgoingCallFragment.onContactAdded();
                        }
                        if (HistoryFragment.this.callLogsFragment != null) {
                            HistoryFragment.this.callLogsFragment.onContactAdded();
                            return;
                        }
                        return;
                    }
                    if (HistoryFragment.this.missedCallFragment != null) {
                        HistoryFragment.this.missedCallFragment.notifyForDataSetChange();
                    }
                    if (HistoryFragment.this.incomingCallFragment != null) {
                        HistoryFragment.this.incomingCallFragment.notifyForDataSetChange();
                    }
                    if (HistoryFragment.this.outgoingCallFragment != null) {
                        HistoryFragment.this.outgoingCallFragment.notifyForDataSetChange();
                    }
                    if (HistoryFragment.this.callLogsFragment != null) {
                        HistoryFragment.this.callLogsFragment.notifyForDataSetChange();
                    }
                }
            };
        }
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.callReceiver, new IntentFilter(Constants.CallDisconnected));
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.callReceiver, new IntentFilter("NewContactAdded"));
        this.horScroll = (HorizontalScrollView) view.findViewById(R.id.horScroll);
        AppCompatTextView appCompatTextView = (AppCompatTextView) view.findViewById(R.id.txtAll);
        this.txtAll = appCompatTextView;
        appCompatTextView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.2
            @Override 
            public void onClick(View view2) {
                HistoryFragment.this.changeBackground();
                HistoryFragment.this.txtAll.setTextColor(HistoryFragment.this.getResources().getColor(R.color.black));
                HistoryFragment.this.txtAll.setBackgroundResource(R.drawable.bg_select);
                HistoryFragment.this.viewPager.setCurrentItem(0);
            }
        });
        AppCompatTextView appCompatTextView2 = (AppCompatTextView) view.findViewById(R.id.txtMissed);
        this.txtMissed = appCompatTextView2;
        appCompatTextView2.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.3
            @Override 
            public void onClick(View view2) {
                HistoryFragment.this.changeBackground();
                HistoryFragment.this.txtMissed.setTextColor(HistoryFragment.this.getResources().getColor(R.color.black));
                HistoryFragment.this.txtMissed.setBackgroundResource(R.drawable.bg_select);
                HistoryFragment.this.viewPager.setCurrentItem(1);
            }
        });
        AppCompatTextView appCompatTextView3 = (AppCompatTextView) view.findViewById(R.id.txtIncoming);
        this.txtIncoming = appCompatTextView3;
        appCompatTextView3.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.4
            @Override 
            public void onClick(View view2) {
                HistoryFragment.this.changeBackground();
                HistoryFragment.this.txtIncoming.setTextColor(HistoryFragment.this.getResources().getColor(R.color.black));
                HistoryFragment.this.txtIncoming.setBackgroundResource(R.drawable.bg_select);
                HistoryFragment.this.viewPager.setCurrentItem(2);
            }
        });
        AppCompatTextView appCompatTextView4 = (AppCompatTextView) view.findViewById(R.id.txtOutgoing);
        this.txtOutgoing = appCompatTextView4;
        appCompatTextView4.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.5
            @Override 
            public void onClick(View view2) {
                HistoryFragment.this.changeBackground();
                HistoryFragment.this.txtOutgoing.setTextColor(HistoryFragment.this.getResources().getColor(R.color.black));
                HistoryFragment.this.txtOutgoing.setBackgroundResource(R.drawable.bg_select);
                HistoryFragment.this.viewPager.setCurrentItem(3);
            }
        });
        this.viewPager = (ViewPager2) view.findViewById(R.id.viewPager);
        this.viewPgHistoryAdapter = new ViewPgHistoryAdapter(this);
        this.callLogsFragment = new CallLogsFragment();
        this.missedCallFragment = new MissedCallFragment();
        this.incomingCallFragment = new IncomingCallFragment();
        this.outgoingCallFragment = new OutgoingCallFragment();
        ArrayList<Fragment> arrayList = new ArrayList<>();
        arrayList.add(this.callLogsFragment);
        arrayList.add(this.missedCallFragment);
        arrayList.add(this.incomingCallFragment);
        arrayList.add(this.outgoingCallFragment);
        this.viewPgHistoryAdapter.setPagers(arrayList);
        this.viewPager.setAdapter(this.viewPgHistoryAdapter);
        this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.6
            @Override 
            public void onPageScrolled(int i, float f, int i2) {
                super.onPageScrolled(i, f, i2);
            }

            @Override 
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                HistoryFragment.this.changeTabBg(i);
            }

            @Override 
            public void onPageScrollStateChanged(int i) {
                super.onPageScrollStateChanged(i);
            }
        });
        changeTabBg(this.viewPager.getCurrentItem());
    }

    
    public void changeTabBg(int i) {
        if (i == 0) {
            CallLogsFragment callLogsFragment = this.callLogsFragment;
            if (callLogsFragment != null) {
                callLogsFragment.onClearSearchView();
            }
            changeBackground();
            this.txtAll.setTextColor(getResources().getColor(R.color.black));
            this.txtAll.setBackgroundResource(R.drawable.bg_select);
            this.horScroll.fullScroll(17);
        } else if (i == 1) {
            MissedCallFragment missedCallFragment = this.missedCallFragment;
            if (missedCallFragment != null) {
                missedCallFragment.onClearSearchView();
            }
            changeBackground();
            this.txtMissed.setTextColor(getResources().getColor(R.color.black));
            this.txtMissed.setBackgroundResource(R.drawable.bg_select);
        } else if (i == 2) {
            IncomingCallFragment incomingCallFragment = this.incomingCallFragment;
            if (incomingCallFragment != null) {
                incomingCallFragment.onClearSearchView();
            }
            changeBackground();
            this.txtIncoming.setTextColor(getResources().getColor(R.color.black));
            this.txtIncoming.setBackgroundResource(R.drawable.bg_select);
        } else if (i != 3) {
        } else {
            OutgoingCallFragment outgoingCallFragment = this.outgoingCallFragment;
            if (outgoingCallFragment != null) {
                outgoingCallFragment.onClearSearchView();
            }
            changeBackground();
            this.txtOutgoing.setTextColor(getResources().getColor(R.color.black));
            this.txtOutgoing.setBackgroundResource(R.drawable.bg_select);
            this.horScroll.fullScroll(66);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setCurrentItem(0);
    }

    
    public void changeBackground() {
        this.txtAll.setBackgroundResource(R.drawable.bg_unselect);
        this.txtAll.setTextColor(getResources().getColor(R.color.white));
        this.txtMissed.setBackgroundResource(R.drawable.bg_unselect);
        this.txtMissed.setTextColor(getResources().getColor(R.color.white));
        this.txtIncoming.setBackgroundResource(R.drawable.bg_unselect);
        this.txtIncoming.setTextColor(getResources().getColor(R.color.white));
        this.txtOutgoing.setBackgroundResource(R.drawable.bg_unselect);
        this.txtOutgoing.setTextColor(getResources().getColor(R.color.white));
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity.OnClearclickListener
    public void onClear() {
        this.callLogsFragment.onContactAdded();
        this.missedCallFragment.onContactAdded();
        this.incomingCallFragment.onContactAdded();
        this.outgoingCallFragment.onContactAdded();
    }
}
