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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.Apic.apic.databinding.FragmentAddFriendBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFriendFragment : Fragment(), DialogAddFriendAdapter.OnAddFriendClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth // 친구 리스트와 자신 email 비교를 위해 가져옴.
    private lateinit var binding: FragmentAddFriendBinding
    private val db = FirebaseFirestore.getInstance()
    //private val itemList = arrayListOf<MemberData>()
    private lateinit var editText: EditText
    private val originalList: ArrayList<MemberData> = ArrayList()
    private val searchList: ArrayList<MemberData> = ArrayList()
    private var adapter = DialogAddFriendAdapter(originalList)
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
        auth = FirebaseAuth.getInstance()

        // Close Button click listener -> FriendFragment
        binding.closeBtn.setOnClickListener {
            (activity as? MainActivity)?.setFragment(0)
        }

        // Button click listener : search Friend email
        binding.searchBtn.setOnClickListener{
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

                    //dialogFriendAdapter.setFriendList(originalList)
                    adapter.setFriendList(searchList)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        // 친구 찾기에서 친구 리스트로 recyclerview로 뜨기
        db.collection("memberDB")   // Collection to work with
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
                adapter.notifyDataSetChanged()  // Update RecyclerView
            }
            .addOnFailureListener { exception ->
                // On failure
                Log.w("AddFriendFragment", "Error getting documents: $exception")
            }


        // add
        //val adapter = DialogAddFriendAdapter(itemList)
        adapter.setOnAddFriendClickListener(this) // Set the listener here
    }

    // checkBtn누르면 firestore에 친구 추가하기.
    override fun onAddFriendClick(position: Int) {
        val clickedItem = originalList[position]
        Log.d("db", "Button clicked for ${clickedItem.name}")   // add : 오류 확인을 위함
        var name  = clickedItem.name
        var email = clickedItem.email
        val reqFriendList = FriendList(email, name)  //
        //val resFriendList = FriendList(auth.currentUser?.email, auth.currentUser?.name)  //
        addFriend(reqFriendList)

        Toast.makeText(requireContext(), "${clickedItem.name} : 친구 추가", Toast.LENGTH_SHORT).show()
    }

    // 친구 추가 하위 컬렉션 추가 : addFriend(friendData)
    private fun addFriend(data: FriendList){
        Log.d("db", "addFriend")
        val db = FirebaseFirestore.getInstance()
        db.collection("memberDB")
            .document(auth.currentUser?.email.toString())
            .collection("FriendList")
            .document(data.email)   // .add(data)해도 상관없음
            .set(data)
            .addOnSuccessListener {
                Log.d("db", "success : addFriend")
            }.addOnFailureListener{
                Log.d("db", "fail : addFriend")
            }
    }
    //

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