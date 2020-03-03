package com.diegoprado.messenger.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.diegoprado.messenger.R
import com.diegoprado.messenger.domain.model.Message
import com.diegoprado.messenger.domain.util.FirebaseUtil

class ChatAdapter(val list: List<Message>, val context: Context): RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    private val TYPE_SENDER = 0
    private val TYPE_RECEIVER = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        lateinit var item: View
        if (viewType == TYPE_SENDER){

            item = LayoutInflater.from(parent.context).inflate(R.layout.sender_message_adapter, parent, false)

        }else if(viewType == TYPE_RECEIVER){

            item = LayoutInflater.from(parent.context).inflate(R.layout.receiver_message_adapter, parent, false)

        }

        return MyViewHolder(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {

        val message: Message = list[position]

        val idUser = FirebaseUtil().getIdentifyUser()

        if (idUser == message.idUser){
            return TYPE_SENDER
        }

        return TYPE_RECEIVER

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val message: Message = list[position]
        val msg = message.messageSend
        val image = message.photoMessage

        if (image != ""){
            val url: Uri = Uri.parse(image)
            holder.image?.let { Glide.with(context).load(url).into(it) }

            holder.message?.visibility = View.GONE
        }else{
            holder.message?.text = msg

            holder.image?.visibility = View.GONE
        }

    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var message: TextView? = null
        var image: ImageView? = null

        init {
            message = itemView.findViewById(R.id.textMensagemRemetente)
            image = itemView.findViewById(R.id.imgMensagemRemetente)
        }

    }
}