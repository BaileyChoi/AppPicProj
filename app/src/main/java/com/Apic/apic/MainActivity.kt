package com.Apic.apic


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.Apic.apic.databinding.ActivityMainBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var fragmentTransaction: FragmentTransaction   //
    private val fragmentAddFriend = AddFriendFragment()
    private var fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentCalendar = CalendarFragment()
    private val fragmentFriend = FriendFragment()
    private val fragmentGroupList = GroupListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar)) // 뒤로가기 메뉴

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()

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

    fun setFragment(n: Int) {
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        when (n) {
            1 -> fragmentTransaction.replace(R.id.menu_frame_view, fragmentAddFriend).commit()  // FriendFragment, addButton눌렀을 때

        }
        when (n) {
            0 -> fragmentTransaction.replace(R.id.menu_frame_view, fragmentFriend).commit()  // FriendFragment, addButton눌렀을 때
        }

    }

}