package com.diegoprado.messenger.ui.viewmodel

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.presenter.IContractFirebase

class RegisterViewModel (application: Application): AndroidViewModel(application){

    private var userRegister: MutableLiveData<User> = MutableLiveData()

    fun registerNewUser(name: String?, email: String?, password: String?, activityRegister: IContractFirebase){
        if (!name?.isEmpty()!!){
            if(!email?.isEmpty()!!){
                if (!password?.isEmpty()!!){

                    val user = User()

                    user.name = name.toString()
                    user.email = email.toString()
                    user.password = password.toString()

                    userRegister.value = user

                    FirebaseUtil().saveNewUserFirebase(userRegister, activityRegister)

                }else{
                    activityRegister.OnError("Preencha a senha!")
                }
            }else{
                activityRegister.OnError("Preencha o e-mail!")
            }
        }else{
            activityRegister.OnError("Preencha o nome!")
        }
    }

}