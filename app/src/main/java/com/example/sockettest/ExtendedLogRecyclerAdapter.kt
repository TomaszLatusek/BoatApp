package com.example.sockettest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExtendedLogRecyclerAdapter(private val context: Context, private val logs: ArrayList<MyLog>) :
    RecyclerView.Adapter<ExtendedLogRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExtendedLogRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.extended_log_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExtendedLogRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int {
        return logs.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTime: TextView
        var txtLog: TextView
        var txtMsg: TextView

        var txtLogDet: TextView
        var txtLogExplained: TextView
        var txtDescription: TextView

        var extendedLogWrapper: LinearLayout

        init {
            txtTime = itemView.findViewById(R.id.txtTimeExt)
            txtLog = itemView.findViewById(R.id.txtLogExt)
            txtMsg = itemView.findViewById(R.id.txtMsgExt)

            txtLogDet = itemView.findViewById(R.id.txtLogDet)
            txtLogExplained = itemView.findViewById(R.id.txtLogExplained)
            txtDescription = itemView.findViewById(R.id.txtDescription)

            extendedLogWrapper = itemView.findViewById(R.id.logExtendedWrapper)

            txtLog.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(log: MyLog) {
            txtTime.text = log.time
            txtLog.text = log.log
            txtMsg.text = log.msg

            val res = context.resources

            txtLogDet.text =
                res.getString(res.getIdentifier(log.log, "string", context.packageName)) + ":"
            txtLogExplained.text =
                res.getString(res.getIdentifier(log.log + "_expl", "string", context.packageName))
            txtDescription.text =
                res.getString(res.getIdentifier(log.log + "_descr", "string", context.packageName)) + log.extra

            txtLog.setOnClickListener {
                if (extendedLogWrapper.visibility == View.VISIBLE) {
                    extendedLogWrapper.visibility = View.GONE
                    txtTime.setTextColor(Color.parseColor("#ADADAD"))
                    txtMsg.setTextColor(Color.parseColor("#ADADAD"))
                } else {
                    extendedLogWrapper.visibility = View.VISIBLE
                    txtTime.setTextColor(Color.WHITE)
                    txtMsg.setTextColor(Color.WHITE)
                }
            }
        }
    }
}