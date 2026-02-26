package com.cast.tv.screen.mirroring.screencasting.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cast.tv.screen.mirroring.screencasting.R;


public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected boolean mIsMvvm = false;
    private View mLoadingView;

    public static void safedk_BaseActivity_startActivityForResult_8537efbb5b3e16ca8ac00e775f392bb0(BaseActivity p0, Intent p1, int p2) {
        if (p1 == null) {
            return;
        }
        p0.startActivityForResult(p1, p2);
    }

    public static void safedk_BaseActivity_startActivity_76a9d59c83554f0672bdbfbea636ff43(BaseActivity p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    protected void bindLayout() {
    }

    public abstract int getLayoutId();

    protected void handlerIntent(Intent intent) {
    }

    protected abstract void init();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        if (this.mIsMvvm) {
            bindLayout();
        } else {
            setContentView(getLayoutId());
        }
        getWindow().setFlags(1024,1024);
        init();
        handlerIntent();
    }

    private void handlerIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        handlerIntent(intent);
    }

    public void startActivity(Class cls) {
        startActivity(new Intent(this.mContext, cls));
    }

    protected void startActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this.mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivityForResult(Class cls, int i) {
        startActivityForResult(new Intent(this.mContext, cls), i);
    }

    protected void startActivityForResult(Class cls, Bundle bundle, int i) {
        Intent intent = new Intent(this.mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, i);
    }

    protected void showLoading() {
        showLoading(null);
    }

    protected void showLoading(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public final void run() {
                BaseActivity.this.showLoadingBaseActivity(view);
            }
        });
    }

    public void showLoadingBaseActivity(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        if (this.mLoadingView == null) {
            View findViewById = findViewById(R.id.ll_loading);
            this.mLoadingView = findViewById;
            findViewById.setOnClickListener(null);
        }
        this.mLoadingView.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        hideLoading(null);
    }

    protected void hideLoading(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public final void run() {
                BaseActivity.this.hideLoadingBaseActivity(view);
            }
        });
    }

    public void hideLoadingBaseActivity(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        View view2 = this.mLoadingView;
        if (view2 != null) {
            view2.setVisibility(View.GONE);
        }
    }
}
