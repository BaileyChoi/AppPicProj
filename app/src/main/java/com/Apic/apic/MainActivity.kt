package com.Apic.apic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    override fun onCreate(savedInstanceState:
                          Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_group)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.viewPager2)

        adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Board"))
        tabLayout.addTab(tabLayout.newTab().setText("Friends"))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!=null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab!=null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab!=null) {
                    viewPager2.currentItem = tab.position
                }
            }

        })

        viewPager2.registerOnPageChangeCallback(object: OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        } )
    }
}