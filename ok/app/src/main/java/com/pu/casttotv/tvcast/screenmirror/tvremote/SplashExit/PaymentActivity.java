package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.adsdemo.vdapps.adsload.api.Ad_Apis;
import com.adsdemo.vdapps.adsload.api.Ad_ApisEndPoint;
import com.adsdemo.vdapps.adsload.api.Ad_onApis;
import com.adsdemo.vdapps.adsload.models.CheckoutDetails;

import com.adsdemo.vdapps.adsload.utils.Ad_PrefUtils;
import com.adsdemo.vdapps.adsload.utils.Ad_Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.models.Packages;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {
    private AlertDialog.Builder alertDialogBuilder;
    Integer ActivityRequestCode = 2;
    CheckoutDetails checkoutDetails;
    Packages packages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

        checkoutDetails = getIntent().getParcelableExtra(Ad_AppPurchaseActivity.CHECKOUT);
        packages = getIntent().getParcelableExtra(Ad_AppPurchaseActivity.PACKAGES);


        if (checkoutDetails.getGatewayType() == 1) {
            doPaymentPayTM(checkoutDetails, packages);
        } else {
            doPaymentRazorpay(checkoutDetails.getRazorpayKey(), packages);
        }
    }

    public void doPaymentRazorpay(String key, Packages packages) {
        try {
            Checkout.preload(getApplicationContext());
            Checkout checkout = new Checkout();
            checkout.setKeyID(key);
            int amount = Integer.parseInt(packages.getPrice());
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency", "INR");
            options.put("amount", amount * 100);
            checkout.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void doPaymentPayTM(CheckoutDetails checkoutDetails, Packages packages) {
        PaytmOrder paytmOrder = new PaytmOrder(checkoutDetails.getOrderId(), checkoutDetails.getMid(), checkoutDetails.getTxnToken(), packages.getPrice(), checkoutDetails.getCallbackUrl());
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle bundle) {
                if (bundle.getString("RESPCODE").equals("01")) {
                    createOrder(checkoutDetails, packages, bundle.getString("TXNID"));
                } else {
                    Toast.makeText(PaymentActivity.this, bundle.getString("RESPMSG"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void networkNotAvailable() {
            }

            @Override
            public void onErrorProceed(String s) {
            }

            @Override
            public void clientAuthenticationFailed(String s) {
            }

            @Override
            public void someUIErrorOccurred(String s) {
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
            }

            @Override
            public void onBackPressedCancelTransaction() {
                startActivity(new Intent(PaymentActivity.this, Ad_AppPurchaseActivity.class));
                finish();
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
            }
        });
        transactionManager.setShowPaymentUrl("https://securegw.paytm.in/theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, ActivityRequestCode);
    }

    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            createOrder(checkoutDetails, packages, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            startActivity(new Intent(PaymentActivity.this, Ad_AppPurchaseActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createOrder(CheckoutDetails checkoutDetails, Packages packages, String paymentId) {
        try {
            HashMap<String, String> createOrder = new HashMap<>();
            createOrder.put("user_id", Ad_PrefUtils.getFromPrefs(PaymentActivity.this, Ad_PrefUtils.USER_ID, "0").toString());
            createOrder.put("app_id", "58");
            createOrder.put("order_id", checkoutDetails.getOrderId());
            createOrder.put("package_id", String.valueOf(packages.getId()));
            createOrder.put("amount", packages.getPrice());
            createOrder.put("payment_transaction_id", paymentId);
            createOrder.put("gateway_name", checkoutDetails.getGatewayType() == 1 ? "razorpay" : "paytm");
            new Ad_Apis(PaymentActivity.this).POST_WITH_FROM_DATA(Ad_ApisEndPoint.BASE_URL + Ad_ApisEndPoint.CREATE_ORDER, createOrder, new Ad_onApis() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("sucess") == 1) {
                            String[] plansDates = Ad_Utils.getCurrentAndFutureDate(Integer.parseInt(packages.getDays()));
                            Ad_PrefUtils.saveToPrefs(PaymentActivity.this, Ad_PrefUtils.PLAN_ACTIVE, true);

                            Ad_PrefUtils.saveToPrefs(PaymentActivity.this, Ad_PrefUtils.PURCHASE_DATE, plansDates[0]);
                            Ad_PrefUtils.saveToPrefs(PaymentActivity.this, Ad_PrefUtils.VALID_DATE, plansDates[1]);

                            Toast.makeText(PaymentActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                            finish();
                        }
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