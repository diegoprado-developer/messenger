package com.diegoprado.messenger.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.presenter.IContractFirebase
import com.diegoprado.messenger.ui.presenter.MainActivity

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseUtil: FirebaseUtil = FirebaseUtil()

    fun saveNewContactUser(contact: String, mainActivity: IContractFirebase){
        firebaseUtil.contactUserAdd(contact, mainActivity)
    }

}