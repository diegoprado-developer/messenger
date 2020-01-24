package com.diegoprado.messenger.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.presenter.ConfigActivity
import com.diegoprado.messenger.ui.presenter.IContractFirebase
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ConfigViewModel: ViewModel() {

    private lateinit var storageImgRefFirebase: StorageReference
    private lateinit var userIdentify: String


    fun storageImgFirebase(image: Bitmap?, activity: IContractFirebase){
        storageImgRefFirebase = FirebaseConfig().getFirebaseStorage()
        userIdentify = FirebaseUtil().getIdentifyUser()

        //recuperar dados da img para o firebase
        val imageByte: ByteArrayOutputStream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 70, imageByte)
        val imgData = imageByte.toByteArray()

        //salvar img no firebase
        val storageImg: StorageReference = storageImgRefFirebase
            .child("imagens")
            .child("perfil")
            .child(userIdentify)
            .child("perfil.jpeg")

        val upLoadTask: UploadTask = storageImg.putBytes(imgData)
        upLoadTask.addOnFailureListener(object: OnFailureListener {
            override fun onFailure(p0: Exception) {
                activity.OnError("Error ao fazer UPLOAD da imagem")
            }
        }).addOnSuccessListener(object: OnSuccessListener<UploadTask.TaskSnapshot>{
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {

                val url: Task<Uri>? = taskSnapshot?.metadata?.reference?.downloadUrl?.addOnSuccessListener {  }
                url?.addOnSuccessListener (object: OnSuccessListener<Uri>{
                    override fun onSuccess(p0: Uri?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })

                activity.OnSuccess()
            }
        })

    }

}