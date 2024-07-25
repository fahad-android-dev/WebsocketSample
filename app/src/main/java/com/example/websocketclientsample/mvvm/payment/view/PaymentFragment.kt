package com.example.websocketclientsample.mvvm.payment.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.websocketclientsample.R
import com.example.websocketclientsample.databinding.FragmentPaymentBinding
import com.example.websocketclientsample.helper.BaseFragment
import com.example.websocketclientsample.helper.Constants
import com.example.websocketclientsample.helper.Extensions
import com.example.websocketclientsample.helper.Extensions.getSerializableViaArgument
import com.example.websocketclientsample.interfaces.CommonInterfaceClickEvent
import com.example.websocketclientsample.mvvm.main.view.MainActivity
import com.example.websocketclientsample.mvvm.payment.adapter.PaymentMethodAdapter
import com.example.websocketclientsample.mvvm.payment.model.PaymentDataModel
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class ProductObj(
    var price : String ?= ""
):java.io.Serializable

class PaymentFragment : BaseFragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentPaymentBinding
    private var adapter = PaymentMethodAdapter()
    private var arrListPayments = ArrayList<PaymentDataModel>()
    var amount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPaymentMethods.adapter = adapter

        initializeFields()
        initializeToolbar()
        setData()
    }

    private fun initializeToolbar() {
        setUpToolbar(
            binding.layoutToolbar,
            title = getString(R.string.payment),
            isBackArrow = true,
            navController = findNavController(),
            toolbarClickListener = object : CommonInterfaceClickEvent {
                override fun onToolBarListener(type: String) {
                    when (type) {
                        Constants.TOOLBAR_ICON_ONE -> {

                        }
                    }
                }
            }
        )
    }

    private fun initializeFields(){
        if (arguments?.containsKey("productObj") == true) {
            val model = arguments?.getSerializableViaArgument("productObj", ProductObj::class.java)
            amount = model?.price ?: ""
        }
    }

    private fun setData() {
        arrListPayments.clear()
        arrListPayments.add(PaymentDataModel(name = "Paypal", image = R.drawable.ic_paypal))
        arrListPayments.add(PaymentDataModel(name = "Visa", image = R.drawable.ic_visa))
        arrListPayments.add(PaymentDataModel(name = "Mada", image = R.drawable.ic_mada))
        arrListPayments.add(PaymentDataModel(name = "Apple Pay", image = R.drawable.ic_apple_pay))

        adapter.onClickEvent = object : CommonInterfaceClickEvent {
            override fun onItemClick(type: String, position: Int) {
                if (type == "itemClicked") {
                    println("here is item clicked")
                    sendMessage()

                }
            }
        }

        adapter.setData(arrListPayments)
    }

    override fun onResume() {
        super.onResume()
        Extensions.handler(1000){
            println("here is model ${mActivity.viewModel.confirmationDataModel}")
            if (mActivity.viewModel.confirmationDataModel != null){
                findNavController().navigate(R.id.action_to_navigation_confirmation)
            }
        }

    }


    private fun sendMessage() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("code", "")
        jsonObject.addProperty("amount", amount)

        mActivity.viewModel.webSocketClient?.sendMessage(jsonObject)
        Log.d("FragmentTwo", "Sending message: $jsonObject")
    }

}


