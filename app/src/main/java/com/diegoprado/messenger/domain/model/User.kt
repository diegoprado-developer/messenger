package com.diegoprado.messenger.domain.model

import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude

class User {

    @get:Exclude
    var id: String? = null
    @get:Exclude
    var password: String? = null

    var name: String? = null
    var email: String? = null

    fun salveNewUser(){
        val firebaseDatabase: DatabaseReference = FirebaseConfig().getFirebaseDatabase()

        firebaseDatabase.child("usuarios")
            .child(this.id!!)
            .setValue(this)
    }
}