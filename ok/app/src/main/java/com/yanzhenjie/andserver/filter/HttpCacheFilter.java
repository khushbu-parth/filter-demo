package com.yanzhenjie.andserver.filter;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.protocol.ETag;
import com.yanzhenjie.andserver.protocol.LastModified;
import com.yanzhenjie.andserver.util.DateUtils;
import com.yanzhenjie.andserver.util.DigestUtils;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import java.io.IOException;
import org.apache.httpcore.Header;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

public class HttpCacheFilter implements Filter {

    @Override // com.yanzhenjie.andserver.filter.Filter
    public void doFilter(RequestHandler requestHandler, HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        boolean z = requestHandler instanceof LastModified;
        long lastModified = z ? ((LastModified) requestHandler).getLastModified(httpRequest) : -1;
        String str = null;
        boolean z2 = requestHandler instanceof ETag;
        if (z2) {
            str = ((ETag) requestHandler).getETag(httpRequest);
        }
        Header firstHeader = httpRequest.getFirstHeader("If-Unmodified-Since");
        if (!z || firstHeader == null || validateIfUnmodifiedSince(httpRequest, lastModified)) {
            Header firstHeader2 = httpRequest.getFirstHeader("If-Modified-Since");
            Header firstHeader3 = httpRequest.getFirstHeader("If-None-Match");
            if (z && z2 && firstHeader2 != null && firstHeader3 != null && validateIfModifiedSince(httpRequest, lastModified) && validateIfNoneMatch(httpRequest, str)) {
                httpResponse.setStatusCode(304);
                httpResponse.addHeader("Cache-Control", "public");
                httpResponse.addHeader("Last-Modified", generateLastModified(lastModified));
                httpResponse.addHeader("ETag", generateETag(str));
            } else if (!z || firstHeader2 == null || !validateIfModifiedSince(httpRequest, lastModified)) {
                requestHandler.handle(httpRequest, httpResponse, httpContext);
                if (z && lastModified >= 0) {
                    httpResponse.addHeader("Last-Modified", generateLastModified(lastModified));
                }
                if (z2 && str != null) {
                    httpResponse.addHeader("ETag", generateETag(str));
                }
                if (z) {
                    httpResponse.addHeader("Cache-Control", "public");
                }
            } else {
                httpResponse.setStatusCode(304);
                httpResponse.addHeader("Cache-Control", "public");
                httpResponse.addHeader("Last-Modified", generateLastModified(lastModified));
            }
        } else {
            httpResponse.setStatusCode(412);
        }
    }

    /* access modifiers changed from: protected */
    public String generateETag(String str) throws IOException {
        return "\"0" + DigestUtils.md5DigestAsHex(str) + '\"';
    }

    /* access modifiers changed from: protected */
    public boolean validateIfNoneMatch(HttpRequest httpRequest, String str) {
        Header firstHeader = httpRequest.getFirstHeader("If-None-Match");
        if (str == null && firstHeader == null) {
            return true;
        }
        if (str == null || firstHeader == null) {
            return false;
        }
        return str.equalsIgnoreCase(firstHeader.getValue());
    }

    /* access modifiers changed from: protected */
    public String generateLastModified(long j) throws IOException {
        return DateUtils.formatMillisToGMT(j);
    }

    /* access modifiers changed from: protected */
    public boolean validateIfModifiedSince(HttpRequest httpRequest, long j) {
        if (j < 0) {
            return false;
        }
        long parseDateHeader = HttpRequestParser.parseDateHeader(httpRequest, "If-Modified-Since");
        if (parseDateHeader >= 0 && parseDateHeader >= (j / 1000) * 1000) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean validateIfUnmodifiedSince(HttpRequest httpRequest, long j) {
        if (j < 0) {
            return false;
        }
        long parseDateHeader = HttpRequestParser.parseDateHeader(httpRequest, "If-Unmodified-Since");
        if (parseDateHeader >= 0 && parseDateHeader < (j / 1000) * 1000) {
            return true;
        }
        return false;
    }
}
