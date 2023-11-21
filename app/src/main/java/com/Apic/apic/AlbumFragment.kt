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
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        val rootView = binding.root

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

    fun updateImageList(data: Intent?) {
        Log.d("AlbumFragment", "updateImageList called")

        if (imageList == null) {
            imageList = ArrayList()
        }

        if (data?.clipData != null) {   // 다중 이미지 갯수
            // 선택한 이미지 갯수
            Log.d("AlbumFragment", "ClipData exists with itemCount: ${data.clipData!!.itemCount}")
            val count = data.clipData!!.itemCount

            for (index in 0 until count) {
                // 이미지 담기
                val imageUri = data.clipData!!.getItemAt(index).uri
                // 이미지 추가
                imageList?.add(imageUri)
                Log.d("AlbumFragment", "Added image to imageList: $imageUri")
            }
        } else { // 싱글 이미지
            Log.d("AlbumFragment", "ClipData is null")
            val imageUri = data?.data
            imageUri?.let {
                imageList?.add(it)
            }
        }

        // 이미지 리스트 업데이트
        updateAdapter()
    }

    private fun updateAdapter() {
        imageList?.let {
            Log.d("AlbumFragment", "imageList size: ${it.size}")

            // Check if albumAdapter is initialized before calling notifyDataSetChanged
            albumAdapter?.notifyDataSetChanged()
            Log.d("AlbumFragment", "notifyDataSetChanged")
        }
    }
}
