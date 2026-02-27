package com.pu.casttotv.tvcast.screenmirror.tvremote.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioAlbumModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.PhotoAlbum;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

public class Utils {
    public static ArrayList<MediaModel> getAllPhoto(Activity activity) {
        String[] strArr = {"title", "_data", "_id"};
        ArrayList<MediaModel> arrayList = new ArrayList<>();
        try {
            Cursor managedQuery = activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "datetaken DESC");
            for (int i = 0; i < managedQuery.getCount(); i++) {
                managedQuery.moveToPosition(i);
                int columnIndex = managedQuery.getColumnIndex("title");
                int columnIndex2 = managedQuery.getColumnIndex("_data");
                String string = managedQuery.getString(columnIndex);
                String string2 = managedQuery.getString(columnIndex2);
                arrayList.add(new MediaModel(string, string2, string2));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        ArrayList<MediaModel> arrayListTemp = new ArrayList<>();
//        MediaModel mediaModel2 = new MediaModel();
//        mediaModel2.header = true;
        arrayListTemp.add(null);
        int i = 1;
        for (MediaModel mediaModel : arrayList) {
            if (i == 16) {
//                MediaModel mediaModel1 = new MediaModel();
//                mediaModel1.header = true;
                arrayListTemp.add(null);
                i = 1;
            }
            arrayListTemp.add(mediaModel);
            i++;

        }
        return arrayListTemp;
    }

    public static void nextScreen(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void backScreen(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static ArrayList<PhotoAlbum> getPhotoAlbums(Activity activity) {
        Cursor cursor;
        ArrayList arrayList = new ArrayList();
        ArrayList<PhotoAlbum> arrayList2 = new ArrayList<>();
        Vector vector = new Vector();
        String[] strArr = {"bucket_display_name", "_data", "_id"};
        try {
            cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, null, null, null);
        } catch (Exception e2) {
            e2.printStackTrace();
            cursor = null;
        }
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("bucket_display_name");
                int columnIndex2 = cursor.getColumnIndex("_data");
                int columnIndex3 = cursor.getColumnIndex("_id");
                do {
                    String string = cursor.getString(columnIndex);
                    String string2 = cursor.getString(columnIndex2);
                    String string3 = cursor.getString(columnIndex3);
                    MediaModel mediaModel = new MediaModel();
                    mediaModel.setAlbumName(string);
                    mediaModel.setPhotoUri(string2);
                    mediaModel.setId(Integer.valueOf(string3).intValue());
                    if (vector.contains(string)) {
                        Iterator it = arrayList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            PhotoAlbum photoAlbum = (PhotoAlbum) it.next();
                            try {
                                if (photoAlbum.getName().equals(string)) {
                                    photoAlbum.getAlbumPhotos().add(mediaModel);
                                    break;
                                }
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    } else {
                        PhotoAlbum photoAlbum2 = new PhotoAlbum();
                        photoAlbum2.setId(mediaModel.getId());
                        photoAlbum2.setName(string);
                        photoAlbum2.setCoverUri(mediaModel.getPhotoUri());
                        photoAlbum2.getAlbumPhotos().add(mediaModel);
                        arrayList.add(photoAlbum2);
                        vector.add(string);
                    }
                } while (cursor.moveToNext());
            }
            ArrayList<MediaModel> allPhoto = getAllPhoto(activity);
            arrayList2.add(new PhotoAlbum(0, "All Photo", allPhoto.get(0).getPhotoUri(), allPhoto));
            arrayList2.addAll(arrayList);
            cursor.close();
        }
        return arrayList2;
    }

    public static ArrayList<MediaModel> getMediaVideos(Activity activity) {
        String[] strArr = {"title", "_data", "_id", "duration"};
        ArrayList<MediaModel> arrayList = new ArrayList<>();
        try {
            Cursor managedQuery = activity.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "datetaken DESC");
            for (int i = 0; i < managedQuery.getCount(); i++) {
                managedQuery.moveToPosition(i);
                int columnIndex = managedQuery.getColumnIndex("title");
                int columnIndex2 = managedQuery.getColumnIndex("_data");
                int columnIndex3 = managedQuery.getColumnIndex("duration");
                String string = managedQuery.getString(columnIndex);
                String string2 = managedQuery.getString(columnIndex2);
                arrayList.add(new MediaModel(string, string2, string2, managedQuery.getLong(columnIndex3)));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        ArrayList<MediaModel> arrayListTemp = new ArrayList<>();
       // MediaModel mediaModel2 = new MediaModel();
        //mediaModel2.header = true;
        arrayListTemp.add(null);
        int i = 1;
        for (MediaModel mediaModel : arrayList) {
            if (i ==16) {
              // MediaModel mediaModel1 = new MediaModel();
              //  mediaModel1.header = true;
                arrayListTemp.add(null);
                i = 1;
            }
            arrayListTemp.add(mediaModel);
            i++;

        }
        return arrayListTemp;
    }

    public static ArrayList<PhotoAlbum> getVideoAlbums(Activity activity) {
        Cursor cursor;
        Vector vector = new Vector();
        ArrayList<PhotoAlbum> arrayList = new ArrayList<>();
        Vector vector2 = new Vector();
        String[] strArr = {"bucket_display_name", "_data", "_id", "duration"};
        try {
            cursor = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, null, null, null);
        } catch (Exception e2) {
            e2.printStackTrace();
            cursor = null;
        }
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("bucket_display_name");
                int columnIndex2 = cursor.getColumnIndex("_data");
                int columnIndex3 = cursor.getColumnIndex("_id");
                int columnIndex4 = cursor.getColumnIndex("duration");
                do {
                    String string = cursor.getString(columnIndex);
                    String string2 = cursor.getString(columnIndex2);
                    String string3 = cursor.getString(columnIndex3);
                    long j = cursor.getLong(columnIndex4);
                    MediaModel mediaModel = new MediaModel();
                    mediaModel.setAlbumName(string);
                    mediaModel.setPhotoUri(string2);
                    mediaModel.setDuration(j);
                    mediaModel.setId(Integer.valueOf(string3).intValue());
                    if (vector2.contains(string)) {
                        Iterator it = vector.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            PhotoAlbum photoAlbum = (PhotoAlbum) it.next();
                            try {
                                if (photoAlbum.getName().equals(string)) {
                                    photoAlbum.getAlbumPhotos().add(mediaModel);
                                    break;
                                }
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    } else {
                        PhotoAlbum photoAlbum2 = new PhotoAlbum();
                        photoAlbum2.setId(mediaModel.getId());
                        photoAlbum2.setName(string);
                        photoAlbum2.setCoverUri(mediaModel.getPhotoUri());
                        photoAlbum2.getAlbumPhotos().add(mediaModel);
                        vector.add(photoAlbum2);
                        vector2.add(string);
                    }
                } while (cursor.moveToNext());
            }
            ArrayList<MediaModel> mediaVideos = getMediaVideos(activity);
            arrayList.add(new PhotoAlbum(0, "All Video", mediaVideos.get(0).getPhotoUri(), mediaVideos));
            arrayList.addAll(vector);
            cursor.close();
        }
        return arrayList;
    }

