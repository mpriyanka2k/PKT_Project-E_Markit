package com.pkt.emarkit.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.FragmentItemWiseBinding
import com.pkt.emarkit.ui.activities.StockActivity
import com.pkt.emarkit.ui.adapter.ItemWiseProductAdapter
import com.pkt.emarkit.ui.interfaces.PassDatafromActivityToFragment
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewmodels.StockViewmodel

class ItemWiseFragment : Fragment(),PassDatafromActivityToFragment {

    lateinit var binding:FragmentItemWiseBinding
    lateinit var stockViewmodel: StockViewmodel
    lateinit var adapter: ItemWiseProductAdapter
    private var activity: StockActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        //Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_item_wise,container,false)
        var view = binding.root
        binding.lifecycleOwner = this

        stockViewmodel = ViewModelProviders.of(this).get(StockViewmodel::class.java)
//        stockViewmodel.itemWiseData()





        initObserver()
        initRecyclerview()

        return view
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is StockActivity) {
            activity = context
            activity!!.setNotificationListener(this)
        } else {
            throw IllegalArgumentException("The activity must implement NotificationListener")
        }
    }

    private fun initRecyclerview() {

        adapter = ItemWiseProductAdapter()
        val RVLayoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
        var recyclerView:RecyclerView = binding.recyclerview
        recyclerView.layoutManager = RVLayoutManager
        recyclerView.adapter = adapter

    }

    private fun initObserver() {
      stockViewmodel.itemWiseProductBinder.observe(viewLifecycleOwner, Observer {

          Log.e("priyanka","itemWiseProductBinder = = = ${it.size}")
          adapter.submitList(it)
      })

        stockViewmodel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.progressBar.visibility = View.VISIBLE
            }
            else{
                binding.progressBar.visibility = View.GONE
            }

        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ItemWiseFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onStatusReceived(status: String?) {
       Log.e("priyanka","onStatusReceived_itemwise = $status")
        stockViewmodel.stockType.value = status
        stockViewmodel.stockSummary(object :RetrofitCallback{
            override fun onSuccess() {
            }

            override fun onFail(message: String) {
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
            }

            override fun onApiFail(message: Throwable) {
                Toast.makeText(context,message.message, Toast.LENGTH_SHORT).show()
            }

            override fun onApiUnsuccessResponse(responseCode: Int) {
                Toast.makeText(context,responseCode, Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onSearchReceived(search: String?) {
        Log.e("priyanka","onSearchReceived_itemwise = $search")
        stockViewmodel.search_value.value = search
        stockViewmodel.stockSummary(object :RetrofitCallback{
            override fun onSuccess() {
            }

            override fun onFail(message: String) {
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
            }

            override fun onApiFail(message: Throwable) {
                Toast.makeText(context,message.message, Toast.LENGTH_SHORT).show()
            }

            override fun onApiUnsuccessResponse(responseCode: Int) {
                Toast.makeText(context,responseCode, Toast.LENGTH_SHORT).show()
            }

        })
    }


}