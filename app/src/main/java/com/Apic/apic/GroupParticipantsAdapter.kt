package com.Apic.apic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.RvGroupParticipantsBinding

class GroupParticipantsAdapter(val items:MutableList<FriendData>) : RecyclerView.Adapter<GroupParticipantsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvGroupParticipantsBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(items : FriendData) {
            binding.memberName.text = items.f_name
            binding.memberId.text = items.f_email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvGroupParticipantsBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupParticipantsAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}