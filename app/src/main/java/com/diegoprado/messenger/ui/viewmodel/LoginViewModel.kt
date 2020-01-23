package com.diegoprado.messenger.ui.viewmodel

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.presenter.IContractFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private var userLogin: MutableLiveData<User> = MutableLiveData()
    var statuslogin: MutableLiveData<Boolean> = MutableLiveData()


    private var authFirebase: FirebaseAuth = FirebaseConfig().getFirebaseAuth()

    fun loginUser(email: String?, password: String?, loginActivity: IContractFirebase){
        if(!email?.isEmpty()!!){
            if (!password?.isEmpty()!!){

                val user = User()
                user.password = password.toString()
                user.email = email.toString()
                userLogin.value = user


                FirebaseUtil().authenticFirebaseUser(userLogin, loginActivity)

            }else{
               loginActivity.OnError("Senha invalida")
            }
        }else{
            loginActivity.OnError("Email Invalido")
        }
    }

    fun userLoggedIn(){
        val userActual: FirebaseUser? = authFirebase.currentUser

        if (userActual != null){
            statuslogin.value = true
        }
    }
}