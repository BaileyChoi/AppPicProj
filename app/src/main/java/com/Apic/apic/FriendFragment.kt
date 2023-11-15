package com.Apic.apic

import android.app.Dialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.DialogAddFriendBinding
import com.Apic.apic.databinding.FragmentFriendBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dialogAddFriendBinding: DialogAddFriendBinding
    private lateinit var dialogAddFriend: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: MyRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFriendBinding.inflate(inflater, container, false)

        mRecyclerView = binding.recyclerView

        // initiate adapter
        mRecyclerAdapter = MyRecyclerAdapter()

        // initiate recyclerview
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val friendItems = ArrayList<FriendItem>()

        for (i in 1..10) {
            val resourceId = R.drawable.person_circle
            friendItems.add(FriendItem(resourceId, "${i}번째 사람", "${i}번째 상태메시지"))
        }

        mRecyclerAdapter.setFriendList(friendItems)


        // dialog 띄우기
        dialogAddFriend = Dialog(requireActivity()) // Dialog initialization
        dialogAddFriend.requestWindowFeature(Window.FEATURE_NO_TITLE) // Remove title
        //dialogAddFriend.setContentView(R.layout.dialog_add_friend) // Connect with XML layout

        // Using DialogFriendBinding to inflate the layout
        dialogAddFriendBinding = DialogAddFriendBinding.inflate(layoutInflater)
        dialogAddFriend.setContentView(dialogAddFriendBinding.root) // Connect with XML layout

        // Button to show the custom dialog
        binding.addButton.setOnClickListener {
            showDialog() // Call the showDialog01() function
        } // dialog 띄우기


        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_friend, container, false)
    }


    // Function to design dialog01
    private fun showDialog() {
        dialogAddFriend.show() // Show the dialog

        // Implement the desired design and functionality here

        // Connect widgets as per preference
        // Use the dialog name as a prefix when using findViewById()
        // e.g., val noBtn = dilaog01.findViewById<Button>(R.id.noBtn)


        // dialog_add_friend event
        dialogAddFriendBinding.closeBtn.setOnClickListener {    // Closebutton
            dialogAddFriend.dismiss() // Close the dialog
        }
        dialogAddFriendBinding.searchBtn.setOnClickListener {// search button
            // Implement desired functionality
            // 친구 추가에서 친구 이메일 아이디로 친구 검색하기 기능... 추후 개발...
        }
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