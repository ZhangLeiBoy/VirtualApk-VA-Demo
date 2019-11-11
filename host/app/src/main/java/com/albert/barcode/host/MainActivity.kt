package com.albert.barcode.host

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.didi.virtualapk.PluginManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.appcompat.v7.Appcompat
import java.io.File


class MainActivity : AppCompatActivity() {
    val PLUGIN_PKG_NAME = "com.albert.barcode.test1"

    //读写权限
    private val PERMISSIONS_STORAGE = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )    //请求状态码
    private val REQUEST_PERMISSION_CODE = 2


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE)
        tv.setOnClickListener {
            Log.e("albert", "click")
            if (PluginManager.getInstance(this).getLoadedPlugin(PLUGIN_PKG_NAME) == null) {
                Toast.makeText(
                    applicationContext,
                    "插件未加载,请尝试重启APP", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val intent = Intent()
            intent.setClassName(
                PLUGIN_PKG_NAME,
                "$PLUGIN_PKG_NAME.MainActivity"
            )
            startActivity(intent)
        }
    }

    private fun installApk() {
        val pluginManager = PluginManager.getInstance(this)
        val apk = File(getExternalStorageDirectory(), "plugin.apk")
        if (apk.exists()) {
            try {
                pluginManager.loadPlugin(apk)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.size >= 2) {
                installApk()
            } else {
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
