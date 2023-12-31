package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.client.models.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime

interface ClientRepository {

    fun updateClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        birthday: DateTime?,
        gender: Gender?,
        phone: String?,
        email: String?,
        avatar: String?
    ): Completable

    fun getClient(): Single<ClientModel>

    fun loadAndSaveClient(): Completable

    fun getClientUpdatedSubject(): Observable<ClientModel>

    fun assignAgent(code: String, phone: String, assignType: String): Completable

    fun postPassport(serial: String, number: String): Completable

    fun updatePassport(id: String, serial: String, number: String): Completable

    fun saveSecondPhone(phone: String): Completable

    fun updateSecondPhone(phone: String, id: String): Completable

    fun deleteContacts(ids: ArrayList<String>): Completable

    fun requestEditAgent(): Completable

    fun getRequestEditAgentTasks(): Single<List<RequestEditAgentTaskModel>>

    fun getEditAgentRequestedSubject(): PublishSubject<Boolean>

    fun getUserDetails(): Single<UserDetailsModel>

    fun getEditClientRequestedSubject(): PublishSubject<Boolean>

    fun getOrders(size: Long, index: Long): Single<List<Order>>

    fun getGeneralInvoices(
        size: Long,
        index: Long,
        status: String? = null,
        num: String? = null,
        fullText: String? = null,
        orderIds: String,
        withPayments: Boolean
    ): Single<List<GeneralInvoice>>

    fun getOrder(orderId: String): Single<Order>

    fun cancelOrder(orderId: String): Completable

    fun getOrderCancelledSubject(): PublishSubject<Unit>

    fun getCancelledTasks(orderId: String): Single<List<CancelledTaskModel>>

    fun requestEditClient(): Completable

    fun getRequestEditClientTasks(): Single<List<RequestEditClientTaskModel>>

    fun saveFCMToken(token: String, deviceId: String): Completable

    fun updateNotificationChannel(notificationChannel: NotificationChannelInfo): Single<ClientModel>

    fun getAllOrders(size: Long, index: Long): Single<List<Order>>

    fun getActualAppVersions(): Single<ActualAppVersionsModel>

}
