package com.foxhole.runningapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.foxhole.runningapp.databinding.ActivityMainBinding
import com.foxhole.runningapp.ui.RunActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.startRunBtn.setOnClickListener {
            Intent(this, RunActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}