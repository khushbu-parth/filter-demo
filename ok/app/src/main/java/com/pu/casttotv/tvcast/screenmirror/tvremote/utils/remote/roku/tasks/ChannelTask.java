package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks;

import android.content.Context;
import com.jaku.api.QueryRequests;
import com.jaku.model.Channel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.CommandHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ChannelTask implements Callable {
    private Context context;

    public ChannelTask(Context context2) {
        this.context = context2;
    }

    @Override // java.util.concurrent.Callable
    public List<Channel> call() {
        try {
            return QueryRequests.queryAppsRequest(CommandHelper.getDeviceURL(this.context));
        } catch (IOException e2) {
            e2.printStackTrace();
            return new ArrayList();
        }
    }
}
