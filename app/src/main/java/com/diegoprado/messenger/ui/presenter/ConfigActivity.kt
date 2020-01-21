package com.diegoprado.messenger.ui.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.diegoprado.messenger.R

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_activity_config)
        setSupportActionBar(toolbar)

        //adc botão voltar na toolbar - adc como filha de mainactivity no manifest
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
