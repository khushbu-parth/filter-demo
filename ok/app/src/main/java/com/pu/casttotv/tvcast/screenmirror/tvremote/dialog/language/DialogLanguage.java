package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.language;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ObjectLanguage;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import java.util.ArrayList;

public class DialogLanguage extends Dialog {
    private LanguageAdapter adapter;
    private Context context;
    private ImageView imvClose;
    private ClickLanguage mListener;
    private RecyclerView recyclerView;

    public interface ClickLanguage {
        void clickItem(ObjectLanguage objectLanguage);
    }

    public DialogLanguage(Context context2) {
        super(context2);
        this.context = context2;
    }

    public void setListener(ClickLanguage clickLanguage) {
        this.mListener = clickLanguage;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_language);
        this.recyclerView = (RecyclerView) findViewById(R.id.rcvListLanguage);
        this.imvClose = (ImageView) findViewById(R.id.imvClose);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(1);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        LanguageAdapter languageAdapter = new LanguageAdapter(this.context, new ArrayList());
        this.adapter = languageAdapter;
        this.recyclerView.setAdapter(languageAdapter);
        this.adapter.setListener(new LanguageAdapter.IItemCountry() {
            @Override
            public void setOnclickItem(int i, ObjectLanguage objectLanguage) {
                if (DialogLanguage.this.mListener != null) {
                    SharedPrefsUtil.getInstance().put("KEY_LANGUAGE_SAVE", objectLanguage.getKey());
                    DialogLanguage.this.mListener.clickItem(objectLanguage);
                    DialogLanguage.this.dismiss();
                }
            }
        });
        this.imvClose.setOnClickListener(new View.OnClickListener() {
            /* class com.magicapps.casttotv.tv.dialog.language.DialogLanguage.AnonymousClass2 */

            public void onClick(View view) {
                DialogLanguage.this.dismiss();
            }
        });
        getData();
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
        getWindow().setLayout(-1, -2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        getWindow().setAttributes(layoutParams);
    }

    private void getData() {
        ArrayList arrayList = new ArrayList();
        String str = "";
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_english), str));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_russian), "ru"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_french), "fr"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_spanish), "es"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_turkish), "tr"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_japan), "ja"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_kr), "kr"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_indonesian), "in"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_hindi), "hi"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_norwegian), "no"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_finnish), "fi"));
        arrayList.add(new ObjectLanguage(this.context.getString(R.string.lag_vn), "vi"));
        String str2 = (String) SharedPrefsUtil.getInstance().get("KEY_LANGUAGE_SAVE", String.class);
        if (str2 != null && !str2.isEmpty()) {
            str = str2;
        }
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (((ObjectLanguage) arrayList.get(i2)).getKey().equalsIgnoreCase(str)) {
                i = i2;
            }
        }
        this.adapter.setData(arrayList, str);
        this.recyclerView.scrollToPosition(i);
    }
}
