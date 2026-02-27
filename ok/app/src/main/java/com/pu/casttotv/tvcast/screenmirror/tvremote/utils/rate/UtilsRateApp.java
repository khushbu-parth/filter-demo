package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.rate;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class UtilsRateApp {
    public static UtilsRateApp utilsRateApp;
    public Activity context;
    private ReviewManager manager;
    private ReviewInfo reviewInfo;

    public static UtilsRateApp getIntance(Activity activity) {
        if (utilsRateApp == null) {
            utilsRateApp = new UtilsRateApp(activity);
        }
        return utilsRateApp;
    }

    public UtilsRateApp(Activity activity) {
        this.context = activity;
    }

    public void activateReviewInfor() {
        ReviewManager create = ReviewManagerFactory.create(this.context);
        this.manager = create;
        create.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<ReviewInfo> task) {
                lambda$activateReviewInfor$0(task);
            }
        });
    }

    /* access modifiers changed from: private */
    public void lambda$activateReviewInfor$0(Task task) {
        if (task.isSuccessful()) {
            this.reviewInfo = (ReviewInfo) task.getResult();
            return;
        }
        Toast.makeText(this.context, "Review failed to start ", 0).show();
        Activity activity = this.context;
        Utils.goToGooglePlay(activity, activity.getPackageName());
    }

    public void startReviewFlow() {
        ReviewInfo reviewInfo2 = this.reviewInfo;
        if (reviewInfo2 != null) {
            this.manager.launchReviewFlow(this.context, reviewInfo2).addOnCompleteListener(new UtilsRateApp$$ExternalSyntheticLambda1(this));
            return;
        }
        Activity activity = this.context;
        Utils.goToGooglePlay(activity, activity.getPackageName());
    }

    /* access modifiers changed from: private */
    public void lambda$startReviewFlow$1(Task task) {
        Toast.makeText(this.context, "Review is completed ", 0).show();
    }
}
