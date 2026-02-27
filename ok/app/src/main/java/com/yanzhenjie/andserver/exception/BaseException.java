package com.yanzhenjie.andserver.exception;

import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.StringEntity;

public class BaseException extends HttpException {
    private HttpEntity mHttpBody;
    private int mHttpCode;

    public BaseException() {
        this(500, "Unknown exception occurred on server.");
    }

    public BaseException(int i, String str) {
        super(str);
        this.mHttpCode = i;
        this.mHttpBody = new StringEntity(str, ContentType.TEXT_PLAIN);
    }

    public int getHttpCode() {
        return this.mHttpCode;
    }

    public HttpEntity getHttpBody() {
        return this.mHttpBody;
    }
}
