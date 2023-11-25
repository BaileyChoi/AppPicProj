package com.Apic.apic

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

    private val fragmentAddMeeting = AddMeetingFragment()
    private lateinit var menuIcon: ImageButton
    lateinit var binding: ActivityGroupBinding

    // 상태 변수 선언
    private var isFabOpen = false
    private var isLiked = false
    var imageList: ArrayList<Uri> = ArrayList()

    private val fragmentManager: FragmentManager = supportFragmentManager

    private lateinit var groupName: TextView
    private lateinit var groupMemberNum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewBinding을 사용하여 콘텐츠 뷰 설정
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // albumAdapter 초기화
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
        tabLayout.addTab(tabLayout.newTab().setText("album"))
        tabLayout.addTab(tabLayout.newTab().setText("member"))
        viewPager2.adapter = adapter
    }

    // 뷰페이저와 콜백을 설정
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

    // 플로팅 버튼 토글
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

    // 즐겨찾기 상태 토글
    private fun toggleLikedState() {
        isLiked = !isLiked
        // 즐겨찾기 상태에 따라 아이콘 변경
        groupLikedButton.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_unliked)
        // 즐겨찾기 상태에 따라 토스트 메세지 출력
        showToast(if (isLiked) "즐겨찾기 등록!" else "즐겨찾기 해제!")
    }

    // 팝업 메뉴 표시
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
                // 추가적인 메뉴 아이템 필요한 경우 여기에 추가
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


