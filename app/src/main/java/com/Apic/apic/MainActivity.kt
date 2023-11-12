package com.Apic.apic

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    private var isFabOpen = false

    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabCamera: FloatingActionButton
    private lateinit var fabCreate: FloatingActionButton

    private lateinit var groupLikedButton: ImageButton
    private var isLiked = false

    private lateinit var menuIcon: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_group)

        initializeViews()
        setupTabLayout()
        setupViewPager()
        setupListeners()
    }

    private fun initializeViews() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.viewPager2)
        fabMain = findViewById(R.id.fab_main)
        fabCamera = findViewById(R.id.fab_camera)
        fabCreate = findViewById(R.id.fab_create)
        groupLikedButton = findViewById(R.id.group_liked)
        menuIcon = findViewById(R.id.menu_icon)
    }

    private fun setupTabLayout() {
        adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText("Board"))
        tabLayout.addTab(tabLayout.newTab().setText("Friends"))
        viewPager2.adapter = adapter
    }

    private fun setupViewPager() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { viewPager2.currentItem = it.position }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let { viewPager2.currentItem = it.position }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let { viewPager2.currentItem = it.position }
            }
        })

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    private fun setupListeners() {
        fabMain.setOnClickListener { toggleFab() }
        fabCamera.setOnClickListener { showToast("카메라 버튼 클릭!") }
        fabCreate.setOnClickListener { showToast("버튼 클릭!") }
        groupLikedButton.setOnClickListener { toggleLikedState() }
        menuIcon.setOnClickListener { showPopupMenu(it) }
    }

    private fun toggleFab() {
        showToast("메인 플로팅 버튼 클릭 $isFabOpen")

        val translateYCamera = if (isFabOpen) 0f else -200f
        val translateYCreate = if (isFabOpen) 0f else -400f

        ObjectAnimator.ofFloat(fabCamera, "translationY", translateYCamera).start()
        ObjectAnimator.ofFloat(fabCreate, "translationY", translateYCreate).start()

        fabMain.setImageResource(R.drawable.ic_add)
        isFabOpen = !isFabOpen
    }

    private fun toggleLikedState() {
        isLiked = !isLiked

        // Change the icon based on the liked state
        groupLikedButton.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_unliked)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_group, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item1 -> {
                    showToast("메뉴 아이템 1 클릭!")
                    true
                }
                R.id.menu_item2 -> {
                    showToast("메뉴 아이템 2 클릭!")
                    true
                }
                // Add more menu items as needed
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
