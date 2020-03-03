package com.diegoprado.messenger.domain.model

import com.google.firebase.database.Exclude
import java.io.Serializable

class Contact: Serializable {

    var idContact: String = ""
    var nameContact: String = ""
    var emailContact: String = ""
    var photoContact: String = ""

}