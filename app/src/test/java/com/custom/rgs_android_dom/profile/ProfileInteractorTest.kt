package com.custom.rgs_android_dom.profile

import android.os.Build
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.FillClientViewState
import com.custom.rgs_android_dom.domain.client.ValidateClientException
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.LocalDate
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
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

    companion object {
        val NAME = "ИВАН"
        val SURNAME = "ИВАНОВ"
        val BIRTHDATE = LocalDate(2002, 10, 3)
        val BIRTHDATESTR = "03.10.2002"
        val GENDER = Gender.FEMALE
        val AGENTCODE = "777"
        val PHONEAGENT = "79044961128"
    }

    lateinit var clientInteractor: ClientInteractor

    @Before
    fun init() {
        val context = RuntimeEnvironment.systemContext
        JodaTimeAndroid.init(context)

        clientInteractor =
            ClientInteractor(registrationRepository = MockRegistrationRepositoryImpl())

        clientInteractor.profileStateSubject.hide()
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
        clientInteractor.updateProfile().blockingGet()

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
        try {
            clientInteractor.updateProfile().blockingGet()
            Assert.fail()
        } catch (e: ValidateClientException){
            Assert.assertTrue(true)
        } catch (e: Exception){
            Assert.assertTrue(false)
        }

        clientInteractor.onAgentCodeChanged("")
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        try {
            clientInteractor.updateProfile().blockingGet()
            Assert.fail()
        } catch (e: ValidateClientException){
            Assert.assertTrue(true)
        } catch (e: Exception){
            Assert.assertTrue(false)
        }

        clientInteractor.onAgentCodeChanged(AGENTCODE)
        clientInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        try {
            clientInteractor.updateProfile().blockingGet()
            Assert.assertTrue(true)
        } catch (e: Exception){
            Assert.fail()
        }
    }
}