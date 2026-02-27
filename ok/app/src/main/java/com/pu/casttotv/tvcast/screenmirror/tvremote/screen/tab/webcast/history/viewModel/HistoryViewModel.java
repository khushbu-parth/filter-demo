package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pu.casttotv.tvcast.screenmirror.tvremote.HistoryBrowser;
import com.pu.casttotv.tvcast.screenmirror.tvremote.HistoryDatabase;
import com.pu.casttotv.tvcast.screenmirror.tvremote.HistoryRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public final class HistoryViewModel extends AndroidViewModel {
    @NotNull
    private final LiveData<List<HistoryBrowser>> readAllData;
    @NotNull
    public static HistoryRepository repository = null;

    public HistoryViewModel(@NotNull Application application) {
        super(application);
        Intrinsics.checkNotNullParameter(application, "application");
        HistoryRepository historyRepository = new HistoryRepository(HistoryDatabase.Companion.getDatabase(application).historyBrowserDao());
        this.repository = historyRepository;
        this.readAllData = historyRepository.getReadAllData();
    }

    @NotNull
    public LiveData<List<HistoryBrowser>> getReadAllData() {
        return this.readAllData;
    }

    public void addHistory(@NotNull HistoryBrowser historyBrowser) {
        Intrinsics.checkNotNullParameter(historyBrowser, "historyBrowser");
//        BuildersKt.launch(ViewModelKt.getViewModelScope(this), Dispatchers.getIO(), null, new HistoryViewModel$addHistory$1(this, historyBrowser, null));
    }

    public final void updateHistory(@NotNull HistoryBrowser historyBrowser) {
        Intrinsics.checkNotNullParameter(historyBrowser, "historyBrowser");
//        BuildersKt.launch(ViewModelKt.getViewModelScope(this), Dispatchers.getIO(), null, new HistoryViewModel$updateHistory$1(this, historyBrowser, null));
    }

    public final void deleteHistory(@NotNull HistoryBrowser historyBrowser) {
        Intrinsics.checkNotNullParameter(historyBrowser, "historyBrowser");
//        BuildersKt.launch(ViewModelKt.getViewModelScope(this), Dispatchers.getIO(), null, new HistoryViewModel$deleteHistory$1(this, historyBrowser, null));
    }

    public final void deleteAllHistory() {
//        BuildersKt.launch(ViewModelKt.getViewModelScope(this), Dispatchers.getIO(), null, new HistoryViewModel$deleteAllHistory$1(this, null));
    }
}
