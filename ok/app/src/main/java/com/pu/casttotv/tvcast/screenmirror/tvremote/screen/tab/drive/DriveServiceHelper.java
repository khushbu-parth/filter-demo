package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes4.dex */
public class DriveServiceHelper {
    private final Drive mDriveService;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private String GOOGLE_CLOUD_FIELD = "files(id, name, size, modifiedTime, mimeType, parents, thumbnailLink, permissions, webViewLink, version, webContentLink)";

    public DriveServiceHelper(Drive drive) {
        this.mDriveService = drive;
    }

    public Task<FileList> queryFiles() {
        return Tasks.call(this.mExecutor, new Callable<FileList>() { // from class: com.thntech.cast68.screen.tab.drive.DriveServiceHelper.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Type inference failed for: r0v5, types: [com.google.api.services.drive.Drive$Files$List] */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public FileList call() throws Exception {
                Drive.Files.List pageSize = DriveServiceHelper.this.mDriveService.files().list().setPageSize(1000);
                return pageSize.setFields(DriveServiceHelper.this.GOOGLE_CLOUD_FIELD + ", nextPageToken").setQ("('root' in parents and trashed = false)").execute();
            }
        });
    }

    public Task<FileList> queryFiles(String str) {
        final String str2 = "('" + str + "' in parents and trashed = false)";
        return Tasks.call(this.mExecutor, new Callable<FileList>() { // from class: com.thntech.cast68.screen.tab.drive.DriveServiceHelper.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Type inference failed for: r0v5, types: [com.google.api.services.drive.Drive$Files$List] */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public FileList call() throws Exception {
                Drive.Files.List pageSize = DriveServiceHelper.this.mDriveService.files().list().setPageSize(1000);
                return pageSize.setFields(DriveServiceHelper.this.GOOGLE_CLOUD_FIELD + ", nextPageToken").setQ(str2).execute();
            }
        });
    }

    public Task<Pair<String, String>> openFileUsingStorageAccessFramework(final ContentResolver contentResolver, final Uri uri) {
        return Tasks.call(this.mExecutor, new Callable() { // from class: com.thntech.cast68.screen.tab.drive.DriveServiceHelper$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                Pair lambda$openFileUsingStorageAccessFramework$3;
                try {
                    lambda$openFileUsingStorageAccessFramework$3 = DriveServiceHelper.lambda$openFileUsingStorageAccessFramework$3(contentResolver, uri);
                    return lambda$openFileUsingStorageAccessFramework$3;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Pair lambda$openFileUsingStorageAccessFramework$3(ContentResolver contentResolver, Uri uri) throws Exception {
        Cursor query = contentResolver.query(uri, null, null, null, null);
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_display_name"));
                    query.close();
                    InputStream openInputStream = contentResolver.openInputStream(uri);
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openInputStream));
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append(readLine);
                        }
                        String sb2 = sb.toString();
                        bufferedReader.close();
                        if (openInputStream != null) {
                            openInputStream.close();
                        }
                        return Pair.create(string, sb2);
                    } catch (Throwable th) {
                        if (openInputStream != null) {
                            try {
                                openInputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                }
                throw th3;
            }
        }
        throw new IOException("Empty cursor returned for file.");
    }
}
