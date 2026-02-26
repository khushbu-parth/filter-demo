package com.cast.tv.screen.mirroring.screencasting.Base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cast.tv.screen.mirroring.screencasting.R;


public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected boolean mIsMvvm;
    private View mLoadingView;
    private View mRootView;

    protected View bindLayout(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }

    public abstract int getLayoutId();

    public abstract void setClickEvent(View view);

    public abstract void setViewData(View view);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (AppCompatActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mIsMvvm) {
            return bindLayout(layoutInflater, viewGroup);
        }
        return layoutInflater.inflate(getLayoutId(), viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mRootView = view;
        setViewData(view);
        setClickEvent(view);
    }

    protected void showLoading() {
        showLoading(null);
    }

    public void showLoading(final View view) {
        this.mActivity.runOnUiThread(new Runnable() {
            @Override
            public final void run() {
                BaseFragment.this.showLoadingBaseFragment(view);
            }
        });
    }

    public void showLoadingBaseFragment(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        if (this.mLoadingView == null) {
            View findViewById = this.mRootView.findViewById(R.id.ll_loading);
            this.mLoadingView = findViewById;
            findViewById.setOnClickListener(null);
        }
        this.mLoadingView.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        hideLoading(null);
    }

    public void hideLoading(final View view) {
        this.mActivity.runOnUiThread(new Runnable() {
            @Override
            public final void run() {
                BaseFragment.this.hideLoadingBaseFragment(view);
            }
        });
    }

    public void hideLoadingBaseFragment(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        View view2 = this.mLoadingView;
        if (view2 != null) {
            view2.setVisibility(View.GONE);
        }
    }
}
