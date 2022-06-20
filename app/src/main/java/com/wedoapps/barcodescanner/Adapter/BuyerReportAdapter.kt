package com.wedoapps.barcodescanner.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wedoapps.barcodescanner.Model.BuyerReportModal
import com.wedoapps.barcodescanner.R
import com.wedoapps.barcodescanner.databinding.LayoutSingleReportItemBinding

class BuyerReportAdapter(private val listener: OnBuyerClick) : RecyclerView.Adapter<BuyerReportAdapter.BuyerReportViewHolder>() {

    inner class BuyerReportViewHolder(private val binding: LayoutSingleReportItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(buyerReportModal: BuyerReportModal) {
            binding.apply {
                tvSingleItemCount.text = ("${bindingAdapterPosition + 1}.").toString()
                tvSingleItemName.text = buyerReportModal.name
                tvSingleItemBarcode.text = if (buyerReportModal.phoneNumber.isNullOrEmpty()) {
                    "NO MOBILE NUMBER AVAILABLE"
                } else {
                    "Mob no.: ${buyerReportModal.phoneNumber}"
                }
                tvSingleItemTotalPrice.text =
                    "${itemView.context.getString(R.string.Rs)} ${buyerReportModal.total}"
                tvSingleItemSinglePrice.text = "Time: ${buyerReportModal.time}"

                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val item = differ.currentList[position]
                        if(item != null) {
                            listener.onClick(item)
                        }
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerReportViewHolder {
        val binding = LayoutSingleReportItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BuyerReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyerReportViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<BuyerReportModal>() {
        override fun areItemsTheSame(
            oldItem: BuyerReportModal,
            newItem: BuyerReportModal
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BuyerReportModal,
            newItem: BuyerReportModal
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnBuyerClick {
        fun onClick(buyerReportModal: BuyerReportModal)
    }
}