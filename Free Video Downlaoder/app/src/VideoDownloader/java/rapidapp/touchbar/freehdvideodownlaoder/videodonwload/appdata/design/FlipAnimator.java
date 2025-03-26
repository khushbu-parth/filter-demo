package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.design;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class FlipAnimator {
    public static void flipView(Context context, View view, View view2, boolean z) {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        AnimatorSet animatorSet2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
        AnimatorSet animatorSet3 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
        AnimatorSet animatorSet4 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in);
        AnimatorSet animatorSet5 = new AnimatorSet();
        AnimatorSet animatorSet6 = new AnimatorSet();
        animatorSet.setTarget(view);
        animatorSet2.setTarget(view2);
        animatorSet5.playTogether(new Animator[]{animatorSet, animatorSet2});
        animatorSet3.setTarget(view);
        animatorSet4.setTarget(view2);
        animatorSet6.playTogether(new Animator[]{animatorSet4, animatorSet3});
        if (z) {
            animatorSet5.start();
        } else {
            animatorSet6.start();
        }
    }
}
