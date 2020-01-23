package com.diegoprado.messenger.ui.presenter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.diegoprado.messenger.R
import com.diegoprado.messenger.ui.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity(), IContractFirebase {

    private var email: TextInputEditText? = null
    private var password: TextInputEditText? = null
    lateinit var flipper: ViewFlipper

    private lateinit var viewModelLogin: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        flipper = findViewById(R.id.vp)

        email = findViewById(R.id.editLoginEmail)
        password = findViewById(R.id.editLoginPassword)

        viewModelLogin = ViewModelProviders.of(this@LoginActivity).get(LoginViewModel::class.java)

    }

    // manter usuario logado
    override fun onStart() {
        super.onStart()

        viewModelLogin.userLoggedIn()

        viewModelLogin.statuslogin.observe(this@LoginActivity, Observer {
            logged ->

            if(logged){
                OnSuccess()
            }
        })
    }

    fun loggadUser(view: View){

        this.flipper.displayedChild = 1

        viewModelLogin.loginUser(email?.text.toString(), password?.text.toString(), this@LoginActivity)

    }

    fun loadActivityRegister(view: View){
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun OnSuccess() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    override fun OnError(excecao: String) {
        this.flipper.displayedChild = 0
        Toast.makeText(this@LoginActivity, excecao, Toast.LENGTH_LONG).show()
    }

    override fun getContext(): Activity = this@LoginActivity


}
