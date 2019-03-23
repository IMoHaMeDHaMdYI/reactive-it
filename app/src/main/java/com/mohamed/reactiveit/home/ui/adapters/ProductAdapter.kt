package com.mohamed.reactiveit.home.ui.adapters

import android.content.Context
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
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(productList: ArrayList<Displayable>, context: Context,
                     private val onItemClicked: (Displayable,RecyclerView.ViewHolder) -> Unit) :
    BaseAdapter(productList) {

    init {
        registerRenderer(
            renderer = ProductRenderer(STATE_START, context) as ViewRenderer<Displayable, RecyclerView.ViewHolder>
        )
        registerRenderer(
            renderer = ProductLoadingRenderer(STATE_LOADING_START, context) as ViewRenderer<Displayable, RecyclerView.ViewHolder>
        )
        registerRenderer(
            renderer = ProductLoadingMoreRenderer(STATE_LOADING, context) as ViewRenderer<Displayable, RecyclerView.ViewHolder>
        )
        registerRenderer(
            renderer = ProductEmptyRenderer(STATE_EMPTY, context) as ViewRenderer<Displayable, RecyclerView.ViewHolder>
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        val renderer = mRendererList.get(item.getType())
        renderer?.bindView(item, holder, onItemClicked)
    }


    class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val imgCart = view.imgCart!!
        val imgProduct = view.imgProduct!!
        val tvName = view.tvName!!
        val tvInformation = view.tvInformation!!
        val tvPrice = view.tvPrice!!
        val ratingBar = view.ratingBar!!
        val circularView = view.viewCircular!!
        fun bind(product: Product, onItemClicked: (Product,ProductViewHolder) -> Unit ) {
            if (product.inCart) {
                imgCart.setImageResource(R.drawable.ic_cart_clicked)
            } else {
                imgCart.setImageResource(R.drawable.ic_cart)
            }
            tvName.text = product.name
            Glide.with(view.context)
                .load(product.url)
                .into(imgProduct)
            val price = "${product.price} Price Less"
            tvPrice.text = price
            tvInformation.text = product.information
            ratingBar.rating = product.rating
            imgCart.setOnClickListener {
                if (product.inCart) {
                    changeImgCart(R.drawable.ic_cart)
                } else {
                    changeImgCart(R.drawable.ic_cart_clicked)
                }
                product.inCart = !product.inCart
            }
            itemView.setOnClickListener { onItemClicked(product,this) }
        }

        private fun changeImgCart(resource: Int) {
            imgCart.setImageResource(resource)
        }
    }

    class ProductRenderer(type: Int, private val context: Context) : ViewRenderer<Product, ProductViewHolder>(type = type) {
        override fun bindView(model: Product, holder: ProductViewHolder, onItemClicked: (Displayable,RecyclerView.ViewHolder) -> Unit) {
            holder.bind(model, onItemClicked)
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
