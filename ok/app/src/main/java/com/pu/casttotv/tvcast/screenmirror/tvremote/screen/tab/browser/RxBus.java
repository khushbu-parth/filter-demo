package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private final Subject<Object> mBus;

    private RxBus() {
        this.mBus = PublishSubject.create();
    }

    public static RxBus getDefault() {
        return RxBusHolder.sInstance;
    }

    public static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }

    public void post(Object obj) {
        this.mBus.onNext(obj);
    }

    public <T> Observable<T> toObservable(Class<T> cls) {
        return (Observable<T>) this.mBus.ofType(cls);
    }
}
