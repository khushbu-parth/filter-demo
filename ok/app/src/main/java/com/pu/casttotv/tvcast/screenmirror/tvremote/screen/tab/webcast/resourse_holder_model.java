package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.util.Log;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/* loaded from: classes4.dex */
public class resourse_holder_model {
    private ArrayList<downloadable_resource_model> image_files;
    private String page_title;
    private ArrayList<downloadable_resource_model> video_files;
    private ArrayList<String> video_types = new ArrayList<>();
    private ArrayList<String> image_types = new ArrayList<>();
    private ArrayList<downloadable_resource_model> audio_files = new ArrayList<>();

    public resourse_holder_model() {
        new ArrayList();
        this.image_files = new ArrayList<>();
        this.video_files = new ArrayList<>();
        this.page_title = "";
        init_arraylists();
    }

    private void init_arraylists() {
        this.video_types.add("mp4");
        this.video_types.add("wmv");
        this.video_types.add("avi");
        this.image_types.add("png");
        this.image_types.add("jpg");
        this.image_types.add("gif");
        this.image_types.add("webp");
    }

    public void add_Video(String str, String str2, String str3, String str4, String str5) {
        add_video_files(new downloadable_resource_model(str4, str3, file_type.VIDEO, str));
    }

    public void add_Image(String str, String str2, String str3, String str4, String str5) {
        add_image_files(new downloadable_resource_model(str4, str3, file_type.IMAGE, str));
    }

    public void add_Audio(String str, String str2, String str3, String str4, String str5) {
        add_audio_file(new downloadable_resource_model(str4, str3, file_type.AUDIO, str));
    }

    public void add_audio_file(downloadable_resource_model downloadable_resource_modelVar) {
        Iterator<downloadable_resource_model> it = this.audio_files.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (it.next().equals(downloadable_resource_modelVar)) {
                z = true;
            }
        }
        if (!z) {
            this.audio_files.add(downloadable_resource_modelVar);
        }
    }

    public void add_video_files(downloadable_resource_model downloadable_resource_modelVar) {
        if (downloadable_resource_modelVar.getURL() == null || downloadable_resource_modelVar.getURL().startsWith("blob")) {
            return;
        }
        try {
            Iterator<downloadable_resource_model> it = this.video_files.iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (it.next().getURL().equals(downloadable_resource_modelVar.getURL())) {
                    z = true;
                }
            }
            if (z) {
                return;
            }
            this.video_files.add(downloadable_resource_modelVar);
        } catch (Exception unused) {
        }
    }

    public void add_image_files(downloadable_resource_model downloadable_resource_modelVar) {
        boolean z = false;
        try {
            Iterator<downloadable_resource_model> it = this.image_files.iterator();
            while (it.hasNext()) {
                downloadable_resource_model next = it.next();
                if (next.getURL() != null && next.getURL().equals(downloadable_resource_modelVar.getURL())) {
                    z = true;
                }
            }
        } catch (ConcurrentModificationException unused) {
        }
        if (!z) {
            this.image_files.add(downloadable_resource_modelVar);
        }
    }

    public ArrayList<downloadable_resource_model> getVideo_files() {
        try {
            Iterator<downloadable_resource_model> it = this.video_files.iterator();
            while (it.hasNext() && this.page_title != null) {
                it.next().setTitle(this.page_title);
                Log.e("##TAG", "getVideo_files 2 : " + video_files.iterator());
            }
            ArrayList<downloadable_resource_model> arrayList = this.video_files;
            return arrayList != null ? arrayList : new ArrayList<>();
        } catch (Exception e2) {
            Log.e("##TAG", "getVideo_  files: " + e2.getMessage());
            e2.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void setVideo_files(ArrayList<downloadable_resource_model> arrayList) {
        this.video_files = arrayList;
    }

    public ArrayList<downloadable_resource_model> getAudio_files() {
        try {
            ArrayList<downloadable_resource_model> arrayList = this.audio_files;
            if (arrayList != null && arrayList.size() > 0) {
                return this.audio_files;
            }
            return new ArrayList<>();
        } catch (Exception e2) {
            e2.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<downloadable_resource_model> getImage_files() {
        ArrayList<downloadable_resource_model> arrayList = this.image_files;
        if (arrayList != null && arrayList.size() > 0) {
            return this.image_files;
        }
        return new ArrayList<>();
    }

    public void setPage_title(String str) {
        this.page_title = str;
    }
}
