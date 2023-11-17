package com.Apic.apic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.startActivity
import com.Apic.apic.databinding.ActivityMainBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var toolbar : ActionBarDrawerToggle // lateinit : '클래스의 값을 이용하는 변수의 경우'에서만!

    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentCalendar = CalendarFragment()
    private val fragmentFriend = FriendFragment()
    private val GroupListActivity = GroupListActivity()

    var authMenuItem : MenuItem?= null


    private val authActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            updateLogStatus()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar)) // 뒤로가기,프로필(인증) 메뉴

        //setSupportActionBar(binding.toolbar) //툴바가 액션바의 역할을 하도록 전달

        toolbar = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed) //ativity_main, string
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.syncState() //toolbar 실행

        //drawer-네비게이션바 연결
        binding.mainDrawer.setNavigationItemSelectedListener(this) //Logcat 출력

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigationview)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.menu_home -> transaction.replace(R.id.menu_frame_view, fragmentCalendar).commitAllowingStateLoss()
                R.id.menu_friend -> transaction.replace(R.id.menu_frame_view, fragmentFriend).commitAllowingStateLoss()
                R.id.menu_share -> {
                    val intent = Intent(this, GroupListActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

    }

    //메뉴 바 설정 시 아래의 두 함수 모두 필요!!
    //-> onCreateOptionMenu, onOtionItemSelected
    //dawer에서 아이템 선택 시 이벤트

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    //drawable 로그인 상태 변경
    private fun updateLogStatus() {
        val navigationView: NavigationView = findViewById(R.id.main_drawer)
        val logTextView = navigationView.getHeaderView(0).findViewById<TextView>(R.id.log)

        if (MyApplication.checkAuth()) {
            val email = MyApplication.email
            logTextView.text = "${MyApplication.email}님"
        } else {
            logTextView.text = "로그인 필요"
        }
    }


    //옵션 메뉴 만들기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        authMenuItem=menu!!.findItem(R.id.menu_auth)
        if (MyApplication.checkAuth()){ //인증이 된 경우
            authMenuItem!!.title = "${MyApplication.email}님"
        }
        else{
            authMenuItem!!.title= "인증"
        }
        return super.onCreateOptionsMenu(menu)
    }

    //자동호출 함수
    override fun onStart(){
        //intent에서 finish된 다음 mainActivity로 돌아올 때 실행
        //oncreate -> onStart
        super.onStart()
        if (authMenuItem != null){
            if (MyApplication.checkAuth()){ //인증이 된 경우
                authMenuItem!!.title = "${MyApplication.email}님"
            }
            else{
                authMenuItem!!.title= "인증"
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //만들어 놓은 toolbar 선택 시 작동
        if (toolbar.onOptionsItemSelected(item)) {
            return true
        }
        if(item.itemId === R.id.menu_auth){
            val intent = Intent(this, AuthActivity::class.java)
            if (authMenuItem!!.title!!.equals("인증")){//login
                intent.putExtra("data", "logout")
            }
            else{ //이메일 이미 표시
                intent.putExtra("data", "login")
            }
            startActivity(Intent(this, AuthActivity::class.java)) //startActivity를 통해 호출
            //finish일 때 돌아옴.

        }

        return super.onOptionsItemSelected(item)
    }




}