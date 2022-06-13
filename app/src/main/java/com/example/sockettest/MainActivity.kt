package com.example.sockettest

import SocketHandler
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var logs = ArrayList<MyLog>()
    lateinit var console: RecyclerView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sliderLeft = findViewById<Slider>(R.id.sliderLeft)
        val sliderRight = findViewById<Slider>(R.id.sliderRight)
        console = findViewById(R.id.recviewConsole)
        val image = findViewById<ImageView>(R.id.img)
        val btnImage = findViewById<ImageView>(R.id.btnImage)
        val btnServo = findViewById<ImageView>(R.id.btnServo)
        val btnGallery = findViewById<TextView>(R.id.btnGallery)
        val window: Window = this@MainActivity.window
        window.navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.darker_grey)

//        if (savedInstanceState != null) {
//            logs = savedInstanceState.getParcelableArrayList<MyLog>("SAVED_RECYCLER_VIEW_DATASET_ID") as ArrayList<MyLog>
//        }

        val adapter = LogRecyclerAdapter(this, logs)
        console.adapter = adapter
        console.layoutManager = LinearLayoutManager(this)

        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()
        mSocket.connect()

        btnImage.setOnClickListener {
            mSocket.emit("controlImage")
        }

        btnServo.setOnClickListener {
            mSocket.emit("controlServo")
            Log.d("LOGG", logs.size.toString())
        }

        btnGallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

        image.setOnClickListener {

            val intent = Intent(this, ShowImageActivity::class.java).apply {
                putExtra("imageFile", ImageUtils.getLastImageFromFolder(this@MainActivity, "BoatApp"))
            }
            startActivity(intent)
        }

        console.setOnTouchListener(object : OnTouchListener {
            private val gestureDetector =
                GestureDetector(this@MainActivity, object : SimpleOnGestureListener() {

                    override fun onSingleTapUp(e: MotionEvent?): Boolean {
                        val intent = Intent(this@MainActivity, ExtendedConsoleActivity::class.java).apply {
                            putExtra("logs", Gson().toJson(logs))
                        }
                        startActivity(intent)
                        return super.onSingleTapUp(e)
                    }
                })

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(event)
                return true
            }
        })

        sliderLeft.addOnChangeListener(Slider.OnChangeListener { slider, _, _ ->
            Log.d(
                "SLIDER",
                "L" + slider.value.toString()
            )
            val data: String =
                "{\"left\": ${slider.value}, \"right\": ${sliderRight.value}}"
            val json: JSONObject = JSONObject(data)
            mSocket.emit("controlMovement", json)
        })

        sliderRight.addOnChangeListener(Slider.OnChangeListener { slider, _, _ ->
            Log.d(
                "SLIDER",
                "R" + slider.value.toString()
            )
            val data: String =
                "{\"left\": ${sliderLeft.value}, \"right\": ${slider.value}}"
            val json: JSONObject = JSONObject(data)
            mSocket.emit("controlMovement", json)
        })

        sliderLeft.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                slider.value = 0f
            }
        })

        sliderRight.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                slider.value = 0f
            }
        })

        mSocket.on("boatImage") { args ->
            val bytes: ByteArray = args[0] as ByteArray
            val x: ByteArray = Base64.decode(bytes, Base64.DEFAULT)
            val bm = BitmapFactory.decodeByteArray(x, 0, x.size)

            runOnUiThread {
                image.visibility = View.VISIBLE
                image.setImageBitmap(bm)
                ImageUtils.saveImage(bm, this, "BoatApp")
                object : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        image.visibility = View.GONE
                    }
                }.start()
            }
        }

        mSocket.on("boatLog") { args ->
            if (args[0] != null) {
                val data = args[0].toString()
                val jsonarray = JSONArray(data)
                val time = jsonarray[0] as String
                val log = jsonarray[1] as String
                val msg = jsonarray[2] as String
                var extra = ""
                if (jsonarray.length() == 4) {
                    extra = jsonarray[3] as String
                }

                runOnUiThread {
                    logs.add(MyLog(time, log, msg, extra))
                    adapter.notifyDataSetChanged()
                    console.scrollToPosition(logs.count() - 1)
                }
            }
        }
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putParcelableArrayList("SAVED_RECYCLER_VIEW_DATASET_ID", logs)
//    }
}