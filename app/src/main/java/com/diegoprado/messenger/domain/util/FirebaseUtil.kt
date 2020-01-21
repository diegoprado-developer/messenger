package com.diegoprado.messenger.domain.util

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.diegoprado.messenger.data.Base64Custom.Base64Custom
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.ui.presenter.LoginActivity
import com.google.firebase.auth.*

class FirebaseUtil(activity: Activity){

    private lateinit var firebaseAuth: FirebaseAuth
    private var activityContext = activity


    fun authenticFirebaseUser(user: MutableLiveData<User>){
        firebaseAuth = FirebaseConfig().getFirebaseAuth()

        firebaseAuth.signInWithEmailAndPassword(
            user.value?.email.toString(),
            user.value?.password.toString()
        ).addOnCompleteListener { complete ->
            if (complete.isSuccessful){
                LoginActivity().loadMainActitty()
            }else{
                var excecao: String = ""

                try {
                    throw complete.exception!!
                }catch (e: FirebaseAuthInvalidCredentialsException){
                    excecao = "E-mail e senha não correspondem a usuário cadastrado"
                }catch (e: FirebaseAuthInvalidUserException){
                    excecao = "Usuário não está cadastrado"
                }catch (e: Exception) {
                    excecao = "Erro ao Logar usuario: " + e.message
                    e.printStackTrace()
                }
                Toast.makeText(activityContext, excecao, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveNewUserFirebase(user: MutableLiveData<User>){
        firebaseAuth = FirebaseConfig().getFirebaseAuth()
        firebaseAuth.createUserWithEmailAndPassword(user.value?.email.toString(), user.value?.password.toString())
            .addOnCompleteListener(activityContext) { complete ->
                if ( complete.isSuccessful ){
                    Toast.makeText(activityContext, "Usuario cadastrado com sucesso", Toast.LENGTH_LONG)
                        .show()

                    try {
                        val identifyUser = Base64Custom.codificarBase64(user.value?.email.toString())
                        user.value?.id = identifyUser
                        user.value?.salveNewUser()

                        activityContext.finish()
                    }catch (e: java.lang.Exception){
                        e.printStackTrace()
                    }

                }else{
                    var excecao: String = ""

                    try {
                        throw complete.exception!!
                    }catch (e: FirebaseAuthWeakPasswordException){
                        excecao = "Digite uma senha mais forte"
                    }catch (e: FirebaseAuthInvalidCredentialsException){
                        excecao = "Digite um e-mail valido"
                    }catch (e: FirebaseAuthUserCollisionException){
                        excecao = "Conta já cadastradada"
                    }catch (e: Exception) {
                        excecao = "Erro ao cadastrar usuario: " + e.message
                        e.printStackTrace()
                    }

                    Toast.makeText(activityContext, excecao, Toast.LENGTH_LONG).show()
                }
            }
    }
}