package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.ui.about_app.AboutAppViewModel
import com.custom.rgs_android_dom.ui.chat.ChatViewModel
import com.custom.rgs_android_dom.ui.countries.CountriesViewModel
import com.custom.rgs_android_dom.ui.demo.DemoViewModel
import com.custom.rgs_android_dom.ui.main.MainViewModel
import com.custom.rgs_android_dom.ui.client.ClientViewModel
import com.custom.rgs_android_dom.ui.client.agent.AgentViewModel
import com.custom.rgs_android_dom.ui.client.agent.edit.EditAgentViewModel
import com.custom.rgs_android_dom.ui.client.agent.request_edit.RequestEditAgentViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.edit.EditPersonalDataViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.PersonalDataViewModel
import com.custom.rgs_android_dom.ui.location.rationale.RequestLocationRationaleViewModel
import com.custom.rgs_android_dom.ui.address.suggestions.AddressSuggestionsViewModel
import com.custom.rgs_android_dom.ui.chat.call.CallViewModel
import com.custom.rgs_android_dom.ui.chat.files.manage.ManageFileViewModel
import com.custom.rgs_android_dom.ui.chat.files.upload.UploadFilesViewModel
import com.custom.rgs_android_dom.ui.chat.files.viewers.image.ImageViewerViewModel
import com.custom.rgs_android_dom.ui.chat.files.viewers.video.VideoPlayerViewModel
import com.custom.rgs_android_dom.ui.main.stub.MainStubViewModel
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

val viewModelModule = module {
    viewModel { parameters -> RegistrationCodeViewModel(phone = parameters[0], token = parameters[1], registrationInteractor = get(), clientInteractor = get()) }
    viewModel { RegistrationPhoneViewModel(countriesInteractor = get(), registrationInteractor = get()) }
    viewModel { SplashViewModel(registrationInteractor = get(), clientInteractor = get()) }
    viewModel { DemoViewModel() }
    viewModel { parameters-> RegistrationAgreementViewModel(phone = parameters[0], closeAfterAccept = parameters[1], registrationInteractor = get()) }
    viewModel { parameters-> RegistrationFillClientViewModel(phone = parameters.get(), clientInteractor= get()) }
    viewModel { parameters-> CountriesViewModel(selectedCountryLetterCode = parameters.get(), countriesInteractor = get())}
    viewModel { ClientViewModel(clientInteractor = get(), registrationInteractor = get(), propertyInteractor = get()) }
    viewModel { MainViewModel(registrationInteractor = get(), clientInteractor = get()) }
    viewModel { PersonalDataViewModel(clientInteractor = get()) }
    viewModel { EditPersonalDataViewModel(clientInteractor = get()) }
    viewModel { AgentViewModel(clientInteractor = get()) }
    viewModel { EditAgentViewModel(clientInteractor = get()) }
    viewModel { AboutAppViewModel() }
    viewModel { ChatViewModel(chatInteractor = get()) }
    viewModel { parameters-> SelectPropertyTypeViewModel(propertyName = parameters[0], propertyAddress = parameters[1], propertyInteractor = get()) }
    viewModel { parameters-> PropertyDetailsViewModel(propertyName = parameters[0], propertyAddress = parameters[1], propertyType = parameters[2], propertyInteractor = get()) }
    viewModel { parameters-> PropertyInfoViewModel(objectId = parameters.get(), propertyInteractor = get()) }
    viewModel { ScreenStubViewModel() }
    viewModel { RequestEditAgentViewModel(clientInteractor = get()) }
    viewModel { RequestEditPersonalDataViewModel(clientInteractor = get()) }
    viewModel { parameters -> SelectAddressViewModel( propertyCount = parameters.get(), propertyInteractor = get(), addressInteractor = get(), context = get()) }
    viewModel { RequestLocationRationaleViewModel() }
    viewModel { AddressSuggestionsViewModel(addressInteractor = get()) }
    viewModel { MainStubViewModel(registrationInteractor = get()) }
    viewModel { WebViewViewModel() }
    viewModel { AddPhotoViewModel(clientInteractor = get()) }
    viewModel { UploadFilesViewModel(chatInteractor = get()) }
    viewModel { parameters-> ImageViewerViewModel(chatFile = parameters.get()) }
    viewModel { parameters-> ManageFileViewModel(chatFile = parameters.get()) }
    viewModel { parameters -> VideoPlayerViewModel(chatFile = parameters.get()) }
    viewModel { parameters -> CallViewModel(callType = parameters[0], chatInteractor = get()) }
}