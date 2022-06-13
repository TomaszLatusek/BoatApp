package com.example.sockettest

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class LogRecyclerAdapter(private val context: Context, private val logs: ArrayList<MyLog>) :
    RecyclerView.Adapter<LogRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.log_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: LogRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int {
        return logs.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTime: TextView
        var txtLog: TextView
        var txtMsg: TextView
        var logWrapper: LinearLayout

        init {
            txtTime = itemView.findViewById(R.id.txtTime)
            txtLog = itemView.findViewById(R.id.txtLog)
            txtMsg = itemView.findViewById(R.id.txtMsg)
            logWrapper = itemView.findViewById(R.id.logWrapper)
        }

        fun bind(log: MyLog) {
            txtTime.text = log.time
            txtLog.text = log.log
            txtMsg.text = log.msg

            logWrapper.setOnClickListener {
                val intent = Intent(context, ExtendedConsoleActivity::class.java).apply {
                    putExtra("logs", Gson().toJson(logs))
                }
                context.startActivity(intent)
            }
        }
    }
}
