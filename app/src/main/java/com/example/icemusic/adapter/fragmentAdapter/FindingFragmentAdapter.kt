package com.example.icemusic.adapter.fragmentAdapter

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.icemusic.R
import com.example.icemusic.fragment.FindingFragment

class FindingFragmentAdapter(var colorList:List<Int>, fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = FindingFragment()
        fragment.arguments = Bundle().apply {
            putInt(FindingFragment.COLOR_OBJECT,colorList[position])
        }
        return fragment
    }

}