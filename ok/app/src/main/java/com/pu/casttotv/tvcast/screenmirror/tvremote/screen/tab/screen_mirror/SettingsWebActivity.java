package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

import java.util.Locale;

@SuppressLint("WrongConstant")
public final class SettingsWebActivity extends PreferenceActivity {
    @SuppressLint("ResourceType")
    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getFragmentManager().beginTransaction().replace(16908290, new ScreenStreamPreferenceFragment()).commit();
    }

    /* loaded from: classes4.dex */
    public static class ScreenStreamPreferenceFragment extends PreferenceFragment {
        int mIndex;
        int mResizeFactor;

        @SuppressLint("WrongConstant")
        public void setResizeFactor(int i, TextView textView) {
            this.mResizeFactor = i;
            textView.setText(String.format(Locale.US, "%.1fx", Float.valueOf(i / 10.0f)));
            if (this.mResizeFactor > 10) {
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                textView.setTypeface(textView.getTypeface(), 1);
                return;
            }
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColorSecondary));
            textView.setTypeface(Typeface.DEFAULT);
        }

        @Override // android.preference.PreferenceFragment, android.app.Fragment
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.preferences);
            final Preference findPreference = findPreference(getString(R.string.pref_key_resize_factor));
            findPreference.setSummary(getString(R.string.pref_resize_summary) + getString(R.string.settings_activity_value) + String.format(Locale.US, "%.1fx", Float.valueOf(MyApplication.getAppPreference().getResizeFactor() / 10.0f)));
            findPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.1
                @SuppressLint("ResourceType")
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    View inflate = LayoutInflater.from(ScreenStreamPreferenceFragment.this.getActivity()).inflate(R.layout.pref_resize, (ViewGroup) null);
                    final TextView textView = (TextView) inflate.findViewById(R.id.pref_resize_dialog_textView);
                    ScreenStreamPreferenceFragment.this.setResizeFactor(MyApplication.getAppPreference().getResizeFactor(), textView);
                    SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.pref_resize_dialog_seekBar);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.1.1
                        @Override // android.widget.SeekBar.OnSeekBarChangeListener
                        public void onStartTrackingTouch(SeekBar seekBar2) {
                        }

                        @Override // android.widget.SeekBar.OnSeekBarChangeListener
                        public void onStopTrackingTouch(SeekBar seekBar2) {
                        }

                        @Override // android.widget.SeekBar.OnSeekBarChangeListener
                        public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                            ScreenStreamPreferenceFragment.this.setResizeFactor(i + 1, textView);
                        }
                    });
                    seekBar.setProgress(ScreenStreamPreferenceFragment.this.mResizeFactor - 1);
                    new AlertDialog.Builder(ScreenStreamPreferenceFragment.this.getActivity()).setView(inflate).setCancelable(true).setIcon(R.drawable.ic_pref_resize_black_24dp).setTitle(R.string.pref_resize).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.1.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MyApplication.getAppPreference().setResizeFactor(ScreenStreamPreferenceFragment.this.mResizeFactor);
                            Preference preference2 = findPreference;
                            preference2.setSummary(ScreenStreamPreferenceFragment.this.getString(R.string.pref_resize_summary) + ScreenStreamPreferenceFragment.this.getString(R.string.settings_activity_value) + String.format(Locale.US, "%.1fx", Float.valueOf(MyApplication.getAppPreference().getResizeFactor() / 10.0f)));
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.1.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).create().show();
                    return true;
                }
            });
            final ListPreference listPreference = (ListPreference) findPreference(getString(R.string.pref_key_jpeg_quality));
            this.mIndex = listPreference.findIndexOfValue(listPreference.getValue());
            listPreference.setSummary(getString(R.string.pref_jpeg_quality_summary) + getString(R.string.settings_activity_value) + ((Object) listPreference.getEntries()[this.mIndex]));
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.2
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    int findIndexOfValue = listPreference.findIndexOfValue(obj.toString());
                    ListPreference listPreference2 = listPreference;
                    listPreference2.setSummary(ScreenStreamPreferenceFragment.this.getString(R.string.pref_jpeg_quality_summary) + ScreenStreamPreferenceFragment.this.getString(R.string.settings_activity_value) + ((Object) listPreference.getEntries()[findIndexOfValue]));
                    return true;
                }
            });
            final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_key_new_pin_on_app_start));
            final CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference(getString(R.string.pref_key_auto_change_pin));
            final EditTextPreference editTextPreference = (EditTextPreference) findPreference(getString(R.string.pref_key_set_pin));
            checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.3
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    editTextPreference.setEnabled(!((Boolean) obj).booleanValue() && !checkBoxPreference2.isChecked());
                    return true;
                }
            });
            checkBoxPreference2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.4
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    editTextPreference.setEnabled(!((Boolean) obj).booleanValue() && !checkBoxPreference.isChecked());
                    return true;
                }
            });
            editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.5
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    if (obj.toString().length() != 4) {
                        Toast.makeText(ScreenStreamPreferenceFragment.this.getActivity().getApplicationContext(), ScreenStreamPreferenceFragment.this.getString(R.string.settings_activity_pin_digits_count), 1).show();
                        return false;
                    }
                    return true;
                }
            });
            editTextPreference.setEnabled(!checkBoxPreference2.isChecked() && !checkBoxPreference.isChecked());
            final String format = String.format(getString(R.string.settings_activity_port_range), 1025, Integer.valueOf((int) 65534));
            ((EditTextPreference) findPreference(getString(R.string.pref_key_server_port))).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.6
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    String obj2 = obj.toString();
                    if (obj2 == null || obj2.length() == 0 || obj2.length() > 5 || obj2.length() < 4) {
                        Toast.makeText(ScreenStreamPreferenceFragment.this.getActivity().getApplicationContext(), format, 1).show();
                        return false;
                    }
                    int parseInt = Integer.parseInt(obj2);
                    if (parseInt >= 1025 && parseInt <= 65534) {
                        return true;
                    }
                    Toast.makeText(ScreenStreamPreferenceFragment.this.getActivity().getApplicationContext(), format, 1).show();
                    return false;
                }
            });
            final ListPreference listPreference2 = (ListPreference) findPreference(getString(R.string.pref_key_client_con_timeout));
            this.mIndex = listPreference2.findIndexOfValue(listPreference2.getValue());
            listPreference2.setSummary(getString(R.string.pref_client_timeout_summary) + getString(R.string.settings_activity_value) + ((Object) listPreference2.getEntries()[this.mIndex]));
            listPreference2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.SettingsWebActivity.ScreenStreamPreferenceFragment.7
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    int findIndexOfValue = listPreference2.findIndexOfValue(obj.toString());
                    ListPreference listPreference3 = listPreference2;
                    listPreference3.setSummary(ScreenStreamPreferenceFragment.this.getString(R.string.pref_client_timeout_summary) + ScreenStreamPreferenceFragment.this.getString(R.string.settings_activity_value) + ((Object) listPreference2.getEntries()[findIndexOfValue]));
                    return true;
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onBackPressed();
    }
}
