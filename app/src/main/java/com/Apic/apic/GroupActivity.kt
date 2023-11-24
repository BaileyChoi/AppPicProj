package com.Apic.apic

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


class GroupActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    private var isFabOpen = false

    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabCamera: FloatingActionButton
    private lateinit var fabCreate: FloatingActionButton

    private lateinit var groupLikedButton: ImageButton
    private var isLiked = false
    private val fragmentAddMeeting = AddMeetingFragment()
    private lateinit var menuIcon: ImageButton

    private val fragmentManager: FragmentManager = supportFragmentManager

    private lateinit var groupName: TextView
    private lateinit var groupMemberNum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticity_group)

        // 뷰 초기화
        initializeViews()
        // 탭 레이아웃 설정
        setupTabLayout()
        // 뷰페이저 설정
        setupViewPager()
        // 리스너 설정
        setupListeners()
    }

    // 뷰 초기화
    private fun initializeViews() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.viewPager2)
        fabMain = findViewById(R.id.fab_main)
        fabCamera = findViewById(R.id.fab_camera)
        fabCreate = findViewById(R.id.fab_create)
        groupLikedButton = findViewById(R.id.group_liked)
        menuIcon = findViewById(R.id.menu_icon)
        groupName = findViewById(R.id.group_name)
        groupMemberNum = findViewById(R.id.group_member_num)

        // 그룹 정보 반영
        val gName = intent.getStringExtra("g_name")
        val gParticipants = intent.getStringExtra("g_participants")
        groupName.text = gName
        groupMemberNum.text = gParticipants

        // MemberFragment에 그룹 정보 전달
        var memberFragment = MemberFragment()
        var bundle = Bundle()
        bundle.putString("g_name", gName)
        memberFragment.arguments = bundle
    }

    // 탭 레이아웃 설정
    private fun setupTabLayout() {
        adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText("date"))
        tabLayout.addTab(tabLayout.newTab().setText("member"))
        viewPager2.adapter = adapter
    }

    // 뷰페이저 설정
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

    // 리스너 설정
    private fun setupListeners() {
        fabMain.setOnClickListener {
            // toggleFab()

        }
        fabCamera.setOnClickListener { showToast("카메라 버튼 클릭!") }
        fabCreate.setOnClickListener { showToast("버튼 클릭!") }
        groupLikedButton.setOnClickListener { toggleLikedState() }
        menuIcon.setOnClickListener { showPopupMenu(it) }
    }

    // 플로팅 버튼 토글
    private fun toggleFab() {
        showToast("메인 플로팅 버튼 클릭!")

        val translateYCamera = if (isFabOpen) 0f else -200f
        val translateYCreate = if (isFabOpen) 0f else -400f

        ObjectAnimator.ofFloat(fabCamera, "translationY", translateYCamera).start()
        ObjectAnimator.ofFloat(fabCreate, "translationY", translateYCreate).start()

        fabMain.setImageResource(R.drawable.ic_add)
        isFabOpen = !isFabOpen
    }

    // 즐겨찾기 상태 토글
    private fun toggleLikedState() {

        isLiked = !isLiked

        // 즐겨찾기 상태에 따라 아이콘 변경
        groupLikedButton.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_unliked)

        // 즐겨찾기 상태에 따라 토스트 메시지 출력
        showToast(if (isLiked) "즐겨찾기 등록!" else "즐겨찾기 해제!")
    }

    // 팝업 메뉴 표시
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
                }// 추가적인 메뉴 아이템이 필요한 경우 여기에 추가
                else -> false
            }
        }

        popupMenu.show()
    }

    // 토스트 메시지 표시
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


