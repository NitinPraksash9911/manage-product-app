package `in`.nitin.roomdatabasesample.ui.adapter

import `in`.nitin.roomdatabasesample.databinding.ProductItemLayoutBinding
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.ProductWithStores
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val clickListener: ClickListener) :
    ListAdapter<ProductWithStores, ProductAdapter.ProductViewHolder>(ProductDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        return ProductViewHolder(
            ProductItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    class ProductViewHolder(
        private val binding: ProductItemLayoutBinding,
        private val clickListener: ClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(data: ProductWithStores) {
            binding.data = data
            binding.viewMore.setOnClickListener {
                clickListener.onItemClick(data,binding.productImage)
            }
            binding.executePendingBindings()

        }


    }


    class ProductDataDiffCallback : DiffUtil.ItemCallback<ProductWithStores>() {
        override fun areItemsTheSame(
            oldItem: ProductWithStores,
            newItem: ProductWithStores
        ): Boolean {
            return oldItem.product!!._id == newItem.product!!._id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ProductWithStores,
            newItem: ProductWithStores
        ): Boolean {
            return oldItem.product == newItem.product
        }
    }


    interface ClickListener {
        fun onItemClick(productWithStores: ProductWithStores, imageView: ImageView)

    }

}