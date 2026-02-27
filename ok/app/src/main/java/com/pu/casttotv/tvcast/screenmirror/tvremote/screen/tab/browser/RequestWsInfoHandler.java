package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import com.yanzhenjie.andserver.RequestHandler;
import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

/* loaded from: classes4.dex */
public class RequestWsInfoHandler implements RequestHandler {
    @Override // com.yanzhenjie.andserver.RequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        StringEntity stringEntity = new StringEntity("" + WebServer.localIpText + ":" + WebServer.portWS, "utf-8");
        StringBuilder sb = new StringBuilder();
        sb.append("handle: ");
        sb.append(stringEntity);
        httpResponse.setEntity(stringEntity);
    }
}
