package com.pkt.emarkit.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pkt.emarkit.ui.fragment.ItemWiseFragment
import com.pkt.emarkit.ui.fragment.PartyWiseFragment

class TabAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
         return when(position){

             0-> ItemWiseFragment()

             1-> PartyWiseFragment()

             else -> {
                 getItem(position)
             }
         }
    }

}