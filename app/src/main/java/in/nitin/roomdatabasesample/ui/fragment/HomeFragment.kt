package `in`.nitin.roomdatabasesample.ui.fragment

import `in`.nitin.roomdatabasesample.R
import `in`.nitin.roomdatabasesample.databinding.FragmentHomeBinding
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.ProductWithStores
import `in`.nitin.roomdatabasesample.ui.activity.MainActivity
import `in`.nitin.roomdatabasesample.ui.adapter.ProductAdapter
import `in`.nitin.roomdatabasesample.ui.utils.ItemDecorationColumns
import `in`.nitin.roomdatabasesample.ui.utils.toTransitionGroup
import `in`.nitin.roomdatabasesample.ui.utils.waitForTransition
import `in`.nitin.roomdatabasesample.viewmodel.ProductViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment(), ProductAdapter.ClickListener {

    lateinit var binding: FragmentHomeBinding

    lateinit var productViewModel: ProductViewModel
    lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeBinding.inflate(layoutInflater)

        (activity as MainActivity?)!!.setSupportActionBar(binding.toolbar)

        initViews()
        subscribeData()
        clickHandler()
        return binding.root
    }


    /**
     * initialisation of views
     * */
    private fun initViews() {
        productAdapter = ProductAdapter(this)
        binding.lifecycleOwner = this
        productViewModel =
            ViewModelProvider(this, providerFactory).get(ProductViewModel::class.java)

        binding.contentLayout.rv.apply {
            adapter = productAdapter
            addItemDecoration(
                ItemDecorationColumns(
                    resources.getInteger(R.integer.list_product_columns),
                    resources.getDimensionPixelSize(R.dimen.list_spacing),
                    true
                )
            )
        }

        waitForTransition(binding.contentLayout.rv)

    }

    /**
     * click event listener
     * */
    private fun clickHandler() {
        binding.addProductBtn.setOnClickListener {

            val destination =
                HomeFragmentDirections.actionHomeFragmentToAddProductFragment(
                    isEdit = false
                )
            findNavController().navigate(destination)

        }
    }


    /*
    * data observer
    * */
    private fun subscribeData() {

        productViewModel.listOfProducts.observe(viewLifecycleOwner, Observer {
            productAdapter.submitList(it)

        })
    }

    /**
     * [Recyclerview] item click listener
     * */
    override fun onItemClick(productWithStores: ProductWithStores, imageView: ImageView) {

        val destination =
            HomeFragmentDirections.actionHomeFragmentToProductDetail(
                prodId = productWithStores.product!!._id,
                imageUrl = productWithStores.product!!.productImage
            )
        val extras = FragmentNavigatorExtras(
            /**
             * Returns the name of the View to be used to identify Views in Transitions.
             * */
            imageView.toTransitionGroup(),
            binding.addProductBtn.toTransitionGroup()
        )

        /**
         * @param extras for shared element transition
         * @param destination to open [ProductDetailFragment]
         * */

        findNavController().navigate(destination, extras)

    }


}
