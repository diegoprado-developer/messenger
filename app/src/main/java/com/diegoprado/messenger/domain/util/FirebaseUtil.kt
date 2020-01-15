package com.diegoprado.messenger.domain.util

import android.app.Activity
import android.widget.Toast
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.ui.activity.LoginActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class FirebaseUtil(activity: Activity){

    private lateinit var firebaseAuth: FirebaseAuth
    private var activityContext = activity


    fun authenticFirebaseUser(user: User){
        firebaseAuth = FirebaseConfig().getFirebaseAuth()

        firebaseAuth.signInWithEmailAndPassword(
            user.email.toString(),
            user.password.toString()
        ).addOnCompleteListener(object: OnCompleteListener<AuthResult>{
            override fun onComplete(complete: Task<AuthResult>) {
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
        })
    }

    fun saveNewUserFirebase(user: User){
        firebaseAuth = FirebaseConfig().getFirebaseAuth()
        firebaseAuth.createUserWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener(activityContext, object : OnCompleteListener<AuthResult> {
                override fun onComplete(complete: Task<AuthResult>) {
                    if ( complete.isSuccessful() ){
                        Toast.makeText(activityContext, "Usuario cadastrado com sucesso", Toast.LENGTH_LONG)
                            .show()
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
            })
    }
}