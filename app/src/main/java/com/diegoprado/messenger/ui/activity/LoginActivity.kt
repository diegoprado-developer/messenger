package com.diegoprado.messenger.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.diegoprado.messenger.MainActivity
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

    // manter usuario logado
    override fun onStart() {
        super.onStart()
        var userActual: FirebaseUser? = authFirebase.currentUser

        if (userActual != null){
            loadMainActitty()
        }
    }

    fun loggadUser(view: View){

        var email = email?.text
        var password = password?.text

        val user = User()

        if(!email?.isEmpty()!!){
            if (!password?.isEmpty()!!){

                user.email = email.toString()
                user.password = password.toString()

                FirebaseUtil(this@LoginActivity).authenticFirebaseUser(user)

                loadMainActitty()

            }else{
                Toast.makeText(this@LoginActivity, "Preencha a senha!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@LoginActivity, "Preencha o e-mail!", Toast.LENGTH_LONG).show()
        }
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
