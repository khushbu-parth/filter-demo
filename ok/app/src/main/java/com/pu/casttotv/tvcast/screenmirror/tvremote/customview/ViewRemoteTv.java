package com.pu.casttotv.tvcast.screenmirror.tvremote.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import com.magicapps.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;

@SuppressLint("WrongConstant")
public class ViewRemoteTv extends ConstraintLayout {
    @BindView(R.id.ct1)
    ConstraintLayout ct1;
    @BindView(R.id.ct2)
    ConstraintLayout ct2;
    @BindView(R.id.ct3)
    ConstraintLayout ct3;
    @BindView(R.id.ct_fire_tv)
    ConstraintLayout ct_fire_tv;
    @BindView(R.id.ct_lg)
    ConstraintLayout ct_lg;
    @BindView(R.id.ct_roku)
    ConstraintLayout ct_roku;
    @BindView(R.id.ct_sony)
    ConstraintLayout ct_sony;
    @BindView(R.id.ct_ss)
    ConstraintLayout ct_ss;
    @BindView(R.id.imv_bg)
    ImageView imv_bg;
    @BindView(R.id.llRemoteDown)
    LinearLayout llRemoteDown;
    @BindView(R.id.llRemoteLeft)
    LinearLayout llRemoteLeft;
    @BindView(R.id.llRemoteOk)
    ConstraintLayout llRemoteOk;
    @BindView(R.id.llRemoteRight)
    LinearLayout llRemoteRight;
    @BindView(R.id.llRemoteUp)
    LinearLayout llRemoteUp;
    @BindView(R.id.ll_remoteSonyBottom)
    LinearLayout ll_remoteSonyBottom;
    @BindView(R.id.ll_remoteSonyLeft)
    LinearLayout ll_remoteSonyLeft;
    @BindView(R.id.ll_remoteSonyOk)
    LinearLayout ll_remoteSonyOk;
    @BindView(R.id.ll_remoteSonyRight)
    LinearLayout ll_remoteSonyRight;
    @BindView(R.id.ll_remoteSonyUp)
    LinearLayout ll_remoteSonyUp;
    @BindView(R.id.ll_remote_down_ss)
    LinearLayout ll_remote_down_ss;
    @BindView(R.id.ll_remote_left_ss)
    LinearLayout ll_remote_left_ss;
    @BindView(R.id.ll_remote_ok_ss)
    ConstraintLayout ll_remote_ok_ss;
    @BindView(R.id.ll_remote_right_ss)
    LinearLayout ll_remote_right_ss;
    @BindView(R.id.ll_remote_up_ss)
    LinearLayout ll_remote_up_ss;
    private IClickTab1 mLister1;
    private IClickTab3 mLister3;
    @BindView(R.id.rlt_eight)
    RelativeLayout rlt_eight;
    @BindView(R.id.rlt_five)
    RelativeLayout rlt_five;
    @BindView(R.id.rlt_four)
    RelativeLayout rlt_four;
    @BindView(R.id.rlt_nine)
    RelativeLayout rlt_nine;
    @BindView(R.id.rlt_one)
    RelativeLayout rlt_one;
    @BindView(R.id.rlt_remote_down)
    LinearLayout rlt_remote_down;
    @BindView(R.id.rlt_remote_left)
    LinearLayout rlt_remote_left;
    @BindView(R.id.rlt_remote_ok)
    ConstraintLayout rlt_remote_ok;
    @BindView(R.id.rlt_remote_right)
    LinearLayout rlt_remote_right;
    @BindView(R.id.rlt_remote_up)
    LinearLayout rlt_remote_up;
    @BindView(R.id.rlt_seven)
    RelativeLayout rlt_seven;
    @BindView(R.id.rlt_six)
    RelativeLayout rlt_six;
    @BindView(R.id.rlt_three)
    RelativeLayout rlt_three;
    @BindView(R.id.rlt_two)
    RelativeLayout rlt_two;
    @BindView(R.id.rlt_zero)
    RelativeLayout rlt_zero;
    @BindView(R.id.view_fireTVDown)
    View view_fireTVDown;
    @BindView(R.id.view_fireTVLeft)
    View view_fireTVLeft;
    @BindView(R.id.view_fireTVRight)
    View view_fireTVRight;
    @BindView(R.id.view_fireTVUp)
    View view_fireTVUp;

