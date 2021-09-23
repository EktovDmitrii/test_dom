package com.custom.rgs_android_dom.data.repositories.translation

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.responses.TranslationResponse
import com.custom.rgs_android_dom.domain.repositories.TranslationRepository
import io.reactivex.Completable
import io.reactivex.Single

class TranslationRepositoryImpl(private val api: MSDApi) :
    TranslationRepository {

    companion object {
        const val PLATFORM = "mobile"
        const val LANG = "ru"
        const val PROJECT = "auto"

        private val translations = HashMap<String, String>()

        fun getTranslate(key: String): String {
            return translations[key] ?: defaultValues[key] ?: key
        }

        private val defaultValues = mapOf<String, String>(
            // PhoneLogin
            "app.phone_login.title" to "Вход и регистрация",
            "app.phone_login.field_title" to "Номер телефона",
            "app.phone_login.button_title" to "Далее",

            // Country select
            "app.country_select.title" to "Выберите страну",
            "app.country_select.search_placeholder" to "Поиск страны",
            "app.country_select.cancel_search" to "Отменить",
            "app.country_select.empty_state" to "Такой страны нет в списке. Пожалуйста,\nпроверьте запрос и попробуйте ещё раз.",
            "app.country_select.button_title" to "Далее",

            // OTP Code
            "app.otp_code.title" to "Введите код из СМС",
            "app.otp_code.phone_subtitle" to "Мы отправили СМС на номер",
            "app.otp_code.timer_subtitle" to "Вы сможете повторно запросить\nкод через %@",
            "app.otp_code.resend_text" to "Отправить код повторно",
            "app.otp_code.button_title" to "Далее",

            // Agreements
            "app.agreements.title" to "Обработка данных",
            "app.agreements.first_text" to "Я принимаю условия ",
            "app.agreements.second_text" to " и даю своё ",
            "app.agreements.first_link" to "пользовательского соглашения, ",
            "app.agreements.second_link" to "политику обработки персональных данных",
            "app.agreements.third_link" to "согласие на обработку персональных данных",
            "app.agreements.button_title" to "Далее",

            // Registration
            "app.registration_info.header.title" to "Расскажите о себе",
            "app.registration_info.lastname.title" to "Фамилия",
            "app.registration_info.lastname.placeholder" to "Иванов",
            "app.registration_info.firstname.title" to "Имя",
            "app.registration_info.firstname.placeholder" to "Иван",
            "app.registration_info.birthday.title" to "дата рождения",
            "app.registration_info.birthday.placeholder" to "25.06.1990",
            "app.registration_info.birthday.validation_error" to "Проверьте, правильно ли введена дата рождения",
            "app.registration_info.sex.title" to "Пол",
            "app.registration_info.sex.male" to "Мужчина",
            "app.registration_info.sex.female" to "Женщина",
            "app.registration_info.agent_info.collapsed_title" to "Знаю код агента",
            "app.registration_info.agent_info.expanded_title" to "Свернуть информацию об агенте",
            "app.registration_info.agent_info.lnr_title" to "Введите код агента",
            "app.registration_info.agent_info.phone_title" to "Введите номер телефона агента",
            "app.registration_info.agent_info.phone_validation_error" to "Проверьте, правильно ли введен номер телефона",
            "app.registration_info.buttons.skip_title" to "Пропустить",
            "app.registration_info.buttons.save_title" to "Сохранить",

            // Profile
            "app.profile.slider.slide_tip_text" to "Потяните, чтобы связаться с мастером",
            "app.profile.header.title" to "Мои объекты",
            "app.profile.header.add" to "Добавить",
            "app.profile.menu.name_placeholder" to "Добавьте ваше имя",
            "app.profile.menu.agent_info" to "Данные об агенте",
            "app.profile.menu.about_app" to "О приложении",
            "app.profile.menu.logout" to "Выйти",

            // Agent info
            "app.agent_info.title" to "Данные агента",
            "app.agent_info.lnr_title" to "Код агента",
            "app.agent_info.lnr_empty" to "Нет данных",
            "app.agent_info.phone_title" to "Номер телефона",
            "app.agent_info.phone_empty" to "Нет данных",

            // Agent info editing
            "app.edit_agent_info.title" to "Редактировать данные",
            "app.edit_agent_info.lnr_title" to "Код агента",
            "app.edit_agent_info.phone_title" to "Номер телефона",
            "app.edit_agent_info.button_title" to "Сохранить",

            // About app
            "app.about_app.title" to "О приложении",
            "app.about_app.subtitle" to "Удобный сервис для домашних дел от флагмана отечественного рынка страхования",
            "app.about_app.menu.rate" to "Оценить приложение",
            "app.about_app.menu.feedback" to "Отправить отзыв",
            "app.about_app.menu.license_agreements" to "Лицензионные соглашения",
            "app.about_app.menu.personal_agreements" to "Политика конфиденциальности",
            "app.about_app.footer.version" to "Версия %@",
            "app.about_app.footer.build" to "Сборка %@",

            // Personal info
            "app.personal_info.title" to "Личные данные",
            "app.personal_info.photo.title" to "Фотографии",

            "app.personal_info.photo.edit" to "Редактировать фотографию",
            "app.personal_info.photo.info" to "Консультантам и специалистам будет приятно видеть, с кем они ведут диалог в чате",
            "app.personal_info.name.title" to "ФИО",
            "app.personal_info.birthday.title" to "Дата рождения",
            "app.personal_info.sex.title" to "Пол",
            "app.personal_info.passport.title" to "Серия и номер паспорта",
            "app.personal_info.passport_series.title" to "Серия паспорта",
            "app.personal_info.passport_number.title" to "Номер паспорта",
            "app.personal_info.phone.title" to "Номер телефона",
            "app.personal_info.phone.placeholder" to "+7 999 999-99-99",
            "app.personal_info.second_phone.title" to "Дополнительный номер",
            "app.personal_info.second_phone.placeholder" to "+7 999 999-99-99",
            "app.personal_info.email.title" to "Электронная почта",
            "app.personal_info.email.placeholder" to "mail@mail.com",
        )
    }

    override fun loadAndSaveTranslations(): Completable {
        return getTranslationsFromNetwork()
            .map {
                it.forEach {
                    translations[it.key] = it.value
                }
            }.flatMapCompletable {
                Completable.complete()
            }
    }

    override fun getTranslate(key: String): String {
        return translations[key] ?: defaultValues[key] ?: key
    }

    private fun getTranslationsFromNetwork(): Single<List<TranslationResponse>> {
        return api.getTranslations(PLATFORM, LANG, PROJECT)
            .map { it.list }
    }

}