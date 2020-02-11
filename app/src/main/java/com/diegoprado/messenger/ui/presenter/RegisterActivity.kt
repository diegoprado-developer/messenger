package com.diegoprado.messenger.ui.presenter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.diegoprado.messenger.R
import com.diegoprado.messenger.ui.viewmodel.RegisterViewModel
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity(), IContractFirebase {

    private var editName: TextInputEditText? = null
    private var editEmail: TextInputEditText? = null
    private var editPass: TextInputEditText? = null

    private var btnNewUser: Button? = null

    private var loader: FrameLayout? = null

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
        loader = findViewById(R.id.icdLoader)

        viewModelRegister = ViewModelProviders.of(this@RegisterActivity).get(RegisterViewModel::class.java)
    }

    fun validNewUser(view: View){
        val name = editName?.text.toString()
        val email = editEmail?.text.toString()
        val password = editPass?.text.toString()

        loader?.visibility = View.VISIBLE
        btnNewUser?.visibility = View.GONE

        viewModelRegister.registerNewUser(name, email, password, this@RegisterActivity)
    }

    override fun OnSuccess() {
        Toast.makeText(this@RegisterActivity, "Usuario cadastrado com sucesso", Toast.LENGTH_LONG).show()
        this.finish()
    }

    override fun OnError(excecao: String) {
        loader?.visibility = View.GONE
        btnNewUser?.visibility = View.VISIBLE

        Toast.makeText(this@RegisterActivity, excecao, Toast.LENGTH_LONG).show()
    }

    override fun getContext(): Activity {
        return this@RegisterActivity
    }
}
