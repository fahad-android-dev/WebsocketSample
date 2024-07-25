package com.example.websocketclientsample.mvvm.comfirmation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.websocketclientsample.R
import com.example.websocketclientsample.databinding.FragmentConfirmationBinding
import com.example.websocketclientsample.helper.AlertDialogInterface
import com.example.websocketclientsample.helper.BaseFragment
import com.example.websocketclientsample.helper.Constants
import com.example.websocketclientsample.helper.Dialogs
import com.example.websocketclientsample.helper.Extensions
import com.example.websocketclientsample.helper.PrefUtils.saveServerAddress
import com.example.websocketclientsample.helper.helper_model.ServerAddressModel
import com.example.websocketclientsample.interfaces.CommonInterfaceClickEvent
import com.example.websocketclientsample.mvvm.main.view.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class ConfirmationFragment : BaseFragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding : FragmentConfirmationBinding

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
            R.layout.fragment_confirmation,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
    }

    private fun setData(){
        val model = mActivity.viewModel.confirmationDataModel
        val transactionData = model?.getAsJsonObject("transactionData")
        val receiptsArray = transactionData?.getAsJsonArray("receipts")
        val firstReceipt = receiptsArray?.get(0)?.asJsonObject
        val actionCode = firstReceipt?.get("action_code")?.asString
        println("here is code ${actionCode}")
        when (actionCode) {
            "112" -> {
                showFailureData()
            }
            "116" -> {
                showFailureData()
            }
            "117" -> {
                showFailureData()
            }
            "190" -> {
                showFailureData()
            }
            "196" -> {
                showFailureData()
            }
            "191" -> {
                showSuccessData()
            }
            "000" -> {
                showSuccessData()
            }
            else -> {
                showFailureData()
            }
        }
    }

    private fun showFailureData(){
        binding.ivConfirmation.setImageResource(R.drawable.ic_failure)
        binding.txtTitle.text = getString(R.string.your_payment_was_unsuccessful)
        Extensions.handler(5000){
            findNavController().popBackStack(R.id.navigation_payment,true)
        }
    }

    private fun showSuccessData(){
        binding.ivConfirmation.setImageResource(R.drawable.ic_success)
        binding.txtTitle.text = getString(R.string.your_payment_was_successful)
        Extensions.handler(5000){
            findNavController().popBackStack(R.id.navigation_payment,true)
        }
    }
}