package com.yanzhenjie.andserver.exception.resolver;

import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

public interface ExceptionResolver {
    void resolveException(Exception exc, HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext);
}
