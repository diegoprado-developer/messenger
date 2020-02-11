package com.diegoprado.messenger.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.presenter.IContractFirebase
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ConfigViewModel: ViewModel() {

    private lateinit var storageImgRefFirebase: StorageReference
    private lateinit var userIdentify: String

    private lateinit var userLogged: User

    fun storageImgFirebase(image: Bitmap?, activity: IContractFirebase) {
        storageImgRefFirebase = FirebaseConfig().getFirebaseStorage()
        userIdentify = FirebaseUtil().getIdentifyUser()

        //recuperar dados da img para o firebase
        val imageByte = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 70, imageByte)
        val imgData = imageByte.toByteArray()

        //salvar img no firebase
        val storageImg: StorageReference = storageImgRefFirebase
            .child("imagens")
            .child("perfil")
            .child(userIdentify)
            .child("perfil.jpeg")

        val upLoadTask: UploadTask = storageImg.putBytes(imgData)
        upLoadTask.addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: Exception) {
                activity.OnError("Error ao fazer UPLOAD da imagem")
            }
            //usando lambda
        }).addOnSuccessListener { taskSnapshot ->
            val url: Task<Uri>? = taskSnapshot?.metadata?.reference?.downloadUrl

            url?.addOnSuccessListener { urlResultUpdate ->
                updatePhotoUser(urlResultUpdate, activity = activity)
                activity.OnSuccess()
            }
        }
    }

    fun getUserLogged(){
        userLogged = FirebaseUtil().getUserDataLogged()
    }

    fun updatePhotoUser(uri: Uri?, activity: IContractFirebase){
        val isOk = FirebaseUtil().updateUserPhoto(uri)

        if (isOk){
            userLogged.photo = uri.toString()
            userLogged.updateUser()

            activity.OnSuccess()
        }
    }

    fun updateUserOn(name: String){
        userLogged.name = name
        userLogged.updateUser()
    }
}