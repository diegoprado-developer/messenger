package com.diegoprado.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.diegoprado.messenger.domain.model.Message
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class ChatViewModel: ViewModel() {

    lateinit var messageRef: DatabaseReference
    lateinit var childEventListenerMessage: ChildEventListener
    var message: Message? = null

    fun createStructMsgs(
        database: DatabaseReference,
        userIdSender: String,
        userIdReceiver: String
    ) {
        messageRef = database.child("mensagens")
            .child(userIdSender)
            .child(userIdReceiver)
    }


    fun retriverMessage(listMessages: ArrayList<Message>) {

        childEventListenerMessage = messageRef.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                message = snapshot.getValue(Message::class.java)

                message?.let { listMessages.add(it) }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}