package com.diegoprado.messenger.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.text.Editable
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil

class RegisterViewModel (application: Application): AndroidViewModel(application){

    private var userRegister: MutableLiveData<User> = MutableLiveData()

    fun registerNewUser(name: Editable?, email: Editable?, password: Editable?, activityRegister: Activity){
        if (!name?.isEmpty()!!){
            if(!email?.isEmpty()!!){
                if (!password?.isEmpty()!!){

                    userRegister.value?.name = name.toString()
                    userRegister.value?.email = email.toString()
                    userRegister.value?.password = password.toString()

                    FirebaseUtil(activityRegister).saveNewUserFirebase(userRegister)

                }else{
                    Toast.makeText(activityRegister, "Preencha a senha!", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(activityRegister, "Preencha o e-mail!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(activityRegister, "Preencha o nome!", Toast.LENGTH_LONG).show()
        }
    }

}