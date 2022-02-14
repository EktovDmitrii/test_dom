package com.custom.rgs_android_dom.data.repositories.policies

import android.annotation.SuppressLint
import android.util.Log
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.data.network.requests.BindPolicyRequest
import com.custom.rgs_android_dom.domain.policies.models.BoundPolicyDialogModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyShortModel
import com.custom.rgs_android_dom.domain.repositories.PoliciesRepository
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantViewState
import com.custom.rgs_android_dom.utils.tryParseDate
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class PoliciesRepositoryImpl(private val api: MSDApi) : PoliciesRepository {

    private var policyDialogSubject = PublishSubject.create<PolicyDialogModel>()
    private val promptSaveSubject = BehaviorSubject.create<Boolean>()

    private var request: BindPolicyRequest = BindPolicyRequest()


    override fun onInsurantDataChange(data: InsurantViewState) {
        request = request.copy(
            contractClientBirthDate = "${data.birthday.tryParseDate()}T00:00:00.000Z",
            contractClientFirstName = data.firstName,
            contractClientLastName = data.lastName,
            contractClientMiddleName = data.middleName
        )
    }

    override fun onPolicyChange(policy: String) {
        request = request.copy(
            contractSerial = policy.substringBefore(" "),
            contractNumber = policy.substringAfter(" ")
        )
    }

    @SuppressLint("CheckResult")
    override fun bindPolicy(): Single<Any> {
        Log.d("Syrgashev", "request: $request")
        return api.bindPolicy(request).map { bindPolicyResponse ->
            PolicyDialogModel(
                bound = BoundPolicyDialogModel(
                    startsAt = bindPolicyResponse.contract?.startDate,
                    endsAt = bindPolicyResponse.contract?.endDate
                )
            )
        }
    }

    override fun getPolicyDialogSubject(): Observable<PolicyDialogModel> {
        return policyDialogSubject.hide()
    }

    override fun getPromptSaveSubject(): Observable<Boolean> {
        return promptSaveSubject.hide()
    }

    override fun newDialog(policyDialogModel: PolicyDialogModel) {
        policyDialogSubject.onNext(policyDialogModel)
    }

    override fun promptSavePersonalData(save: Boolean) {
        promptSaveSubject.onNext(save)
    }

    override fun getRequest(): BindPolicyRequest {
        return request
    }

    override fun getPoliciesSingle(): Single<List<PolicyShortModel>> {

        val contractIds = api.getPolicyContracts().map {
            if (!it.contracts.isNullOrEmpty()){
                it.contracts.joinToString(",") { it.id }
            } else ""
        }.blockingGet()

        return if (!contractIds.isNullOrEmpty()){
            api.getClientProducts(50, 0, contractIds)
                .map {
                    if (it.clientProducts != null) {
                        it.clientProducts.map { ClientMapper.responseToPolicyShort(it) }
                    } else {
                        listOf()
                    }
                }
        } else { Single.just(listOf()) }
    }

    override fun getPolicySingle(contractId: String): Single<PolicyModel> {
        val clientProducts  = api.getClientProducts(1, 0, contractId)
        val contracts = api.getPolicyContracts()
        return Single.zip(clientProducts,contracts){clientProducts,contracts ->
            ClientMapper.responseToPolicy(clientProducts.clientProducts?.get(0),
                contracts.contracts?.first { it.id == contractId })
        }


        /*api.getClientProducts(1, 0, contractId)
            .map {
                if (it.clientProducts != null) {
                     CatalogMapper.responseToClientProduct(it.clientProducts.last())
                } else {
                    null
                }
            }*/
    }
}