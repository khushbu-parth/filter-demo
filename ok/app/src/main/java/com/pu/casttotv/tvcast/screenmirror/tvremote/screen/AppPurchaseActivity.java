package com.pu.casttotv.tvcast.screenmirror.tvremote.screen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;


public class AppPurchaseActivity extends AppCompatActivity {

    LinearLayout llFirst, llSecond;
    ImageView ivFirst, ivSecond;
    TextView txtPrize1, txtPrize2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_purchase);

        llFirst = findViewById(R.id.llFirst);
        llSecond = findViewById(R.id.llSecond);


        ivFirst = findViewById(R.id.ivFirst);

        ivSecond = findViewById(R.id.ivSecond);


        txtPrize1 = findViewById(R.id.txtPrize1);
        txtPrize2 = findViewById(R.id.txtPrize2);


        llFirst.setOnClickListener(v -> {
            selectedView(true, false);
        });

        llSecond.setOnClickListener(v -> {
            selectedView(false, true);
        });


        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void selectedView(Boolean first, Boolean second) {
        if (first) {
            llFirst.setBackground(getResources().getDrawable(R.drawable.premium_background_selected));
            ivFirst.setImageResource(R.drawable.premium_round_selected);
            txtPrize1.setTextColor(getResources().getColor(R.color.white));

            llSecond.setBackground(getResources().getDrawable(R.drawable.premium_background));
            ivSecond.setImageResource(R.drawable.premium_round);
            txtPrize2.setTextColor(getResources().getColor(R.color.white));


        } else if (second) {
            llSecond.setBackground(getResources().getDrawable(R.drawable.premium_background_selected));
            ivSecond.setImageResource(R.drawable.premium_round_selected);
            txtPrize2.setTextColor(getResources().getColor(R.color.white));

            llFirst.setBackground(getResources().getDrawable(R.drawable.premium_background));
            ivFirst.setImageResource(R.drawable.premium_round);
            txtPrize1.setTextColor(getResources().getColor(R.color.white));


        }

    }
}