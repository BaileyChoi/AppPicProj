package com.Apic.apic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IllegalArgumentException
class GroupFragment : Fragment() {

    private lateinit var firstFragment: FirstFragment
    private lateinit var secondFragment: SecondFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false)







    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
//        val adapter = PagerAdapter(this)
//        viewPager.adapter = adapter
//
//        val tabLayout: TabLayout = view.findViewById(R.id.tabs)
//
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                val transaction = childFragmentManager.beginTransaction()
//                when (tab?.text) {
//                    "Tab1" -> transaction.replace(R.id.tab_content, BoardFragment())
//                    "Tab2" -> transaction.replace(R.id.tab_content, FriendsFragment())
//                }
//                transaction.commit()
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                // Unselected tab logic, if needed
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                // Reselected tab logic, if needed
//            }
//        })
//


    }

    class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FirstFragment()
                1 -> SecondFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }

}
