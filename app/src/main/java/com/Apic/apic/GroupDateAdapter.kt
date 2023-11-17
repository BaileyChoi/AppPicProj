package com.Apic.apic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentDateBinding
import com.Apic.apic.databinding.GroupDateRecyclerviewBinding

class GroupDateAdapter: RecyclerView.Adapter<GroupDateAdapter.ViewHolder>() {
    private var data = ArrayList<String>()

    inner class ViewHolder(val binding: GroupDateRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : String) {
            binding.groupDate.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        val binding = GroupDateRecyclerviewBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(data[position])

//        holder.layout.layoutListItem.setOnClickListener {
//            Toast.makeText(holder.layout.context, "${list[position]} Click!", Toast.LENGTH_SHORT).show()
//        }
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