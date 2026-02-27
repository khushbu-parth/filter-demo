package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.adsdemo.vdapps.adsload.AdsManager;

import com.adsdemo.vdapps.adsload.api.Ad_Apis;
import com.adsdemo.vdapps.adsload.api.Ad_ApisEndPoint;
import com.adsdemo.vdapps.adsload.api.Ad_onApis;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.adsdemo.vdapps.adsload.models.CheckoutDetails;

import com.adsdemo.vdapps.adsload.utils.Ad_AutoScrollViewPager;
import com.adsdemo.vdapps.adsload.utils.Ad_PrefUtils;
import com.adsdemo.vdapps.adsload.utils.Ad_Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.adapter.Ad_PagerImage;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.adapter.Ad_PlansAdapter;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.models.Packages;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.models.SliderData;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ad_AppPurchaseActivity extends AppCompatActivity {
    public static final String PACKAGES = "packages";
    public static final String CHECKOUT = "CHECKOUT";
    Ad_Apis apis;
    ArrayList<Packages> packagesLists;

    RecyclerView rvPlans;
    RelativeLayout root;
    Ad_PlansAdapter plansAdapter;
    Ad_AutoScrollViewPager viewPager;
    TabLayout tabDot;
    ArrayList<SliderData> arrayList = new ArrayList<>();
    Packages packageDetail;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mSignInClient;
    FirebaseAuth mAuth;
    String android_id;
    TextView txtSkip, ivDone;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_app_purchase);


        initView();
        callPackage();

        ivDone.setEnabled(false);

        Transition transition = new Fade();
        transition.setDuration(3000);

        transition.addTarget(R.id.txtSkip);
        new Handler().postDelayed(() -> {
            TransitionManager.beginDelayedTransition(root, transition);
            txtSkip.setVisibility(View.VISIBLE);
        }, 3000);


        txtSkip.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });


        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
        mSignInClient = GoogleSignIn.getClient(this, options);

        arrayList.add(new SliderData("", "Smooth Experience\nwith Ad-Free Version", R.drawable.ad_imv_iap_slide3));
        Ad_PagerImage pagerImage = new Ad_PagerImage(Ad_AppPurchaseActivity.this, arrayList);

        viewPager.setAdapter(pagerImage);
        viewPager.setCycle(true);

        tabDot.setupWithViewPager(viewPager);
        ivDone.setOnClickListener(v -> {
            if (!isSignedIn()) {
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            } else {
                generateOrder();
            }

        });


        txtSkip.setOnClickListener(view -> {
            AdsManager.CallInterstitialAdLoad(Ad_AppPurchaseActivity.this, 0, new MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(Ad_AppPurchaseActivity.this, MainActivity.class));
                }
            });
        });


    }

    @Override
    public void onBackPressed() {
        AdsManager.CallInterstitialAdLoad(this, 1, new MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewPager != null) {
            viewPager.startAutoScroll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (viewPager != null) {
            viewPager.stopAutoScroll();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {

                GoogleSignInAccount acct = task.getResult();
                firebaseAuthWithGoogle(acct.getIdToken());
                createUser(acct);
            } else {
                Toast.makeText(Ad_AppPurchaseActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initView() {
        mAuth = FirebaseAuth.getInstance();
        apis = new Ad_Apis(Ad_AppPurchaseActivity.this);

        packagesLists = new ArrayList<>();
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        txtSkip = findViewById(R.id.txtSkip);
        root = findViewById(R.id.root);

        viewPager = findViewById(R.id.viewPager);
        tabDot = findViewById(R.id.tabDot);

        ivDone = findViewById(R.id.ivDone);
        rvPlans = findViewById(R.id.rvPlans);
    }

    private void callPackage() {
        try {
            HashMap<String, String> getPackege = new HashMap<>();
            getPackege.put("app_id", AdsManager.app_id);
            apis.POST_WITH_FROM_DATA(Ad_ApisEndPoint.BASE_URL + Ad_ApisEndPoint.GET_PACKEGE, getPackege, new Ad_onApis() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int status = response.getInt("sucess");
                        if (status == 1) {
                            JSONObject responseJsonObject = new JSONObject(response.getString("data"));
                            JSONArray packageArray = responseJsonObject.getJSONArray("package");
                            for (int i = 0; i < packageArray.length(); i++) {
                                JSONObject packageDetails = packageArray.getJSONObject(i);
                                packagesLists.add(
                                        new Packages(
                                                packageDetails.getString("price"),
                                                packageDetails.getString("description"),
                                                packageDetails.getString("days"),
                                                packageDetails.getInt("id"),
                                                packageDetails.getString("title"),
                                                AdsManager.app_id
                                        )
                                );

                            }
                            plansAdapter = new Ad_PlansAdapter(Ad_AppPurchaseActivity.this, packagesLists, packagesDetail -> {
                                packageDetail = packagesDetail;
                                ivDone.setEnabled(true);
                                ivDone.setBackground(getResources().getDrawable(R.drawable.ad_bg_button_short));
                            });
                            rvPlans.setHasFixedSize(false);

                            rvPlans.setLayoutManager(new LinearLayoutManager(Ad_AppPurchaseActivity.this, LinearLayoutManager.VERTICAL, false));
                            rvPlans.setAdapter(plansAdapter);
                        }
                    } catch (JSONException ignored) {

                    }
                }

                @Override
                public void onErrorResponse(String error) {

                }
            });
        } catch (JSONException ignored) {

        }

    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(Ad_AppPurchaseActivity.this) != null;
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        Toast.makeText(Ad_AppPurchaseActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void generateOrder() {
        try {
            String OrderId = Ad_Utils.generateAlphaNumericString(10);
            HashMap<String, String> generateOrder = new HashMap<>();
            generateOrder.put("app_id", String.valueOf(packageDetail.getAppId()));
            generateOrder.put("order_id", OrderId);
            generateOrder.put("order_amount", packageDetail.getPrice());
            apis.POST_WITH_FROM_DATA(Ad_ApisEndPoint.BASE_URL + Ad_ApisEndPoint.GET_CHECK_SUM, generateOrder, new Ad_onApis() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String txnToken = new JSONObject(new JSONObject(response.getString("paytmResponse")).getString("body")).getString("txnToken");
                        CheckoutDetails checkoutDetails = new CheckoutDetails(
                                response.getString("mid"),
                                response.getString("callbackUrl"),
                                txnToken,
                                response.getString("razorpayKey"),
                                response.getInt("gatewayType"),
                                OrderId
                        );
                        startActivity(
                                new Intent(
                                        Ad_AppPurchaseActivity.this,
                                        PaymentActivity.class
                                )
                                        .putExtra(PACKAGES, packageDetail)
                                        .putExtra(CHECKOUT, checkoutDetails)
                        );
                        finish();
                    } catch (Exception ignored) {

                    }
                }

                @Override
                public void onErrorResponse(String error) {
                }
            });
        } catch (Exception ignored) {
        }

    }

    public void createUser(GoogleSignInAccount acct) {
        try {
            HashMap<String, String> createUser = new HashMap<>();
            createUser.put("app_id", packageDetail.getAppId());
            createUser.put("email", acct.getEmail());
            createUser.put("token", acct.getIdToken());
            createUser.put("device_id", android_id);
            apis.POST_WITH_FROM_DATA(Ad_ApisEndPoint.BASE_URL + Ad_ApisEndPoint.LOGIN_EMAIL, createUser, new Ad_onApis() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Ad_PrefUtils.saveToPrefs(Ad_AppPurchaseActivity.this, Ad_PrefUtils.USER_ID, response.getString("id"));
                        generateOrder();
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void onErrorResponse(String error) {
                }
            });
        } catch (Exception ignored) {
        }
    }
}