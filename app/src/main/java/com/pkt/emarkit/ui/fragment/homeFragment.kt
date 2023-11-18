package com.pkt.emarkit.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.pkt.emarkit.R
import com.pkt.emarkit.databinding.FragmentHomeBinding
import com.pkt.emarkit.ui.activities.PendingActivity
import com.pkt.emarkit.ui.viewmodels.HomeViewmodel

class homeFragment : Fragment() {

    lateinit var homeViewmodel: HomeViewmodel
    lateinit var binding:FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
       var view = binding.root
       binding.lifecycleOwner = this
       homeViewmodel = ViewModelProviders.of(this).get(HomeViewmodel::class.java)
       binding.viewBinder = homeViewmodel.dashboardBinder

        initObserver()
        homeViewmodel.getDashboardData()

        //open Pending
        binding.linearPending.setOnClickListener {
            var intent = Intent(context,PendingActivity::class.java)
            intent.putExtra("Delivery_type","Pending")
            startActivity(intent)
        }

        //open Completed
        binding.linearCompleted.setOnClickListener {
            var intent = Intent(context,PendingActivity::class.java)
            intent.putExtra("Delivery_type","Completed")
            startActivity(intent)
        }

        //open cancelled
        binding.linearCancelled.setOnClickListener {
            var intent = Intent(context,PendingActivity::class.java)
            intent.putExtra("Delivery_type","Cancelled")
            startActivity(intent)
        }

        return view


    }

    private fun initObserver() {
        homeViewmodel.loading.observe(viewLifecycleOwner, Observer {
            if (it){
             binding.progressbar.visibility = View.VISIBLE
            }
            else{
             binding.progressbar.visibility = View.GONE
            }
        })
    }

    companion object {
        fun newInstance() =
            homeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}