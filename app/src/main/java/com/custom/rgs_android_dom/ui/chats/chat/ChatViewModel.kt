package com.custom.rgs_android_dom.ui.chats.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseTimePeriodModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductLauncher
import com.custom.rgs_android_dom.ui.chats.call.CallFragment
import com.custom.rgs_android_dom.ui.chats.chat.files.viewers.image.ImageViewerFragment
import com.custom.rgs_android_dom.ui.chats.chat.files.viewers.video.VideoPlayerFragment
import com.custom.rgs_android_dom.ui.client.order_detail.OrderDetailFragment
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager
import com.custom.rgs_android_dom.ui.navigation.ScreenInfo
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.ui.promo_code.PromoCodesFragment
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment
import com.custom.rgs_android_dom.ui.purchase.payments.PaymentWebViewFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderLauncher
import com.custom.rgs_android_dom.ui.purchase.service_order.WidgetOrderErrorFragment
import com.custom.rgs_android_dom.utils.logException
import com.yandex.metrica.YandexMetrica
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.io.File

class ChatViewModel(
    private val case: CaseModel,
    private val backScreen: ScreenInfo?,
    private val chatInteractor: ChatInteractor,
    private val catalogInteractor: CatalogInteractor,
    private val clientInteractor: ClientInteractor,
    private val propertyInteractor: PropertyInteractor,
    private val notificationManager: MSDNotificationManager
) : BaseViewModel() {

    private val caseController = MutableLiveData<CaseModel>()
    val caseObserver: LiveData<CaseModel> = caseController

    private val chatItemsController = MutableLiveData<List<ChatItemModel>>()
    val chatItemsObserver: LiveData<List<ChatItemModel>> = chatItemsController

    private val newItemsController = MutableLiveData<List<ChatItemModel>>()
    val newItemsObserver: LiveData<List<ChatItemModel>> = newItemsController

    private val downloadFileController = MutableLiveData<ChatFileModel>()
    val downloadFileObserver: LiveData<ChatFileModel> = downloadFileController

    private var email: String? = null

    init {

        notificationManager.cancel(case.channelId.hashCode())

        caseController.value = case

        chatInteractor.startListenNewMessageEvent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        loadChatHistory()

        chatInteractor.newChatItemsSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty() && it[0].channelId == case.channelId) {
                        newItemsController.value = it
                        viewChannel()
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getFilesToUploadSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    postFilesInChat(it)
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.getPersonalData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { personalData ->
                    email = personalData.email.ifEmpty { null }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getWsEventsSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it.event) {
                        WsEvent.SOCKET_CONNECTED -> {
                            loadChatHistory()
                        }
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        YandexMetrica.reportEvent("chat_start", "{\"chatID\":\"${chatInteractor.getMasterOnlineCase().channelId}\"}")
    }

    fun onBackClick() {
        if (backScreen != null) {
            when (backScreen.targetScreen){
                TargetScreen.ORDER_DETAILS -> {
                    closeController.value = Unit
                    val order = backScreen.params as Order
                    ScreenManager.showScreen(OrderDetailFragment.newInstance(order))
                }
                TargetScreen.PROMO_CODE -> {
                    closeController.value = Unit
                    ScreenManager.showScreen(PromoCodesFragment())
                }
                else -> {
                    closeController.value = Unit
                }
            }
        } else {
            closeController.value = Unit
        }

    }

    fun onSendMessageClick(newMessage: String) {
        chatInteractor.sendMessage(channelId = case.channelId, message = newMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { handleNetworkException(it) }
            .subscribeBy(
                onError = {
                    handleNetworkException(it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    fun onFileClick(chatFile: ChatFileModel) {
        when {
            chatFile.mimeType.contains("image") -> {
                val imageViewerFragment = ImageViewerFragment.newInstance(chatFile)
                ScreenManager.showScreen(imageViewerFragment)
            }
            chatFile.mimeType.contains("video") -> {
                val videoPlayerFragment = VideoPlayerFragment.newInstance(chatFile)
                ScreenManager.showScreen(videoPlayerFragment)
            }
            else -> {
                downloadFileController.value = chatFile
            }
        }

    }

    fun onAudioCallClick() {
        val callFragment = CallFragment.newInstance(
            case.channelId,
            CallType.AUDIO_CALL,
            chatInteractor.getCurrentConsultant()
        )
        ScreenManager.showScreen(callFragment)
    }

    fun onVideoCallClick() {
        val callFragment = CallFragment.newInstance(
            case.channelId,
            CallType.VIDEO_CALL,
            chatInteractor.getCurrentConsultant()
        )
        ScreenManager.showScreen(callFragment)
    }

    fun onChatClose() {
        viewChannel()
        if (backScreen != null) {
            when (backScreen.targetScreen){
                TargetScreen.ORDER_DETAILS -> {
                    val order = backScreen.params as Order
                    ScreenManager.showScreen(OrderDetailFragment.newInstance(order))
                }
            }
        }
    }

    fun onUserTyping() {
        chatInteractor.notifyTyping(case.channelId)
    }

    fun onPayClick(paymentUrl: String, productId: String, amount: Int) {
        // TODO Add Product Version id
        ScreenManager.showBottomScreen(
            PaymentWebViewFragment.newInstance(
                url = paymentUrl,
                productId = productId,
                productVersionId = "",
                email = email ?: "",
                orderId = ""
            )
        )
    }

    private fun loadChatHistory() {
        chatInteractor.getChatHistory(case.channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    chatItemsController.value = it

                    viewChannel()
                },
                onError = {
                    logException(this, it)

                    handleNetworkException(it)

                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun postFilesInChat(files: List<File>) {
        chatInteractor.postFilesToChat(case.channelId, files)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun viewChannel() {
        chatInteractor.viewChannel(case.channelId)
            .andThen(chatInteractor.loadCases())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onWidgetPayClick(widget: WidgetModel.WidgetAdditionalInvoiceModel) {
        // TODO ADD Product version id
        ScreenManager.showBottomScreen(
            PaymentWebViewFragment.newInstance(
                url = widget.paymentUrl ?: "",
                productId = /*widget.productId*/"",
                productVersionId = "",
                email = email ?: "",
                orderId = widget.orderId ?: ""
            )
        )
    }

    // Клик по виджету для заказа комплексного продукта
    fun onWidgetProductClick(widget: WidgetModel.WidgetOrderProductModel) {
        catalogInteractor.getProduct(widget.productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {product->
                    if (product.defaultProduct){
                        ScreenManager.showBottomScreen(SingleProductFragment.newInstance(SingleProductLauncher(product.id, product.versionId)))
                    } else {
                        ScreenManager.showBottomScreen(ProductFragment.newInstance(ProductLauncher(product.id, product.versionId)))
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    // Обработка заказа дефолтного продукта
    fun onWidgetOrderDefaultProductClick(widget: WidgetModel.WidgetOrderDefaultProductModel) {
        val propertyRequest: Single<PropertyItemModel> = if (widget.objId != null) {
            propertyInteractor.getPropertyItem(widget.objId)
        } else {
            Single.just(PropertyItemModel.empty())
        }
        Single.zip(
            catalogInteractor.getProduct(widget.productId),
            catalogInteractor.getProductServices(widget.productId, false, null),
            propertyRequest
        ) { product, services, property->
            PurchaseModel(
                id = widget.productId,
                versionId = product.versionId ?: "",
                defaultProduct = product.defaultProduct,
                duration = product.duration,
                deliveryTime = product.deliveryTime,
                deliveryType = services[0].serviceDeliveryType,
                propertyItemModel = if (!property.isEmpty) property else null,
                purchaseDateTimeModel = getPurchaseDate(widget.orderDate, widget.orderTime),
                logoSmall = product.logoSmall,
                name = product.name,
                price = product.price
            )
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {purchaseModel->
                val purchaseFragment = PurchaseFragment.newInstance(purchaseModel, null)
                ScreenManager.showBottomScreen(purchaseFragment)
            },
            onError = {
                logException(this, it)
                ScreenManager.showScreen(WidgetOrderErrorFragment())
            }
        ).addTo(dataCompositeDisposable)
    }

    // Обработка клику по виджету заказа УСЛУГИ НА БАЛАНСЕ (просто у виджета такой нэйминг)
    fun onWidgetOrderComplexProductClick(widget: WidgetModel.WidgetOrderComplexProductModel) {
        val propertyRequest: Single<PropertyItemModel> = if (widget.objId != null) {
            propertyInteractor.getPropertyItem(widget.objId)
        } else {
            Single.just(PropertyItemModel.empty())
        }
        widget.clientServiceId?.let { id ->
            Single.zip(
                propertyRequest,
                catalogInteractor.getServiceFromAvailable(id)
            ) { property, service ->
                ServiceOrderLauncher(
                    serviceId = service.serviceId,
                    productId = service.productId,
                    clientProductId = service.clientProductId,
                    serviceVersionId = service.serviceVersionId,
                    property = if (!property.isEmpty) property else null,
                    dateTime = getPurchaseDate(widget.orderDate, widget.orderTime),
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val serviceOrderFragment = ServiceOrderFragment.newInstance(it)
                    ScreenManager.showScreen(serviceOrderFragment)
                },
                onError = {
                    ScreenManager.showScreen(WidgetOrderErrorFragment())
                }
            ).addTo(dataCompositeDisposable)
        }
    }

    private fun getPurchaseDate(orderDate: DateTime?, orderTime: OrderTimeModel?): PurchaseDateTimeModel? {
        return if (orderDate != null && orderTime != null) {
            PurchaseDateTimeModel(
                selectedDate = orderDate.toLocalDateTime(),
                selectedPeriodModel = getPurchaseTime(orderTime),
            )
        } else {
            null
        }
    }

    private fun getPurchaseTime(orderTime: OrderTimeModel?) = PurchaseTimePeriodModel(
        id = when (orderTime?.from.toString()) {
            "6:00" -> 0
            "9:00" -> 1
            "12:00" -> 2
            "18:00" -> 3
            else -> throw IllegalArgumentException("wrong argument timeOfDay")
        },
        timeOfDay = when (orderTime?.from.toString()) {
            "6:00" -> "Утро"
            "9:00" -> "До полудня"
            "12:00" -> "День"
            "18:00" -> "Вечер"
            else -> throw IllegalArgumentException("wrong argument timeOfDay")
        },
        timeFrom = orderTime?.from.toString(),
        timeTo = orderTime?.to.toString(),
        isSelectable = true,
        isSelected = true
    )
}