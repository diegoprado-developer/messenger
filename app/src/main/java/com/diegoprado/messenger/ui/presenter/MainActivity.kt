package com.diegoprado.messenger.ui.presenter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.diegoprado.messenger.R
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.ui.fragment.ContactsFragment
import com.diegoprado.messenger.ui.viewmodel.MainActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import android.widget.LinearLayout
import android.widget.Toast
import com.diegoprado.messenger.domain.util.FirebaseUtil
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity(), IContractFirebase {

    var authFirebase: FirebaseAuth? = null

    private lateinit var viewModelLogin: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authFirebase = FirebaseConfig().getFirebaseAuth()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        val tabAdapter = FragmentPagerItemAdapter(
            supportFragmentManager,
            FragmentPagerItems.with(this@MainActivity)
                .add("Chat", ContactsFragment::class.java)
                .create()
        )

        val viewPager: ViewPager = findViewById(R.id.viewpager)
        viewPager.adapter = tabAdapter

        val viewPagerTab: SmartTabLayout = findViewById(R.id.viewpagertab)
        viewPagerTab.setViewPager(viewPager)

        viewModelLogin = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuLogaut -> {
                logautUser()
                finish()
            }

            R.id.menuConfig -> {
                openConfigActivity()
                finish()
            }

            R.id.menuAdd -> {
                newRegisterContact()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logautUser(){
        try{
            authFirebase?.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun newRegisterContact(){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.apply {
            builder.setTitle("Adicionar Contato")
            builder.setMessage("e-mail do usuário")
            builder.setCancelable(false)

            val input = EditText(this@MainActivity)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            input.layoutParams = lp
            builder.setView(input)

            builder.setPositiveButton("Cadastrar"){dialog, which ->

                val contact = input.text.toString()
                if (contact.isEmpty()){
                    Toast.makeText(this@MainActivity, "Preencha o e-mail", Toast.LENGTH_SHORT).show()
                }else{
                    viewModelLogin.saveNewContactUser(contact, this@MainActivity)
                }

            }
            builder.setNegativeButton("Cancelar"){dialog, which ->
            }
        }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun openConfigActivity(){
        val intent = Intent(this@MainActivity, ConfigActivity::class.java)
        startActivity(intent)
    }

    override fun OnSuccess() {
    }

    override fun OnError(excecao: String) {
        Toast.makeText(this@MainActivity, excecao, Toast.LENGTH_SHORT).show()
    }

    override fun getContext(): Activity {
        return this
    }
}
