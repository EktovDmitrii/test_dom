package com.custom.rgs_android_dom.data.repositories.policies

import android.annotation.SuppressLint
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.CatalogMapper
import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.data.network.requests.BindPolicyRequest
import com.custom.rgs_android_dom.data.network.responses.ProductServicesResponse
import com.custom.rgs_android_dom.data.network.responses.PropertyItemResponse
import com.custom.rgs_android_dom.domain.policies.models.*
import com.custom.rgs_android_dom.domain.repositories.PoliciesRepository
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantViewState
import com.custom.rgs_android_dom.utils.tryParseDate
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class PoliciesRepositoryImpl(private val api: MSDApi) : PoliciesRepository {

    private var policyModel: PolicyDialogModel? = null

    private var policyDialogSubject = PublishSubject.create<PolicyDialogModel>()
    private val promptSaveSubject = BehaviorSubject.create<Boolean>()

    private var request: BindPolicyRequest = BindPolicyRequest()


    override fun onPolicyChange(policy: String) {
        request = request.copy(
            contractSerial = policy.substringBefore(" "),
            contractNumber = policy.substringAfter(" ")
        )
    }

    @SuppressLint("CheckResult")
    override fun bindPolicy(insurantViewState: InsurantViewState): Single<Any> {
        request = request.copy(
            contractClientBirthDate = "${insurantViewState.birthday.tryParseDate()}T00:00:00.000Z",
            contractClientFirstName = insurantViewState.firstName,
            contractClientLastName = insurantViewState.lastName,
            contractClientMiddleName = insurantViewState.middleName
        )

        return api.bindPolicy(request).map { bindPolicyResponse ->
            val contractId = bindPolicyResponse.contract?.id
            val products = api.getClientProducts(size = 50, index = 0, businessLines = BuildConfig.BUSINESS_LINE, contractIds = contractId, status = "active").blockingGet()

            PolicyDialogModel(
                bound = BoundPolicyDialogModel(
                    clientProductIds = bindPolicyResponse.clientProductIds ?: listOf(),
                    contractId = bindPolicyResponse.contract?.id ?: "",
                    startsAt = bindPolicyResponse.contract?.startDate,
                    endsAt = bindPolicyResponse.contract?.endDate,
                    name = products.clientProducts?.get(0)?.productName ?: "",
                    logo = "${BuildConfig.BASE_URL}/api/store/${products.clientProducts?.get(0)?.productIcon}",
                    contractClientBirthDate = "${insurantViewState.birthday.tryParseDate()}T00:00:00.000Z",
                    contractClientFirstName = insurantViewState.firstName,
                    contractClientLastName = insurantViewState.lastName,
                    contractClientMiddleName = insurantViewState.middleName
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
        policyModel = policyDialogModel
        policyDialogSubject.onNext(policyDialogModel)
    }

    override fun promptSavePersonalData(save: Boolean) {
        promptSaveSubject.onNext(save)
    }

    override fun getPoliciesSingle(): Single<List<PolicyShortModel>> {

        val policyContractSingle = api.getPolicyContracts()

        val contractIds = policyContractSingle.map {
            if (!it.contracts.isNullOrEmpty()){
                it.contracts.joinToString(",") { it.id }
            } else ""
        }.blockingGet()

        val contractsSingle = api.getPolicyContracts()

        val clientProductsSingle = api.getClientProducts(size = 50, index = 0, businessLines = BuildConfig.BUSINESS_LINE, contractIds = contractIds)

        return if (!contractIds.isNullOrEmpty()){
            Single.zip(contractsSingle, clientProductsSingle) { contracts, clientProducts ->
                if (clientProducts.clientProducts != null){
                    clientProducts.clientProducts.map { ClientMapper.responseToPolicyShort(it, contracts) }.sortedByDescending { it.expiresAt }
                } else {
                    emptyList()
                }

            }
        } else { Single.just(emptyList()) }
    }

    override fun getPolicySingle(contractId: String): Single<PolicyModel> {
        val clientProductsSingle  = api.getClientProducts(size = 1, index = 0, businessLines = BuildConfig.BUSINESS_LINE, contractIds = contractId)
        val contractsSingle = api.getPolicyContracts()
        val availableServicesSingle = api.getAvailableServices(size = 100, index = 0, withBalance = true, businessLine = BuildConfig.BUSINESS_LINE, contractId = contractId).map { response->
            CatalogMapper.responseToBalanceServices(response)
        }

        return Single.zip(clientProductsSingle, contractsSingle, availableServicesSingle) { clientProducts, contracts, availableServices ->
            val clientProductResponse = clientProducts.clientProducts?.get(0)
            val objectId = clientProductResponse?.objectId
            var propertyItemResponse: PropertyItemResponse? = null

            if (objectId != null) {
                propertyItemResponse = api.getAllProperty().blockingGet().objects?.firstOrNull { it.id == objectId }
            }

            var productServicesResponse: ProductServicesResponse? = null
            if (clientProductResponse?.productId != null){
                productServicesResponse = api.getProductServicesByVersion(clientProductResponse.productVersionId ?: "", 100, 0).blockingGet()
            }

            ClientMapper.responseToPolicy(
                clientProductResponse,
                contracts.contracts?.firstOrNull { it.id == contractId },
                propertyItemResponse,
                productServicesResponse,
                availableServices
            )
        }
    }

    override fun getPolicyDialogModel(): PolicyDialogModel? {
        return policyModel
    }

}