package com.diegoprado.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loadActivityRegister(view: View){
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(intent)
    }
}
