package com.example.websocketclientsample.mvvm.main.view

import AppNavigation.navigateToMain
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.websocketclientsample.R
import com.example.websocketclientsample.databinding.FragmentHomeBinding
import com.example.websocketclientsample.helper.AlertDialogInterface
import com.example.websocketclientsample.helper.BaseFragment
import com.example.websocketclientsample.helper.Constants
import com.example.websocketclientsample.helper.Dialogs
import com.example.websocketclientsample.helper.Dialogs.showCustomAlert
import com.example.websocketclientsample.helper.LocaleHelper
import com.example.websocketclientsample.helper.PrefUtils.getServerAddress
import com.example.websocketclientsample.helper.PrefUtils.saveServerAddress
import com.example.websocketclientsample.helper.helper_model.ServerAddressModel
import com.example.websocketclientsample.interfaces.CommonInterfaceClickEvent
import com.example.websocketclientsample.mvvm.main.adapter.ProductListAdapter
import com.example.websocketclientsample.mvvm.main.model.ProductListDataModel
import com.example.websocketclientsample.mvvm.payment.view.ProductObj


class HomeFragment : BaseFragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentHomeBinding
    private var productListAdapter = ProductListAdapter()
    private var arrListProducts = ArrayList<ProductListDataModel>()

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
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvProducts.adapter = productListAdapter

        initializeToolbar()
        setData()
    }


    private fun initializeToolbar() {
        setUpToolbar(
            binding.layoutToolbar,
            title = getString(R.string.food_ordering),
            isBackArrow = false,
            navController = findNavController(),
            iconTwo = R.drawable.ic_language,
            toolbarClickListener = object : CommonInterfaceClickEvent {
                override fun onToolBarListener(type: String) {
                    when (type) {
                        Constants.TOOLBAR_ICON_ONE -> {
                            Dialogs.showPairingDialog(
                                activity = mActivity,
                                alertDialogInterface = object : AlertDialogInterface {
                                    override fun onConnectionConfirm(
                                        ipAddress: String,
                                        port: String,
                                        code: String
                                    ) {
                                        mActivity.saveServerAddress(
                                            ServerAddressModel(
                                                ipAddress = ipAddress, port = port, code = code

                                            )
                                        )
                                        mActivity.viewModel.connectWebSocket(ipAddress, port, code) // Pass necessary
                                    }
                                }
                            )
                        }
                        Constants.TOOLBAR_ICON_TWO -> {
                            showChangeLanguageAlert()
                        }
                    }
                }
            }
        )
    }

    private fun showChangeLanguageAlert() {
        showCustomAlert(
            activity = mActivity,
            title = getString(R.string.alert_title_lang),
            msg = resources.getString(R.string.alert_language),
            yesBtn = resources.getString(R.string.yes_lang),
            noBtn = resources.getString(R.string.no_lang),
            alertDialogInterface = object : AlertDialogInterface {
                override fun onYesClick() {
                    LocaleHelper.changeLanguage(mActivity)
                    mActivity.navigateToMain{}
                }

                override fun onNoClick() {}
            })
    }


    private fun setData() {
        arrListProducts.clear()
        arrListProducts.add(ProductListDataModel(name = "Chicken Grilled Burger", price = "1.00", image = R.drawable.ic_burger_two))
        arrListProducts.add(ProductListDataModel(name = "Chicken Burger", price = "51.12", image = R.drawable.ic_burger_two))
        arrListProducts.add(ProductListDataModel(name = "Strawberry fudge", price = "51.17", image = R.drawable.ic_fudge))
        arrListProducts.add(ProductListDataModel(name = "Chocolate fudge", price = "51.16", image = R.drawable.ic_fudge))
        arrListProducts.add(ProductListDataModel(name = "Chicken Grilled Burger", price = "51.90", image = R.drawable.ic_burger_two))
        arrListProducts.add(ProductListDataModel(name = "Chicken Grilled Burger", price = "51.99", image = R.drawable.ic_burger_two))
        arrListProducts.add(ProductListDataModel(name = "Chicken Grilled Burger", price = "50.00", image = R.drawable.ic_burger_two))

        productListAdapter.onClickEvent = object : CommonInterfaceClickEvent {
            override fun onItemClick(type: String, position: Int) {
                if (type == "itemClicked"){
                    if (mActivity.viewModel.isConnected) {
                        mActivity.viewModel.connectWebSocket(mActivity.getServerAddress()?.ipAddress ?: "", mActivity.getServerAddress()?.port ?: "", mActivity.getServerAddress()?.code ?: "") // Pass necessary
                        val bundle = Bundle()
                        bundle.putSerializable("productObj", ProductObj(
                            price = arrListProducts[position].price
                        ))
                        findNavController().navigate(R.id.action_to_navigation_payment,bundle)

                    }
                }
            }
        }

        productListAdapter.setData(arrListProducts)
    }

    override fun onResume() {
        super.onResume()
        mActivity.viewModel.confirmationDataModel = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity.viewModel.webSocketClient?.disconnect()
    }



}