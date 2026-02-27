package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony;

import java.util.ArrayList;

public interface AppsListener {
    void onAppsFetched(ArrayList<TVApp> arrayList);

    void onError();
}
