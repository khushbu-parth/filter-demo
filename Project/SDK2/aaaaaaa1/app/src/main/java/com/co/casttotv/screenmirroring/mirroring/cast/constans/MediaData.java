package com.co.casttotv.screenmirroring.mirroring.cast.constans;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;
import java.util.HashSet;

public class MediaData {
    public static final String TYPE_MEDIA_KEY = "MEDIA_TYPE_KEY";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_PHOTO = "photo";
    public static final String TYPE_VIDEO = "video";

    private static final ArrayList<String> imageFolderList = new ArrayList<>();
    private static final ArrayList<MediaModel> imageList = new ArrayList<>();

    private static final ArrayList<String> videoFolderList = new ArrayList<>();
    private static final ArrayList<MediaModel> videoList = new ArrayList<>();

    private static final ArrayList<MediaModel> audioList = new ArrayList<>();
    private static final ArrayList<String> audioAlbumList = new ArrayList<>();
    private static final ArrayList<String> audioArtistList = new ArrayList<>();

    /*For Images*/
    private static ArrayList<MediaModel> getAllImages(Context mContext) {
        imageFolderList.clear();
        imageList.clear();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
        };

        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME + " ASC";
        try (Cursor cursor = mContext.getApplicationContext().getContentResolver().query(collection, projection, null, null, sortOrder
        )) {
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

            while (cursor.moveToNext()) {
                String data = cursor.getString(dataColumn);
                String name = cursor.getString(nameColumn);

                MediaModel model = new MediaModel();
                model.setPath(data);
                model.setDisplayName(name);

                String[] strings = data.split("/");
                imageFolderList.add(strings[strings.length - 2]);
                model.setAlbumName(strings[strings.length - 2]);
                imageList.add(model);
            }
        }

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(imageFolderList);
        imageFolderList.clear();
        imageFolderList.addAll(hashSet);
        imageFolderList.add(0, "All Photos");
        return imageList;
    }

    public static ArrayList<MediaModel> getImagesFrom(Context mContext, String bucket) {
        ArrayList<MediaModel> arrayList = new ArrayList<>();
        if (imageList.size() == 0) {
            getAllImages(mContext);
        }

        if (bucket.equalsIgnoreCase("All Photos")) {
            return imageList;
        }

        for (MediaModel model : imageList) {
            if (model.getAlbumName().equals(bucket)) arrayList.add(model);
        }

        return arrayList;
    }

    public static ArrayList<String> getImageFolder(Context context) {
        if (imageFolderList.size() == 0) getAllImages(context);
        return imageFolderList;
    }


    /*For Videos*/
    private static ArrayList<MediaModel> getAllVideos(Context mContext) {
        videoFolderList.clear();
        videoList.clear();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
        };

        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";
        try (Cursor cursor = mContext.getApplicationContext().getContentResolver().query(collection, projection, null, null, sortOrder
        )) {
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            while (cursor.moveToNext()) {
                String data = cursor.getString(dataColumn);
                String name = cursor.getString(nameColumn);
                long j = cursor.getLong(durationColumn);

                MediaModel model = new MediaModel();
                model.setPath(data);
                model.setDisplayName(name);
                model.setDuration(j);

                String[] strings = data.split("/");
                videoFolderList.add(strings[strings.length - 2]);
                model.setAlbumName(strings[strings.length - 2]);
                videoList.add(model);
            }
        }

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(videoFolderList);
        videoFolderList.clear();
        videoFolderList.addAll(hashSet);
        videoFolderList.add(0, "All Videos");
        return videoList;
    }

    public static ArrayList<MediaModel> getVideosFrom(Context mContext, String bucket) {
        ArrayList<MediaModel> arrayList = new ArrayList<>();
        if (videoList.size() == 0) {
            getAllVideos(mContext);
        }

        if (bucket.equalsIgnoreCase("All Videos")) {
            return videoList;
        }

        for (MediaModel model : videoList) {
            if (model.getAlbumName().equals(bucket)) arrayList.add(model);
        }

        return arrayList;
    }

    public static ArrayList<String> getVideoFolder(Context context) {
        if (videoFolderList.size() == 0) getAllVideos(context);
        return videoFolderList;
    }


    /*For Audios*/
    public static ArrayList<MediaModel> getAllAudioFiles(Context context) {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION};

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = context.getContentResolver().query(collection, projection, selection, null, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {
                String path = cursor.getString(dataColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);
                String title = cursor.getString(titleColumn);
                long duration = cursor.getLong(durationColumn);

                MediaModel model = new MediaModel();
                model.setPath(path);
                model.setDisplayName(title);
                model.setAlbumName(album);
                model.setArtistName(artist);
                model.setDuration(duration);

                audioList.add(model);
                audioArtistList.add(artist);
                audioAlbumList.add(album);

            } while (cursor.moveToNext());
            cursor.close();
        }

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(audioArtistList);
        audioArtistList.clear();
        audioArtistList.addAll(hashSet);

        HashSet<String> hashSet1 = new HashSet<String>();
        hashSet1.addAll(audioAlbumList);
        audioAlbumList.clear();
        audioAlbumList.addAll(hashSet1);

        return audioList;
    }

    public static ArrayList<String> getAudioAlbums(Context context) {
        if (audioAlbumList.size() == 0) getAllAudioFiles(context);
        return audioAlbumList;
    }

    public static ArrayList<String> getAudioArtists(Context context) {
        if (audioArtistList.size() == 0) getAllAudioFiles(context);
        return audioArtistList;
    }

    public static ArrayList<MediaModel> getAudiosFrom(Context mContext, String bucket, Boolean isAlbum) {
        ArrayList<MediaModel> arrayList = new ArrayList<>();
        if (audioList.size() == 0) {
            getAllAudioFiles(mContext);
        }

        for (MediaModel model : audioList) {
            String s = isAlbum ? model.getAlbumName() : model.getArtistName();
            if (s.equals(bucket)) arrayList.add(model);
        }
        return arrayList;
    }
}
