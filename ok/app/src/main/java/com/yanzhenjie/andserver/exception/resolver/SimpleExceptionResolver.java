package com.yanzhenjie.andserver.exception.resolver;

import com.yanzhenjie.andserver.exception.BaseException;
import com.yanzhenjie.andserver.view.View;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

public class SimpleExceptionResolver implements ExceptionResolver {
    @Override // com.yanzhenjie.andserver.exception.resolver.ExceptionResolver
    public final void resolveException(Exception exc, HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) {
        View resolveException = resolveException(exc, httpRequest, httpResponse);
        httpResponse.setStatusCode(resolveException.getHttpCode());
        httpResponse.setEntity(resolveException.getHttpEntity());
        httpResponse.setHeaders(resolveException.getHeaders());
    }

    public View resolveException(Exception exc, HttpRequest httpRequest, HttpResponse httpResponse) {
        return resolveException(exc);
    }

    /* access modifiers changed from: protected */
    public View resolveException(Exception exc) {
        if (exc instanceof BaseException) {
            BaseException baseException = (BaseException) exc;
            return new View(baseException.getHttpCode(), baseException.getHttpBody());
        }
        return new View(500, new StringEntity(String.format("Server error occurred:\n%1$s", exc.getMessage()), ContentType.TEXT_PLAIN));
    }
}
