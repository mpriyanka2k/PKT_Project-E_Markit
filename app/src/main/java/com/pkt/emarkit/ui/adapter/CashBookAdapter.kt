package com.pkt.emarkit.ui.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.CashBookLayoutBinding
import com.pkt.emarkit.databinding.ItemWiseProductBinding
import com.pkt.emarkit.ui.viewBinders.CashBookBinder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CashBookAdapter:ListAdapter<CashBookBinder,CashBookAdapter.Holder>(DiffCallback()) {

    lateinit var context: Context
    private class DiffCallback: DiffUtil.ItemCallback<CashBookBinder>() {
        override fun areItemsTheSame(oldItem: CashBookBinder, newItem: CashBookBinder): Boolean {
            if (oldItem.id != newItem.id)
                return false
            // check if id is the same
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CashBookBinder, newItem: CashBookBinder): Boolean {
            return oldItem.equals(newItem)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):Holder {
        context = parent.context
        val binding: CashBookLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cash_book_layout,
            parent, false)
        return Holder(binding)
    }

    inner class Holder(binding: CashBookLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var binding1: CashBookLayoutBinding = binding
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        var binding:CashBookLayoutBinding = holder.binding1
        binding.model = getItem(position)

        binding.name.text = getItem(position).name

        if (getItem(position).type.equals("deposit")){
            Log.e("priyanka_deposit","position = $position = ${getItem(position).amount}")
         binding.deposit.text = getItem(position).amount.toString()
        }
        else{
            binding.deposit.text = ""
        }

        if (getItem(position).type.equals("collect")){
            binding.collect.text = getItem(position).amount.toString()
            Log.e("priyanka_collect","position = $position = ${getItem(position).amount}")
        }
        else{
            binding.collect.text = ""
        }

        ////////////////////////////////////////////////////////////////////////
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(getItem(position).date, formatter)
        val output = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        binding.date.text = output
        /////////////////////////////////////////////////////////////////////

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


