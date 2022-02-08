package com.custom.rgs_android_dom.ui.client.orders

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
import com.custom.rgs_android_dom.domain.client.models.Bill
import com.custom.rgs_android_dom.domain.client.models.BillType
import com.custom.rgs_android_dom.domain.client.models.OrderItemModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener


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

                root.setOnDebouncedClickListener {
                    onOrderClick(item)
                }
            }
        }

        private fun initBillItems(billContainer: LinearLayout, bills: List<Bill>) {
            val context = billContainer.context
            billContainer.removeAllViews()
            if (bills.isNotEmpty()) {
                bills.forEach {
                    if (it.type == BillType.MAIN) {
                        if (bills.size > 1) {
                            ItemOrderMainBillBinding.inflate(LayoutInflater.from(context), billContainer, false).apply {
                                root.setOnDebouncedClickListener {
                                }
                                billPayTextView.setBackgroundResource(R.drawable.rectangle_stroke_1dp_primary_500_radius_8dp)
                                billPayTextView.setTextColor(ContextCompat.getColor(context, R.color.primary500))
                                billContainer.addView(root)
                            }
                        } else {
                            ItemOrderMainBillBinding.inflate(LayoutInflater.from(context), billContainer, false).apply {
                                root.setOnDebouncedClickListener {
                                }
                                billContainer.addView(root)
                            }
                        }
                    } else if (it.type == BillType.ADDITIONAL) {
                        ItemOrderAdditionalBillBinding.inflate(LayoutInflater.from(context), billContainer, false).apply {
                            billPayTextView.setOnDebouncedClickListener {
                            }
                            descriptionTextView.text = it.description
                            billContainer.addView(root)
                        }
                    }
                }
            }
        }
    }
}
