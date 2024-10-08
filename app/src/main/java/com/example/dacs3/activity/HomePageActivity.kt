package com.example.dacs3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.dacs3.R
import com.example.dacs3.fragment.AccountFragment
import com.example.dacs3.fragment.CartFragment
import com.example.dacs3.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePageActivity : AppCompatActivity() {
    val fragmentHome: Fragment = HomeFragment()
    val fragmentCart:Fragment = CartFragment()
    val fragmentAccount: Fragment = AccountFragment()
    val fm : FragmentManager =supportFragmentManager
    var active :Fragment =fragmentHome


    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setUpBottomNav()
    }
    fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentCart).hide(fragmentCart).commit()
        fm.beginTransaction().add(R.id.container, fragmentAccount).hide(fragmentAccount).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    callFargment(0, fragmentHome)
                }

                R.id.navigation_cart -> {
                    callFargment(1, fragmentCart)
                }

                R.id.navigation_account -> {
                        callFargment(2, fragmentAccount)


                }
            }

            false
        }
    }
    fun callFargment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}