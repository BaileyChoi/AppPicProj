package com.Apic.apic

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlbumAdapter() : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    lateinit var imageList: ArrayList<Uri>
    lateinit var context: Context

    constructor(imageList: ArrayList<Uri>, context: Context): this(){
        this.imageList = imageList
        this.context = context
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumView: ImageView = view.findViewById(R.id.albumView)
    }

    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_picture, parent, false)
        return ViewHolder(view)
    }

    // 데이터 설정
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position])    // 사진 위치
            .into(holder.albumView) // 보여줄 위치
    }

    // 아이템 갯수
    override fun getItemCount(): Int {
        return imageList.size
    }


}
