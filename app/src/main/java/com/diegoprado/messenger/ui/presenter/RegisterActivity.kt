package com.diegoprado.messenger.ui.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.diegoprado.messenger.R
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.viewmodel.LoginViewModel
import com.diegoprado.messenger.ui.viewmodel.RegisterViewModel
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private var editName: TextInputEditText? = null
    private var editEmail: TextInputEditText? = null
    private var editPass: TextInputEditText? = null

    private var btnNewUser: Button? = null

    private lateinit var viewModelRegister: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPass = findViewById(R.id.editPassword)
        btnNewUser = findViewById(R.id.btnNewUser)

        viewModelRegister = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    fun validNewUser(view: View){
        val name = editName?.text
        val email = editEmail?.text
        val password = editPass?.text

        viewModelRegister.registerNewUser(name, email, password, this)
    }
}
