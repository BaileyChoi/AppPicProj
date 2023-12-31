package com.Apic.apic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.Apic.apic.databinding.ActivityMainBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding

    private lateinit var fragmentTransaction: FragmentTransaction   //
    private val fragmentAddFriend = AddFriendFragment()
    private var fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentCalendar = CalendarFragment()
    private val fragmentFriend = FriendFragment()
    private val fragmentGroupList = GroupListFragment()
    private val fragmentFourcut = FourcutFragment()

    private lateinit var auth: FirebaseAuth

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000 // 아무 숫자나 상관없지만 중복되지 않도록 정의
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_view,fragmentCalendar).commitAllowingStateLoss()

            //Login// -> menu_nav로 옮겨서 아래 Item에 있어요

            //로그인 한 회원만 사용 가능하도록

        /*
            val logoutBtn: Button = findViewById(R.id.logOut)
            logoutBtn.setOnClickListener {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
         */


            val bottomNavigationView =
                findViewById<BottomNavigationView>(R.id.bottom_navigationview)
            bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
                val transaction = fragmentManager.beginTransaction()

                when (menuItem.itemId) {
                    R.id.menu_home -> transaction.replace(R.id.menu_frame_view, fragmentCalendar)
                        .commitAllowingStateLoss()
                    R.id.menu_friend -> transaction.replace(R.id.menu_frame_view, fragmentFriend)
                        .commitAllowingStateLoss()
                    R.id.menu_share -> transaction.replace(R.id.menu_frame_view, fragmentGroupList)
                        .commitAllowingStateLoss()
                    R.id.menu_fourcut -> transaction.replace(R.id.menu_frame_view, fragmentFourcut)
                        .commitAllowingStateLoss()
                }
                true
            }


        //drawer-> navigation_hear에 있는 textView에 현재 로그인 정보 전달
        val navigationView: NavigationView = findViewById(R.id.main_drawer)
        navigationView.setNavigationItemSelectedListener(this)

        val headerView = navigationView.getHeaderView(0)
        //xml
        val auth_name: TextView = headerView.findViewById(R.id.auth_name)
        val auth_email: TextView = headerView.findViewById(R.id.auth_email)
        //firebaseAuth 데이터 가져오기
        val user = auth.currentUser

        user?.let { currentUser ->
            user?.let {
                // Name, email address, and profile photo Url
                //val name = it.displayName
                val email = it.email

                // 가져온 이름과 이메일을 TextView에 설정
                //auth_name.text = name
                auth_email.text = email
            }
        }
        //name을 가져오기 위해 그냥 Edit 가져올게요..
        val name = intent.getStringExtra("name")
        auth_name.text = name


        //사람 이미지 클릭 시 drawer
        val authBtn: ImageView = findViewById(R.id.authBtn)
        val drawlayout: DrawerLayout = findViewById(R.id.drawer)
        authBtn.setOnClickListener {
            drawlayout.openDrawer(GravityCompat.START)

            // group activity test code
//        val intent = Intent(this, GroupActivity::class.java)
//        startActivity(intent)
//        finish()
        }


    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.galleryButton -> {
                Log.d("item", "사진 불러오기")

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)

                true
            }
            R.id.logOut -> {
                Log.d("logout", "로그아웃 버튼 클릭")

                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

        //옵션메뉴 -> 메뉴바 보여지도록 가시화(명시)
        //옵션 멘 만들기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_navigation, menu)
        return super.onCreateOptionsMenu(menu)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                // 선택한 이미지 URI를 auth_img에 설정
                val auth_img: ImageView = findViewById(R.id.userImageView)
                auth_img.setImageURI(selectedImageUri)
            }
        }
    }

}