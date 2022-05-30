package com.custom.rgs_android_dom.data.repositories.translation

object EnglishLocaleClass {

    val defaultEnglishValues = mapOf<String, String>(
        // PhoneLogin
        "app.phone.title" to "Registration",
        "app.phone.field_title" to "Phone number",
        "app.phone.button_title" to "Next",

        // Country select
        "app.country.title" to "Choose your country",
        "app.country.search_placeholder" to "Searching your country",
        "app.country.cancel_search" to "Cancel",
        "app.country.empty_state" to "There is no such country in ourlist.\nPlease, try again.",
        "app.country.button_title" to "Next",

        // OTP Code
        "app.registration.code.title" to "Please, enter the code",
        "app.registration.code.phone_subtitle" to "We've sent the code to the",
        "app.registration.code.timer_subtitle" to "You can request another\ncode in %@",
        "app.registration.code.resend_text" to "Send another code",
        "app.registration.code.button_title" to "Next",

        // Agreements
        "app.registration.agreement.title" to "Data processing",
        "app.registration.agreement.first_text" to "I accept the terms of ",
        "app.registration.agreement.second_text" to " and giving my",
        "app.registration.agreement.first_link" to "user agreement,",
        "app.registration.agreement.second_link" to "terms of service",
        "app.registration.agreement.third_link" to "consent for personal data processing",
        "app.registration.agreement.button_title" to "Next",

        // Registration
        "app.profile.add.header.title" to "Please, tell us about yourself",
        "app.profile.add.lastname.title" to "Last Name",
        "app.profile.add.lastname.placeholder" to "Smith",
        "app.profile.add.lastname.validation_error" to "Please, check that the last name is correct",
        "app.profile.add.firstname.title" to "Name",
        "app.profile.add.firstname.placeholder" to "Jack",
        "app.profile.add.firstname.validation_error" to "Please, check that the name is correct",
        "app.profile.add.birthday.title" to "Date of birth",
        "app.profile.add.birthday.placeholder" to "24.04.1990",
        "app.profile.add.birthday.validation_error" to "Please, check that the date is correct",
        "app.profile.add.sex.title" to "Sex",
        "app.profile.add.sex.male" to "Male",
        "app.profile.add.sex.female" to "Female",
        "app.profile.add.agent_info.button_title" to "I know the agent code",
        "app.profile.add.agent_info.collapsed_title" to "I know the agent code",
        "app.profile.add.agent_info.expanded_title" to "Hide the agent informantion",
        "app.profile.add.agent_info.lnr_title" to "Please, enter the agent code",
        "app.profile.add.agent_info.placeholder" to "999999",
        "app.profile.add.agent_info.phone_title" to "Enter your agent phone number",
        "app.profile.add.agent_info.code_skip" to "Enter your agent code, if you know it",
        "app.profile.add.agent_info.phone.placeholder" to "+7 999 999-99-99",
        "app.profile.add.agent_info.phone_validation_error" to "Please, check the agent phone again",
        "app.profile.add.agent_info.code_validation_error" to "Please, check the agent code again",
        "app.profile.add.buttons.skip_title" to "Skip",
        "app.profile.add.buttons.save_title" to "Save",
        "app.profile.client.photo.title" to "Your photo",
        "app.profile.client.photo.edit" to "Edit the photo",
        "app.profile.client.photo.empty_title" to "Our consultants would be glad\nto see who they are talking to",
        "app.profile.client.photo.load_error" to "Photo upload error",
        "app.profile.client.name.title" to "Your name",
        "app.profile.client.name.empty_title" to "No data",
        "app.profile.client.birthday.title" to "Date of birth",
        "app.profile.client.birthday.empty_title" to "No data",
        "app.profile.client.sex.title" to "Sex",//"Пол",
        "app.profile.client.sex.empty_title" to "No data",
        "app.profile.client.pasport.title" to "Passport No:",
        "app.profile.client.pasport.empty_title" to "No data",
        "app.profile.client.phone.title" to "Phone number",
        "app.profile.client.phone.empty_title" to "No data",
        "app.profile.client.additional_phone.title" to "Additional number",
        "app.profile.client.additional_phone.empty_title" to "No data",
        "app.profile.client.email.title" to "E-mail",
        "app.profile.client.email.empty_title" to "No data",
        "app.profile.client.title" to "Personal info",

        // Profile
        "app.profile.slider.slide_tip_text" to "Pull to contact the master",
        "app.profile.header.title" to "My objects",
        "app.profile.header.add" to "Append",
        "app.profile.menu.name_placeholder" to "Your name",
        "app.profile.menu.agent_info" to "Agent's info",
        "app.profile.menu.about_app" to "About us",
        "app.profile.menu.logout" to "Logout",

        // Agent info
        "app.agent_info.title" to "Agent's info",//"Данные агента",
        "app.agent_info.lnr_title" to "Agent code",//"Код агента",
        "app.agent_info.lnr_empty" to "No data",//"Нет данных",
        "app.agent_info.phone_title" to "Agent phone number",
        "app.agent_info.phone_empty" to "No data",//"Нет данных",
        "app.agent_info.request_text_start" to "Please ",
        "app.agent_info.request_text_middle" to "leave a request",
        "app.agent_info.request_text_end" to ", to change\nthe agent's code and phone number.",

        // Agent info editing
        "app.edit_agent_info.title" to "Edit data",
        "app.edit_agent_info.lnr_title" to "Agent code",//"Код агента",
        "app.edit_agent_info.phone_title" to "Agent phone number",
        "app.edit_agent_info.button_title" to "Save",//"Сохранить",

        // About app
        "app.about_app.title" to "About us",
        "app.about_app.subtitle" to "My_Service DOM - housing app\nfrom the leading insurance\ncompany",
        "app.about_app.menu.rate" to "Rate us",
        "app.about_app.menu.feedback" to "Give a feedback",
        "app.about_app.menu.license_agreements" to "Licensing agreements",
        "app.about_app.menu.personal_agreements" to "Terms of service",
        "app.about_app.footer.version" to "Version %@",//"Версия %@",
        "app.about_app.footer.build" to "Build $@",//"Сборка %@",

        // Personal info
        "app.personal_info.title" to "Personal info",//"Личные данные",
        "app.personal_info.photo.title" to "Photos",//"Фотографии",
        "app.personal_info.photo.edit" to "Edit photo",
        "app.personal_info.photo.info" to "Consultants and specialists will be pleased to see who they are having a chat with",
        "app.personal_info.name.title" to "Name",//"ФИО",
        "app.personal_info.birthday.title" to "Date of birth",
        "app.personal_info.sex.title" to "Sex",//"Пол",
        "app.personal_info.passport.title" to "Passport No:",
        "app.personal_info.phone.title" to "Phone number",
        "app.personal_info.second_phone.title" to "Additional phone",
        "app.personal_info.email.title" to "E-mail",

        // Chats
        "app.chat.chat_list.title" to "Chats",//"Чаты",
        "app.chat.chat_list.archive_delimeter_title" to
        "History",//"Архив",
        "app.chat.chat_screen.bottom_archive_label_title" to "Chat archieved",
        "app.chat.chat_list.case_substatus_solved" to "Done",//"Выполнен",
        "app.chat.chat_list.case_substatus_cancelled" to
        "Cancelled",//"Отменен",
         "app.chat.chat_list.case_substatus_deleted" to "Deleted",//"Удален",

        // Call
        "app.call.incoming.title" to "Incoming call",

        // Catalog
        "app.catalog.main_catalog" to "All",//"Все",
        "app.catalog.products" to "My products",//"Мои продукты",
        "app.catalog.available_services" to "Available services",//"Доступные услуги",
        "app.catalog.subcategory.other" to "Other",//"Прочее",
        "app.catalog.search.empty_title" to "No results",
        "app.catalog.search.empty_subtitle" to "We could not find anything for you",
        "app.catalog.search.emptyState" to "Chat with online",
        "app.catalog.search.popular_header" to "Popular",//"Популярные",
        "app.catalog.search.placeholder" to "How can I help you?",

        // Поиск адреса
        "app.address.sugestion.input.placeholder" to "Hounsing address",//"Адрес дома",
        "app.address.sugestion.empty_title" to "No data",

        // Добалена недвижимости
        "app.object.add.step_geo.name.help_title" to "Tap, to edit the name",//"Нажмите, чтобы редактировать название",
        "app.object.add.step_geo.name.title" to "Object name",//"Название объекта",
        "app.object.add.step_geo.address.edit" to "Edit",//"Ред.",
        "app.object.add.step_geo.buttons.next" to "Next",//"Далее",
        "app.object.add.step_geo.header.title" to "Step 1 of 3",
        "app.object.add.step_geo.header.subtitle" to "Address",//"Адрес",
        "app.object.add.step_type.header.title" to "Step 2 of 3",
        "app.object.add.step_type.header.subtitle" to "Type of object",
        "app.object.add.step_type.buttons.next" to "Next",//"Далее",
        "app.object.add.step_type.house.title" to "House",//"Дом",
        "app.object.add.step_type.house.tip" to "If you want to add a private house, a cottage or a townhouse, please chooseHouse",
        "app.object.add.step_type.flat.title" to "Flat",//"Квартира",
        "app.object.add.step_type.flat.tip" to "If you want to add a flat, a part of the flat, or an apartment, please choose Flat",
        "app.object.add.step_info.header.title" to "Step 3 of 3",
        "app.object.add.step_info.header.subtitle" to "General information",
        "app.object.add.step_info.address.separator.title" to
        "Address",//"Адрес",
        "app.object.add.step_info.address.title" to "Address",//"Адрес",
        "app.object.add.step_info.address.approach.title" to
        "Approach",//"Подъезд",
        "app.object.add.step_info.address.approach.placeholder" to
        "Approach",//"Подъезд",
        "app.object.add.step_info.address.corpus.title" to
        "Korpus",//"Корпус",
        "app.object.add.step_info.address.corpus.placeholder" to
        "Korpus",//"Корпус",
        "app.object.add.step_info.address.floor.title" to
        "Floor",//"Этаж",
        "app.object.add.step_info.address.floor.placeholder" to
        "Floor",//"Этаж",
        "app.object.add.step_info.address.flat.title" to
        "Flat",//"Квартира",
        "app.object.add.step_info.address.flat.placeholder" to
        "Flat",//"Квартира",
        "app.object.add.step_info.address.locality.title" to
        "Town",//"Населённый пункт",
        "app.object.add.step_info.address.locality.placeholder" to
        "Town",//"Населённый пункт",
        "app.object.add.step_info.object.separator.title" to "Object information",//"Информация об объекте",
        "app.object.add.step_info.object.square.title" to "Object size (м\\u{00B2})",//"Площадь (м\u{00B2})",
        "app.object.add.step_info.object.square.placeholder" to "Object size (м\\u{00B2})",//"Площадь (м\u{00B2})",
        "app.object.add.step_info.object.questions.object_is_owned" to
        "Do you own the object?",//"Недвижимость в собственности?",
        "app.object.add.step_info.object.questions.for_rent" to "Is the object rented?",//"Сдаётся в аренду?",
        "app.object.add.step_info.object.questions.temporary_residence" to "Is it your temporary residence?",//"Временное проживание?",
        "app.object.add.step_info.object.questions.info" to "Is needed to form the insurance",
        "app.object.add.step_info.object.questions.yes" to "Yes",
        "app.object.add.step_info.object.questions.no" to "No",//"Нет",
        "app.object.add.step_info.additional.separator.title" to
        "Additional",//"Дополнительно",
        "app.object.add.step_info.additional.comment.placeholder" to
        "Add a comment",//"Добавить комментарий",
        "app.object.add.step_info.additional.documents.placeholder" to
        "Add documents",//"Добавить документы",
        "app.object.add.step_info.buttons.next" to "Add an object",//"Добавить объект",
        "app.object.add.step_setting.button.to_settings" to "To Settings",//"Перейти в настройки",
        "app.object.add.step_setting.title" to "Allow the access to you\nposition so we may help\nyou better?",
        "app.object.add.step_cancel.question.title" to "Do you want to exit?",//"Хотите выйти?",
        "app.object.add.step_cancel.question.subtitle" to "If you exit now, your\nobject details won't be saved",
        "app.object.add.step_cancel.buttons.confirm" to "Yes,exit",//"Да, выйти",
        "app.object.add.step_cancel.buttons.cancel" to "No, stay",//"Нет, остаться",
        "app.object.main.info.title" to "Information",//"Информация",
        "app.object.main.info.adress.title" to "Address",//"Адрес",
        "app.object.main.info.type.title" to "Type of object",
        "app.object.main.info.is_own.title" to "In ownership",
        "app.object.main.info.is_rent.title" to "Rented",
        "app.object.main.info.is_temporary.title" to "Temporary residence",//"Временное проживание",
        "app.object.main.info.area.title" to "Total size",
        "app.object.main.info.comment.title" to
        "Comment",//"Комментарий",
        "app.object.main.actions.add" to "Add an object",
        "app.object.main.actions.edit" to "Edit an object",//"Редактировать недвижимость",
        "app.object.main.actions.delete" to "Delete an object",//"Удалить недвижимость",
        "app.object.main.actions.load_photo" to "Upload a photo",//"Загрузить фотографию",
        "app.object.main.actions.cancel" to "Cancel",//"Отмена",

        // Mainscreen
        "app.main.chat_state.defaultName" to "Online master",
        "app.main.chat_state.connectingTopTitle" to
        "Connecting…",//"Подключаемся...",
        "app.main.chat_state.connectingBottomTitle" to "Searching for a suitable operator",//"Ищем свободного оператора",
        "app.main.chat_state.finishBottomTitle" to
        "Finished",//"Завершен",

        // Policies
        "app.policies.archive.title" to "Archive",//"Архив",

        // Cards
        "app.client.payment_methods.title" to "Payment methods",//"Способы оплаты",
        "app.client.payment_methods.title_edit" to "Card's editing",//"Редактирование карт",
        "app.client.payment_methods.delete_card" to "Remove card",//"Удалить карту",
        "app.client.payment_methods.card_deleted" to "The card has been deleted",//"Карта удалена",
        "app.client.payment_methods.no_linked_cards" to "You don't have any linked cards yet",//"У вас пока нет привязанных карт",
        "app.client.payment_methods.error.error_code" to "Error code %@",
        "app.client.payment_methods.error.title" to "Unable to process",//"Невозможно обработать",
        "app.client.payment_methods.error.subtitle" to "We are unable to process your request.\nPlease try again",
        "app.client.payment_methods.error.try_again" to
        "Try",//"Попробовать еще раз",
        "app.client.payment_methods.error.contact_online_master" to
        "Connect to the online master",//"Написать онлайн мастеру",

        "app.alerts.default_error_title" to "Error",//"Ошибка",
// Home
        "app.main.top_title.online" to "Online",//"Онлайн",
        "app.main.top_title.master" to "Master",//"Мастер",
        "app.main.top_title.subtitle" to "Pull down to open",
        "app.main.container_view.bottom_bar.profile_tab_title" to
        "Profile",//"Профиль",
        "app.main.container_view.bottom_bar.login_tab_title" to
        "Login",//"Войти",
        "app.main.container_view.bottom_bar.home_tab_title" to
        "Home",//"Главная",
        "app.main.container_view.bottom_bar.catalog_tab_title" to
        "Catalog",//"Каталог",
        "app.main.container_view.bottom_bar.chats_tab_title" to
        "Chats",//"Чаты",

// Menu home
        "app.menu.error_view.title" to "Unable to load data",
        "app.menu.error_view.retry_title" to "Try again",
        "app.menu.home.popular_services_block.title" to "Popular services",//"Популярные услуги",
        "app.menu.home.popular_services_block.button_all.title" to
        "All",//"Все",
        "app.menu.home.popular_products_block.title" to "Popular products",//"Популярные услуги",
        "app.menu.home.popular_products_block.button_all.title" to
        "All",//"Все",
        "app.menu.home.popular_category_block.title" to "Popular categories",//"Популярные категории",
        "app.menu.home.popular_category_block.button_all.title" to
        "All",//"Все",
        "app.menu.home.about_service_block.label_title" to "My_service Dom — property service\nfrom the flagship of domestic insurance",
        "app.menu.home.about_service_block.button_title" to "About us",//"О сервисе",
        "app.menu.home.shortcut_actions_block.profile_title" to
        "Profile",//"Профиль",
        "app.menu.home.shortcut_actions_block.myhome_title" to "My home",//"Мой дом",
        "app.menu.home.shortcut_actions_block.append_title" to
        "Append",//"Добавить",
        "app.menu.home.shortcut_actions_block.login_title" to "Log in",//"Войти",
        "app.menu.home.shortcut_actions_block.sos_title" to
        "SOS",//"SOS",
        "app.menu.home.shortcut_actions_block.products_title" to
        "Products",//"Продукты",
        "app.menu.home.shortcut_actions_block.orders_title" to "Orders",//"Заказы",
        "app.menu.home.shortcut_actions_block.polices_title" to
        "Polices",//"Полисы",
        "app.menu.home.search_block.search_button_title" to "Catalog search",//"Поиск по каталогу услуг",
        "app.menu.home.benefits_screen.view_title" to "Service benefits",//"Преимущества сервиса",
        "app.menu.home.benefits_block.name.new_service_title" to
        "New\nservice",//"Новый\nсервис",
        "app.menu.home.benefits_block.name.work_garantee" to
        "Work\nguarantee",//"Гарантия\nна работу",
        "app.menu.home.benefits_block.name.support_title" to
        "24/7\nsupport",//"Поддержка\n24/7",
        "app.menu.home.benefits_block.story_name.new_service_title" to
        "New service",//"Новый сервис",
        "app.menu.home.benefits_block.story_name.work_garantee" to
        "Guarantee",//"Гарантия",
        "app.menu.home.benefits_block.story_name.support_title" to
        "Round-the-clock support",//"Круглосуточная поддержка",
        "app.menu.home.benefits_block.story_description.new_service_title" to "Consultations on any property issue, repairs, remote diagnostics, support of an Online Master 24/7 and much more in one application!",//"Консультации по любому имущественному вопросу,
        "app.menu.home.benefits_block.story_description.work_garantee" to "We guarantee the quality of our services. All our specialists are trained and tested to solve a variety of everyday problems.",
        "app.menu.home.benefits_block.story_description.support_title" to
        "Can't find some service? Need a garden consultation? A pipe burst? Just to ask? The consultant will answer any household question and offer the best solution!",
        "app.menu.home.raiting_block.view_title" to "Mastersrating",//"Рейтинг мастеров",
        "app.menu.home.raiting_block.view_subtitle" to "More than 10 thousand works,\nmade by our masters",
        "app.menu.home.raiting_block.raiting_cell.one_name" to "Sergey",//"Сергей",
        "app.menu.home.raiting_block.raiting_cell.one_description" to "Everything is very competent, fast, everything was explained according to the application.",
        "app.menu.home.raiting_block.raiting_cell.two_name" to "Arnold",//"Ханума",
        "app.menu.home.raiting_block.raiting_cell.two_description" to "Everything was organized quickly, we didn't have to wait long, the master is polite and cultured. He said he'd be back.",
        "app.menu.home.raiting_block.raiting_cell.three_name" to
        "Iren",//"Ирина",
        "app.menu.home.raiting_block.raiting_cell.three_description" to
        "We arrived on time, we did everything quickly, I liked the master very much, I am ready to recommend.",
        "app.menu.home.raiting_block.raiting_cell.four_name" to
        "Sara",//"Татьяна",
        "app.menu.home.raiting_block.raiting_cell.four_description" to "I am very pleased, everything was timely, the master was always in touch.",
        "app.menu.home.raiting_block.raiting_cell.five_name" to "Mary",//"Серафима",
        "app.menu.home.raiting_block.raiting_cell.five_description" to "The master is a good, intelligent, jack-of-all-trades master.",//"Мастер - хороший, толковый, на все руки мастер.",
        "app.menu.home.raiting_block.raiting_cell.six_name" to "Anastasia",//"Анастасия",
        "app.menu.home.raiting_block.raiting_cell.six_description" to "Everything is super quickly organized, the quality of work is at a high level, I liked everything.",
        "app.catalog.accent_node.all_title" to "All",//"Все",
        "app.catalog.default_node.all_service_in_categort_title" to "All services in the category",

// Префиксы с расширений
        "app.support_service.prefix_titles.countServiceKindText.single" to "type",//"вид",
        "app.support_service.prefix_titles.countServiceKindText.plurar" to
        "types",//"видов",
        "app.support_service.prefix_titles.countServiceKindText.from" to
        "types",//"вида",
        "app.support_service.prefix_titles.countServiceKindText.service_title" to " services",//" услуг",
        "app.support_service.prefix_titles.daysPrefix.one" to
        "day",//"день",
        "app.support_service.prefix_titles.daysPrefix.two" to
        "day",//"дня",
        "app.support_service.prefix_titles.daysPrefix.three" to
        "days",//"дней",
        "app.support_service.prefix_titles.daysPrefix.four" to
        "days",//"дней",
        "app.support_service.prefix_titles.monthsPrefix.one" to
        "month",//"месяц",
        "app.support_service.prefix_titles.monthsPrefix.two" to
        "month",//"месяца",
        "app.support_service.prefix_titles.monthsPrefix.three" to
        "months",//"месяцев",
        "app.support_service.prefix_titles.monthsPrefix.four" to
        "months",//"месяцев",
        "app.support_service.prefix_titles.yearsPrefix.one" to
        "year",//"год",
        "app.support_service.prefix_titles.yearsPrefix.two" to
        "year",//"года",
        "app.support_service.prefix_titles.yearsPrefix.three" to
        "years",//"лет",
        "app.support_service.prefix_titles.yearsPrefix.four" to
        "years",//"лет",
        "app.support_service.prefix_titles.hoursPrefix.one" to
        "hour",//"час",
        "app.support_service.prefix_titles.hoursPrefix.two" to
        "hour",//"часа",
        "app.support_service.prefix_titles.hoursPrefix.three" to
        "hours",//"часов",
        "app.support_service.prefix_titles.hoursPrefix.four" to
        "hours",//"часов",
        "app.support_service.prefix_titles.minutesPrefix.one" to
        "minute",//"минута",
        "app.support_service.prefix_titles.minutesPrefix.two" to
        "minute",//"минуты",
        "app.support_service.prefix_titles.minutesPrefix.three" to "minutes",//"минут",
        "app.support_service.prefix_titles.minutesPrefix.four" to
        "minutes",//"минут",

// Авторизация
        "app.phone.error_label_title" to "Check if you entered the phone number correctly",
        "app.phone.description_label_title" to "Enter your phone number — get access to\nyour profile and the full functionality of\nthe application",
        "app.registration.code.error_label_title" to "Incorrect code",//"Неправильный код",
// Плитки меню верхние
        "app.menu.background_page.chat_button.title" to "Chat",//"Чат",
        "app.menu.background_page.chat_button.description" to "Ask a master\nin chat",//"Задать\nвопрос в чате",
        "app.menu.background_page.call_button.title" to
        "Call",//"Звонок",
        "app.menu.background_page.call_button.description" to
        "Online\naudio call",//"Онлайн\nаудиозвонок",
        "app.menu.background_page.video_button.title" to
        "Video",//"Видео",
        "app.menu.background_page.video_button.description" to
        "Online\nvideo call",//"Видеосвязь\nонлайн",
        "app.menu.background_page.freecall_button.title" to
        "8-800",//"8-800",
        "app.menu.background_page.freecall_button.description" to
        "Free\nphone call",//"Бесплатный\nзвонок",
        "app.menu.background_page.bottom_button_client.title" to "Go to all chats ",
        "app.menu.background_page.bottom_button_quest.description" to "Login to see your chats",//"Войдите, чтобы видеть свои чаты →",
// Профиль
        "app.profile.my_object_title" to "My apartments",
        "app.profile.my_object.append_title" to "Add",//"Добавить",
        "app.profile.menu_rows.orders" to "Orders history",
        "app.profile.menu_rows.polises" to "Poliсes",//"Полисы",
        "app.profile.menu_rows.paymant_methods" to "Payment methods",//"Способы оплаты",
        "app.profile.menu_rows.notifications_settings" to "Notification Settings",//"Настройки уведомлений",
        "app.profile.menu_rows.communucations" to "Communication methods",//"Способы связи",
        "app.profile.menu_rows.agent_data" to "Agent data",
        "app.profile.menu_rows.no_agent_data" to "I know agentcode",//"Я знаю код агента",
        "app.profile.menu_rows.about_us" to "About us",//"О приложении",
        "app.profile.menu_rows.logout" to "Logout",//"Выйти",
// История заказов
        "app.orders_history.view_title" to "Orders history",
        "app.orders_history.order_cell.purchase_title" to
        "Purchase",//"Оплатить",
        "app.orders_history.order_cell.no_service" to "No data",
        "app.orders_history.order_cell.no_price" to "No data",
        "app.orders.additional_invoince.purchase_button_title" to
        "Purchase",//"Оплатить",
        "app.orders.additional_invoince.additional_invoince_title" to
        "Additional invoince ∙ ",//"Дополнительный счёт ∙ ".
        "app.orders_history.order_cell.order_state.draft" to
        "Draft",//"Cоздан"
        "app.orders_history.order_cell.order_state.confirmed" to
        "Confirmed",//"Подтвержден"
        "app.orders_history.order_cell.order_state.active" to
        "Active",//"Активный"
        "app.orders_history.order_cell.order_state.resolved" to
        "Resolved",//"Завершен"
        "app.orders_history.order_cell.order_state.cancelled" to
        "Cancelled",//"Отменен"
        "app.orders_history.order_cell.order_state.no_data" to "No data",
        "app.order_detail.order_title" to "Order",//"Заказ",
        "app.order_detail.buing_block_title" to "Payment",//"Оплата",
        "app.order_detail.cancel_button_title" to "Cancel order",//"Отменить заказ",
        "app.order_detail.service_title" to "Service",//"Услуга",
        "app.order_detail.address_title" to "Address",//"Адрес",
        "app.order_detail.datetime_title" to "Desired date and time",//"Желаемая дата и время",
        "app.order_detail.comment_title" to "Comment",//"Комментарий",
        "app.order_detail.payment_methods_title" to "Payment methods",//"Способы оплаты",
        "app.order_detail.email_forcheck_title" to "Email for the receipt",//"Почта для чека",
        "app.order_detail.order_state.draft" to "Draft",//"Заказ создан",
        "app.order_detail.order_state.confirmed" to "Confirmed",
        "app.order_detail.order_state.active" to "Active",
        "app.order_detail.order_state.resolved" to "Resolved",
        "app.order_detail.order_state.cancelled" to "Cancelled",
        "app.order_detail.order_state.no_data" to "No data",
        "app.order_detail.additional_invoinces.view_title" to
        "Additional invoince",//"Дополнительный счёт",
        "app.order_detail.additional_invoinces.services" to
        "Services",//"Услуги",
        "app.order_detail.additional_invoinces.purchase_button" to
        "Purchase",//"Оплатить",
        "app.order_detail.additional_invoinces.all_price" to "Total cost",
        "app.order_detail.additional_invoinces.master_online_title" to
        "If you have any questions,\ncontact the Online Master",
        "app.order_detail.billing_block_from_balance.title" to "Payment methods",//"Способ оплаты",
        "app.order_detail.order_cancel_request_view.title" to "The request to cancel the\norder has been sent",
        "app.order_detail.order_empty_view.title" to "You don't have any orders",//"У вас пока нет истории заказов",
        "app.order_detail.order_empty_view.go_to_catalog" to "Go to the service catalog",//"Перейти в каталог услуг",
// Полисы
        "app.polices.add_step_one.description_label" to "Enter the series and number of the policy you want to link",
        "app.polices.add_step_one.serial_number_label" to "Policy series and number",
        "app.polices.add_step_one.serial_number_input" to "Enter the policy series and number",
        "app.polices.add_step_one.serial_number_error_label" to "Check if the policy number is entered correctly",
        "app.polices.add_step_one.find_button" to "Where it?",
        "app.polices.add_step_one.next_button" to "Next",//"Далее",
        "app.polices.add_step_one.view_title" to "Step 1 of 2",
        "app.polices.add_step_one.view_subtitle" to "Policy details",//"Данные полиса",
        "app.polices.add_step_two.view_title" to "Step 2 of 2",
        "app.polices.add_step_two.view_subtitle" to "Data of the policyholder",//"Данные страхователя",
        "app.polices.add_step_two.next_button" to "Next",//"Далее",
        "app.polices.add_step_two.description_label" to "Fill in the data specified in the policy to confirm its ownership",
        "app.polices.add_step_two.last_name_label" to "Last name",//"Фамилия страхователя",
        "app.polices.add_step_two.last_name_input" to
        "Anderson",//"Иванов",
        "app.polices.add_step_two.last_name_error_label" to "Check if the last name is entered correctly",
        "app.polices.add_step_two.first_name_label" to "First name",//"Имя страхователя",
        "app.polices.add_step_two.first_name_input" to "Tomas",//"Иван",
        "app.polices.add_step_two.first_name_error_label" to "Check if the name is entered correctly",
        "app.polices.add_step_two.middle_name_label" to "Middle name",//"Отчество страхователя",
        "app.polices.add_step_two.middle_name_input" to
        "Smith",//"Иванович",
        "app.polices.add_step_two.middle_name_error_label" to "Check if the middle name is entered correctly",
        "app.polices.add_step_two.birth_date_label" to "Date of birth",//"Дата рождения",
        "app.polices.add_step_two.birth_date_error_label" to "Check if the date of birth is entered correctly",
        "app.polices.search_policy.loading_view.loading_label" to
        "Looking for a policy...",//"Ищем полис...",
        "app.polices.search_policy.result_view.result_status_label" to
        "The policy is linked",//"Полис привязан",
        "app.polices.search_policy.result_view.confirm_button" to "Got it!",
        "app.polices.search_policy.result_view.valid_from_text" to
        "Valid from ",//"Действует с ",
        "app.polices.search_policy.result_view.to_text" to "to",//"по",
        "app.polices.search_policy.result_view.subtitle_text" to "We have added a policy to your insurance products. The validity period of the insurance policy from ",
        "app.polices.serial_number_gide_view.title_label" to "Policy number",//"Номер полиса",
        "app.polices.serial_number_gide_view.confirm_button" to "Got it!",//"Понятно!",
        "app.polices.polices_view.view_title" to "Polices",//"Полисы",
        "app.polices.polices_view.empty_polices_label" to "You don't have\ninsurance policies",
        "app.polices.polices_view.add_police_button" to "Link polices",//"Привязать полис",
        "app.polices.polices_contract_view.valid_from_text" to "Valid from ",//"Действует с ",
        "app.polices.polices_contract_view.to_text" to "to",//"по",
        "app.polices.polices_detail_view.address_label" to
        "ADDRESS",//"АДРЕС",
        "app.polices.polices_detail_view.policy_number_label" to "POLICY NUMBER",//"НОМЕР ПОЛИСА",
        "app.polices.polices_detail_view.fullname_label" to "FULL NAME OF THE POLICYHOLDER",//"ФИО СТРАХОВАТЕЛЯ",
        "app.polices.polices_detail_view.valid_from_label" to "VALID FROM",//"ДЕЙСТВУЕТ С",
        "app.polices.polices_detail_view.valid_until_label" to "VALID UNTIL",//"ДЕЙСТВУЕТ ДО",
// Агент
        "app.agent_info.agent_code_label" to "Agent code",
        "app.agent_info.no_data_text" to "No data",//"Нет данных",
        "app.agent_info.agent_phone_label" to "Agent phone",
        "app.agent_info.request_top_label" to "The agent's code and phone number can\nbe changed only upon request",
        "app.agent_info.view_title" to "Agent data",//"Данные агента",
        "app.agent_info.request_submited_top_label" to "The request for editing will be considered",
        "app.agent_info_edit.agent_code_title" to "Agent code",
        "app.agent_info_edit.agent_code_error_label" to "An agent with such a code does not exist",//"Агент с таким кодом не существует",
        "app.agent_info_edit.agent_phone_label" to "Agent phone",//"Номер телефона агента",
        "app.agent_info_edit.agent_phone_error_label" to "Check that the phone number is entered correctly",
        "app.agent_info_edit.save_button" to "Save",//"Сохранить",
        "app.agent_info_edit.view_title" to "Edit data",
        "app.agent_info_edit_request.question_label" to "Do you really want\nto submit a request?",
        "app.agent_info_edit_request.question_body_label" to "The Online Master will\ncontact you in the chat",
        "app.agent_info_edit_request.confirm_button" to "Send request",//"Отправить заявку",
        "app.agent_info_edit_request.cancel_button" to "No, thanks",//"Нет, спасибо",
        "app.agent_info_edit_request.question_sended_body_label" to "The application has been sent.\nThe operator will contact you",//"Заявка отправлена.\nОператор свяжется с вами",
        "app.agent_info_edit_request.confirm_sended_button" to "Great!",//"Отлично!",
// Данные пользователя
        "app.profile.client.no_data_text" to "No data",//"Нет данных",
        "app.profile.client.loading_photo_action" to "Upload photo",//"Загрузить фотографию",
        "app.profile.client.delete_photo_action" to "Delete photo",//"Удалить фотографию",
        "app.profile.client.cancel_action" to "Cancel",//"Отмена",
        "app.profile.client.sex_male" to "Male",//"Муж",
        "app.profile.client.sex_female" to "Female",//"Жен",
        "app.profile.client_edit.last_name_label" to "Last name",//"Фамилия",
        "app.profile.client_edit.last_name_input" to
        "Anderson",//"Иванов",
        "app.profile.client_edit.last_name_error_label" to "Check that the last name is entered correctly",
        "app.profile.client_edit.first_name_label" to "First name",//"Имя",
        "app.profile.client_edit.first_name_input" to "Tomas",//"Иван",
        "app.profile.client_edit.first_name_error_label" to "Check that the first name is entered correctly",
        "app.profile.client_edit.middle_name_label" to "Middle name",//"Отчество",
        "app.profile.client_edit.middle_name_input" to
        "Smith",//"Иванович",
        "app.profile.client_edit.middle_name_error_label" to "Check that the middle name is entered correctly",
        "app.profile.client_edit.date_birth_label" to "Date of birth",//"Дата рождения",
        "app.profile.client_edit.date_birth_error_label" to "Check that the date of birth is entered correctly",
        "app.profile.client_edit.sex_label" to "Sex",//"Пол",
        "app.profile.client_edit.sex_male_button" to "Male",//"Мужчина",
        "app.profile.client_edit.sex_female_button" to
        "Female",
        "app.profile.client_edit.passport_series_label" to "Passport series",//"Серия паспорта",
        "app.profile.client_edit.passport_series_error_label" to "Check the correctness of the introduction of the passport series",//"Проверьте, правильно ли введена серия паспорта",
        "app.profile.client_edit.passpoert_number_label" to "Passport number",//"Номер паспорта",
        "app.profile.client_edit.passport_number_error_label" to "Check if the passport number is entered correctly",
        "app.profile.client_edit.phone_number_label" to "Phone number",//"Номер телефона",
        "app.profile.client_edit.request_top_text" to "To edit personal data,",//"Чтобы редактировать личные данные,",
        "app.profile.client_edit.request_end_text" to "\nsend a request",//"\nоставьте заявку",
        "app.profile.client_edit.additional_number_label" to
        "Additional_number",//"Дополнительный телефон",
        "app.profile.client_edit.additional_number_error_label" to
        "Check if the phone number is entered correctly",
        "app.profile.client_edit.email_label" to "Email",
        "app.profile.client_edit.email_error_label" to "Check if the email is entered correctly",
        "app.profile.client_edit.save_button" to "Save",//"Сохранить",
        "app.profile.client_edit.view_title" to "Edit data",
        "app.profile.client_edit.request_sended_label" to "The application for data editing\nwill be considered",
        "app.profile.client_edit_request.question_title_label" to "Do you really want\nto submit a request?",
        "app.profile.client_edit_request.question_body_label" to "The Online Master will\ncontact you in the chat",
        "app.profile.client_edit_request.confirm_button" to "Send request",//"Отправить заявку",
        "app.profile.client_edit_request.cancel_button" to "No thanks.",//"Нет, спасибо",
        "app.profile.client_edit_request.question_sended_label" to "The request has been sent.\nThe operator will contact you",
        "app.profile.client_edit_request.confirm_sended_button" to
        "Great!",//"Отлично!",
// Карточки продуктов
        "app.product_cards.product_detail_view.price_view.price_label" to
        "PRICE",
        "app.product_cards.product_detail_view.price_view.include_product_label" to "Include in product",//"Включено в продукт",
        "app.product_cards.product_detail_view.price_view.from_text" to
        "from",//"от",
        "app.product_cards.product_detail_view.price_view.price_description_label" to "The exact cost will be formed after answering a few clarifying questions.",
        "app.product_cards.product_detail_view.price_view.paid_for_label" to
        "Paid for",//"Оплачено",
        "app.product_cards.product_detail_view.additional.more_about" to
        "MORE ABOUT THE PRODUCT",//"ПОДРОБНЕЕ О ПРОДУКТЕ",
        "app.product_cards.product_detail_view.benefits.benefites" to
        "PRODUCT BENEFITS",//"ПРЕИМУЩЕСТВА ПРОДУКТА",
        "app.product_cards.product_detail_view.services.whats_included" to
        "WHAT'S INCLUDED?",//"ЧТО ВХОДИТ?",
        "app.product_cards.product_detail_view.validaty.terms_conditions" to
        "TERMS AND CONDITIONS",//"СРОКИ ДЕЙСТВИЯ И УСЛОВИЯ",
        "app.product_cards.product_detail_view.duration.duration_title" to
        "DURATION OF EXECUTION",//"ПРОДОЛЖИТЕЛЬНОСТЬ ВЫПОЛНЕНИЯ",
        "app.product_cards.product_detail_view.buy_button" to
        "Buy",//"Купить",
        "app.product_cards.product_detail_view.after_duration_text" to
        "after purchase",//"после покупки",
        "app.product_cards.service_detail_view.more_about" to "MORE ABOUT THE SERVICE",//"ПОДРОБНЕЕ ОБ УСЛУГЕ",
        "app.product_cards.service_detail_view.benefites_title" to
        "SERVICE BENEFITS",//"ПРЕИМУЩЕСТВА УСЛУГИ",
        "app.product_cards.service_detail_view.product_benegites.title" to
        "MOI_SERVICE BENEFITES",//"ПРЕИМУЩЕСТВА МОЙ_СЕРВИС",
        "app.product_cards.service_detail_view.product_benegites.fix_price" to "24/7 support",//"Поддержка 24/7",
        "app.product_cards.service_detail_view.product_benegites.guarantee" to "12 months warranty",//"Гарантия 12 месяцев",
        "app.product_cards.service_detail_view.terms_conditions" to
        "TERMS AND CONDITIONS",//"СРОК ДЕЙСТВИЯ И УСЛОВИЯ",
        "app.product_cards.service_detail_view.duration_title" to
        "DURATION OF EXECUTION",//"ПРОДОЛЖИТЕЛЬНОСТЬ ВЫПОЛНЕНИЯ",
        "app.product_cards.service_detail_view.buy_button_order" to
        "Order",//"Заказать",
        "app.product_cards.service_detail_view.buy_button_arrange" to
        "Arrange",
        "app.product_cards.service_detail_view.is_fixed_from" to "from",//"от",
        "app.product_cards.service_detail_view.after_purchase" to "after purchase",//"после покупки",
        "app.product_cards.service_detail_view.moi_service" to
        "Moi_service",//"Мой_Сервис",
        "app.product_cards.service_in_product_detail.order_button" to
        "Order",//"Заказать",
        "app.product_cards.service_in_product_detail.more_about" to
        "MORE ABOUT THE SERVICE",//"ПОДРОБНЕЕ ОБ УСЛУГЕ",
        "app.product_cards.service_in_product_detail.address_first" to
        "ADDRESS",//"АДРЕС",
        "app.product_cards.service_in_product_detail.benefits" to
        "SERVICE BENEFITS",//"ПРЕИМУЩЕСТВА УСЛУГИ",
        "app.product_cards.service_in_product_detail.terms" to "TERMS AND CONDITIONS",//"СРОК ДЕЙСТВИЯ И УСЛОВИЯ",
        "app.product_cards.service_in_product_detail.duration_title" to
        "DURATION OF EXECUTION",//"ПРОДОЛЖИТЕЛЬНОСТЬ ВЫПОЛНЕНИЯ",
        "app.product_cards.service_in_product_detail.after_purchase" to
        "after purchase",//"после покупки",
        "app.product_cards.service_in_product_detail.valid_until" to
        "VALID UNTIL",//"ДЕЙСТВУЕТ ДО",
        "app.product_cards.service_in_product_detail.valid_from" to
        "VALID FROM",//"ДЕЙСТВУЕТ С",
// Экран сос
        "app.sos_screen.title_label" to "What happened?",
        "app.sos_screen.subtitle_label" to "Contact the Online Master for quick help with any questions.",
        "app.sos_screen.action_type.call_name" to "8-800",//"8-800",
        "app.sos_screen.action_type.call_description" to "Free phone call",//"Бесплатный звонок",
        "app.sos_screen.action_type.chat_name" to "Chat",//"Чат",
        "app.sos_screen.action_type.chat_description" to "Ask question",//"Задать вопрос в чате",
        "app.sos_screen.action_type.audio_name" to "Call",//"Звонок",
        "app.sos_screen.action_type.audio_description" to "Online audiocall",//"Онлайн аудиозвонок",
        "app.sos_screen.action_type.video_name" to "Video",//"Видео",
        "app.sos_screen.action_type.video_description" to "Online videocall",//"Видеосвязь онлайн",
// Экран онборда
        "app.onboarding.next_button" to "Next",//"Далее",
        "app.onboarding.login_button" to "Login",//"Войти,
        "app.onboarding.skip_button" to "Skip",//"Проаустить",
        "app.onboarding.step_one.title_label" to "Convenient service\nfor household chores",
        "app.onboarding.step_two.title_label" to "Many home services\nin one app",//"Сотни услуг для дома\nв одном месте",
        "app.onboarding.step_three.title_label" to "Linking insurance\npolicies",//"Привязка страховых\nполисов",
        "app.onboarding.step_four.title_label" to "Online Master\n24/7 for help with\nany questions",
        "app.onboarding.step_five.title_label" to "Welcome to\nMoi_service Dom!",//"Добро пожаловать\nв Мой_Сервис Дом!",
// Чаты
        "app.chat.chat_start.back_label" to "Online Master",
        "app.chat.chat_start.bottom_input" to "Input message",
        "app.chat.chat_start.connecting_label" to
        "Connecting...",//"Подключаемся...",
        "app.chats.chat.call.call_ended" to "Ended",
        "app.chat.chat_start.add_document.photo_alert" to "Photo or video",//"Фото или Видео",
        "app.chat.chat_start.add_document.drive_alert" to "iCloud Drive",//"iCloud Drive",
        "app.chat.chat_start.add_document.cancel_alert" to
        "Cancel",//"Отмена",
        "app.chat.chat_start.full_screen_image.save_picture_alert" to
        "Save picture",//"Сохранить картинку",
        "app.chat.chat_start.full_screen_image.delete_alert" to
        "Delete",//"Удалить",
        "app.chat.chat_start.full_screen_image.cancel_alert" to
        "Cancel",//"Отмена",
        "app.chat.chat_start.web_document_view.reload_alert" to "Reload documents",//"Перезагрузить документы",
        "app.chat.chat_start.web_document_view.edit_alert" to "Edit name",//"Редактировать название",
        "app.chat.chat_start.web_document_view.save_alert" to
        "Save",//""Сохранить"",
        "app.chat.chat_start.web_document_view.delete_alert" to "Delete document",//"Удалить документ",
        "app.chat.chat_start.web_document_view.cancel_alert" to
        "Cancel",//"Отмена",
        "app.chat.chat_start.bottom_input_view.placeholder" to "Input message",//"Введите сообщение",
        "app.chat.service.master_model.title" to "Online Master",//"Онлайн Мастер",
        "app.chat.service.service_model.title" to "Moi_service Dom",//"Мой_Сервис Дом",
// Звонок
        "app.call.outgoing.connecting_title" to
        "Connecting...",//"Подключаемся...",
        "app.call.outgoing.subtitle_label" to "Looking for a free operator",
        "app.call.outgoing.default_consultant_name" to "Online Master",//"Онлайн Мастер",
        "app.call.privacy.question_title" to "Allow access so that the\nconsultant or the master can hear you",
        "app.call.privacy.to_settings_button" to "To settings",//"Перейти в настройки",
        "app.invoince_service_view.no_data" to "No data",//"Нет данных",
        "app.chat_invoince_service_view.no_data" to "No data",
// Объект недвиги
        "app.object_detail.no_data" to "No data",//"Нет данных",
        "app.object_detail.yes" to "Yes",//"Да",
        "app.object_detail.no" to "No",//"Да",
        "app.object_detail.photo_alert" to "Photo",//"Фото",
        "app.object_detail.drive_alert" to "iCloud Drive",
        "app.object_detail.cencel_alert" to "Cancel",//"Отмена",
        "app.object_detail.documents.title_label" to
        "Documents",//"Документы",
        "app.object_detail.documents.all_button" to "All",//"Все",
        "app.object_detail.documents_add.description_label" to "You don't have any linked documents yet",
        "app.object_detail.documents.add_button" to "Add documents",//"Добавить документы",
        "app.object_detail.documents.add_alert" to "Add document",//"Добавить документ",
        "app.object_detail.documents.edit_alert" to "Edit list",//"Редактировать список",
        "app.object_detail.documents.cancel_alert" to
        "Cancel",//"Отмена",
        "app.object_detail.documents.photo_alert" to "Photo",//"Фото",
        "app.object_detail.documents.drive_alert" to "iCloud Drive",//"iCloud Drive",
        "app.object_detail.documents.view_title" to
        "Documents",//"Документы",
        "app.object_detail.documents.no_data_placeholder" to "Nodata",//"Не определено",
        "app.catalog.more_view.title" to "More",//"Ещё",
        "app.catalog.search.cancel" to "Cancel",
        "app.support_service.prefix_titles.work_estimation" to "Work will take ~",
        "app.product.purchase.select_property.no_property" to "You don't have any properties\nwould you like to add one?",
        "app.product.purchase.select_property.add_object" to "Add new object",
        "app.product.purchase.select_property_title" to "Select property",
        "app.product.purchase.property.leave_comment" to "Leave comment",
        "app.product.purchase.property.edit_comment" to "Edit comment",
        "app.product.purchase.card_payment" to "Card payment",
        "app.product.purchase.new_card_payment" to "New card payment",
        "app.product.purchase.payment_method.title" to "Payment method",
        "app.product.purchase.payment_method.new_card" to "New card",
        "app.product.purchase.payment_method.save_card_for_new_payments" to "Save card for next payments",
        "app.product.purchase.payment_method.select" to "Select",
        "app.chat.chat_start.add_document.file_alert" to "File",
        "app.object.main.info.documents.title" to "Documents",
        "app.object.main.info.documents.title_all" to "All",
        "app.object.main.info.property_type.house" to "House",
        "app.object.main.info.property_type.appartment" to "Flat",
        )
}