package com.diegoprado.messenger.domain.util

import androidx.lifecycle.MutableLiveData
import com.diegoprado.messenger.data.Base64Custom.Base64Custom
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.ui.presenter.IContractFirebase
import com.google.firebase.auth.*

class FirebaseUtil {

    private lateinit var firebaseAuth: FirebaseAuth

    fun authenticFirebaseUser(user: MutableLiveData<User>, activity: IContractFirebase) {
        firebaseAuth = FirebaseConfig().getFirebaseAuth()

        firebaseAuth.signInWithEmailAndPassword(
            user.value?.email.toString(),
            user.value?.password.toString()).addOnCompleteListener(activity.getContext()) { complete ->
            if (complete.isSuccessful){

                activity.OnSuccess()
                activity.getContext().finish()

            }else{

                var excecao = ""

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
               activity.OnError(excecao)
            }
        }
    }

    fun saveNewUserFirebase(user: MutableLiveData<User>, activity: IContractFirebase){
        firebaseAuth = FirebaseConfig().getFirebaseAuth()
        firebaseAuth.createUserWithEmailAndPassword(user.value?.email.toString(), user.value?.password.toString())
            .addOnCompleteListener(activity.getContext()) { complete ->
                if ( complete.isSuccessful ){
                    try {
                        val identifyUser = Base64Custom.codificarBase64(user.value?.email.toString())
                        user.value?.id = identifyUser
                        user.value?.salveNewUser()

                        activity.OnSuccess()
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

                    activity.OnError(excecao)
                }
            }
    }

    fun getIdentifyUser(): String{

        val userAuth: FirebaseAuth = FirebaseConfig().getFirebaseAuth()
        val user: String = userAuth.currentUser?.email.toString()
        val identifyUser: String = Base64Custom.codificarBase64(user)

        return identifyUser
    }

    fun getLoggedUser(): FirebaseUser? {
        val userAuth: FirebaseAuth = FirebaseConfig().getFirebaseAuth()
        return userAuth.currentUser
    }
}