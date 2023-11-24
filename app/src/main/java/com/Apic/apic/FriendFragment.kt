package com.Apic.apic

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Apic.apic.databinding.FragmentAddFriendBinding
import com.Apic.apic.databinding.FragmentFriendBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: DialogFriendAdapter
    private lateinit var editText: EditText
    private val originalList: ArrayList<MemberData> = ArrayList()
    private val searchList: ArrayList<MemberData> = ArrayList()

    // firestore에서 data가져올때 사용
    private lateinit var auth: FirebaseAuth // 친구 리스트와 자신 email 비교를 위해 가져옴.
    private val db = FirebaseFirestore.getInstance()
    //private val itemList = arrayListOf<MemberData>()
    //private lateinit var dialogFriendAdapter: DialogFriendAdapter
    private var adapter = DialogFriendAdapter(originalList)
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
        val binding = FragmentFriendBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        mRecyclerView = binding.recyclerView


        // initiate adapter & recyclerview
        mRecyclerAdapter = DialogFriendAdapter(originalList)
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 친구 firestore 'memberDB'의 friendlist를 가져옴.
        // 친구 찾기에서 친구 리스트로 recyclerview로 뜨기
        db.collection("memberDB")
            .document(auth.currentUser?.email.toString())
            .collection("FriendList") // friendlist 항목 넣어야함.
            .get()                    // Get documents
            .addOnSuccessListener { result ->
                // On success
                originalList.clear()
                for (document in result) {
                    val item = MemberData(
                        document["email"] as String,
                        document["name"] as String,
                        //document["password"] as String
                    )
                    if (!auth.currentUser?.email.toString().equals(document["email"])) {    // 현재 유저가 아닐때
                        originalList.add(item)
                    }
                }

                // 데이터 로드가 완료된 후에 어댑터 초기화와 업데이트
                adapter.notifyDataSetChanged()

                //adapter.notifyDataSetChanged()  // Update RecyclerView
//                adapter.setFriendList(originalList)
//                mRecyclerView.adapter = adapter
//                mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
            .addOnFailureListener { exception ->
                Log.w("AddFriendFragment", "Error getting documents: $exception")
            }//

        /*val friendItems = ArrayList<FriendItem>()
        for (i in 1..50) {
            val resourceId = R.drawable.ic_person_circle
            //friendItems.add(FriendItem(resourceId, "${i}@gmail.com", "${i}p"))
            originalList.add(FriendItem(resourceId, "${i}@gmail.com", "${i}p"))
        }*/

        mRecyclerAdapter.setFriendList(originalList)

        // Button click listener : AddFriendFragment로 이동
        binding.addButton.setOnClickListener {
            (activity as? MainActivity)?.setFragment(1)
        }

        // Button click listener : search Friend email
        binding.searchButton.setOnClickListener{
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
        }

        editText = binding.searchView   // 검색어 변수로 받음.

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Optional: Add any functionality needed before text changes
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Optional: Add any functionality needed during text changes
            }


            override fun afterTextChanged(editable: Editable) {
                //Log.d("db", "addTextChangedListener")
                val searchText = editText.text.toString().trim() //editable.toString()
                //Log.d("db", "$searchText")
                searchList.clear()  // 검색 결과 갱신

                if (searchText.isEmpty()) {
                    adapter.setFriendList(originalList)
                } else {
                    // 검색 단어를 포함하는지 확인
                    for (a in 0 until originalList.size) {
                        //Log.d("db", "for loop: $a")
                        if (originalList[a].getName().toLowerCase().contains(searchText.toLowerCase())) {
                            searchList.add(originalList[a])
                        }
                    }
                    /*for (original in originalList){
                        if(searchText.contains(original.getName(), ignoreCase=true)){
                            val fname = original.name
                            val femail = original.email
                            Log.d("db", fname)
                            Log.d("db", femail)
                            val search = MemberData(femail. fname)
                            searchList.add(search)
                            searchList = MemberData(femail, fname)
                        }
                    }*/
                    //dialogFriendAdapter.setFriendList(originalList)
                    adapter.setFriendList(searchList)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        // 리사이클러뷰, 어댑터 연결
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //어댑터 연결 확인
        //adapter = DialogFriendAdapter(originalList) //
        adapter.setFriendList(originalList) //
        recyclerView.adapter = adapter  //
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