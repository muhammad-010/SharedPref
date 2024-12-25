package com.example.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        FILE_NAME, Context.MODE_PRIVATE
    )

    companion object {
        private const val FILE_NAME = "SharedPreferencesApp"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"

        @Volatile
        private var instance: PrefManager? = null;
        fun getInstance(context: Context): PrefManager {
            return instance?: synchronized(this){
                instance?:PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
    fun setLoggedIn(isLoggedIn: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    fun getUsername(): String{
        return sharedPreferences.getString(KEY_USERNAME, "").orEmpty()
    }
    fun getPassword(): String{
        return sharedPreferences.getString(KEY_PASSWORD, "").orEmpty()
    }
    fun saveUsername(username: String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }
    fun savePassword(password: String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, password)
        editor.apply()
    }
    fun clear(){
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }
}