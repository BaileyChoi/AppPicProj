package com.Apic.apic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentDateBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var groupDateAdapter: GroupDateAdapter
    private var dateList : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun setOnClickEvent() {
        groupDateAdapter.setItemClickListener(object: GroupDateAdapter.OnItemClickListener {
            override fun onClick(view:View, position:Int) {
                super.onClick(view, position)
                Toast.makeText(view.context, "테스트 - ${dateList[position]} 클릭", Toast.LENGTH_SHORT).show()
                // 약속 프레그먼트(게시글/사진첩)로 이동
                // val intent = Intent(getActivity(), GroupActivity::class.java)
                // startActivity(intent)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDateBinding.inflate(inflater, container, false)
        dateList.add("09.07")
        dateList.add("09.13")
        dateList.add("08.16")
        dateList.add("08.23")
        dateList.add("07.11")
        dateList.add("06.22")
        dateList.add("05.01")

        val recyclerView:RecyclerView = binding.groupDateRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        groupDateAdapter = GroupDateAdapter()
        groupDateAdapter.replaceList(dateList)
        recyclerView.adapter = groupDateAdapter

        setOnClickEvent()

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}