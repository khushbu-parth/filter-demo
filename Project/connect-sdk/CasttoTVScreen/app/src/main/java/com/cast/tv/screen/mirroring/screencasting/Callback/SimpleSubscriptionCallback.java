package com.cast.tv.screen.mirroring.screencasting.Callback;

import com.cast.tv.screen.mirroring.screencasting.Utils.L;

import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;


public class SimpleSubscriptionCallback extends SubscriptionCallback {
    private final String TAG;

    public SimpleSubscriptionCallback(Service service) {
        super(service);
        this.TAG = "SimpleSubscriptionCallback";
    }

    @Override
    protected void failed(GENASubscription gENASubscription, UpnpResponse upnpResponse, Exception exc, String str) {
        L.e("SimpleSubscriptionCallback", "-------------------------------- failed --------------------------------");
    }

    @Override
    protected void established(GENASubscription gENASubscription) {
        L.e("SimpleSubscriptionCallback", "-------------------------------- established --------------------------------");
    }

    @Override
    protected void ended(GENASubscription gENASubscription, CancelReason cancelReason, UpnpResponse upnpResponse) {
        L.e("SimpleSubscriptionCallback", "-------------------------------- ended --------------------------------");
    }

    @Override
    protected void eventReceived(GENASubscription gENASubscription) {
        L.e("SimpleSubscriptionCallback", "-------------------------------- eventReceived --------------------------------");
    }

    @Override
    protected void eventsMissed(GENASubscription gENASubscription, int i) {
        L.e("SimpleSubscriptionCallback", "-------------------------------- eventsMissed --------------------------------");
    }
}
