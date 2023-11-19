package com.Apic.apic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.RvGroupListBinding

class GroupListAdapter : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener
    private var data = ArrayList<GroupListItem>()

    inner class ViewHolder(val binding: RvGroupListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: GroupListItem) {
            binding.groupName.text =  item.getGroupName() // 그룹 이름
            binding.groupNum.text = item.getGroupNum().toString()   // 그룹 멤버명수
            binding.groupLiked.isActivated = item.isGroupLiked()  // 그룹 즐겨찾기 (안됨)
//            Glide.with(binding)                                       // 그룹 사진
//                .load(item.group_photo)
//                .into(binding.groupPhoto)
        }
    }
    // 아이템 클릭리스너
    interface OnItemClickListener {
        fun onClick(view: View, position: Int) {
            // Toast.makeText(view.context, "테스트 - ${position}클릭", Toast.LENGTH_SHORT).show()
        }
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvGroupListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    fun replaceList(newList: ArrayList<GroupListItem>) {
        data = newList
        // 어댑터에 데이터가 변했다는 notify를 날린다
        notifyDataSetChanged()
    }




}
