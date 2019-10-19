package com.medlab.medlabtest.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListViewHolder<T : BaseItemList>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bindItem(item: T)
}
