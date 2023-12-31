package com.custom.rgs_android_dom.ui.policies.policy

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentPolicyBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PolicyFragment : BaseFragment<PolicyViewModel, FragmentPolicyBinding>(R.layout.fragment_policy) {

    companion object {

        const val KEY_CONTRACT_ID = "KEY_CONTRACT_ID"
        const val KEY_IS_ACTIVE = "KEY_IS_ACTIVE"

        fun newInstance(contractId: String, isActive: Boolean) = PolicyFragment().args {
            putString(KEY_CONTRACT_ID, contractId)
            putBoolean(KEY_IS_ACTIVE, isActive)
        }

    }

    private val inclusionAdapter: PolicyProductInclusionAdapter
        get() = binding.includes.includesRecycler.adapter as PolicyProductInclusionAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backImageView.setOnDebouncedClickListener {
            requireActivity().onBackPressed()
            //viewModel.onBackClick()
        }
        // TODO Add product verion id
        subscribe(viewModel.productObserver) {
            binding.contentLinearLayout.visible()
            binding.loadingProgressBar.gone()
            binding.includes.includesRecycler.adapter = PolicyProductInclusionAdapter(
                onServiceClick = { serviceShortModel ->
                    viewModel.onServiceClick(
                        serviceShortModel,
                        ProductLauncher(it.productId, "")
                    )
                },
                onOrderClick = { serviceShortModel ->
                    viewModel.onServiceOrderClick(
                        serviceShortModel,
                        ProductLauncher(it.productId, "")
                    )
                }
            )
            if (it.logo != null) {
                GlideApp.with(requireContext())
                    .load(GlideUrlProvider.makeHeadersGlideUrl(it.logo))
                    .transform(RoundedCorners(15.dp(requireContext())))
                    .into(binding.productImageView)
            }
            inclusionAdapter.setStatus(it.isActive)
            inclusionAdapter.setItems(it.includedProducts ?: listOf())
            binding.titleTextView.text = it.productTitle
            binding.startDateTextView.text = it.startsAt?.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
            binding.expirationDateTextView.text = it.expiresAt?.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
            binding.policyDataTextView.text = it.policySeriesAndNumber
            binding.addressTitleTextView.goneIf(it.address == null)
            binding.addressTextView.goneIf(it.address == null)
            binding.addressTextView.text = it.address ?: ""
            binding.descriptionTextView.text = it.productDescription
            binding.clientNameTextView.text = it.clientName
        }
        subscribe(viewModel.isActiveObserver) {
            if (!it) {
                binding.archiveTextView.visible()
                binding.titleTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary500
                    )
                )
                binding.descriptionTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary500
                    )
                )
                binding.policyDataTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary500
                    )
                )
                binding.clientNameTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary500
                    )
                )
                binding.startDateTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary500
                    )
                )
                binding.expirationDateTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary500
                    )
                )
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(KEY_CONTRACT_ID, null),
            requireArguments().getBoolean(KEY_IS_ACTIVE, true),
        )
    }

    override fun onContent() {
        super.onContent()
        binding.contentLinearLayout.visible()
        binding.loadingProgressBar.gone()
    }

    override fun onLoading() {
        super.onLoading()
        binding.contentLinearLayout.gone()
        binding.loadingProgressBar.visible()
    }

}