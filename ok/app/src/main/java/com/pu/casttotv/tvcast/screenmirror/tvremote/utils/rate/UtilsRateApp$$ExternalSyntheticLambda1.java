package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.rate;

import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

public final class UtilsRateApp$$ExternalSyntheticLambda1 implements OnCompleteListener {
    public final UtilsRateApp f$0;

    public UtilsRateApp$$ExternalSyntheticLambda1(UtilsRateApp utilsRateApp) {
        this.f$0 = utilsRateApp;
    }

    @Override // com.google.android.play.core.tasks.OnCompleteListener
    public final void onComplete(Task task) {
        this.f$0.lambda$startReviewFlow$1(task);
    }
}
