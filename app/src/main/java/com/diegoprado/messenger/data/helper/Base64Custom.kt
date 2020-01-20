package com.diegoprado.messenger.data.Base64Custom

import android.util.Base64

class Base64Custom {

    companion object {
        @JvmStatic
        fun codificarBase64(texto: String): String {
            return Base64.encodeToString(texto.toByteArray(), Base64.DEFAULT)
                .replace("(\\n|\\r)".toRegex(), "")
        }

        fun decodificarBase64(textoDecodificado: String): String {
            return String(Base64.decode(textoDecodificado, Base64.DEFAULT))

        }
    }
}