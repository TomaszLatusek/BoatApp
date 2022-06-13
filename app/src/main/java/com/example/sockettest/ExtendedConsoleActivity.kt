package com.example.sockettest

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ExtendedConsoleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extended_console)

        var console: RecyclerView = findViewById(R.id.recViewExtendedConsole)
        val type: Type = object : TypeToken<List<MyLog?>?>() {}.type
        var logs = Gson().fromJson<Any>(intent.getStringExtra("logs"), type)
        val window: Window = this@ExtendedConsoleActivity.window
        window.navigationBarColor = ContextCompat.getColor(this@ExtendedConsoleActivity, R.color.black)

        val adapter = ExtendedLogRecyclerAdapter(this, logs as ArrayList<MyLog>)
        console.adapter = adapter
        console.layoutManager = LinearLayoutManager(this)

        console.scrollToPosition(logs.size - 1)
    }
}