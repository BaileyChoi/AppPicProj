package com.Apic.apic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: DialogAddFriendAdapter
    private lateinit var fragmentTransaction: FragmentTransaction   //
    private val fragmentFriend = FriendFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = com.Apic.apic.databinding.FragmentAddFriendBinding.inflate(inflater, container, false)
        mRecyclerView = binding.recyclerView
        mRecyclerAdapter = DialogAddFriendAdapter()
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val friendItems = ArrayList<FriendItem>()
        for (i in 1..10) {
            val resourceProfile = R.drawable.person_circle
            friendItems.add(FriendItem(resourceProfile, "${i}@gmail.com", "${i}p"))
        }

        mRecyclerAdapter.setFriendList(friendItems)

        // Close Button click listener -> MainA
        binding.closeBtn.setOnClickListener {
            (activity as? MainActivity)?.setFragment(0)
        }


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_add_friend.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}