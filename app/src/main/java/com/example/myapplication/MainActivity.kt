package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.BodyFragment
import com.ColorSlider

class MainActivity : AppCompatActivity(), ColorSlider.PositionCallback {


    lateinit var fragmentManager :FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager = supportFragmentManager;
        setContentView(R.layout.activity_main)
        fragmentManager.beginTransaction().replace(R.id.fragment_container, BodyFragment()).commit()
    }

}
