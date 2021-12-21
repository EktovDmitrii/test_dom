package com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks

data class MockCatalogPrimaryUnitModel(
    val title: String,
    val products: List<MockProductModel> = listOf(
        MockProductModel(
            productTitle = "Сезонное мытье окон",
            productImage = "",
            numberOfServices = "12"
        ),
        MockProductModel(
            productTitle = "Тепловизионная диагностика недвижимости",
            productImage = "",
            numberOfServices = "4"
        ),
        MockProductModel(
            productTitle = "Выездная диагностика",
            productImage = "",
            numberOfServices = "6"
        )
    )
)