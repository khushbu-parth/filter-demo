package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks;

import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.RokuRequestTypes;

public abstract class RequestCallback {
    public abstract void onErrorResponse(RequestTask.Result result);

    public abstract void requestResult(RokuRequestTypes rokuRequestTypes, RequestTask.Result result);
}
