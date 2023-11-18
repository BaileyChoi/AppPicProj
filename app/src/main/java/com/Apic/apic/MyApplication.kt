package com.Apic.apic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.Apic.apic.MyApplication.Companion.auth
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

//전역 변수를 담당하고 있는 클래스

//전역 응용 프로그램 상태를 유지하기 위한 기본 클래스
//첫 번째 액티비티(MainActivity)가 표시되기 전에 전역 상태를 초기화하는 데 사용
class MyApplication : MultiDexApplication() {

    companion object {
        lateinit var db: FirebaseFirestore
        //lateinit var auth : FirebaseAuth
        val auth: FirebaseAuth= Firebase.auth
        var email: String? = null

        fun checkAuth(): Boolean {
            val currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: false
        }


    }

    override fun onCreate() {
        super.onCreate()
        //auth = Firebase.auth //초기화

    }
}