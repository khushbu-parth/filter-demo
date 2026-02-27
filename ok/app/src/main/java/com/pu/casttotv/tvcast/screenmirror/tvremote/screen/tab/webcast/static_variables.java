package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.util.Log;

import java.util.ArrayList;

/* loaded from: classes4.dex */
public class static_variables {
    public static resourse_holder_model resourse_holder = new resourse_holder_model();

    public static ArrayList<downloadable_resource_model> get_downloadable_resource_model_By_Type(file_type file_typeVar) {
        try {
            Log.e("##TAG", "File Type: "+file_typeVar );
            if (file_typeVar == file_type.VIDEO) {
                if (resourse_holder.getVideo_files() != null) {
                    Log.e("##TAG", "getVideo_files: " + resourse_holder.getVideo_files());
                    return resourse_holder.getVideo_files();
                }
                return new ArrayList<>();
            } else if (file_typeVar == file_type.IMAGE) {
                if (resourse_holder.getImage_files() != null) {
                    return resourse_holder.getImage_files();
                }
                return new ArrayList<>();
            } else if (file_typeVar != file_type.AUDIO) {
                return null;
            } else {
                if (resourse_holder.getAudio_files() != null) {
                    return resourse_holder.getAudio_files();
                }
                return new ArrayList<>();
            }
        } catch (Exception e2) {
            Log.e("##TAG", "Exception: "+e2.getMessage() );
            e2.printStackTrace();
            return null;
        }
    }
}
