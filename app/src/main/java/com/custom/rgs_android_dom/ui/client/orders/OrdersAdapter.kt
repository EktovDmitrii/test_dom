package com.custom.rgs_android_dom.ui.client.orders

import android.graphics.Typeface
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemOrderBinding
import com.custom.rgs_android_dom.domain.client.models.OrderItemModel
import com.custom.rgs_android_dom.utils.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.custom.rgs_android_dom.domain.client.models.Bill
import com.custom.rgs_android_dom.domain.client.models.BillType


class OrdersAdapter(
    private val onOrderClick: (OrderItemModel) -> Unit,
    private val onPayClick: (OrderItemModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var orders: List<OrderItemModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderViewHolder).bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun setItems(orders: List<OrderItemModel>){
        this.orders = orders
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderItemModel) {
            with(binding) {
                val context = root.context
                if (item.icon.isNotBlank()) {
                    GlideApp.with(context)
                        .load(GlideUrlProvider.makeHeadersGlideUrl(item.icon))
                        .transform(RoundedCorners(8.dp(context)))
                        .error(R.drawable.bg_secondary100_rounded_16dp)
                        .into(binding.orderImageView)
                }
                orderNameTextView.text = item.title
                orderDetailTextView.text = Html.fromHtml(
                    item.description,
                    Html.FROM_HTML_MODE_LEGACY
                )
                initBillItems(billContainer, item.bills)

                root.setOnDebouncedClickListener { onOrderClick(item) }
            }
        }

        private fun initBillItems(billContainer: LinearLayout, bills: List<Bill>) {
            val context = billContainer.context
            billContainer.removeAllViews()
            if (bills.isNotEmpty()) {
                val fontFamily = ResourcesCompat.getFont(context, R.font.my_service_medium)
                val additionalTextColor = ContextCompat.getColor(context, R.color.primary500)
                val mainTextColor = ContextCompat.getColor(context, R.color.secondary_default)
                val descriptionColor = ContextCompat.getColor(context, R.color.secondary500)
                val text = "Оплатить"
                bills.forEach {
                    if (it.type == BillType.MAIN) {
                        if (bills.size > 1) {
                            billContainer.apply {
                                addView(
                                    getNewBillPayItem(
                                        text = text,
                                        fontFamily = fontFamily,
                                        textColor = additionalTextColor,
                                        background = R.drawable.rectangle_stroke_1dp_primary_500_radius_8dp
                                    )
                                )
                            }
                        } else {
                            billContainer.apply {
                                addView(
                                    getNewBillPayItem(
                                        text = text,
                                        fontFamily = fontFamily,
                                        textColor = mainTextColor,
                                        background = R.drawable.rectangle_stroke_1dp_secondary_200_radius_8dp
                                    )
                                )
                            }
                        }
                    } else if (it.type == BillType.ADDITIONAL) {
                        billContainer.apply {
                            addView(getNewBillDescriptionItem(it.description, descriptionColor))
                            addView(
                                getNewBillPayItem(
                                    text = text,
                                    fontFamily = fontFamily,
                                    textColor = additionalTextColor,
                                    background = R.drawable.rectangle_stroke_1dp_primary_500_radius_8dp
                                )
                            )
                        }
                    }
                }
            }
        }

        private fun getNewBillPayItem(
            text: String,
            fontFamily: Typeface?,
            textColor: Int,
            background: Int
        ): TextView {
            val context = binding.root.context
            return TextView(context).apply {
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 42.dpToPx(context))
                val sixteenDp = 16.dpToPx(context)
                params.setMargins(0, sixteenDp, 0, 0)
                setBackgroundResource(background)
                layoutParams = params
                gravity = Gravity.CENTER
                setPadding(sixteenDp, 0, sixteenDp, 0)
                typeface = fontFamily
                textSize = 14f
                setTextColor(textColor)
                setText(text)
            }
        }

        private fun getNewBillDescriptionItem(
            text: String,
            textColor: Int
        ): TextView {
            return TextView(binding.root.context).apply {
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val sixteenDp = 16.dpToPx(context)
                params.setMargins(0, sixteenDp, 0, 0)
                layoutParams = params
                setTextColor(textColor)
                textSize = 12f
                this.text = text
            }
        }
    }
}
