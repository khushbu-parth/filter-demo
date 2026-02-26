package com.cast.tv.screen.mirroring.screencasting.splashExit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import com.cast.tv.screen.mirroring.screencasting.BuildConfig
import com.cast.tv.screen.mirroring.screencasting.R
import com.library.info.CastTvAppManager
import com.library.info.BaseAdActivity

class SplashActivity : BaseAdActivity() {
    private var hasAndroidPermissions: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        hasAndroidPermissions = hasPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        )

//        Handler(Looper.getMainLooper()).postDelayed({
//            gotoAct()
//        }, 5000)

        LoadSplash(
            packageName, this, BuildConfig.VERSION_CODE
        ) { gotoAct() }

    }

    private fun gotoAct() {
        if (!hasAndroidPermissions) {
            startActivity(Intent(this@SplashActivity, iuc_PermissionActivity::class.java))
            finish()
        } else {
//            callNextActivity()
            startActivity(Intent(this@SplashActivity, InfoActivity::class.java))
            finish()
        }
    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        var hasAllPermissions = true
        for (permission in permissions) {
            if (!hasPermission(permission)) {
                hasAllPermissions = false
            }
        }
        return hasAllPermissions
    }

    private fun callNextActivity() {
        val intent: Intent = if (CastTvAppManager.startScreen == 1) {
            Intent(this, InfoActivity::class.java)
        } else {
            Intent(this, InfoActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }


    private fun hasPermission(permission: String): Boolean {
        val res = checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    override fun onBackPressed() {

    }
}
