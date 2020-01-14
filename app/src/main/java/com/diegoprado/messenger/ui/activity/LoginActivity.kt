package com.diegoprado.messenger.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.diegoprado.messenger.MainActivity
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.util.ValidFields
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var email: TextInputEditText? = null
    private var password: TextInputEditText? = null

    private lateinit var authFirebase: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.editLoginEmail)
        password = findViewById(R.id.editLoginPassword)

        authFirebase = FirebaseConfig().getFirebaseAuth()
    }

    fun loggadUser(view: View){

        var email = email?.text
        var password = password?.text

        ValidFields(this@LoginActivity).validLoggin(email, password)
    }

    fun loadActivityRegister(view: View){
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun loadMainActitty(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}
