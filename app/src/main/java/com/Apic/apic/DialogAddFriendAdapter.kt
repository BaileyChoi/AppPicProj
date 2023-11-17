package com.Apic.apic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class DialogAddFriendAdapter : RecyclerView.Adapter<DialogAddFriendAdapter.ViewHolder>() {

    private var mFriendList: ArrayList<FriendItem> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_add_recyclerview, parent, false)

        return ViewHolder(view)
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profile: ImageView = itemView.findViewById(R.id.profile)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val emailId: TextView = itemView.findViewById(R.id.emailId)

        fun onBind(item: FriendItem) {
            profile.setImageResource(item.getResourceId())
            emailId.text = item.getID()
            name.text = item.getName()
        }
    }

}