package com.diegoprado.messenger.ui.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.diegoprado.messenger.R
import com.diegoprado.messenger.domain.model.Contact
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.diegoprado.messenger.ui.adapter.ContactsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.diegoprado.messenger.data.helper.RecyclerItemClickListener
import com.diegoprado.messenger.domain.model.User
import com.diegoprado.messenger.ui.presenter.ChatActivity
import com.google.firebase.auth.FirebaseUser


class ContactsFragment : Fragment() {

    private lateinit var recycleView: RecyclerView
    private lateinit var labelChat: TextView
    private lateinit var adapter: ContactsAdapter
    private var listContact: ArrayList<Contact> = ArrayList()
    private lateinit var valueEventListener: ValueEventListener
    private var userDataRef = FirebaseUtil().getUserContacts()
    private var userActual: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_contacts, container, false)

        recycleView = view.findViewById(R.id.rvContacts)
        labelChat = view.findViewById(R.id.labelChat)

        userActual = FirebaseUtil().getLoggedUser()

        adapter = getActivity()?.let { ContactsAdapter(listContact, it) }!!

//        val itemDecorator = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
//        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.line_divisor)!!)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
//        recycleView.addItemDecoration(itemDecorator)
        recycleView.addOnItemTouchListener(

            RecyclerItemClickListener(
                activity.let { it!!.applicationContext },
                recycleView,
                object: RecyclerItemClickListener.OnItemClickListener{
                    override fun onItemClick(view: View, position: Int) {
                        //passando o contato clicado para a tela de chat
                        val userSelected = listContact[position]
                        val intent: Intent = Intent(activity, ChatActivity::class.java)
                        intent.putExtra("chatContact", userSelected)
                        startActivity(intent)
                    }

                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                    }

                    override fun onLongItemClick(view: View?, position: Int) {

                    }

                }
            )
        )

        recycleView.adapter = adapter

        if (recycleView.size >= 1){
            labelChat.visibility = View.GONE
        }

        return view
    }

    override fun onStart() {
        listContactsByUser()
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

        userDataRef.removeEventListener(valueEventListener)
    }

    fun listContactsByUser(){

        val preference = activity?.let { com.diegoprado.messenger.data.helper.Preferences(it) }

        val identifyUser = preference?.getIdentify()

        identifyUser.let {
            userDataRef = userDataRef.child(it.toString())
        }

        valueEventListener = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                listContact.clear()

                for (data: DataSnapshot in snapshot.children){

                    val userContact: Contact = data.getValue(Contact::class.java) as Contact

                    val userOn = userActual?.email

                    if (!userOn.equals(userContact.emailContact)){
                        listContact.add(userContact)
                    }

                }
                if (listContact.size >= 1) {
                    labelChat.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }
            }
        }

        userDataRef.addValueEventListener(valueEventListener)
    }
}
