package com.medlab.medlabtest.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.medlab.medlabtest.base.BaseScrollingView
import com.medlab.medlabtest.ui.main.favs.FavsFragment
import com.medlab.medlabtest.ui.main.list.MovieListFragment
import com.medlab.medlabtest.utils.Properties

class MainPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = arrayOfNulls<Fragment>(count)

    override fun getItem(position: Int): Fragment {

        val fragment: Fragment = when (position) {
            PAGE_FAV -> FavsFragment.newInstance()
            PAGE_HOME -> MovieListFragment.newInstance()
            else -> MovieListFragment.newInstance()
        }

        fragments[position] = fragment

        return fragment
    }

    override fun getCount(): Int {
        return 2
    }

    companion object {

        const val PAGE_HOME = 0
        const val PAGE_FAV = 1
    }
}

