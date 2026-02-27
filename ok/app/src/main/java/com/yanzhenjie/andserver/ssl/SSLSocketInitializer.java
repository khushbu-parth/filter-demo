package com.yanzhenjie.andserver.ssl;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;
import org.apache.httpcore.impl.bootstrap.SSLServerSetupHandler;

public interface SSLSocketInitializer {
    void onCreated(SSLServerSocket sSLServerSocket) throws SSLException;

    public static final class SSLSocketInitializerWrapper implements SSLServerSetupHandler {
        private SSLSocketInitializer mSSLSocketInitializer;

        public SSLSocketInitializerWrapper(SSLSocketInitializer sSLSocketInitializer) {
            this.mSSLSocketInitializer = sSLSocketInitializer;
        }

        @Override // org.apache.httpcore.impl.bootstrap.SSLServerSetupHandler
        public void initialize(SSLServerSocket sSLServerSocket) throws SSLException {
            SSLSocketInitializer sSLSocketInitializer = this.mSSLSocketInitializer;
            if (sSLSocketInitializer != null) {
                sSLSocketInitializer.onCreated(sSLServerSocket);
            }
        }
    }
}
