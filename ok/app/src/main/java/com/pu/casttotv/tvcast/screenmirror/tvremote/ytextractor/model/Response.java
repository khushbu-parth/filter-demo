package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model;

public class Response {
    private Args args;
    private String playerJs;

    public void setPlayerJs(String str) {
        this.playerJs = str;
    }

    public String getPlayerJs() {
        if (this.playerJs.startsWith("http") && this.playerJs.contains("youtube.com")) {
            return this.playerJs.replace("\\", "");
        }
        return "https://www.youtube.com" + this.playerJs.replace("\\", "");
    }

    public Args getArgs() {
        return this.args;
    }

    public void setArgs(Args args2) {
        this.args = args2;
    }

    public class Args {
        private String adaptive_fmts;
        private String player_response;
        private String url_encoded_fmt_stream_map;

        public Args() {
        }

        public String getAdaptiveFmts() {
            return this.adaptive_fmts;
        }

        public void setAdaptiveFmts(String str) {
            this.adaptive_fmts = str;
        }

        public String getPlayerResponse() {
            return this.player_response;
        }

        public void setPlayerResponse(String str) {
            this.player_response = str;
        }

        public String getUrlEncodedFmtStreamMap() {
            return this.url_encoded_fmt_stream_map;
        }

        public void setUrlEncodedFmtStreamMap(String str) {
            this.url_encoded_fmt_stream_map = str;
        }
    }
}
