package com.custom.rgs_android_dom.ui.chats

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentChatsBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf
import com.custom.rgs_android_dom.views.NavigationScope


class ChatsFragment : BaseBottomSheetFragment<ChatsViewModel, FragmentChatsBinding>() {

    override val TAG: String = "CHATS_FRAGMENT"

    private val activeCasesAdapter: CasesAdapter
        get() = binding.activeCasesRecyclerView.adapter as CasesAdapter

    private val archivedCasesAdapter: CasesAdapter
        get() = binding.archivedCasesRecyclerView.adapter as CasesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activeCasesRecyclerView.adapter = CasesAdapter(){
            viewModel.onCaseClick(it)
        }

        binding.archivedCasesRecyclerView.adapter = CasesAdapter(){
            viewModel.onCaseClick(it)
        }

        subscribe(viewModel.casesObserver){clientCases->

            binding.archivedDividerConstraintLayout.visibleIf(clientCases.archivedCases.isNotEmpty())
            binding.archivedCasesRecyclerView.visibleIf(clientCases.archivedCases.isNotEmpty())

            activeCasesAdapter.setItems(clientCases.activeCases)
            archivedCasesAdapter.setItems(clientCases.archivedCases)
        }

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_CHATS
    }

}
