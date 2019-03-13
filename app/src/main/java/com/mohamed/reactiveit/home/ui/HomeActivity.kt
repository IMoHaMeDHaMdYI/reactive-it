package com.mohamed.reactiveit.home.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.baseadapter.BaseAdapter
import com.mohamed.reactiveit.common.baseadapter.Displayable
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.home.ui.adapters.ProductAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private var c = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val productList = arrayListOf<Displayable>()
        for (i in 0..30) {
            productList.add(Product("number+$i", "", false))
        }
        productAdapter = ProductAdapter(productList, this)
        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = productAdapter
        motionLayout.transitionToEnd()
        frameFragmentHolder.setOnClickListener {
            Log.d(TAG,"here")
            if (c % 2 == 0)
                productAdapter.setState(BaseAdapter.STATE_LOADING_START)
            else
                productAdapter.setState(BaseAdapter.STATE_START)
            c++
        }
    }
}
