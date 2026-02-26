package com.cast.tv.screen.mirroring.screencasting.UI.audio_visual;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseFragment;
import com.cast.tv.screen.mirroring.screencasting.Callback.DirectoriesItemClick;
import com.cast.tv.screen.mirroring.screencasting.Contract.IntentContracts;
import com.cast.tv.screen.mirroring.screencasting.Dialog.ConnectDeviceDialog;
import com.cast.tv.screen.mirroring.screencasting.Dialog.NormalTipDialog;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.DirectoriesAdapter;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.AudioVideoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.PhotoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.help.HelpActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.main.MainActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.T;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoriesFragment extends BaseFragment {
    private RecyclerView mRv;
    private TextView mTextDirectories;

    public static DirectoriesFragment newInstance(String str) {
        DirectoriesFragment directoriesFragment = new DirectoriesFragment();
        Bundle bundle = new Bundle();
        if (str != null && !TextUtils.isEmpty(str)) {
            bundle.putString(IntentContracts.INTENT_DIRECTORIES_PATH, str);
        }
        directoriesFragment.setArguments(bundle);
        return directoriesFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_directories;
    }

    @Override
    public void setViewData(View view) {

        view.findViewById(R.id.view_root).setOnClickListener(null);
        view.findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                DirectoriesFragment.this.setViewData0DirectoriesFragment(view2);
            }
        });
        this.mTextDirectories = (TextView) view.findViewById(R.id.text_directories);
        this.mRv = (RecyclerView) view.findViewById(R.id.recycler_view);
        handlerArguments();
    }

    public void setViewData0DirectoriesFragment(View view) {
        this.mActivity.onBackPressed();
    }

    @Override
    public void setClickEvent(View view) {
        view.findViewById(R.id.image_cast_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                try {
                    DirectoriesFragment.this.setClickEvent2DirectoriesFragment(view2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        view.findViewById(R.id.image_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                DirectoriesFragment.this.setClickEvent3DirectoriesFragment(view2);
            }
        });
    }

    public void setClickEvent2DirectoriesFragment(View view) {
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
    }

    public void setClickEvent3DirectoriesFragment(View view) {
        startActivity(new Intent(this.mActivity, HelpActivity.class));
    }

    private void handlerArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(IntentContracts.INTENT_DIRECTORIES_PATH);
            setTextTitle(string);
            setRecyclerViewData(string);
        }
    }

    private void setTextTitle(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            str = "";
        }
        this.mTextDirectories.setText(str);
    }

    private void setRecyclerViewData(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return;
        }
        this.mRv.setLayoutManager(new LinearLayoutManager(this.mActivity));
        List<FileModel> obtainDataByPath = obtainDataByPath(str);
        if (ListUtil.getSize(obtainDataByPath) <= 0) {
            return;
        }
        DirectoriesAdapter directoriesAdapter = new DirectoriesAdapter(obtainDataByPath);
        this.mRv.setAdapter(directoriesAdapter);
        directoriesAdapter.setItemClick(new DirectoriesItemClick() {
            @Override
            public final void onItemClick(FileModel fileModel) {
                DirectoriesFragment.this.setRecyclerViewData4DirectoriesFragment(fileModel);
            }
        });
    }

    public void setRecyclerViewData4DirectoriesFragment(FileModel fileModel) {
        if (fileModel.getFileType() == 275) {
            ((MainActivity) this.mActivity).openDirectories(fileModel.getPath());
            return;
        }
        castScreenFile(fileModel);
    }

    private void castScreenFile(FileModel fileModel) {
        AudioVisualHelper.setIsPlaySingle(true);
        AudioVisualHelper.mCastFileModel.setValue(fileModel);
        AudioVisualHelper.setSelectChildIndex(0);
        if (DLNAHelper.isConnectDevice()) {
            if (MaxRewardUtil.obtainCastFileNum() <= 0) {
                RewardDialogEvent.post(1);
                return;
            }
            int fileType = fileModel.getFileType();
            if (fileType == 272) {
                startActivity(new Intent(this.mActivity, PhotoCastActivity.class));
                return;
            } else if (fileType == 273 || fileType == 274) {
                startActivity(new Intent(this.mActivity, AudioVideoCastActivity.class));
                return;
            } else {
                T.showShort(this.mActivity, "Unsupported file type");
                return;
            }
        }
        ConnectDeviceDialog.newInstance(fileModel).show(getChildFragmentManager(), "ConnectDevice");
    }

    private List<FileModel> obtainDataByPath(String str) {
        File[] listFiles;
        ArrayList arrayList = null;
        if (str != null && !TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                ArrayList arrayList2 = null;
                for (File file2 : listFiles) {
                    FileModel fileModel = new FileModel();
                    if (file2.isDirectory()) {
                        fileModel.setPath(file2.getAbsolutePath());
                        fileModel.setDisplayName(file2.getName());
                        fileModel.setChildCount(getChildCount(file2.getAbsolutePath()));
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(fileModel);
                    } else {
                        fileModel.setPath(file2.getAbsolutePath());
                        fileModel.setDisplayName(file2.getName());
                        if (isAddFile(fileModel)) {
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList();
                            }
                            arrayList2.add(fileModel);
                        }
                    }
                }
                ArrayList arrayList3 = new ArrayList();
                if (arrayList != null) {
                    arrayList3.addAll(arrayList);
                }
                if (arrayList2 != null) {
                    arrayList3.addAll(arrayList2);
                }
                return arrayList3;
            }
        }
        return arrayList;
    }

    private int getChildCount(String str) {
        File[] listFiles;
        if (str != null && !TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists() && file.isDirectory() && (listFiles = file.listFiles()) != null) {
                return listFiles.length;
            }
            return 0;
        }
        return 0;
    }

    private boolean isAddFile(FileModel fileModel) {
        if (fileModel == null) {
            return false;
        }
        int fileType = fileModel.getFileType();
        if (fileType == 273) {
            String path = fileModel.getPath();
            fileModel.setMiniKind(AudioVisualHelper.getVideoMiniKind(path));
            fileModel.setDuration(AudioVisualHelper.getVideoDuration(path));
        } else if (fileType == 274) {
            String path2 = fileModel.getPath();
            fileModel.setDuration(AudioVisualHelper.getAudioDuration(path2));
            fileModel.setSubTitle(AudioVisualHelper.getAudioSubtitle(path2));
        }
        return fileType == 275 || fileType == 272 || fileType == 273 || fileType == 274;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }

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
    }
}
