package com.yanzhenjie.andserver;

import com.yanzhenjie.andserver.Server;

public class AndServer {
    public static Server.Builder serverBuilder() {
        return Core.newBuilder();
    }
}
