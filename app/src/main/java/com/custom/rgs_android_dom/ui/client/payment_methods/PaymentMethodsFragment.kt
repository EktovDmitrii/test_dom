package com.custom.rgs_android_dom.ui.client.payment_methods

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPaymentMethodsBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.client.payment_methods.delete.DeletePaymentMethodFragment
import com.custom.rgs_android_dom.ui.client.payment_methods.error.ErrorDeletePaymentMethodFragment
import com.custom.rgs_android_dom.utils.*

class PaymentMethodsFragment : BaseFragment<PaymentMethodsViewModel, FragmentPaymentMethodsBinding>(R.layout.fragment_payment_methods),
    ErrorDeletePaymentMethodFragment.ErrorDeletePaymentMethodListener {

    private val paymentMethodsAdapter: PaymentMethodsAdapter
        get() = binding.paymentMethodsRecyclerView.adapter as PaymentMethodsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.paymentMethodsRecyclerView.adapter = PaymentMethodsAdapter(){
            viewModel.onDeleteClick(it)
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.editImageView.setOnDebouncedClickListener {
            viewModel.onEditClick()
        }

        binding.confirmImageView.setOnDebouncedClickListener {
            viewModel.onConfirmClick()
        }

        subscribe(viewModel.paymentMethodsObserver){cards->
            binding.paymentMethodsRecyclerView.visible()
            cards?.let {
                paymentMethodsAdapter.setItems(it)
            }
        }

        subscribe(viewModel.isInEditModeObserver){
            binding.editImageView.goneIf(it)
            binding.confirmImageView.visibleIf(it)
            paymentMethodsAdapter.setIsInEditMode(it)
        }

        subscribe(viewModel.noPaymentMethodsObserver){
            binding.paymentMethodsRecyclerView.goneIf(it)
            binding.noPaymentMethodsLinearLayout.visibleIf(it)

            binding.confirmImageView.goneIf(it)
            binding.editImageView.goneIf(it)
        }

        subscribe(viewModel.showDeleteFragmentObserver){
            val deleteFragment = DeletePaymentMethodFragment.newInstance(it)
            deleteFragment.show(childFragmentManager, deleteFragment.TAG)
        }

        subscribe(viewModel.paymentMethodDeletedObserver){
            notification(TranslationInteractor.getTranslation("app.client.payment_methods.card_deleted"))
        }
    }

    override fun onLoading() {
        super.onLoading()
        binding.contentRelativeLayout.gone()
        binding.loadingProgressBar.visible()
    }

    override fun onContent() {
        super.onContent()
        binding.contentRelativeLayout.visible()
        binding.loadingProgressBar.gone()
    }

    override fun contactMasterOnline() {
        viewModel.contactMasterOnline()
    }

}