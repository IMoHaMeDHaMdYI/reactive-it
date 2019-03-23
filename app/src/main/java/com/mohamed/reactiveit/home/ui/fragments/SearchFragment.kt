package com.mohamed.reactiveit.home.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.CurrentUser
import com.mohamed.reactiveit.common.baseadapter.BaseAdapter
import com.mohamed.reactiveit.common.baseadapter.Displayable
import com.mohamed.reactiveit.common.db.ECommerceDatabase
import com.mohamed.reactiveit.common.db.repository.ProductRepository
import com.mohamed.reactiveit.home.ui.adapters.ProductAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            val db = ECommerceDatabase.getInstance(this.context.applicationContext)
            val productRepo = ProductRepository(db.productDao())
            val adapter = ProductAdapter(ArrayList(), this.context){a,b->}
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(this.context)
            productRepo.getUserProduct(CurrentUser.user.id, 100, 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val a = ArrayList<Displayable>().apply { addAll(it) }
                    adapter.setState(BaseAdapter.STATE_START)
                    adapter.add(a)
                }
        }
    }

    companion object {
        val instance: SearchFragment by lazy { SearchFragment() }
        const val Tag = "search"
    }
}
