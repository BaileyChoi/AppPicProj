package com.Apic.apic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.RvGroupParticipantsBinding

class GroupParticipantsAdapter(val items:MutableList<FriendData>) : RecyclerView.Adapter<GroupParticipantsAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener

    inner class ViewHolder(val binding: RvGroupParticipantsBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(items : FriendData) {
            binding.memberName.text = items.f_name
            binding.memberId.text = items.f_email
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, position: Int) {
            // Toast.makeText(view.context, "테스트 - ${position}클릭", Toast.LENGTH_SHORT).show()
        }
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvGroupParticipantsBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupParticipantsAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}