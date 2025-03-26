package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.api;

import android.app.Activity;

import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.TiktokModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.TwitterResponse;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.story.FullDetailModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.story.StoryModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class CommonClassForAPI {
    private static CommonClassForAPI CommonClassForAPI;
    private static Activity mActivity;

    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }

    public void callResult(final DisposableObserver disposableObserver, String str, String str2) {
        if (Utils.isNullOrEmpty(str2)) {
            str2 = "";
        }
        RestClient.getInstance(mActivity).getService().callResult(str, str2, "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(JsonObject jsonObject) {
                disposableObserver.onNext(jsonObject);
            }

            @Override
            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            @Override
            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void callTwitterApi(final DisposableObserver disposableObserver, String str, String str2) {
        RestClient.getInstance(mActivity).getService().callTwitter(str, str2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<TwitterResponse>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(TwitterResponse twitterResponse) {
                disposableObserver.onNext(twitterResponse);
            }

            @Override
            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            @Override
            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void getStories(final DisposableObserver disposableObserver, String str) {
        if (Utils.isNullOrEmpty(str)) {
            str = "";
        }
        RestClient.getInstance(mActivity).getService().getStoriesApi("https://i.instagram.com/api/v1/feed/reels_tray/", str, "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StoryModel>() {

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(StoryModel storyModel) {
                disposableObserver.onNext(storyModel);
            }

            @Override
            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            @Override
            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void getFullDetailFeed(final DisposableObserver disposableObserver, String str, String str2) {
        APIServices service = RestClient.getInstance(mActivity).getService();
        service.getFullDetailInfoApi("https://i.instagram.com/api/v1/users/" + str + "/full_detail_info?max_id=", str2, "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<FullDetailModel>() {

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(FullDetailModel fullDetailModel) {
                disposableObserver.onNext(fullDetailModel);
            }

            @Override
            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            @Override
            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void callTiktokVideo(final DisposableObserver disposableObserver, String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("link", str);
        RestClient.getInstance(mActivity).getService().getTiktokData(Utils.TikTokUrl, hashMap).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<TiktokModel>() {

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(TiktokModel tiktokModel) {
                disposableObserver.onNext(tiktokModel);
            }

            @Override
            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            @Override
            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void callSnackVideoData(final DisposableObserver disposableObserver, String str, String str2, String str3, String str4, String str5) {
        RestClient.getInstance(mActivity).getService().callSnackVideo(str, str2, str3, str4, str5).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(JsonObject jsonObject) {
                disposableObserver.onNext(jsonObject);
            }

            @Override
            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            @Override
            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }
}