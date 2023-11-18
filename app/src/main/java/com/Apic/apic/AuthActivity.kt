package com.Apic.apic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.Apic.apic.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisibility("logout")
        //changeVisibility(intent.getStringExtra("data").toString())


        binding.goSignInBtn.setOnClickListener{
            // 회원가입
            changeVisibility("signin") //회원가입하게 만들어줘
        }

        //회원가입
        binding.signBtn.setOnClickListener { //sign 버튼을 눌렀을 때....
            //이메일,비밀번호 회원가입........................
            //사용자가 입력한 text 값 가져오기
            val email:String = binding.authEmailEditView.text.toString() //회원가입 할 이메일
            val password:String = binding.authPasswordEditView.text.toString() //회원가입 할 password

            MyApplication.auth.createUserWithEmailAndPassword(email, password) //회원가입 하기
                //전달된 게 올바로 실행되었는가?
                .addOnCompleteListener(this){ task-> //task 형태로 전달
                    if(task.isSuccessful) { //성공적으로 회원가입
                        MyApplication.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                                sendTask ->
                            if(sendTask.isSuccessful){
                                Toast.makeText(baseContext, "회원가입 성공..이메일 확인", Toast.LENGTH_LONG).show() //이메일 전송 완료!
                                changeVisibility("logout") //회원가입이 되었어도 아직 이메일 확인을 하지 않음
                            }
                            else{
                                Toast.makeText(baseContext, "메일 전송 실패...", Toast.LENGTH_LONG).show() //이메일 전송 실패!
                                changeVisibility("logout") //메일 전송 실패로 회원가입이 되지 않음
                            }
                        } //이메일 주소 확인작업
                    }
                    else { //회원가입 실패
                        Toast.makeText(baseContext, "회원가입 실패..", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                    binding.authEmailEditView.text.clear() //이전 작성했던 회원가입 내용 지우기
                    binding.authPasswordEditView.text.clear()
                }
        }

        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            //사용자가 입력한 text 값 가져오기
            val email:String = binding.authEmailEditView.text.toString() //회원가입 할 이메일
            val password:String = binding.authPasswordEditView.text.toString() //회원가입 할 password
            MyApplication.auth.signInWithEmailAndPassword(email, password) //사용자가 입력한 이메일과 password를 통해 로그인
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){ //로그인 성공
                        //firebase에 등록된 유저인지 확인 필요
                        if (MyApplication.checkAuth()){
                            MyApplication.email = email //사용자 이전 로그인을 간직
                            changeVisibility("login")
                        }
                        else{
                            Toast.makeText(baseContext, "이메일 인증 실패", Toast.LENGTH_LONG).show()
                        }
                    }
                    //else{
                    if(!task.isSuccessful) {
                        Toast.makeText(baseContext, "로그인 실패..", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                    //이전 기록 지우기
                    binding.authEmailEditView.text.clear() //이전 작성했던 회원가입 내용 지우기
                    binding.authPasswordEditView.text.clear() //password 지우기
                }
        }

        binding.logoutBtn.setOnClickListener {
            //로그아웃...........
            //세팅 안 할 시 마지막 로그인상태로 남아있음
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility("logout")

        }

        //받은 id가 진짜 구글 아이디인지 확인
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data) //사용자 입력받은 내용 전달
            //발생오류: Api exception 오류
            try { //인증된 객체를 통한 이메일 처리
                val account = task.getResult(ApiException::class.java) //자바에서 예외 처리
                val credential = GoogleAuthProvider.getCredential(account.idToken, null) // account를 받아 credential을 통해 껐다 켜도 로그인 유지
                MyApplication.auth.signInWithCredential(credential) //이메일, 구글 정보 포함 인증

                    .addOnCompleteListener(this){task -> //인증이 완료된 이후 (인증을 task로 전달받음)
                        //성공적 전달
                        if(task.isSuccessful){
                            MyApplication.email = account.email //이메일 정보를 MyApplication에 저장
                            changeVisibility("login")
                            Log.d("mobileApp", "GoogleSignIn - Successful")
                        }
                        //전달 실패
                        else{
                            changeVisibility("logout")
                            Log.d("mobileApp", "GoogleSignIn - NOT Successful")
                        }
                    }
            }catch (e: ApiException) { //오류 발생
                changeVisibility("logout")
                Log.d("mobileApp", "GoogleSignIn - ${e.message}")
            }
        }


        binding.googleLoginBtn.setOnClickListener {
            //구글 로그인....................
            val gso : GoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //디폴트 기본 창 사용
                .requestIdToken(getString(R.string.default_web_client_id)) //id 전달 토큰
                .requestEmail() //이메일을 요청
                .build() //로그인창 생성
            //구글 계정을 가지고 와 사용
            val signInIntent : Intent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }
    }

    fun changeVisibility(mode: String){
        if(mode.equals("signin")){ //회원가입
            binding.run {
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE

                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE //회원가입버튼 가시화

                loginBtn.visibility = View.GONE
            }
        }else if(mode.equals("login")){ //로그인된 상태 -> email이 들어옴
            binding.run {
                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                logoutBtn.visibility= View.VISIBLE
                goSignInBtn.visibility= View.GONE
                googleLoginBtn.visibility= View.GONE
                authEmailEditView.visibility= View.GONE
                authPasswordEditView.visibility= View.GONE
                signBtn.visibility= View.GONE
                loginBtn.visibility= View.GONE
            }

        }else if(mode.equals("logout")){
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        }
    }
}
