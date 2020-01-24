package com.diegoprado.messenger.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseConfig{

    private var database: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseStorage: StorageReference? = null

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

    fun getFirebaseStorage(): StorageReference{
        if( firebaseStorage == null ){
            firebaseStorage = FirebaseStorage.getInstance().reference
        }
        return firebaseStorage ?: getFirebaseStorage()
    }


}