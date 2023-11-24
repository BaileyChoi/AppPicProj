package com.Apic.apic


import TodoListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.data.Todo

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoEditText: EditText
    private lateinit var calendarView: CalendarView
    private lateinit var addButton: Button

    private lateinit var viewModel: MainViewModel
    private var selectedDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // 뷰 초기화
        recyclerView = view.findViewById(R.id.recyclerView)
        todoEditText = view.findViewById(R.id.todoEditText)
        calendarView = view.findViewById(R.id.calendar)
        addButton = view.findViewById(R.id.btnAdd)

        // MainViewModel 초기화
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // RecyclerView 초기화
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        // Adapter 설정 부분은 그대로 유지
        val adapter = TodoListAdapter { todo -> viewModel.deleteTodo(todo.id) }
        recyclerView.adapter = adapter

        // CalendarView 이벤트 핸들링
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
        }

        // 초기에 오늘 날짜로 선택되도록 설정
        val today = Calendar.getInstance()
        selectedDate = today.timeInMillis
        calendarView.date = selectedDate

        // "추가" 버튼 클릭 이벤트 처리
        addButton.setOnClickListener {
            val todoText = todoEditText.text.toString()
            if (selectedDate != 0L && todoText.isNotBlank()) {
                // Todo 객체 생성 및 저장
                val newTodo = Todo(todoText, selectedDate)
                viewModel.addTodo(newTodo)

                // EditText 초기화
                todoEditText.text.clear()
            }
        }

        // ViewModel에서 데이터 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { todos ->
                adapter.submitList(todos)
            }
        }

        return view
    }
}