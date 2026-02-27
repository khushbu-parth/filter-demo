package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model;

import java.util.List;

public class PlayerResponse {
    private Captions captions;
    private PlayabilityStatus playabilityStatus;
    private String playerJs;
    private StreamingData streamingData;
    private YoutubeMeta videoDetails;

    public void setPlayerJs(String str) {
        this.playerJs = str;
    }

    public String getPlayerJs() {
        if (this.playerJs.startsWith("http") && this.playerJs.contains("youtube.com")) {
            return this.playerJs.replace("\\", "");
        }
        return "https://www.youtube.com" + this.playerJs.replace("\\", "");
    }

    public void setCaptions(Captions captions2) {
        this.captions = captions2;
    }

    public Captions getCaptions() {
        return this.captions;
    }

    public void setPlayabilityStatus(PlayabilityStatus playabilityStatus2) {
        this.playabilityStatus = playabilityStatus2;
    }

    public PlayabilityStatus getPlayabilityStatus() {
        return this.playabilityStatus;
    }

    public void setStreamingData(StreamingData streamingData2) {
        this.streamingData = streamingData2;
    }

    public StreamingData getStreamingData() {
        return this.streamingData;
    }

    public void setVideoDetails(YoutubeMeta youtubeMeta) {
        this.videoDetails = youtubeMeta;
    }

    public YoutubeMeta getVideoDetails() {
        return this.videoDetails;
    }

    public class Captions {
        private PlayerCaptionsTracklistRenderer playerCaptionsTracklistRenderer;

        public Captions() {
        }

        public void setPlayerCaptionsTracklistRenderer(PlayerCaptionsTracklistRenderer playerCaptionsTracklistRenderer2) {
            this.playerCaptionsTracklistRenderer = playerCaptionsTracklistRenderer2;
        }

        public PlayerCaptionsTracklistRenderer getPlayerCaptionsTracklistRenderer() {
            return this.playerCaptionsTracklistRenderer;
        }

        public class PlayerCaptionsTracklistRenderer {
            private List<YTSubtitles> captionTracks;

            public PlayerCaptionsTracklistRenderer() {
            }

            public void setCaptionTracks(List<YTSubtitles> list) {
                this.captionTracks = list;
            }

            public List<YTSubtitles> getCaptionTracks() {
                return this.captionTracks;
            }
        }
    }

    public class PlayabilityStatus {
        private boolean playableInEmbed;
        private String status;

        public PlayabilityStatus() {
        }

        public void setStatus(String str) {
            this.status = str;
        }

        public String getStatus() {
            return this.status;
        }

        public void setPlayableInEmbed(boolean z) {
            this.playableInEmbed = z;
        }

        public boolean isPlayableInEmbed() {
            return this.playableInEmbed;
        }
    }
}
