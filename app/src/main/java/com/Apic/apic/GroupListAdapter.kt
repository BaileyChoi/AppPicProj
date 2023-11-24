package com.Apic.apic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.RvGroupListBinding

class GroupListAdapter(val items:MutableList<GroupData>) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener

    inner class ViewHolder(val binding: RvGroupListBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(items: GroupData) {
            binding.groupName.text =  items.g_name // 그룹 이름
            binding.groupNum.text = items.g_participants  // 그룹 멤버명수
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
        return items.size
    }

    override fun onBindViewHolder(holder: GroupListAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

}
