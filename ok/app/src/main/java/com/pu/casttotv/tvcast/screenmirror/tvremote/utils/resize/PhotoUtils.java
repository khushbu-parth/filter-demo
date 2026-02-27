package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.resize;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;

import com.ironsource.mediationsdk.utils.IronSourceConstants;

import org.apache.http.cookie.ClientCookie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PhotoUtils.kt */
public final class PhotoUtils {
    @NotNull
    public static final Companion Companion = new Companion(null);

    /* compiled from: PhotoUtils.kt */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void castPhotoOnline(@NotNull String str, @Nullable Context context, @NotNull DownloadManager downloadManager, @NotNull CastPhotoOnlineError castPhotoOnlineError) {
            Intrinsics.checkNotNullParameter(str, ClientCookie.PATH_ATTR);
            Intrinsics.checkNotNullParameter(downloadManager, "manager");
            Intrinsics.checkNotNullParameter(castPhotoOnlineError, "castPhotoOnlineError");
//            Job unused = BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, Dispatchers.getMain(), null, new PhotoUtils$Companion$castPhotoOnline$1(downloadManager, str, context, castPhotoOnlineError, null), 2, null);
        }

        @SuppressLint("Range")
        public final void getStatusMessage(long j, String str, Context context, DownloadManager downloadManager, CastPhotoOnlineError castPhotoOnlineError) {
            boolean z;
            boolean z2 = true;
            while (z2) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(j);
                Cursor query2 = downloadManager.query(query);
                if (query2 != null) {
                    try {
                        if (query2.moveToFirst()) {
                            z = true;
                            if (z) {
                                int i = query2.getInt(query2.getColumnIndex("status"));
                                int i2 = query2.getInt(query2.getColumnIndex(IronSourceConstants.EVENTS_ERROR_REASON));
                                if (i != 1) {
                                    if (i == 2) {
                                        query2.getInt(query2.getColumnIndex("bytes_so_far"));
                                        query2.getInt(query2.getColumnIndex("total_size"));
                                    } else if (i != 4) {
                                        if (i != 8) {
                                            if (i == 16) {
                                                switch (i2) {
                                                    case 1000:
                                                    case 1001:
                                                    case 1002:
                                                    case 1003:
                                                    case 1004:
                                                    case 1005:
                                                    case 1006:
                                                    case 1007:
                                                    case 1008:
                                                    case 1009:
                                                    default:
                                                        query2.close();
                                                        break;
                                                }
                                            }
                                        } else {
                                            if (context != null) {
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("link download: ");
                                                File externalCacheDir = context.getExternalCacheDir();
                                                String str2 = null;
                                                sb.append(externalCacheDir != null ? externalCacheDir.getAbsolutePath() : null);
                                                String str3 = File.separator;
                                                sb.append(str3);
                                                sb.append(str);
                                                StringBuilder sb2 = new StringBuilder();
                                                File externalCacheDir2 = context.getExternalCacheDir();
                                                if (externalCacheDir2 != null) {
                                                    str2 = externalCacheDir2.getAbsolutePath();
                                                }
                                                sb2.append(str2);
                                                sb2.append(str3);
                                                sb2.append(str);
                                                castPhotoOnlineError.playAgainOnline(sb2.toString());
                                            }
                                            query2.close();
                                        }
                                    }
                                }
                                if (query2 != null) {
                                    query2.close();
                                }
                            }
                            z2 = false;
                            if (query2 != null) {
                            }
                        }
                    } catch (Throwable th) {
                        if (query2 != null) {
                            query2.close();
                        }
                        throw th;
                    }
                }
                z = false;
                if (z) {
                }
                z2 = false;
                if (query2 != null) {
                }
            }
        }
    }
}
