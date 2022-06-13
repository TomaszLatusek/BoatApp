package com.example.sockettest

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class MyLog() : Parcelable {
    lateinit var time: String
    lateinit var log: String
    lateinit var msg: String
    lateinit var extra: String

    constructor(parcel: Parcel) : this() {
        time = parcel.readString().toString()
        log = parcel.readString().toString()
        msg = parcel.readString().toString()
        extra = parcel.readString().toString()
    }

    constructor(time: String, log: String, msg: String, extra: String) : this() {
        this.time = time
        this.log = log
        this.msg = msg
        this.extra = extra
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeString(log)
        parcel.writeString(msg)
        parcel.writeString(extra)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<MyLog> {
        override fun createFromParcel(parcel: Parcel): MyLog {
            return MyLog(parcel)
        }

        override fun newArray(size: Int): Array<MyLog?> {
            return arrayOfNulls(size)
        }
    }

}