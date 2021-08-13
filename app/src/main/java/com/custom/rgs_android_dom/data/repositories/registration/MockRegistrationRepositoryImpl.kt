package com.custom.rgs_android_dom.data.repositories.registration

/*class MockRegistrationRepositoryImpl(private val api: MSDApi) : RegistrationRepository {

    private val logout = BehaviorRelay.create<Unit>()

    override fun login(phone: String, code: String, token: String): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            if (phone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
     }

    override fun getCode(phone: String): Single<String> {
        Thread.sleep(2000)
        return if (phone.endsWith("9")){
            throw InvalidPropertiesFormatException("Wrong format")
        } else {
            Single.fromCallable {
                "11111"
            }
        }
    }

   override fun signOpd(clientId: String): Completable {
       return Completable.fromAction {
           Thread.sleep(2000)
       }
    }

    override fun getAuthToken(): String? {
        return ""
    }

    override fun updateProfile(
        phone: String,
        name: String?,
        surname: String?,
        birthday: LocalDate?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?
    ): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(3000)
            val agentPhone = agentPhone
            if (agentPhone != null && agentPhone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
    }

    override fun logout(): Completable {
        return Completable.fromCallable {
            logout.accept(Unit)
        }
        //
    }

    override fun getLogoutSubject(): BehaviorRelay<Unit> {
        return logout
    }
}*/