    public interface IClickTab1 {
        void clickBottom();

        void clickCancel();

        void clickDowns();

        void clickLeft();

        void clickOK();

        void clickRight();

        void clickTop();
    }

    public interface IClickTab3 {
        void clickEight();

        void clickFive();

        void clickFour();

        void clickNine();

        void clickOne();

        void clickSeven();

        void clickSix();

        void clickThree();

        void clickTwo();

        void clickZero();
    }

    public void setLister1(IClickTab1 iClickTab1) {
        this.mLister1 = iClickTab1;
    }

    public void setLister3(IClickTab3 iClickTab3) {
        this.mLister3 = iClickTab3;
    }

    public ViewRemoteTv(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        intViews(context, attributeSet);
    }

    public ViewRemoteTv(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        intViews(context, attributeSet);
    }

    private void intViews(Context context, AttributeSet attributeSet) {
        ButterKnife.bind(this, LayoutInflater.from(context).inflate(R.layout.layout_custom_view_tv, (ViewGroup) this, true));
        this.ct2.setOnTouchListener(new OnSwipeTouchListener(context) {
            /* class com.magicapps.casttotv.tv.customview.ViewRemoteTv.AnonymousClass1 */

            @Override // com.magicapps.casttotv.tv.customview.OnSwipeTouchListener
            public void onSwipeOk() {
                super.onSwipeOk();
                if (ViewRemoteTv.this.mLister1 != null) {
                    ViewRemoteTv.this.mLister1.clickOK();
                }
            }

            @Override // com.magicapps.casttotv.tv.customview.OnSwipeTouchListener
            public void onSwipeTop() {
                super.onSwipeTop();
                if (ViewRemoteTv.this.mLister1 != null) {
                    ViewRemoteTv.this.mLister1.clickTop();
                }
            }

            @Override // com.magicapps.casttotv.tv.customview.OnSwipeTouchListener
            public void onSwipeBottom() {
                super.onSwipeBottom();
                if (ViewRemoteTv.this.mLister1 != null) {
                    ViewRemoteTv.this.mLister1.clickBottom();
                }
            }

            @Override // com.magicapps.casttotv.tv.customview.OnSwipeTouchListener
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if (ViewRemoteTv.this.mLister1 != null) {
                    ViewRemoteTv.this.mLister1.clickLeft();
                }
            }

            @Override // com.magicapps.casttotv.tv.customview.OnSwipeTouchListener
            public void onSwipeRight() {
                super.onSwipeRight();
                if (ViewRemoteTv.this.mLister1 != null) {
                    ViewRemoteTv.this.mLister1.clickRight();
                }
            }

            @Override // com.magicapps.casttotv.tv.customview.OnSwipeTouchListener
            public void onCancel() {
                super.onCancel();
                if (ViewRemoteTv.this.mLister1 != null) {
                    ViewRemoteTv.this.mLister1.clickCancel();
                }
            }

            @Override // com.magicapps.casttotv.tv.customview.OnSwipeTouchListener
            public void onDownS() {
                super.onDownS();
                if (ViewRemoteTv.this.mLister1 != null) {
                    ViewRemoteTv.this.mLister1.clickDowns();
                }
            }
        });
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomRemoteVIew);
            @SuppressLint("ResourceType") int i = obtainStyledAttributes.getInt(1, 1);
            int i2 = obtainStyledAttributes.getInt(0, 1);
            setTypeVew(i);
            setTypeTv(i2);
            obtainStyledAttributes.recycle();
        }
    }

    public void setTypeTv(int i) {
        this.ct_roku.setVisibility(8);
        this.ct_fire_tv.setVisibility(8);
        this.ct_ss.setVisibility(8);
        this.ct_sony.setVisibility(8);
        this.ct_lg.setVisibility(8);
        if (i == TVType.TypeRoku) {
            this.ct_roku.setVisibility(0);
        } else if (i == TVType.TypeLG) {
            this.ct_lg.setVisibility(0);
        } else if (i == TVType.TypeSamsung) {
            this.ct_ss.setVisibility(0);
        } else if (i == TVType.TypeFireTV) {
            this.ct_fire_tv.setVisibility(0);
        } else if (i == TVType.TypeSony) {
            this.ct_sony.setVisibility(0);
        }
    }

    public void setTypeVew(int i) {
        this.ct1.setVisibility(8);
        this.ct2.setVisibility(8);
        this.ct3.setVisibility(8);
        if (i == 1) {
            this.ct1.setVisibility(0);
        } else if (i == 2) {
            this.ct2.setVisibility(0);
        } else if (i == 3) {
            this.ct3.setVisibility(0);
        }
    }

    @OnClick
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.llRemoteDown:
            case R.id.ll_remoteSonyBottom:
            case R.id.ll_remote_down_ss:
            case R.id.rlt_remote_down:
            case R.id.view_fireTVDown:
                IClickTab1 iClickTab1 = this.mLister1;
                if (iClickTab1 != null) {
                    iClickTab1.clickBottom();
                    return;
                }
                return;
            case R.id.llRemoteLeft:
            case R.id.ll_remoteSonyLeft:
            case R.id.ll_remote_left_ss:
            case R.id.rlt_remote_left:
            case R.id.view_fireTVLeft:
                IClickTab1 iClickTab12 = this.mLister1;
                if (iClickTab12 != null) {
                    iClickTab12.clickLeft();
                    return;
                }
                return;
            case R.id.llRemoteOk:
            case R.id.ll_remoteSonyOk:
            case R.id.ll_remote_ok_ss:
            case R.id.rlt_remote_ok:
            case R.id.view_fireTVOk:
                IClickTab1 iClickTab13 = this.mLister1;
                if (iClickTab13 != null) {
                    iClickTab13.clickOK();
                    return;
                }
                return;
            case R.id.llRemoteRight:
            case R.id.ll_remoteSonyRight:
            case R.id.ll_remote_right_ss:
            case R.id.rlt_remote_right:
            case R.id.view_fireTVRight:
                IClickTab1 iClickTab14 = this.mLister1;
                if (iClickTab14 != null) {
                    iClickTab14.clickRight();
                    return;
                }
                return;
            case R.id.llRemoteUp:
            case R.id.ll_remoteSonyUp:
            case R.id.ll_remote_up_ss:
            case R.id.rlt_remote_up:
            case R.id.view_fireTVUp:
                IClickTab1 iClickTab15 = this.mLister1;
                if (iClickTab15 != null) {
                    iClickTab15.clickTop();
                    return;
                }
                return;
            case R.id.rlt_eight:
                IClickTab3 iClickTab3 = this.mLister3;
                if (iClickTab3 != null) {
                    iClickTab3.clickEight();
                    return;
                }
                return;
            case R.id.rlt_five:
                IClickTab3 iClickTab32 = this.mLister3;
                if (iClickTab32 != null) {
                    iClickTab32.clickFive();
                    return;
                }
                return;
            case R.id.rlt_four:
                IClickTab3 iClickTab33 = this.mLister3;
                if (iClickTab33 != null) {
                    iClickTab33.clickFour();
                    return;
                }
                return;
            case R.id.rlt_nine:
                IClickTab3 iClickTab34 = this.mLister3;
                if (iClickTab34 != null) {
                    iClickTab34.clickNine();
                    return;
                }
                return;
            case R.id.rlt_one:
                IClickTab3 iClickTab35 = this.mLister3;
                if (iClickTab35 != null) {
                    iClickTab35.clickOne();
                    return;
                }
                return;
            case R.id.rlt_seven:
                IClickTab3 iClickTab36 = this.mLister3;
                if (iClickTab36 != null) {
                    iClickTab36.clickSeven();
                    return;
                }
                return;
            case R.id.rlt_six:
                IClickTab3 iClickTab37 = this.mLister3;
                if (iClickTab37 != null) {
                    iClickTab37.clickSix();
                    return;
                }
                return;
            case R.id.rlt_three:
                IClickTab3 iClickTab38 = this.mLister3;
                if (iClickTab38 != null) {
                    iClickTab38.clickThree();
                    return;
                }
                return;
            case R.id.rlt_two:
                IClickTab3 iClickTab39 = this.mLister3;
                if (iClickTab39 != null) {
                    iClickTab39.clickTwo();
                    return;
                }
                return;
            case R.id.rlt_zero:
                IClickTab3 iClickTab310 = this.mLister3;
                if (iClickTab310 != null) {
                    iClickTab310.clickZero();
                    return;
                }
                return;
            default:
                return;
        }
    }
}
