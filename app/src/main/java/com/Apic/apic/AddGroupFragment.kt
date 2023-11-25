package com.Apic.apic

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Apic.apic.databinding.FragmentAddGroupBinding
import com.Apic.apic.databinding.FragmentGroupListBinding

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
        binding = FragmentAddGroupBinding.inflate(inflater, container, false)


        // 체크 버튼
        binding.checkBtn.setOnClickListener {
            Toast.makeText(context, "모임 ${binding.EtGoupName.text}(${binding.groupMemberNum.text}명)가 생성되었습니다.", Toast.LENGTH_SHORT).show()
            // AddMeetingFragment 테스트용
            val transaction = fragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.menu_frame_view, AddMeetingFragment()).commitAllowingStateLoss()
            }
            // 그룹 추가

            // 그룹 리스트로 돌아가기
//            val transaction = fragmentManager?.beginTransaction()
//            if (transaction != null) {
//                transaction.replace(R.id.menu_frame_view, GroupListFragment()).commitAllowingStateLoss()
//            }
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

    private fun setOnClickListener() {
        val btnCheck = binding.checkBtn
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