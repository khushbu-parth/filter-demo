package com.yanzhenjie.andserver;

import com.yanzhenjie.andserver.filter.Filter;
import com.yanzhenjie.andserver.website.WebSite;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public interface Server {

    public interface Builder {
        Server build();

        Builder filter(Filter filter);

        Builder inetAddress(InetAddress inetAddress);

        Builder listener(ServerListener serverListener);

        Builder port(int i);

        Builder registerHandler(String str, RequestHandler requestHandler);

        Builder timeout(int i, TimeUnit timeUnit);

        Builder website(WebSite webSite);
    }

    public interface ServerListener {
        void onError(Exception exc);

        void onStarted();

        void onStopped();
    }

    void shutdown();

    void startup();
}
