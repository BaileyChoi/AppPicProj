package com.Apic.apic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FriendRecyclerviewBinding


class DialogFriendAdapter
    : RecyclerView.Adapter<DialogFriendAdapter.ViewHolder>() {

    private var mFriendList: ArrayList<FriendItem> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FriendRecyclerviewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mFriendList[position])
    }

    fun setFriendList(list: ArrayList<FriendItem>) {
        mFriendList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mFriendList.size
    }

    inner class ViewHolder(private val binding: FriendRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: FriendItem) {
            binding.profile.setImageResource(item.getResourceId())
            binding.emailId.text = item.getID()
            binding.name.text = item.getName()
        }
    }
}
