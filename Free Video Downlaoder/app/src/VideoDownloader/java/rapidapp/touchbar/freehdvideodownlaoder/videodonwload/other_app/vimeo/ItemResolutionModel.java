package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.vimeo;

public class ItemResolutionModel {
    private String http_link;
    private String idVideo;
    private String resol;
    private String videoname;

    public ItemResolutionModel(String resolution, String link, String idVideo, String name) {
        this.resol = resolution;
        this.http_link = link;
        this.idVideo = idVideo;
        this.videoname = name;
    }

    public ItemResolutionModel(String link, String nameVideo) {
        this.http_link = link;
        this.videoname = nameVideo;
    }

    public String getResolution() {
        return this.resol;
    }

    public void setResolution(String resolution) {
        this.resol = resolution;
    }

    public String getLink() {
        return this.http_link;
    }

    public void setLink(String link) {
        this.http_link = link;
    }

    public String getIdVideo() {
        return this.idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getNameVideo() {
        return this.videoname;
    }

    public void setNameVideo(String nameVideo) {
        this.videoname = nameVideo;
    }
}