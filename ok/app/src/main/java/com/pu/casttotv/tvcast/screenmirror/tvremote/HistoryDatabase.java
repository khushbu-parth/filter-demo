package com.pu.casttotv.tvcast.screenmirror.tvremote;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HistoryDatabase.kt */
public abstract class HistoryDatabase extends RoomDatabase {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private static volatile HistoryDatabase INSTANCE;

    @NotNull
    public abstract HistoryBrowserDao historyBrowserDao();

    /* compiled from: HistoryDatabase.kt */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final HistoryDatabase getDatabase(@NotNull Context context) {
            HistoryDatabase historyDatabase;
            Intrinsics.checkNotNullParameter(context, "context");
            HistoryDatabase historyDatabase2 = HistoryDatabase.INSTANCE;
            if (historyDatabase2 != null) {
                return historyDatabase2;
            }
            synchronized (this) {
                RoomDatabase build = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, "history_database").build();
                Intrinsics.checkNotNullExpressionValue(build, "databaseBuilder(\n       …                ).build()");
                historyDatabase = (HistoryDatabase) build;
                Companion companion = HistoryDatabase.Companion;
                HistoryDatabase.INSTANCE = historyDatabase;
            }
            return historyDatabase;
        }
    }
}
