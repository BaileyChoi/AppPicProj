package com.Apic.apic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.Apic.apic.databinding.ActivityMainBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction   //
    private val fragmentCalendar = CalendarFragment()
    private val fragmentFriend = FriendFragment()
    private val fragmentAddFriend = AddFriendFragment()
    private val fragmentGroupList = GroupListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // calendar
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigationview)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.menu_home -> transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()
                R.id.menu_friend -> {
                    transaction.replace(R.id.menu_frame_view, fragmentFriend).commitAllowingStateLoss()
                    setFragment(0); // 첫번째 프래그먼트 화면 설정
                }
                R.id.menu_share -> transaction.replace(R.id.menu_frame_view, fragmentGroupList).commitAllowingStateLoss()
            }
            true
        }

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