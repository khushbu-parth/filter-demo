package com.cast.tv.screen.mirroring.screencasting.UI.cast;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.Contract.SPContracts;
import com.cast.tv.screen.mirroring.screencasting.Dialog.ConnectDeviceDialog;
import com.cast.tv.screen.mirroring.screencasting.Dialog.NormalTipDialog;
import com.cast.tv.screen.mirroring.screencasting.Dialog.PlayLoadingDialog;
import com.cast.tv.screen.mirroring.screencasting.Dialog.PlayQueueDialog;
import com.cast.tv.screen.mirroring.screencasting.Dialog.PlayTipDialog;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.ConnectStatus;
import com.cast.tv.screen.mirroring.screencasting.Observer.DeviceVolume;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Report.ReportUtil;
import com.cast.tv.screen.mirroring.screencasting.UI.help.HelpActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.SPUtils;
import com.cast.tv.screen.mirroring.screencasting.Utils.TimeUtil;

import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportState;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;
import java.util.Random;

public class  AudioVideoCastActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = "AudioVideoCastActivity";
    private ImageView imageChangePlayMode;
    private ImageView imagePlay,imageView2,imageStop;
    private boolean isAddVolume;
    private boolean isLoadNext;
    private boolean isPlayNext;
    private boolean isPlaying;
    private boolean isSetVolume;
    private boolean isShowPlayTipDialog;
    private int mCurrentPlayModel;
    private int mIndex;
    private List<FileModel> mList;
    private long mOpenPageTime;
    private PlayLoadingDialog mPlayLoadingDialog;
    private PlayTipDialog mPlayWellTipDialog;
    private PlayQueueDialog mQueueDialog;
    private SeekBar mSeekBar;
    private TextView mTextTitle,mPlayText;
    private TextView mTextVolume;
    private TextView textAllTime;
    private TextView textPlayTime,mTextStop,mTextQueue;
    private int totalTime;
    private Intent intent;
    private String castType,castNameDisplay;
    private Runnable mShowPlayTipDialogRunnable = new Runnable() {
        @Override
        public void run() {
            if (AudioVideoCastActivity.this.isShowPlayTipDialog) {
               // AudioVideoCastActivity.this.showPlayTipDialog();
                AudioVideoCastActivity.this.isShowPlayTipDialog = false;
            }
        }
    };

    private Runnable mPlayCompleteRunnable = new Runnable() {
        public void run() {
            try {
                if (AudioVideoCastActivity.this.mPlayLoadingDialog != null) {
                    AudioVideoCastActivity.this.mPlayLoadingDialog.dismiss();
                    AudioVideoCastActivity.this.mPlayLoadingDialog = null;

                }
                if (!AudioVideoCastActivity.this.isLoadNext)
                    return;
                AudioVideoCastActivity.this.playComplete();
                return;
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }
    };

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public int getLayoutId() {
        return R.layout.activity_audio_video_cast;

    }    private Runnable mPlayTimeRunnable = new Runnable() {
        @Override
        public final void run() {
            AudioVideoCastActivity.this.runPlayTime();
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {

    }    private Runnable mHideVolumeRunnable = new Runnable() {
        @Override
        public void run() {
            AudioVideoCastActivity.this.mTextVolume.setVisibility(GONE);
            AudioVideoCastActivity.this.mHandler.removeCallbacks(AudioVideoCastActivity.this.mHideVolumeRunnable);
        }
    };

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCreate(Bundle bundle) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onCreate(bundle);
    }

    @Override
    protected void init() {
        ReportUtil.loadControlVAPage();
        intent = getIntent();
       castType = intent.getStringExtra("castType");
       castNameDisplay=intent.getStringExtra("castNameDisplay");
        this.mOpenPageTime = System.currentTimeMillis();
        this.mIndex = AudioVisualHelper.getSelectChildIndex();
        Log.d("Index", String.valueOf(this.mIndex));
        this.mList = AudioVisualHelper.getAudioVisualPlayList();
        Log.d("ListName", String.valueOf(this.mList.get(this.mIndex).getDisplayName()));
        resetIndex();
        imageStop=findViewById(R.id.image_stop);
        mPlayText=findViewById(R.id.text_play);
        mTextStop=findViewById(R.id.text_stop);
        mTextQueue=findViewById(R.id.text_queue);
        mTextTitle=findViewById(R.id.text_title);
        findViewById(R.id.image_back).setOnClickListener(this);
        findViewById(R.id.image_fast_forward).setOnClickListener(this);
        findViewById(R.id.image_back_off).setOnClickListener(this);
        findViewById(R.id.image_cast_screen).setOnClickListener(this);
        findViewById(R.id.image_help).setOnClickListener(this);
        imageStop.setOnClickListener(this);
        findViewById(R.id.image_volume_add).setOnClickListener(this);
        findViewById(R.id.image_volume_reduce).setOnClickListener(this);
        findViewById(R.id.image_queue).setOnClickListener(this);
        findViewById(R.id.image_exit).setOnClickListener(this);
        this.mTextVolume = (TextView) findViewById(R.id.text_volume);
        ImageView imageView = (ImageView) findViewById(R.id.image_mode);
        this.imageChangePlayMode = imageView;
        imageView.setOnClickListener(this);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        this.mSeekBar = seekBar;
        seekBar.setOnSeekBarChangeListener(this);
        this.textPlayTime = (TextView) findViewById(R.id.text_play_time);
        this.textAllTime = (TextView) findViewById(R.id.text_all_time);
        setPlayTime();
        setTransportState();
         imageView2 = (ImageView) findViewById(R.id.image_play);
        this.imagePlay = imageView2;
        imageView2.setOnClickListener(this);
        findViewById(R.id.image_previous).setOnClickListener(this);
        findViewById(R.id.image_next).setOnClickListener(this);
        initPage();
        setConnectDlnaVolume();
        setCastScreenStatus();

        this.mTextStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
        this.mTextQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlayListDialog();
            }
        });
    }

    private void initPage() {
        setPageTitle();
        setPlayMode();
        DLNAHelper.getTransportInfo();
        DLNAHelper.getPositionInfo();
    }

    private void setPageTitle() {
        FileModel fileModel;
        int size = ListUtil.getSize(this.mList);
        int i = this.mIndex;
        if (size <= i || (fileModel = this.mList.get(i)) == null) {
            return;
        }

        try{
            if(!castNameDisplay.isEmpty()){
               this.mTextTitle.setText(castNameDisplay);
            }else{
                this.mTextTitle.setText(fileModel.getDisplayName());
            }
            if(castType.equals("Video")){
                this.mTextStop.setText("Stop Video");
                this.mTextQueue.setText("List of Video Files");
            }else if(castType.equals("Audio")){
                this.mTextStop.setText("Stop Audio");
                this.mTextQueue.setText("List of Audio Files");
            }else{
                this.mTextStop.setText("Stop");
                this.mTextQueue.setText("List of Files");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
}

    private void showRewardDialog() {
        //  ShowRewardAdDialog.newInstance().show(getSupportFragmentManager(), "Reward");
    }

    private void setPlayMode() {
        int intValue = ((Integer) SPUtils.get(this.mContext, SPContracts.PLAY_MODE, 100)).intValue();
        this.mCurrentPlayModel = intValue;
        if (intValue == 100) {
            this.imageChangePlayMode.setImageResource(R.mipmap.icon_video_mode_loop);
        } else if (intValue == 101) {
            this.imageChangePlayMode.setImageResource(R.mipmap.icon_video_mode_random);
        } else {
            this.imageChangePlayMode.setImageResource(R.mipmap.icon_video_mode_single);
        }
    }

    private void setConnectDlnaVolume() {
        DLNAHelper.mDeviceVolume.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                AudioVideoCastActivity.this.setConnectDlnaVolume$0$AudioVideoCastActivity((DeviceVolume) obj);
            }
        });
    }

    public void setConnectDlnaVolume$0$AudioVideoCastActivity(DeviceVolume deviceVolume) {
        if (!this.isSetVolume) {
            return;
        }
        int i = deviceVolume.volume;
        L.i("AudioVideoCastActivity", "setConnectDlnaVolume volume: " + i);
        if (this.isAddVolume) {
            if (i < 100) {
                i++;
                DLNAHelper.setVolume(i);
            }
        } else if (i > 0) {
            i--;
            DLNAHelper.setVolume(i);
        }
        setTextVolume(i);
    }

    private void setTextVolume(int i) {
        this.isSetVolume = false;
        if (this.mTextVolume.getVisibility() == GONE) {
            this.mTextVolume.setVisibility(View.VISIBLE);
        }
        TextView textView = this.mTextVolume;
        textView.setText(i + "%");

        Log.e("===", "Volume" + i + "%");
        this.mHandler.removeCallbacks(this.mHideVolumeRunnable);
        this.mHandler.postDelayed(this.mHideVolumeRunnable, 2000L);
    }

    private void setCastScreenStatus() {
        DLNAHelper.mConnectStatus.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                AudioVideoCastActivity.this.setCastScreenStatus$1$AudioVideoCastActivity((ConnectStatus) obj);
            }
        });
    }

    public void setCastScreenStatus$1$AudioVideoCastActivity(ConnectStatus connectStatus) {
        if (connectStatus == ConnectStatus.FAIL) {
            L.i("AudioVideoCastActivity", "ConnectStatus fail");
            this.isShowPlayTipDialog = true;
            Handler handler = this.mHandler;
            if (handler == null) {
                return;
            }
            handler.removeCallbacks(this.mShowPlayTipDialogRunnable);
            this.mHandler.postDelayed(this.mShowPlayTipDialogRunnable, 2000L);
        } else if (connectStatus != ConnectStatus.DISCONNECT && connectStatus != ConnectStatus.STOP) {


        } else {
            this.isPlaying = false;
            this.imagePlay.setImageResource(R.mipmap.icon_video_playwhite);
            this.mHandler.removeCallbacks(this.mPlayTimeRunnable);
        }
    }

    @Override
    public void onClick(View view) {
        int i;
        switch (view.getId()) {
            case R.id.image_back:
                clickBack();
                return;
            case R.id.image_back_off:
                backOff();
                return;
            case R.id.image_cast_screen:
                try {
                    showCastScreenDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            case R.id.image_fast_forward:
                fastForward();
                return;
            case R.id.image_help:
                startActivity(HelpActivity.class);
                return;
            case R.id.image_mode:
                changePlayModel();
                return;
            case R.id.image_next:
                if (AudioVisualHelper.isPlaySingle()) {
                    return;
                }
                int size = ListUtil.getSize(this.mList) - 1;
                int i2 = this.mIndex;
                if (size <= i2) {
                    return;
                }
                int i3 = i2 + 1;
                this.mIndex = i3;
                AudioVisualHelper.setPlayListSelectChildIndex(i3);
                setPageTitle();
                return;
            case R.id.image_play:
                if (this.isPlaying) {
                    mPlayText.setText("Play");
                    this.imagePlay.setImageResource(R.drawable.icon_pause_54);
                    DLNAHelper.pause();
                    return;
                }
                if(!this.isPlaying){
                    mPlayText.setText("Pause");
                    this.imagePlay.setImageResource(R.drawable.icon_play_54);
                    DLNAHelper.play();
                    return;
                }

            case R.id.image_previous:
                if (AudioVisualHelper.isPlaySingle() || (i = this.mIndex) <= 0) {
                    return;
                }
                this.mIndex = i - 1;
                int size2 = ListUtil.getSize(this.mList);
                int i4 = this.mIndex;
                if (size2 > i4) {
                    AudioVisualHelper.setPlayListSelectChildIndex(i4);
                }
                setPageTitle();
                return;
            case R.id.image_queue:
                showPlayListDialog();
                return;
            case R.id.image_stop:
                stop();
            case R.id.image_volume_add:
                volumeAdd();
                return;
            case R.id.image_volume_reduce:
                volumeReduce();
                return;
            case R.id.image_exit:
                deleteFolder();
            default:
                return;
        }
    }

    private void showPlayListDialog() {
        if (this.mQueueDialog == null) {
            PlayQueueDialog newInstance = PlayQueueDialog.newInstance();
            this.mQueueDialog = newInstance;
            newInstance.setDismissCallback(new BaseDialogFragment.IDismissCallback() {
                @Override
                public final void onDismiss() {
                    AudioVideoCastActivity.this.resetIndex();
                }
            });
            this.mQueueDialog.setItemClickCallback(new PlayQueueDialog.ItemClickCallback() {
                @Override
                public final void onItemClick(int i) {
                    AudioVideoCastActivity.this.showPlayListDialog$3$AudioVideoCastActivity(i);
                }
            });
        }
        if (!this.mQueueDialog.isAdded()) {
            this.mQueueDialog.show(getSupportFragmentManager(), "QUEUE");
        }
    }

    public void showPlayListDialog$3$AudioVideoCastActivity(int i) {
        this.mIndex = i;
        setPageTitle();
    }

    public void resetIndex() {
        boolean z;
        FileModel value = AudioVisualHelper.mCastFileModel.getValue();
        if (value != null && ListUtil.getSize(this.mList) > 0) {
            for (int i = 0; i < this.mList.size(); i++) {
                if (this.mList.get(i).getPath().equals(value.getPath())) {
                    this.mIndex = i;
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (!z) {
            this.mIndex = 0;
        }
    }

    private void showCastScreenDialog() {
        if (DLNAHelper.isConnectDevice()) {
            String friendlyName = DLNAHelper.getConnectDevice().getDevice().getDetails().getFriendlyName();
            new NormalTipDialog.Builder().setCancel("CANCEL").setContinue("DISCONNECT", new View.OnClickListener() {
                @Override
                public final void onClick(View view) {
                    AudioVideoCastActivity.this.showCastScreenDialog$4$AudioVideoCastActivity(view);
                }
            }).setContent("Connected to " + friendlyName).build().show(getSupportFragmentManager(), "Disconnect");
            return;
        }
        ConnectDeviceDialog.newInstance(AudioVisualHelper.mCastFileModel.getValue()).show(getSupportFragmentManager(), "ConnectDevice");
    }

    public void showCastScreenDialog$4$AudioVideoCastActivity(View view) {
        DLNAHelper.disconnectDevice();
        //finish();
    }

    private void stop() {
        DLNAHelper.setVolume(5);
        DLNAHelper.stop();
        finish();
    }

    private void fastForward() {
        String charSequence = this.textPlayTime.getText().toString();
        L.i("AudioVideoCastActivity", "current: " + charSequence);
        DLNAHelper.seekFastForward(charSequence);
    }

    private void backOff() {
        String charSequence = this.textPlayTime.getText().toString();
        L.i("AudioVideoCastActivity", "current: " + charSequence);
        DLNAHelper.seekBackOff(charSequence);
    }

    private void volumeAdd() {
        try {
            this.isSetVolume = true;
            this.isAddVolume = true;
            DLNAHelper.getVolume();

        } catch (Exception unused) {
            L.e("AudioVideoCastActivity", "");
        }
    }

    private void volumeReduce() {
        try {
            this.isSetVolume = true;
            this.isAddVolume = false;
            DLNAHelper.getVolume();

        } catch (Exception unused) {
            L.e("AudioVideoCastActivity", "");
        }
    }

    private void setPlayTime() {
        DLNAHelper.mPlayPosition.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                AudioVideoCastActivity.this.setPlayTime$5$AudioVideoCastActivity((PositionInfo) obj);
            }
        });
    }

    public void setPlayTime$5$AudioVideoCastActivity(PositionInfo positionInfo) {
        L.i("AudioVideoCastActivity", "mPlayPosition");
        this.textPlayTime.setText(TimeUtil.formatStrTime(positionInfo.getRelTime()));
        this.textAllTime.setText(TimeUtil.formatStrTime(positionInfo.getTrackDuration()));
        this.totalTime = TimeUtil.string2Int(positionInfo.getTrackDuration());
        this.mSeekBar.setProgress(TimeUtil.string2Int(positionInfo.getRelTime()));
        this.mSeekBar.setMax(this.totalTime);
        startPlayRunnable();
    }

    private void setTransportState() {
        DLNAHelper.mTransportState.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                AudioVideoCastActivity.this.setTransportState$6$AudioVideoCastActivity((TransportState) obj);
            }
        });
    }

    public void setTransportState$6$AudioVideoCastActivity(TransportState transportState) {
        try {
            this.isShowPlayTipDialog = false;
            String value = transportState.getValue();
            L.i("AudioVideoCastActivity", "value: " + TransportState.valueOf(value));
            if (TransportState.valueOf(value) == TransportState.PLAYING) {
                this.isLoadNext = false;
                if (!this.isPlayNext && !AudioVisualHelper.isPlaySingle()) {
                    this.isPlayNext = true;
                }
                this.isPlaying = true;
                this.imagePlay.setImageResource(R.drawable.icon_pause_54);
            } else if (TransportState.valueOf(value) == TransportState.PAUSED_PLAYBACK) {
                this.isPlaying = false;
                this.imagePlay.setImageResource(R.drawable.icon_play_54);
                this.mHandler.removeCallbacks(this.mPlayTimeRunnable);
            } else if (TransportState.valueOf(value) == TransportState.TRANSITIONING) {
                this.isPlaying = false;
                this.mHandler.removeCallbacks(this.mPlayTimeRunnable);
            } else if (TransportState.valueOf(value) == TransportState.STOPPED) {
                if (this.isLoadNext) {
                    return;
                }
                if (!this.isPlayNext) {
                    DLNAHelper.stop();
                    this.imagePlay.setImageResource(R.drawable.icon_play_54);
                    this.isPlaying = false;
                    this.mHandler.removeCallbacks(this.mPlayTimeRunnable);
                } else if (this.mPlayLoadingDialog != null) {
                } else {
                    this.isLoadNext = true;
                    showPlayLoadingDialog();
                }
            } else if (TransportState.valueOf(value) != TransportState.NO_MEDIA_PRESENT) {
            } else {
             //   showPlayTipDialog();
            }
        } catch (Exception unused) {
            L.e("AudioVideoCastActivity", "");
        }
    }

    private void showPlayLoadingDialog() {
        if (this.mPlayLoadingDialog == null) {
            this.mHandler.removeCallbacks(this.mPlayCompleteRunnable);
            PlayLoadingDialog newInstance = PlayLoadingDialog.newInstance();
            this.mPlayLoadingDialog = newInstance;
            newInstance.show(getSupportFragmentManager(), "PlayLoading");
            this.mHandler.postDelayed(this.mPlayCompleteRunnable, 2000L);
        }
    }

    public void showPlayTipDialog() {
        try {
            if (this.mPlayWellTipDialog == null) {
                this.mPlayWellTipDialog = PlayTipDialog.newInstance();
            }
            if (this.mPlayWellTipDialog.isShowing()) {
                return;
            }
            this.mPlayWellTipDialog.show(getSupportFragmentManager(), "PlayWellTip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePlayModel() {
        int i = this.mCurrentPlayModel;
        if (i == 100) {
            this.mCurrentPlayModel = 101;
            this.imageChangePlayMode.setImageResource(R.mipmap.icon_video_mode_random);
        } else if (i == 101) {
            this.mCurrentPlayModel = 102;
            this.imageChangePlayMode.setImageResource(R.mipmap.icon_video_mode_single);
        } else {
            this.mCurrentPlayModel = 100;
            this.imageChangePlayMode.setImageResource(R.mipmap.icon_video_mode_loop);
        }
        SPUtils.put(this.mContext, SPContracts.PLAY_MODE, Integer.valueOf(this.mCurrentPlayModel));
    }

    private void playComplete() {
        Log.e("===", "playComplete");

        int i = this.mCurrentPlayModel;
        if (i == 102) {
            Log.e("===", "playComplete_1");

            AudioVisualHelper.setPlayListSelectChildIndex(this.mIndex);
            return;
        }
        if (i == 100) {
            Log.e("===", "playComplete_2");

            i = ListUtil.getSize(this.mList);
            int k = this.mIndex;
            if (i - 1 > k) {
                i = k + 1;
                this.mIndex = i;
                AudioVisualHelper.setPlayListSelectChildIndex(i);
                setPageTitle();
                return;
            }
            DLNAHelper.stop();


            return;
        }
        if (ListUtil.getSize(this.mList) <= 0)
            return;
        this.mIndex = (new Random()).nextInt(this.mList.size());
        i = ListUtil.getSize(this.mList);
        int j = this.mIndex;
        if (i > j) {
            Log.e("===", "playComplete_3");

            AudioVisualHelper.setPlayListSelectChildIndex(j);
            setPageTitle();
        }
    }

    public void runPlayTime() {
        if (this.isPlaying) {
            int string2Int = TimeUtil.string2Int(this.textPlayTime.getText().toString());
            if (string2Int < this.totalTime) {
                int i = string2Int + 1;
                this.textPlayTime.setText(TimeUtil.formatStrTime(TimeUtil.int2String(i)));
                this.mSeekBar.setProgress(i);
                startPlayRunnable();
                return;
            }
            this.mHandler.removeCallbacks(this.mPlayTimeRunnable);
        }
    }

    private void startPlayRunnable() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mPlayTimeRunnable);
            this.mHandler.postDelayed(this.mPlayTimeRunnable, 1000L);
        }
    }

    @Subscribe
    public void handlerRewardEvent(RewardDialogEvent rewardDialogEvent) {
        if (rewardDialogEvent.mViewType == 3) {
            showRewardDialog();
        }
    }

    @Override
    public void onDestroy() {
        ReportUtil.stayAVDuration(this.mOpenPageTime);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        this.isSetVolume = false;
        this.isPlayNext = false;
        this.isLoadNext = false;
        Handler handler = this.mHandler;
        if (handler != null) {
            Runnable runnable = this.mPlayTimeRunnable;
            if (runnable != null) {
                handler.removeCallbacks(runnable);
                this.mPlayTimeRunnable = null;
            }
            Runnable runnable2 = this.mPlayCompleteRunnable;
            if (runnable2 != null) {
                this.mHandler.removeCallbacks(runnable2);
                this.mPlayCompleteRunnable = null;
            }
            Runnable runnable3 = this.mHideVolumeRunnable;
            if (runnable3 != null) {
                this.mHandler.removeCallbacks(runnable3);
                this.mHideVolumeRunnable = null;
            }
            Runnable runnable4 = this.mShowPlayTipDialogRunnable;
            if (runnable4 != null) {
                this.mHandler.removeCallbacks(runnable4);
                this.mShowPlayTipDialogRunnable = null;
            }
            this.mHandler = null;
        }
        PlayLoadingDialog playLoadingDialog = this.mPlayLoadingDialog;
        if (playLoadingDialog != null && playLoadingDialog.isVisible()) {
            this.mPlayLoadingDialog.dismiss();
            this.mPlayLoadingDialog = null;
        }
        if (this.mQueueDialog != null) {
            this.mQueueDialog = null;
        }
        DLNAHelper.mTransportState.removeObservers(this);
        DLNAHelper.mDeviceVolume.removeObservers(this);
        DLNAHelper.mPlayPosition.removeObservers(this);
        DLNAHelper.mConnectStatus.removeObservers(this);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        L.i("SeekBar", "onStopTrackingTouch");
        int progress = seekBar.getProgress();
        this.textPlayTime.setText(TimeUtil.formatStrTime(TimeUtil.int2String(progress)));
        DLNAHelper.seekTo(progress);
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    private void clickBack() {
        deleteFolder();
        finish();
    }

    private void deleteFolder() {
        try {
            if (!DLNAHelper.isConnectDevice()) {
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CastFolder");
                if(dir.exists()) {
                    deleteFiles(dir);
                }
//                File videodir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");
//                if(videodir.exists()) {
//                    deleteFiles(videodir);
//                }
//                LayoutInflater inflater = LayoutInflater.from(AudioVideoCastActivity.this);
//                View viewDialog = inflater.inflate(R.layout.exit_dialog, null);
//                Dialog exitDialog = new Dialog(AudioVideoCastActivity.this, R.style.CustomAlertDialog);
//                exitDialog.setContentView(viewDialog);
//                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//                layoutParams.copyFrom(exitDialog.getWindow().getAttributes());
//                layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
//                layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.20);
//
//                TextView noTextView = viewDialog.findViewById(R.id.noTextView);
//                TextView exitTextView = viewDialog.findViewById(R.id.exitTextView);
//
//
//                TextView msg = viewDialog.findViewById(R.id.exitMsg);
//                //   msg.setText("Thanks for using our App, Visit Again");
//
//                noTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        finish();
//                    }
//                });
//
//
//                exitTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        finishAffinity();
//                        finish();
//                    }
//                });
//                exitDialog.setCancelable(true);
//                exitDialog.setCanceledOnTouchOutside(false);
//                exitDialog.getWindow().setAttributes(layoutParams);
//                exitDialog.show();


            }
//


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFiles(File file) {
        File file2 = new File(file.getPath());
        if (file2.exists()) {
            file2.delete();
        }
    }






}
