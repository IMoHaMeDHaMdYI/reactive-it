package com.mohamed.reactiveit.home.ui.activities

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.mohamed.reactiveit.R
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class HomeViewModel : ViewModel() {

    val fragmentSubject = PublishSubject.create<Pair<Fragment, String>>()
    val currentTabSubject = BehaviorSubject.create<Int>()

    init {
        currentTabSubject.onNext(R.id.tab_home)
    }

}