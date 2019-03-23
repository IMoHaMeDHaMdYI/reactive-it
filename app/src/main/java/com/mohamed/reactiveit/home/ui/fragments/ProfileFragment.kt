package com.mohamed.reactiveit.home.ui.fragments


import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.CurrentUser
import com.mohamed.reactiveit.common.baseadapter.BaseAdapter
import com.mohamed.reactiveit.common.baseadapter.Displayable
import com.mohamed.reactiveit.common.db.ECommerceDatabase
import com.mohamed.reactiveit.common.db.repository.ProductRepository
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.models.User
import com.mohamed.reactiveit.common.utils.CURRENT_USER
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.common.utils.TouchHelperCallback
import com.mohamed.reactiveit.common.utils.click
import com.mohamed.reactiveit.home.ui.activities.AddProductActivity
import com.mohamed.reactiveit.home.ui.activities.ShowProductActivity
import com.mohamed.reactiveit.home.ui.adapters.ProductAdapter
import com.mohamed.reactiveit.home.ui.adapters.ProductEmptyViewHolder
import com.mohamed.reactiveit.home.ui.adapters.ProductLoadingMoreViewHolder
import com.mohamed.reactiveit.home.ui.adapters.ProductLoadingViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.view.*

lateinit var mUser: User

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    //----------
    private var isFirst = true
    //----------

    private var isScrolling = false
    private var isScrollable = true
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel: ProfileFragmentViewModel
    private val uiCompositeDisposable = CompositeDisposable()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            val db = ECommerceDatabase.getInstance(view.context.applicationContext)
            val viewModelFactory = ProfileFragmentViewModel.Factory(ProductRepository(db.productDao()))
            viewModel = ViewModelProviders.of(this@ProfileFragment, viewModelFactory)
                .get(ProfileFragmentViewModel::class.java)
            productAdapter = ProductAdapter(
                viewModel.dataList,
                view.context
            ) { displayable, holder ->
                val productHolder = holder as ProductAdapter.ProductViewHolder
                (displayable as Product).let { product ->
                    Intent(view.context, ShowProductActivity::class.java).apply {
                        putExtra("product", product)
                        putExtra(CURRENT_USER, mUser)
                        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                            activity,
                            Pair(
                                productHolder.circularView,
                                getString(R.string.img_transition)
                            )
                        )
                        startActivity(this, transitionActivityOptions.toBundle())
                    }
                }
            }
            if (isFirst) {
                Log.d(TAG, "first loading")
                loadData()
                isFirst = false
            }
            setUpRecyclerView(this)
        }
    }


    private fun setUpRecyclerView(view: View) {
        val manager = LinearLayoutManager(view.context)
        with(view) {
            with(rvProducts) {
                layoutManager = manager
                adapter = productAdapter
                object : RecyclerView.ItemDecoration() {

                }
                addItemDecoration(DividerItemDecoration(view.context, VERTICAL))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val currentItems = manager.childCount
                        val totalItems = manager.itemCount
                        val scrollOutItems = manager.findFirstVisibleItemPosition()
                        Log.d(TAG, "scroll listener")
                        if (isScrolling && (currentItems + scrollOutItems) == totalItems && isScrollable) {
                            Log.d(TAG, "scroll listener")
                            productAdapter.setState(BaseAdapter.STATE_LOADING)
                            loadData()
                        }
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                            isScrolling = true
                    }
                })
                ItemTouchHelper(DismissTouchHelper(
                    view.context
                ) {
                    when (it) {
                        is ProductEmptyViewHolder, is ProductLoadingMoreViewHolder, is ProductLoadingViewHolder -> {
                            0
                        }
                        else -> {
                            ItemTouchHelper.LEFT
                        }
                    }
                }).attachToRecyclerView(rvProducts)

            }

        }
    }

    inner class DismissTouchHelper(context: Context, directions: (RecyclerView.ViewHolder) -> Int) :
        TouchHelperCallback(context, directions) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (direction == ItemTouchHelper.LEFT && productAdapter.size() > 0) {
                val pos = viewHolder.adapterPosition
                viewModel.deleteProduct(viewModel.dataList[pos] as Product, pos)
            }
        }

    }

    fun loadData() {
        isScrolling = false
        isScrollable = false
        Log.d(TAG, "state loading, ${productAdapter.size()}")
        Handler().postDelayed(
            {
                viewModel.getProducts(mUser.id, productAdapter.size())

            }, 500
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                loadData()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Destroyed")
        uiCompositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()
        view?.apply {
            uiCompositeDisposable.addAll(
                fabAdd.click().subscribe {
                    val addProductIntent = Intent(activity, AddProductActivity::class.java)
                    addProductIntent.putExtra(CURRENT_USER, mUser)
                    startActivityForResult(addProductIntent, REQUEST_ADD_PRODUCT)
                },
                viewModel.comingProductListSubject
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        // Here comes new Array
                        val newList = ArrayList<Displayable>().apply { addAll(it) }
                        Log.d(TAG, "Here START")
                        productAdapter.setState(BaseAdapter.STATE_START)
                        productAdapter.add(newList)
                        if (productAdapter.size() == 0) {
                            Log.d(TAG, "state empty")
                            productAdapter.setState(BaseAdapter.STATE_EMPTY)
                        }
                        isScrollable = true
                    },
                viewModel.deletedProductSubject.observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d(TAG, "DELETED AT $it ,, ${viewModel.dataList[it]}")
                        productAdapter.removeAt(it)
                        if (productAdapter.size() == 0) productAdapter.setState(BaseAdapter.STATE_EMPTY)
                    }
            )
        }
    }

    companion object {
        fun instance(user: User = CurrentUser.user): ProfileFragment = lazy {
            mUser = user
            ProfileFragment()
        }.value

        const val Tag = "profile"

        val REQUEST_ADD_PRODUCT = 0

    }

}
