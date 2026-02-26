package com.lib.nginxserver.nginx;

import android.content.Context;
import android.util.Log;

import com.jrummyapps.android.shell.CommandResult;
import com.jrummyapps.android.shell.Shell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NginxHelper {
    private static final String CONF_FILE_RELATIVE_PATH = "/conf/nginx.conf";
    private static final String EXE_FILE_RELATIVE_PATH = "/sbin/nginx";
    private static final String TAG = "NginxHelper";
    private static String mNginxDir;

    public static String getHttpServerConfig() {
        return ":9572/";
    }

    public static String getRtmpLiveServerConfig() {
        return ":9577/live/";
    }

    public static CommandResult installNginxServer(Context context) {
        mNginxDir = getAppDataDir(context) + "/nginx";
        copyFileOrDirFromAsset(context, "nginx");
        CommandResult run = Shell.SH.run("chmod -R 777 " + mNginxDir);
        Log.d(TAG, run.exitCode + "\n" + run.stdout + "\n" + run.stderr);
        return run;
    }

    public static CommandResult startNginxServer() {
        CommandResult run = Shell.SH.run(mNginxDir + EXE_FILE_RELATIVE_PATH + " -p " + mNginxDir + " -c " + mNginxDir + CONF_FILE_RELATIVE_PATH);
        StringBuilder sb = new StringBuilder();
        sb.append(run.exitCode);
        sb.append("\n");
        sb.append(run.stdout);
        sb.append("\n");
        sb.append(run.stderr);
        Log.d(TAG, sb.toString());
        return run;
    }

    public static CommandResult stopNginxServer() {
        CommandResult run = Shell.SH.run(mNginxDir + EXE_FILE_RELATIVE_PATH + " -p " + mNginxDir + " -s quit");
        Log.d(TAG, run.exitCode + "\n" + run.stdout + "\n" + run.stderr);
        return run;
    }

    private static String getAppDataDir(Context context) {
        return context.getApplicationInfo().dataDir;
    }

    private static void copyFileOrDirFromAsset(Context context, String str) {
        try {
            String[] list = context.getAssets().list(str);
            if (list != null && list.length != 0) {
                File file = new File(getAppDataDir(context) + "/" + str);
                if (!file.exists()) {
                    file.mkdir();
                }
                for (String str2 : list) {
                    copyFileOrDirFromAsset(context, str + "/" + str2);
                }
                return;
            }
            copyFile(context, str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(Context context, String str) {
        Log.d(TAG, "copy file path : " + str);
        FileOutputStream fileOutputStream;
        InputStream assets2;
        try {
            try {
                try {
                    assets2 = context.getAssets().open(str);
                    try {
                        FileOutputStream fileOutputStream2 = new FileOutputStream(getAppDataDir(context) + "/" + str);
                        try {
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = assets2.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream2.write(bArr, 0, read);
                            }
                            assets2.close();
                            fileOutputStream2.flush();
                            fileOutputStream2.close();
                            fileOutputStream2.close();
                            if (assets2 == null) {
                                return;
                            }
                        } catch (Exception e) {
                            e = e;
                            fileOutputStream = fileOutputStream2;
                            e.printStackTrace();
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (assets2 == null) {
                                return;
                            }
                            assets2.close();
                        } catch (Throwable th) {
                            th = th;
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException unused) {
                                    throw th;
                                }
                            }
                            if (assets2 != null) {
                                assets2.close();
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                    }
                } catch (Exception e3) {
                    assets2 = null;
                } catch (Throwable th2) {
                    assets2 = null;
                }
                assets2.close();
            } catch (Exception unused2) {
            }
        } catch (Throwable th3) {
        }
    }
}
