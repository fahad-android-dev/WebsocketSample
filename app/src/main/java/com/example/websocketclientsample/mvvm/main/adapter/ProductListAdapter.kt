package com.example.websocketclientsample.mvvm.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.websocketclientsample.R
import com.example.websocketclientsample.databinding.LvItemProductListBinding
import com.example.websocketclientsample.interfaces.CommonInterfaceClickEvent
import com.example.websocketclientsample.mvvm.main.model.ProductListDataModel

class ProductListAdapter() : RecyclerView.Adapter<ProductListAdapter.MyViewHolder>() {

    var arrClientList: ArrayList<ProductListDataModel> = ArrayList()
    var onClickEvent: CommonInterfaceClickEvent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: LvItemProductListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lv_item_product_list,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val a = arrClientList[position]

        holder.binding.txtName.text = a.name ?: ""
        holder.binding.txtPrice.text = "SAR ${a.price ?: ""}"
        holder.binding.ivMain.setImageResource(a.image ?: 0)

        holder.binding.rootLayout.setOnClickListener {
            onClickEvent?.onItemClick("itemClicked", position)
        }

    }

    override fun getItemCount(): Int {
        return arrClientList.size
    }

    class MyViewHolder(var binding: LvItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<ProductListDataModel>) {
        if (data.isNullOrEmpty()) {
            arrClientList = ArrayList()
        }
        arrClientList = data
        notifyDataSetChanged()
    }
}