package com.yanzhenjie.andserver.website;

import com.yanzhenjie.andserver.RequestHandler;
import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.protocol.HttpContext;

public interface WebSite extends RequestHandler {
    boolean intercept(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException;
}
