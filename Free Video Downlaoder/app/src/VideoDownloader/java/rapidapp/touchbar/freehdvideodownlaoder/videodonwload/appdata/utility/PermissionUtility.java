package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

public final class PermissionUtility {
    private PermissionUtility() {
    }

    private static void check(final Fragment fragment, String[] strArr, int[] iArr) {
        int length = strArr.length;
        int[] iArr2 = new int[length];
        boolean z = false;
        for (int i = 0; i < strArr.length; i++) {
            iArr2[i] = ContextCompat.checkSelfPermission(fragment.getActivity(), strArr[i]);
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < length; i2++) {
            if (iArr2[i2] != 0) {
                arrayList.add(strArr[i2]);
            }
        }
        if (!arrayList.isEmpty()) {
            final String[] strArr2 = (String[]) arrayList.toArray(new String[0]);
            int i3 = 0;
            while (true) {
                if (i3 >= strArr.length) {
                    break;
                } else if (fragment.shouldShowRequestPermissionRationale(strArr[i3])) {
                    Snackbar.make(fragment.getView(), iArr[i3], -2).setAction(17039370, (View.OnClickListener) new View.OnClickListener() {
                        public final void onClick(View view) {
                            fragment.requestPermissions(strArr2, 3);
                        }
                    }).show();
                    z = true;
                    break;
                } else {
                    i3++;
                }
            }
            if (!z) {
                fragment.requestPermissions(strArr2, 3);
            }
        }
    }

    private static boolean check(final Fragment fragment) {
        int checkSelfPermission = ContextCompat.checkSelfPermission(fragment.getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE");
        if (checkSelfPermission != 0) {
            if (fragment.shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                Snackbar.make(fragment.getView(), (int) R.string.permission_write_exterstorage, -2).setAction(17039370, (View.OnClickListener) new View.OnClickListener() {
                    public final void onClick(View view) {
                        fragment.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
                    }
                }).show();
            } else {
                fragment.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
            }
        }
        if (checkSelfPermission == 0) {
            return true;
        }
        return false;
    }

    public static void checkPermissionAccessLocation(Fragment fragment) {
        check(fragment, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, new int[]{R.string.permission_access_location, R.string.permission_access_location});
    }

    public static boolean checkPermissionWriteExternalStorage(Fragment fragment) {
        return check(fragment);
    }
}
