package com.Apic.apic

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentFriendBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: DialogFriendAdapter
    private val originalList: ArrayList<FriendItem> = ArrayList()   // =friendItems // Adapter list이름 확인
    private val searchList: ArrayList<FriendItem> = ArrayList()
    private lateinit var editText: EditText
    private lateinit var dialogFriendAdapter: DialogFriendAdapter
    private lateinit var imm: InputMethodManager;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFriendBinding.inflate(inflater, container, false)

        mRecyclerView = binding.recyclerView

        // initiate adapter
        mRecyclerAdapter = DialogFriendAdapter()

        // initiate recyclerview
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        //val friendItems = ArrayList<FriendItem>()
        for (i in 1..50) {
            val resourceId = R.drawable.ic_person_circle
            //friendItems.add(FriendItem(resourceId, "${i}@gmail.com", "${i}p"))
            originalList.add(FriendItem(resourceId, "${i}@gmail.com", "${i}p"))
        }

        mRecyclerAdapter.setFriendList(originalList)

        // Button click listener
        binding.addButton.setOnClickListener {
            (activity as? MainActivity)?.setFragment(1)
        }

        binding.searchButton.setOnClickListener{
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
        }

        editText = binding.searchView   // 검색어 변수로 받음.

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Optional: Add any functionality needed before text changes
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Optional: Add any functionality needed during text changes
            }

            override fun afterTextChanged(editable: Editable) {
                val searchText = editText.text.toString() //editable.toString()
                searchList.clear()

                if (searchText.isEmpty()) {
                    dialogFriendAdapter.setFriendList(originalList)
                } else {
                    // 검색 단어를 포함하는지 확인
                    for (a in 0 until originalList.size) {
                        if (originalList[a].getName().toLowerCase().contains(searchText.toLowerCase())) {
                            searchList.add(originalList[a])
                        }
                    }
                    //dialogFriendAdapter.setFriendList(originalList)
                    dialogFriendAdapter.setFriendList(searchList)
                }
            }
        })
        //
        // 리사이클러뷰, 어댑터 연결
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogFriendAdapter = DialogFriendAdapter()
        dialogFriendAdapter.setFriendList(originalList)
        recyclerView.adapter = dialogFriendAdapter

        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FriendFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}