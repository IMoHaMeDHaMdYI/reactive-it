package com.mohamed.reactiveit.home.ui.fragments


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.baseadapter.BaseAdapter
import com.mohamed.reactiveit.common.baseadapter.Displayable
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.utils.TouchHelperCallback
import com.mohamed.reactiveit.home.ui.adapters.ProductAdapter
import com.mohamed.reactiveit.home.ui.adapters.ProductEmptyViewHolder
import com.mohamed.reactiveit.home.ui.adapters.ProductLoadingMoreViewHolder
import com.mohamed.reactiveit.home.ui.adapters.ProductLoadingViewHolder
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {


    private lateinit var productAdapter: ProductAdapter
    private var c = 0
    private var isScrolling = false
    private var isScrollable = true
    private lateinit var productList: ArrayList<Displayable>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productList = arrayListOf()
        with(view) {
            val layoutManager = LinearLayoutManager(view.context)
            rvProducts.layoutManager = layoutManager
            productAdapter = ProductAdapter(productList, view.context){a,b->}
            Handler().postDelayed(
                {
                    productAdapter.setState(BaseAdapter.STATE_START)
                    for (i in 0..30) {
                        productList.add(Product("number+$i", "", 1,25f))
                    }
                }, 3000
            )

            rvProducts.adapter = productAdapter
            rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val currentItems = layoutManager.childCount
                    val totalItems = layoutManager.itemCount
                    val scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    if (isScrolling && (currentItems + scrollOutItems) == totalItems && isScrollable) {
                        isScrolling = false
                        addData(totalItems)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        isScrolling = true
                }
            })

            val itemTouchCallback = object: TouchHelperCallback(this.context,{
                when(it){
                    is ProductEmptyViewHolder,is ProductLoadingMoreViewHolder,is ProductLoadingViewHolder ->{
                        0
                    }
                    else ->{
                        ItemTouchHelper.LEFT
                    }
                }
            }){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }

            }
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rvProducts)

        }

    }

    fun addData(totalItems: Int) {
        productAdapter.setState(BaseAdapter.STATE_LOADING)
        isScrollable = false
        Handler().postDelayed({
            productAdapter.setState(BaseAdapter.STATE_START)
            for (i in totalItems..totalItems + 10) {
                productAdapter.add(Product("number+$i", "", 1,25f))
            }
            isScrollable = true
        }, 2000)

    }

    companion object {
        val instance: HomeFragment by lazy { HomeFragment() }
        const val Tag = "home"
    }
}
