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
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.PartyWiseProductBinding
import com.pkt.emarkit.ui.viewBinders.PartyWiseProductBinder


class PartyWiseProductAdapter:ListAdapter<PartyWiseProductBinder,PartyWiseProductAdapter.Holder>(DiffCallback()) {

    lateinit var context: Context

    private class DiffCallback: DiffUtil.ItemCallback<PartyWiseProductBinder>() {
        @SuppressLint("SuspiciousIndentation")
        override fun areItemsTheSame(oldItem: PartyWiseProductBinder, newItem: PartyWiseProductBinder): Boolean {
            if (oldItem.id != newItem.id)
                return false
            // check if id is the same
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: PartyWiseProductBinder, newItem: PartyWiseProductBinder): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val binding: PartyWiseProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.party_wise_product,
            parent, false)
        return Holder(binding)
    }

    inner class Holder(binding: PartyWiseProductBinding): RecyclerView.ViewHolder(binding.root) {
        var binding1: PartyWiseProductBinding = binding
    }

    override fun onBindViewHolder(holder:Holder, position: Int) {
        var binding:PartyWiseProductBinding = holder.binding1
        binding.model = getItem(position)
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