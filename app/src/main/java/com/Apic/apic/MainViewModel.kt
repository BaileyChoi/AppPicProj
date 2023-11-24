// MainViewModel.kt
package com.Apic.apic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.Apic.apic.data.Todo
import com.Apic.apic.data.TodoDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // 데이터베이스
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java, "todo"
    ).build()

    // db의 결과를 관찰
    private val _items = MutableStateFlow<List<Todo>>(emptyList())
    val items: StateFlow<List<Todo>> = _items

    // 초기화시 모든 데이터를 읽어 옴
    init {
        viewModelScope.launch {
            db.todoDao().getAll().collect { todos ->
                _items.value = todos
            }
        }
    }

    // 추가
    fun addTodo(todo: Todo) { // Modify this line
        viewModelScope.launch {
            db.todoDao().insert(todo) // Modify this line
        }
    }

    fun deleteTodo(id: Long) {
        _items.value
            .find { todo -> todo.id == id }
            ?.let { todo ->
                viewModelScope.launch {
                    db.todoDao().delete(todo)
                }
            }
    }
}
