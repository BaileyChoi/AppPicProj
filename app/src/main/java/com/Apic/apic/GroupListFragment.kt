package com.Apic.apic

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentGroupListBinding

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

    private val group_datas : ArrayList<GroupListItem> = ArrayList()
    private lateinit var groupListAdapter: GroupListAdapter

    private fun setOnClickEvent() {
        groupListAdapter.setItemClickListener(object: GroupListAdapter.OnItemClickListener {
            override fun onClick(view:View, position:Int) {
                super.onClick(view, position)
                Toast.makeText(view.context, "테스트 - ${group_datas[position].getGroupName()}클릭", Toast.LENGTH_SHORT).show()
                val intent = Intent(getActivity(), GroupActivity::class.java)
                startActivity(intent)
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

        group_datas.add(GroupListItem("GroupA", 3, true))
        group_datas.add(GroupListItem("GroupB", 5, true))
        group_datas.add(GroupListItem("GroupC", 6, true))
        group_datas.add(GroupListItem("GroupD", 9, true))

        val recyclerView: RecyclerView = binding.groupRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        groupListAdapter = GroupListAdapter()
        groupListAdapter.replaceList(group_datas)
        recyclerView.adapter = groupListAdapter

        setOnClickEvent()

        // 그룹 추가
        binding.addBtn.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.menu_frame_view, AddGroupFragment()).commitAllowingStateLoss()
            }

        }

        //(activity as AppCompatActivity).setSupportActionBar(R.id.back) // 뒤로가기 메뉴

        return binding.root
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