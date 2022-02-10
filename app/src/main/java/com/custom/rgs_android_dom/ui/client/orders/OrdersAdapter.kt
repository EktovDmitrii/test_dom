package com.custom.rgs_android_dom.ui.client.orders

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemOrderAdditionalBillBinding
import com.custom.rgs_android_dom.databinding.ItemOrderBinding
import com.custom.rgs_android_dom.databinding.ItemOrderMainBillBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductPriceModel
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.formatPrice
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener


class OrdersAdapter(
    private val onOrderClick: (Order) -> Unit,
    private val onPayClick: (PurchaseModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var orders: List<Order> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderViewHolder).bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun setItems(orders: List<Order>){
        this.orders = orders
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order) {
            with(binding) {
                val service = if (item.services?.isNotEmpty() == true) item.services[0] else null
                val context = root.context
                if (service?.serviceLogoMiddle?.isNotBlank() == true) {
                    GlideApp.with(context)
                        .load(GlideUrlProvider.makeHeadersGlideUrl(service.serviceLogoMiddle))
                        .transform(RoundedCorners(8.dp(context)))
                        .error(R.drawable.bg_secondary100_rounded_16dp)
                        .into(binding.orderImageView)
                }
                orderNameTextView.text = service?.serviceName
                orderDetailTextView.text = Html.fromHtml(
                    item.getOrderDescription(),
                    Html.FROM_HTML_MODE_LEGACY
                )
                initBillItems(billContainer, item)

                root.setOnDebouncedClickListener {
                    onOrderClick(item)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun initBillItems(billContainer: LinearLayout, item: Order) {
            val context = billContainer.context
            billContainer.removeAllViews()
            val invoices = item.generalInvoice ?: emptyList()
            val service = if (item.services?.isNotEmpty() == true) item.services[0] else null
            val status = item.status
            if (status == OrderStatus.DRAFT || status == OrderStatus.CONFIRMED) {
                val purchaseModel = PurchaseModel(
                    id = item.id,
                    defaultProduct = service?.defaultProduct ?: false,
                    duration = null,
                    deliveryTime = item.deliveryTime?.let { time -> "${time.from} - ${time.to}" } ?: "",
                    logoSmall = if (service?.serviceLogoMiddle?.isNotBlank() == true) service.serviceLogoMiddle else "empty",
                    name = service?.serviceName ?: "",
                    price = ProductPriceModel(
                        amount = null,
                        fix = service?.serviceFixPrice ?: false,
                        vatType = null
                    )
                )
                if (invoices.isNotEmpty()) {
                    ItemOrderMainBillBinding.inflate(LayoutInflater.from(context), billContainer, false).apply {
                        root.setOnDebouncedClickListener {
                            onPayClick.invoke(purchaseModel)
                        }
                        billPayTextView.setBackgroundResource(R.drawable.rectangle_stroke_1dp_primary_500_radius_8dp)
                        billPayTextView.setTextColor(ContextCompat.getColor(context, R.color.primary500))
                        billContainer.addView(root)
                    }
                } else {
                    ItemOrderMainBillBinding.inflate(LayoutInflater.from(context), billContainer, false).apply {
                        root.setOnDebouncedClickListener {
                            onPayClick.invoke(purchaseModel)
                        }
                        billContainer.addView(root)
                    }
                }
            }

            invoices.forEach {
                val purchaseModel = PurchaseModel(
                    id = item.id,
                    defaultProduct = service?.defaultProduct ?: false,
                    duration = null,
                    deliveryTime = item.deliveryTime?.let { time -> "${time.from} - ${time.to}" } ?: "",
                    logoSmall = if (service?.serviceLogoMiddle?.isNotBlank() == true) service.serviceLogoMiddle else "empty",
                    name = service?.serviceName ?: "",
                    price = ProductPriceModel(
                        amount = null,
                        fix = service?.serviceFixPrice ?: false,
                        vatType = null
                    )
                )

                ItemOrderAdditionalBillBinding.inflate(LayoutInflater.from(context), billContainer, false).apply {
                    billPayTextView.setOnDebouncedClickListener {
                        onPayClick.invoke(purchaseModel)
                    }
                    descriptionTextView.text = "Дополнительный счёт  ∙  ${it.getFullPrice().formatPrice()}"
                    billContainer.addView(root)
                }
            }
        }
    }
}
