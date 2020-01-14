package com.diegoprado.messenger.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseConfig{

    private var database: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null

    fun getFirebaseDatabase(): DatabaseReference{
        if( database == null ){
            database = FirebaseDatabase.getInstance().reference
        }
        return database ?: getFirebaseDatabase()
    }

    fun getFirebaseAuth(): FirebaseAuth{
        if( firebaseAuth == null ){
            firebaseAuth = FirebaseAuth.getInstance()
        }
        return firebaseAuth ?: getFirebaseAuth()
    }
}