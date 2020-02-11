package com.diegoprado.messenger.domain.util

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.diegoprado.messenger.data.Base64Custom.Base64Custom
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.ui.presenter.IContractFirebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
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
                        updateUser(user.value?.name.toString())

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

    fun updateUser(name: String): Boolean{

        try {
            val user: FirebaseUser? = getLoggedUser()
            val profileChange: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user?.updateProfile(profileChange)?.addOnCompleteListener {object: OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    if (!task.isSuccessful){
                        Log.d("Update?", "Erro ao atualizar a nome do perfil no firebase")
                    }
                }
            }}

            return true
        }catch (e: java.lang.Exception){

            return false
        }
    }

    fun updateUserPhoto(url: Uri?): Boolean{

        try {
            val user: FirebaseUser? = getLoggedUser()
            val profileChange: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                .setPhotoUri(url)
                .build()

            user?.updateProfile(profileChange)?.addOnCompleteListener {object: OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    if (!task.isSuccessful){
                        Log.d("Update?", "Erro ao atualizar a foto ao firebase")
                    }
                }
            }}

            return true
        }catch (e: java.lang.Exception){

            return false
        }
    }

    fun getUserDataLogged(): User {

        val userOn = getLoggedUser()

        val user = User()
        user.email = userOn?.email.toString()
        user.name = userOn?.displayName.toString()

        if (userOn?.photoUrl == null){
            user.photo = ""
        }else{
            user.photo = userOn.photoUrl.toString()
        }

        return user
    }
}