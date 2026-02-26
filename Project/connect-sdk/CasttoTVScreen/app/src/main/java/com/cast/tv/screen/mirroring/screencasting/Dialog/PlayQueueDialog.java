package com.cast.tv.screen.mirroring.screencasting.Dialog;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.Callback.EmptyQueueCallback;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.PlayQueueAdapter;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.AudioVideoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.PhotoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlayQueueDialog extends BaseDialogFragment {
    public PlayQueueAdapter mAdapter;
    private ItemClickCallback mItemClickCallback;
    private List<FileModel> mList;
    private TextView textCount;
    private String imagefile;

    public static PlayQueueDialog newInstance() {
        return new PlayQueueDialog();
    }

    @Override
    protected int setDialogGravity() {
        return 80;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_play_queue;
    }

    @Override
    public void onStart() {
        Window window;
        super.onStart();
        if (getDialog() == null || (window = getDialog().getWindow()) == null) {
            return;
        }
        window.setWindowAnimations(R.style.dialogWindowAnim);
    }

    @Override
    protected int setDialogWidth() {
        return ScreenUtil.getScreenWidth(this.mContext);
    }

    @Override
    protected int setDialogHeight() {
        return ScreenUtil.getScreenHeight(this.mContext) - ScreenUtil.dip2px(this.mContext, 200.0f);
    }

    @Override
    protected void initView(View view) {
        this.mList = AudioVisualHelper.getAudioVisualPlayList();
        setCurrentSelect();
        this.textCount = (TextView) view.findViewById(R.id.text_count);
        if (ListUtil.getSize(this.mList) <= 0) {
            this.textCount.setText("0");
            return;
        }
        this.textCount.setText(String.valueOf(this.mList.size()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        PlayQueueAdapter playQueueAdapter = new PlayQueueAdapter(this.mList);
        this.mAdapter = playQueueAdapter;
        recyclerView.setAdapter(playQueueAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i) {
            PlayQueueDialog.this.initViewPlayQueueDialog(baseQuickAdapter, view2, i);

            }
        });

        this.mAdapter.setDeleteItemCallback(new PlayQueueAdapter.IDeleteItemCallback() {
            @Override
            public void onDeleteItem(int i) {
                mList.remove(i);
                mAdapter.notifyDataSetChanged();
               textCount.setText(String.valueOf(mList.size()));
            }
        });
        setCurrentFile();
    }

    private void showCasting(int i) {
        FileModel item = this.mAdapter.getItem(i);
        try{
            if(item.getPath().contains(".png")){
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CastFolder");

                if(!dir.exists()) {
                    dir.mkdirs();
                }
                // final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CastFolder/";
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "/CastImageFile" + timeStamp;
                imagefile = dir +imageFileName+ ".jpg" ;
                File createDir = new File( imagefile);
                createDir.createNewFile();
                if(!createDir.exists()) {
                    createDir.mkdir();
                }
                copyFile(new File(item.getPath()),new File(imagefile));
                if(new File(imagefile).exists()){
                    SharedPreferences imageSharedPreferences = getActivity().getSharedPreferences("ImageCastSharedPreference", MODE_PRIVATE);
                    SharedPreferences.Editor imageEditor =imageSharedPreferences.edit();
                    imageEditor.putString("ImagePath", imagefile);
                    imageEditor.commit();
                }else{
                    Toast.makeText(mContext, "File not exists", Toast.LENGTH_SHORT).show();
                }


            }

            if(item.getPath().contains(".wav")){
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AudioCastSharedPreference", MODE_PRIVATE);
                SharedPreferences.Editor audioEditor = sharedPreferences.edit();
                audioEditor.putString("AudioPath", item.getPath());
                audioEditor.commit();
            } else if(item.getPath().contains(".mp3") || item.getPath().contains(".aac")|| item.getPath().contains(".m4p")||item.getPath().contains(".webm")){
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "/CastAudioFile" + timeStamp;
                imagefile = dir +imageFileName+ ".wav" ;
                File createDir = new File( imagefile);
                createDir.createNewFile();
                if(!createDir.exists()) {
                    createDir.mkdir();
                }
                copyFile(new File(item.getPath()),new File(imagefile));
                if(new File(imagefile).exists()){
                    SharedPreferences imageSharedPreferences = getActivity().getSharedPreferences("AudioCastSharedPreference", MODE_PRIVATE);
                    SharedPreferences.Editor imageEditor =imageSharedPreferences.edit();
                    imageEditor.putString("AudioPath", imagefile);
                    imageEditor.commit();
                }else{
                    Toast.makeText(mContext, "File not exists", Toast.LENGTH_SHORT).show();
                }


            }
            if(item.getPath().contains(".mp4")){
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("VideoCastSharedPreference", MODE_PRIVATE);
                SharedPreferences.Editor videoEditor = sharedPreferences.edit();
                videoEditor.putString("VideoPath", item.getPath());
                videoEditor.commit();
            } else if(item.getPath().contains(".3gp") || item.getPath().contains(".mkv")|| item.getPath().contains(".webm")||item.getPath().contains(".mov")){
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");

                if(!dir.exists()) {
                    dir.mkdirs();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "/CastVideoFile" + timeStamp;
                imagefile = dir +imageFileName+ ".mp4" ;
                File createDir = new File( imagefile);
                createDir.createNewFile();
                if(!createDir.exists()) {
                    createDir.mkdir();
                }
                copyFile(new File(item.getPath()),new File(imagefile));
                if(new File(imagefile).exists()){
                    SharedPreferences imageSharedPreferences = getActivity().getSharedPreferences("VideoCastSharedPreference", MODE_PRIVATE);
                    SharedPreferences.Editor imageEditor =imageSharedPreferences.edit();
                    imageEditor.putString("VideoPath", imagefile);
                    imageEditor.commit();
                }else{
                    Toast.makeText(mContext, "File not exists", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            e.printStackTrace();
        }


        if (DLNAHelper.isConnectDevice()) {
            if (MaxRewardUtil.obtainCastFileNum() <= 0) {
                RewardDialogEvent.post(1);
                return;
            } else if (item.getFileType() == 272) {
                startActivity(new Intent(this.mContext, PhotoCastActivity.class));
            } else {
                startActivity(new Intent(this.mContext, AudioVideoCastActivity.class));
            }
        } else {
            ConnectDeviceDialog.newInstance(item).show(getChildFragmentManager(), "ConnectDevice");

        }

        AudioVisualHelper.setSelectChildIndex(i);
    }
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

    public void initViewPlayQueueDialog(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        FileModel fileModel = this.mList.get(i);
        showCasting(i);
        if (fileModel.isSelect()) {
            return;
        }
        AudioVisualHelper.mCastFileModel.setValue(fileModel);
        DLNAHelper.startPlay(fileModel);
        ItemClickCallback itemClickCallback = this.mItemClickCallback;
        if (itemClickCallback == null) {
            return;
        }
        itemClickCallback.onItemClick(i);
    }

    public void initViewPlayQueueDialog1(int i) {
        this.mAdapter.removeAt(i);
        this.textCount.setText(String.valueOf(this.mList.size()));
    }

    private void setCurrentFile() {
        AudioVisualHelper.mCastFileModel.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                PlayQueueDialog.this.setCurrentFilePlayQueueDialog((FileModel) obj);
            }
        });
    }

    public void setCurrentFilePlayQueueDialog(FileModel fileModel) {
        setCurrentSelect();
    }

    public void setItemClickCallback(ItemClickCallback itemClickCallback) {
        this.mItemClickCallback = itemClickCallback;
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                PlayQueueDialog.this.initListenerPlayQueueDialog(view2);
            }
        });
        view.findViewById(R.id.image_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                PlayQueueDialog.this.initListenerPlayQueueDialog1(view2);
            }
        });
    }

    public void initListenerPlayQueueDialog(View view) {
        dismiss();
    }

    public void initListenerPlayQueueDialog1(View view) {
        if (ListUtil.getSize(this.mList) <= 0) {
            return;
        }
        ClearEmptyQueueTipDialog newInstance = ClearEmptyQueueTipDialog.newInstance();
        newInstance.show(getChildFragmentManager(), "EmptyTip");
        newInstance.setCallback(new EmptyQueueCallback() {
            @Override
            public final void clearQueue() {
                PlayQueueDialog.this.PlayQueueDialog();
            }
        });
    }

    public void PlayQueueDialog() {
        this.mList.clear();
        this.textCount.setText("0");
        PlayQueueAdapter playQueueAdapter = this.mAdapter;
        if (playQueueAdapter != null) {
            playQueueAdapter.setList(this.mList);
        }
    }

    private void setCurrentSelect() {
        FileModel value;
        if (ListUtil.getSize(this.mList) <= 0 || (value = AudioVisualHelper.mCastFileModel.getValue()) == null) {
            return;
        }
        for (FileModel fileModel : this.mList) {
            fileModel.setSelect(fileModel.getPath().equals(value.getPath()));
        }
        PlayQueueAdapter playQueueAdapter = this.mAdapter;
        if (playQueueAdapter == null) {
            return;
        }
        playQueueAdapter.setList(this.mList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AudioVisualHelper.mCastFileModel.removeObservers(this);
    }

    public interface ItemClickCallback {
        void onItemClick(int i);
    }
}
