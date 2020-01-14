package com.diegoprado.messenger.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.ValidFields
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {

    private var editName: TextInputEditText? = null
    private var editEmail: TextInputEditText? = null
    private var editPass: TextInputEditText? = null

    private var btnNewUser: Button? = null

    private var authFirebase: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPass = findViewById(R.id.editPassword)
        btnNewUser = findViewById(R.id.btnNewUser)

        authFirebase = FirebaseConfig().getFirebaseAuth()

    }

    fun validNewUser(view: View){

        var name = editName?.text
        var email = editEmail?.text
        var password = editPass?.text

        ValidFields(this@RegisterActivity).validNewUser(name, email, password)
    }
}
