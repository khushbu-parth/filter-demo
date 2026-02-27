package com.yanzhenjie.andserver.filter;

import com.yanzhenjie.andserver.RequestHandler;
import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

public interface Filter {
    void doFilter(RequestHandler requestHandler, HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException;
}
