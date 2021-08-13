package com.custom.rgs_android_dom

import android.content.Context
import com.custom.rgs_android_dom.data.repositories.registration.MockRegistrationRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.ProfileInteractor
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.ProfileViewState
import net.danlew.android.joda.JodaTimeAndroid

import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.junit.Assert.assertEquals
import org.koin.android.ext.koin.androidContext
import org.mockito.Mockito.mock


class ProfileInteractorTest: KoinComponent {

    companion object{
        val NAME = "ИВАН"
        val SURNAME = "ИВАНОВ"
        val BIRTHDATE = LocalDate(2002, 10, 3)
        val BIRTHDATESTR = "03.10.2002"
        val GENDER = Gender.FEMALE
        val AGENTCODE = "777"
        val PHONEAGENT = "79044961128"
    }

    @Before
    fun init(){
        JodaTimeAndroid.init(mock(Context::class.java))
        startKoin {
            androidContext(mock(Context::class.java))
            modules(
                module {
                    single<RegistrationRepository> { MockRegistrationRepositoryImpl() }
                    factory { ProfileInteractor(registrationRepository = get()) }
                }
            )
        }

        profileInteractor.profileStateSubject.hide()
            .subscribe { profileViewState = it }

        profileInteractor.validateSubject.hide()
            .subscribe { isValidate = it  }
    }

    private var profileViewState = ProfileViewState("")
    private var isValidate = false

    private val profileInteractor: ProfileInteractor by inject()


    @Test
    fun updateProfileTest(){
        profileInteractor.onSurnameChanged(SURNAME)
        assertEquals(SURNAME,profileViewState.surname)

        profileInteractor.onNameChanged(NAME)
        assertEquals(NAME,profileViewState.name)

        profileInteractor.onBirthdayChanged(BIRTHDATESTR,true)
        assertEquals(BIRTHDATE, profileViewState.birthday)

        profileInteractor.onAgentCodeChanged(AGENTCODE)
        assertEquals(AGENTCODE, profileViewState.agentCode)

        profileInteractor.onAgentPhoneChanged(PHONEAGENT, true)
        assertEquals(PHONEAGENT, profileViewState.phone)

        profileInteractor.onGenderSelected(GENDER)
        assertEquals(GENDER, profileViewState.gender)

        assertEquals(SURNAME,profileViewState.surname)
        assertEquals(NAME,profileViewState.name)
        assertEquals(BIRTHDATE, profileViewState.birthday)
        assertEquals(AGENTCODE, profileViewState.agentCode)
        assertEquals(PHONEAGENT, profileViewState.phone)
        assertEquals(GENDER, profileViewState.gender)
    }
}