package com.example.dacs3.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.dacs3.model.User
import com.google.gson.Gson

class SharedPref(activity: Activity) {
    val login = "login"
    val nama = "name"
    val email = "email"
    val user = "user"

    val mypref = "MAIN_PRF"
    val sp: SharedPreferences

    init {
        sp = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }
    fun setUser(value: User) {
        val data: String = Gson().toJson(value, User::class.java)
        sp.edit().putString(user, data).apply()
    }

    fun getUser(): User? {
        val data: String = sp.getString(user, null) ?: return null
        return Gson().fromJson<User>(data, User::class.java)
    }
}