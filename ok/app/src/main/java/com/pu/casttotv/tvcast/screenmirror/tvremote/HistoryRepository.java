package com.pu.casttotv.tvcast.screenmirror.tvremote;

import androidx.lifecycle.LiveData;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HistoryRepository.kt */
public final class HistoryRepository {
    @NotNull
    private final HistoryBrowserDao historyBrowserDao;
    @NotNull
    private final LiveData<List<HistoryBrowser>> readAllData;

    public HistoryRepository(@NotNull HistoryBrowserDao historyBrowserDao2) {
        Intrinsics.checkNotNullParameter(historyBrowserDao2, "historyBrowserDao");
        this.historyBrowserDao = historyBrowserDao2;
        this.readAllData = historyBrowserDao2.readAllData();
    }

    @NotNull
    public final LiveData<List<HistoryBrowser>> getReadAllData() {
        return this.readAllData;
    }

    @Nullable
    public final Object addHistory(@NotNull HistoryBrowser historyBrowser, @NotNull Continuation<? super Unit> continuation) {
        Object addHistory = this.historyBrowserDao.addHistory(historyBrowser, continuation);
        return addHistory == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? addHistory : Unit.INSTANCE;
    }

    @Nullable
    public final Object updateHistory(@NotNull HistoryBrowser historyBrowser, @NotNull Continuation<? super Unit> continuation) {
        Object updateHistory = this.historyBrowserDao.updateHistory(historyBrowser, continuation);
        return updateHistory == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? updateHistory : Unit.INSTANCE;
    }

    @Nullable
    public final Object deleteHistory(@NotNull HistoryBrowser historyBrowser, @NotNull Continuation<? super Unit> continuation) {
        Object deleteHistory = this.historyBrowserDao.deleteHistory(historyBrowser, continuation);
        return deleteHistory == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? deleteHistory : Unit.INSTANCE;
    }

    @Nullable
    public final Object deleteAllHistory(@NotNull Continuation<? super Unit> continuation) {
        Object deleteAllHistory = this.historyBrowserDao.deleteAllHistory(continuation);
        return deleteAllHistory == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? deleteAllHistory : Unit.INSTANCE;
    }
}
