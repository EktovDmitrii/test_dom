package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.*

class TabCatalogViewModel : BaseViewModel() {

    private val viewStateController = MutableLiveData<ProductViewState>()
    val viewStateObserver: LiveData<ProductViewState> = viewStateController

    init {
        viewStateController.value = ProductViewState(
            primaryCategories = listOf(
                ProductItemModel(title = "Выездная диагностика", servicesQuantity = "4 услуги"),
                ProductItemModel(title = "Выездная диагностика", servicesQuantity = "4 услуги"),
                ProductItemModel(title = "Выездная диагностика", servicesQuantity = "4 услуги"),
                MoreProductsItemModel()
            ),
            secondaryCategories = listOf(
                SecondaryCategoryModel(
                    title = "Сантехника",
                    modelsWithPicture = listOf(
                        SecondaryCategoryModelWithPicture(title = "Устранение засоров"),
                        SecondaryCategoryModelWithPicture(title = "Работы с унитазами"),
                        SecondaryCategoryModelWithPicture(title = "Работы с душевыми стойками")
                    ),
                    modelsWithoutPicture = listOf(
                        SecondaryCategoryModelWithoutPicture(
                            title = "Установка раковины, мойки",
                            servicesQuantity = "10 видов услуг"
                        )
                    )
                ),
                SecondaryCategoryModel(
                    title = "Электромонтажные работы",
                    modelsWithPicture = listOf(
                        SecondaryCategoryModelWithPicture(title = "Работы с розетками"),
                        SecondaryCategoryModelWithPicture(title = "Люстры и светильники"),
                        SecondaryCategoryModelWithPicture(title = "Электросчетчики")
                    ),
                    modelsWithoutPicture = listOf(
                        SecondaryCategoryModelWithoutPicture(
                            title = "Установка автоматов и подключение щита",
                            servicesQuantity = "10 видов услуг"
                        ),
                        SecondaryCategoryModelWithoutPicture(
                            title = "Демонтаж старого и прокладка нового кабеля",
                            servicesQuantity = "12 видов услуг"
                        )
                    )
                ),
                SecondaryCategoryModel(
                    title = "Уборка",
                    modelsWithPicture = listOf(
                        SecondaryCategoryModelWithPicture(title = "Разовая уборка"),
                        SecondaryCategoryModelWithPicture(title = "Генеральная уборка"),
                        SecondaryCategoryModelWithPicture(title = "Химчистка")
                    ),
                    modelsWithoutPicture = listOf(
                        SecondaryCategoryModelWithoutPicture(
                            title = "Уборка после ремонт",
                            servicesQuantity = "10 видов услуг"
                        ),
                        SecondaryCategoryModelWithoutPicture(
                            title = "Уборка после пожара",
                            servicesQuantity = "12 видов услуг"
                        )
                    )
                ),
                SecondaryCategoryModel(
                    title = "Бытовая техника",
                    modelsWithPicture = listOf(
                        SecondaryCategoryModelWithPicture(title = "Установка стиральной, посудомоечной машины"),
                        SecondaryCategoryModelWithPicture(title = "Водонагреватели и эл.сушилки"),
                        SecondaryCategoryModelWithPicture(title = "Электропли-ты, духовые шкафы и пр.")
                    ),
                    modelsWithoutPicture = listOf(
                        SecondaryCategoryModelWithoutPicture(
                            title = "Установка вытяжки и монтаж вентиляции",
                            servicesQuantity = "10 видов услуг"
                        )
                    )
                ),
                SecondaryCategoryModel(
                    title = "Двери, замки",
                    modelsWithPicture = listOf(
                        SecondaryCategoryModelWithPicture(title = "Аварийное вскрытие без ключа"),
                        SecondaryCategoryModelWithPicture(title = "Аварийное вскрытие с ключом"),
                        SecondaryCategoryModelWithPicture(title = "Аварийное вскрытие сейфов")
                    ),
                    modelsWithoutPicture = listOf(
                        SecondaryCategoryModelWithoutPicture(
                            title = "Аварийное вскрытие автомобиля",
                            servicesQuantity = "10 видов услуг"
                        )
                    )
                )
            ),
            tertiaryCategories = listOf(
                TertiaryCategoryModel(
                    title = "Замена стекла",
                    items = listOf(
                        TertiaryItemModel(
                            title = "Ремонт окон",
                            quantity = "23 вида услуг"
                        ),
                        TertiaryItemModel(
                            title = "Утепление балкона",
                            quantity = "10 видов услуг"
                        )
                    )
                ),
                TertiaryCategoryModel(
                    title = "Работы по кровле",
                    items = listOf(
                        TertiaryItemModel(
                            title = "Монтаж стропильной конструкции кровли",
                            quantity = "10 видов услуг"
                        ),
                        TertiaryItemModel(
                            title = "Монтаж обрешетки",
                            quantity = "10 видов услуг"
                        ),
                        TertiaryItemModel(
                            title = "Устройство «кровельного пирога» (изоляции кровли)",
                            quantity = "10 видов услуг"
                        )
                    )
                ),
                TertiaryCategoryModel(
                    title = "Распил и вывоз деревьев",
                    items = listOf(
                        TertiaryItemModel(
                            title = "Распил и вывоз деревьев",
                            quantity = "10 видов услуг"
                        )
                    )
                )
            )
        )
    }

    fun onShowAllPrimaryCategoryClick() {
        notificationController.value = "onShowAllPrimaryCategoryClick"
    }

    fun onProductClick() {
        //navigate to product or smth
        notificationController.value = "onProductClick"
    }

    fun onMoreProductsClick() {
        // retrieve more products
        notificationController.value = "onMoreProductsClick"
    }

    fun onServiceClick() {
        notificationController.value = "onServiceClick"
    }

}
