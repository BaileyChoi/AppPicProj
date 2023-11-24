package com.Apic.apic

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FriendRecyclerviewBinding

class DialogFriendAdapter(val itemList: ArrayList<MemberData>) : RecyclerView.Adapter<DialogFriendAdapter.ViewHolder>() {

    private var mFriendList: ArrayList<FriendItem> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding = FriendRecyclerviewBinding.inflate(layoutInflater, parent, false)
//        return ViewHolder(binding)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
    override fun onBindViewHolder(holder: DialogFriendAdapter.ViewHolder, position: Int) {
        Log.d("db", "Adapter1")
        holder.name.text = itemList[position].name  // adapter에 있는 이름에 MemberFriendData의 이름 데이터를 넣는다.
        holder.emailId.text = itemList[position].email
    }

    fun setFriendList(list: ArrayList<FriendItem>) {
        mFriendList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val emailId: TextView = itemView.findViewById(R.id.emailId)
    }
}