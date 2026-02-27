package com.yanzhenjie.andserver.view;

import org.apache.httpcore.Header;
import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.message.HeaderGroup;

public class View {
    private HeaderGroup mHeaderGroup = new HeaderGroup();
    private int mHttpCode;
    private HttpEntity mHttpEntity;

    public View(int i, HttpEntity httpEntity) {
        this.mHttpCode = i;
        this.mHttpEntity = httpEntity;
    }

    public int getHttpCode() {
        return this.mHttpCode;
    }

    public Header[] getHeaders() {
        return this.mHeaderGroup.getAllHeaders();
    }

    public HttpEntity getHttpEntity() {
        return this.mHttpEntity;
    }
}
