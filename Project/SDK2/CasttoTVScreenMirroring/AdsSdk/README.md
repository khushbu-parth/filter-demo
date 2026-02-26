# AdsSdk ||Release date 20-02-2023
How to implement AdsSdk in your project.


********************|| Step 1 ||********************

# Add this line in your gradle.properties.

    android.enableJetifier=true

# Add this to root build.gradle // If you are using a newer version of Android Studio, then add it to settings.gradle

    repositories {
    ...
        maven {
            url "https://jitpack.io"
        }
    }

# Change your sir url by following the class below.

    com.ads.sdk.configs => rootApiBase();

    https://yoursirurl.com/ Replace this string to your sir URL

# Initialize SdkManager in your Application class.

    @Override
    public void onCreate() {
        super.onCreate();
            SdkManager.initialize(this, YourLauncherActivity.class);
    }


********************|| Step 2 ||********************

# Extend SplashBaseActivity in your SplashActivity class.

    public class SplashActivity extends SplashBaseActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
    
             loadSplash(BuildConfig.DEBUG, BuildConfig.VERSION_CODE, "1"); //"1" is your app ID
        }
    
        @Override
        public void onComplete() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

# Make sure *BuildConfig.DEBUG* import package is your app package name.


********************|| Step 3 ||********************

# Call ads in your app.

	//For banner ad
 	    SdkManager.loadBanner(MainActivity.this, binding.bannerView);

	//For Native ad
        SdkManager.loadNative(MainActivity.this, binding.nativeView);

	//For Native Banner ad
        SdkManager.loadNativeBanner(MainActivity.this, binding.nativeBannerView);

	//For Interstitial ad
        SdkManager.showInterstitialAd(MainActivity.this, new OnShowAdCompleteListener() {
            @Override
            public void onShowAdComplete() {
                Toast.makeText(MainActivity.this, "Well done", Toast.LENGTH_SHORT).show();
            }
        });


********************|| Step 4 ||********************

# Add this line in your final exit button click.

    exitButton.setOnClickListener(view -> {
        SdkManager.finalExit(MainActivity.this);
    });