package com.Apic.apic

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.DialogAddFriendBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: DialogFriendAdapter

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
        val binding = com.Apic.apic.databinding.FragmentFriendBinding.inflate(inflater, container, false)

        mRecyclerView = binding.recyclerView

        // initiate adapter
        mRecyclerAdapter = DialogFriendAdapter()

        // initiate recyclerview
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val friendItems = ArrayList<FriendItem>()
        for (i in 1..10) {
            val resourceId = R.drawable.person_circle
            friendItems.add(FriendItem(resourceId, "${i}번째 사람", "${i}번째 상태메시지"))
        }

        mRecyclerAdapter.setFriendList(friendItems)

        // Button click listener
        binding.addButton.setOnClickListener {
            (activity as? MainActivity)?.setFragment(1)
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