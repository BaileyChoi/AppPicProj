package com.Apic.apic


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import com.Apic.apic.databinding.ActivityMainBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentCalendar = CalendarFragment()
    private val fragmentFriend = FriendFragment()
    private val fragmentGroupList = GroupListFragment()

    lateinit var email: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
//        email = findViewById(R.id.userEmail)
//        email.text = auth.currentUser?.email    // 회원 이메일 표시

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()

        val logoutBtn : Button = findViewById(R.id.logOut)
        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigationview)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.menu_home -> transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()
                R.id.menu_friend -> transaction.replace(R.id.menu_frame_view, fragmentFriend).commitAllowingStateLoss()
                R.id.menu_share -> transaction.replace(R.id.menu_frame_view, fragmentGroupList).commitAllowingStateLoss()

            }
            true
        }

        // group activity test code
//        val intent = Intent(this, GroupActivity::class.java)
//        startActivity(intent)
//        finish()
    }

}