package com.Apic.apic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d("splash","보여지기 전")

        val backExecutor : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor() //실제 실행시킬 내용
        val mainExecutor: Executor = ContextCompat.getMainExecutor(this) //메인 액티비티 부르기
        backExecutor.schedule({
            mainExecutor.execute{
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } //mainActivity 실행 후 종료

        }, 3, TimeUnit.SECONDS) //{실행내용}, 딜레이(얼마간 실행), 숫자에 대한 단위 (2초)
        Log.d("splash", "나와라ㅑ")
    }
}