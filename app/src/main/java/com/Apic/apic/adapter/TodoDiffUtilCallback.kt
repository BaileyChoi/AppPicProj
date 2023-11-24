package com.Apic.apic.adapter

import androidx.recyclerview.widget.DiffUtil
import com.Apic.apic.data.Todo
class TodoDiffUtilCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        // 내용이 변경되었을 때만 false를 반환하도록 수정
        return oldItem == newItem
    }
}