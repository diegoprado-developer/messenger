package com.diegoprado.messenger.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diegoprado.messenger.R
import com.diegoprado.messenger.domain.model.Contact
import com.diegoprado.messenger.domain.model.User
import de.hdodenhof.circleimageview.CircleImageView

class ContactsAdapter(val list: List<Contact>, val context: Context): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.adapter_contacts, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Contact = list[position]
        holder.apply {
            name.text = contact.nameContact
            email.text = contact.emailContact

            if (contact.photoContact != null){
                val uri: Uri = Uri.parse(contact.photoContact)
                Glide.with(context).load(uri).into(circleImg)
            }else{
                circleImg.setImageResource(R.drawable.padrao)
            }

        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val circleImg: CircleImageView = itemView.findViewById(R.id.imgContact)
        val name: TextView = itemView.findViewById(R.id.tvNameContact)
        val email: TextView = itemView.findViewById(R.id.tvEmailContact)
    }
}