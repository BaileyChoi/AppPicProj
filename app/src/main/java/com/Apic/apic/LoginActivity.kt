package com.Apic.apic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.Apic.apic.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    lateinit var nameEt: EditText //이름
    lateinit var emailEt: EditText //이메일
    lateinit var passwordEt: EditText //pw
    lateinit var loginBtn: Button

    private lateinit var auth: FirebaseAuth

    //firebase sampleNumber
    private var sampleNumber =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //topbar에 있는 회원페이지 안 보이게 하기 (사실상 로그인 페이지에 더이상 topbar가 필요 없어서 topbar자체를 삭제)
        /*
        val auth_Btn:  ImageView = findViewById(R.id.authBtn)
        val logout_Btn : TextView = findViewById(R.id.logOut)
        auth_Btn.visibility = View.INVISIBLE
        logout_Btn.visibility = View.INVISIBLE
         */

        auth = FirebaseAuth.getInstance()

        nameEt = findViewById(R.id.user_name)
        emailEt = findViewById(R.id.user_id)
        // 로그인
        passwordEt = findViewById(R.id.user_pw)
        loginBtn = findViewById(R.id.button)

        loginBtn.setOnClickListener {
            var email = emailEt.text.toString()
            var password = passwordEt.text.toString()
            var name = nameEt.text.toString()


            auth.createUserWithEmailAndPassword(email, password) // 회원 가입
                .addOnCompleteListener(this) { result ->
                    if (result.isSuccessful) {
                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        if (auth.currentUser != null) {
                            sampleNumber++

                            //data를 firebase에 저장
                            val data = FirebaseData(sampleNumber, name, email, password)
                            setDocument(data) // 데이터 Firestore에 저장
                            // val memberData = MemberData(email, name, password)
                            val memberData = MemberData(email, name)
                            setMember(memberData)
                            var intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("name", name) //name도 함께 전달
                            startActivity(intent)

                        }
                    } else if (result.exception?.message.isNullOrEmpty()) {
                        Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        login(email, password)
                        Toast.makeText(this, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun login(email:String,password:String){
        nameEt = findViewById(R.id.user_name)
        var name = nameEt.text.toString()

        auth.signInWithEmailAndPassword(email,password) // 로그인
            .addOnCompleteListener {
                    result->
                if(result.isSuccessful){
                    var intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("name", name) //name도 함께 전달
                    startActivity(intent)
                }
            }
    }


    // Firestore의 memberDB에 데이터 저장 함수
    private fun setMember(data : MemberData){
        Log.d("db", "firebaseStore")
        val db = FirebaseFirestore.getInstance()
        db.collection("memberDB")
            .document(data.email)
            .set(data)
            .addOnSuccessListener {
                Log.d("db", "success")
            }.addOnFailureListener{
                Log.d("db", "fail")
            }
    }

    // Firestore에 데이터를 저장하는 함수
    private fun setDocument(data: FirebaseData) {
        Log.d("DB", "firebaseStore")
        val db = FirebaseFirestore.getInstance()
        db.collection("authDB")
            .add(data) // Firestore에 데이터 추가
            .addOnSuccessListener {
                Log.d("DB", "success!!")
            }
            .addOnFailureListener { e ->
                Log.w("DB", "Error adding document", e)
            }
    }
}