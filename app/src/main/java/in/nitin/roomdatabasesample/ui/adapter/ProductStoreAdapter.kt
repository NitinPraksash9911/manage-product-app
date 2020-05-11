package `in`.nitin.roomdatabasesample.ui.adapter

import `in`.nitin.roomdatabasesample.databinding.ProductStoreLayoutBinding
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Store
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProductStoreAdapter :
    ListAdapter<Store, ProductStoreAdapter.ProductStoreViewHolder>(StoreDataDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductStoreViewHolder {

        return ProductStoreViewHolder(
            ProductStoreLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductStoreViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class ProductStoreViewHolder(private val binding: ProductStoreLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(data: Store) {

            binding.data = data
            binding.executePendingBindings()
        }

    }


    class StoreDataDiffCallback : DiffUtil.ItemCallback<Store>() {
        override fun areItemsTheSame(
            oldItem: Store,
            newItem: Store
        ): Boolean {
            return oldItem.storeAddress == newItem.storeAddress
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Store,
            newItem: Store
        ): Boolean {
            return oldItem == newItem
        }

    }
}