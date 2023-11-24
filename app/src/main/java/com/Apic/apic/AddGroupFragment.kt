package com.Apic.apic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentAddGroupBinding
import com.google.android.play.core.integrity.p
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddGroupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAddGroupBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private lateinit var groupParticipantsAdapter: GroupParticipantsAdapter

    private val friendList = mutableListOf<FriendData>()

    private val participantsList = mutableListOf<FriendData>()
    private val participantsNameList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // 친구 리스트에서 참여자 선택
    private fun setOnClickEvent() {
        groupParticipantsAdapter.setItemClickListener(object: GroupParticipantsAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                super.onClick(view, position)
                Toast.makeText(view.context, "테스트 - ${friendList[position].f_name} 클릭", Toast.LENGTH_SHORT).show()
                participantsList.add(friendList[position])
                participantsNameList.add(friendList[position].f_name)
                binding.groupParticipants.text = participantsNameList.toString()
                binding.groupMemberNum.text = participantsNameList.size.toString()
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddGroupBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        val recyclerView: RecyclerView = binding.groupParticipantsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        groupParticipantsAdapter = GroupParticipantsAdapter(friendList)
        recyclerView.adapter = groupParticipantsAdapter

        getFriendData() // 친구 목록 불러오기

        setOnClickEvent()   // 참여자 선택

        // 체크 버튼
        binding.checkBtn.setOnClickListener {

            // 그룹 추가
            val gName = binding.EtGoupName.text.toString()
            val gParticipants = participantsNameList.size.toString()

            val groupData = GroupData(gName, gParticipants)
            addGroup(groupData)

            // 참여자 추가
            for (participant in participantsList) {
                val fName = participant.f_name
                val fEmail = participant.f_email
                val participantsData = FriendData(fName, fEmail)
                addParticipants(groupData, participantsData)
            }

            Toast.makeText(context, "그룹 생성 완료", Toast.LENGTH_SHORT).show()

            // 그룹 리스트로 돌아가기
            val transaction = fragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.menu_frame_view, GroupListFragment()).commitAllowingStateLoss()
            }
        }

        // 엑스 버튼
        binding.cancelBtn.setOnClickListener {
            // 그룹 리스트로 돌아가기
            val transaction = fragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.menu_frame_view, GroupListFragment()).commitAllowingStateLoss()
            }
        }

        return binding.root
    }

    private fun getFriendData() {
        db.collection("memberDB")
            .document(auth.currentUser!!.email.toString())
            .collection("FriendList")
            .get()
            .addOnSuccessListener { result ->
                friendList.clear()
                for (document in result) {
                    val friend = FriendData (
                        document["email"] as String,
                        document["name"] as String
                    )
                    friendList.add(friend)
                }
                groupParticipantsAdapter.notifyDataSetChanged()
                Log.d("db", "success")
            }
            .addOnFailureListener {
                Log.d("db", "fail")
            }
    }

    private fun addGroup(groupData: GroupData) {

        db.collection("memberDB")
            .document(auth.currentUser!!.email.toString())
            .collection("groups")
            .document(groupData.g_name)
            .set(groupData)
            .addOnCompleteListener {
                Log.d("db", "success")
            }
            .addOnFailureListener {
                Log.d("db", "fail")
            }
    }

    private fun addParticipants(groupData: GroupData, participantsData: FriendData) {

        db.collection("memberDB")
            .document(auth.currentUser!!.email.toString())
            .collection("groups")
            .document(groupData.g_name)
            .collection("participants")
            .document(participantsData.f_email)
            .set(participantsData)
            .addOnCompleteListener {
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
         * @return A new instance of fragment AddGroupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}