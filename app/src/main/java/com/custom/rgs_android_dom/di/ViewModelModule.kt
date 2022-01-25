package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.ui.about_app.AboutAppViewModel
import com.custom.rgs_android_dom.ui.chat.ChatViewModel
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
import com.custom.rgs_android_dom.ui.catalog.product.MyProductViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.more.MoreSingleProductViewModel
import com.custom.rgs_android_dom.ui.catalog.search.CatalogSearchViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogSubcategoriesViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ServiceViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogPrimaryProductsViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.availableservices.TabAvailableServicesViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.TabCatalogViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.products.TabProductsViewModel
import com.custom.rgs_android_dom.ui.chat.call.CallViewModel
import com.custom.rgs_android_dom.ui.chat.call.media_output_chooser.MediaOutputChooserViewModel
import com.custom.rgs_android_dom.ui.rationale.RequestRationaleViewModel
import com.custom.rgs_android_dom.ui.chat.files.manage.ManageFileViewModel
import com.custom.rgs_android_dom.ui.chat.files.upload.UploadFilesViewModel
import com.custom.rgs_android_dom.ui.chat.files.viewers.image.ImageViewerViewModel
import com.custom.rgs_android_dom.ui.chat.files.viewers.video.VideoPlayerViewModel
import com.custom.rgs_android_dom.ui.main.MainViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.add_photo.AddPhotoViewModel
import com.custom.rgs_android_dom.ui.property.add.details.PropertyDetailsViewModel
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressViewModel
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
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsViewModel
import com.custom.rgs_android_dom.ui.property.document.DocumentViewModel
import com.custom.rgs_android_dom.ui.property.document.detail_document.DetailDocumentViewModel
import com.custom.rgs_android_dom.ui.sos.SOSViewModel
import com.custom.rgs_android_dom.ui.purchase_service.PurchaseViewModel
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time.PurchaseDateTimeViewModel
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address.PurchaseAddressViewModel
import com.custom.rgs_android_dom.ui.purchase_service.AddAgentViewModel
import com.custom.rgs_android_dom.ui.purchase_service.AddEmailViewModel
import com.custom.rgs_android_dom.ui.purchase_service.SelectCardViewModel

