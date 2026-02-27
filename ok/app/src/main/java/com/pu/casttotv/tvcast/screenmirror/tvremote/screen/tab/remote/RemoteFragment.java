package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseFragment;
import com.pu.casttotv.tvcast.screenmirror.tvremote.customview.CustomScrollView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.customview.TouchCustom;
import com.pu.casttotv.tvcast.screenmirror.tvremote.customview.ViewRemoteTv;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.DialogConnectFireTV;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.DialogPinCode;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.KeyboardDialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.KeyboardFireTVDialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.NumberKeyboardDialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.TextInputDialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium.IapUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Const;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv.FireTVButtonKeyEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv.FireTVManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.ButtonKeyCode;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.SamSungRemoteController;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.SamsungRemoteManeger;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.ViewUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestTask;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RxRequestTask;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.util.AsyncTask;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.CommandHelper;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.RokuRequestTypes;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.RemoteSonyManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.SonyButtonKeyCode;
import com.connectsdk.core.ChannelInfo;
import com.connectsdk.core.ProgramList;
import com.connectsdk.service.airplay.PListParser;
import com.connectsdk.service.capability.KeyControl;
import com.connectsdk.service.capability.PowerControl;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.command.ServiceCommandError;
import com.jaku.core.JakuRequest;
import com.jaku.core.KeypressKeyValues;
import com.jaku.request.KeypressRequest;
import com.tananaev.adblib.AdbConnection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

@SuppressLint("WrongConstant")
public class RemoteFragment extends BaseFragment implements ViewRemoteTv.IClickTab1, ViewRemoteTv.IClickTab3 {
    public static FireTVManager fireTVManager;
    @BindView(R.id.ctFireTV)
    ConstraintLayout ctFireTV;
    @BindView(R.id.ctLG)
    ConstraintLayout ctLG;
    @BindView(R.id.ctRoku)
    ConstraintLayout ctRoku;
    @BindView(R.id.ctSamsung)
    ConstraintLayout ctSamsung;
    @BindView(R.id.ctSony)
    ConstraintLayout ctSony;
    @BindView(R.id.fmRemote_scrollView)
    CustomScrollView fmRemote_scrollView;
    @BindView(R.id.imv_fireTVBack)
    ImageView imv_fireTVBack;
    @BindView(R.id.imv_fireTVHome)
    ImageView imv_fireTVHome;
    @BindView(R.id.imv_fireTVKeyBoard)
    ImageView imv_fireTVKeyBoard;
    @BindView(R.id.imv_fireTVMenu)
    ImageView imv_fireTVMenu;
    @BindView(R.id.imv_fireTVNext)
    ImageView imv_fireTVNext;
    @BindView(R.id.imv_fireTVPlay)
    ImageView imv_fireTVPlay;
    @BindView(R.id.imv_fireTVPre)
    ImageView imv_fireTVPre;
    @BindView(R.id.imv_fireTVSearch)
    ImageView imv_fireTVSearch;
    @BindView(R.id.imv_tab_lg)
    ImageView imv_tab_lg;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.llBackRoku)
    LinearLayout llBackRoku;
    @BindView(R.id.llBlueLG)
    LinearLayout llBlueLG;
    @BindView(R.id.llBlueSamsung)
    LinearLayout llBlueSamsung;
    @BindView(R.id.llFwd)
    LinearLayout llFwd;
    @BindView(R.id.llGreenLG)
    LinearLayout llGreenLG;
    @BindView(R.id.llGreenSamsung)
    LinearLayout llGreenSamsung;
    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.llInFor)
    LinearLayout llInFor;
    @BindView(R.id.llKeyboard)
    LinearLayout llKeyboard;
    @BindView(R.id.llMute)
    LinearLayout llMute;
    @BindView(R.id.llPlay)
    LinearLayout llPlay;
    @BindView(R.id.llPower)
    LinearLayout llPower;
    @BindView(R.id.llRedLG)
    LinearLayout llRedLG;
    @BindView(R.id.llRedSamsung)
    LinearLayout llRedSamsung;
    @BindView(R.id.llRev)
    LinearLayout llRev;
    @BindView(R.id.llVolumeDown)
    ConstraintLayout llVolumeDown;
    @BindView(R.id.llVolumeUp)
    ConstraintLayout llVolumeUp;
    @BindView(R.id.llYellowLG)
    LinearLayout llYellowLG;
    @BindView(R.id.llYellowSamsung)
    LinearLayout llYellowSamsung;
    @BindView(R.id.ll_input_samsung)
    LinearLayout ll_input_samsung;
    @BindView(R.id.ll_record)
    LinearLayout ll_record;
    @BindView(R.id.ll_remoteSonyAudio)
    LinearLayout ll_remoteSonyAudio;
    @BindView(R.id.ll_remoteSonyBlue)
    LinearLayout ll_remoteSonyBlue;
    @BindView(R.id.ll_remoteSonyBlueHelp)
    LinearLayout ll_remoteSonyBlueHelp;
    @BindView(R.id.ll_remoteSonyCC)
    LinearLayout ll_remoteSonyCC;
    @BindView(R.id.ll_remoteSonyChannelDown)
    LinearLayout ll_remoteSonyChannelDown;
    @BindView(R.id.ll_remoteSonyChannelUp)
    LinearLayout ll_remoteSonyChannelUp;
    @BindView(R.id.ll_remoteSonyGreen)
    LinearLayout ll_remoteSonyGreen;
    @BindView(R.id.ll_remoteSonyHome)
    LinearLayout ll_remoteSonyHome;
