package com.foxhole.runningapp.ui

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.foxhole.runningapp.R
import com.foxhole.runningapp.databinding.ActivityRunBinding
import com.google.android.gms.maps.GoogleMap

class RunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunBinding
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        makeFullScreen()
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
        }
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    fun makeFullScreen(){
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}