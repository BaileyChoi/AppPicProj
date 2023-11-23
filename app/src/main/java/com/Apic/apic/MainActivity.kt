package com.Apic.apic


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.Apic.apic.databinding.ActivityMainBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.Apic.apic.databinding.TopbarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import kotlin.math.log

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding

    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentCalendar = CalendarFragment()
    private val fragmentFriend = FriendFragment()
    private val fragmentGroupList = GroupListFragment()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
//        email = findViewById(R.id.userEmail)
//        email.text = auth.currentUser?.email    // 회원 이메일 표시

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()


            //Login//

            //로그인 한 회원만 사용 가능하도록
            val logoutBtn: Button = findViewById(R.id.logOut)
            logoutBtn.setOnClickListener {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

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

                }
                true
            }

        //drawer-> navigation_hear에 있는 textView에 현재 로그인 정보 전달
        val navigationView: NavigationView = findViewById(R.id.main_drawer)
        val headerView = navigationView.getHeaderView(0)
        //xml
        val auth_name: TextView = headerView.findViewById(R.id.auth_name)
        val auth_email: TextView = headerView.findViewById(R.id.auth_email)
        //firebaseAuth 데이터 가져오기
        val user = auth.currentUser
        val db = FirebaseFirestore.getInstance()

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

            return true
        }

        //옵션메뉴 -> 메뉴바 보여지도록 가시화(명시)
        //옵션 멘 만들기
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {

            return super.onCreateOptionsMenu(menu)
        }
}



