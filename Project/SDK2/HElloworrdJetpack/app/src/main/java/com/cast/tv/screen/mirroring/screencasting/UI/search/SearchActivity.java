package com.cast.tv.screen.mirroring.screencasting.UI.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.Contract.IntentContracts;
import com.cast.tv.screen.mirroring.screencasting.Dialog.ConnectDeviceDialog;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.Observer.SimpleObserver;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.AudioListAdapter;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.VideoListAdapter;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.AudioVideoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends BaseActivity implements TextWatcher, TextView.OnEditorActionListener {
    private final int AUDIO = 65793;
    private final int VIDEO = 65794;
    private BaseQuickAdapter<FileModel, BaseViewHolder> mAdapter;
    private EditText mEditInput;
    private ImageView mImageClear;
    private List<FileModel> mList;
    private RecyclerView mRv;
    private int mSearchType;
    private TextView mTextEmpty;


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onCreate(bundle);
    }

    @Override
    protected void init() {
        this.mList = new ArrayList();
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                SearchActivity.this.initSearchActivity1(view);
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.image_clear);
        this.mImageClear = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                SearchActivity.this.initSearchActivity(view);
            }
        });
        this.mTextEmpty = (TextView) findViewById(R.id.text_empty);
        EditText editText = (EditText) findViewById(R.id.edit_input);
        this.mEditInput = editText;
        editText.addTextChangedListener(this);
        this.mEditInput.setOnEditorActionListener(this);
        this.mRv = (RecyclerView) findViewById(R.id.recycler_view);
    }

    public void initSearchActivity1(View view) {
        finish();
    }

    public void initSearchActivity(View view) {
        clearInput();
    }

    @Override
    protected void handlerIntent(Intent intent) {
        int intExtra = intent.getIntExtra(IntentContracts.INTENT_SEARCH_TYPE, 0);
        if (intExtra == 768 || intExtra == 769) {
            this.mSearchType = 65794;
            this.mEditInput.setHint(R.string.SearchVideo);
            setVideoListAdapter();
            return;
        }
        this.mSearchType = 65793;
        this.mEditInput.setHint(R.string.SearchAudio);
        setAudioListAdapter();
    }

    private void setVideoListAdapter() {
        this.mRv.setLayoutManager(new GridLayoutManager(this.mContext, 2));
        VideoListAdapter videoListAdapter = new VideoListAdapter(this.mList);
        this.mAdapter = videoListAdapter;
        this.mRv.setAdapter(videoListAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                SearchActivity.this.setVideoListAdapterSearchActivity(baseQuickAdapter, view, i);
            }
        });
    }

    public void setVideoListAdapterSearchActivity(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        castScreenFile(this.mList.get(i));
    }

    private void setAudioListAdapter() {
        this.mRv.setLayoutManager(new LinearLayoutManager(this.mContext));
        AudioListAdapter audioListAdapter = new AudioListAdapter(this.mList);
        this.mAdapter = audioListAdapter;
        this.mRv.setAdapter(audioListAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                SearchActivity.this.setAudioListAdapterSearchActivity3(baseQuickAdapter, view, i);
            }
        });
    }

    public void setAudioListAdapterSearchActivity3(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        castScreenFile(this.mList.get(i));
    }

    private void castScreenFile(FileModel fileModel) {
        AudioVisualHelper.setIsPlaySingle(true);
        AudioVisualHelper.mCastFileModel.setValue(fileModel);
        if (DLNAHelper.isConnectDevice()) {
            if (MaxRewardUtil.obtainCastFileNum() <= 0) {
                showRewardDialog();
                return;
            }
            DLNAHelper.startPlay(fileModel);
            startActivity(new Intent(this.mContext, AudioVideoCastActivity.class));
            return;
        }
        ConnectDeviceDialog.newInstance(fileModel).show(getSupportFragmentManager(), "ConnectDevice");
    }

    private void showRewardDialog() {
        //ShowRewardAdDialog.newInstance().show(getSupportFragmentManager(), "Reward");
    }

    private void clearInput() {
        this.mEditInput.setText("");
        this.mImageClear.setVisibility(View.GONE);
        this.mTextEmpty.setVisibility(View.GONE);
        this.mList.clear();
        this.mAdapter.setList(this.mList);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String obj = editable.toString();
        if (TextUtils.isEmpty(obj)) {
            this.mImageClear.setVisibility(View.GONE);
            this.mTextEmpty.setVisibility(View.GONE);
            this.mList.clear();
            this.mAdapter.setList(this.mList);
            return;
        }
        this.mImageClear.setVisibility(View.VISIBLE);
        search(obj);
    }

    private void search(final String str) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public final void subscribe(ObservableEmitter observableEmitter) {
                searchSearchActivity(str, observableEmitter);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleObserver<Object>() {
            @Override
            public void onNext(Object obj) {
                if (SearchActivity.this.isFinishing()) {
                    return;
                }
                List list = (List) obj;
                SearchActivity.this.mAdapter.setList(list);
                if (ListUtil.getSize(list) <= 0) {
                    SearchActivity.this.mTextEmpty.setVisibility(View.VISIBLE);
                } else {
                    SearchActivity.this.mTextEmpty.setVisibility(View.GONE);
                }
            }
        });
    }

    public void searchSearchActivity(String str, ObservableEmitter observableEmitter) {
        int i = this.mSearchType;
        if (i == 65793) {
            this.mList = AudioVisualHelper.searchAudio(str);
        } else if (i == 65794) {
            this.mList = AudioVisualHelper.searchVideo(str);
        }
        observableEmitter.onNext(this.mList);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            hintKbTwo(this.mEditInput);
            return false;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        showSoftInputFromWindow(this.mEditInput);
    }

    @Override
    public void onStop() {
        super.onStop();
        hintKbTwo(this.mEditInput);
    }

    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
        getWindow().setSoftInputMode(5);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
    }

    private void hintKbTwo(EditText editText) {
        editText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!inputMethodManager.isActive() || getCurrentFocus() == null || getCurrentFocus().getWindowToken() == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
    }

    @Subscribe
    public void handlerRewardEvent(RewardDialogEvent rewardDialogEvent) {
        if (rewardDialogEvent.mViewType == 2) {
            showRewardDialog();
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        List<FileModel> list = this.mList;
        if (list != null) {
            list.clear();
            this.mList = null;
        }
    }
}
