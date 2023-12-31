package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.ui.about_app.AboutAppViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatViewModel
import com.custom.rgs_android_dom.ui.countries.CountriesViewModel
import com.custom.rgs_android_dom.ui.demo.DemoViewModel
import com.custom.rgs_android_dom.ui.root.RootViewModel
import com.custom.rgs_android_dom.ui.client.ClientViewModel
import com.custom.rgs_android_dom.ui.client.agent.AgentViewModel
import com.custom.rgs_android_dom.ui.client.agent.edit.EditAgentViewModel
import com.custom.rgs_android_dom.ui.client.agent.request_edit.RequestEditAgentViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.edit.EditPersonalDataViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.PersonalDataViewModel
import com.custom.rgs_android_dom.ui.address.suggestions.AddressSuggestionsViewModel
import com.custom.rgs_android_dom.ui.catalog.MainCatalogViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.more.MoreSingleProductViewModel
import com.custom.rgs_android_dom.ui.catalog.search.CatalogSearchViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogSubcategoriesViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductViewModel
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogPrimaryProductsViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.availableservices.TabAvailableServicesViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.TabCatalogViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.products.TabMyProductsViewModel
import com.custom.rgs_android_dom.ui.chats.call_request.CallRequestViewModel
import com.custom.rgs_android_dom.ui.chats.call.CallViewModel
import com.custom.rgs_android_dom.ui.chats.call.media_output_chooser.MediaOutputChooserViewModel
import com.custom.rgs_android_dom.ui.rationale.RequestRationaleViewModel
import com.custom.rgs_android_dom.ui.chats.chat.files.manage.ManageFileViewModel
import com.custom.rgs_android_dom.ui.chats.chat.files.upload.UploadFilesViewModel
import com.custom.rgs_android_dom.ui.chats.chat.files.viewers.image.ImageViewerViewModel
import com.custom.rgs_android_dom.ui.chats.chat.files.viewers.video.VideoPlayerViewModel
import com.custom.rgs_android_dom.ui.chats.ChatsViewModel
import com.custom.rgs_android_dom.ui.client.notifications_settings.NotificationsSettingsViewModel
import com.custom.rgs_android_dom.ui.client.order_detail.OrderDetailViewModel
import com.custom.rgs_android_dom.ui.client.order_detail.cancel_order.CancelOrderViewModel
import com.custom.rgs_android_dom.ui.client.orders.OrdersViewModel
import com.custom.rgs_android_dom.ui.client.payment_methods.PaymentMethodsViewModel
import com.custom.rgs_android_dom.ui.client.payment_methods.delete.DeletePaymentMethodViewModel
import com.custom.rgs_android_dom.ui.client.payment_methods.error.ErrorDeletePaymentMethodViewModel
import com.custom.rgs_android_dom.ui.main.MainViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.add_photo.AddPhotoViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.delete_client.DeleteClientViewModel
import com.custom.rgs_android_dom.ui.property.add.details.PropertyDetailsViewModel
import com.custom.rgs_android_dom.ui.property.add.select_type.SelectPropertyTypeViewModel
import com.custom.rgs_android_dom.ui.property.info.PropertyInfoViewModel
import com.custom.rgs_android_dom.ui.registration.code.RegistrationCodeViewModel
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementViewModel
import com.custom.rgs_android_dom.ui.registration.fill_client.RegistrationFillClientViewModel
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneViewModel
import com.custom.rgs_android_dom.ui.screen_stub.ScreenStubViewModel
import com.custom.rgs_android_dom.ui.splash.SplashViewModel
import com.custom.rgs_android_dom.ui.web_view.WebViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.custom.rgs_android_dom.ui.client.personal_data.request_edit.RequestEditPersonalDataViewModel
import com.custom.rgs_android_dom.ui.onboarding.OnboardingViewModel
import com.custom.rgs_android_dom.ui.onboarding.TabOnboardingViewModel
import com.custom.rgs_android_dom.ui.policies.PoliciesViewModel
import com.custom.rgs_android_dom.ui.policies.add.AddPolicyViewModel
import com.custom.rgs_android_dom.ui.policies.add.info.InfoPolicyViewModel
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantViewModel
import com.custom.rgs_android_dom.ui.policies.insurant.dialogs.PolicyDialogsViewModel
import com.custom.rgs_android_dom.ui.policies.policy.PolicyViewModel
import com.custom.rgs_android_dom.ui.promo_code.PromoCodesViewModel
import com.custom.rgs_android_dom.ui.promo_code.add_agent.AddAgentPromoCodeViewModel
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeViewModel
import com.custom.rgs_android_dom.ui.promo_code.dialogs.PromoCodeDialogViewModel
import com.custom.rgs_android_dom.ui.promo_code.info_promo_code.InfoPromoCodeViewModel
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsViewModel
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressViewModel
import com.custom.rgs_android_dom.ui.property.delete.DeletePropertyViewModel
import com.custom.rgs_android_dom.ui.property.document.DocumentViewModel
import com.custom.rgs_android_dom.ui.property.document.detail_document.DetailDocumentViewModel
import com.custom.rgs_android_dom.ui.property.info.more.PropertyMoreViewModel
import com.custom.rgs_android_dom.ui.property.info.edit.avatar.EditPropertyAvatarBottomSheetViewModel
import com.custom.rgs_android_dom.ui.property.info.edit.EditPropertyInfoViewModel
import com.custom.rgs_android_dom.ui.property.info.edit.request_edit.RequestPropertyInfoEditViewModel
import com.custom.rgs_android_dom.ui.purchase.*
import com.custom.rgs_android_dom.ui.purchase.add.agent.AddAgentViewModel
import com.custom.rgs_android_dom.ui.purchase.add.comment.AddCommentViewModel
import com.custom.rgs_android_dom.ui.sos.SOSViewModel
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeViewModel
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressViewModel
import com.custom.rgs_android_dom.ui.purchase.select.card.SelectCardViewModel
import com.custom.rgs_android_dom.ui.purchase.add.email.AddEmailViewModel
import com.custom.rgs_android_dom.ui.purchase.payments.PaymentWebViewViewModel
import com.custom.rgs_android_dom.ui.purchase.payments.error.PaymentErrorViewModel
import com.custom.rgs_android_dom.ui.purchase.payments.success.PaymentSuccessViewModel
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderViewModel
import com.custom.rgs_android_dom.ui.purchase.service_order.WidgetOrderErrorViewModel
import com.custom.rgs_android_dom.ui.stories.StoriesViewModel
import com.custom.rgs_android_dom.ui.stories.tabs.TabGuaranteeViewModel
import com.custom.rgs_android_dom.ui.stories.tabs.TabNewServiceViewModel
import com.custom.rgs_android_dom.ui.stories.tabs.TabSupportViewModel
import com.custom.rgs_android_dom.ui.update_app.UpdateAppViewModel
import com.custom.rgs_android_dom.ui.promo_code.modal.ModalPromoCodesViewModel

