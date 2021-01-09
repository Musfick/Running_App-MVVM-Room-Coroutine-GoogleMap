package com.foxhole.runningapp.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.foxhole.runningapp.MainActivity
import com.foxhole.runningapp.R
import com.foxhole.runningapp.databinding.ActivityRunBinding
import com.foxhole.runningapp.services.Polyline
import com.foxhole.runningapp.services.TrackingService
import com.foxhole.runningapp.utils.Constants.ACTION_PAUSE_SERVICE
import com.foxhole.runningapp.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.foxhole.runningapp.utils.Constants.ACTION_STOP_SERVICE
import com.foxhole.runningapp.utils.Constants.POLYLINE_COLOR
import com.foxhole.runningapp.utils.Constants.POLYLINE_WIDTH
import com.foxhole.runningapp.utils.TrackingUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunBinding
    private var map: GoogleMap? = null

    private var isTracking = false
    private var pathPoint = mutableListOf<Polyline>()
    private var curTimeInMillis = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
            addAllPolyLines()
        }

        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }
        subscribeToObserves()

        binding.cancelButton.setOnClickListener {
            showCancelTrackingDialog()
        }

        if (curTimeInMillis > 0L){
            binding.cancelButton.visibility = View.VISIBLE
        }
    }

    private fun subscribeToObserves(){
        TrackingService.isTracking.observe(this, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoint.observe(this, Observer {
            pathPoint = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(this, Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            binding.durationTv.text = formattedTime
        })
    }

    private fun toggleRun(){
        if (isTracking){
            binding.cancelButton.visibility = View.VISIBLE
            sendCommendToService(ACTION_PAUSE_SERVICE)
        }else {
            sendCommendToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun showCancelTrackingDialog(){
        val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("Cancel the Run ?")
                .setMessage("Are you sure to cancel the current run and delete all its data")
                .setPositiveButton("Yes"){ _, _ ->
                    stopRun()
                }
                .setNegativeButton("No"){ dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .create()
        dialog.show()
    }

    private fun stopRun(){
        sendCommendToService(ACTION_STOP_SERVICE)
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if (!isTracking){
            binding.btnToggleRun.text = "Start"
            binding.finishRunBtn.isEnabled = false
        }else {
            binding.cancelButton.visibility = View.VISIBLE
            binding.btnToggleRun.text = "Stop"
            binding.finishRunBtn.isEnabled = true
        }
    }

    private fun moveCameraToUser(){
        if (pathPoint.isNotEmpty() && pathPoint.last().isNotEmpty()){
            map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            pathPoint.last().last(),
                            15f
                    )
            )
        }
    }

    private fun addAllPolyLines(){
        for (polyline in pathPoint){
            val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline(){
        if (pathPoint.isNotEmpty() && pathPoint.last().size > 1){
            val preLastLatLng = pathPoint.last()[pathPoint.last().size - 2]
            val lastLatLng = pathPoint.last().last()
            val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .add(preLastLatLng)
                    .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommendToService(action: String){
        Intent(this, TrackingService::class.java).also {
            it.action = action
            startService(it)
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