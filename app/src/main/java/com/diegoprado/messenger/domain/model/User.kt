package com.diegoprado.messenger.domain.model

import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.HashMap

class User {

    @get:Exclude
    var id: String = ""
    @get:Exclude
    var password: String = ""

    var name: String = ""
    var email: String = ""
    var photo: String = ""

    fun salveNewUser(){
        val firebaseDatabase: DatabaseReference = FirebaseConfig().getFirebaseDatabase()

        firebaseDatabase.child("usuarios")
            .child(this.id!!)
            .setValue(this)
    }

    fun updateUser(){
        val identifyUser = FirebaseUtil().getIdentifyUser()
        val database = FirebaseConfig().getFirebaseDatabase()
        val userRef = database.child("usuarios")
                        .child(identifyUser)

        val valuesUser = convertMap()

        userRef.updateChildren(valuesUser)
    }

    @Exclude
    fun convertMap(): Map<String, Any> {

        val userMap = HashMap<String, Any>()
        userMap.put("email", email)
        userMap.put("name", name)
        userMap.put("photo", photo)

        return userMap
    }

}