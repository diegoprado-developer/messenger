package com.diegoprado.messenger.ui.presenter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.helper.Permission
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.viewmodel.ConfigViewModel
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class ConfigActivity : AppCompatActivity(), IContractFirebase {

    private val permissionRequired =
        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)

    private var imgCam: ImageButton? = null
    private var imgGallery: ImageButton? = null

    val CAMERA_REQUEST = 100
    val GALLERY_REQUEST = 200

    private var imgCirclePerfil: CircleImageView? = null
    private var configViewModel: ConfigViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_activity_config)
        setSupportActionBar(toolbar)

        //adc botão voltar na toolbar - adc como filha de mainactivity no manifest
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Permission.verifyAccpetedsPermissions(permissionRequired, this@ConfigActivity, 1)

        configViewModel = ViewModelProviders.of(this@ConfigActivity).get(ConfigViewModel::class.java)

        imgCam = findViewById(R.id.imgButtonCam)
        imgGallery = findViewById(R.id.imgButtonGallery)
        imgCirclePerfil = findViewById(R.id.circleImgViewPhotoProfile)

        val user: FirebaseUser? = FirebaseUtil().getLoggedUser()

        //abrir camera
        imgCam?.setOnClickListener (object: View.OnClickListener {
            override fun onClick(v: View?) {
                val intentCam = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //validar se foi aberta corretamente
                if (intentCam.resolveActivity(packageManager) != null) {
                    startActivityForResult(intentCam, CAMERA_REQUEST)
                }
            }
        })

        imgGallery?.setOnClickListener {
            val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            if (intentGallery.resolveActivity(packageManager) != null){
                startActivityForResult(intentGallery, GALLERY_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK){
            var image: Bitmap? = null

            try {
                when(requestCode){
                    CAMERA_REQUEST -> {
                        image = data?.extras?.get("data") as Bitmap?
                    }
                    GALLERY_REQUEST -> {
                        val localeImgSelect = data?.data

                        try {
                            localeImgSelect?.let {
                                //getbitmap esta deprecated, a partir da API-29, como alternativa esta validação
                                //ImageDecoder é a nova forma
                                if(Build.VERSION.SDK_INT < 28) {
                                    image = MediaStore.Images.Media.getBitmap(
                                        this.contentResolver,
                                        localeImgSelect
                                    )
                                } else {
                                    val source = ImageDecoder.createSource(
                                        this.contentResolver,
                                        localeImgSelect
                                    )
                                    image = ImageDecoder.decodeBitmap(source)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                if (image != null){

                    imgCirclePerfil?.setImageBitmap(image)

                    configViewModel?.storageImgFirebase(image, this@ConfigActivity)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
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

    override fun OnSuccess() {
        Toast.makeText(this@ConfigActivity, "Foto Atualizada", Toast.LENGTH_LONG).show()
    }

    override fun OnError(excecao: String) {
        Toast.makeText(this@ConfigActivity, excecao, Toast.LENGTH_LONG).show()
    }

    override fun getContext(): Activity = this@ConfigActivity
}
