package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class ItemYoutube {
    @SerializedName("id")
    private IdModelTube id;
    @SerializedName("snippet")
    private SnippetTube snippet;

    public ItemYoutube() {
    }

    public ItemYoutube(SnippetTube snippetTube, IdModelTube idModelTube) {
        this.snippet = snippetTube;
        this.id = idModelTube;
    }

    public SnippetTube getSnippet() {
        return this.snippet;
    }

    public void setSnippet(SnippetTube snippetTube) {
        this.snippet = snippetTube;
    }

    public IdModelTube getId() {
        return this.id;
    }

    public void setId(IdModelTube idModelTube) {
        this.id = idModelTube;
    }
}
