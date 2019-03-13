package com.mohamed.reactiveit.home.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.baseadapter.BaseAdapter
import com.mohamed.reactiveit.common.baseadapter.Displayable
import com.mohamed.reactiveit.common.baseadapter.ViewRenderer
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.utils.TAG
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(productList: ArrayList<Displayable>, val context: Context) :
    BaseAdapter(productList) {

    init {
        registerRenderer(
            renderer = ProductRenderer(STATE_START, context) as ViewRenderer<Displayable, RecyclerView.ViewHolder>
        )
        registerRenderer(
            renderer = ProductLoadingRenderer(STATE_LOADING, context) as ViewRenderer<Displayable, RecyclerView.ViewHolder>
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        val renderer = mRendererList.get(item.getType())
        Log.d(TAG, "${item.getType()} + $renderer")
        renderer?.bindView(item, holder)
    }


    class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imgCart = view.imgCart!!
        val imgProduct = view.imgProduct!!
        val tvName = view.tvName!!
        fun bind(product: Product) {
            if (product.inCart) {
                imgCart.setImageResource(R.drawable.ic_cart_clicked)
            } else {
                imgCart.setImageResource(R.drawable.ic_cart)
            }
            tvName.text = product.name
            Glide.with(view.context)
                .load(product.url)
                .into(imgProduct)
            imgCart.setOnClickListener {
                if (product.inCart) {
                    changeImgCart(R.drawable.ic_cart)
                } else {
                    changeImgCart(R.drawable.ic_cart_clicked)
                }
                product.inCart = !product.inCart
            }
        }

        private fun changeImgCart(resource: Int) {
            imgCart.setImageResource(resource)
        }
    }

    class ProductRenderer(type: Int, private val context: Context) : ViewRenderer<Product, ProductViewHolder>(type = type) {
        override fun bindView(model: Product, holder: ProductViewHolder) {
            holder.bind(model)
        }

        override fun createViewHolder(parent: ViewGroup): ProductViewHolder {
            return ProductViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_product, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].getType()
    }
}


// TODO Create an Empty, Loading, Error, and Default ViewHolders
// TODO Create a pass the state of the recycler view from the previous view holders
