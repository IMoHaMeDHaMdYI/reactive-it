package com.mohamed.reactiveit.home.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.baseadapter.Displayable
import com.mohamed.reactiveit.common.baseadapter.ViewRenderer

class ProductLoadingRenderer(type: Int, private val context: Context) : ViewRenderer<Displayable, ProductLoadingViewHolder>(type = type) {
    override fun bindView(model: Displayable, holder: ProductLoadingViewHolder, onItemClicked: (Displayable) -> Unit) {
    }


    override fun createViewHolder(parent: ViewGroup): ProductLoadingViewHolder {
        return ProductLoadingViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_full_screen_loading, parent, false))
    }

}

class ProductLoadingViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class ProductLoadingMoreRenderer(type: Int, private val context: Context) : ViewRenderer<Displayable, ProductLoadingMoreViewHolder>(type = type) {
    override fun bindView(model: Displayable, holder: ProductLoadingMoreViewHolder, onItemClicked: (Displayable) -> Unit) {

    }


    override fun createViewHolder(parent: ViewGroup): ProductLoadingMoreViewHolder {
        return ProductLoadingMoreViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_loading, parent, false))
    }

}

class ProductLoadingMoreViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class ProductEmptyRenderer(type: Int, private val context: Context) : ViewRenderer<Displayable, ProductEmptyViewHolder>(type = type) {
    override fun bindView(model: Displayable, holder: ProductEmptyViewHolder, onItemClicked: (Displayable) -> Unit) {
    }

    override fun createViewHolder(parent: ViewGroup): ProductEmptyViewHolder {
        return ProductEmptyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_empty, parent, false))
    }

}

class ProductEmptyViewHolder(val view: View) : RecyclerView.ViewHolder(view)