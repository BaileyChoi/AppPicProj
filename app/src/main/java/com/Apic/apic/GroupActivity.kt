// GroupActivity.kt

package com.Apic.apic

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.Apic.apic.databinding.ActivityGroupBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class GroupActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    private lateinit var albumAdapter: AlbumAdapter
    private var isFabOpen = false

    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabCamera: FloatingActionButton
    private lateinit var fabCreate: FloatingActionButton

    private lateinit var groupLikedButton: ImageButton
    private var isLiked = false

    private lateinit var menuIcon: ImageButton

    var imageList: ArrayList<Uri> = ArrayList()
    lateinit var binding: ActivityGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize albumAdapter here
        albumAdapter = AlbumAdapter(imageList, this)

        // 뷰 초기화
        initializeViews()
        // 탭 레이아웃 설정
        setupTabLayout()
        // 뷰페이저 설정
        setupViewPager()
        // 리스너 설정
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
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"))
        tabLayout.addTab(tabLayout.newTab().setText("album"))
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

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    private fun setupListeners() {
        fabMain.setOnClickListener { toggleFab() }
        fabCamera.setOnClickListener {
            showToast("카메라 버튼 클릭!")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            // 다중 선택 기능
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activityResult.launch(intent)
        }
        fabCreate.setOnClickListener { showToast("버튼 클릭!") }
        groupLikedButton.setOnClickListener { toggleLikedState() }
        menuIcon.setOnClickListener { showPopupMenu(it) }
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            Log.d("GroupActivity", "ActivityResultLauncher called")

            // 직접 AlbumFragment를 가져와서 이미지 업데이트
            val albumFragment = supportFragmentManager.findFragmentByTag("f${viewPager2.currentItem}") as? AlbumFragment
            albumFragment?.updateImageList(result.data)
        }
    }





    private fun toggleFab() {
        showToast("메인 플로팅 버튼 클릭!")

        val translateYCamera = if (isFabOpen) 0f else -200f
        val translateYCreate = if (isFabOpen) 0f else -400f

        ObjectAnimator.ofFloat(fabCamera, "translationY", translateYCamera).start()
        ObjectAnimator.ofFloat(fabCreate, "translationY", translateYCreate).start()

        fabMain.setImageResource(R.drawable.ic_add)
        isFabOpen = !isFabOpen
    }

    private fun toggleLikedState() {
        isLiked = !isLiked
        groupLikedButton.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_unliked)
        showToast(if (isLiked) "즐겨찾기 등록!" else "즐겨찾기 해제!")
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
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
