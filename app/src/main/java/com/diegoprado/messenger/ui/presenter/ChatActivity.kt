package com.diegoprado.messenger.ui.presenter

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.Base64Custom.Base64Custom
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.domain.model.Contact
import com.diegoprado.messenger.domain.model.Message
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.adapter.ChatAdapter
import com.diegoprado.messenger.ui.viewmodel.ChatViewModel
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var textNameChat: TextView
    private lateinit var imgChat: CircleImageView
    private lateinit var editMessage: EditText
    private lateinit var userChat: Contact

    private var firebaseUtil = FirebaseUtil()
    private lateinit var database: DatabaseReference

    private lateinit var viewModelLogin: ChatViewModel

    private lateinit var userIdSender: String
    private lateinit var userIdReceiver: String

    private lateinit var recycleMessage: RecyclerView
    private lateinit var adapter: ChatAdapter
    private var listMessages: ArrayList<Message> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        toolbar.setTitle("")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textNameChat = findViewById(R.id.nameChat)
        imgChat = findViewById(R.id.photoChat)
        editMessage = findViewById(R.id.editMessage)

        recycleMessage = findViewById(R.id.recycleChat)

        userIdSender = firebaseUtil.getIdentifyUser()

        //recebendo contato
        val bundle: Bundle? = intent.extras

        if (bundle != null){
            userChat = bundle.getSerializable("chatContact") as Contact
            textNameChat.text = userChat.nameContact

            val photo = userChat.photoContact

            if (photo != null && photo != ""){
                val url: Uri = Uri.parse(userChat.photoContact)
                Glide.with(this@ChatActivity)
                    .load(url)
                    .into(imgChat)

            }else{
                imgChat.setImageResource(R.drawable.padrao)
            }

            userIdReceiver = Base64Custom.codificarBase64( userChat.emailContact )

        }

        //config recycle
        adapter = ChatAdapter(listMessages, this@ChatActivity)

        val layoutManager = LinearLayoutManager(this@ChatActivity)
        recycleMessage.layoutManager = layoutManager
        recycleMessage.setHasFixedSize(true)
        recycleMessage.adapter = adapter

        database = firebaseUtil.getFirebaseReference()

        viewModelLogin = ViewModelProviders.of(this@ChatActivity).get(ChatViewModel::class.java)


        viewModelLogin.createStructMsgs(database, userIdSender, userIdReceiver)
        viewModelLogin.retriverMessage(listMessages)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
        viewModelLogin.messageRef.removeEventListener(viewModelLogin.childEventListenerMessage)
    }

    fun sendMessage(view: View){

        val textMsg = editMessage.text.toString()

        if (textMsg.isNotEmpty()){
            val message = Message()
            message.idUser = userIdSender
            message.messageSend = textMsg


            saveMessage(userIdSender, userIdReceiver, message)
        }else{
            Toast.makeText(this@ChatActivity,"Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show()
        }

    }

    fun saveMessage(sender: String, receiver: String, message: Message){

        val reference: DatabaseReference = FirebaseConfig().getFirebaseDatabase()
        viewModelLogin.messageRef = reference.child("mensagens")

        viewModelLogin.messageRef.child(sender)
            .child(receiver)
            .push()
            .setValue(message)

        editMessage.setText("")
    }


}
