package com.diegoprado.messenger.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.text.Editable
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var userLogin: MutableLiveData<User> = MutableLiveData()
    var statuslogin: MutableLiveData<Boolean> = MutableLiveData()

    private var authFirebase: FirebaseAuth = FirebaseConfig().getFirebaseAuth()

    fun loginUser(email: Editable?, password: Editable?, loginActivity: Activity){
        if(!email?.isEmpty()!!){
            if (!password?.isEmpty()!!){

                userLogin.value?.email = email.toString()
                userLogin.value?.password = password.toString()

                FirebaseUtil(loginActivity).authenticFirebaseUser(userLogin)
            }else{
                Toast.makeText(loginActivity, "Preencha a senha!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(loginActivity, "Preencha o e-mail!", Toast.LENGTH_LONG).show()
        }
    }

    fun userLoggedIn(){
        val userActual: FirebaseUser? = authFirebase.currentUser

        if (userActual != null){
            statuslogin.value = true
        }
    }

}