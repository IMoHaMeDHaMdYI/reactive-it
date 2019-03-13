package com.mohamed.reactiveit.common.baseadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.reactiveit.common.utils.TAG

abstract class ViewRenderer<in D : Displayable, VH : RecyclerView.ViewHolder>(val type: Int) {
    abstract fun bindView(model: D, holder: VH)
    abstract fun createViewHolder(parent: ViewGroup): VH
    override fun toString(): String {
        return TAG
    }
}