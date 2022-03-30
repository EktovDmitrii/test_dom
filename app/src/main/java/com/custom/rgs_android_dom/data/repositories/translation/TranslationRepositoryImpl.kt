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
            "app.phone.title" to "Вход и регистрация",
            "app.phone.field_title" to "Номер телефона",
            "app.phone.button_title" to "Далее",
            "app.phone.field_description" to "Введите номер телефона — получите доступ\\nк вашему профилю и полному функционалу\\nприложения",

            // Country select
            "app.country.title" to "Выберите страну",
            "app.country.search_placeholder" to "Поиск страны",
            "app.country.cancel_search" to "Отменить",
            "app.country.empty_state" to "Такой страны нет в списке. Пожалуйста,\nпроверьте запрос и попробуйте ещё раз.",
            "app.country.button_title" to "Далее",

            // OTP Code
            "app.registration.code.title" to "Введите код из СМС",
            "app.registration.code.phone_subtitle" to "Мы отправили СМС на номер",
            "app.registration.code.timer_subtitle" to "Вы сможете повторно запросить\nкод через %@",
            "app.registration.code.resend_text" to "Отправить код повторно",
            "app.registration.code.button_title" to "Далее",

            // Agreements
            "app.registration.agreement.title" to "Обработка данных",
            "app.registration.agreement.first_text" to "Я принимаю условия ",
            "app.registration.agreement.second_text" to " и даю своё ",
            "app.registration.agreement.first_link" to "пользовательского соглашения, ",
            "app.registration.agreement.second_link" to "политику обработки персональных данных",
            "app.registration.agreement.third_link" to "согласие на обработку персональных данных",
            "app.registration.agreement.button_title" to "Далее",

            // Registration
            "app.profile.add.header.title" to "Расскажите о себе",
            "app.profile.add.lastname.title" to "Фамилия",
            "app.profile.add.lastname.placeholder" to "Иванов",
            "app.profile.add.firstname.title" to "Имя",
            "app.profile.add.firstname.placeholder" to "Иван",
            "app.profile.add.birthday.title" to "Дата рождения",
            "app.profile.add.birthday.placeholder" to "25.06.1990",
            "app.profile.add.birthday.validation_error" to "Проверьте, правильно ли введена дата рождения",
            "app.profile.add.sex.title" to "Пол",
            "app.profile.add.sex.male" to "Мужчина",
            "app.profile.add.sex.female" to "Женщина",
            "app.profile.add.agent_info.collapsed_title" to "Знаю код агента",
            "app.profile.add.agent_info.expanded_title" to "Свернуть информацию об агенте",
            "app.profile.add.agent_info.lnr_title" to "Введите код агента",
            "app.profile.add.agent_info.phone_title" to "Введите номер телефона агента",
            "app.profile.add.agent_info.phone_validation_error" to "Проверьте, правильно ли введен номер телефона",
            "app.profile.add.buttons.skip_title" to "Пропустить",
            "app.profile.add.buttons.save_title" to "Сохранить",
            "app.profile.add.lastname.validation_error" to "Проверьте, правильно ли введена фамилия",
            "app.profile.add.firstname.validation_error" to "Проверьте, правильно ли введено имя",
            "app.profile.add.birthday.placeholder" to "24.04.1990",
            "app.profile.add.agent_info.placeholder" to "999999",
            "app.profile.add.agent_info.code_skip" to "Введите код агента, если он вам известен\nЭтот шаг можно пропустить",
            "app.profile.add.agent_info.phone.placeholder" to "+7 999 999-99-99",
            "app.profile.add.agent_info.code_validation_error" to "Проверьте, правильно ли введен код агента",
            "app.profile.add.agent_info.phone_validation_error" to "Проверьте, правильно ли введен номер телефона",

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

            //Calls
            "app.chats.chat.call.connecting" to "Подключаемся...",
            "app.chats.chat.call.waiting_operator" to "Ищем свободного оператора",
            "app.chats.chat.call.connection_error" to "Проблемы со связью",
            "app.chats.chat.call.call_ended" to "Завершён",
            "app.chats.chat.call.online_master" to "Онлайн мастер",

            //Payment methods
            "app.client.payment_methods.title" to "Способы оплаты",
            "app.client.payment_methods.title_edit" to "Редактирование карт",
            "app.client.payment_methods.delete_card" to "Удалить карту",
            "app.client.payment_methods.card_deleted" to "Карта удалена",
            "app.client.payment_methods.no_linked_cards" to "У вас пока нет привязанных карт",
            "app.client.payment_methods.error.error_code" to "Код ошибки: %@",
            "app.client.payment_methods.error.title" to "Невозможно обработать",
            "app.client.payment_methods.error.subtitle" to "Мы не смогли обработать ваш запрос.\nПожалуйста, попробуйте еще раз",
            "app.client.payment_methods.error.try_again" to "Попробовать еще раз",
            "app.client.payment_methods.error.contact_online_master" to "Написать онлайн мастеру",
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