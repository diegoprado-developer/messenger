package com.diegoprado.messenger.domain.util

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.diegoprado.messenger.domain.model.User

class ValidFields(activity: Activity){

    var activityContext = activity


    fun validLoggin(email: Editable?,password: Editable?){
        val user = User()

        if(!email?.isEmpty()!!){
            if (!password?.isEmpty()!!){

                user.email = email.toString()
                user.password = password.toString()

                FirebaseUtil(activityContext).authenticFirebaseUser(user)

            }else{
                Toast.makeText(activityContext, "Preencha a senha!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(activityContext, "Preencha o e-mail!", Toast.LENGTH_LONG).show()
        }
    }

    fun validNewUser(name: Editable?,email: Editable?,password: Editable?){
        val user = User()

        if (!name?.isEmpty()!!){
            if(!email?.isEmpty()!!){
                if (!password?.isEmpty()!!){

                    user.name = name.toString()
                    user.email = email.toString()
                    user.password = password.toString()

                    FirebaseUtil(activityContext).saveNewUserFirebase(user)

                }else{
                    Toast.makeText(activityContext, "Preencha a senha!", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(activityContext, "Preencha o e-mail!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(activityContext, "Preencha o nome!", Toast.LENGTH_LONG).show()
        }

    }
}
