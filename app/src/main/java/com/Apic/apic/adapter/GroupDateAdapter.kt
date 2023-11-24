package com.Apic.apic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.RvGroupDateBinding

class GroupDateAdapter: RecyclerView.Adapter<GroupDateAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener
    private var data = ArrayList<String>()

    inner class ViewHolder(val binding: RvGroupDateBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : String) {
            binding.groupDate.text = item
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
        val binding = RvGroupDateBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
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