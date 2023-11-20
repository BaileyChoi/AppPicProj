package com.Apic.apic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.Apic.apic.databinding.FragmentBoardBinding
import com.google.firebase.firestore.Query

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentBoardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBoardBinding.inflate(inflater, container, false)

        myCheckPermission(requireActivity() as AppCompatActivity) //카메라 접근을 허용하시겠습니까? 등의 확인 절차

        /*
        binding.mainFab.setOnClickListener { //board.xml
            if(MyApplication.checkAuth()) { //인증된 유저만 사용 가능
                startActivity(Intent(requireContext(), AddActivity::class.java))
            }else {
                Toast.makeText(requireContext(), "인증진행해주세요..", Toast.LENGTH_SHORT).show()
            }
        }*/
        return binding.root
    }

    //이전 작업 내역 받아오기
    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth()){
            Log.d("mobileApp", "onStart")
            MyApplication.db.collection("news")
                .orderBy("data", Query.Direction.DESCENDING) //data 필드로 정렬 -> 내림차순 정렬
                .get() //데이터 받아오기
                //저장된 데이터를 출력
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemBoardModel>() //저장 값을 초기화
                    //저장된 데이터 수 만큼 반목문을 통해 받아 itemList에 저장
                    for (document in result){
                        val item = document.toObject(ItemBoardModel::class.java)
                        item.docId = document.id //fireBase에 있는 아이디를 통해 docId 저장
                        itemList.add(item) //각각 하나씩 추가
                    }

                    //binding.boardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    //binding.boardRecyclerView.adapter = MyBoardAdapter(requireContext(),itemList)
                }
                .addOnFailureListener{ exception ->
                    Toast.makeText(requireContext(), "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //데이터가 잘 저장되어 있는지 확인
    fun myCheckPermission(activity: AppCompatActivity) {
        Log.d("mobileApp", "myCheckPermission")

        val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                Toast.makeText(activity, "권한 승인", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "권한 거부", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(//추가 여부
                activity, Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission( //허용 엽
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //R 버전 이상 시 사용 가능
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                activity.startActivity(intent)
            }
        }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BoardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}