package com.Apic.apic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var albumRecyclerView: RecyclerView

    private var albumAdapter: AlbumAdapter? = null
    private var imageList: ArrayList<Uri>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding을 사용하여 프래그먼트의 레이아웃 설정
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // RecyclerView 초기화 및 그리드 레이아웃 매니저 설정
        albumRecyclerView = binding.albumRecyclerView
        albumRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        // 이미지 리스트가 null이면 새로 생성, 이미 있다면 기존 리스트 사용
        imageList = imageList ?: ArrayList()

        // AlbumAdapter 초기화 및 RecyclerView에 연결
        albumAdapter = AlbumAdapter(imageList!!, requireContext())
        albumRecyclerView.adapter = albumAdapter

        Log.d("AlbumFragment", "onCreateView called")

        return rootView
    }

    // MainActivity에서 호출하여 이미지 리스트 업데이트
    fun updateImageList(data: Intent?) {
        Log.d("AlbumFragment", "updateImageList called")

        // 이미지 리스트가 null인 경우 새로 생성
        if (imageList == null) {
            imageList = ArrayList()
        }

        if (data?.clipData != null) {   // 다중 이미지 선택 시
            // 선택한 이미지의 갯수
            Log.d("AlbumFragment", "ClipData exists with itemCount: ${data.clipData!!.itemCount}")
            val count = data.clipData!!.itemCount

            // 각 이미지를 리스트에 추가
            for (index in 0 until count) {
                val imageUri = data.clipData!!.getItemAt(index).uri
                imageList?.add(imageUri)
                Log.d("AlbumFragment", "Added image to imageList: $imageUri")
            }
        } else { // 단일 이미지 선택 시
            Log.d("AlbumFragment", "ClipData is null")
            val imageUri = data?.data
            imageUri?.let {
                imageList?.add(it)
            }
        }

        // 이미지 리스트 업데이트
        updateAdapter()
    }

    // RecyclerView 어댑터 업데이트 및 갱신
    private fun updateAdapter() {
        imageList?.let {
            Log.d("AlbumFragment", "imageList size: ${it.size}")

            // AlbumAdapter가 초기화되었는지 확인 후 notifyDataSetChanged 호출
            albumAdapter?.notifyDataSetChanged()
            Log.d("AlbumFragment", "notifyDataSetChanged")
        }
    }
}