val viewModelModule = module {
    viewModel { parameters -> RegistrationCodeViewModel(phone = parameters[0], token = parameters[1], registrationInteractor = get(), clientInteractor = get()) }
    viewModel { RegistrationPhoneViewModel(countriesInteractor = get(), registrationInteractor = get()) }
    viewModel { SplashViewModel(registrationInteractor = get(), clientInteractor = get()) }
    viewModel { OnboardingViewModel() }
    viewModel { TabOnboardingViewModel() }
    viewModel { DemoViewModel() }
    viewModel { parameters-> RegistrationAgreementViewModel(phone = parameters[0], registrationInteractor = get()) }
    viewModel { parameters-> RegistrationFillClientViewModel(phone = parameters.get(), clientInteractor= get()) }
    viewModel { parameters-> CountriesViewModel(selectedCountryLetterCode = parameters.get(), countriesInteractor = get())}
    viewModel { ClientViewModel(clientInteractor = get(), registrationInteractor = get(), propertyInteractor = get()) }
    viewModel { OrdersViewModel(clientInteractor = get(), purchaseInteractor = get()) }
    viewModel { parameters -> OrderDetailViewModel(chatInteractor = get(), order = parameters.get(), clientInteractor = get(), notificationManager = get()) }
    viewModel { RootViewModel(registrationInteractor = get(), clientInteractor = get(), chatInteractor = get(), notificationsInteractor = get(), mediaOutputManager = get(), notificationManager = get()) }
    viewModel { PersonalDataViewModel(clientInteractor = get()) }
    viewModel { EditPersonalDataViewModel(clientInteractor = get()) }
    viewModel { AgentViewModel(clientInteractor = get()) }
    viewModel { EditAgentViewModel(clientInteractor = get()) }
    viewModel { AboutAppViewModel() }
    viewModel { parameters -> ChatViewModel(case = parameters[0], backScreen = parameters[1], chatInteractor = get(), clientInteractor = get(), catalogInteractor = get(), propertyInteractor = get(), notificationManager = get()) }
    viewModel { parameters-> SelectPropertyTypeViewModel(propertyName = parameters[0], propertyAddress = parameters[1], propertyInteractor = get()) }
    viewModel { parameters-> PropertyDetailsViewModel(propertyName = parameters[0], propertyAddress = parameters[1], propertyType = parameters[2], propertyInteractor = get(), connectivityManager = get()) }
    viewModel { parameters-> PropertyInfoViewModel(objectId = parameters.get(), propertyInteractor = get(), clientInteractor = get(), connectivityManager = get(), context = get()) }
    viewModel { parameters-> EditPropertyInfoViewModel(objectId = parameters[0], isEditable = parameters[1], propertyInteractor = get(), addressInteractor = get()) }
    viewModel { parameters-> EditPropertyAvatarBottomSheetViewModel(propertyInteractor = get()) }
    viewModel { parameters-> RequestPropertyInfoEditViewModel(objectId = parameters.get(), propertyInteractor = get()) }
    viewModel { parameters-> DocumentViewModel(objectId = parameters.get(), propertyInteractor = get(), context = get()) }
    viewModel { parameters-> DetailDocumentViewModel( objectId = parameters[0], documentIndex = parameters[1], propertyItemModel = parameters[2], propertyInteractor = get()) }
    viewModel { ScreenStubViewModel() }
    viewModel { RequestEditAgentViewModel(clientInteractor = get()) }
    viewModel { RequestEditPersonalDataViewModel(clientInteractor = get()) }
    viewModel { parameters -> SelectAddressViewModel( propertyCount = parameters.get(), propertyInteractor = get(), addressInteractor = get(), context = get()) }
    viewModel { AddressSuggestionsViewModel(addressInteractor = get()) }
    viewModel { MainViewModel(registrationInteractor = get(), propertyInteractor = get(), catalogInteractor = get(), clientInteractor = get(), context = get()) }
    viewModel { WebViewViewModel() }
    viewModel { AddPhotoViewModel(clientInteractor = get()) }
    viewModel { UploadFilesViewModel(chatInteractor = get()) }
    viewModel { parameters-> ImageViewerViewModel(chatFile = parameters.get()) }
    viewModel { parameters-> ManageFileViewModel(chatFile = parameters.get()) }
    viewModel { parameters -> VideoPlayerViewModel(chatFile = parameters.get()) }
    viewModel { parameters -> CallViewModel(channelId = parameters[0], callType = parameters[1], consultant = parameters[2], callId = parameters[3] ,chatInteractor = get(), mediaOutputManager = get()) }
    viewModel { parameters -> CallRequestViewModel(callerId = parameters[0], callId = parameters[1], channelId = parameters[2], chatInteractor = get(), notificationManager = get()) }
    viewModel { PropertyUploadDocumentsViewModel(context = get(), propertyInteractor = get()) }
    viewModel { MediaOutputChooserViewModel(mediaOutputManager = get()) }
    viewModel { RequestRationaleViewModel() }
    viewModel { parameters -> MainCatalogViewModel(tab = parameters.get()) }
    viewModel { TabCatalogViewModel(catalogInteractor = get(), registrationInteractor = get()) }
    viewModel { TabMyProductsViewModel(catalogInteractor = get(), registrationInteractor = get(), purchaseInteractor = get(), clientInteractor = get()) }
    viewModel { TabAvailableServicesViewModel(catalogInteractor = get(), registrationInteractor = get(), purchaseInteractor = get()) }
    viewModel { parameters -> CatalogSubcategoriesViewModel(category = parameters.get(), registrationInteractor = get()) }
    viewModel { parameters -> CatalogSubcategoryViewModel(subCategory = parameters.get(), registrationInteractor = get()) }
    viewModel { parameters -> SingleProductViewModel(product = parameters.get(), registrationInteractor = get(), catalogInteractor = get()) }
    viewModel { parameters -> ServiceViewModel(service = parameters.get(), catalogInteractor = get(), propertyInteractor = get(), purchaseInteractor = get()) }
    viewModel { MoreSingleProductViewModel() }
    viewModel { parameters -> CatalogSearchViewModel(tag = parameters[0], catalogInteractor = get(), registrationInteractor = get(), clientInteractor = get(), chatInteractor = get()) }
    viewModel { parameters -> ProductViewModel(product = parameters.get(), registrationInteractor = get(), catalogInteractor = get(), propertyInteractor = get(), purchaseInteractor = get()) }
    viewModel { parameters -> CatalogPrimaryProductsViewModel(category = parameters.get()) }
    viewModel { parameters -> PurchaseViewModel(model = parameters[0], propertyInteractor = get(), clientInteractor = get(), purchaseInteractor = get(), promoCodesInteractor = get()) }
    viewModel { parameters -> SelectPurchaseAddressViewModel(selectedPropertyItem = parameters[0], propertyInteractor = get())}
    viewModel { PurchaseDateTimeViewModel() }
    viewModel { parameters -> SelectCardViewModel(selectedCard = parameters[0], purchaseInteractor = get()) }
    viewModel { parameters -> AddCommentViewModel(comment = parameters[0]) }
    viewModel { parameters -> AddEmailViewModel(parameters[0]) }
    viewModel { AddAgentViewModel(clientInteractor = get()) }
    viewModel { SOSViewModel(chatInteractor = get(), registrationInteractor = get(), clientInteractor = get(), context = get()) }
    viewModel { parameters -> PaymentWebViewViewModel(url = parameters.get()) }
    viewModel { PaymentErrorViewModel(chatInteractor = get()) }
    viewModel { parameters -> PaymentSuccessViewModel(productId = parameters[0], productVersionId = parameters[1], email = parameters[2], orderId = parameters[3], purchaseInteractor = get(), catalogInteractor = get(), clientInteractor = get()) }
    viewModel { parameters -> StoriesViewModel(tab = parameters.get()) }
    viewModel { TabNewServiceViewModel() }
    viewModel { TabGuaranteeViewModel() }
    viewModel { TabSupportViewModel() }
    viewModel { PoliciesViewModel(policiesInteractor = get(), clientInteractor = get()) }
    viewModel { AddPolicyViewModel(policiesInteractor = get()) }
    viewModel { InfoPolicyViewModel() }
    viewModel { InsurantViewModel(policiesInteractor = get()) }
    viewModel { parameters -> PolicyDialogsViewModel(policiesInteractor = get(), model = parameters.get(), chatInteractor = get()) }
    viewModel { parameters -> ServiceOrderViewModel(serviceOrderLauncher = parameters.get(), propertyInteractor = get(), catalogInteractor = get(), purchaseInteractor = get()) }
    viewModel { WidgetOrderErrorViewModel(chatInteractor = get()) }
    viewModel { parameters -> PolicyViewModel(contractId = parameters[0], isActivePolicy = parameters[1], policiesInteractor = get(), catalogInteractor = get()) }
    viewModel { ChatsViewModel(chatInteractor = get(), registrationInteractor = get()) }
    viewModel { parameters -> CancelOrderViewModel(order = parameters[0], clientInteractor = get()) }
    viewModel { parameters -> PropertyMoreViewModel(property = parameters[0], clientInteractor = get(), catalogInteractor = get())}
    viewModel { parameters -> DeletePropertyViewModel(property = parameters.get(), propertyInteractor = get(), clientInteractor = get(), chatInteractor = get(), catalogInteractor = get())}
    viewModel { PaymentMethodsViewModel(purchaseInteractor = get(), chatInteractor = get()) }
    viewModel { parameters -> DeletePaymentMethodViewModel(bindingId = parameters[0], purchaseInteractor = get()) }
    viewModel { parameters -> ErrorDeletePaymentMethodViewModel(errorCode = parameters[0]) }
    viewModel { NotificationsSettingsViewModel(clientInteractor = get()) }
    viewModel { UpdateAppViewModel(clientInteractor = get()) }
    viewModel { PromoCodesViewModel(promoCodesInteractor = get(), clientInteractor = get()) }
    viewModel { AddPromoCodeViewModel() }
    viewModel { parameters -> PromoCodeDialogViewModel (promoCode = parameters[0], purchaseModel = parameters[1], shouldShowAgentView =parameters[2], promoCodesInteractor = get(), chatInteractor = get()) }
    viewModel { parameters -> AddAgentPromoCodeViewModel(promoCode = parameters[0], shouldShowAgentView = parameters[1], purchaseModel = parameters[2], clientInteractor = get()) }
    viewModel { parameters -> ModalPromoCodesViewModel(promoCodesInteractor = get(), clientInteractor = get(), purchaseModel = parameters[0]) }
    viewModel { parameters -> DeleteClientViewModel(activeOrders = parameters[0], registrationInteractor = get()) }
    viewModel { InfoPromoCodeViewModel() }

}
