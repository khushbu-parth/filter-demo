package com.cast.tv.screen.mirroring.screencasting.splashExit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.cast.tv.screen.mirroring.screencasting.R
import com.library.info.AdCallbackListenerCastTv
import com.library.info.CastTvAppManager

class InfoActivity : AppCompatActivity() {

    private lateinit var cb1: CheckBox
    private lateinit var cb2: CheckBox
    private lateinit var btnGuideStart: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(1024, 1024)
        setContentView(R.layout.activity_guide)

        CastTvAppManager.getInstance(this).showNativeAds(
            this,
            findViewById(R.id.fl_native_banner),
            findViewById(R.id.native_space_img),
            1
        )


        cb1 = findViewById(R.id.cb1)
        cb2 = findViewById(R.id.cb2)
        btnGuideStart = findViewById(R.id.btnGuideStart)

        btnGuideStart.setOnClickListener {
            if (cb1.isChecked && cb2.isChecked) {

                CastTvAppManager.getInstance(this).showInterstitialAd(this,
                    AdCallbackListenerCastTv {
                        startActivity(
                            Intent(this, First_Activity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        finish()
                    })

            } else {
                Toast.makeText(this@InfoActivity, "Please check both checkboxes.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        if (CastTvAppManager.exitScreen == 1) {
            CastTvAppManager.getInstance(this).showInterstitialBackAd(this,
                AdCallbackListenerCastTv {
                    startActivity(
                        Intent(this, Exit_Activity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                })
        } else {
//            AppManager.openExitDialog(this)

            startActivity(
                Intent(this, Exit_Activity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )

        }
    }
}
