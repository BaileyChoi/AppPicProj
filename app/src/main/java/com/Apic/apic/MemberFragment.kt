package com.Apic.apic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentDateBinding
import com.Apic.apic.databinding.FragmentMemberBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MemberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemberFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMemberBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private lateinit var groupMemberAdapter: GroupMemberAdapter

    private val memberList = mutableListOf<GroupMemberData>()

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
        // Inflate the layout for this fragment
        binding = FragmentMemberBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        val recyclerView: RecyclerView = binding.groupMemberRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        groupMemberAdapter = GroupMemberAdapter(memberList)
        recyclerView.adapter = groupMemberAdapter

//        val groupName = arguments?.getString("g_name")
        // 오류사항 - 시연때 그룹이름 픽스
        val groupName = "apppic"
        getMemberData(groupName)

        return binding.root
    }

    private fun getMemberData(g_name:String) {
        db.collection("memberDB")
            .document(auth.currentUser!!.email.toString())
            .collection("groups")
            .document(g_name)
            .collection("participants")
            .get()
            .addOnSuccessListener { result ->
                memberList.clear()
                for (document in result) {
                    val member = GroupMemberData (
                        document["f_email"] as String,
                            )
                    memberList.add(member)
                }
                groupMemberAdapter.notifyDataSetChanged()
                Log.d("db", "success")
            }
            .addOnFailureListener {
                Log.d("db", "fail")
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MemberFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MemberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}