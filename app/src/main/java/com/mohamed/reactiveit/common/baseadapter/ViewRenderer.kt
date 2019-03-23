package com.mohamed.reactiveit.common.baseadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.reactiveit.common.utils.TAG

abstract class ViewRenderer<in D : Displayable, VH : RecyclerView.ViewHolder>(val type: Int) {
    open fun bindView(model: D, holder: VH, onItemClicked: (Displayable) -> Unit = {}){}
    open fun bindView(model: D, holder: VH, onItemClicked: (Displayable,RecyclerView.ViewHolder) -> Unit = {_,_1->}){}
    abstract fun createViewHolder(parent: ViewGroup): VH
    override fun toString(): String {
        return TAG
    }
}