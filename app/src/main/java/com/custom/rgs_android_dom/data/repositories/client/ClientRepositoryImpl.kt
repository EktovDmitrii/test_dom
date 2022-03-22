package com.custom.rgs_android_dom.data.repositories.client

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.data.network.mappers.GeneralInvoiceMapper
import com.custom.rgs_android_dom.data.network.mappers.OrdersMapper
import com.custom.rgs_android_dom.data.network.requests.DeleteContactsRequest
import com.custom.rgs_android_dom.data.network.requests.UpdateClientRequest
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.client.mappers.AgentMapper
import com.custom.rgs_android_dom.domain.client.models.*
import com.custom.rgs_android_dom.domain.repositories.ClientRepository
import com.custom.rgs_android_dom.utils.PATTERN_DATE_TIME_MILLIS
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import com.custom.rgs_android_dom.utils.formatTo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.joda.time.LocalDateTime

class ClientRepositoryImpl(
    private val api: MSDApi,
    private val clientSharedPreferences: ClientSharedPreferences
) : ClientRepository {

    private val clientUpdatedSubject: PublishSubject<ClientModel> = PublishSubject.create()
    private val editClientRequestedSubject: PublishSubject<Boolean> = PublishSubject.create()
    private val orderCancelledSubject: PublishSubject<Unit> = PublishSubject.create()
    private val editAgentRequestedSubject: PublishSubject<Boolean> = PublishSubject.create()

    override fun updateClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        birthday: LocalDateTime?,
        gender: Gender?,
        phone: String?,
        email: String?,
        avatar: String?
    ): Completable {
        val request = UpdateClientRequest(
            avatar = avatar,
            birthdate = birthday?.formatTo(PATTERN_DATE_TIME_MILLIS),
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            sex = gender?.shortcode,
            phone = phone,
            email = email
        )
        return api.putMyClient(request)
            .flatMapCompletable { response ->
                val client = ClientMapper.responseToClient(response)
                clientSharedPreferences.saveClient(client)
                clientSharedPreferences.getClient()?.let {
                    clientUpdatedSubject.onNext(it)
                }
                Completable.complete()
            }
    }

    override fun getClient(): Single<ClientModel> {
        clientSharedPreferences.getClient()?.let {
            return Single.fromCallable { it }
        }

        return api.getMyClient().map { response ->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            val agent = ClientMapper.responseToAgent(api.getAgent().blockingGet())
            clientSharedPreferences.saveAgent(agent)
            clientUpdatedSubject.onNext(client)
            return@map clientSharedPreferences.getClient()
        }
    }

    override fun loadAndSaveClient(): Completable {
        return api.getMyClient().flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)
            val agentResponse = api.getAgent().blockingGet()
            val agent = ClientMapper.responseToAgent(agentResponse)
            client.agent = agent
            val cachedClient = clientSharedPreferences.getClient()
            if (cachedClient != null && cachedClient != client || cachedClient == null) {
                clientSharedPreferences.saveClient(client)
                clientSharedPreferences.saveAgent(agent)
                clientUpdatedSubject.onNext(client)
            }

            return@flatMapCompletable Completable.complete()
        }
    }

    override fun getClientUpdatedSubject(): PublishSubject<ClientModel> {
        return clientUpdatedSubject
    }

    override fun assignAgent(code: String, phone: String, assignType: String): Completable {
        val request = ClientMapper.agentToRequest(code, phone, assignType)
        return api.assignAgent(request)
            .flatMapCompletable { response ->
                val agent = ClientMapper.responseToAgent(response)
                clientSharedPreferences.saveAgent(agent)
                clientSharedPreferences.getClient()?.let { client ->
                    clientUpdatedSubject.onNext(client)
                }
                Completable.complete()
            }
    }

    override fun postPassport(serial: String, number: String): Completable {
        val postDocumentsRequest = ClientMapper.passportToRequest(serial, number)
        return api.postDocuments(postDocumentsRequest).flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientSharedPreferences.getClient()?.let {
                clientUpdatedSubject.onNext(it)
            }
            Completable.complete()
        }
    }

    override fun updatePassport(id: String, serial: String, number: String): Completable {
        val updateDocumentsRequest = ClientMapper.passportToRequest(id, serial, number)
        return api.updateDocuments(updateDocumentsRequest).flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientSharedPreferences.getClient()?.let {
                clientUpdatedSubject.onNext(it)
            }
            Completable.complete()
        }
    }

    override fun saveSecondPhone(phone: String): Completable {
        val updateContactsRequest = ClientMapper.phoneToRequest(phone.formatPhoneForApi())
        return api.postContacts(updateContactsRequest).flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientSharedPreferences.getClient()?.let {
                clientUpdatedSubject.onNext(it)
            }
            Completable.complete()
        }
    }

    override fun updateSecondPhone(phone: String, id: String): Completable {
        val updateContactsRequest = ClientMapper.phoneToRequest(phone.formatPhoneForApi(), id)
        return api.putContacts(updateContactsRequest).flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientSharedPreferences.getClient()?.let {
                clientUpdatedSubject.onNext(it)
            }
            Completable.complete()
        }
    }

    override fun deleteContacts(ids: ArrayList<String>): Completable {
        val deleteContactsRequest = DeleteContactsRequest(ids = ids)
        return api.deleteContacts(deleteContactsRequest).flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientSharedPreferences.getClient()?.let {
                clientUpdatedSubject.onNext(it)
            }
            Completable.complete()
        }
    }

    override fun requestEditAgent(): Completable {
        return api.requestEditAgent().doOnComplete {
            editAgentRequestedSubject.onNext(true)
        }
    }

    override fun getEditAgentRequestedSubject(): PublishSubject<Boolean> {
        return editAgentRequestedSubject
    }

    override fun getRequestEditAgentTasks(): Single<List<RequestEditAgentTaskModel>> {
        return api.getRequestEditAgentTasks().map {
            AgentMapper.responseToRequestEditAgentTaskModel(it)
        }
    }

    override fun getUserDetails(): Single<UserDetailsModel> {
        return api.getUser().map {
            ClientMapper.responseToUserDetails(it)
        }
    }

    override fun getEditClientRequestedSubject(): PublishSubject<Boolean> {
        return editClientRequestedSubject
    }

    override fun getOrders(size: Long, index: Long): Single<List<Order>> {
        return api.getOrders()
            .flatMap { orderResponse ->
                val orders = orderResponse.orders
                if (orders != null){
                    val orderIds = orderResponse.orders.map { order -> order.id }
                    getGeneralInvoices(size = size, index = index, orderIds = orderIds.joinToString(","), withPayments = true)
                        .flatMap {
                            Single.just(OrdersMapper.responseToOrders(it, orderResponse))
                        }
                } else {
                    Single.just(listOf())
                }

            }

    }

    override fun getGeneralInvoices(
        size: Long,
        index: Long,
        status: String?,
        num: String?,
        fullText: String?,
        orderIds: String,
        withPayments: Boolean
    ): Single<List<GeneralInvoice>> {
        return api.getGeneralInvoices(size = size, index = index, orderIds = orderIds, withPayments = withPayments)
            .flatMap {
                Single.just(GeneralInvoiceMapper.responseToDomainModel(it))
            }
    }

    override fun getOrder(orderId: String): Single<Order> {
        return api.getOrderDetail(orderId).flatMap {orderResponse->
            val orderIds = listOf(orderResponse.id)
            return@flatMap getGeneralInvoices(size = 1000, index = 0, orderIds = orderIds.joinToString(","), withPayments = true)
                .flatMap {
                    Single.just(OrdersMapper.responseToOrder(it, orderResponse))
                }
        }
    }

    override fun cancelOrder(orderId: String): Completable {
        return api.cancelTask(orderId).flatMapCompletable {
            orderCancelledSubject.onNext(Unit)
            Completable.complete()
        }
    }

    override fun getOrderCancelledSubject(): PublishSubject<Unit> {
        return orderCancelledSubject
    }

    override fun getCancelledTasks(orderId: String): Single<List<CancelledTaskModel>> {
        return api.getCancelledTasks(orderId).map { response->
            OrdersMapper.responseToCancelledTasks(response)
        }
    }

    override fun requestEditClient(): Completable {
        return api.requestEditClient().doOnComplete {
            editClientRequestedSubject.onNext(true)
        }
    }

    override fun getRequestEditClientTasks(): Single<List<RequestEditClientTaskModel>> {
        return api.getRequestEditClientTasks().map {
            ClientMapper.responseToRequestEditClientTasks(it)
        }
    }

}
