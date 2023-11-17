package com.Apic.apic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.RvGroupMemberBinding

class GroupMemberAdapter: RecyclerView.Adapter<GroupMemberAdapter.ViewHolder>() {
    private var data = ArrayList<String>()

    inner class ViewHolder(val binding: RvGroupMemberBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : String) {
            binding.memberName.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvGroupMemberBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun replaceList(newList:ArrayList<String>) {
        data = newList
        // 어댑터에 데이터가 변했다는 notify를 날린다
        notifyDataSetChanged()
    }
}