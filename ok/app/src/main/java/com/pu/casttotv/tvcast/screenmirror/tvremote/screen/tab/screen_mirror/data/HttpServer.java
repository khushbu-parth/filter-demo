package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data;

import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/* loaded from: classes4.dex */
public final class HttpServer {
    private volatile boolean isPinEnabled;
    private ImageDispatcher mImageDispatcher;
    private ServerSocket mServerSocket;
    private final Object mLock = new Object();
    private String mCurrentPinUri = "/?pin=";
    private String mCurrentStreamAddress = "/screen_stream.mjpeg";
    private HttpServerThread mHttpServerThread = new HttpServerThread();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class HttpServerThread extends Thread {
        HttpServerThread() {
            super(HttpServerThread.class.getSimpleName());
        }

        /* JADX WARN: Removed duplicated region for block: B:69:0x00a9 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:71:0x009d A[SYNTHETIC] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            try {
                Socket accept = null;
                String readLine = null;
                while (!isInterrupted()) {
                    synchronized (HttpServer.this.mLock) {
                        try {
                            try {
                                accept = HttpServer.this.mServerSocket.accept();
                                readLine = new BufferedReader(new InputStreamReader(accept.getInputStream(), "UTF8")).readLine();
                            } finally {
                            }
                        } catch (IOException unused) {
                        }
                        if (readLine != null && readLine.startsWith("GET")) {
                            String[] split = readLine.split(" ");
                            if (split.length >= 2) {
                                String str = split[1];
                                if (HttpServer.this.isPinEnabled) {
                                    if ("/".equals(str)) {
                                        sendPinRequestPage(accept, false);
                                    } else if (str.startsWith("/?pin=")) {
                                        if (str.equals(HttpServer.this.mCurrentPinUri)) {
                                            sendMainPage(accept, HttpServer.this.mCurrentStreamAddress);
                                        } else {
                                            sendPinRequestPage(accept, true);
                                        }
                                    } else if (!HttpServer.this.mCurrentStreamAddress.equals(str)) {
                                        HttpServer.this.mImageDispatcher.addClient(accept);
                                    } else if ("/favicon.ico".equals(str)) {
                                        sendFavicon(accept);
                                    } else {
                                        sendNotFound(accept);
                                    }
                                } else if ("/".equals(str)) {
                                    sendMainPage(accept, HttpServer.this.mCurrentStreamAddress);
                                } else if (!HttpServer.this.mCurrentStreamAddress.equals(str)) {
                                }
                            } else {
                                sendNotFound(accept);
                            }
                        }
                        sendNotFound(accept);
                    }
                }
            } catch (Exception e) {

            }
        }

        private void sendPinRequestPage(Socket socket, boolean z) throws IOException {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
            try {
                outputStreamWriter.write("HTTP/1.1 200 OK\r\n");
                outputStreamWriter.write("Content-Type: text/html\r\n");
                outputStreamWriter.write("Connection: close\r\n");
                outputStreamWriter.write("\r\n");
                outputStreamWriter.write(MyApplication.getAppData().getPinRequestHtml(z));
                outputStreamWriter.write("\r\n");
                outputStreamWriter.flush();
                outputStreamWriter.close();
            } catch (Throwable th) {
                try {
                    outputStreamWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        private void sendMainPage(Socket socket, String str) throws IOException {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
            try {
                outputStreamWriter.write("HTTP/1.1 200 OK\r\n");
                outputStreamWriter.write("Content-Type: text/html\r\n");
                outputStreamWriter.write("Connection: close\r\n");
                outputStreamWriter.write("\r\n");
                outputStreamWriter.write(MyApplication.getAppData().getIndexHtml(str));
                outputStreamWriter.write("\r\n");
                outputStreamWriter.flush();
                outputStreamWriter.close();
            } catch (Throwable th) {
                try {
                    outputStreamWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        private void sendFavicon(Socket socket) throws IOException {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
            try {
                outputStreamWriter.write("HTTP/1.1 200 OK\r\n");
                outputStreamWriter.write("Content-Type: image/png\r\n");
                outputStreamWriter.write("Connection: close\r\n");
                outputStreamWriter.write("\r\n");
                outputStreamWriter.flush();
                socket.getOutputStream().write(MyApplication.getAppData().getIcon());
                socket.getOutputStream().flush();
                outputStreamWriter.close();
            } catch (Throwable th) {
                try {
                    outputStreamWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        private void sendNotFound(Socket socket) throws IOException {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
            try {
                outputStreamWriter.write("HTTP/1.1 301 Moved Permanently\r\n");
                outputStreamWriter.write("Location: " + MyApplication.getAppData().getServerAddress() + "\r\n");
                outputStreamWriter.write("Connection: close\r\n");
                outputStreamWriter.write("\r\n");
                outputStreamWriter.flush();
                outputStreamWriter.close();
            } catch (Throwable th) {
                try {
                    outputStreamWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    public void start() {
        if (this.mHttpServerThread.isAlive()) {
            return;
        }
        this.mCurrentStreamAddress = "/screen_stream.mjpeg";
        this.mCurrentPinUri = "/?pin=";
        this.isPinEnabled = MyApplication.getAppPreference().isEnablePin();
        if (this.isPinEnabled) {
            String currentPin = MyApplication.getAppPreference().getCurrentPin();
            this.mCurrentPinUri = "/?pin=" + currentPin;
            this.mCurrentStreamAddress = getRandomStreamAddress(currentPin);
        }
        try {
            InetAddress ipAddress = MyApplication.getAppData().getIpAddress();
            if (ipAddress == null) {
                EventBus.getDefault().post(new BusMessages("MESSAGE_STATUS_HTTP_ERROR_NO_IP"));
                return;
            }
            ServerSocket serverSocket = new ServerSocket(MyApplication.getAppPreference().getSeverPort(), 4, ipAddress);
            this.mServerSocket = serverSocket;
            serverSocket.setSoTimeout(50);
            ImageDispatcher imageDispatcher = new ImageDispatcher();
            this.mImageDispatcher = imageDispatcher;
            imageDispatcher.start();
            this.mHttpServerThread.start();
            EventBus.getDefault().postSticky(new BusMessages("MESSAGE_STATUS_HTTP_OK"));
        } catch (BindException unused) {
            EventBus.getDefault().postSticky(new BusMessages("MESSAGE_STATUS_HTTP_ERROR_PORT_IN_USE"));
        } catch (IOException unused2) {
            EventBus.getDefault().post(new BusMessages("MESSAGE_STATUS_HTTP_ERROR_UNKNOWN"));
        }
    }

    public void stop(byte[] bArr) {
        try {
            if (!this.mHttpServerThread.isAlive()) {
                return;
            }
            this.mHttpServerThread.interrupt();
            synchronized (this.mLock) {
                this.mImageDispatcher.stop(bArr);
                this.mImageDispatcher = null;
                try {
                    this.mServerSocket.close();
                } catch (IOException unused) {
                }
                this.mServerSocket = null;
                this.mHttpServerThread = new HttpServerThread();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private String getRandomStreamAddress(String str) {
        Random random = new Random(Long.parseLong(str));
        char[] cArr = new char[10];
        for (int i = 0; i < 10; i++) {
            cArr[i] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(random.nextInt(62));
        }
        return "/screen_stream_" + String.valueOf(cArr) + ".mjpeg";
    }
}
