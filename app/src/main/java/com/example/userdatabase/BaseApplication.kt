package com.example.userdatabase

import android.app.Application
import com.example.userdatabase.data.UserDatabase

class BaseApplication : Application() {
    val database: UserDatabase by lazy { UserDatabase.getDatabase(this) }
}