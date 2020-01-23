package com.diegoprado.messenger.data.helper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permission {

    companion object {

        fun verifyAccpetedsPermissions(permissions: Array<String>, activity: Activity, requestCode: Int){


            if (Build.VERSION.SDK_INT >= 23){

                val listPermission = arrayListOf<String>()

                //verifica se tem permissão liberada
                for (permission in permissions) {
                    val permissionGranted: Boolean =
                        ContextCompat.checkSelfPermission(activity, permission) ==
                                PackageManager.PERMISSION_GRANTED

                    if (!permissionGranted) listPermission.add(permission)
                }

                // se for vazia não precisa solicitar permissão
                if (listPermission.isEmpty()) return

                val permissionIsNotGranted = arrayOfNulls<String>(listPermission.size)
                listPermission.toArray(permissionIsNotGranted)

                //solicitando permissão
                ActivityCompat.requestPermissions(activity, permissionIsNotGranted, requestCode)
            }
        }
    }

}