package com.example.sockettest

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.max
import kotlin.math.min


class ShowImageActivity : AppCompatActivity() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        img= findViewById(R.id.imgFullscreen)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        val window: Window = this@ShowImageActivity.window
        window.navigationBarColor = ContextCompat.getColor(this@ShowImageActivity, R.color.black)
        val imageFile = intent.getSerializableExtra("imageFile")

        GlideApp.with(this@ShowImageActivity).load(imageFile).into(img)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            img.scaleX = scaleFactor
            img.scaleY = scaleFactor
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            scaleFactor = 1.0f
            img.scaleX = scaleFactor
            img.scaleY = scaleFactor
        }
    }
}