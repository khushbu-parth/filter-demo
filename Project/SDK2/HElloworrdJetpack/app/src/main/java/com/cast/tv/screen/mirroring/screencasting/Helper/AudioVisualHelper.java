package com.cast.tv.screen.mirroring.screencasting.Helper;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.cast.tv.screen.mirroring.screencasting.CastApp;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioVisualHelper {
    public static final int AUDIO_VISUAL_AUDIO = 3;
    public static final int AUDIO_VISUAL_IMAGE = 1;
    public static final int AUDIO_VISUAL_VIDEO = 2;
    private static final int RECENT_MIN = 50;
    private static final List<FileModel> mPhotoList = new ArrayList();
    private static final List<FileModel> mVideoList = new ArrayList();
    private static final List<FileModel> mAudioList = new ArrayList();
    public static MutableLiveData<FileModel> mCastFileModel = new MutableLiveData<>();
    private static int CURRENT_TYPE = 0;
    private static String mAudioVisualCurrentName;
    private static List<FileModel> mAudioVisualPlayList;
    private static int mCurrentCastAudioIndex;
    private static int mCurrentCastPhotoIndex;
    private static int mCurrentCastVideoIndex;
    private static boolean mIsPlaySingle;
    private static int mSelectIndex = 0;
    private static int mSelectChildIndex = 0;

    private AudioVisualHelper() {
    }

    public static synchronized List<FileModel> obtainPhotoList() {
        synchronized (AudioVisualHelper.class) {
            List<FileModel> list = mPhotoList;
            if (ListUtil.getSize(list) > 0) {
                return list;
            }
            return obtain(1, list);
        }
    }

    public static synchronized List<FileModel> obtainVideoList() {
        synchronized (AudioVisualHelper.class) {
            List<FileModel> list = mVideoList;
            if (ListUtil.getSize(list) > 0) {
                return list;
            }
            return obtain(2, list);
        }
    }

    public static synchronized List<FileModel> obtainAudioList() {
        synchronized (AudioVisualHelper.class) {
            List<FileModel> list = mAudioList;
            if (ListUtil.getSize(list) > 0) {
                return list;
            }
            return obtain(3, list);
        }
    }

    private static List<FileModel> obtain(int i, List<FileModel> list) {
        List<FileModel> systemAudioVisualList = getSystemAudioVisualList(i);
        if (systemAudioVisualList != null && ListUtil.getSize(systemAudioVisualList) > 0) {
            list.add(recent(systemAudioVisualList));
            list.addAll(conversion(systemAudioVisualList));
        }
        return list;
    }

    private static FileModel recent(List<FileModel> list) {
        int size = (int) (list.size() * 0.3f);
        if (size < 50) {
            size = 50;
        }
        FileModel fileModel = new FileModel();
        int i = 0;
        String str = null;
        ArrayList arrayList = new ArrayList();
        for (int size2 = list.size() - 1; size2 >= 0; size2--) {
            if (str == null || str.isEmpty()) {
                str = list.get(size2).getPath();
            }
            arrayList.add(list.get(size2));
            i++;
            if (i >= size) {
                break;
            }
        }
        fileModel.setDisplayName("Recent");
        fileModel.setChildFiles(arrayList);
        fileModel.setCover(str);
        return fileModel;
    }

    private static List<FileModel> conversion(List<FileModel> list) {
        ArrayList<FileModel> arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        for (FileModel fileModel : list) {
            String parentName = getParentName(fileModel);
            if (hashMap.get(parentName) == null) {
                FileModel fileModel2 = new FileModel();
                fileModel2.setDisplayName(parentName);
                fileModel2.setCover(fileModel.getPath());
                arrayList.add(fileModel2);
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(fileModel);
                hashMap.put(parentName, arrayList2);
            } else {
                List list2 = (List) hashMap.get(parentName);
                if (list2 != null) {
                    list2.add(fileModel);
                }
            }
        }
        for (FileModel fileModel3 : arrayList) {
            fileModel3.setChildFiles((List) hashMap.get(fileModel3.getDisplayName()));
        }
        return arrayList;
    }

    private static String getParentName(FileModel fileModel) {
        return getParentName(fileModel.getPath());
    }

    private static List<FileModel> getSystemAudioVisualList(int i) {
        Uri uri = null;
        Uri uri2;
        int columnIndexOrThrow = 0;
        ArrayList arrayList = new ArrayList();
        if (i == 1) {
            uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (i == 2) {
            uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (i != 3) {
            Cursor query = CastApp.mContext.getContentResolver().query(uri, null, null, null, null);
            if (query == null || query.getCount() <= 0) {
                return null;
            }
            while (query.moveToNext()) {
                long j = 0;
                String str = "";
                if (i == 1) {
                    columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                } else if (i == 2) {
                    long j2 = query.getLong(query.getColumnIndexOrThrow("duration"));
                    if (j2 > 0) {
                        columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                        j = j2;
                    }
                } else {
                    long j3 = query.getLong(query.getColumnIndexOrThrow("duration"));
                    if (j3 > 0) {
                        columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                        str = query.getString(query.getColumnIndexOrThrow("artist"));
                        j = j3;
                    }
                }
                String string = query.getString(columnIndexOrThrow);
                if (string != null && !string.isEmpty()) {
                    File file = new File(string);
                    if (file.exists()) {
                        FileModel fileModel = new FileModel();
                        fileModel.setDisplayName(file.getName());
                        fileModel.setPath(string);
                        if (i == 2) {
                            fileModel.setMiniKind(ThumbnailUtils.createVideoThumbnail(string, 1));
                            fileModel.setDuration(j);
                        } else if (i == 3) {
                            fileModel.setSubTitle(str);
                            fileModel.setDuration(j);
                        }
                        arrayList.add(fileModel);
                    }
                }
            }
            query.close();
            return arrayList;
        } else {
            uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        uri = uri2;
        if (uri != null) {
            Cursor query = CastApp.mContext.getContentResolver().query(uri, null, null, null, null);
            if (query == null || query.getCount() <= 0) {
                return null;
            }
            while (query.moveToNext()) {
                long j = 0;
                String str = "";
                if (i == 1) {
                    columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                } else if (i == 2) {
                    long j2 = query.getLong(query.getColumnIndexOrThrow("duration"));
                    if (j2 > 0) {
                        columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                        j = j2;
                    }
                } else {
                    long j3 = query.getLong(query.getColumnIndexOrThrow("duration"));
                    if (j3 > 0) {
                        columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                        str = query.getString(query.getColumnIndexOrThrow("artist"));
                        j = j3;
                    }
                }
                String string = query.getString(columnIndexOrThrow);
                if (string != null && !string.isEmpty()) {
                    File file = new File(string);
                    if (file.exists()) {
                        FileModel fileModel = new FileModel();
                        fileModel.setDisplayName(file.getName());
                        fileModel.setPath(string);
                        if (i == 2) {
                            fileModel.setMiniKind(ThumbnailUtils.createVideoThumbnail(string, 1));
                            fileModel.setDuration(j);
                        } else if (i == 3) {
                            fileModel.setSubTitle(str);
                            fileModel.setDuration(j);
                        }
                        arrayList.add(fileModel);
                    }
                }
            }
            query.close();
            return arrayList;
        }
        return null;
    }

    public static long getVideoDuration(String str) {
        FileModel dataByPath = getDataByPath(2, str);
        if (dataByPath == null) {
            return 0L;
        }
        return dataByPath.getDuration();
    }

    public static Bitmap getVideoMiniKind(String str) {
        FileModel dataByPath = getDataByPath(2, str);
        if (dataByPath == null) {
            return null;
        }
        return dataByPath.getMiniKind();
    }

    public static long getAudioDuration(String str) {
        FileModel dataByPath = getDataByPath(3, str);
        if (dataByPath == null) {
            return 0L;
        }
        return dataByPath.getDuration();
    }

    public static String getAudioSubtitle(String str) {
        FileModel dataByPath = getDataByPath(3, str);
        return dataByPath == null ? "" : dataByPath.getSubTitle();
    }

    private static FileModel getDataByPath(int i, String str) {
        List<FileModel> list;
        if (i == 1) {
            list = mPhotoList;
        } else if (i == 2) {
            list = mVideoList;
        } else {
            list = i == 3 ? mAudioList : null;
        }
        if (list != null && ListUtil.getSize(list) > 0) {
            for (FileModel fileModel : list) {
                if (getParentName(str).equals(fileModel.getDisplayName())) {
                    for (FileModel fileModel2 : fileModel.getChildFiles()) {
                        if (fileModel2.getPath().equals(str)) {
                            return fileModel2;
                        }
                    }
                    continue;
                }
            }
        }
        return null;
    }

    private static String getParentName(String str) {
        if (str != null && !str.isEmpty()) {
            File file = new File(str);
            if (file.exists() && file.getParent() != null) {
                File file2 = new File(file.getParent());
                if (file2.exists()) {
                    return file2.getName();
                }
            }
        }
        return "";
    }

    public static FileModel getAudioVisualList() {
        int i = CURRENT_TYPE;
        if (i == 1) {
            return getCurrentPhoto();
        }
        if (i == 3) {
            return getCurrentAudio();
        }
        if (i != 2) {
            return null;
        }
        return getCurrentVideo();
    }

    public static void setSelectIndex(int i, int i2) {
        mSelectIndex = i;
        CURRENT_TYPE = i2;
        if (i2 == 1) {
            mCurrentCastPhotoIndex = i;
        } else if (i2 == 3) {
            mCurrentCastAudioIndex = i;
        } else {
            mCurrentCastVideoIndex = i;
        }
    }

    public static void setPlayListSelectChildIndex(int i) {
        int obtainCastFileNum = MaxRewardUtil.obtainCastFileNum();
        if (obtainCastFileNum <= 2) {
            RewardDialogEvent.post(3);
            if (obtainCastFileNum <= 0) {
                return;
            }
        }
        mSelectChildIndex = i;
        if (i < 0) {
            List<FileModel> list = mAudioVisualPlayList;
            if (list == null) {
                return;
            }
            list.clear();
            mAudioVisualPlayList = null;
            DLNAHelper.stopPlayPhotoService();
            return;
        }
        int size = ListUtil.getSize(mAudioVisualPlayList);
        int i2 = mSelectChildIndex;
        if (size <= i2) {
            return;
        }
        FileModel fileModel = mAudioVisualPlayList.get(i2);
        mCastFileModel.setValue(fileModel);
        DLNAHelper.startPlay(fileModel);
    }

    public static int getSelectChildIndex() {
        if (mIsPlaySingle) {
            return 0;
        }
        return mSelectChildIndex;
    }

    public static void setSelectChildIndex(int i) {
        FileModel audioVisualList;
        mSelectChildIndex = i;
        if (i < 0) {
            mCastFileModel.setValue(null);
            return;
        }
        try {
            List<FileModel> list = mAudioVisualPlayList;
            if (list == null) {
                mAudioVisualPlayList = new ArrayList();
            } else {
                list.clear();
            }
            DLNAHelper.stopPlayPhotoService();
            if (isPlaySingle()) {
                audioVisualList = mCastFileModel.getValue();
                if (audioVisualList != null) {
                    mAudioVisualPlayList.add(audioVisualList.m146clone());
                }
            } else {
                audioVisualList = getAudioVisualList();
                if (audioVisualList == null) {
                    return;
                }
                mAudioVisualCurrentName = audioVisualList.getDisplayName();
                List<FileModel> childFiles = audioVisualList.getChildFiles();
                int size = ListUtil.getSize(childFiles);
                int i2 = mSelectChildIndex;
                if (size > i2) {
                    audioVisualList = childFiles.get(i2);
                    mCastFileModel.setValue(audioVisualList);
                }
                for (FileModel fileModel : childFiles) {
                    mAudioVisualPlayList.add(fileModel.m146clone());
                }
            }
            DLNAHelper.startPlay(audioVisualList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileModel getCurrentPhoto() {
        mSelectIndex = mCurrentCastPhotoIndex;
        return getCurrentData(mPhotoList);
    }

    public static FileModel getCurrentVideo() {
        mSelectIndex = mCurrentCastVideoIndex;
        return getCurrentData(mVideoList);
    }

    public static FileModel getCurrentAudio() {
        mSelectIndex = mCurrentCastAudioIndex;
        return getCurrentData(mAudioList);
    }

    private static FileModel getCurrentData(List<FileModel> list) {
        int size = ListUtil.getSize(list);
        int i = mSelectIndex;
        if (size > i) {
            return list.get(i);
        }
        return null;
    }

    public static List<FileModel> getAudioVisualPlayList() {
        List<FileModel> list = mAudioVisualPlayList;
        return list == null ? new ArrayList() : list;
    }

    public static String getAudioVisualCurrentDisplayName() {
        String str = mAudioVisualCurrentName;
        if (str != null) {
            return str;
        }
        FileModel value = mCastFileModel.getValue();
        return value != null ? value.getDisplayName() : "";
    }

    public static List<FileModel> searchAudio(String str) {
        return search(str, mAudioList);
    }

    public static List<FileModel> searchVideo(String str) {
        return search(str, mVideoList);
    }

    private static List<FileModel> search(String str, List<FileModel> list) {
        ArrayList arrayList = new ArrayList();
        if (ListUtil.getSize(list) > 1) {
            for (int i = 1; i < list.size(); i++) {
                FileModel fileModel = list.get(i);
                if (ListUtil.getSize(fileModel.getChildFiles()) > 0) {
                    for (FileModel fileModel2 : fileModel.getChildFiles()) {
                        String displayName = fileModel2.getDisplayName();
                        if (!TextUtils.isEmpty(displayName) && displayName.contains(str)) {
                            arrayList.add(fileModel2);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public static void setIsPlaySingle(boolean z) {
        mIsPlaySingle = z;
    }

    public static boolean isPlaySingle() {
        return mIsPlaySingle;
    }

    public static void recycler() {
        mPhotoList.clear();
        mVideoList.clear();
        mAudioList.clear();
    }
}
