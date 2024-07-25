package com.example.websocketclientsample.mvvm.payment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.websocketclientsample.R
import com.example.websocketclientsample.databinding.LvItemPaymentMethodBinding
import com.example.websocketclientsample.interfaces.CommonInterfaceClickEvent
import com.example.websocketclientsample.mvvm.payment.model.PaymentDataModel

class PaymentMethodAdapter() : RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder>() {

    var arrClientList: ArrayList<PaymentDataModel> = ArrayList()
    var onClickEvent: CommonInterfaceClickEvent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: LvItemPaymentMethodBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lv_item_payment_method,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val a = arrClientList[position]

        holder.binding.txtName.text = a.name ?: ""
        holder.binding.ivPayment.setImageResource(a.image ?: 0)

        holder.binding.rootLayout.setOnClickListener {
            onClickEvent?.onItemClick("itemClicked", position)
        }

    }

    override fun getItemCount(): Int {
        return arrClientList.size
    }

    class MyViewHolder(var binding: LvItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<PaymentDataModel>) {
        if (data.isNullOrEmpty()) {
            arrClientList = ArrayList()
        }
        arrClientList = data
        notifyDataSetChanged()
    }
}