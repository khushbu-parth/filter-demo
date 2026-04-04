package com.colorcallscreen.colorphone.callscreen.calltheme.service.notification;

import android.app.Notification;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import java.util.ArrayList;
import java.util.Arrays;


public class NotificationWear {
    private NotificationCompat.Action action;
    private Notification.Action actionExtra;
    private String identifier;
    private RemoteInput[] remoteInputs;
    private android.app.RemoteInput[] remoteInputsExtra;
    private final String sender;

    public NotificationWear(NotificationCompat.WearableExtender wearableExtender, String str, Notification notification, String str2) {
        android.app.RemoteInput[] remoteInputs;
        try {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (NotificationCompat.Action action : wearableExtender.getActions()) {
                if (action != null && action.getRemoteInputs() != null) {
                    arrayList.addAll(Arrays.asList(action.getRemoteInputs()));
                    this.action = action;
                }
            }
            if (arrayList.size() == 0) {
                int actionCount = NotificationCompat.getActionCount(notification);
                for (int i = 0; i < actionCount; i++) {
                    try {
                        NotificationCompat.CarExtender.UnreadConversation unreadConversation = new NotificationCompat.CarExtender(notification).getUnreadConversation();
                        if (unreadConversation != null) {
                            NotificationCompat.Action build = new NotificationCompat.Action.Builder(0, "send_reply", unreadConversation.getReplyPendingIntent()).addRemoteInput(unreadConversation.getRemoteInput()).build();
                            arrayList.addAll(Arrays.asList(build.getRemoteInputs()));
                            this.action = build;
                        } else {
                            Notification.Action action2 = notification.actions[i];
                            if (action2 != null && (remoteInputs = action2.getRemoteInputs()) != null) {
                                arrayList2.addAll(Arrays.asList(remoteInputs));
                                this.actionExtra = action2;
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
            RemoteInput[] remoteInputArr = new RemoteInput[arrayList.size()];
            this.remoteInputs = remoteInputArr;
            arrayList.toArray(remoteInputArr);
            android.app.RemoteInput[] remoteInputArr2 = new android.app.RemoteInput[arrayList2.size()];
            this.remoteInputsExtra = remoteInputArr2;
            arrayList2.toArray(remoteInputArr2);
        } catch (Exception unused2) {
        }
        this.sender = str;
    }

    public NotificationCompat.Action getAction() {
        return this.action;
    }

    public Notification.Action getActionExtra() {
        return this.actionExtra;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public RemoteInput[] getRemoteInputs() {
        return this.remoteInputs;
    }

    public android.app.RemoteInput[] getRemoteInputsExtra() {
        return this.remoteInputsExtra;
    }

    public String getSender() {
        return this.sender;
    }

    public void setActionExtra(Notification.Action action) {
        this.actionExtra = action;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }
}