//    @BindView(R.id.ll_remoteSonyMenu)
//    LinearLayout ll_remoteSonyMenu;
    @BindView(R.id.ll_remoteSonyMute)
    LinearLayout ll_remoteSonyMute;
    @BindView(R.id.ll_remoteSonyNext)
    LinearLayout ll_remoteSonyNext;
    @BindView(R.id.ll_remoteSonyOptions)
    LinearLayout ll_remoteSonyOptions;
    @BindView(R.id.ll_remoteSonyPre)
    LinearLayout ll_remoteSonyPre;
    @BindView(R.id.ll_remoteSonyRed)
    LinearLayout ll_remoteSonyRed;
    @BindView(R.id.ll_remoteSonyReturn)
    LinearLayout ll_remoteSonyReturn;
    @BindView(R.id.ll_remoteSonySource)
    LinearLayout ll_remoteSonySource;
    @BindView(R.id.ll_remoteSonyStop)
    LinearLayout ll_remoteSonyStop;
    @BindView(R.id.ll_remoteSonyVolumeDown)
    LinearLayout ll_remoteSonyVolumeDown;
    @BindView(R.id.ll_remoteSonyVolumeUp)
    LinearLayout ll_remoteSonyVolumeUp;
    @BindView(R.id.ll_remoteSonyYellow)
    LinearLayout ll_remoteSonyYellow;
    @BindView(R.id.ll_remote_ch_down_ss)
    LinearLayout ll_remote_ch_down_ss;
    @BindView(R.id.ll_remote_ch_up_ss)
    LinearLayout ll_remote_ch_up_ss;
    @BindView(R.id.ll_remote_channel_ss)
    LinearLayout ll_remote_channel_ss;
    @BindView(R.id.ll_remote_home_ss)
    LinearLayout ll_remote_home_ss;
    @BindView(R.id.ll_remote_info_ss)
    LinearLayout ll_remote_info_ss;
    @BindView(R.id.ll_remote_mute_ss)
    LinearLayout ll_remote_mute_ss;
    @BindView(R.id.ll_remote_number_ss)
    LinearLayout ll_remote_number_ss;
    @BindView(R.id.ll_remote_program_ss)
    LinearLayout ll_remote_program_ss;
    @BindView(R.id.ll_remote_return)
    LinearLayout ll_remote_return;
    @BindView(R.id.ll_remote_setting_ss)
    LinearLayout ll_remote_setting_ss;
    @BindView(R.id.ll_remote_vol_dow_ss)
    LinearLayout ll_remote_vol_dow_ss;
    @BindView(R.id.ll_remote_vol_up_ss)
    LinearLayout ll_remote_vol_up_ss;
    @BindView(R.id.ll_rm1)
    LinearLayout ll_rm1;
    @BindView(R.id.ll_rm2)
    LinearLayout ll_rm2;
    @BindView(R.id.ll_rm3)
    LinearLayout ll_rm3;
    @BindView(R.id.ll_voice)
    LinearLayout ll_voice;
    @BindView(R.id.main_ads_native)
    ViewGroup main_ads_native;
    @BindView(R.id.rl_fragmentRemoteHeader)
    RelativeLayout rl_fragmentRemoteHeader;
    @BindView(R.id.rlt_remote_3d_model)
    LinearLayout rlt_remote_3d_model;
    @BindView(R.id.rlt_remote_ch_down)
    LinearLayout rlt_remote_ch_down;
    @BindView(R.id.rlt_remote_ch_up)
    LinearLayout rlt_remote_ch_up;
    @BindView(R.id.rlt_remote_channel_list)
    LinearLayout rlt_remote_channel_list;
    @BindView(R.id.rlt_remote_enter)
    LinearLayout rlt_remote_enter;
    @BindView(R.id.rlt_remote_home)
    LinearLayout rlt_remote_home;
    @BindView(R.id.rlt_remote_keyboard)
    LinearLayout rlt_remote_keyboard;
    @BindView(R.id.rlt_remote_number)
    LinearLayout rlt_remote_number;
    @BindView(R.id.rlt_remote_program_list)
    LinearLayout rlt_remote_program_list;
    @BindView(R.id.rlt_remote_volume_down)
    LinearLayout rlt_remote_volume_down;
    @BindView(R.id.rlt_remote_volume_mute)
    LinearLayout rlt_remote_volume_mute;
    @BindView(R.id.rlt_remote_volume_up)
    LinearLayout rlt_remote_volume_up;
    @BindView(R.id.scroll_lg)
    CustomScrollView scroll_lg;
    @BindView(R.id.view_remote_fire_tv)
    ViewRemoteTv view_remote_fire_tv;
    @BindView(R.id.view_remote_lg)
    ViewRemoteTv view_remote_lg;
    @BindView(R.id.view_remote_roku)
    ViewRemoteTv view_remote_roku;
    @BindView(R.id.view_remote_sony)
    ViewRemoteTv view_remote_sony;
    //    @BindView(R.id.view_remote_ss2)
