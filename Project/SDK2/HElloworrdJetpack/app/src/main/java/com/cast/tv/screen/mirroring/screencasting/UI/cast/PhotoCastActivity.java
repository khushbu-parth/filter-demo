package com.cast.tv.screen.mirroring.screencasting.UI.cast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.CastApp;
import com.cast.tv.screen.mirroring.screencasting.Dialog.ConnectDeviceDialog;
import com.cast.tv.screen.mirroring.screencasting.Dialog.NormalTipDialog;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.PlayIndex;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Report.ReportUtil;
import com.cast.tv.screen.mirroring.screencasting.Service.PlayPhotoService;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.PhotoCastAdapter;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.PhotoViewPagerAdapter;
import com.cast.tv.screen.mirroring.screencasting.UI.help.HelpActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.BitmapUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class PhotoCastActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "PhotoCastActivity";
    private boolean isPlay;
    private boolean isSinglePlay;
    private PhotoCastAdapter mAdapter;
    private boolean mBound;
    private ImageView mImagePlay;
    private int mIndex;
    private List<FileModel> mList;
    private long mOpenPageTime;
    private PhotoViewPagerAdapter mPhotoViewPagerAdapter;
    private int mPlayIndex;
    private RecyclerView mRvPhoto;
    private PlayPhotoService mService;
    private TextView mTextTitle;
    private ViewPager mViewPager;
    private ServiceConnection mConnection = new AnonymousClass2();

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_cast;
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
        ReportUtil.loadControlPPage();
        this.mOpenPageTime = System.currentTimeMillis();
        this.isSinglePlay = AudioVisualHelper.isPlaySingle();
        findViewById(R.id.image_back).setOnClickListener(this);
        findViewById(R.id.image_cast_screen).setOnClickListener(this);
        findViewById(R.id.image_help).setOnClickListener(this);
        findViewById(R.id.image_rotate).setOnClickListener(this);
        this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mTextTitle = (TextView) findViewById(R.id.text_title);
        this.mRvPhoto = (RecyclerView) findViewById(R.id.recycler_view);
        setPhotoView();
        setBomView();
        bindPlayPhotoService();
        // showEvaluateGuide();
        if (MaxRewardUtil.isShowRewardDialog()) {
            //showRewardDialog();
        }
    }

    private void showRewardDialog() {
        //ShowRewardAdDialog.newInstance().show(getSupportFragmentManager(), "Reward");
    }

    private void setPhotoView() {
        String audioVisualCurrentDisplayName;
        if (this.isSinglePlay) {
            FileModel value = AudioVisualHelper.mCastFileModel.getValue();
            if (value == null) {
                finish();
                return;
            }
            audioVisualCurrentDisplayName = value.getDisplayName();
            ArrayList arrayList = new ArrayList();
            this.mList = arrayList;
            arrayList.add(value);
        } else {
            this.mList = AudioVisualHelper.getAudioVisualPlayList();
            audioVisualCurrentDisplayName = AudioVisualHelper.getAudioVisualCurrentDisplayName();
            resetList(this.mList);
        }
        this.mIndex = AudioVisualHelper.getSelectChildIndex();
        setRecyclerView(audioVisualCurrentDisplayName);
        setViewPager();
    }

    private void resetList(List<FileModel> list) {
        if (ListUtil.getSize(list) > 0) {
            for (FileModel fileModel : list) {
                fileModel.setSelect(false);
            }
        }
    }

    private void setViewPager() {
        ViewPager viewPager = this.mViewPager;
        PhotoViewPagerAdapter photoViewPagerAdapter = new PhotoViewPagerAdapter(this.mList);
        this.mPhotoViewPagerAdapter = photoViewPagerAdapter;
        viewPager.setAdapter(photoViewPagerAdapter);
        this.mViewPager.setCurrentItem(this.mIndex);
        this.mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                ((FileModel) PhotoCastActivity.this.mList.get(PhotoCastActivity.this.mIndex)).setSelect(false);
                ((FileModel) PhotoCastActivity.this.mList.get(i)).setSelect(true);
                PhotoCastActivity.this.mAdapter.notifyItemChanged(PhotoCastActivity.this.mIndex);
                PhotoCastActivity.this.mAdapter.notifyItemChanged(i);
                PhotoCastActivity.this.mIndex = i;
                PhotoCastActivity.this.mRvPhoto.scrollToPosition(PhotoCastActivity.this.mIndex);
                if (!PhotoCastActivity.this.isPlay) {
                    AudioVisualHelper.setPlayListSelectChildIndex(PhotoCastActivity.this.mIndex);
                }
            }
        });
    }

    private void setRecyclerView(String str) {
        this.mTextTitle.setText(str);
        List<FileModel> list = this.mList;
        if (list == null || list.size() <= this.mIndex) {
            return;
        }
        this.mRvPhoto.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.HORIZONTAL, false));
        this.mList.get(this.mIndex).setSelect(true);
        PhotoCastAdapter photoCastAdapter = new PhotoCastAdapter(this.mList);
        this.mAdapter = photoCastAdapter;
        this.mRvPhoto.setAdapter(photoCastAdapter);
        this.mRvPhoto.scrollToPosition(this.mIndex);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                PhotoCastActivity.this.lambda$setRecyclerView$0$PhotoCastActivity(baseQuickAdapter, view, i);
            }
        });
    }

    public void lambda$setRecyclerView$0$PhotoCastActivity(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        setCurrentItem(i);
    }

    private void setBomView() {
        findViewById(R.id.image_previous).setOnClickListener(this);
        findViewById(R.id.image_next).setOnClickListener(this);
        ImageView imageView = (ImageView) findViewById(R.id.image_play);
        this.mImagePlay = imageView;
        imageView.setOnClickListener(this);
    }

    private void bindPlayPhotoService() {
        if (this.isSinglePlay || this.mBound) {
            return;
        }
        DLNAHelper.startPlayPhotoService();
        bindService(new Intent(this, PlayPhotoService.class), this.mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                clickBack();
                return;
            case R.id.image_cast_screen:
                showConnectDeviceDialog();
                return;
            case R.id.image_help:
                startActivity(HelpActivity.class);
                return;
            case R.id.image_next:
                next();
                return;
            case R.id.image_play:
                play();
                return;
            case R.id.image_previous:
                previous();
                return;
            case R.id.image_rotate:
                rotatePhoto();
                return;
            default:
                return;
        }
    }

    private void previous() {
        int i = this.mIndex;
        this.mPlayIndex = i;
        if (i > 0) {
            int i2 = i - 1;
            this.mPlayIndex = i2;
            setCurrentItem(i2);
        }
    }

    private void next() {
        int i = this.mIndex;
        this.mPlayIndex = i;
        if (i + 1 < this.mList.size()) {
            int i2 = this.mPlayIndex + 1;
            this.mPlayIndex = i2;
            setCurrentItem(i2);
        }
    }

    private void play() {
        if (this.isSinglePlay) {
            return;
        }
        if (this.isPlay) {
            this.mImagePlay.setImageResource(R.drawable.icon_play_54);
            PlayPhotoService playPhotoService = this.mService;
            if (playPhotoService != null) {
                playPhotoService.stop();
            }
        } else {
            this.mPlayIndex = this.mIndex;
            this.mImagePlay.setImageResource(R.drawable.icon_pause_54);
            PlayPhotoService playPhotoService2 = this.mService;
            if (playPhotoService2 != null) {
                playPhotoService2.start();
            }
        }
        this.isPlay = !this.isPlay;
    }

    private void showConnectDeviceDialog() {
        if (DLNAHelper.isConnectDevice()) {
            String friendlyName = DLNAHelper.getConnectDevice().getDevice().getDetails().getFriendlyName();
            new NormalTipDialog.Builder().setCancel("CANCEL").setContinue("DISCONNECT", new View.OnClickListener() {
                @Override
                public final void onClick(View view) {
                    PhotoCastActivity.this.lambda$showConnectDeviceDialog$1$PhotoCastActivity(view);
                }
            }).setContent("Connected to " + friendlyName).build().show(getSupportFragmentManager(), "Disconnect");
            return;
        }
        ConnectDeviceDialog.newInstance(AudioVisualHelper.mCastFileModel.getValue()).show(getSupportFragmentManager(), "ConnectDevice");
    }

    public void lambda$showConnectDeviceDialog$1$PhotoCastActivity(View view) {
        DLNAHelper.disconnectDevice();
        this.isPlay = false;
        finish();
    }

    private void setCurrentItem(int i) {
        if (this.isPlay) {
            PlayPhotoService playPhotoService = this.mService;
            if (playPhotoService != null) {
                playPhotoService.stop();
            }
            this.isPlay = false;
            this.mImagePlay.setImageResource(R.drawable.icon_play_54);
        }
        this.mViewPager.setCurrentItem(i);
    }

    private void rotatePhoto() {
        FileModel fileModel;
        String tempPath;
        int size = ListUtil.getSize(this.mList);
        int i = this.mIndex;
        if (size <= i || (tempPath = (fileModel = this.mList.get(i)).getTempPath()) == null || TextUtils.isEmpty(tempPath)) {
            return;
        }
        L.i("PhotoCastActivity", "path: " + tempPath);
        fileModel.setTempPath(BitmapUtil.rotateImage(tempPath));
        this.mPhotoViewPagerAdapter.notifyDataSetChanged();
        DLNAHelper.startPlay(fileModel);
        if (!this.isPlay) {
            return;
        }
        PlayPhotoService playPhotoService = this.mService;
        if (playPhotoService != null) {
            playPhotoService.stop();
        }
        this.isPlay = false;
        this.mImagePlay.setImageResource(R.drawable.icon_play_54);
    }

    private void showEvaluateGuide() {
//        if (SubscribeUtil.isAppearRate() && CastApp.mIntoRCNum == 2) {
//            EvaluateDialog newInstance = EvaluateDialog.newInstance();
//            newInstance.show(getSupportFragmentManager(), "Evaluate");
//            newInstance.setCallback(new EvaluateCallback() {
//                @Override
//                public final void showRateGuideDialog() {
//                    PhotoCastActivity.this.lambda$showEvaluateGuide$2$PhotoCastActivity();
//                }
//            });
//        }
        CastApp.mIntoRCNum++;
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    private void clickBack() {
        finish();
    }

    @Subscribe
    public void handlerRewardEvent(RewardDialogEvent rewardDialogEvent) {
        if (rewardDialogEvent.mViewType == 3) {
            showRewardDialog();
            if (!this.isPlay) {
                return;
            }
            PlayPhotoService playPhotoService = this.mService;
            if (playPhotoService != null) {
                playPhotoService.stop();
            }
            this.isPlay = false;
            this.mImagePlay.setImageResource(R.drawable.icon_play_54);
        }
    }

    @Override
    public void onDestroy() {
        ServiceConnection serviceConnection;
        ReportUtil.stayPDuration(this.mOpenPageTime);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        PlayPhotoService playPhotoService = this.mService;
        if (playPhotoService != null && !playPhotoService.isPlay()) {
            DLNAHelper.stopPlayPhotoService();
        }
        if (!this.mBound || (serviceConnection = this.mConnection) == null) {
            return;
        }
        unbindService(serviceConnection);
        this.mConnection = null;
    }

    public class AnonymousClass2 implements ServiceConnection {
        AnonymousClass2() {
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("PhotoCastActivity", "onServiceConnected");
            PhotoCastActivity.this.mService = ((PlayPhotoService.MyBinder) iBinder).getService();
            if (PhotoCastActivity.this.mService == null) {
                return;
            }
            PhotoCastActivity photoCastActivity = PhotoCastActivity.this;
            photoCastActivity.isPlay = photoCastActivity.mService.isPlay();
            if (PhotoCastActivity.this.isPlay) {
                PhotoCastActivity.this.mImagePlay.setImageResource(R.drawable.icon_pause_54);
            } else {
                PhotoCastActivity.this.mImagePlay.setImageResource(R.drawable.icon_play_54);
            }
            PhotoCastActivity.this.mService.mIndex.observe(PhotoCastActivity.this, new Observer() {
                @Override
                public final void onChanged(Object obj) {
                    AnonymousClass2.this.lambda$onServiceConnected$0$PhotoCastActivity$2((PlayIndex) obj);
                }
            });
            PhotoCastActivity.this.mBound = true;
        }

        public void lambda$onServiceConnected$0$PhotoCastActivity$2(PlayIndex playIndex) {
            PhotoCastActivity.this.mPlayIndex = playIndex.index;
            PhotoCastActivity.this.mViewPager.setCurrentItem(PhotoCastActivity.this.mPlayIndex);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("PhotoCastActivity", "onServiceDisconnected");
            if (PhotoCastActivity.this.mService != null) {
                PhotoCastActivity.this.mService.mIndex.removeObservers(PhotoCastActivity.this);
                PhotoCastActivity.this.mService = null;
            }
            PhotoCastActivity.this.mBound = false;
        }
    }
}
