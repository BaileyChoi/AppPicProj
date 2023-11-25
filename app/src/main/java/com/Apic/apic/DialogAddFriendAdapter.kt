package com.Apic.apic

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView

class DialogAddFriendAdapter(var itemList: ArrayList<MemberData>) : RecyclerView.Adapter<DialogAddFriendAdapter.ViewHolder>() {

    // recyclerview의 chekButton을 위한 함수
    interface OnAddFriendClickListener {
        fun onAddFriendClick(position: Int)
    }
    private var listener: OnAddFriendClickListener? = null
    fun setOnAddFriendClickListener(listener: OnAddFriendClickListener) {
        this.listener = listener
    }
    //

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogAddFriendAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_add_recyclerview, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: DialogAddFriendAdapter.ViewHolder, position: Int) {
        Log.d("db", "Adapter1")
        holder.name.text = itemList[position].name  // adapter에 있는 이름에 MemberFriendData의 이름 데이터를 넣는다.
        holder.emailId.text = itemList[position].email
    }

    fun setFriendList(list: ArrayList<MemberData>) {
        itemList = list
        notifyDataSetChanged()
        //Log.d("db", "notifyDataSetChanged called")
    }

    // inner 추가
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.name)
        val emailId: TextView = itemView.findViewById(R.id.emailId)


        // add : recyclerview의 checkBtn눌렀을 때 반응
        val checkButton: AppCompatImageButton = itemView.findViewById(R.id.checkButton)
        init {
            checkButton?.setOnClickListener {
                // 반응
                listener?.onAddFriendClick(adapterPosition)
            }
        }

    }



}