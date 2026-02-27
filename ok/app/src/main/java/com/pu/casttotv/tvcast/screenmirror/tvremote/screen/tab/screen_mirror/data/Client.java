package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data;

import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class Client {
    private volatile boolean isClosing;
    private volatile boolean isSending;
    private final Socket mClientSocket;
    private final ExecutorService mClientThreadPool = Executors.newFixedThreadPool(2);
    private final OutputStreamWriter mOtputStreamWriter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Client(Socket socket) throws IOException {
        this.mClientSocket = socket;
        this.mOtputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSocket() {
        this.isClosing = true;
        MyApplication.getAppData().getClientQueue().remove(this);
        MyApplication.getScreenMirrorViewModel().setClients(MyApplication.getAppData().getClientQueue().size());
        try {
            this.mClientThreadPool.shutdownNow();
            this.mOtputStreamWriter.close();
            this.mClientSocket.close();
        } catch (IOException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendClientData(final int i, final byte[] bArr, final boolean z) {
        if (this.isClosing) {
            return;
        }
        if (z && bArr == null) {
            closeSocket();
        }
        if (this.isSending) {
            return;
        }
        this.isSending = true;
        try {
            this.mClientThreadPool.execute(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.data.Client.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Client.this.mClientThreadPool.submit(new Callable<Object>() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.data.Client.1.1
                            @Override // java.util.concurrent.Callable
                            public Object call() throws Exception {
                                if (i == 1) {
                                    Client.this.sendHeader();
                                }
                                if (i == 2) {
                                    Client.this.sendImage(bArr);
                                    return null;
                                }
                                return null;
                            }
                        }).get(MyApplication.getAppPreference().getClientTimeout(), TimeUnit.MILLISECONDS);
                        if (z) {
                            Client.this.closeSocket();
                        }
                        Client.this.isSending = false;
                    } catch (Exception unused) {
                        Client.this.closeSocket();
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendHeader() throws IOException {
        this.mOtputStreamWriter.write("HTTP/1.1 200 OK\r\n");
        this.mOtputStreamWriter.write("Content-Type: multipart/x-mixed-replace; boundary=y5exa7CYPPqoASFONZJMz4Ky\r\n");
        this.mOtputStreamWriter.write("Cache-Control: no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0\r\n");
        this.mOtputStreamWriter.write("Pragma: no-cache\r\n");
        this.mOtputStreamWriter.write("Connection: keep-alive\r\n");
        this.mOtputStreamWriter.write("\r\n");
        this.mOtputStreamWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendImage(byte[] bArr) throws IOException {
        this.mOtputStreamWriter.write("--y5exa7CYPPqoASFONZJMz4Ky\r\n");
        this.mOtputStreamWriter.write("Content-Type: image/jpeg\r\n");
        OutputStreamWriter outputStreamWriter = this.mOtputStreamWriter;
        outputStreamWriter.write("Content-Length: " + bArr.length + "\r\n");
        this.mOtputStreamWriter.write("\r\n");
        this.mOtputStreamWriter.flush();
        this.mClientSocket.getOutputStream().write(bArr);
        this.mClientSocket.getOutputStream().flush();
        this.mOtputStreamWriter.write("\r\n");
        this.mOtputStreamWriter.flush();
    }
}
