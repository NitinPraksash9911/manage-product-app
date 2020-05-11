package `in`.nitin.roomdatabasesample.ui.adapter

import `in`.nitin.roomdatabasesample.databinding.ProductColorLayoutBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductColorAdapter : RecyclerView.Adapter<ProductColorAdapter.ProductColorViewHolder>() {

    private var colorList = arrayListOf<String>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductColorViewHolder {

        return ProductColorViewHolder(
            ProductColorLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

     fun setColorList(newList: ArrayList<String>) {
        colorList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = colorList.size

    override fun onBindViewHolder(
        holder: ProductColorViewHolder,
        position: Int
    ) {

        holder.bindTo(colorList.get(position))

    }

    class ProductColorViewHolder(private val binding: ProductColorLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(data: String) {
            binding.color = data
            binding.executePendingBindings()

        }
    }
}