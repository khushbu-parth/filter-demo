package com.yanzhenjie.andserver.protocol;

import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;

public interface ETag {
    String getETag(HttpRequest httpRequest) throws HttpException, IOException;
}
