package com.custom.rgs_android_dom.ui.client

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentClientBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration
import com.custom.rgs_android_dom.views.NavigationScope
import com.yandex.metrica.YandexMetrica

class ClientFragment() : BaseBottomSheetFragment<ClientViewModel, FragmentClientBinding>() {

    companion object {
        private const val ACTION_OPEN_MED_APP = "com.custom.my_service_android_client.action.auth"
        private const val MED_APP_PROD_ID = "com.custom.my_service_android_client"
    }

    override val TAG: String = "CLIENT_FRAGMENT"

    private val propertyItemsAdapter: PropertyItemsAdapter
        get() = binding.propertyItemsRecycler.adapter as PropertyItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.secondary600)

        binding.propertyItemsRecycler.adapter = PropertyItemsAdapter(
            onAddPropertyClick = {
                YandexMetrica.reportEvent("profile_object_add_start")

                viewModel.onAddPropertyClick()
            },
            onPropertyItemClick = {
                YandexMetrica.reportEvent("profile_object_view")

                viewModel.onPropertyItemClick(it)
            }
        )

        binding.propertyItemsRecycler.addItemDecoration(
            HorizontalItemDecoration(
                gap = 16.dp(requireContext())
            )
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
        }

        binding.logoutRelativeLayout.setOnDebouncedClickListener {
            viewModel.onLogoutClick()

            YandexMetrica.reportEvent("session_start_offline")
        }

        binding.personalDataRelativeLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_menu", "{\"profile_menu_item\":\"Личные данные\"}")

            viewModel.onPersonalDataClick()
        }


        binding.agentInfoLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_menu", "{\"profile_menu_item\":\"Данные об агенте\"}")

            viewModel.onAgentInfoClick()
        }

        binding.aboutAppLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_menu", "{\"profile_menu_item\":\"О приложении\"}")

            viewModel.onAboutAppClick()
        }

        binding.ordersHistoryLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_menu", "{\"profile_menu_item\":\"История заказов\"}")

            viewModel.onOrdersHistoryClick()
        }

        binding.openMedAppLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_menu", "{\"profile_menu_item\":\"Перейти в Мой_Сервис Мед\"}")

            viewModel.onOpenMedAppClick()
        }

        binding.policiesLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_menu", "{\"profile_menu_item\":\"Полисы\"}")

            viewModel.onPoliciesClick()
        }

        binding.paymentMethodsLinearLayout.setOnDebouncedClickListener {
            viewModel.onPaymentMethodsClick()
        }

        subscribe(viewModel.propertyItemsObserver) { propertyItems ->
            propertyItemsAdapter.setItems(propertyItems)
        }

        subscribe(viewModel.clientShortViewStateObserver) { state ->
            binding.phoneTextView.text = state.phone

            if (state.firstName.isEmpty() && state.lastName.isEmpty()) {
                binding.nameTextView.text = "Добавьте ваше имя"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.primary500))
            } else {
                binding.nameTextView.text = "${state.lastName} ${state.firstName}"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.secondary900))
            }

            if (state.hasAgentInfo){
                binding.agentInfoTextView.text = "Данные об агенте"
            } else {
                binding.agentInfoTextView.text = "Я знаю код агента"
            }

            if (state.avatar.isEmpty()){
                binding.avatarImageView.setImageResource(R.drawable.ic_avatar)
            } else {
                GlideApp.with(requireContext())
                    .load(GlideUrlProvider.makeHeadersGlideUrl(state.avatar))
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_avatar)
                    .into(binding.avatarImageView)
            }
        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }

        subscribe(viewModel.navigateToMedAppObserver) {
            openMedApp()
        }

        subscribe(viewModel.swipeRefreshingObserver){
            binding.swipeRefreshLayout.isRefreshing = it
        }

        subscribe(viewModel.notificationObserver){
            notification(it)
        }
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_PROFILE
    }

    private fun openMedApp(){
        try {
            val intent = Intent().apply {
                action = ACTION_OPEN_MED_APP
                `package` = BuildConfig.RME_APP_ID
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } catch (e: Exception){
            navigateToGooglePlay()
        }
    }

    private fun navigateToGooglePlay(){
        val uri = Uri.parse("market://details?id=$MED_APP_PROD_ID")
        var intent = Intent(Intent.ACTION_VIEW, uri)

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$MED_APP_PROD_ID"))
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }
    }
}
