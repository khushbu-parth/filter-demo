package com.lib.screening.listener;

import org.fourthline.cling.model.action.ActionInvocation;

public interface DLNAControlCallback {

    void onFailure(ActionInvocation actionInvocation, int i, String str);

    void onReceived(ActionInvocation actionInvocation, Object... objArr);

    void onSuccess(ActionInvocation actionInvocation);
}
