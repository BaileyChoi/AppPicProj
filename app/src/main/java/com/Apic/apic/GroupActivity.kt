// GroupActivity.kt

package com.Apic.apic

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

    // 뷰 변수 선언
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabCamera: FloatingActionButton
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var groupLikedButton: ImageButton
    private lateinit var menuIcon: ImageButton
    lateinit var binding: ActivityGroupBinding

    // 상태 변수 선언
    private var isFabOpen = false
    private var isLiked = false
    var imageList: ArrayList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewBinding을 사용하여 콘텐츠 뷰 설정
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // albumAdapter 초기화
        albumAdapter = AlbumAdapter(imageList, this)

        // 뷰 초기화, 탭 레이아웃, 뷰페이저, 리스너 설정
        initializeViews()
        setupTabLayout()
        setupViewPager()
        setupListeners()
    }

    // 뷰 참조를 초기화하는 함수
    private fun initializeViews() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.viewPager2)
        fabMain = findViewById(R.id.fab_main)
        fabCamera = findViewById(R.id.fab_camera)
        fabCreate = findViewById(R.id.fab_create)
        groupLikedButton = findViewById(R.id.group_liked)
        menuIcon = findViewById(R.id.menu_icon)
    }

    // 탭 레이아웃을 설정하는 함수
    private fun setupTabLayout() {
        adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"))
        tabLayout.addTab(tabLayout.newTab().setText("album"))
        viewPager2.adapter = adapter
    }

    // 뷰페이저와 콜백을 설정하는 함수
    private fun setupViewPager() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // 탭 선택 이벤트 처리
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

        // 뷰페이저 페이지 변경 콜백 설정
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    // FAB, 버튼 및 메뉴 아이콘의 클릭 리스너 설정
    private fun setupListeners() {
        fabMain.setOnClickListener { toggleFab() }
        fabCamera.setOnClickListener {
            showToast("카메라 버튼 클릭!")

            // 이미지 선택 액티비티 시작
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 다중 이미지 선택 활성화
            activityResult.launch(intent)
        }
        fabCreate.setOnClickListener { showToast("버튼 클릭!") }
        groupLikedButton.setOnClickListener { toggleLikedState() }
        menuIcon.setOnClickListener { showPopupMenu(it) }
    }

    // 이미지 선택 액티비티 결과 처리
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // 선택한 이미지를 가져와 현재 프래그먼트의 이미지 목록 업데이트
            val albumFragment = supportFragmentManager.findFragmentByTag("f${viewPager2.currentItem}") as? AlbumFragment
            albumFragment?.updateImageList(result.data)
        }
    }

    // FAB의 가시성 및 이동을 전환하는 함수 (애니메이션)
    private fun toggleFab() {
        showToast("메인 플로팅 버튼 클릭!")

        val translateYCamera = if (isFabOpen) 0f else -200f
        val translateYCreate = if (isFabOpen) 0f else -400f

        // FAB 애니메이션
        ObjectAnimator.ofFloat(fabCamera, "translationY", translateYCamera).start()
        ObjectAnimator.ofFloat(fabCreate, "translationY", translateYCreate).start()

        // 메인 FAB 아이콘 변경 및 열린 상태 업데이트
        fabMain.setImageResource(R.drawable.ic_add)
        isFabOpen = !isFabOpen
    }

    // 즐겨찾기 상태를 전환하고 즐겨찾기 버튼의 아이콘 업데이트
    private fun toggleLikedState() {
        isLiked = !isLiked
        groupLikedButton.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_unliked)
        showToast(if (isLiked) "즐겨찾기 등록!" else "즐겨찾기 해제!")
    }

    // 메뉴 아이콘이 클릭될 때 팝업 메뉴를 표시하는 함수
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_group, popupMenu.menu)

        // 메뉴 아이템 클릭 처리
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

    // 짧은 Toast 메시지 표시
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
