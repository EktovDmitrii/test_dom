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
            "app.profile.client.photo.title" to "Фотография",
            "app.profile.client.photo.edit" to "Редактировать фотографию",
            "app.profile.client.photo.empty_title" to "Консультантам и специалистам будет приятно\nвидеть, с кем они ведут диалог в чате",
            "app.profile.client.photo.load_error" to "Ошибка загрузки аватарки",
            "app.profile.client.name.title" to "ФИО",
            "app.profile.client.name.empty_title" to "Нет данных",
            "app.profile.client.pasport.title" to "Серия и номер паспорта",
            "app.profile.client.additional_phone.title" to "Дополнительный телефон",
            "app.profile.client.email.title" to "Электронная почта",
            "app.profile.client.title" to "Личные данные",
            "app.profile.client.edit_request.title" to "Чтобы редактировать личные данные,",
            "app.profile.client.edit_request.button_title" to "оставьте заявку",
            "app.profile.client.edit_request.success" to "Заявка на редактирование данных будет рассмотрена",
            "app.profile.client.passport_series.title" to "Серия паспорта",
            "app.profile.client.passport_number.title" to "Номер паспорта",
            "app.profile.client.phone.title" to "Номер телефона",
            "app.profile.client.email.placeholder" to "mail@mail.com",

            // Profile
            "app.profile.slider.slide_tip_text" to "Потяните вниз, чтобы открыть",
            "app.profile.header.title" to "Моя недвижимость",
            "app.profile.header.add" to "Добавить",
            "app.profile.menu.name_placeholder" to "Добавьте ваше имя",
            "app.profile.menu.orders" to "История заказов",
            "app.profile.menu.policies" to "Полисы",
            "app.profile.menu.agent_info" to "Данные об агенте",
            "app.profile.menu.agent_code" to "Я знаю код агента",
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
            "app.about_app.subtitle" to "Удобный сервис для домашних дел от флагмана\nотечественного рынка страхования",
            "app.about_app.menu.rate" to "Оценить приложение",
            "app.about_app.menu.feedback" to "Обратная связь",
            "app.about_app.menu.license_agreements" to "Пользовательское соглашение",
            "app.about_app.menu.personal_agreements" to "Политика конфиденциальности",
            "app.about_app.footer.version" to "Версия %@",
            "app.about_app.footer.build" to "Сборка %@",

            //Calls
            "app.chats.chat.call.connecting" to "Подключаемся...",
            "app.chats.chat.call.waiting_operator" to "Ищем свободного оператора",
            "app.chats.chat.call.connection_error" to "Проблемы со связью",
            "app.chats.chat.call.call_ended" to "Завершён",
            "app.chats.chat.call.online_master" to "Онлайн мастер",

            //Catalog
            "app.catalog.main_catalog" to "Все",
            "app.catalog.products" to "Мои продукты",
            "app.catalog.available_services" to "Доступные услуги",

            //Catalog Search
            "app.catalog.search.empty_title" to "Не смогли что-то найти?",
            "app.catalog.search.empty_subtitle" to "Попробуйте новый поиск или напишите Онлайн Мастеру, мы поможем подобрать нужный сервис под любой запрос!",
            "app.catalog.search.emptyState" to "Чат с Онлайн Мастером",
            "app.catalog.search.popular_header" to "Популярные",
            "app.catalog.search.cancel" to "Отменить",
            "app.catalog.search.placeholder" to "Чем вам помочь?",
            "app.catalog.search.title" to "Поиск по каталогу услуг",

            //Catalog Subcategory
            "app.catalog.subcategory.other" to "Прочее",

            //Catalog Tabs
            "app.catalog.tabs.title" to "Каталог",
            "app.catalog.tabs.all" to "Все",
            "app.catalog.tabs.all_services" to "Все услуги в категории",
            "app.catalog.tabs.my_products_empty_state" to "У вас пока нет продуктов. Вы можете\nпривязать существующий продукт",
            "app.catalog.tabs.my_products_connect_policy" to "Привязать полис",
            "app.catalog.tabs.my_services_empty_state" to "У вас пока нет доступных услуг.\nВы можете купить услуги в составе\nкомплексных продуктов",
            "app.catalog.tabs.my_services_navigate_catalog" to "Перейти в каталог услуг",

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
            "app.client.payment_methods.cancel" to "Отмена",

            //Property
            "app.object.main.actions.add.err_file_limit" to "Размер файла больше 10 mb",

            // Property (address suggestions)
            "app.address.sugestion.input.placeholder" to "Адрес дома",
            "app.address.sugestion.empty_title" to "Ничего не найдено",

            //Property (info)
            "app.object.main.info.title" to "Информация",
            "app.object.main.info.adress.title" to "Адрес",
            "app.object.main.info.type.title" to "Тип объекта",
            "app.object.main.info.is_own.title" to "В собственности",
            "app.object.main.info.is_rent.title" to "Сдается в аренду",
            "app.object.main.info.is_temporary.title" to "Временное проживание",
            "app.object.main.info.area.title" to "Общая площадь",
            "app.object.main.info.comment.title" to "Комментарий",
            "app.object.main.actions.add" to "Добавить объект",
            "app.object.main.actions.edit" to "Редактировать объект",
            "app.object.main.actions.delete" to "Удалить объект",
            "app.object.main.actions.load_photo" to "Загрузить фотографию",
            "app.object.main.actions.cancel" to "Отмена",
            "app.object.main.info.documents.title" to "Документы",
            "app.object.main.info.documents.title_all" to "Все",
            "app.object.main.info.property_type.house" to "Дом",
            "app.object.main.info.property_type.appartment" to "Квартира",
            "app.object.main.info.yes" to "Да",
            "app.object.main.info.no" to "Нет",
            "app.object.main.actions.add.photo" to "Фото",
            "app.object.main.actions.add.file" to "Файл",

            // Add Property
            "app.object.add.step_geo.name.help_title" to "Нажмите, чтобы редактировать название",
            "app.object.add.step_geo.name.title" to "Название объекта",
            "app.object.add.step_geo.address.edit" to "Ред.",
            "app.object.add.step_geo.buttons.next" to "Далее",
            "app.object.add.step_geo.header.title" to "Шаг 1 из 3",
            "app.object.add.step_geo.header.subtitle" to "Адрес",
            "app.object.add.step_type.header.title" to "Шаг 2 из 3",
            "app.object.add.step_type.header.subtitle" to "Тип недвижимости",
            "app.object.add.step_type.buttons.next" to "Далее",
            "app.object.add.step_type.house.title" to "Дом",
            "app.object.add.step_type.house.tip" to "Если хотите добавить частный дом, загородный дом, дачу или таунхаус, выберите Дом.",
            "app.object.add.step_type.flat.title" to "Квартира",
            "app.object.add.step_type.flat.tip" to "Если хотите добавить квартиру, часть квартиры или этаж во владении в частном доме, выберите Квартиру.",
            "app.object.add.step_info.header.title" to "Шаг 3 из 3",
            "app.object.add.step_info.header.subtitle" to "Общая информация",
            "app.object.add.step_info.address.separator.title" to "Адрес",
            "app.object.add.step_info.address.title" to "Адрес",
            "app.object.add.step_info.address.approach.title" to "Подъезд",
            "app.object.add.step_info.address.approach.placeholder" to "Подъезд",
            "app.object.add.step_info.address.corpus.title" to "Корпус",
            "app.object.add.step_info.address.corpus.placeholder" to "Корпус",
            "app.object.add.step_info.address.floor.title" to "Этаж",
            "app.object.add.step_info.address.floor.placeholder" to "Этаж",
            "app.object.add.step_info.address.flat.title" to "Квартира",
            "app.object.add.step_info.address.flat.placeholder" to "Квартира",
            "app.object.add.step_info.address.locality.title" to "Населённый пункт",
            "app.object.add.step_info.address.locality.placeholder" to "Населённый пункт",
            "app.object.add.step_info.object.separator.title" to "Информация об объекте",
            "app.object.add.step_info.object.square.title" to "Площадь (м²)",
            "app.object.add.step_info.object.square.placeholder" to "Площадь (м²)",
            "app.object.add.step_info.object.questions.object_is_owned" to "Объект в собственности?",
            "app.object.add.step_info.object.questions.for_rent" to "Сдаётся в аренду?",
            "app.object.add.step_info.object.questions.temporary_residence" to "Временное проживание?",
            "app.object.add.step_info.object.questions.info" to "Понадобится для формирования страхового полиса",
            "app.object.add.step_info.object.questions.yes" to "Да",
            "app.object.add.step_info.object.questions.no" to "Нет",
            "app.object.add.step_info.additional.separator.title" to "Дополнительно",
            "app.object.add.step_info.additional.comment.placeholder" to "Добавить комментарий",
            "app.object.add.step_info.additional.documents.placeholder" to "Добавить документы",
            "app.object.add.step_info.buttons.next" to "Добавить объект",
            "app.object.add.step_setting.button.to_settings" to "Перейти в настройки",
            "app.object.add.step_setting.title" to "Разрешите доступ к вашей\nгеопозиции, чтобы нам проще\nбыло вас найти и помочь",
            "app.object.add.step_cancel.question.title" to "Хотите выйти?",
            "app.object.add.step_cancel.question.subtitle" to "Если вы покинете страницу сейчас,\nданные об объекте недвижимости\nне сохранятся",
            "app.object.add.step_cancel.buttons.confirm" to "Да, выйти",
            "app.object.add.step_cancel.buttons.confirm_decline" to "Нет, остаться",

            // Property Document
            "app.document.refresh_documents" to "Перезагрузить документы",
            "app.document.edit_name" to "Редактировать название",
            "app.document.remove_document" to "Удалить документ",
            "app.document.add_document" to "Добавить документ",
            "app.document.edit_list" to "Редактировать список",
            "app.document.documents" to "Документы",
            "app.document.delete_document_question" to "Удалить документ?",
            "app.document.document_description" to "Документ будет удален безвозвратно. В случае необходимости вы сможете загрузить его заново",
            "app.document.yes_delete" to "Да, удалить",
            "app.document.no_stay" to "Нет, оставить",

            //System request
            "app.alert.call_request.audio" to "Разрешите доступ, чтобы консультант или мастер могли слышать вас",
            "app.alert.call_request.camera" to "Разрешите доступ, чтобы консультант или мастер могли видеть вас",

            //Notifications settigns
            "app.profile.notifications_settings.title" to "Настройки уведомлений",
            "app.profile.notifications_settings.items.sms" to "SMS",
            "app.profile.notifications_settings.items.push" to "Push-уведомления",

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