package com.mohamed.reactiveit.common.models

import com.mohamed.reactiveit.common.baseadapter.BaseAdapter
import com.mohamed.reactiveit.common.baseadapter.Displayable

class Product(val name: String, val url: String, var inCart: Boolean) :Displayable {
    override fun getType(): Int {
        return BaseAdapter.STATE_START
    }
}