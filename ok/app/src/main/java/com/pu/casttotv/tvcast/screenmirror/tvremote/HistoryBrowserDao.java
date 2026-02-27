package com.pu.casttotv.tvcast.screenmirror.tvremote;

import androidx.lifecycle.LiveData;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HistoryBrowserDao.kt */
public interface HistoryBrowserDao {
    @Nullable
    Object addHistory(@NotNull HistoryBrowser historyBrowser, @NotNull Continuation<? super Unit> continuation);

    @Nullable
    Object deleteAllHistory(@NotNull Continuation<? super Unit> continuation);

    @Nullable
    Object deleteHistory(@NotNull HistoryBrowser historyBrowser, @NotNull Continuation<? super Unit> continuation);

    @NotNull
    LiveData<List<HistoryBrowser>> readAllData();

    @Nullable
    Object updateHistory(@NotNull HistoryBrowser historyBrowser, @NotNull Continuation<? super Unit> continuation);
}
