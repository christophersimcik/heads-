package com

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.myapplication.R
import com.google.android.play.core.internal.i
import kotlinx.android.synthetic.main.test_layout.*

class BodyFragment : Fragment() {

    lateinit var test: TestView
    lateinit var mouthButton : Button
    lateinit var headButton : Button
    lateinit var eyesButton : Button
    lateinit var noseButton : Button

    lateinit var eyeRotation : Button
    lateinit var eyeType : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.test_layout, container, false)
        view.post {
            test.head.initDim()
        }
        test = view.findViewById(R.id.test_view)
        mouthButton = view.findViewById(R.id.button_mouth)
        mouthButton.setOnClickListener{test.select(TestView.MOUTH)}
        headButton = view.findViewById(R.id.button_head)
        headButton.setOnClickListener{test.select(TestView.HEAD)}
        eyesButton = view.findViewById(R.id.button_eye)
        eyesButton.setOnClickListener{test.select(TestView.EYES)}
        noseButton = view.findViewById(R.id.button_nose)
        eyeRotation = view.findViewById(R.id.button_eye_rot)
        eyeRotation.setBackgroundColor(resources.getColor(R.color.design_default_color_primary,null))
        eyeRotation.setOnClickListener{test.rotationToggle(eyeRotation)}
        eyeType = view.findViewById(R.id.button_eye_type)
        eyeType.setBackgroundColor(resources.getColor(R.color.design_default_color_primary,null))
        eyeType.setOnClickListener{test.typeToggle(eyeType)}
        return view
    }

}