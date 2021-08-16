package com.custom.rgs_android_dom.profile

import android.os.Build
import com.custom.rgs_android_dom.domain.profile.ProfileInteractor
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.profile.ProfileViewState
import com.custom.rgs_android_dom.domain.registration.ValidateProfileException
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

    lateinit var profileInteractor: ProfileInteractor

    @Before
    fun init() {
        val context = RuntimeEnvironment.systemContext
        JodaTimeAndroid.init(context)

        profileInteractor =
            ProfileInteractor(registrationRepository = MockRegistrationRepositoryImpl())

        profileInteractor.profileStateSubject.hide()
            .subscribe {
                profileViewState = it
            }

        profileInteractor.validateSubject.hide()
            .subscribe { isValidate = it }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    private var profileViewState = ProfileViewState("")
    private var isValidate = false


    @Test
    fun updateProfileTest() {
        profileInteractor.onSurnameChanged(SURNAME)
        profileInteractor.onNameChanged(NAME)
        profileInteractor.onBirthdayChanged(BIRTHDATESTR, true)
        profileInteractor.onAgentCodeChanged(AGENTCODE)
        profileInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        profileInteractor.onGenderSelected(GENDER)
        profileInteractor.updateProfile().blockingGet()

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
        profileInteractor.onSurnameChanged(SURNAME)
        assertEquals(isValidate, true)
        profileInteractor.onSurnameChanged("")
        assertEquals(isValidate, false)
    }

    @Test
    fun validateProfileNameTest() {

        profileInteractor.onNameChanged(NAME)
        assertEquals(isValidate, true)
        profileInteractor.onNameChanged("")
        assertEquals(isValidate, false)

    }

    @Test
    fun validateProfileBirthdayTest() {
        profileInteractor.onBirthdayChanged(BIRTHDATESTR, true)
        assertEquals(isValidate, true)
        profileInteractor.onBirthdayChanged(BIRTHDATESTR, false)
        assertEquals(isValidate, false)
        profileInteractor.onBirthdayChanged("", true)
        assertEquals(isValidate, false)
    }

    @Test
    fun validateProfileAgentCodeTest() {
        profileInteractor.onAgentCodeChanged(AGENTCODE)
        assertEquals(isValidate, true)
        profileInteractor.onAgentCodeChanged("")
        assertEquals(isValidate, false)
    }

    @Test
    fun validateProfileAgentPhoneTest() {
        profileInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        assertEquals(isValidate, true)
        profileInteractor.onAgentPhoneChanged(PHONEAGENT, false)
        assertEquals(isValidate, false)
        profileInteractor.onAgentPhoneChanged("",false)
        assertEquals(isValidate, false)

        profileInteractor.onGenderSelected(GENDER)
        assertEquals(isValidate, true)
    }

    @Test
    fun validateProfileGenderTest() {
        profileInteractor.onGenderSelected(GENDER)
        assertEquals(isValidate, true)
    }

    @Test
    fun saveProfileTest(){
        profileInteractor.onAgentCodeChanged(AGENTCODE)
        try {
            profileInteractor.updateProfile().blockingGet()
            Assert.fail()
        } catch (e: ValidateProfileException){
            Assert.assertTrue(true)
        } catch (e: Exception){
            Assert.assertTrue(false)
        }

        profileInteractor.onAgentCodeChanged("")
        profileInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        try {
            profileInteractor.updateProfile().blockingGet()
            Assert.fail()
        } catch (e: ValidateProfileException){
            Assert.assertTrue(true)
        } catch (e: Exception){
            Assert.assertTrue(false)
        }

        profileInteractor.onAgentCodeChanged(AGENTCODE)
        profileInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        try {
            profileInteractor.updateProfile().blockingGet()
            Assert.assertTrue(true)
        } catch (e: Exception){
            Assert.fail()
        }
    }
}