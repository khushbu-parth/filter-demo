package com.yanzhenjie.andserver;

import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.exception.BaseException;
import com.yanzhenjie.andserver.exception.MethodNotSupported;
import com.yanzhenjie.andserver.exception.NotFoundException;
import com.yanzhenjie.andserver.exception.resolver.ExceptionResolver;
import com.yanzhenjie.andserver.exception.resolver.SimpleExceptionResolver;
import com.yanzhenjie.andserver.filter.Filter;
import com.yanzhenjie.andserver.interceptor.Interceptor;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import com.yanzhenjie.andserver.website.WebSite;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;
import org.apache.httpcore.protocol.HttpRequestHandler;

class DispatchRequestHandler implements HttpRequestHandler {
    private static ExceptionResolver sDefaultExceptionResolver = new SimpleExceptionResolver();
    private ExceptionResolver mExceptionResolver = sDefaultExceptionResolver;
    private Filter mFilter;
    private Interceptor mInterceptor;
    private Map<String, RequestHandler> mRequestHandlerMapper = new LinkedHashMap();
    private WebSite mWebSite;

    DispatchRequestHandler() {
    }

    /* access modifiers changed from: package-private */
    public void setInterceptor(Interceptor interceptor) {
        this.mInterceptor = interceptor;
    }

    /* access modifiers changed from: package-private */
    public void setWebSite(WebSite webSite) {
        this.mWebSite = webSite;
    }

    /* access modifiers changed from: package-private */
    public void registerRequestHandler(String str, RequestHandler requestHandler) {
        this.mRequestHandlerMapper.put(str, requestHandler);
    }

    /* access modifiers changed from: package-private */
    public void setFilter(Filter filter) {
        this.mFilter = filter;
    }

    /* access modifiers changed from: package-private */
    public void setExceptionResolver(ExceptionResolver exceptionResolver) {
        this.mExceptionResolver = exceptionResolver;
    }

    @Override // org.apache.httpcore.protocol.HttpRequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        try {
            Interceptor interceptor = this.mInterceptor;
            if (interceptor == null || !interceptor.onBeforeExecute(httpRequest, httpResponse, httpContext)) {
                RequestHandler requestHandler = getRequestHandler(httpRequest, httpContext);
                if (requestHandler != null) {
                    handleRequest(requestHandler, httpRequest, httpResponse, httpContext);
                    Interceptor interceptor2 = this.mInterceptor;
                    if (interceptor2 != null) {
                        interceptor2.onAfterExecute(httpRequest, httpResponse, httpContext);
                        return;
                    }
                    return;
                }
                throw new NotFoundException(HttpRequestParser.getRequestPath(httpRequest));
            }
        } catch (Exception e2) {
            try {
                this.mExceptionResolver.resolveException(e2, httpRequest, httpResponse, httpContext);
            } catch (Exception unused) {
                sDefaultExceptionResolver.resolveException(e2, httpRequest, httpResponse, httpContext);
            }
        }
    }

    private void handleRequest(RequestHandler requestHandler, HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        verifyHandler(httpRequest, requestHandler);
        Filter filter = this.mFilter;
        if (filter != null) {
            filter.doFilter(requestHandler, httpRequest, httpResponse, httpContext);
        } else {
            requestHandler.handle(httpRequest, httpResponse, httpContext);
        }
    }

    private RequestHandler getRequestHandler(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String requestPath = HttpRequestParser.getRequestPath(httpRequest);
        WebSite webSite = this.mWebSite;
        if (webSite == null || !webSite.intercept(httpRequest, httpContext)) {
            return this.mRequestHandlerMapper.get(requestPath);
        }
        return this.mWebSite;
    }

    private void verifyHandler(HttpRequest httpRequest, RequestHandler requestHandler) throws BaseException {
        RequestMethod reverse = RequestMethod.reverse(httpRequest.getRequestLine().getMethod());
        try {
            RequestMapping requestMapping = (RequestMapping) requestHandler.getClass().getMethod("handle", HttpRequest.class, HttpResponse.class, HttpContext.class).getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                return;
            }
            if (!Arrays.asList(requestMapping.method()).contains(reverse)) {
                throw new MethodNotSupported(reverse);
            }
        } catch (NoSuchMethodException unused) {
        }
    }
}
