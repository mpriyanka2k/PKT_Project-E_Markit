package com.pkt.emarkit.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.FragmentPartyWiseBinding
import com.pkt.emarkit.ui.activities.StockActivity
import com.pkt.emarkit.ui.adapter.PartyWiseProductAdapter
import com.pkt.emarkit.ui.interfaces.PassDatafromActivityToFragment
import com.pkt.emarkit.ui.interfaces.PassDatafromActivitytoFragment1
import com.pkt.emarkit.ui.interfaces.RetrofitCallback
import com.pkt.emarkit.ui.viewmodels.StockViewmodel

class PartyWiseFragment : Fragment(), PassDatafromActivitytoFragment1 {

    lateinit var binding:FragmentPartyWiseBinding
    lateinit var adapter:PartyWiseProductAdapter
    lateinit var stockViewmodel: StockViewmodel
    private var activity: StockActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_party_wise,container,false)
        var view = binding.root
        binding.lifecycleOwner = this

        stockViewmodel = ViewModelProviders.of(this).get(StockViewmodel::class.java)

//        stockViewmodel.partiWiseData()



        initRecyclerview()
        initObserver()

        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is StockActivity) {
            activity = context
            activity!!.setNotificationListener1(this)
        } else {
            throw IllegalArgumentException("The activity must implement NotificationListener")
        }
    }

    private fun initObserver() {

        stockViewmodel.partyWiseProductBinder.observe(viewLifecycleOwner, Observer {
            Log.e("priyanka","partyWiseProductBinder = = = ${it.size}")
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

    private fun initRecyclerview() {
      adapter = PartyWiseProductAdapter()
      var RVLayoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
      var recyclerView = binding.recyclerview
      recyclerView.layoutManager = RVLayoutManager
      recyclerView.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance() = PartyWiseFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onStatusReceived(status: String?) {
        Log.e("priyanka","onStatusReceived_partywise = $status")
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
        Log.e("priyanka","onSearchReceived_partywise = $search")
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