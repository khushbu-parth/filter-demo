package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.BaseDialog;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class available_files_dialog extends BaseDialog {
    private file_type _type;
    private Activity activity;
    private ImageButton imvClose;
    private ImageView imvNoFile;
    private boolean isShowError;
    private RecyclerView result_recycler_view;

    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public int getLayoutId() {
        return R.layout.available_files_dialog;
    }

    public available_files_dialog(Activity activity, file_type file_typeVar, boolean z) {
        super(activity);
        this.isShowError = false;
        this.activity = activity;
        this._type = file_typeVar;
        this.isShowError = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setLayout(-1, -1);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);
    }

    @SuppressLint("WrongConstant")
    @Override // com.magicapps.casttotv.tv.dialog.BaseDialog
    public void initView() {
        this.imvNoFile = (ImageView) findViewById(R.id.imvNoFile);
        this.imvClose = (ImageButton) findViewById(R.id.imvClose);
        this.result_recycler_view = (RecyclerView) findViewById(R.id.result_recycler_view);
        ArrayList<downloadable_resource_model> arrayList = static_variables.get_downloadable_resource_model_By_Type(this._type);
        Activity activity = this.activity;
        ResultHolderAdapter resultHolderAdapter = new ResultHolderAdapter(activity, this._type, activity, new ArrayList());
        this.result_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        this.result_recycler_view.setAdapter(resultHolderAdapter);
        if (arrayList != null && arrayList.size() > 0) {
            resultHolderAdapter.setData(arrayList);
        }
        if (this.isShowError) {
            this.imvNoFile.setVisibility(0);
        } else {
            this.imvNoFile.setVisibility(8);
        }
        this.imvClose.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.webcast.available_files_dialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                available_files_dialog.this.dismiss();
            }
        });
    }
}