//    ViewRemoteTv view_remote_ss;
    private int type = 0;
    private boolean power = true;
    private boolean isMute = false;
    private boolean is3DMode = false;
    private boolean isPlayingSony = true;
    private String[] numberKey = {ButtonKeyCode.KEYCODE_0.getValue(), ButtonKeyCode.KEYCODE_1.getValue(), ButtonKeyCode.KEYCODE_2.getValue(), ButtonKeyCode.KEYCODE_3.getValue(), ButtonKeyCode.KEYCODE_4.getValue(), ButtonKeyCode.KEYCODE_5.getValue(), ButtonKeyCode.KEYCODE_6.getValue(), ButtonKeyCode.KEYCODE_7.getValue(), ButtonKeyCode.KEYCODE_8.getValue(), ButtonKeyCode.KEYCODE_9.getValue()};
    SamsungRemoteManeger samsungRemote = null;
    private int numberClick = 0;

    /* JADX INFO: Access modifiers changed from: private */
    public static void lambda$performRequest$0(Object obj) throws Exception {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_remote, viewGroup, false);
        ButterKnife.bind(this, inflate);
        initView();
        return inflate;
    }

    @Override
    // com.magicapps.casttotv.tv.base.BaseFragment, androidx.fragment.app.Fragment
    public void onResume() {
        ViewGroup viewGroup;
        super.onResume();
        if ((IapUtils.isIapAll() || IapUtils.isPaymentMirror()) && (viewGroup = this.main_ads_native) != null) {
            viewGroup.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Unit actionCommon() {
        return Unit.INSTANCE;
    }

    @OnClick
    public void onclick(View view) {
        if (TVConnectUtils.getInstance().getDeviveName().contains("Samsung")) {
            SamsungRemoteManeger samsungRemoteManeger = SamSungRemoteController.getInstance(getContext()).getSamsungRemoteManeger();
            this.samsungRemote = samsungRemoteManeger;
            if (samsungRemoteManeger == null) {
                connectActivity();
                return;
            }
        }
        try {
            switch (view.getId()) {
                case R.id.imv_fireTVBack /* 2131362419 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    FireTVButtonPress(FireTVButtonKeyEvent.BACK.getValue());
                    return;
                case R.id.imv_fireTVHome /* 2131362420 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    FireTVButtonPress(FireTVButtonKeyEvent.HOME.getValue());
                    return;
                case R.id.imv_fireTVKeyBoard /* 2131362421 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    new KeyboardFireTVDialog(getActivity()).show();
                    return;
                case R.id.imv_fireTVMenu /* 2131362422 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    FireTVButtonPress(FireTVButtonKeyEvent.MENU.getValue());
                    return;
                case R.id.imv_fireTVNext /* 2131362423 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    FireTVButtonPress(FireTVButtonKeyEvent.FASTFORWARD.getValue());
                    return;
                case R.id.imv_fireTVPlay /* 2131362424 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    FireTVButtonPress(FireTVButtonKeyEvent.PLAYPAUSE.getValue());
                    return;
                case R.id.imv_fireTVPre /* 2131362425 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    FireTVButtonPress(FireTVButtonKeyEvent.REWIND.getValue());
                    return;
                case R.id.imv_fireTVSearch /* 2131362426 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    FireTVButtonPress("input keyevent 84");
                    new KeyboardFireTVDialog(getActivity()).show();
                    return;
                case R.id.llBack /* 2131362499 */:
                    if (checkRunContinue()) {
                        if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            FireTVButtonPress(FireTVButtonKeyEvent.BACK.getValue());
                            break;
                        } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            SamsungRemoteManeger samsungRemoteManeger2 = this.samsungRemote;
                            if (samsungRemoteManeger2 != null && samsungRemoteManeger2.isConnected()) {
                                this.samsungRemote.sendKeyEvent(ButtonKeyCode.BACK.getValue());
                                break;
                            } else {
                                connectActivity();
                                break;
                            }
                        } else if (TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            sonyButtonPress(SonyButtonKeyCode.Back.getValue());
                            break;
                        } else if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            performKeypress(KeypressKeyValues.BACK);
                            getContext().sendBroadcast(new Intent("wseemann.media.romote.UPDATE_DEVICE"));
                            break;
                        } else {
                            this.type = 13;
                            remote();
                            break;
                        }
                    } else {
                        return;
                    }
                case R.id.llBackRoku /* 2131362500 */:
                    if (checkRunContinue()) {
                        performKeypress(KeypressKeyValues.BACK);
                        getContext().sendBroadcast(new Intent("wseemann.media.romote.UPDATE_DEVICE"));
                        break;
                    } else {
                        return;
                    }
                case R.id.llBlueLG /* 2131362501 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 24;
                    remote();
                    return;
                case R.id.llBlueSamsung /* 2131362502 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger3 = this.samsungRemote;
                    if (samsungRemoteManeger3 != null && samsungRemoteManeger3.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.PROG_BLUE.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.llFwd /* 2131362510 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    performKeypress(KeypressKeyValues.FWD);
                    return;
                case R.id.llGreenLG /* 2131362512 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 22;
                    remote();
                    return;
                case R.id.llGreenSamsung /* 2131362513 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger4 = this.samsungRemote;
                    if (samsungRemoteManeger4 != null && samsungRemoteManeger4.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.PROG_GREEN.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.llHome /* 2131362515 */:
                    if (checkRunContinue()) {
                        performKeypress(KeypressKeyValues.HOME);
                        getContext().sendBroadcast(new Intent("wseemann.media.romote.UPDATE_DEVICE"));
                        break;
                    } else {
                        return;
                    }
                case R.id.llInFor /* 2131362518 */:
                    if (checkRunContinue()) {
                        performKeypress(KeypressKeyValues.INFO);
                        getContext().sendBroadcast(new Intent("wseemann.media.romote.UPDATE_DEVICE"));
                        break;
                    } else {
                        return;
                    }
                case R.id.llKeyboard /* 2131362519 */:
                    if (!TVConnectUtils.getInstance().isConnected()) {
                        connectActivity();
                        return;
                    }
                    try {
                        if (!checkRunContinue()) {
                            return;
                        }
                        new TextInputDialog(getContext()).show();
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                case R.id.llMute /* 2131362527 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    performKeypress(KeypressKeyValues.VOLUME_MUTE);
                    return;
                case R.id.llPlay /* 2131362532 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    performKeypress(KeypressKeyValues.PLAY);
                    return;
                case R.id.llPower /* 2131362533 */:
                    if (checkRunContinue()) {
                        if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            break;
                        } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            SamsungRemoteManeger samsungRemoteManeger5 = this.samsungRemote;
                            if (samsungRemoteManeger5 != null && samsungRemoteManeger5.isConnected()) {
                                this.samsungRemote.sendKeyEvent(ButtonKeyCode.POWER.getValue());
                                getContext();
                                break;
                            } else {
                                connectActivity();
                                break;
                            }
                        } else if (TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            sonyButtonPress(SonyButtonKeyCode.Power.getValue());
                            break;
                        } else if (!TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                            this.type = 1;
                            remote();
                            break;
                        } else {
                            break;
                        }
                    } else {
                        return;
                    }
                case R.id.llRedLG /* 2131362535 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 21;
                    remote();
                    return;
                case R.id.llRedSamsung /* 2131362536 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger6 = this.samsungRemote;
                    if (samsungRemoteManeger6 != null && samsungRemoteManeger6.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.PROG_RED.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.llReplay /* 2131362542 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    performKeypress(KeypressKeyValues.INTANT_REPLAY);
                    return;
                case R.id.llRev /* 2131362543 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    performKeypress(KeypressKeyValues.REV);
                    return;
                case R.id.llVolumeDown /* 2131362555 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    performKeypress(KeypressKeyValues.VOLUME_DOWN);
                    return;
                case R.id.llVolumeUp /* 2131362556 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    performKeypress(KeypressKeyValues.VOLUME_UP);
                    return;
                case R.id.llYellowLG /* 2131362560 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 23;
                    remote();
                    return;
                case R.id.llYellowSamsung /* 2131362561 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger7 = this.samsungRemote;
                    if (samsungRemoteManeger7 != null && samsungRemoteManeger7.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.PROG_YELLOW.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_input_samsung /* 2131362563 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger8 = this.samsungRemote;
                    if (samsungRemoteManeger8 != null && samsungRemoteManeger8.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEY_SOURCE.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_record /* 2131362565 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger9 = this.samsungRemote;
                    if (samsungRemoteManeger9 != null && samsungRemoteManeger9.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.MEDIA_RECORD.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remoteSonyAudio /* 2131362566 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Audio.getValue());
                    return;
                case R.id.ll_remoteSonyBlue /* 2131362567 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Blue.getValue());
                    return;
                case R.id.ll_remoteSonyBlueHelp /* 2131362568 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Help.getValue());
                    return;
                case R.id.ll_remoteSonyCC /* 2131362570 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Dot.getValue());
                    return;
                case R.id.ll_remoteSonyChannelDown /* 2131362571 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.ChannelDown.getValue());
                    return;
                case R.id.ll_remoteSonyChannelUp /* 2131362572 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.ChannelUp.getValue());
                    return;
                case R.id.ll_remoteSonyGreen /* 2131362573 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Green.getValue());
                    return;
                case R.id.ll_remoteSonyHome /* 2131362574 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Home.getValue());
                    return;
//                case R.id.ll_remoteSonyMenu /* 2131362576 */:
//                    if (!checkRunContinue()) {
//                        return;
//                    }
//                    sonyButtonPress(SonyButtonKeyCode.SyncMenu.getValue());
//                    return;
                case R.id.ll_remoteSonyMute /* 2131362577 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Mute.getValue());
                    return;
                case R.id.ll_remoteSonyNext /* 2131362578 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Next.getValue());
                    return;
                case R.id.ll_remoteSonyOptions /* 2131362580 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Options.getValue());
                    return;
                case R.id.ll_remoteSonyPlayPause /* 2131362581 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    if (this.isPlayingSony) {
                        sonyButtonPress(SonyButtonKeyCode.Pause.getValue());
                        this.isPlayingSony = false;
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Play.getValue());
                    this.isPlayingSony = true;
                    return;
                case R.id.ll_remoteSonyPre /* 2131362582 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Prev.getValue());
                    return;
                case R.id.ll_remoteSonyRed /* 2131362583 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Red.getValue());
                    return;
                case R.id.ll_remoteSonyReturn /* 2131362584 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Back.getValue());
                    return;
                case R.id.ll_remoteSonySource /* 2131362586 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Input.getValue());
                    return;
                case R.id.ll_remoteSonyStop /* 2131362587 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Stop.getValue());
                    return;
                case R.id.ll_remoteSonyVolumeDown /* 2131362589 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.VolumeDown.getValue());
                    return;
                case R.id.ll_remoteSonyVolumeUp /* 2131362590 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.VolumeUp.getValue());
                    return;
                case R.id.ll_remoteSonyYellow /* 2131362591 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    sonyButtonPress(SonyButtonKeyCode.Yellow.getValue());
                    return;
                case R.id.ll_remote_ch_down_ss /* 2131362592 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger10 = this.samsungRemote;
                    if (samsungRemoteManeger10 != null && samsungRemoteManeger10.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.CHANNEL_DOWN.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_ch_up_ss /* 2131362593 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger11 = this.samsungRemote;
                    if (samsungRemoteManeger11 != null && samsungRemoteManeger11.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.CHANNEL_UP.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_channel_ss /* 2131362594 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger12 = this.samsungRemote;
                    if (samsungRemoteManeger12 != null && samsungRemoteManeger12.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEY_CH_LIST.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_home_ss /* 2131362596 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger13 = this.samsungRemote;
                    if (samsungRemoteManeger13 != null && samsungRemoteManeger13.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.HOME.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_info_ss /* 2131362597 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger14 = this.samsungRemote;
                    if (samsungRemoteManeger14 != null && samsungRemoteManeger14.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEY_INFO.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_mute_ss /* 2131362599 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger15 = this.samsungRemote;
                    if (samsungRemoteManeger15 != null && samsungRemoteManeger15.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.VOLUME_MUTE.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_number_ss /* 2131362600 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger16 = this.samsungRemote;
                    if (samsungRemoteManeger16 != null && samsungRemoteManeger16.isConnected()) {
                        getContext();
                        if (getContext() == null) {
                            return;
                        }
                        new NumberKeyboardDialog(getContext(), new NumberKeyboardDialog.NumberKeyboardListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.1
                            @Override
                            // com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.NumberKeyboardListener
                            public void onNumberClick(int i) {
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("onClickKeyBoard: ");
                                    sb.append(i);
                                    RemoteFragment remoteFragment = RemoteFragment.this;
                                    remoteFragment.samsungRemote.sendKeyEvent(remoteFragment.numberKey[i]);
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("onClickKeyBoard error: ");
                                    sb2.append(e3.toString());
                                }
                            }
                        }).show();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_program_ss /* 2131362602 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger17 = this.samsungRemote;
                    if (samsungRemoteManeger17 != null && samsungRemoteManeger17.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.APP_LIST.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_return /* 2131362603 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger18 = this.samsungRemote;
                    if (samsungRemoteManeger18 != null && samsungRemoteManeger18.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.EXIT.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_setting_ss /* 2131362605 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger19 = this.samsungRemote;
                    if (samsungRemoteManeger19 != null && samsungRemoteManeger19.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEY_MENU.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_vol_dow_ss /* 2131362607 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger20 = this.samsungRemote;
                    if (samsungRemoteManeger20 != null && samsungRemoteManeger20.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.VOLUME_DOWN.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_remote_vol_up_ss /* 2131362608 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    SamsungRemoteManeger samsungRemoteManeger21 = this.samsungRemote;
                    if (samsungRemoteManeger21 != null && samsungRemoteManeger21.isConnected()) {
                        this.samsungRemote.sendKeyEvent(ButtonKeyCode.VOLUME_UP.getValue());
                        getContext();
                        return;
                    }
                    connectActivity();
                    return;
                case R.id.ll_rm1 /* 2131362609 */:
                    setTabControl(1);
                    return;
                case R.id.ll_rm2 /* 2131362610 */:
                    if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                        setTabControl(2);
                        return;
                    } else {
                        connectActivity();
                        return;
                    }
                case R.id.ll_rm3 /* 2131362611 */:
                    if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                        setTabControl(3);
                        return;
                    } else {
                        connectActivity();
                        return;
                    }
                case R.id.ll_voice /* 2131362614 */:
                    if (!TVConnectUtils.getInstance().isConnected()) {
                        connectActivity();
                        return;
                    } else if (!checkRunContinue() || !TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                        return;
                    } else {
                        promptSpeechInput();
                        return;
                    }
                case R.id.rlt_remote_3d_model /* 2131362935 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 16;
                    remote();
                    return;
                case R.id.rlt_remote_ch_down /* 2131362936 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 11;
                    remote();
                    return;
                case R.id.rlt_remote_ch_up /* 2131362937 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 10;
                    remote();
                    return;
                case R.id.rlt_remote_channel_list /* 2131362938 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 18;
                    remote();
                    return;
                case R.id.rlt_remote_enter /* 2131362940 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 15;
                    remote();
                    return;
                case R.id.rlt_remote_home /* 2131362941 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 14;
                    remote();
                    return;
                case R.id.rlt_remote_keyboard /* 2131362942 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 17;
                    remote();
                    return;
                case R.id.rlt_remote_number /* 2131362944 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 19;
                    remote();
                    return;
                case R.id.rlt_remote_program_list /* 2131362946 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 20;
                    remote();
                    return;
                case R.id.rlt_remote_volume_down /* 2131362949 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 3;
                    remote();
                    return;
                case R.id.rlt_remote_volume_mute /* 2131362950 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 12;
                    remote();
                    return;
                case R.id.rlt_remote_volume_up /* 2131362951 */:
                    if (!checkRunContinue()) {
                        return;
                    }
                    this.type = 2;
                    remote();
                    return;
                default:
            }
        } catch (Exception unused) {
        }
    }

    private boolean checkRunContinue() {
        if (((Boolean) SharedPrefsUtil.getInstance().get(Const.KEY_TIER, Boolean.class)).booleanValue() || IapUtils.isIapAll() || IapUtils.isPaymentMirror()) {
            return true;
        }
        gotoPremium();
        return false;
    }

    private void gotoPremium() {
        Utils.nextScreen(getActivity());
    }

    private void setTabControl(int i) {
        this.ll_rm1.setBackgroundResource(R.drawable.bg_rm_no_select);
        this.ll_rm2.setBackgroundResource(R.drawable.bg_rm_no_select);
        this.ll_rm3.setBackgroundResource(R.drawable.bg_rm_no_select);
        if (i == 1) {
            this.ll_rm1.setBackgroundResource(R.drawable.bg_rm1_selected);
        } else if (i == 2) {
            this.ll_rm2.setBackgroundResource(R.drawable.bg_rm1_selected);
        } else if (i == 3) {
            this.ll_rm3.setBackgroundResource(R.drawable.bg_rm1_selected);
        }
        try {
            if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.view_remote_fire_tv.setTypeVew(i);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
//                this.view_remote_ss.setTypeVew(i);
            } else if (TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.view_remote_sony.setTypeVew(i);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.view_remote_lg.setTypeVew(i);
                if (i == 2) {
                    this.imv_tab_lg.setVisibility(0);
                    this.view_remote_lg.setVisibility(8);
                    return;
                }
                this.imv_tab_lg.setVisibility(8);
                this.view_remote_lg.setVisibility(0);
            } else {
                this.view_remote_roku.setTypeVew(i);
            }
        } catch (Exception unused) {
        }
    }

    private void initView() {
        if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.ctRoku.setVisibility(0);
                this.ctLG.setVisibility(8);
                this.ctSamsung.setVisibility(8);
                this.ctFireTV.setVisibility(8);
                this.ctSony.setVisibility(8);
                this.llBack.setVisibility(8);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.ctRoku.setVisibility(8);
                this.ctSamsung.setVisibility(8);
                this.ctLG.setVisibility(0);
                this.ctFireTV.setVisibility(8);
                this.ctSony.setVisibility(8);
                this.llBack.setVisibility(0);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.ctRoku.setVisibility(8);
                this.ctLG.setVisibility(8);
                this.ctSamsung.setVisibility(0);
                this.ctFireTV.setVisibility(8);
                this.ctSony.setVisibility(8);
                this.llBack.setVisibility(0);
                this.samsungRemote = SamSungRemoteController.getInstance(getContext()).getSamsungRemoteManeger();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.ctFireTV.setVisibility(0);
                this.ctRoku.setVisibility(8);
                this.ctLG.setVisibility(8);
                this.ctSamsung.setVisibility(8);
                this.ctSony.setVisibility(8);
                this.rl_fragmentRemoteHeader.setVisibility(8);
                this.llBack.setVisibility(0);
                if (getActivity() == null) {
                    return;
                }
                startActivity(new Intent(getActivity(), GuideFireTVActivity.class));
                Utils.nextScreen(getActivity());
                final DialogConnectFireTV dialogConnectFireTV = new DialogConnectFireTV(getActivity(), ConnectActivity.IpAddressFireTV);
                dialogConnectFireTV.show();
                FireTVManager fireTVManager2 = new FireTVManager(getActivity());
                fireTVManager = fireTVManager2;
                fireTVManager2.createConnection(ConnectActivity.IpAddressFireTV);
                fireTVManager.setListener(new FireTVManager.UpdateDataListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.2
                    @Override
                    // com.magicapps.casttotv.tv.utils.remote.firetv.FireTVManager.UpdateDataListener
                    public void onSuccess() {
                        if (dialogConnectFireTV.isShowing()) {
                            dialogConnectFireTV.dismiss();
                        }
                        RemoteFragment.fireTVManager.getChannelsList("pm list packages -3");
                    }
                });
            } else if (TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.ctFireTV.setVisibility(8);
                this.ctRoku.setVisibility(8);
                this.ctLG.setVisibility(8);
                this.ctSamsung.setVisibility(8);
                this.ctSony.setVisibility(0);
                this.llBack.setVisibility(0);
                RemoteSonyManager.getInstance().accessControlWithPIN(getActivity(), null, RemoteSonyManager.TEST_IP, TVConnectUtils.getInstance().getConnectableDevice().getIpAddress(), new RemoteSonyManager.NetworkListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.3
                    @Override
                    // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.NetworkListener
                    public void onDeviceRegistrationCompleted(boolean z) {
                    }

                    @Override
                    // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.NetworkListener
                    public void onDevicePincodeGenerated(boolean z) {
                        new DialogPinCode(RemoteFragment.this.getActivity()).show();
                    }

                    @Override
                    // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.NetworkListener
                    public void onFailedToConnect() {
                        try {
                            Toast.makeText(RemoteFragment.this.getContext(), RemoteFragment.this.getString(R.string.fail_connect_sony), 0).show();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
        } else {
            this.ctRoku.setVisibility(0);
            this.ctLG.setVisibility(8);
            this.ctSamsung.setVisibility(8);
            this.ctFireTV.setVisibility(8);
            this.ctSony.setVisibility(8);
            this.llBack.setVisibility(8);
        }
        setUpView();
        setUpNumber();
        RemoteFragment.this.callbackDone();
        TouchCustom touchCustom = new TouchCustom(TVConnectUtils.getInstance().getConnectableDevice());
        this.imv_tab_lg.setOnTouchListener(touchCustom);
        touchCustom.setListener(new TouchCustom.IMoveLG() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.4
            @Override // com.magicapps.casttotv.tv.customview.TouchCustom.IMoveLG
            public void moveCancel() {
                RemoteFragment.this.fmRemote_scrollView.setEnableScrolling(true);
                RemoteFragment.this.scroll_lg.setEnableScrolling(true);
            }

            @Override // com.magicapps.casttotv.tv.customview.TouchCustom.IMoveLG
            public void moveDown() {
                RemoteFragment.this.scroll_lg.setEnableScrolling(false);
                RemoteFragment.this.fmRemote_scrollView.setEnableScrolling(false);
            }
        });
    }

    private void setUpNumber() {
        this.view_remote_roku.setLister3(this);
        this.view_remote_lg.setLister3(this);
//        this.view_remote_ss.setLister3(this);
        this.view_remote_fire_tv.setLister3(this);
        this.view_remote_sony.setLister3(this);
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab1
    public void clickLeft() {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            connectActivity();
        } else if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            performKeypress(KeypressKeyValues.LEFT);
        } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            this.type = 5;
            remote();
        } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
            if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                this.samsungRemote.sendKeyEvent(ButtonKeyCode.DPAD_LEFT.getValue());
                getContext();
                return;
            }
            connectActivity();
        } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            FireTVButtonPress(FireTVButtonKeyEvent.LEFT.getValue());
        } else if (!TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
        } else {
            sonyButtonPress(SonyButtonKeyCode.Left.getValue());
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab1
    public void clickRight() {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            connectActivity();
        } else if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            performKeypress(KeypressKeyValues.RIGHT);
        } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            this.type = 7;
            remote();
        } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
            if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                this.samsungRemote.sendKeyEvent(ButtonKeyCode.DPAD_RIGHT.getValue());
                getContext();
                return;
            }
            connectActivity();
        } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            FireTVButtonPress(FireTVButtonKeyEvent.RIGHT.getValue());
        } else if (!TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
        } else {
            sonyButtonPress(SonyButtonKeyCode.Right.getValue());
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab1
    public void clickTop() {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            connectActivity();
        } else if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            performKeypress(KeypressKeyValues.UP);
        } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            this.type = 4;
            remote();
        } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
            if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                this.samsungRemote.sendKeyEvent(ButtonKeyCode.DPAD_UP.getValue());
                getContext();
                return;
            }
            connectActivity();
        } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            FireTVButtonPress(FireTVButtonKeyEvent.UP.getValue());
        } else if (!TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
        } else {
            sonyButtonPress(SonyButtonKeyCode.Up.getValue());
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab1
    public void clickBottom() {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            connectActivity();
        } else if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            performKeypress(KeypressKeyValues.DOWN);
        } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            this.type = 8;
            remote();
        } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
            if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                this.samsungRemote.sendKeyEvent(ButtonKeyCode.DPAD_DOWN.getValue());
                getContext();
                return;
            }
            connectActivity();
        } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            FireTVButtonPress(FireTVButtonKeyEvent.DOWN.getValue());
        } else if (!TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
        } else {
            sonyButtonPress(SonyButtonKeyCode.Down.getValue());
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab1
    public void clickOK() {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            connectActivity();
        } else if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            performKeypress(KeypressKeyValues.SELECT);
        } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            this.type = 6;
            remote();
        } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
            if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                this.samsungRemote.sendKeyEvent(ButtonKeyCode.ENTER.getValue());
                getContext();
                return;
            }
            connectActivity();
        } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            FireTVButtonPress(FireTVButtonKeyEvent.OK.getValue());
        } else if (!TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
        } else {
            sonyButtonPress(SonyButtonKeyCode.Confirm.getValue());
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab1
    public void clickDowns() {
        this.fmRemote_scrollView.setEnableScrolling(false);
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab1
    public void clickCancel() {
        this.fmRemote_scrollView.setEnableScrolling(true);
    }

    private void setUpView() {
        this.view_remote_roku.setLister1(this);
        this.view_remote_sony.setLister1(this);
//        this.view_remote_ss.setLister1(this);
        this.view_remote_fire_tv.setLister1(this);
        this.view_remote_lg.setLister1(this);
    }

    public void promptSpeechInput() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
        intent.putExtra("android.speech.extra.PROMPT", getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, 68);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getContext().getApplicationContext(), getString(R.string.speech_not_supported), 0).show();
        }
    }

    private void sendStringLiteral(String str) {
        String deviceURL = CommandHelper.getDeviceURL(getActivity());
        new RequestTask(new JakuRequest(new KeypressRequest(deviceURL, KeypressKeyValues.LIT_.getValue() + str), null), new RequestCallback() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.5
            @Override
            // com.magicapps.casttotv.tv.utils.remote.roku.tasks.RequestCallback
            public void onErrorResponse(RequestTask.Result result) {
            }

            @Override
            // com.magicapps.casttotv.tv.utils.remote.roku.tasks.RequestCallback
            public void requestResult(RokuRequestTypes rokuRequestTypes, RequestTask.Result result) {
            }
        }).execute(RokuRequestTypes.keypress);
        new ArrayDeque();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        String str;
        super.onActivityResult(i, i2, intent);
        if (i == 68 && i2 == -1 && intent != null) {
            ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("android.speech.extra.RESULTS");
            StringBuilder sb = new StringBuilder();
            sb.append("onActivityResult: 2222");
            sb.append(stringArrayListExtra.get(0));
            if (stringArrayListExtra.size() <= 0 || (str = stringArrayListExtra.get(0)) == null) {
                return;
            }
            for (int i3 = 0; i3 < str.length(); i3++) {
                String valueOf = String.valueOf(str.charAt(i3));
                if (valueOf.equals(" ")) {
                    valueOf = "%20";
                }
                sendStringLiteral(valueOf);
            }
        }
    }

    private void performRequest(JakuRequest jakuRequest, RokuRequestTypes rokuRequestTypes) {
        if (getContext() == null || getContext().getApplicationContext() == null) {
            return;
        }
        Observable.fromCallable(new RxRequestTask(getContext().getApplicationContext(), jakuRequest, rokuRequestTypes)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                RemoteFragment.lambda$performRequest$0(o);
            }
        });
    }

    private void performKeypress(KeypressKeyValues keypressKeyValues) {
        if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
            ViewUtils.provideHapticFeedback(getContext(), 100);
            String deviceURL = CommandHelper.getDeviceURL(getActivity());
            StringBuilder sb = new StringBuilder();
            sb.append("performKeypress: ");
            sb.append(deviceURL);
            performRequest(new JakuRequest(new KeypressRequest(deviceURL, keypressKeyValues.getValue()), null), RokuRequestTypes.keypress);
            return;
        }
        startActivity(new Intent(getContext(), ConnectActivity.class));
        Utils.nextScreen(getActivity());
    }

    private void remote() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                int i = this.type;
                boolean z = true;
                if (i == 1) {
                    power();
                } else if (i == 2) {
                    if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                        setupVolume(true);
                    } else {
                        connectActivity();
                    }
                } else if (i == 3) {
                    if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                        setupVolume(false);
                    } else {
                        connectActivity();
                    }
                } else if (i == 4) {
                    controlUpDownLeftRight(1);
                } else if (i == 5) {
                    controlUpDownLeftRight(2);
                } else if (i == 6) {
                    controlUpDownLeftRight(3);
                } else if (i == 7) {
                    controlUpDownLeftRight(4);
                } else if (i == 8) {
                    controlUpDownLeftRight(5);
                } else if (i == 9) {
                    input();
                } else if (i == 10) {
                    channel(true);
                } else if (i == 11) {
                    channel(false);
                } else if (i == 12) {
                    controlMetuBackHomeEnter(1);
                } else if (i == 13) {
                    controlMetuBackHomeEnter(2);
                } else if (i == 14) {
                    controlMetuBackHomeEnter(3);
                } else if (i == 15) {
                    controlMetuBackHomeEnter(4);
                } else if (i == 16) {
                    if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                        if (this.is3DMode) {
                            z = false;
                        }
                        this.is3DMode = z;
                        ((TVControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(TVControl.class)).set3DEnabled(this.is3DMode, null);
                    } else {
                        connectActivity();
                    }
                } else if (i == 17) {
                    keyboard();
                } else if (i == 18) {
                    if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                        ((TVControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(TVControl.class)).getChannelList(new TVControl.ChannelListListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.8
                            @Override
                            // com.connectsdk.service.capability.listeners.ResponseListener
                            public void onSuccess(List<ChannelInfo> list) {
                                try {
                                    if (RemoteFragment.this.getContext() == null) {
                                        return;
                                    }
                                    Toast.makeText(RemoteFragment.this.getContext(), (int) R.string.not_support, 0).show();
                                } catch (Resources.NotFoundException e2) {
                                    e2.printStackTrace();
                                }
                            }

                            @Override // com.connectsdk.service.capability.listeners.ErrorListener
                            public void onError(ServiceCommandError serviceCommandError) {
                                try {
                                    if (RemoteFragment.this.getContext() == null) {
                                        return;
                                    }
                                    Toast.makeText(RemoteFragment.this.getContext(), "error", 0).show();
                                } catch (Resources.NotFoundException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        });
                    } else {
                        connectActivity();
                    }
                } else if (i == 19) {
                    numberKeyboard();
                } else if (i == 20) {
                    if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                        ((TVControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(TVControl.class)).getProgramList(new TVControl.ProgramListListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.9
                            @Override
                            // com.connectsdk.service.capability.listeners.ResponseListener
                            public void onSuccess(ProgramList programList) {
                                try {
                                    if (RemoteFragment.this.getContext() == null) {
                                        return;
                                    }
                                    Toast.makeText(RemoteFragment.this.getContext(), (int) R.string.not_support, 0).show();
                                } catch (Resources.NotFoundException e2) {
                                    e2.printStackTrace();
                                }
                            }

                            @Override // com.connectsdk.service.capability.listeners.ErrorListener
                            public void onError(ServiceCommandError serviceCommandError) {
                                try {
                                    if (RemoteFragment.this.getContext() == null) {
                                        return;
                                    }
                                    Toast.makeText(RemoteFragment.this.getContext(), "error", 0).show();
                                } catch (Resources.NotFoundException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        });
                    } else {
                        connectActivity();
                    }
                } else if (i == 21) {
                    controlUpDownLeftRight(6);
                } else if (i == 22) {
                    controlUpDownLeftRight(7);
                } else if (i == 23) {
                    controlUpDownLeftRight(8);
                } else if (i == 24) {
                    controlUpDownLeftRight(9);
                }
                if (!TVConnectUtils.getInstance().isConnected()) {
                    return;
                }
                getContext();
                return;
            }
            connectActivity();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void input() {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            connectActivity();
        }
    }

    private void keyboard() {
        try {
            if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                if (getActivity() != null) {
                    new KeyboardDialog(getActivity()).show();
                } else {
                    Toast.makeText(getContext(), "error", 1).show();
                }
            } else {
                connectActivity();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void numberKeyboard() {
        new NumberKeyboardDialog(getActivity(), new NumberKeyboardDialog.NumberKeyboardListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.10
            @Override
            // com.magicapps.casttotv.tv.dialog.NumberKeyboardDialog.NumberKeyboardListener
            public void onNumberClick(int i) {
                if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
                    RemoteFragment.this.connectActivity();
                    return;
                }
                KeyControl keyControl = (KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class);
                try {
                    if (i == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onNumberClick: ");
                        KeyControl.KeyCode keyCode = KeyControl.KeyCode.NUM_0;
                        sb.append(keyCode);
                        keyControl.sendKeyCode(keyCode, null);
                    } else if (i == 1) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_1, null);
                    } else if (i == 2) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_2, null);
                    } else if (i == 3) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_3, null);
                    } else if (i == 4) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_4, null);
                    } else if (i == 5) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_5, null);
                    } else if (i == 6) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_6, null);
                    } else if (i == 7) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_7, null);
                    } else if (i == 8) {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_8, null);
                    } else if (i != 9) {
                    } else {
                        keyControl.sendKeyCode(KeyControl.KeyCode.NUM_9, null);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }).show();
    }

    private void controlUpDownLeftRight(int i) {
        if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
            KeyControl keyControl = (KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class);
            if (i == 1) {
                keyControl.up(null);
                return;
            } else if (i == 2) {
                keyControl.left(null);
                return;
            } else if (i == 3) {
                keyControl.sendKeyCode(KeyControl.KeyCode.ENTER, null);
                return;
            } else if (i == 4) {
                keyControl.right(null);
                return;
            } else if (i == 5) {
                keyControl.down(null);
                return;
            }
//            else if (i == 6) {
//                keyControl.red(null);
//                return;
//            } else if (i == 7) {
//                keyControl.green(null);
//                return;
//            } else if (i == 8) {
//                keyControl.yellow(null);
//                return;
//            } else if (i != 9) {
//                return;
//            } else {
//                keyControl.blue(null);
//                return;
//            }
        }
        connectActivity();
    }

    private void controlMetuBackHomeEnter(int i) {
        if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
            KeyControl keyControl = (KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class);
            if (i == 1) {
                boolean z = !this.isMute;
                this.isMute = z;
                ((VolumeControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(VolumeControl.class)).setMute(z, null);
                return;
            } else if (i == 2) {
                keyControl.back(null);
                return;
            } else if (i == 3) {
                keyControl.home(null);
                return;
            } else if (i != 4) {
                return;
            } else {
                keyControl.sendKeyCode(KeyControl.KeyCode.ENTER, null);
                return;
            }
        }
        connectActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectActivity() {
        startActivity(new Intent(getActivity(), ConnectActivity.class));
        Utils.nextScreen(getActivity());
    }

    private void power() {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            return;
        }
        PowerControl powerControl = (PowerControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(PowerControl.class);
        if (this.power) {
            powerControl.powerOff(null);
        } else {
            powerControl.powerOn(null);
        }
        this.power = !this.power;
    }

    private void channel(boolean z) {
        if (!TVConnectUtils.getInstance().isConnected() || TVConnectUtils.getInstance().isConnectWeb) {
            connectActivity();
        } else if (z) {
            if (!TVConnectUtils.getInstance().getConnectableDevice().hasCapability(TVControl.Channel_Up)) {
                return;
            }
            getTVControl().channelUp(null);
        } else if (!TVConnectUtils.getInstance().getConnectableDevice().hasCapability(TVControl.Channel_Up)) {
        } else {
            getTVControl().channelDown(null);
        }
    }

    private TVControl getTVControl() {
        return (TVControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(TVControl.class);
    }

    private void setupVolume(boolean z) {
        float max;
        VolumeControl volumeControl = (VolumeControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(VolumeControl.class);
        if (volumeControl != null) {
            if (z) {
                max = Math.min(TVConnectUtils.getInstance().volume + 0.01f, 1.0f);
            } else {
                max = Math.max(TVConnectUtils.getInstance().volume - 0.01f, 0.0f);
            }
            TVConnectUtils.getInstance().volume = max;
            volumeControl.setVolume(TVConnectUtils.getInstance().volume, null);
        }
    }

    public void FireTVButtonPress(String str) {
        try {
            new AsyncTaskRunner().execute(ConnectActivity.IpAddressFireTV, str, PListParser.TAG_FALSE);
            ViewUtils.provideHapticFeedback(getContext(), 100);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void FireTVButtonPressDialog(String str, Context context) {
        try {
            new AsyncTaskRunner().execute(ConnectActivity.IpAddressFireTV, str, PListParser.TAG_FALSE);
            ViewUtils.provideHapticFeedback(context, 100);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickOne() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_1, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_1, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_1.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num1.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickTwo() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_2, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_2, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_2.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num2.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickThree() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_3, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_3, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_3.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num3.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickFour() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_4, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_4, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_4.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num4.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickFive() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_5, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_5, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_5.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num5.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickSix() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_6, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_6, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_6.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num6.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickSeven() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_7, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_7, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_7.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num7.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickEight() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_8, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_8, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_8.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num8.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickNine() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_9, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_9, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_9.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num9.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.magicapps.casttotv.tv.customview.ViewRemoteTv.IClickTab3
    public void clickZero() {
        try {
            if (getContext() != null) {
                ViewUtils.provideHapticFeedback(getContext(), 100);
            }
            KeyControl keyControl = (KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class);
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                if (keyControl == null) {
                    return;
                }
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_0, null);
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                if (keyControl == null) {
                    return;
                }
                ((KeyControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(KeyControl.class)).sendKeyCode(KeyControl.KeyCode.NUM_0, null);
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                SamsungRemoteManeger samsungRemoteManeger = this.samsungRemote;
                if (samsungRemoteManeger != null && samsungRemoteManeger.isConnected()) {
                    this.samsungRemote.sendKeyEvent(ButtonKeyCode.KEYCODE_0.getValue());
                    getContext();
                    return;
                }
                connectActivity();
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice()) || !TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
            } else {
                sonyButtonPress(SonyButtonKeyCode.Num0.getValue());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class AsyncTaskRunner extends AsyncTask<String, String, Void> {
        private AsyncTaskRunner() {
        }

        @Override // com.magicapps.casttotv.tv.utils.remote.roku.util.AsyncTask
        public Void doInBackground(String... strArr) {
            try {
                AdbConnection adbConnection = RemoteFragment.fireTVManager.getAdbConnection();
                adbConnection.open("shell:" + strArr[1]);
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    private void sonyButtonPress(String str) {
        if (getActivity() != null) {
            ViewUtils.provideHapticFeedback(getActivity(), 100);
        }
        RemoteSonyManager.getInstance().remoteButton(getActivity(), str, new RemoteSonyManager.CommandListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteFragment.11
            @Override
            // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.CommandListener
            public void onCommandSucceeded() {
            }

            @Override
            // com.magicapps.casttotv.tv.utils.remote.sony.RemoteSonyManager.CommandListener
            public void onUnauthorizedError() {
            }
        });
    }
}
