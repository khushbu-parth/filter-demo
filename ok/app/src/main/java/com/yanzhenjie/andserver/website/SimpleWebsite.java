package com.yanzhenjie.andserver.website;

import com.yanzhenjie.andserver.view.View;
import java.io.File;
import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

public abstract class SimpleWebsite implements WebSite {
    /* access modifiers changed from: protected */
    public abstract View handle(HttpRequest httpRequest) throws HttpException, IOException;

    /* access modifiers changed from: protected */
    public String addStartSlash(String str) {
        String str2 = File.separator;
        if (str.startsWith(str2)) {
            return str;
        }
        return str2 + str;
    }

    /* access modifiers changed from: protected */
    public String addEndSlash(String str) {
        String str2 = File.separator;
        if (str.endsWith(str2)) {
            return str;
        }
        return str + str2;
    }

    /* access modifiers changed from: protected */
    public String trimStartSlash(String str) {
        while (str.startsWith(File.separator)) {
            str = str.substring(1);
        }
        return str;
    }

    /* access modifiers changed from: protected */
    public String trimEndSlash(String str) {
        while (str.endsWith(File.separator)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override // com.yanzhenjie.andserver.RequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        View handle = handle(httpRequest, httpResponse);
        httpResponse.setStatusCode(handle.getHttpCode());
        httpResponse.setEntity(handle.getHttpEntity());
        httpResponse.setHeaders(handle.getHeaders());
    }

    /* access modifiers changed from: protected */
    public View handle(HttpRequest httpRequest, HttpResponse httpResponse) throws HttpException, IOException {
        return handle(httpRequest);
    }
}
