package com.Apic.apic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.RvGroupMemberBinding

class GroupMemberAdapter(val items:MutableList<GroupMemberData>): RecyclerView.Adapter<GroupMemberAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvGroupMemberBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(items : GroupMemberData) {
            binding.memberName.text = items.gm_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvGroupMemberBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupMemberAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}