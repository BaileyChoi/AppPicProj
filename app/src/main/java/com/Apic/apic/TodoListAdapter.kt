package com.Apic.apic

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.Apic.apic.data.Todo
import com.Apic.apic.databinding.ItemTodoBinding
import androidx.recyclerview.widget.RecyclerView


class TodoListAdapter(
    private val onClick: (Todo) -> Unit
) : ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoDiffUtilCallback()) {

    // ViewHolder 생성, 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding, onClick)
    }

    // ViewHolder와 데이터 바인딩
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setOnClickListener(getItem(position))
    }

    // ViewHolder
    class TodoViewHolder(
        private val binding: ItemTodoBinding,
        private val onClick: (Todo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.text1.text = todo.title
            binding.text2.text = DateFormat.format("yyyy/MM/dd", todo.date)
        }

        fun setOnClickListener(todo: Todo) {
            binding.root.setOnClickListener {
                onClick(todo)
            }
        }
    }
}
