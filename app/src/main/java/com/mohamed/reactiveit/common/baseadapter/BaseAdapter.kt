package com.mohamed.reactiveit.common.baseadapter

import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.reactiveit.common.utils.TAG


abstract class BaseAdapter(protected val dataList: ArrayList<Displayable>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val STATE_START = 0
        // This is used when the list is all empty.
        val STATE_LOADING_START = 1
        // This is used when there is scroll to load more.
        val STATE_LOADING = 2
        val STATE_NETWORK_ERROR = 3
        val STATE_EMPTY = 4

    }

    init {
        setState(STATE_START)
    }

    // When true indicates that there is no previous error, loading, or empty states.
    private var mReady = false

    protected val mRendererList = SparseArray<ViewRenderer<Displayable, RecyclerView.ViewHolder>>()


    fun setState(state: Int) {
        if (state != STATE_START) mReady = false
        when (state) {
            STATE_START -> onLoaded()
            STATE_EMPTY -> onEmpty()
            STATE_LOADING, STATE_LOADING_START -> onLoading()
            STATE_NETWORK_ERROR -> onNetworkError()
        }
    }

    private fun onLoading() {
        add(
            if (dataList.size == 0) STATE_LOADING_START
            else STATE_LOADING
        )
    }

    private fun onNetworkError() {
        add(STATE_NETWORK_ERROR)
    }

    private fun onEmpty() {
        add(STATE_EMPTY)
    }

    private fun onLoaded() {
        if (!mReady) {
            dataList.removeAt(dataList.size - 1)
            notifyItemRemoved(dataList.size)
        }
        mReady = true
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "Type in the BaseAdapter : $viewType")
        val viewRenderer = mRendererList.get(viewType)
        viewRenderer?.let { return it.createViewHolder(parent) }
        throw RuntimeException("Not Supported Item View Type: $viewType")
    }

    fun registerRenderer(renderer: ViewRenderer<Displayable, RecyclerView.ViewHolder>) {
        val type = renderer.type
        if (mRendererList.get(type) == null) {
            mRendererList.put(type, renderer)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun replace(list: ArrayList<Displayable>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: Displayable) {
        dataList.add(item)
        notifyItemInserted(dataList.size - 1)
    }

    fun add(itemList: ArrayList<Displayable>) {
        val oldSz = itemList.size
        dataList.addAll(itemList)
        notifyItemRangeInserted(oldSz, itemList.size)
    }

    private fun add(type: Int) {
        dataList.add(object : Displayable {
            override fun getType(): Int {
                return type
            }
        })
        notifyItemInserted(dataList.size-1)
    }

}
