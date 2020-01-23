package com.diegoprado.messenger.ui.presenter

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.helper.Permission
import java.util.jar.Manifest

class ConfigActivity : AppCompatActivity() {

    private val permissionRequired =
        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_activity_config)
        setSupportActionBar(toolbar)

        //adc botão voltar na toolbar - adc como filha de mainactivity no manifest
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Permission.verifyAccpetedsPermissions(permissionRequired, this@ConfigActivity, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (permissionResult in grantResults){
            if (permissionResult == PackageManager.PERMISSION_DENIED){
                alertPermission()
            }
        }
    }

    fun alertPermission(){
        val builder = AlertDialog.Builder(this@ConfigActivity)
        builder.setTitle("Permissão Negada")
        builder.setMessage("Para poder utilizar o APP é preciso aceitar as permissões")
        builder.setCancelable(false)
        builder.setPositiveButton("CONFIRMAR"){dialog, which ->
            finish()
        }

//        builder.setNegativeButton("NÃO"){dialog,which ->
//        }
//        builder.setNeutralButton("CANCELAR"){dialog,which ->
//        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }
}
