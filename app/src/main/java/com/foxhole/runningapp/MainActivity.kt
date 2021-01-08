package com.foxhole.runningapp

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.foxhole.runningapp.databinding.ActivityMainBinding
import com.foxhole.runningapp.ui.RunActivity
import com.foxhole.runningapp.utils.Constants.ACTION_SHOW_TRACKING_ACTIVITY
import com.foxhole.runningapp.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.foxhole.runningapp.utils.TrackingUtility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navigateToTrackingActivity(intent)
        requestPermission()

        binding.startRunBtn.setOnClickListener {
            Intent(this, RunActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingActivity(intent)
    }

    private fun navigateToTrackingActivity(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_ACTIVITY){
            Intent(this, RunActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private  fun requestPermission(){
        if (TrackingUtility.hasLocationPermission(this)){
            return
        }else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                EasyPermissions.requestPermissions(
                        this,
                        "You need to accept location permissions to use this app.",
                        REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
            }else {
                EasyPermissions.requestPermissions(
                        this,
                        "You need to accept location permissions to use this app.",
                        REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}