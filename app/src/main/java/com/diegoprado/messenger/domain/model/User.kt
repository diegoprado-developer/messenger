package com.diegoprado.messenger.domain.model

import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude

class User {

    @get:Exclude
    var id: String = ""
    @get:Exclude
    var password: String = ""

    var name: String = ""
    var email: String = ""

    fun salveNewUser(){
        val firebaseDatabase: DatabaseReference = FirebaseConfig().getFirebaseDatabase()

        firebaseDatabase.child("usuarios")
            .child(this.id!!)
            .setValue(this)
    }
}