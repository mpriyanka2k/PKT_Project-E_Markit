package com.pkt.emarkit.ui.adapter

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
//import com.pkt.emarkit.api.api_models.DataX
import com.pkt.emarkit.databinding.ProductItemLayoutBinding
import com.pkt.emarkit.ui.viewBinders.ProductItemBinder

class ProductItemAdapter:ListAdapter<ProductItemBinder,ProductItemAdapter.Holder>(DiffCallback()) {

    lateinit var context: Context
    var productItemAdapterInterface: AdapterInterface? = null


    private class DiffCallback:DiffUtil.ItemCallback<ProductItemBinder>() {
        override fun areItemsTheSame(oldItem: ProductItemBinder, newItem: ProductItemBinder): Boolean {
            if (oldItem.id != newItem.id)
                return false
            // check if id is the same
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductItemBinder, newItem: ProductItemBinder): Boolean {
            return oldItem.equals(newItem)
        }
    }

    fun initCallback(adapterInterface:AdapterInterface){
        this.productItemAdapterInterface = adapterInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val binding: ProductItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_item_layout,
            parent, false)
        return Holder(binding)
    }

    inner class Holder(binding: ProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var binding1:ProductItemLayoutBinding = binding
    }

//    override fun getItem(position: Int): ProductItemBinder {
//        return super.getItem(position)
//    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       val binding:ProductItemLayoutBinding = holder.binding1
       binding.model = getItem(position)

        var image = getItem(position).product_img
        Glide.with(context).load(image).into(binding.productImg);

        binding.decrease.setOnClickListener {
            productItemAdapterInterface!!.decrease(position,getItem(position).available_qty,getItem(position).amount!!.toInt())
        }

        binding.increase.setOnClickListener {
            productItemAdapterInterface!!.increase(position,getItem(position).available_qty, getItem(position).amount!!.toInt())

        }

        binding.executePendingBindings()
    }



    interface AdapterInterface{
        fun increase(position: Int,available_qty: Int?,single_product_amount:Int)
        fun decrease(position: Int,available_qty: Int?,single_product_amount:Int)
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    fun <T> clone(original: MutableSet<T>): MutableSet<T> {
        return HashSet(original)
    }

}


