package com.custom.rgs_android_dom.profile

import android.os.Build
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepositoryMock
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.ValidateClientException
import com.custom.rgs_android_dom.domain.client.view_states.ClientShortViewState
import com.custom.rgs_android_dom.domain.client.view_states.FillClientViewState
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.AGENTCODE
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.BIRTHDATE
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.BIRTHDATESTR
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.GENDER
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.NAME
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.PHONEAGENT
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.PHONEMASKED
import com.custom.rgs_android_dom.profile.MockClientRepository.Companion.SURNAME
import com.custom.rgs_android_dom.utils.CacheHelper
import net.danlew.android.joda.JodaTimeAndroid
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ProfileInteractorTest {

    lateinit var clientInteractor: ClientInteractor

    @Before
    fun init() {
        val context = RuntimeEnvironment.systemContext
        JodaTimeAndroid.init(context)
        CacheHelper.init()

        clientInteractor =
            ClientInteractor(registrationRepository = MockRegistrationRepositoryImpl(), countriesRepository = CountriesRepositoryMock(), clientRepository = MockClientRepository())

        clientInteractor.fillClientStateSubject.hide()
            .subscribe {
                profileViewState = it
            }

        clientInteractor.validateSubject.hide()
            .subscribe { isValidate = it }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    private var profileViewState = FillClientViewState("")
    private var isValidate = false


    @Test
    fun updateProfileTest() {
        clientInteractor.onSurnameChanged(SURNAME)
        clientInteractor.onNameChanged(NAME)
        clientInteractor.onBirthdayChanged(BIRTHDATESTR, true)
        clientInteractor.onAgentCodeChanged(AGENTCODE)
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        clientInteractor.onGenderSelected(GENDER)
        clientInteractor.updateNewClient().blockingGet()

        assertEquals(GENDER, profileViewState.gender)

        assertEquals(SURNAME, profileViewState.surname)
        assertEquals(NAME, profileViewState.name)
        assertEquals(BIRTHDATE, profileViewState.birthday)
        assertEquals(AGENTCODE, profileViewState.agentCode)
        assertEquals(PHONEAGENT, profileViewState.agentPhone)
        assertEquals(GENDER, profileViewState.gender)
    }

    @Test
    fun validateProfileSurnameTest() {
        clientInteractor.onSurnameChanged(SURNAME)
        assertEquals(isValidate, true)
        clientInteractor.onSurnameChanged("")
        assertEquals(isValidate, false)
    }

    @Test
    fun validateProfileNameTest() {

        clientInteractor.onNameChanged(NAME)
        assertEquals(isValidate, true)
        clientInteractor.onNameChanged("")
        assertEquals(isValidate, false)

    }

    @Test
    fun validateProfileBirthdayTest() {
        clientInteractor.onBirthdayChanged(BIRTHDATESTR, true)
        assertEquals(isValidate, true)
        clientInteractor.onBirthdayChanged(BIRTHDATESTR, false)
        assertEquals(isValidate, false)
        clientInteractor.onBirthdayChanged("", true)
        assertEquals(isValidate, false)
    }

    @Test
    fun validateProfileAgentCodeTest() {
        clientInteractor.onAgentCodeChanged(AGENTCODE)
        assertEquals(isValidate, true)
        clientInteractor.onAgentCodeChanged("")
        assertEquals(isValidate, false)
    }

    @Test
    fun validateProfileAgentPhoneTest() {
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        assertEquals(isValidate, true)
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, false)
        assertEquals(isValidate, false)
        clientInteractor.onAgentPhoneChanged("",false)
        assertEquals(isValidate, false)

        clientInteractor.onGenderSelected(GENDER)
        assertEquals(isValidate, true)
    }

    @Test
    fun validateProfileGenderTest() {
        clientInteractor.onGenderSelected(GENDER)
        assertEquals(isValidate, true)
    }

    @Test
    fun saveProfileTest(){
        clientInteractor.onAgentCodeChanged(AGENTCODE)

        var ex = clientInteractor.updateNewClient().blockingGet()
        if(ex is ValidateClientException){
            assertTrue(true)
        }  else {
            assertTrue(false)
        }

        clientInteractor.onAgentCodeChanged("")
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, true)

        ex =  clientInteractor.updateNewClient().blockingGet()
        if(ex is ValidateClientException){
            assertTrue(true)
        }  else {
            assertTrue(false)
        }

        clientInteractor.onAgentCodeChanged(AGENTCODE)
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, true)

        ex = clientInteractor.updateNewClient().blockingGet()
        assertTrue(ex == null)

    }

    @Test
    fun getShortClientTest(){
        clientInteractor.onSurnameChanged(SURNAME)
        clientInteractor.onNameChanged(NAME)
        clientInteractor.onBirthdayChanged(BIRTHDATESTR, true)
        clientInteractor.onAgentCodeChanged(AGENTCODE)
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        clientInteractor.onGenderSelected(GENDER)
        clientInteractor.updateNewClient().blockingGet()

        val client = clientInteractor.getClient().blockingGet()

        assertEquals(client, ClientShortViewState(NAME, SURNAME, PHONEMASKED))
    }
}