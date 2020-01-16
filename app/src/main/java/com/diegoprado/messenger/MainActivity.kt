package com.diegoprado.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.diegoprado.messenger.data.firebase.FirebaseConfig
import com.diegoprado.messenger.ui.activity.ConfigActivity
import com.diegoprado.messenger.ui.fragment.ContactsFragment
import com.google.firebase.auth.FirebaseAuth
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class MainActivity : AppCompatActivity() {

    var authFirebase: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authFirebase = FirebaseConfig().getFirebaseAuth()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        val tabAdapter: FragmentPagerItemAdapter = FragmentPagerItemAdapter(
            supportFragmentManager,
            FragmentPagerItems.with(this@MainActivity)
                .add("Chat", ContactsFragment::class.java)
                .create()
        )

        val viewPager: ViewPager = findViewById(R.id.viewpager)
        viewPager.adapter = tabAdapter

        val viewPagerTab: SmartTabLayout = findViewById(R.id.viewpagertab)
        viewPagerTab.setViewPager(viewPager)
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logautUser(){
        try{
            authFirebase?.signOut()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun openConfigActivity(){
        val intent = Intent(this@MainActivity, ConfigActivity::class.java)
        startActivity(intent)
    }
}
