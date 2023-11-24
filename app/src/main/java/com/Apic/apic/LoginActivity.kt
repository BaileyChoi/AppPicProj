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

class LoginActivity : AppCompatActivity() {
    lateinit var nameEt: EditText
    lateinit var emailEt: EditText
    lateinit var passwordEt: EditText
    lateinit var loginBtn: Button

    private lateinit var auth: FirebaseAuth

    private var sampleNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        nameEt = findViewById(R.id.user_name)
        emailEt = findViewById(R.id.user_id)
        passwordEt = findViewById(R.id.user_pw)
        loginBtn = findViewById(R.id.button)

        loginBtn.setOnClickListener {
            var name = nameEt.text.toString()
            var email = emailEt.text.toString()
            var password = passwordEt.text.toString()

            auth.createUserWithEmailAndPassword(email,password) // 회원 가입
                .addOnCompleteListener(this) {  result ->
                    if(result.isSuccessful){
//                        auth.currentUser?.sendEmailVerification()   // 인증 메일 발송
//                            ?.addOnCompleteListener { sendTask ->
//                                if (sendTask.isSuccessful) {
//                                    //
//                                }
//                                else {
//
//                                }
//                            }
                        Toast.makeText(this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                        if(auth.currentUser!=null){
                            sampleNumber++
                            val data = FirebaseData(sampleNumber, name, email, password)
                            setDocument(data)
                            val memberData = MemberData(email, name, password)
                            setMember(memberData)
                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else if(result.exception?.message.isNullOrEmpty()){
                        Toast.makeText(this,"오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        login(email,password)
                        Toast.makeText(this,"로그인이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun login(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password) // 로그인
            .addOnCompleteListener {
                    result->
                if(result.isSuccessful){
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
    }

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