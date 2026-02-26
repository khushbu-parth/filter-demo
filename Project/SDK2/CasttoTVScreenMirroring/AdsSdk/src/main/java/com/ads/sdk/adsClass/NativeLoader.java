package com.ads.sdk.adsClass;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.ads.sdk.R;
import com.ads.sdk.configs.Config;
import com.ads.sdk.configs.PreferenceManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class NativeLoader {
    private static final String TAG = NativeLoader.class.getName();

    public static AdView bannerAdView = null;
    public static NativeAd nativeAdView = null;
    public static NativeAd nativeBannerAdView = null;

    public static Boolean isLoadingBanner = false;
    public static Boolean isLoadingNative = false;
    public static Boolean isLoadingNativeBanner = false;

    public void loadBanner(Activity activity) {
        if (isLoadingBanner) return;

        isLoadingBanner = true;
        if (bannerAdView != null) {
            bannerAdView = null;
        }
        bannerAdView = new AdView(activity);
        bannerAdView.setAdUnitId(PreferenceManager.getBannerID());
        bannerAdView.setAdSize(AdSize.BANNER);
        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();
        bannerAdView.loadAd(adRequest);
        bannerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Config.log(TAG, "Banner onAdFailedToLoad: " + loadAdError.toString());
                if (bannerAdView != null) {
                    bannerAdView = null;
                }
                isLoadingBanner = false;
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Config.log(TAG, "Banner onAdLoaded: ");
                isLoadingBanner = false;
            }
        });
    }

    public void loadNative(Activity activity) {
        if (isLoadingNative) return;

        isLoadingNative = true;
        if (nativeAdView != null) {
            nativeAdView = null;
        }
        AdLoader.Builder builder = new AdLoader.Builder(activity, PreferenceManager.getNativeID());
        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        Config.log(TAG, "Native onNativeAdLoaded: ");
                        nativeAdView = nativeAd;
                        isLoadingNative = false;
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader =
                builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        Config.log(TAG, "Native onAdFailedToLoad: " + loadAdError.toString());
                                        if (nativeAdView != null) {
                                            nativeAdView = null;
                                        }
                                        isLoadingNative = false;
                                    }
                                })
                        .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadNativeBanner(Activity activity) {
        if (isLoadingNativeBanner) return;

        isLoadingNativeBanner = true;
        if (nativeBannerAdView != null) {
            nativeBannerAdView = null;
        }
        AdLoader.Builder builder = new AdLoader.Builder(activity, PreferenceManager.getNativeID());
        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        Config.log(TAG, "Native Banner onNativeAdLoaded: ");
                        nativeBannerAdView = nativeAd;
                        isLoadingNativeBanner = false;
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader =
                builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        Config.log(TAG, "Native Banner onAdFailedToLoad: " + loadAdError.toString());
                                        if (nativeBannerAdView != null) {
                                            nativeBannerAdView = null;
                                        }
                                        isLoadingNativeBanner = false;
                                    }
                                })
                        .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void populateNativeAdView(NativeAd nativeAd, NativeAdView adView, Boolean aBoolean) {
        // Set the media view.
        if (!aBoolean) {
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        }

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();
        // Updates the UI to say whether or not this ad has a video asset.
        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        }
    }
}
