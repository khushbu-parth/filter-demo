package com.colorcallscreen.colorphone.callscreen.calltheme.component;

import android.os.Build;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import androidx.core.content.ContextCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.SimListAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.SimChooseActivity;

import java.util.List;


public class SimChooserHandler {
    public static String SELECTED_SIM_PREF = "selected_sim";
    private SimChooseActivity context;
    public SimListAdapter simListAdapter;

    public SimChooserHandler(SimChooseActivity simChooseActivity, CallModel callModel) {
        this.context = simChooseActivity;
        List<PhoneAccountHandle> phoneAccounts = getPhoneAccounts();
        if (phoneAccounts == null || phoneAccounts.size() <= 0) {
            return;
        }
        SimListAdapter simListAdapter = new SimListAdapter(simChooseActivity, phoneAccounts);
        this.simListAdapter = simListAdapter;
        simListAdapter.setCallModel(callModel);
    }

    public static String getSelectedSim() {
        return PreferenceUtils.getInstance().getString(SELECTED_SIM_PREF);
    }

    public List<PhoneAccountHandle> getPhoneAccounts() {
        if (Build.VERSION.SDK_INT >= 23) {
            TelecomManager telecomManager = (TelecomManager) this.context.getSystemService("telecom");
            if (ContextCompat.checkSelfPermission(this.context, BoloPermission.READ_PHONE_STATE) != 0) {
                return null;
            }
            return telecomManager.getCallCapablePhoneAccounts();
        }
        return null;
    }

    public void onCancelTapped() {
        if (this.simListAdapter.timer != null) {
            this.simListAdapter.timer.cancel();
        }
    }

    public void onSimSelected(PhoneAccount phoneAccount) {
        if (Build.VERSION.SDK_INT >= 23) {
            saveSelectedSim(phoneAccount.getLabel().toString());
        }
    }

    public void saveSelectedSim(String str) {
        PreferenceUtils.getInstance().putPreference(SELECTED_SIM_PREF, str);
    }
}
