package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallLogModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogsView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CallLogsFragment extends Fragment implements CallLogsAdapter.OnRefreshListener, HistoryFragment.HistoryUpdateListener, CallLogsView {
    private CallLogsAdapter callLogsAdapter;
    private Thread filterListThread;
    private long fromDate;
    AppCompatImageView ivClose;
    AppCompatImageView ivSearch;
    CallLogComponent logComponent;
    TextView permission_txt;
    private RecyclerView rVCallLog;
    private long toDate;
    public AppCompatTextView tvNoFound;
    AppCompatEditText tvSearch;
    List<CallLogModel> allCallHistory = new ArrayList();
    private String queryStr = "";
    List<CallLogModel> contactList = new ArrayList();

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.call_log_frag, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.logComponent = new CallLogComponent(BoloApplication.getApplication(), this);
        this.ivSearch = (AppCompatImageView) view.findViewById(R.id.ivSearch);
        this.ivClose = (AppCompatImageView) view.findViewById(R.id.ivClose);
        AppCompatEditText appCompatEditText = (AppCompatEditText) view.findViewById(R.id.tvSearch);
        this.tvSearch = appCompatEditText;
        appCompatEditText.addTextChangedListener(new TextWatcher() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.CallLogsFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                    CallLogsFragment.this.queryStr = "";
                    CallLogsFragment.this.callLogsAdapter.hasSearch = false;
                    CallLogsFragment.this.callLogsAdapter.query = null;
                    CallLogsFragment callLogsFragment = CallLogsFragment.this;
                    callLogsFragment.searchContactsForQuery(callLogsFragment.queryStr, CallLogsFragment.this.allCallHistory);
                    CallLogsFragment.this.ivClose.setVisibility(8);
                    Utility.hideSoftKeyboard(CallLogsFragment.this.requireActivity());
                    CallLogsFragment.this.tvSearch.clearFocus();
                    return;
                }
                CallLogsFragment.this.queryStr = editable.toString();
                Log.println(7, "log===", CallLogsFragment.this.queryStr);
                if (CallLogsFragment.this.callLogsAdapter != null) {
                    CallLogsFragment.this.callLogsAdapter.query = CallLogsFragment.this.queryStr;
                    CallLogsFragment.this.callLogsAdapter.hasSearch = true;
                    CallLogsFragment callLogsFragment2 = CallLogsFragment.this;
                    callLogsFragment2.searchContactsForQuery(callLogsFragment2.queryStr, CallLogsFragment.this.allCallHistory);
                }
                CallLogsFragment.this.ivClose.setVisibility(0);
            }
        });
        this.ivClose.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.CallLogsFragment.2
            @Override 
            public void onClick(View view2) {
                CallLogsFragment.this.onClearSearchView();
            }
        });
        this.tvNoFound = (AppCompatTextView) view.findViewById(R.id.tvNoFound);
        TextView textView = (TextView) view.findViewById(R.id.permission_txt);
        this.permission_txt = textView;
        textView.setVisibility(8);
        this.fromDate = CallLogUtils.getLastDateFromToday(10);
        this.toDate = CallLogUtils.getCurrentDate();
        this.rVCallLog = (RecyclerView) view.findViewById(R.id.rVCallLog);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 1, false);
        this.rVCallLog.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.CallLogsFragment.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (CallLogsFragment.this.getActivity() != null && ((InputMethodManager) CallLogsFragment.this.getActivity().getSystemService("input_method")).isActive()) {
                    Utility.hideSoftKeyboard(CallLogsFragment.this.getActivity());
                }
            }
        });
        this.rVCallLog.setLayoutManager(linearLayoutManager);
        if (ContextCompat.checkSelfPermission(getContext(), BoloPermission.READ_CALL_LOG) != 0 || ContextCompat.checkSelfPermission(getContext(), BoloPermission.WRITE_CALL_LOG) != 0 || ContextCompat.checkSelfPermission(getActivity(), BoloPermission.PHONE_CALLS) != 0) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{BoloPermission.READ_CALL_LOG, BoloPermission.WRITE_CALL_LOG, BoloPermission.PHONE_CALLS}, 102);
        } else {
            this.logComponent.loadCallLogs(null, -1L, -1L, false, 0, 0);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.HistoryUpdateListener
    public void onClearSearchView() {
        try {
            Utility.hideSoftKeyboard(requireActivity());
            AppCompatImageView appCompatImageView = this.ivClose;
            if (appCompatImageView != null) {
                appCompatImageView.setVisibility(8);
            }
            AppCompatEditText appCompatEditText = this.tvSearch;
            if (appCompatEditText != null) {
                appCompatEditText.setText("");
                this.tvSearch.clearFocus();
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.HistoryUpdateListener
    public void notifyForDataSetChange() {
        new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.CallLogsFragment.4
            @Override 
            public void run() {
                if (CallLogsFragment.this.callLogsAdapter == null) {
                    return;
                }
                if (CallLogsFragment.this.allCallHistory.size() > 0) {
                    CallLogsFragment callLogsFragment = CallLogsFragment.this;
                    boolean z = false;
                    callLogsFragment.fromDate = Long.parseLong(callLogsFragment.allCallHistory.get(0).getDate()) + 1;
                    CallLogsFragment.this.toDate = CallLogUtils.getCurrentDate();
                    List<CallLogModel> queryForCallLogs = CallLogsFragment.this.logComponent.queryForCallLogs(null, CallLogsFragment.this.fromDate, CallLogsFragment.this.toDate, true, -1, 0);
                    if (queryForCallLogs != null && !queryForCallLogs.isEmpty()) {
                        Iterator<CallLogModel> it = queryForCallLogs.iterator();
                        if (it.hasNext()) {
                            it.next();
                            CallLogModel callLogModel = queryForCallLogs.get(queryForCallLogs.size() - 1);
                            Log.println(7, "callLogModel---", callLogModel.getDisplayName() + "--" + callLogModel.getCallType());
                            if (CallLogsFragment.this.allCallHistory.size() > 0 && callLogModel.getNumber() != null) {
                                CallLogModel callLogModel2 = CallLogsFragment.this.allCallHistory.get(0);
                                if (callLogModel2.getNumber().equals(callLogModel.getNumber())) {
                                    callLogModel2.setSameNumberCount(callLogModel2.getSameNumberCount() + 1);
                                    callLogModel2.setDisplayName(callLogModel2.getName() + " (" + callLogModel2.getSameNumberCount() + ")");
                                    callLogModel2.setDate(callLogModel.getDate());
                                    callLogModel2.setCallType(callLogModel.getCallType() + "");
                                    callLogModel2.setIsRead(callLogModel.getIsRead());
                                    callLogModel2.setDuration(callLogModel.getDuration());
                                    callLogModel2.setSimName(callLogModel.getSimName());
                                    callLogModel2.setImgUri(callLogModel.getImgUri());
                                    CallLogsFragment.this.allCallHistory.set(0, callLogModel2);
                                } else {
                                    CallLogsFragment.this.allCallHistory.add(0, callLogModel);
                                }
                            } else {
                                CallLogsFragment.this.allCallHistory.add(0, callLogModel);
                            }
                            z = true;
                        }
                    }
                    if (CallLogsFragment.this.callLogsAdapter == null || !z) {
                        return;
                    }
                    CallLogsFragment.this.callLogsAdapter.setCallLogModel(CallLogsFragment.this.allCallHistory);
                    CallLogsFragment.this.rVCallLog.stopScroll();
                    return;
                }
                CallLogsFragment.this.logComponent.loadCallLogs(null, -1L, -1L, false, 0, 0);
            }
        }, 500L);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (BoloPermission.DEVICE_SETTING_REQ == i) {
            if (ContextCompat.checkSelfPermission(getContext(), BoloPermission.READ_CALL_LOG) != 0 && ContextCompat.checkSelfPermission(getContext(), BoloPermission.WRITE_CALL_LOG) != 0) {
                Toast.makeText(getContext(), "Call Permission not granted", 0).show();
                return;
            }
            this.logComponent.loadCallLogs(null, this.fromDate, this.toDate, false, -1, 0);
            this.permission_txt.setVisibility(8);
        } else if (i == 1003 && i2 == -1) {
            String stringExtra = intent.getStringExtra("phoneNumber");
            for (CallLogModel callLogModel : this.allCallHistory) {
                if (callLogModel.getNumber().equals(stringExtra)) {
                    this.allCallHistory.remove(callLogModel);
                    this.callLogsAdapter.setCallLogModel(this.allCallHistory);
                    return;
                }
            }
        }
    }

    public void searchContactsForQuery(String str, final List<CallLogModel> list) {
        final String lowerCase = str.toLowerCase();
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        if (!lowerCase.isEmpty() && !list.isEmpty()) {
            Thread thread = this.filterListThread;
            if (thread != null) {
                thread.interrupt();
            }
            Thread thread2 = new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.CallLogsFragment.5
                @Override 
                public void run() {
                    for (CallLogModel callLogModel : list) {
                        if (CallLogsFragment.this.filterListThread == null || CallLogsFragment.this.filterListThread.isInterrupted()) {
                            return;
                        }
                        if (callLogModel.getName().toLowerCase().contains(lowerCase)) {
                            if (callLogModel.getName().toLowerCase().startsWith(lowerCase)) {
                                arrayList.add(callLogModel);
                            } else {
                                arrayList2.add(callLogModel);
                            }
                        } else if (callLogModel.getNumber().toLowerCase().contains(lowerCase)) {
                            if (callLogModel.getNumber().toLowerCase().startsWith(lowerCase)) {
                                arrayList.add(callLogModel);
                            } else {
                                arrayList2.add(callLogModel);
                            }
                        }
                    }
                    arrayList3.addAll(arrayList);
                    arrayList3.addAll(arrayList2);
                    if (CallLogsFragment.this.filterListThread == null || CallLogsFragment.this.filterListThread.isInterrupted()) {
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.CallLogsFragment.5.1
                        @Override 
                        public void run() {
                            CallLogsFragment.this.filterListThread = null;
                            if (arrayList3.isEmpty() && lowerCase.isEmpty()) {
                                arrayList3.addAll(list);
                            }
                            CallLogsFragment.this.onFilterCompleted(arrayList3);
                        }
                    });
                }
            });
            this.filterListThread = thread2;
            thread2.start();
            return;
        }
        onFilterCompleted(list);
    }

    public void onFilterCompleted(List<CallLogModel> list) {
        this.contactList.clear();
        this.contactList.addAll(list);
        CallLogsAdapter callLogsAdapter = this.callLogsAdapter;
        if (callLogsAdapter == null) {
            CallLogsAdapter callLogsAdapter2 = new CallLogsAdapter(requireActivity(), this.contactList, R.layout.item_call_logs, this);
            this.callLogsAdapter = callLogsAdapter2;
            this.rVCallLog.setAdapter(callLogsAdapter2);
            return;
        }
        callLogsAdapter.setCallLogModel(this.contactList);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogsView
    public void onCallLogLoaded(List<CallLogModel> list) {
        if (list == null) {
            this.allCallHistory = new ArrayList();
        } else {
            this.allCallHistory.addAll(list);
        }
        try {
            CallLogsAdapter callLogsAdapter = new CallLogsAdapter(requireActivity(), this.allCallHistory, R.layout.item_call_logs, this);
            this.callLogsAdapter = callLogsAdapter;
            this.rVCallLog.setAdapter(callLogsAdapter);
        } catch (Exception unused) {
        }
        onRefresh();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment.HistoryUpdateListener
    public void onContactAdded() {
        Log.e("onContactAdded", "onContactAdded");
        this.allCallHistory.clear();
        onRefresh();
        this.callLogsAdapter.setCallLogModel(this.allCallHistory);
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        Context context = getContext();
        if (context == null) {
            context = BoloApplication.getApplication();
        }
        if (ContextCompat.checkSelfPermission(context, BoloPermission.READ_CALL_LOG) != 0 && ContextCompat.checkSelfPermission(context, BoloPermission.WRITE_CALL_LOG) != 0 && ContextCompat.checkSelfPermission(context, BoloPermission.PHONE_CALLS) != 0) {
            if (Build.VERSION.SDK_INT >= 28) {
                this.permission_txt.setVisibility(0);
                this.permission_txt.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.CallLogsFragment.6
                    @Override 
                    public void onClick(View view) {
                        BoloPermission.openApplicationSetting(CallLogsFragment.this);
                    }
                });
                return;
            }
            return;
        }
        this.permission_txt.setVisibility(8);
        this.logComponent.loadCallLogs(null, this.fromDate, this.toDate, false, -1, 0);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.OnRefreshListener
    public void onRefresh() {
        if (this.allCallHistory.size() > 0) {
            this.tvNoFound.setVisibility(8);
        } else {
            this.tvNoFound.setVisibility(0);
        }
    }
}
