package com.Apic.apic

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentGroupListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val groupList = mutableListOf<GroupData>()
    private lateinit var groupListAdapter: GroupListAdapter

    private val searchList: ArrayList<GroupData> = ArrayList()
    private lateinit var Etsearch: EditText

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()


    private fun setOnClickEvent() {
        groupListAdapter.setItemClickListener(object:GroupListAdapter.OnItemClickListener {
            override fun onClick(view:View, position:Int) {
                super.onClick(view, position)
                Toast.makeText(view.context, "테스트 - ${groupList[position].g_name}클릭", Toast.LENGTH_SHORT).show()

                // 그룹 정보 넘기기 -> groupActivity
                val intent = Intent(activity, GroupActivity::class.java)
                intent.putExtra("g_name", groupList[position].g_name)
                intent.putExtra("g_participants", groupList[position].g_participants)
                startActivity(intent)

                // 그룹 정보 넘기기 -> memberFragment
                var memberFragment = MemberFragment()
                var bundle = Bundle()
                bundle.putString("g_name", groupList[position].g_name)
                Log.d("groupname", bundle.toString())
                memberFragment.arguments = bundle
            }
        })
    }
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
        val binding = FragmentGroupListBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        val recyclerView: RecyclerView = binding.groupRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        groupListAdapter = GroupListAdapter(groupList)
        recyclerView.adapter = groupListAdapter

        setOnClickEvent()

        // 그룹 추가
        binding.addBtn.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.menu_frame_view, AddGroupFragment()).commitAllowingStateLoss()
            }
        }

        // 그룹 리스트
        getGroupData()
        //(activity as AppCompatActivity).setSupportActionBar(R.id.back) // 뒤로가기 메뉴

        // 그룹 검색
        Etsearch = binding.Etsearch
        Etsearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun afterTextChanged(p0: Editable?) {
                val searchText = Etsearch.text.toString().trim()
                searchList.clear()

                if (searchText.isEmpty()) {
                    groupListAdapter.setGroupList(groupList)
                }
                else {
                    for (a in 0 until groupList.size) {
                        if (groupList[a].g_name.toLowerCase().contains(searchText.toLowerCase())) {
                            searchList.add(groupList[a])
                        }
                    }
                }
                groupListAdapter.setGroupList(searchList)
                groupListAdapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }

    private fun getGroupData() {
        db.collection("memberDB")
            .document(auth.currentUser!!.email.toString())
            .collection("groups")
            .get()
            .addOnSuccessListener { result ->
                groupList.clear()
                for (document in result) {
                    val group = GroupData (
                        document["g_name"] as String,
                        document["g_participants"] as String
                    )
                    groupList.add(group)
                }
                groupListAdapter.notifyDataSetChanged()
                Log.d("db", "success")
            }
            .addOnFailureListener {
                Log.d("db", "fail")
            }
    }

    // 뒤로가기 버튼
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.toolbar_menu, menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            R.id.back -> {
//                // 뒤로 가기 메뉴
//            }
//        }
//        return false
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GroupListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}