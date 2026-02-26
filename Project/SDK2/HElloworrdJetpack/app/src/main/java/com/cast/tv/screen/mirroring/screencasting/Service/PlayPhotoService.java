package com.cast.tv.screen.mirroring.screencasting.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.PlayIndex;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PlayPhotoService extends Service {
    private final int mPlayDelayTime = 4200;
    private final MyBinder mBinder = new MyBinder();
    public MutableLiveData<PlayIndex> mIndex;
    private Handler mHandler;
    private List<FileModel> mListData;
    private boolean isPlay = false;
    private Context mContext;

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }


    private Runnable mPlayRunnable = new Runnable() {
        @Override
        public void run() {
            if (PlayPhotoService.this.mIndex == null || PlayPhotoService.this.mIndex.getValue() == null) {
                PlayPhotoService.this.stop();
                return;
            }
            int i = PlayPhotoService.this.mIndex.getValue().index + 1;
            if (i < ListUtil.getSize(PlayPhotoService.this.mListData)) {
                PlayPhotoService.this.mIndex.setValue(new PlayIndex(i));
                AudioVisualHelper.setPlayListSelectChildIndex(i);
                PlayPhotoService.this.mHandler.postDelayed(PlayPhotoService.this.mPlayRunnable, 4200L);
                return;
            }
            PlayPhotoService.this.stop();
        }
    };


    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }


    }
    public void start() {
        this.mListData = AudioVisualHelper.getAudioVisualPlayList();
        this.mIndex.setValue(new PlayIndex(AudioVisualHelper.getSelectChildIndex()));
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        this.mHandler.postDelayed(this.mPlayRunnable, 4200L);
        this.isPlay = true;
        try{
            if(PlayPhotoService.this.mListData.get(PlayPhotoService.this.mIndex.getValue().index).getPath().contains(".png") || PlayPhotoService.this.mListData.get(PlayPhotoService.this.mIndex.getValue().index).getPath().contains(".jpg")
                    ||PlayPhotoService.this.mListData.get(PlayPhotoService.this.mIndex.getValue().index).getPath().contains(".jpeg")){
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CastFolder");
                if(dir.exists()) {
                    deleteFiles(dir);
                }
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "/CastImageFile" + timeStamp;
                String imagefile = dir +imageFileName+ ".jpg" ;
                File createDir = new File( imagefile);
                createDir.createNewFile();
                if(!createDir.exists()) {
                    createDir.mkdir();
                }
                copyFile(new File(PlayPhotoService.this.mListData.get(PlayPhotoService.this.mIndex.getValue().index).getPath()),new File(imagefile));
                if(new File(imagefile).exists()){
                    SharedPreferences imageSharedPreferences = mContext.getSharedPreferences("ImageCastSharedPreference", MODE_PRIVATE);
                    SharedPreferences.Editor imageEditor =imageSharedPreferences.edit();
                    imageEditor.putString("ImagePath", imagefile);
                    imageEditor.putString("ImageName", PlayPhotoService.this.mListData.get(PlayPhotoService.this.mIndex.getValue().index).getDisplayName());
                    imageEditor.commit();
                }else{
                    Toast.makeText(mContext, "File not exists", Toast.LENGTH_SHORT).show();
                }


            }

            if(PlayPhotoService.this.mListData.get(PlayPhotoService.this.mIndex.getValue().index).getPath().contains(".mp4")){
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("VideoCastSharedPreference", MODE_PRIVATE);
                SharedPreferences.Editor videoEditor = sharedPreferences.edit();
                videoEditor.putString("VideoPath", PlayPhotoService.this.mListData.get(PlayPhotoService.this.mIndex.getValue().index).getPath());
                videoEditor.commit();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteFiles(File file) {
        File file2 = new File(file.getPath());
        if (file2.exists()) {
            file2.delete();
        }
    }
    public void stop() {
        this.mHandler.removeCallbacks(this.mPlayRunnable);
        this.isPlay = false;

    }


    public boolean isPlay() {
        return this.isPlay;
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.mIndex = new MutableLiveData<>();
        return this.mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mPlayRunnable);
            this.mPlayRunnable = null;
            this.mHandler = null;

        }


    }

    public class MyBinder extends Binder {
        public MyBinder() {
        }

        public PlayPhotoService getService() {
            return PlayPhotoService.this;
        }
    }


}
