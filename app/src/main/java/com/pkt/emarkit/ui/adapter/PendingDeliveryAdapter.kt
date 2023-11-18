package com.pkt.emarkit.ui.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import com.pkt.emarkit.databinding.PendingProductItemBinding
//import com.pkt.emarkit.databinding.PendingProductItemBinding
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pkt.emarkit.R

import com.pkt.emarkit.ui.viewBinders.DeliveryItemBinder


class PendingDeliveryAdapter:ListAdapter<DeliveryItemBinder,PendingDeliveryAdapter.Holder>(DiffCallback()){

    lateinit var context: Context
    var pendingDeliveryAdapterInterface:AdapterInterface? = null

    private class DiffCallback:DiffUtil.ItemCallback<DeliveryItemBinder>() {
        @SuppressLint("SuspiciousIndentation")
        override fun areItemsTheSame(oldItem: DeliveryItemBinder, newItem: DeliveryItemBinder): Boolean {
         if (oldItem.id != newItem.id)
             return false
            // check if id is the same
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: DeliveryItemBinder, newItem: DeliveryItemBinder): Boolean {
           return oldItem.equals(newItem)
        }
    }

    fun initCallback(adapterInterface:AdapterInterface){
        this.pendingDeliveryAdapterInterface = adapterInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
     val binding:PendingProductItemBinding = DataBindingUtil.inflate(
         LayoutInflater.from(parent.context),
         R.layout.pending_product_item,
         parent, false)
     return Holder(binding)
    }

    inner class Holder(binding: PendingProductItemBinding):RecyclerView.ViewHolder(binding.root) {
      var binding1:PendingProductItemBinding = binding
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: Holder, position: Int) {
      val binding:PendingProductItemBinding = holder.binding1
      binding.model = getItem(position)
      if (getItem(position).delivery_status.equals("Pending Deliveries")){
          binding.constraintLayout.setBackgroundColor(context.getColor(R.color.light_green))
          binding.gotonext.setImageDrawable(context.getDrawable(R.drawable.chevron_right_p))
      }
      if (getItem(position).delivery_status.equals("Cancelled Deliveries")){
          binding.constraintLayout.setBackgroundColor(context.getColor(R.color.light_red))
//          binding.gotonext.setImageDrawable(context.getDrawable(R.drawable.chevron1))
          binding.gotonext.setImageDrawable(null)
      }
      if (getItem(position).delivery_status.equals("Completed Deliveries")){
          binding.constraintLayout.setBackgroundColor(context.getColor(R.color.light_pink))
//          binding.gotonext.setImageDrawable(context.getDrawable(R.drawable.chevron2))
          binding.gotonext.setImageDrawable(null)
      }
      binding.gotonext.setOnClickListener {

        if (getItem(position).delivery_status.equals("Pending Deliveries")){
//            pendingDeliveryAdapterInterface!!.gotoDeliveriesPage(getItem(position).order_code!!)
            pendingDeliveryAdapterInterface!!.gotoDeliveriesPage(getItem(position).order_code!!)
        }
      }

        binding.executePendingBindings()
    }

    interface AdapterInterface{
       fun gotoDeliveriesPage(order_code:String)
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    fun <T> clone(original: MutableSet<T>): MutableSet<T> {
        return HashSet(original)
    }

}


