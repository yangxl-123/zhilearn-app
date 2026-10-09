package com.example.literacy

import android.app.Application
import com.example.literacy.data.DatabaseProvider

class LiteracyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.init(this)
    }
}