    public static ArrayList<AudioModel> getAllAudioFromDevice(Context context) {
        ArrayList<AudioModel> arrayList = new ArrayList<>();
        try {
            Cursor query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "title", "album", "artist", "duration"}, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    AudioModel audioModel = new AudioModel();
                    String string = query.getString(0);
                    String string2 = query.getString(1);
                    String string3 = query.getString(2);
                    String string4 = query.getString(3);
                    long j = query.getLong(4);
                    audioModel.setSongTitle(string2);
                    audioModel.setSongAlbum(string3);
                    audioModel.setSongArtist(string4);
                    audioModel.setSongPath(string);
                    audioModel.setDuration(j);
                    arrayList.add(audioModel);
                }
                query.close();
            }
            ArrayList<AudioModel> arrayListTemp = new ArrayList<>();
            AudioModel mediaModel2 = new AudioModel();
            mediaModel2.header = true;
            arrayListTemp.add(mediaModel2);
            int i = 1;
            for (AudioModel mediaModel : arrayList) {
                if (i == 10) {
                    AudioModel mediaModel1 = new AudioModel();
                    mediaModel1.header = true;
                    arrayListTemp.add(mediaModel1);
                    i = 1;
                }
                arrayListTemp.add(mediaModel);
                i++;

            }
            return arrayListTemp;
        } catch (Exception e2) {
            e2.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static ArrayList<AudioAlbumModel> getAllArtist(Context context) {
        ArrayList<AudioModel> allAudioFromDevice = getAllAudioFromDevice(context);
        ArrayList<AudioAlbumModel> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < allAudioFromDevice.size(); i++) {
            if (!arrayList2.contains(allAudioFromDevice.get(i).getSongArtist())) {
                arrayList2.add(allAudioFromDevice.get(i).getSongArtist());
                ArrayList arrayList3 = new ArrayList();
                for (int i2 = 0; i2 < allAudioFromDevice.size(); i2++) {
                    if (!(allAudioFromDevice.get(i2).getSongArtist() == null || allAudioFromDevice.get(i).getSongArtist() == null || !allAudioFromDevice.get(i2).getSongArtist().equals(allAudioFromDevice.get(i).getSongArtist()))) {
                        arrayList3.add(allAudioFromDevice.get(i2));
                    }
                }
                arrayList.add(new AudioAlbumModel(allAudioFromDevice.get(i).getSongArtist(), arrayList3));
            }
        }
        ArrayList<AudioAlbumModel> arrayListTemp = new ArrayList<>();
        AudioAlbumModel mediaModel2 = new AudioAlbumModel();
        mediaModel2.header = true;
        arrayListTemp.add(mediaModel2);
        int i = 1;
        for (AudioAlbumModel mediaModel : arrayList) {
            if (i == 10) {
                AudioAlbumModel mediaModel1 = new AudioAlbumModel();
                mediaModel1.header = true;
                arrayListTemp.add(mediaModel1);
                i = 1;
            }
            arrayListTemp.add(mediaModel);
            i++;

        }
        return arrayListTemp;
    }

    public static ArrayList<AudioAlbumModel> getAllAlbumAudio(Context context) {
        ArrayList<AudioModel> allAudioFromDevice = getAllAudioFromDevice(context);
        ArrayList<AudioAlbumModel> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < allAudioFromDevice.size(); i++) {
            if (!arrayList2.contains(allAudioFromDevice.get(i).getSongAlbum())) {
                arrayList2.add(allAudioFromDevice.get(i).getSongAlbum());
                ArrayList arrayList3 = new ArrayList();
                for (int i2 = 0; i2 < allAudioFromDevice.size(); i2++) {
                    if (!(allAudioFromDevice.get(i2).getSongAlbum() == null || allAudioFromDevice.get(i).getSongAlbum() == null || !allAudioFromDevice.get(i2).getSongAlbum().equals(allAudioFromDevice.get(i).getSongAlbum()))) {
                        arrayList3.add(allAudioFromDevice.get(i2));
                    }
                }
                arrayList.add(new AudioAlbumModel(allAudioFromDevice.get(i).getSongAlbum(), arrayList3));
            }
        }
        ArrayList<AudioAlbumModel> arrayListTemp = new ArrayList<>();
        AudioAlbumModel mediaModel2 = new AudioAlbumModel();
        mediaModel2.header = true;
        arrayListTemp.add(mediaModel2);
        int i = 1;
        for (AudioAlbumModel mediaModel : arrayList) {
            if (i == 10) {
                AudioAlbumModel mediaModel1 = new AudioAlbumModel();
                mediaModel1.header = true;
                arrayListTemp.add(mediaModel1);
                i = 1;
            }
            arrayListTemp.add(mediaModel);
            i++;

        }
        return arrayListTemp;
    }

    public static String formatTime(long j) {
        int i = (int) (j / 1000);
        int i2 = i / 3600;
        int i3 = i % 3600;
        int i4 = i3 / 60;
        int i5 = i3 % 60;
        if (i2 > 0) {
            return String.format(Locale.US, "%d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i4), Integer.valueOf(i5));
        }
        return String.format(Locale.US, "%d:%02d", Integer.valueOf(i4), Integer.valueOf(i5));
    }

    @SuppressLint("WrongConstant")
    public static void goToGooglePlay(Context context, String str) {
        SharedPrefsUtil.getInstance().put(Const.KEY_RATED, Boolean.TRUE);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + str));
        if (Build.VERSION.SDK_INT >= 21) {
            intent.addFlags(1476919296);
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + str)));
        }
    }

    @SuppressLint("WrongConstant")
    public static void sendFeedBack(Context context, String str, String str2) {
        SharedPrefsUtil.getInstance().put(Const.KEY_RATED, Boolean.TRUE);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/email");
        intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
        intent.putExtra("android.intent.extra.SUBJECT", context.getResources().getString(R.string.rate_title));
        intent.putExtra("android.intent.extra.TEXT", str2);
        Intent createChooser = Intent.createChooser(intent, "Send Feedback:");
        createChooser.addFlags(268435456);
        context.startActivity(createChooser);
    }

    public static String readableFileSize(long j) {
        if (j <= 0) {
            return "0";
        }
        try {
            double d = (double) j;
            int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
            return new DecimalFormat("#,##0.#").format(d / Math.pow(1024.0d, (double) log10)) + " " + new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"}[log10];
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }
}
