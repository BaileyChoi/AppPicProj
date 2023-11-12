package com.Apic.apic

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    private var isFabOpen = false

    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabCamera: FloatingActionButton
    private lateinit var fabCreate: FloatingActionButton

    private lateinit var groupLikedButton: ImageButton
    private var isLiked = false;

    private lateinit var menuIcon : ImageButton


    override fun onCreate(savedInstanceState:
                          Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_group)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.viewPager2)

        // Initialize FloatingActionButton instances after setContentView
        fabMain = findViewById(R.id.fab_main)
        fabCamera = findViewById(R.id.fab_camera)
        fabCreate = findViewById(R.id.fab_create)


        adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)

        // tab
        tabLayout.addTab(tabLayout.newTab().setText("Board"))
        tabLayout.addTab(tabLayout.newTab().setText("Friends"))

        viewPager2.adapter = adapter

        // group liked
        groupLikedButton = findViewById(R.id.group_liked)

        // menu
        menuIcon = findViewById(R.id.menu_icon)


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


        // 플로팅 이벤트 구현
        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        fabMain.setOnClickListener{
            toggleFab()
        }

        // 플로팅 버튼 클릭 이벤트 - 카메라
        fabCamera.setOnClickListener{
            Toast.makeText(this, "카메라 버튼 클릭!", Toast.LENGTH_SHORT).show()
        }

        // 플로팅 버튼 클릭 이벤트 - 카메라
        fabCreate.setOnClickListener{
            Toast.makeText(this, " 버튼 클릭!", Toast.LENGTH_SHORT).show()
        }

        // 즐겨찾기 버튼 클릭 이벤트
        groupLikedButton.setOnClickListener{
            isLiked =! isLiked

            // Change the icon based on the liked state
            if (isLiked) {
                groupLikedButton.setImageResource(R.drawable.ic_liked)
            } else {
                groupLikedButton.setImageResource(R.drawable.ic_unliked)
            }

        }

        // 메뉴
        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun toggleFab(){

        Toast.makeText(this, "메인 플로팅 버튼 클릭 $isFabOpen", Toast.LENGTH_SHORT).show()


        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (isFabOpen) {
            ObjectAnimator.ofFloat(fabCamera, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(fabCreate, "translationY", 0f).apply { start() }
            fabMain.setImageResource(R.drawable.ic_add)
        } else {
            ObjectAnimator.ofFloat(fabCamera, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(fabCreate, "translationY", -400f).apply { start() }
            fabMain.setImageResource(R.drawable.ic_add)
        }

        isFabOpen =!isFabOpen

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_group, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item1 -> {
                    // Handle menu item 1 click
                    true
                }
                R.id.menu_item2 -> {
                    // Handle menu item 2 click
                    true
                }
                // Add more menu items as needed
                else -> false
            }
        }

        popupMenu.show()
    }


}