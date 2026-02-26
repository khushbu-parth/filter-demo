package com.cast.tv.screen.mirroring.screencasting.UI.help;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;

public class HelpActivity extends BaseActivity {
    private String formatLineSpacing() {
        return "\n\n";
    }

    private String warp() {
        return "\n";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void init() {
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                HelpActivity.this.initHelpActivity(view);
            }
        });
        TextView textView = (TextView) findViewById(R.id.text_content);
        textView.setText(formatTextContent());
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void initHelpActivity(View view) {
        finish();
    }

    private SpannableStringBuilder formatTextContent() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextTitleSpannableStr("Screen mirror"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("Screen mirroring lets you mirror what’s on your phone’s screen to a bigger screen, like a smart TV. Exactly what’s showing on your phone will appear on the TV, whether it’s a video, a photo, or a Settings menu. Whenever you do something on your phone (like navigating to a different app), that action will also be shown on the other screen."));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("Requirements"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" An Android phone or tablet running Android 5.0 or later."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Miracast or WiDi receiver on your TV."));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("Steps to Use"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("1. Connect your mobile device & TV To The Same WiFi Network."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("2. Enable Miracast Display on your TV."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("3. Enable Wireless Display on your mobile device."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("4. On your mobile device, select the TV device that is displayed on the setup screen of the TV."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("5. If you want to exit screen mirroring, you can exit directly from the TV, or disconnect the current device in the screen mirroring option within our app."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextTitleSpannableStr("Cast"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("Casting refers to receiving online content via a digital media player to a TV, projector, or monitor via a wireless connection.\nWhen you’re casting, the video or movie will appear only on the TV. You can control the video on your phone, but you can also close the app and do other things without interrupting the video as long as the phone is still connected. It’s a super convenient way to enjoy both a movie and your phone."));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("Requirements"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" A TV and a mobile device with the minimum system requirements"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Your TV and mobile device must be connected to the same network"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("Steps to Use"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("1. Connect your mobile device & TV To The Same WiFi Network."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("2. Select a TV device that you want to stream to."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("3. Choose a media file you want to play."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("4. You can switch media files using the sidebar navigation in the upper-left-hand corner."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("5. If you would like to autoplay files."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextTitleSpannableStr("FAQ"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("1. Screen Mirroring or Casting Not Working"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Verify the TV Input"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Cast one Device at a Time"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Restart Your Smartphone"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Remove Physical Obstacles"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Disable Bluetooth on the TV"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Restart Your TV"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Reboot Your WiFi Router"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("2. Can’t find my cast device"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Make sure that your device and the TV are connected to the same home network."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Make sure your phone’s VPN is turned off."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" If still not work, please restart your TV, phone or router, and wait 1 minute to try again."));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("3. Can’t connect to TV"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Ensure that your TV has the latest software"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Restart TV"));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextDotSpannableStr("·"));
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr(" Disable your phone’s VPN"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextSubtitleSpannableStr("4. Other questions"));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("If you need any further assistance, feel free to send an E-mail to us."));
        spannableStringBuilder.append((CharSequence) warp());
        spannableStringBuilder.append((CharSequence) setTextContentSpannableStr("You can find “Contact us” through the app’s sidebar navigation."));
        spannableStringBuilder.append((CharSequence) formatLineSpacing());
        return spannableStringBuilder;
    }

    private SpannableString setTextTitleSpannableStr(String str) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(getTitleTextSize()), 0, str.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(getTitleTextColor()), 0, str.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(getTitleTextColor()), 0, str.length(), 33);
        spannableString.setSpan(new StyleSpan(1), 0, str.length(), 33);
        return spannableString;
    }

    private SpannableString setTextSubtitleSpannableStr(String str) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(getSubtitleTextSize()), 0, str.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(getTitleTextColor()), 0, str.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(getTitleTextColor()), 0, str.length(), 33);
        spannableString.setSpan(new StyleSpan(1), 0, str.length(), 33);
        return spannableString;
    }

    private SpannableString setTextContentSpannableStr(String str) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(getContentTextSize()), 0, str.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(getContentTextColor()), 0, str.length(), 33);
        return spannableString;
    }

    private SpannableString setTextDotSpannableStr(String str) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(getDotTextSize()), 0, str.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(getContentTextColor()), 0, str.length(), 33);
        spannableString.setSpan(new StyleSpan(1), 0, str.length(), 33);
        return spannableString;
    }

    private int getTitleTextSize() {
        return ScreenUtil.dip2px(this.mContext, 24.0f);
    }

    private int getSubtitleTextSize() {
        return ScreenUtil.dip2px(this.mContext, 18.0f);
    }

    private int getContentTextSize() {
        return ScreenUtil.dip2px(this.mContext, 14.0f);
    }

    private int getDotTextSize() {
        return ScreenUtil.dip2px(this.mContext, 20.0f);
    }

    private int getTitleTextColor() {
        return getResources().getColor(R.color.color_222222);
    }

    private int getContentTextColor() {
        return getResources().getColor(R.color.color_222222);
    }
}
