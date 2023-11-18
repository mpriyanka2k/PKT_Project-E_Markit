package com.pkt.emarkit.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.ItemWiseProductBinding

import com.pkt.emarkit.ui.viewBinders.ItemWiseProductBinder

class ItemWiseProductAdapter:ListAdapter<ItemWiseProductBinder,ItemWiseProductAdapter.Holder>(DiffCallback()) {

    lateinit var context: Context

    private class DiffCallback: DiffUtil.ItemCallback<ItemWiseProductBinder>() {
        @SuppressLint("SuspiciousIndentation")
        override fun areItemsTheSame(oldItem: ItemWiseProductBinder, newItem: ItemWiseProductBinder): Boolean {
            if (oldItem.id != newItem.id)
                return false
            // check if id is the same
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ItemWiseProductBinder, newItem: ItemWiseProductBinder): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val binding: ItemWiseProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_wise_product,
            parent, false)
        return Holder(binding)
    }

    inner class Holder(binding: ItemWiseProductBinding): RecyclerView.ViewHolder(binding.root) {
        var binding1: ItemWiseProductBinding = binding
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var binding:ItemWiseProductBinding = holder.binding1
        binding.model = getItem(position)
        Glide.with(context).load(getItem(position).image).into(binding.productImg)
        binding.executePendingBindings()
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    fun <T> clone(original: MutableSet<T>): MutableSet<T> {
        return HashSet(original)
    }


}