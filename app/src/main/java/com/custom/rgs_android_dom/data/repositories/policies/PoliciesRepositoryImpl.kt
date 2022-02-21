package com.custom.rgs_android_dom.data.repositories.policies

import android.annotation.SuppressLint
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.data.network.requests.BindPolicyRequest
import com.custom.rgs_android_dom.data.network.responses.ProductServicesResponse
import com.custom.rgs_android_dom.data.network.responses.PropertyItemResponse
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

        val policyContractSingle = api.getPolicyContracts()

        val contractIds = policyContractSingle.map {
            if (!it.contracts.isNullOrEmpty()){
                it.contracts.joinToString(",") { it.id }
            } else ""
        }.blockingGet()

        val contractsSingle = api.getPolicyContracts()

        val clientProductsSingle = api.getClientProducts(50, 0, contractIds)

        return if (!contractIds.isNullOrEmpty()){
            Single.zip(contractsSingle, clientProductsSingle) { contracts, clientProducts ->
                if (clientProducts.clientProducts != null){
                    clientProducts.clientProducts.map { ClientMapper.responseToPolicyShort(it, contracts) }
                } else {
                    listOf()
                }

            }
        } else { Single.just(listOf()) }
    }

    override fun getPolicySingle(contractId: String): Single<PolicyModel> {
        val clientProductsSingle  = api.getClientProducts(1, 0, contractId)
        val contractsSingle = api.getPolicyContracts()

        return Single.zip(clientProductsSingle,contractsSingle) { clientProducts, contracts ->
            val product = clientProducts.clientProducts?.get(0)
            val objectId = product?.objectId
            var propertyItemResponse: PropertyItemResponse? = null

            if (objectId != null) {
                propertyItemResponse = api.getAllProperty().blockingGet().objects?.first { it.id == objectId }
            }

            var productServicesResponse: ProductServicesResponse? = null
            if (product?.productId != null){
                productServicesResponse = api.getProductServicesResponse(product.productId, 100, 0).blockingGet()
            }

            ClientMapper.responseToPolicy(
                product,
                contracts.contracts?.first { it.id == contractId },
                propertyItemResponse,
                productServicesResponse
            )
        }
    }

    override fun restoreViewState(viewState: InsurantViewState) {
        request = request.copy(
            contractClientFirstName = viewState.firstName,
            contractClientMiddleName = viewState.middleName,
            contractClientLastName = viewState.lastName)
    }
}