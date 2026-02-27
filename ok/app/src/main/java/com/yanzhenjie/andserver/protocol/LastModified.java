package com.yanzhenjie.andserver.protocol;

import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;

public interface LastModified {
    long getLastModified(HttpRequest httpRequest) throws HttpException, IOException;
}
