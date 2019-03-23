package com.mohamed.reactiveit.common.utils

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AppFragmentManager(
    private val fragmentList: ArrayList<Pair<String, Fragment>>
    , private val supportFragmentManager: FragmentManager
    , private val fragmentView: Int
) {
    private var currentFragment: Fragment? = null

    private fun addFragment(fragment: Fragment, tag: String) {
        fragmentList.add(Pair(tag, fragment))
        supportFragmentManager.beginTransaction().add(fragmentView, fragment, tag).commitNow()
    }

    fun showFragment(fragment: Fragment, tag: String) {
        currentFragment?.let {
            if (fragment::class.java.simpleName == it::class.java.simpleName) return
//            Log.d(TAG, "${it::class.java.simpleName} - ${fragment::class.java.simpleName}")
        }
        if (!fragmentExist(tag)) addFragment(fragment, tag)
        currentFragment?.let { hideFragment(it) }
        supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(tag)!!).commit()
        currentFragment = supportFragmentManager.findFragmentByTag(tag)!!
//        Log.d(TAG, supportFragmentManager.fragments.toString())
    }

    private fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(fragment).commit()
    }

    private fun fragmentExist(tag: String): Boolean {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            return false
        }
        return true
    }

}