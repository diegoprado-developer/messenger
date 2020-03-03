package com.diegoprado.messenger.domain.util

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.diegoprado.messenger.data.Base64Custom.Base64Custom
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.data.helper.Preferences
import com.diegoprado.messenger.domain.model.Contact
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.ui.presenter.IContractFirebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseUtil {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDataRef: DatabaseReference
    private lateinit var excecao: String

    fun authenticFirebaseUser(user: MutableLiveData<User>, activity: IContractFirebase) {
        firebaseAuth = FirebaseConfig().getFirebaseAuth()

        firebaseAuth.signInWithEmailAndPassword(
            user.value?.email.toString(),
            user.value?.password.toString()).addOnCompleteListener(activity.getContext()) { complete ->
            if (complete.isSuccessful){

                val preference: Preferences = Preferences(activity.getContext())
                val identifyUser = Base64Custom.codificarBase64(user.value?.email.toString())
                preference.saveData(identifyUser)

                activity.OnSuccess()
                activity.getContext().finish()

            }else{

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
                        val identifyNewUser = Base64Custom.codificarBase64(user.value?.email.toString())
                        user.value?.id = identifyNewUser
                        user.value?.salveNewUser()

                        val preference: Preferences = Preferences(activity.getContext())
                        preference.saveData(identifyNewUser)

                        updateUser(user.value?.name.toString())

                        activity.OnSuccess()
                    }catch (e: java.lang.Exception){
                        e.printStackTrace()
                    }

                }else{

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

    fun getUserContacts(): DatabaseReference {
        firebaseDataRef = getFirebaseReference().child("contatos")

       return firebaseDataRef
    }

    fun getFirebaseReference(): DatabaseReference {
        firebaseDataRef = FirebaseConfig().getFirebaseDatabase()

        return firebaseDataRef
    }

    fun contactUserAdd(identify: String, activity: IContractFirebase){
        val identifyUserContact = Base64Custom.codificarBase64(identify)

        firebaseDataRef = getFirebaseReference().child("usuarios").child(identifyUserContact)

        firebaseDataRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(dataError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.value != null){

                    val userContact: User = dataSnapshot.getValue(User::class.java) as User

                    val preferences: Preferences = Preferences(activity.getContext())
                    val userOn = preferences.getIdentify()

                    firebaseDataRef = getFirebaseReference()
                    firebaseDataRef = firebaseDataRef.child("contatos")
                                            .child(userOn.toString())
                                            .child(identifyUserContact)

                    val contact: Contact = Contact()

                    contact.idContact = identifyUserContact
                    contact.emailContact = userContact.email
                    contact.nameContact = userContact.name
                    contact.photoContact = userContact.photo

                    firebaseDataRef.setValue(contact)

                }else{
                   activity.OnError("Usuário não cadastrado!!!")
                }

            }

        })
    }
}