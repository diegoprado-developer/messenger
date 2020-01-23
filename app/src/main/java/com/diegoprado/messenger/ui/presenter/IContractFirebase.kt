package com.diegoprado.messenger.ui.presenter

import android.app.Activity

interface IContractFirebase {

    fun OnSuccess()
    fun OnError(excecao: String)
    fun getContext(): Activity
}