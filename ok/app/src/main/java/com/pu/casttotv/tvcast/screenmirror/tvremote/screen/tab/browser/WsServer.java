package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import android.util.Log;

import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

/* loaded from: classes4.dex */
public class WsServer extends WebSocketServer {
    private List<String> connList;
    private boolean running;
    private WsServerListener wsServerListener;

    @Override // org.java_websocket.server.WebSocketServer
    public void onClose(WebSocket webSocket, int i, String str, boolean z) {
    }

    @Override // org.java_websocket.server.WebSocketServer
    public void onMessage(WebSocket webSocket, ByteBuffer byteBuffer) {
    }

    public void setListener(WsServerListener wsServerListener) {
        this.wsServerListener = wsServerListener;
    }

    public WsServer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
        this.running = false;
        this.connList = new ArrayList();
    }

    public static WsServer init(String str, int i) {
        return new WsServer(new InetSocketAddress(str, i));
    }

    @Override // org.java_websocket.server.WebSocketServer
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        String replace = webSocket.getRemoteSocketAddress().getAddress().toString().replace("/", "");
        this.connList.add(replace);
        this.wsServerListener.onWsServerConnChanged(this.connList);
        StringBuilder sb = new StringBuilder();
        sb.append("onOpen: // ");
        sb.append(replace);
        sb.append(" //Opened connection number  ");
        sb.append(this.connList.size());
    }

    @Override // org.java_websocket.server.WebSocketServer
    public void onMessage(WebSocket webSocket, String str) {
        try {
            EventBus.getDefault().post(new MessageEvent("KEY_TIME_WEB", (long) Double.parseDouble(str)));
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
        }
    }

    @Override // org.java_websocket.server.WebSocketServer
    public void onError(WebSocket webSocket, Exception exc) {
        StringBuilder sb = new StringBuilder();
        sb.append("onError: ");
        sb.append(exc.getMessage());
        exc.printStackTrace();
        Log.e("##TAG", "onError: "+exc.getMessage() );
        if (exc.getMessage() != null && exc.getMessage().contains("Address already in use")) {
            this.wsServerListener.onWsServerError(1);
        } else {
            this.wsServerListener.onWsServerError(0);
        }
    }

    @Override // org.java_websocket.server.WebSocketServer
    public void onClosing(WebSocket webSocket, int i, String str, boolean z) {
        super.onClosing(webSocket, i, str, z);
        String replace = webSocket.getRemoteSocketAddress().getAddress().toString().replace("/", "");
        Iterator<String> it = this.connList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            if (next.equals(replace)) {
                this.connList.remove(next);
                break;
            }
        }
        this.wsServerListener.onWsServerConnChanged(this.connList);
        StringBuilder sb = new StringBuilder();
        sb.append("onClosing: // ");
        sb.append(replace);
        sb.append(" //Opened connection number  ");
        sb.append(this.connList.size());
    }

    @Override // org.java_websocket.WebSocketAdapter, org.java_websocket.WebSocketListener
    public void onWebsocketPing(WebSocket webSocket, Framedata framedata) {
        super.onWebsocketPing(webSocket, framedata);
    }

    @Override // org.java_websocket.server.WebSocketServer
    public void onStart() {
        this.running = true;
        this.wsServerListener.onWsServerStatusChanged(true);
    }

    @Override // org.java_websocket.WebSocketAdapter, org.java_websocket.WebSocketListener
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket webSocket, Draft draft, ClientHandshake clientHandshake) throws InvalidDataException {
        return super.onWebsocketHandshakeReceivedAsServer(webSocket, draft, clientHandshake);
    }
}
