package com.mohamed.reactiveit.home.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.models.User
import com.mohamed.reactiveit.common.utils.AppFragmentManager
import com.mohamed.reactiveit.common.utils.CURRENT_USER
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.home.ui.fragments.HomeFragment
import com.mohamed.reactiveit.home.ui.fragments.ProfileFragment
import com.mohamed.reactiveit.home.ui.fragments.SearchFragment
import com.mohamed.reactiveit.home.ui.fragments.StoresFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_navigator.*
import kotlinx.android.synthetic.main.app_action_bar.*

class HomeActivity : AppCompatActivity() {

    private lateinit var appFragmentManager: AppFragmentManager
    private lateinit var viewModel: HomeViewModel
    private val uiCompositeDisposable = CompositeDisposable()
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_navigator)
        fetchIntent()
        Log.d(TAG,"onCreate")
        appFragmentManager = AppFragmentManager(ArrayList(), supportFragmentManager, R.id.frameFragment)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        initializeSubjects()
        setNavigationViewListener()
        setBottomNavigationListener()
    }

    // Utilities functions
    private fun setNavigationViewListener() {
        imgMenu.setOnClickListener { drawerLayout.toggle() }
    }

    fun DrawerLayout.toggle() {
        if (isDrawerOpen(GravityCompat.START))
            closeDrawer(GravityCompat.START)
        else openDrawer(GravityCompat.START)
    }


    private fun setBottomNavigationListener() {
        bottomNav.setOnNavigationItemSelectedListener(onBottomNavigationItemSelected)
    }

    private val onBottomNavigationItemSelected =
        BottomNavigationView.OnNavigationItemSelectedListener {
            viewModel.currentTabSubject.onNext(it.itemId)
            return@OnNavigationItemSelectedListener true
        }

    private fun fetchIntent() {
        if (intent.hasExtra(CURRENT_USER)) {
            mUser = intent.getParcelableExtra(CURRENT_USER)
        } else {
            finish()
        }
    }

    private fun initializeSubjects() {
        uiCompositeDisposable.add(viewModel.fragmentSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                appFragmentManager.showFragment(it.first, it.second)
            }
        )
        uiCompositeDisposable.add(viewModel.currentTabSubject.subscribe {
            when (it) {
                R.id.tab_home -> {
                    Log.d(TAG, "Home")
                    viewModel.fragmentSubject.onNext(Pair(
                        HomeFragment.instance.apply { retainInstance = true },
                        HomeFragment.Tag
                    ))
                }
                R.id.tab_profile -> {
                    Log.d(TAG, "profile")
                    viewModel.fragmentSubject.onNext(Pair(
                        ProfileFragment.instance(mUser).apply { retainInstance = true },
                        ProfileFragment.Tag
                    ))
                }
                R.id.tab_search ->
                    viewModel.fragmentSubject.onNext(Pair(
                        SearchFragment.instance.apply { retainInstance = true },
                        SearchFragment.Tag
                    ))
                R.id.tab_sellers ->
                    viewModel.fragmentSubject.onNext(Pair(
                        StoresFragment.instance.apply { retainInstance = true },
                        StoresFragment.Tag
                    ))
            }
        })
    }



    // ********************
    // Activity functions
    override fun onStop() {
        super.onStop()
        Log.d(TAG,"Stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        uiCompositeDisposable.clear()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.toggle()
        else
            super.onBackPressed()
    }
}

/*
* States I have For The Activity
*   1- Current Fragment Tag
*   2- Drawer Open Or Not
*   3- Cart Open Or Not
*
* */
