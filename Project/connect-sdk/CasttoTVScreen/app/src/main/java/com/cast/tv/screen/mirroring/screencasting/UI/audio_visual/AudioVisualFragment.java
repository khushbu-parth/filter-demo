package com.cast.tv.screen.mirroring.screencasting.UI.audio_visual;

import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cast.tv.screen.mirroring.screencasting.Base.BaseFragment;
import com.cast.tv.screen.mirroring.screencasting.Contract.Contracts;
import com.cast.tv.screen.mirroring.screencasting.Contract.IntentContracts;
import com.cast.tv.screen.mirroring.screencasting.Dialog.ConnectDeviceDialog;
import com.cast.tv.screen.mirroring.screencasting.Dialog.NormalTipDialog;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.ConnectStatus;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.Observer.SimpleObserver;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Report.ReportUtil;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.AudioAdapter;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.AudioListAdapter;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.PhotoAdapter;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.PhotoListAdapter;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.VideoAdapter;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.VideoListAdapter;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.AudioVideoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.PhotoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.help.HelpActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.main.MainActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.search.SearchActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.library.info.CastTvAppManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AudioVisualFragment extends BaseFragment {
    private BaseQuickAdapter<FileModel, BaseViewHolder> mAdapter;
    private FrameLayout mBannerContainer;
    private ImageView mImageDirectories;
    private ImageView mIvHelp;
    private ImageView mIvScreen;
    private ImageView mIvSearch;
    private int mPageType;
    private RecyclerView mRv;
    private TextView mTvTitle;
    private View viewDirectories;
    private View viewEmpty;
    private String castNameDisplay,castType;

    public static void AudioVisualFragment_startActivity(AudioVisualFragment p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    public static AudioVisualFragment newInstance(int i) {
        AudioVisualFragment audioVisualFragment = new AudioVisualFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentContracts.INTENT_OPEN_PAGE_TYPE, i);
        audioVisualFragment.setArguments(bundle);
        return audioVisualFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_audio_visual;
    }

    @Override
    public void setViewData(View view) {

        CastTvAppManager.getInstance(requireActivity()).showBannerAds(requireActivity(), view.findViewById(R.id.fl_banner));
        CastTvAppManager.getInstance(requireActivity()).showNativeAds(requireActivity(), view.findViewById(R.id.fl_native_banner), view.findViewById(R.id.native_space_img), 2);

        this.mTvTitle = (TextView) view.findViewById(R.id.text_title);
        this.mIvSearch = (ImageView) view.findViewById(R.id.image_search);
        this.mIvHelp = (ImageView) view.findViewById(R.id.image_help);
        this.mIvScreen = (ImageView) view.findViewById(R.id.image_cast_screen);
        this.viewDirectories = view.findViewById(R.id.view_directories);
        this.viewEmpty = view.findViewById(R.id.view_empty);
        this.mBannerContainer = (FrameLayout) view.findViewById(R.id.mBannerContainer);
        this.mImageDirectories = (ImageView) view.findViewById(R.id.image_directories);
        this.mRv = (RecyclerView) view.findViewById(R.id.recycler_view);
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    setRecyclerView();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void setRecyclerView() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        int i = arguments.getInt(IntentContracts.INTENT_OPEN_PAGE_TYPE);
        this.mPageType = i;
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    setRecyclerViewAdapter(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        ImageView imageView = this.mIvSearch;
        int i2 = this.mPageType;
        imageView.setVisibility((i2 == 773 || i2 == 772) ? View.GONE : View.VISIBLE);
    }

    private void setRecyclerViewAdapter(int i) {
        if (i == 773) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setPhotoAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

        } else if (i == 772) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        setPhotoListAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else if (i == 768) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setVideoAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else if (i == 769) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setVideoListAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else if (i == 770) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setAudioAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setAudioListAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    @Override
    public void setClickEvent(View view) {
        view.findViewById(R.id.view_root).setOnClickListener(null);
        view.findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {

                AudioVisualFragment.this.setClickEvent$0$AudioVisualFragment(view2);
            }
        });
        this.viewDirectories.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {

                AudioVisualFragment.this.setClickEvent$1$AudioVisualFragment(view2);
            }
        });
        this.mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {

                AudioVisualFragment.this.setClickEvent$2$AudioVisualFragment(view2);
            }
        });
        this.mIvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {

                AudioVisualFragment.this.setClickEvent$3$AudioVisualFragment(view2);
            }
        });
        this.mIvScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                try {
                    AudioVisualFragment.this.setClickEvent$5$AudioVisualFragment(view2);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void setClickEvent$0$AudioVisualFragment(View view) {
        CastTvAppManager.getInstance(requireActivity()).showInterstitialBackAd(requireActivity(), () -> {
            startActivity(new Intent(getActivity(), MainActivity.class));

        });

    }

    public void setClickEvent$1$AudioVisualFragment(View view) {
        ((MainActivity) this.mActivity).openDirectories(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public void setClickEvent$2$AudioVisualFragment(View view) {
        startSearch();
    }

    public void setClickEvent$3$AudioVisualFragment(View view) {
        AudioVisualFragment_startActivity(this, new Intent(this.mActivity, HelpActivity.class));
    }

    public void setClickEvent$5$AudioVisualFragment(View view) {
        try {
            if (DLNAHelper.isConnectDevice()) {
                String friendlyName = DLNAHelper.getConnectDevice().getDevice().getDetails().getFriendlyName();
                new NormalTipDialog.Builder().setCancel("CANCEL").setContinue("DISCONNECT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DLNAHelper.disconnectDevice();
                    }
                }).setContent("Connected to " + friendlyName).build().show(getChildFragmentManager(), "Disconnect");
                return;
            }

            ConnectDeviceDialog.newInstance(null).show(getChildFragmentManager(), "ConnectDevice");
        } catch (Exception e) {
            e.printStackTrace();
            //  Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void startSearch() {
        Intent intent = new Intent(this.mActivity, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(IntentContracts.INTENT_SEARCH_TYPE, this.mPageType);
        intent.putExtras(bundle);
        AudioVisualFragment_startActivity(this, intent);
    }

    private void setPhotoAdapter() {
        ReportUtil.loadPhotoPage();
        this.viewDirectories.setVisibility(View.GONE);
        this.viewEmpty.setVisibility(View.GONE);
        this.mTvTitle.setText(R.string.Photo);
        this.mRv.setLayoutManager(new LinearLayoutManager(this.mActivity));
        PhotoAdapter photoAdapter = new PhotoAdapter(new ArrayList());
        this.mAdapter = photoAdapter;
        this.mRv.setAdapter(photoAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AudioVisualFragment.this.setPhotoAdapter$6$AudioVisualFragment(baseQuickAdapter, view, i);
            }
        });
        obtainAudioVisual(Contracts.OPEN_VIEW_TYPE_IMAGE_LIST);
    }

    public void setPhotoAdapter$6$AudioVisualFragment(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        AudioVisualHelper.setSelectIndex(i, 1);
        ((MainActivity) this.mActivity).openAudioVisual(Contracts.OPEN_VIEW_TYPE_IMAGE_GRID);
    }

    private void setPhotoListAdapter() {
        this.viewDirectories.setVisibility(View.GONE);
        this.viewEmpty.setVisibility(View.GONE);
        FileModel currentPhoto = AudioVisualHelper.getCurrentPhoto();
        if (currentPhoto == null) {
            return;
        }
        this.mTvTitle.setText(currentPhoto.getDisplayName());
        List<FileModel> childFiles = currentPhoto.getChildFiles();
        this.mRv.setLayoutManager(new GridLayoutManager(this.mActivity, 3));
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(childFiles);
        this.mAdapter = photoListAdapter;
        this.mRv.setAdapter(photoListAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AudioVisualFragment.this.setPhotoListAdapter$7$AudioVisualFragment(baseQuickAdapter, view, i);
            }
        });
    }

    public void setPhotoListAdapter$7$AudioVisualFragment(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        showConnectDeviceDialog(i);
    }

    private void setVideoAdapter() {
        ReportUtil.loadVideoPage();
        this.viewEmpty.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mImageDirectories.getLayoutParams();
        layoutParams.width = ScreenUtil.dip2px(this.mActivity, 62.0f);
        layoutParams.height = ScreenUtil.dip2px(this.mActivity, 52.0f);
        this.mImageDirectories.setLayoutParams(layoutParams);
        this.mImageDirectories.setImageResource(R.drawable.videos);
        this.mTvTitle.setText(R.string.Video);
        this.mRv.setLayoutManager(new LinearLayoutManager(this.mActivity));
        VideoAdapter videoAdapter = new VideoAdapter(new ArrayList());
        this.mAdapter = videoAdapter;
        this.mRv.setAdapter(videoAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AudioVisualFragment.this.setVideoAdapter$8$AudioVisualFragment(baseQuickAdapter, view, i);
            }
        });
        obtainAudioVisual(768);
    }

    public void setVideoAdapter$8$AudioVisualFragment(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        AudioVisualHelper.setSelectIndex(i, 2);
        ((MainActivity) this.mActivity).openAudioVisual(Contracts.OPEN_VIEW_TYPE_VIDEO_GRID);
    }

    private void setVideoListAdapter() {
        this.viewDirectories.setVisibility(View.GONE);
        this.viewEmpty.setVisibility(View.GONE);
        FileModel currentVideo = AudioVisualHelper.getCurrentVideo();
        if (currentVideo == null) {
            return;
        }
        this.mTvTitle.setText(currentVideo.getDisplayName());
        List<FileModel> childFiles = currentVideo.getChildFiles();
        this.mRv.setLayoutManager(new GridLayoutManager(this.mActivity, 2));
        VideoListAdapter videoListAdapter = new VideoListAdapter(childFiles);
        this.mAdapter = videoListAdapter;
        this.mRv.setAdapter(videoListAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AudioVisualFragment.this.setVideoListAdapter$9$AudioVisualFragment(baseQuickAdapter, view, i);
            }
        });
    }

    public void setVideoListAdapter$9$AudioVisualFragment(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        showConnectDeviceDialog(i);
    }

    private void setAudioAdapter() {
        ReportUtil.loadAudioPage();
        this.viewEmpty.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mImageDirectories.getLayoutParams();
        layoutParams.width = ScreenUtil.dip2px(this.mActivity, 62.0f);
        layoutParams.height = ScreenUtil.dip2px(this.mActivity, 60.0f);
        this.mImageDirectories.setLayoutParams(layoutParams);
        this.mImageDirectories.setImageResource(R.drawable.audios);
        this.mTvTitle.setText(R.string.Audio);
        this.mRv.setLayoutManager(new LinearLayoutManager(this.mActivity));
        AudioAdapter audioAdapter = new AudioAdapter(new ArrayList());
        this.mAdapter = audioAdapter;
        this.mRv.setAdapter(audioAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AudioVisualFragment.this.setAudioAdapter$10$AudioVisualFragment(baseQuickAdapter, view, i);
            }
        });
        obtainAudioVisual(Contracts.OPEN_VIEW_TYPE_AUDIO_LIST);
    }

    public void setAudioAdapter$10$AudioVisualFragment(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        AudioVisualHelper.setSelectIndex(i, 3);
        ((MainActivity) this.mActivity).openAudioVisual(Contracts.OPEN_VIEW_TYPE_AUDIO_GRID);
    }

    private void setAudioListAdapter() {
        this.viewDirectories.setVisibility(View.GONE);
        this.viewEmpty.setVisibility(View.GONE);
        FileModel currentAudio = AudioVisualHelper.getCurrentAudio();
        if (currentAudio == null) {
            return;
        }
        this.mTvTitle.setText(currentAudio.getDisplayName());
        List<FileModel> childFiles = currentAudio.getChildFiles();
        this.mRv.setLayoutManager(new LinearLayoutManager(this.mActivity));
        AudioListAdapter audioListAdapter = new AudioListAdapter(childFiles);
        this.mAdapter = audioListAdapter;
        this.mRv.setAdapter(audioListAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AudioVisualFragment.this.setAudioListAdapter$11$AudioVisualFragment(baseQuickAdapter, view, i);
            }
        });
    }

    public void setAudioListAdapter$11$AudioVisualFragment(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        showConnectDeviceDialog(i);
    }

    private void obtainAudioVisual(final int i) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public final void subscribe(ObservableEmitter observableEmitter) {
                obtainAudioVisual$12$AudioVisualFragment(i, observableEmitter);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new AnonymousClass1(i));
    }

    public void obtainAudioVisual$12$AudioVisualFragment(int i, ObservableEmitter observableEmitter) {
        try {
            showLoading(this.mRv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (i == 773) {
                observableEmitter.onNext(AudioVisualHelper.obtainPhotoList());
                return;
            }
            List<FileModel> obtainAudioList = AudioVisualHelper.obtainAudioList();
            List<FileModel> obtainVideoList = AudioVisualHelper.obtainVideoList();
            if (i == 768) {
                observableEmitter.onNext(obtainVideoList);
            } else {
                observableEmitter.onNext(obtainAudioList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showConnectDeviceDialog(int i) {


        if (this.mAdapter != null) {
            AudioVisualHelper.setIsPlaySingle(false);
            FileModel item = this.mAdapter.getItem(i);
            try{
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CastFolder");
              //  File videodir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");
                if(dir.exists()) {
                    deleteFiles(dir);
                }


                if(item.getPath().contains(".png") || item.getPath().contains(".jpg")||item.getPath().contains(".jpeg")){
              File      imagedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CastFolder");
                    if(!imagedir .exists()) {
                        imagedir .mkdirs();
                    }

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "/CastImageFile" + timeStamp;
                   String imagefile = imagedir  +imageFileName+ ".jpg" ;
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
                        Toast.makeText(mActivity, "File not exists", Toast.LENGTH_SHORT).show();
                    }


                }
                if(item.getPath().contains(".wav")||item.getPath().contains(".mp3") || item.getPath().contains(".aac")|| item.getPath().contains(".m4p")||item.getPath().contains(".webm")) {
                    castNameDisplay= item.getDisplayName();
                    castType="Audio";
                }
                //  File   audiodir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");
//                    if(!audiodir.exists()) {
//                        audiodir.mkdirs();
//                    }
//                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                    String imageFileName = "/CastAudioFile" + timeStamp;
//                   String imagefile = audiodir +imageFileName+ ".wav" ;
//                    File createDir = new File( imagefile);
//                    createDir.createNewFile();
//                    if(!createDir.exists()) {
//                        createDir.mkdir();
//                    }
//                    copyFile(new File(item.getPath()),new File(imagefile));
//                    if(new File(imagefile).exists()){
//                        SharedPreferences imageSharedPreferences = getActivity().getSharedPreferences("AudioCastSharedPreference", MODE_PRIVATE);
//                        SharedPreferences.Editor imageEditor =imageSharedPreferences.edit();
//                        imageEditor.putString("AudioPath", imagefile);
//                        imageEditor.commit();
//                    }else{
//                        Toast.makeText(mActivity, "File not exists", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
                if(item.getPath().contains(".mp4") ||item.getPath().contains(".3gp") || item.getPath().contains(".mkv")|| item.getPath().contains(".webm")||item.getPath().contains(".mov")) {
                    castNameDisplay= item.getDisplayName();
                    castType="Video";
                }

                    // videodir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");
//
//                    if(!videodir.exists()) {
//                        videodir.mkdirs();
//                    }
//                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                    String imageFileName = "/CastVideoFile" + timeStamp;
//                   String imagefile = videodir +imageFileName+ ".mp4" ;
//                    File createDir = new File( imagefile);
//                    createDir.createNewFile();
//                    if(!createDir.exists()) {
//                        createDir.mkdir();
//                    }
//                    copyFile(new File(item.getPath()),new File(imagefile));
//                    if(new File(imagefile).exists()){
//                        SharedPreferences imageSharedPreferences = getActivity().getSharedPreferences("VideoCastSharedPreference", MODE_PRIVATE);
//                        SharedPreferences.Editor imageEditor =imageSharedPreferences.edit();
//                        imageEditor.putString("VideoPath", imagefile);
//                        imageEditor.commit();
//                    }else{
//                        Toast.makeText(mActivity, "File not exists", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
            }catch(Exception e){
                e.printStackTrace();
            }


            if (DLNAHelper.isConnectDevice()) {
                if (MaxRewardUtil.obtainCastFileNum() <= 0) {
                    RewardDialogEvent.post(1);
                    return;
                } else if (item.getFileType() == 272) {
                    AudioVisualFragment_startActivity(this, new Intent(this.mActivity, PhotoCastActivity.class));
                } else {
                    Intent intent = new Intent(this.mActivity,AudioVideoCastActivity.class);
                    intent.putExtra("castType", castType);
                  intent.putExtra("castNameDisplay",castNameDisplay);
                   startActivity(intent);
                }
            } else {
                ConnectDeviceDialog.newInstance(item).show(getChildFragmentManager(), "ConnectDevice");



            }

            AudioVisualHelper.setSelectChildIndex(i);
        }
    }

    public void deleteFiles(File file) {
        File file2 = new File(file.getPath());
        if (file2.exists()) {
            file2.delete();
        }
    }
    public static boolean deleteFolder(File removableFolder) {
        File[] files = removableFolder.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                boolean success;
                if (file.isDirectory())
                    success = deleteFolder(file);
                else success = file.delete();
                if (!success) return false;
            }
        }
        return removableFolder.delete();
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

//    private void copyFile(String inputPath, String inputFile, String outputPath) {
//
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//
//            //create output directory if it doesn't exist
//            File dir = new File (outputPath);
//            if (!dir.exists())
//            {
//                dir.mkdirs();
//            }
//
//
//            in = new FileInputStream(inputPath + inputFile);
//            out = new FileOutputStream(outputPath + inputFile);
//            Log.d("infile", String.valueOf(in));
//            Log.d("Outfile", String.valueOf(out));
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//            in = null;
//
//            // write the output file (You have now copied the file)
//            out.flush();
//            out.close();
//            out = null;
//
//        }  catch (FileNotFoundException fnfe1) {
//            Log.e("tag", fnfe1.getMessage());
//        }
//        catch (Exception e) {
//            Log.e("tag", e.getMessage());
//        }
//
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        int i = this.mPageType;
        if (i == 770 || i == 768 || i == 773) {
            if (DLNAHelper.mConnectStatus.getValue() != null && DLNAHelper.mConnectStatus.getValue() != ConnectStatus.DISCONNECT) {
                return;
            }
            AudioVisualHelper.recycler();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }
        try {
            if (!DLNAHelper.isConnectDevice()) {
                getView().setFocusableInTouchMode(true);
                getView().requestFocus();
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                        if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                // Toast.makeText(mActivity, "Please disconnect the device", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class AnonymousClass1 extends SimpleObserver<Object> {
        final int val$pageType;

        AnonymousClass1(int i) {
            this.val$pageType = i;
        }

        @Override
        public void onNext(final Object obj) {
            if (AudioVisualFragment.this.mActivity.isFinishing()) {
                return;
            }
            Handler handler = new Handler();
            final int i = this.val$pageType;
            handler.postDelayed(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass1.this.onNext$0$AudioVisualFragment$1(i, obj);
                }
            }, 100L);
        }

        public void onNext$0$AudioVisualFragment$1(int i, Object obj) {
            AudioVisualFragment audioVisualFragment = AudioVisualFragment.this;
            audioVisualFragment.hideLoading(audioVisualFragment.mRv);
            if (i != 773) {
                AudioVisualFragment.this.viewDirectories.setVisibility(View.VISIBLE);
            }
            AudioVisualFragment.this.mAdapter.setList((List) obj);
        }
    }

}
