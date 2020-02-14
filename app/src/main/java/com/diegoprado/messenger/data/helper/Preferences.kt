package com.diegoprado.messenger.data.helper

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences



class Preferences {

    private lateinit var mContext: Context
    private lateinit var preferences: SharedPreferences
    private val NAME_ARCHIVE = "messenger.preferencias"
    private val MODE = 0
    private lateinit var editor: SharedPreferences.Editor

    private val KEY_IDENTIFY = "identifyUserLogged"

    constructor(context: Context) {

        mContext = context
        preferences = mContext.getSharedPreferences(NAME_ARCHIVE, MODE)
        editor = preferences.edit()

    }

    fun saveData(identifyUser: String) {

        editor.putString(KEY_IDENTIFY, identifyUser)
        editor.commit()

    }

    fun getIdentify(): String? {
        return preferences.getString(KEY_IDENTIFY, null)
    }
}