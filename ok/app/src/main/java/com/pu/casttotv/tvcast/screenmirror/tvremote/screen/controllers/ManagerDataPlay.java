package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers;

import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ItemYoutube;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.PhotoOnlineModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive.GoogleDriveItem;
import java.util.ArrayList;
import java.util.List;

public class ManagerDataPlay {
    private static ManagerDataPlay managerDataPlay;
    public int currentPosCast;
    private List<GoogleDriveItem> driveItemList;
    public Long duration = 0L;
    private List<AudioModel> listAudio;
    private ArrayList<MediaModel> listPhoto;
    public String pathCast = "";
    private List<PhotoOnlineModel> photoOnlineModelList;
    private int posSelected = 0;
    public String thumbCast = "";
    public String titleAudio = "";
    public String titleCast = "";
    private int type = 0;
    private List<ItemYoutube> youtubeModelList;

    public static ManagerDataPlay getInstance() {
        if (managerDataPlay == null) {
            managerDataPlay = new ManagerDataPlay();
        }
        return managerDataPlay;
    }

    public int getTypePlay() {
        return this.type;
    }

    public void setTypePlay(int i) {
        this.type = i;
    }

    public void setPosSelected(int i) {
        this.posSelected = i;
    }

    public int getPosSelected() {
        return this.posSelected;
    }

    public ArrayList<MediaModel> getListMedia() {
        return this.listPhoto;
    }

    public void setListMedia(ArrayList<MediaModel> arrayList) {
        ArrayList<MediaModel> arrayList2 = new ArrayList<>();
        this.listPhoto = arrayList2;
        arrayList2.addAll(arrayList);
    }

    public List<AudioModel> getListAudio() {
        return this.listAudio;
    }

    public void setListAudio(ArrayList<AudioModel> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        this.listAudio = arrayList2;
        arrayList2.addAll(arrayList);
    }

    public List<ItemYoutube> getListYoutube() {
        return this.youtubeModelList;
    }

    public List<PhotoOnlineModel> getListPhotoOnl() {
        return this.photoOnlineModelList;
    }

    public void setListPhotoOnl(ArrayList<PhotoOnlineModel> arrayList) {
        this.photoOnlineModelList = new ArrayList();
        if (arrayList != null && arrayList.size() > 0) {
            this.photoOnlineModelList.addAll(arrayList);
        }
    }

    public List<GoogleDriveItem> getListDriver() {
        return this.driveItemList;
    }

    public void setListDriver(List<GoogleDriveItem> list) {
        this.driveItemList = new ArrayList();
        if (list != null && list.size() > 0) {
            this.driveItemList.addAll(list);
        }
    }
}
