package com.yanzhenjie.andserver;

import com.yanzhenjie.andserver.exception.resolver.ExceptionResolver;
import com.yanzhenjie.andserver.filter.Filter;
import com.yanzhenjie.andserver.interceptor.Interceptor;
import com.yanzhenjie.andserver.ssl.SSLSocketInitializer;
import com.yanzhenjie.andserver.util.Executors;
import com.yanzhenjie.andserver.website.WebSite;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import org.apache.httpcore.ExceptionLogger;
import org.apache.httpcore.config.ConnectionConfig;
import org.apache.httpcore.config.SocketConfig;
import org.apache.httpcore.impl.bootstrap.HttpServer;
import org.apache.httpcore.impl.bootstrap.ServerBootstrap;
import org.slf4j.Marker;

/* access modifiers changed from: package-private */
public final class Core implements Server {
    private boolean isRunning;
    private final ExceptionResolver mExceptionResolver;
    private final Filter mFilter;
    private HttpServer mHttpServer;
    private final InetAddress mInetAddress;
    private final Interceptor mInterceptor;
    private final Server.ServerListener mListener;
    private final int mPort;
    private final Map<String, RequestHandler> mRequestHandlerMap;
    private final SSLContext mSSLContext;
    private final SSLSocketInitializer mSSLSocketInitializer;
    private final int mTimeout;
    private final WebSite mWebSite;

    static Builder newBuilder() {
        return new Builder();
    }

    private Core(Builder builder) {
        this.mInetAddress = builder.mInetAddress;
        this.mPort = builder.mPort;
        this.mTimeout = builder.mTimeout;
        this.mSSLContext = builder.mSSLContext;
        this.mSSLSocketInitializer = builder.mSSLSocketInitializer;
        this.mInterceptor = builder.mInterceptor;
        this.mWebSite = builder.mWebSite;
        this.mRequestHandlerMap = builder.mRequestHandlerMap;
        this.mFilter = builder.mFilter;
        this.mExceptionResolver = builder.mExceptionResolver;
        this.mListener = builder.mListener;
    }

    @Override // com.yanzhenjie.andserver.Server
    public void startup() {
        if (!this.isRunning) {
            Executors.getInstance().submit(new Runnable() {
                /* class com.yanzhenjie.andserver.Core.AnonymousClass1 */

                public void run() {
                    DispatchRequestHandler dispatchRequestHandler = new DispatchRequestHandler();
                    dispatchRequestHandler.setInterceptor(Core.this.mInterceptor);
                    dispatchRequestHandler.setWebSite(Core.this.mWebSite);
                    if (Core.this.mRequestHandlerMap != null && Core.this.mRequestHandlerMap.size() > 0) {
                        for (Map.Entry entry : Core.this.mRequestHandlerMap.entrySet()) {
                            dispatchRequestHandler.registerRequestHandler((String) entry.getKey(), (RequestHandler) entry.getValue());
                        }
                    }
                    dispatchRequestHandler.setFilter(Core.this.mFilter);
                    dispatchRequestHandler.setExceptionResolver(Core.this.mExceptionResolver);
                    Core.this.mHttpServer = ServerBootstrap.bootstrap().setSocketConfig(SocketConfig.custom().setSoKeepAlive(true).setSoReuseAddress(true).setSoTimeout(Core.this.mTimeout).setTcpNoDelay(false).build()).setConnectionConfig(ConnectionConfig.custom().setBufferSize(4096).setCharset(Charset.defaultCharset()).build()).setLocalAddress(Core.this.mInetAddress).setListenerPort(Core.this.mPort).setSslContext(Core.this.mSSLContext).setSslSetupHandler(new SSLSocketInitializer.SSLSocketInitializerWrapper(Core.this.mSSLSocketInitializer)).setServerInfo("AndServer").registerHandler(Marker.ANY_MARKER, dispatchRequestHandler).setExceptionLogger(ExceptionLogger.STD_ERR).create();
                    try {
                        Core.this.isRunning = true;
                        Core.this.mHttpServer.start();
                        Executors.getInstance().post(new Runnable() {
                            /* class com.yanzhenjie.andserver.Core.AnonymousClass1.AnonymousClass1 */

                            public void run() {
                                if (Core.this.mListener != null) {
                                    Core.this.mListener.onStarted();
                                }
                            }
                        });
                        Runtime.getRuntime().addShutdownHook(new Thread() {
                            /* class com.yanzhenjie.andserver.Core.AnonymousClass1.AnonymousClass2 */

                            public void run() {
                                Core.this.mHttpServer.shutdown(3, TimeUnit.SECONDS);
                            }
                        });
                    } catch (Exception e2) {
                        Executors.getInstance().post(new Runnable() {
                            /* class com.yanzhenjie.andserver.Core.AnonymousClass1.AnonymousClass3 */

                            public void run() {
                                if (Core.this.mListener != null) {
                                    Core.this.mListener.onError(e2);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override // com.yanzhenjie.andserver.Server
    public void shutdown() {
        if (this.isRunning) {
            Executors.getInstance().execute(new Runnable() {
                /* class com.yanzhenjie.andserver.Core.AnonymousClass2 */

                public void run() {
                    if (Core.this.mHttpServer != null) {
                        Core.this.mHttpServer.shutdown(3, TimeUnit.MINUTES);
                    }
                    Executors.getInstance().post(new Runnable() {
                        /* class com.yanzhenjie.andserver.Core.AnonymousClass2.AnonymousClass1 */

                        public void run() {
                            if (Core.this.mListener != null) {
                                Core.this.mListener.onStopped();
                            }
                        }
                    });
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static final class Builder implements Server.Builder {
        private ExceptionResolver mExceptionResolver;
        private Filter mFilter;
        private InetAddress mInetAddress;
        private Interceptor mInterceptor;
        private Server.ServerListener mListener;
        private int mPort;
        private Map<String, RequestHandler> mRequestHandlerMap;
        private SSLContext mSSLContext;
        private SSLSocketInitializer mSSLSocketInitializer;
        private int mTimeout;
        private WebSite mWebSite;

        private Builder() {
            this.mRequestHandlerMap = new LinkedHashMap();
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server.Builder inetAddress(InetAddress inetAddress) {
            this.mInetAddress = inetAddress;
            return this;
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server.Builder port(int i) {
            this.mPort = i;
            return this;
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server.Builder timeout(int i, TimeUnit timeUnit) {
            this.mTimeout = (int) Math.min(timeUnit.toMillis((long) i), 2147483647L);
            return this;
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server.Builder registerHandler(String str, RequestHandler requestHandler) {
            this.mRequestHandlerMap.put(str, requestHandler);
            return this;
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server.Builder filter(Filter filter) {
            this.mFilter = filter;
            return this;
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server.Builder website(WebSite webSite) {
            this.mWebSite = webSite;
            return this;
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server.Builder listener(Server.ServerListener serverListener) {
            this.mListener = serverListener;
            return this;
        }

        @Override // com.yanzhenjie.andserver.Server.Builder
        public Server build() {
            return new Core(this);
        }
    }
}
