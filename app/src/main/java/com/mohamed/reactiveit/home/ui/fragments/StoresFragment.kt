package com.mohamed.reactiveit.home.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.utils.TAG

class StoresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stores, container, false)
    }

    companion object {
        val instance: StoresFragment by lazy { StoresFragment() }
        const val Tag = "stores"
    }
}
