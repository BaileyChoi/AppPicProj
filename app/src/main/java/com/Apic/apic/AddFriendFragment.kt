package com.Apic.apic

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentAddFriendBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.util.jar.Attributes

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFriendFragment : Fragment(), DialogAddFriendAdapter.OnAddFriendClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAddFriendBinding
    private val db = FirebaseFirestore.getInstance()
    private val itemList = arrayListOf<MemberFriendData>()
    private val adapter = DialogAddFriendAdapter(itemList)
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
        binding = FragmentAddFriendBinding.inflate(inflater, container, false)

        // Close Button click listener -> FriendFragment
        binding.closeBtn.setOnClickListener {
            (activity as? MainActivity)?.setFragment(0)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        binding.searchBtn.setOnClickListener {
            db.collection("memberFriendDB")   // Collection to work with
                .get()                    // Get documents
                .addOnSuccessListener { result ->
                    // On success
                    itemList.clear()
                    for (document in result) {
                        val item = MemberFriendData(
                            document["email"] as String,
                            document["name"] as String
                        )
                        itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()  // Update RecyclerView
                }
                .addOnFailureListener { exception ->
                    // On failure
                    Log.w("AddFriendFragment", "Error getting documents: $exception")
                }
        }

        // add
        //val adapter = DialogAddFriendAdapter(itemList)
        adapter.setOnAddFriendClickListener(this) // Set the listener here
    }

    override fun onAddFriendClick(position: Int) {
        // Handle the button click event for the item at the given position
        // You can show a dialog, perform an action, etc.
        val clickedItem = itemList[position]
        Log.d("db", "Button clicked for ${clickedItem.name}")   // add : 오류 확인을 위함
        // Example: Show a Toast
        Toast.makeText(requireContext(), "Button clicked for ${clickedItem.name}", Toast.LENGTH_SHORT).show()
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