package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.WebApplication;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBDownloadManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.SettingsItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.NetworkUtil;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.StoreUserData;


public class FragmentMenu extends Fragment {
    public final List<DownloadingItem> CancelNoti = new ArrayList();
    private Button ManageEngines;
    public final List<DownloadingItem> downloadingList = new ArrayList();
    private TextView engineName;
    View inflate;
    public CardView linear;
    public Context mContext;
    public SettingsItem settingsPOJO;
    private LinearLayout settings_help;
    public StoreUserData storeUserData;
    private SwitchCompat switch_download_limit;
    private SwitchCompat switch_downloading;
    public SwitchCompat switch_mobile;
    private TextView txt;

    public void DWPause() {
        for (int i = 0; i < this.downloadingList.size(); i++) {
            this.downloadingList.get(i).setIsInPause(1);
            WebApplication.getInstance().downloadService.pauseDownload(this.downloadingList.get(i));
        }
    }

    public void MobileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle(R.string.alert);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(this.mContext.getString(R.string.actived));
        builder.setPositiveButton(this.mContext.getString(R.string.pauseoff), (DialogInterface.OnClickListener) null);
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.setCancelable(true);
        final AlertDialog create = builder.create();
        create.setCancelable(false);
        create.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                Button button = create.getButton(-1);
                Button button2 = create.getButton(-2);
                button.setTextColor(FragmentMenu.this.getResources().getColor(R.color.black));
                button2.setTextColor(FragmentMenu.this.getResources().getColor(R.color.material_cancel));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FragmentMenu.this.DWPause();
                        FragmentMenu.this.settingsPOJO.setMobile(false);
                        FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                        FragmentMenu.this.switch_mobile.setChecked(false);
                        create.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FragmentMenu.this.settingsPOJO.setMobile(true);
                        FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                        FragmentMenu.this.switch_mobile.setChecked(true);
                        create.dismiss();
                    }
                });
            }
        });
        create.show();
    }

    public void check_limit() {
        int maximumDownload = this.settingsPOJO.getMaximumDownload();
        this.engineName.setText(this.mContext.getResources().getStringArray(R.array.engine)[this.settingsPOJO.getEngine()]);
        if (this.settingsPOJO.isDownloadLimit()) {
            this.switch_download_limit.setChecked(true);
            this.linear.setVisibility(View.VISIBLE);
            this.txt.setText(this.mContext.getString(R.string.blank, new Object[]{Integer.valueOf(maximumDownload)}));
        } else if (!this.settingsPOJO.isDownloadLimit()) {
            this.switch_download_limit.setChecked(false);
            this.linear.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        this.settings_help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentMenu.this.show_info();
            }
        });
        check_limit();
        boolean isDownloadingNotification = this.storeUserData.getSettings().isDownloadingNotification();
        boolean isMobile = this.storeUserData.getSettings().isMobile();
        this.switch_downloading.setChecked(isDownloadingNotification);
        this.switch_mobile.setChecked(isMobile);
        this.switch_mobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    FragmentMenu.this.settingsPOJO.setMobile(true);
                    FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                    FragmentMenu.this.switch_mobile.setChecked(true);
                }
                else if (DBDownloadManager.getInstance(FragmentMenu.this.mContext).getCurrentDownloadingCount() != 0) {
                    FragmentMenu.this.downloadingList.addAll(DBDownloadManager.getInstance(FragmentMenu.this.getActivity()).getActiveDownloadingData());
                    if (FragmentMenu.this.downloadingList.size() <= 0) {
                        return;
                    }
                    if (NetworkUtil.getMobileConnectivityStatus(FragmentMenu.this.mContext)) {
                        FragmentMenu.this.MobileDialog();
                        return;
                    }
                    FragmentMenu.this.settingsPOJO.setMobile(false);
                    FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                    FragmentMenu.this.switch_mobile.setChecked(false);
                }
                else {
                    FragmentMenu.this.settingsPOJO.setMobile(false);
                    FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                    FragmentMenu.this.switch_mobile.setChecked(false);
                }
            }
        });
        this.switch_download_limit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    FragmentMenu.this.settingsPOJO.setDownloadLimit(false);
                    FragmentMenu.this.settingsPOJO.setMaximumDownload(6);
                    FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                    FragmentMenu.this.linear.setVisibility(View.GONE);
                } else {
                    FragmentMenu.this.settingsPOJO.setDownloadLimit(true);
                    FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                    FragmentMenu.this.linear.setVisibility(View.VISIBLE);
                }
                FragmentMenu.this.check_limit();
            }
        });
        this.txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentMenu.this.showMaximumDownloadPopup();
            }
        });
        this.ManageEngines.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentMenu.this.showEngine();
            }
        });
        this.switch_downloading.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                FragmentMenu.this.settingsPOJO.setDownloadingNotification(z);
                FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                if (!z) {
                    FragmentMenu.this.CancelNoti.addAll(DBDownloadManager.getInstance(FragmentMenu.this.getActivity()).getActiveDownloadingData());
                    int i = 0;
                    while (i < FragmentMenu.this.CancelNoti.size()) {
                        try {
                            WebApplication.getInstance().downloadService.CancelNoti(FragmentMenu.this.CancelNoti.get(i).getDownloadId());
                            i++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void showEngine() {
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.engine);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle(R.string.chooseengine);
        builder.setSingleChoiceItems(stringArray, this.settingsPOJO.getEngine(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentMenu.this.settingsPOJO.setEngine(i);
                FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                FragmentMenu.this.check_limit();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void showMaximumDownloadPopup() {
        final String[] strArr = new String[6];
        int i = 0;
        while (i < 6) {
            StringBuilder sb = new StringBuilder();
            int i2 = i + 1;
            sb.append(i2);
            strArr[i] = sb.toString();
            i = i2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle(R.string.maxlimit);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        @SuppressLint("ResourceType") ArrayAdapter arrayAdapter = new ArrayAdapter(this.mContext, 17367055);
        arrayAdapter.addAll(strArr);
        builder.setSingleChoiceItems(arrayAdapter, this.settingsPOJO.getMaximumDownload() - 1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentMenu.this.settingsPOJO.setMaximumDownload(Integer.parseInt(strArr[i]));
                FragmentMenu.this.storeUserData.setSettings(FragmentMenu.this.settingsPOJO);
                dialogInterface.dismiss();
                FragmentMenu.this.check_limit();
            }
        });
        builder.create().show();
    }

    public void show_info() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        View inflate2 = getLayoutInflater().inflate(R.layout.tos_popup, (ViewGroup) null);
        Button button = (Button) inflate2.findViewById(R.id.tos_video);
        builder.setView(inflate2);
        builder.setTitle(R.string.how_to_use_this_app);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(R.string.iunder, (DialogInterface.OnClickListener) null);
        builder.setCancelable(false);
        final AlertDialog create = builder.create();
        create.setCancelable(false);
        create.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(final DialogInterface dialogInterface) {
                create.getButton(-1).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        create.show();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initViews();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StoreUserData storeUserData2 = new StoreUserData(getActivity());
        this.storeUserData = storeUserData2;
        this.settingsPOJO = storeUserData2.getSettings();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.inflate = layoutInflater.inflate(R.layout.fragmentmenu, viewGroup, false);
        this.mContext = viewGroup.getContext();
        this.mContext = layoutInflater.getContext();
        this.switch_download_limit = (SwitchCompat) this.inflate.findViewById(R.id.switch_download_limit);
        this.switch_downloading = (SwitchCompat) this.inflate.findViewById(R.id.switch_downloading);
        this.switch_mobile = (SwitchCompat) this.inflate.findViewById(R.id.switch_mobile);
        this.linear = (CardView) this.inflate.findViewById(R.id.choose_max_download);
        this.settings_help = (LinearLayout) this.inflate.findViewById(R.id.settings_help);
        this.txt = (TextView) this.inflate.findViewById(R.id.set_maximum_download);
        this.engineName = (TextView) this.inflate.findViewById(R.id.engine_name);
        this.ManageEngines = (Button) this.inflate.findViewById(R.id.settings_engines);
        return this.inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }
}
