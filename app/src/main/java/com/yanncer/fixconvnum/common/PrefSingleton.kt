package com.yanncer.fixconvnum.common

import android.content.Context
import android.content.SharedPreferences
import com.yanncer.fixconvnum.common.Constants.PREFS_NAME

object PrefSingleton {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    fun saveBool(objectReference: String?, value: Boolean?) {
        editor!!.putBoolean(objectReference, value!!)
        editor!!.commit()

    }


    private fun saveString(objectReference: String?, value: String?) {
        editor!!.putString(objectReference, value)
        editor!!.commit()
    }

    fun removeString(objectReference: String?) {
        editor!!.remove(objectReference)
        editor!!.commit()
    }

    fun saveInt(objectReference: String?, value: Int) {
        editor!!.putInt(objectReference, value)
        editor!!.commit()
    }

    fun clear() {

        editor!!.clear()
        editor!!.apply()
    }


    fun getBool(objectReference: String?): Boolean {
        return sharedPreferences!!.getBoolean(objectReference, false)
    }

    fun getString(objectReference: String?): String? {
        return sharedPreferences!!.getString(objectReference, "")
    }


}