package com.diegoprado.messenger.ui.presenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.diegoprado.messenger.R
import com.diegoprado.messenger.ui.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private var email: TextInputEditText? = null
    private var password: TextInputEditText? = null

    private lateinit var viewModelLogin: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.editLoginEmail)
        password = findViewById(R.id.editLoginPassword)

        viewModelLogin = ViewModelProviders.of(this).get(LoginViewModel::class.java)

    }

    // manter usuario logado
    override fun onStart() {
        super.onStart()

        viewModelLogin.userLoggedIn()

        viewModelLogin.statuslogin.observe(this, Observer {
            logged ->

            if(logged){
                loadMainActitty()
            }
        })
    }

    fun loggadUser(view: View){

        val email = email?.text
        val password = password?.text

        viewModelLogin.loginUser(email, password, this@LoginActivity)

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
