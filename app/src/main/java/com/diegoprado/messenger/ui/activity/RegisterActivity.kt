package com.diegoprado.messenger.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*

class RegisterActivity : AppCompatActivity() {

    private var editName: TextInputEditText? = null
    private var editEmail: TextInputEditText? = null
    private var editPass: TextInputEditText? = null

    private var btnNewUser: Button? = null


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

    }

    fun validNewUser(view: View){

        val name = editName?.text
        val email = editEmail?.text
        val password = editPass?.text

        val user = User()

        if (!name?.isEmpty()!!){
            if(!email?.isEmpty()!!){
                if (!password?.isEmpty()!!){

                    user.name = name.toString()
                    user.email = email.toString()
                    user.password = password.toString()

                    FirebaseUtil(this@RegisterActivity).saveNewUserFirebase(user)

                }else{
                    Toast.makeText(this@RegisterActivity, "Preencha a senha!", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this@RegisterActivity, "Preencha o e-mail!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@RegisterActivity, "Preencha o nome!", Toast.LENGTH_LONG).show()
        }
    }
}
