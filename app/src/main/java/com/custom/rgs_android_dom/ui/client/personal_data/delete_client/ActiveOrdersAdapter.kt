package com.custom.rgs_android_dom.ui.client.personal_data.delete_client

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemActiveOrderBinding
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp

class ActiveOrdersAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var orders: List<Order> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemActiveOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class OrderViewHolder(private val binding: ItemActiveOrderBinding) : RecyclerView.ViewHolder(binding.root) {

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
            }
        }
    }

}