val viewModelModule = module {
    viewModel { parameters -> RegistrationCodeViewModel(phone = parameters[0], token = parameters[1], registrationInteractor = get(), clientInteractor = get()) }
    viewModel { RegistrationPhoneViewModel(countriesInteractor = get(), registrationInteractor = get()) }
    viewModel { SplashViewModel(registrationInteractor = get(), clientInteractor = get()) }
    viewModel { DemoViewModel() }
    viewModel { parameters-> RegistrationAgreementViewModel(phone = parameters[0], closeAfterAccept = parameters[1], registrationInteractor = get()) }
    viewModel { parameters-> RegistrationFillClientViewModel(phone = parameters.get(), clientInteractor= get()) }
    viewModel { parameters-> CountriesViewModel(selectedCountryLetterCode = parameters.get(), countriesInteractor = get())}
    viewModel { ClientViewModel(clientInteractor = get(), registrationInteractor = get(), propertyInteractor = get()) }
    viewModel { RootViewModel(registrationInteractor = get(), clientInteractor = get()) }
    viewModel { PersonalDataViewModel(clientInteractor = get()) }
    viewModel { EditPersonalDataViewModel(clientInteractor = get()) }
    viewModel { AgentViewModel(clientInteractor = get()) }
    viewModel { EditAgentViewModel(clientInteractor = get()) }
    viewModel { AboutAppViewModel() }
    viewModel { ChatViewModel(chatInteractor = get()) }
    viewModel { parameters-> SelectPropertyTypeViewModel(propertyName = parameters[0], propertyAddress = parameters[1], propertyInteractor = get()) }
    viewModel { parameters-> PropertyDetailsViewModel(propertyName = parameters[0], propertyAddress = parameters[1], propertyType = parameters[2], propertyInteractor = get(), context = get()) }
    viewModel { parameters-> PropertyInfoViewModel(objectId = parameters.get(), propertyInteractor = get()) }
    viewModel { parameters-> DocumentViewModel(objectId = parameters[0], propertyItemModel = parameters[1], propertyInteractor = get()) }
    viewModel { parameters-> DetailDocumentViewModel( objectId = parameters[0], documentIndex = parameters[1], propertyItemModel = parameters[2], propertyInteractor = get()) }
    viewModel { ScreenStubViewModel() }
    viewModel { RequestEditAgentViewModel(clientInteractor = get()) }
    viewModel { RequestEditPersonalDataViewModel(clientInteractor = get()) }
    viewModel { parameters -> SelectAddressViewModel( propertyCount = parameters.get(), propertyInteractor = get(), addressInteractor = get(), context = get()) }
    viewModel { AddressSuggestionsViewModel(addressInteractor = get()) }
    viewModel { MainViewModel(registrationInteractor = get(), propertyInteractor = get(), catalogInteractor = get()) }
    viewModel { WebViewViewModel() }
    viewModel { AddPhotoViewModel(clientInteractor = get()) }
    viewModel { UploadFilesViewModel(chatInteractor = get()) }
    viewModel { parameters-> ImageViewerViewModel(chatFile = parameters.get()) }
    viewModel { parameters-> ManageFileViewModel(chatFile = parameters.get()) }
    viewModel { parameters -> VideoPlayerViewModel(chatFile = parameters.get()) }
    viewModel { parameters -> CallViewModel(callType = parameters[0], consultant = parameters[1], chatInteractor = get(), mediaOutputManager = get()) }
    viewModel { PropertyUploadDocumentsViewModel(propertyInteractor = get()) }
    viewModel { MediaOutputChooserViewModel(mediaOutputManager = get()) }
    viewModel { RequestRationaleViewModel() }
    viewModel { parameters -> MainCatalogViewModel(tab = parameters.get()) }
    viewModel { TabCatalogViewModel(catalogInteractor = get(), registrationInteractor = get()) }
    viewModel { TabProductsViewModel(catalogInteractor = get()) }
    viewModel { TabAvailableServicesViewModel() }
    viewModel { parameters -> CatalogSubcategoriesViewModel(category = parameters.get(), registrationInteractor = get()) }
    viewModel { parameters -> CatalogSubcategoryViewModel(subCategory = parameters.get(), registrationInteractor = get()) }
    viewModel { parameters -> SingleProductViewModel(productId = parameters.get(), catalogInteractor = get()) }
    viewModel { MoreSingleProductViewModel() }
    viewModel { parameters -> CatalogSearchViewModel(tag = parameters[0], catalogInteractor = get()) }
    viewModel { parameters -> ProductViewModel(productId = parameters.get(), catalogInteractor = get()) }
    viewModel { parameters -> CatalogPrimaryProductsViewModel(category = parameters.get()) }
    viewModel { MyProductViewModel() }
    viewModel { parameters -> ServiceViewModel(product = parameters.get()) }
    viewModel { parameters -> PurchaseViewModel(parameters.get(), propertyInteractor = get(), purchaseInteractor = get()) }
    viewModel { parameters -> PurchaseAddressViewModel(selectedPropertyItem = parameters.get(), propertyInteractor = get())}
    viewModel { parameters -> PurchaseDateTimeViewModel(purchaseDateTimeModel = parameters.get(), purchaseInteractor = get())}
    viewModel { SelectCardViewModel(purchaseInteractor = get()) }
    viewModel { AddEmailViewModel() }
    viewModel { AddAgentViewModel(clientInteractor = get()) }
    viewModel { SOSViewModel(chatInteractor = get(), registrationInteractor = get(), clientInteractor = get(), context = get()) }
}
