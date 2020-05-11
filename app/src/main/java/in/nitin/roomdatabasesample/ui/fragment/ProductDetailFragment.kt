package `in`.nitin.roomdatabasesample.ui.fragment

import `in`.nitin.roomdatabasesample.R
import `in`.nitin.roomdatabasesample.databinding.EditProductLayoutBinding
import `in`.nitin.roomdatabasesample.databinding.FragmentProductDetailBinding
import `in`.nitin.roomdatabasesample.datasource.helper.Result
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.ProductWithStores
import `in`.nitin.roomdatabasesample.ui.adapter.ProductColorAdapter
import `in`.nitin.roomdatabasesample.ui.adapter.ProductStoreAdapter
import `in`.nitin.roomdatabasesample.ui.utils.ItemDecorationColumns
import `in`.nitin.roomdatabasesample.ui.utils.snack
import `in`.nitin.roomdatabasesample.ui.utils.toTransitionGroup
import `in`.nitin.roomdatabasesample.viewmodel.AddOrRemoveProductViewModel
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * A simple [Fragment] subclass.
 */
class ProductDetailFragment : BaseFragment() {

    /**
     * this key is used to re/store the data from bundle on configuration change or when fragment restored
     * */
    private val RESTORE_KEY = "ProductDetailData"

    lateinit var binding: FragmentProductDetailBinding
    lateinit var addOrRemoveProductViewModel: AddOrRemoveProductViewModel
    lateinit var productColorAdapter: ProductColorAdapter
    lateinit var productStoreAdapter: ProductStoreAdapter


    lateinit var editProdBinding: EditProductLayoutBinding
    lateinit var editProdBottomSheetDialog: BottomSheetDialog

    private val args: ProductDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        binding = FragmentProductDetailBinding.inflate(layoutInflater)

        /**
         *
         * we are getting current product ID  in argument
         * we also getting image url in argument for shared element transition
         * */
        binding.imageUrl = args.imageUrl
        initViews()
        subscribeData(args.prodId)
        clickHandler()
        initBottomSheet()
        return binding.root
    }


    private fun initViews() {

        productColorAdapter = ProductColorAdapter()
        productStoreAdapter = ProductStoreAdapter()

        binding.lifecycleOwner = this

        addOrRemoveProductViewModel =
            ViewModelProvider(this, providerFactory).get(AddOrRemoveProductViewModel::class.java)


        binding.apply {

            colorRV.apply {
                adapter = productColorAdapter
            }
            storeRv.apply {
                adapter = productStoreAdapter
                ItemDecorationColumns(
                    resources.getInteger(R.integer.list_store_columns),
                    resources.getDimensionPixelSize(R.dimen.list_spacing),
                    true
                )
            }
        }
    }

    /**
     * click event listener
     * */
    private fun clickHandler() {
        /**
         * to see image in full size with pinch zoom
         * */
        binding.productImage.setOnClickListener {

            val destination =
                ProductDetailFragmentDirections.actionProductDetailToImagePreviewFragment(
                    args.imageUrl
                )

            val extras = FragmentNavigatorExtras(
                /**
                 * Returns the name of the View to be used to identify Views in Transitions.
                 * */
                binding.productImage.toTransitionGroup()
            )

            /**
             * @param extras for shared element transition
             * @param destination to open [ImagePreviewFragment]
             * */
            findNavController().navigate(destination, extras)

        }

        binding.editBtn.setOnClickListener {
            if (editProdBottomSheetDialog.isShowing) {
                editProdBottomSheetDialog.dismiss()
            } else {
                editProdBottomSheetDialog.show()
            }
        }


    }

    /**
     * data observer
     * */
    private fun subscribeData(prodId: Long) {
        /**
         * observing product when fragment restored or configuration changes
         * */
        addOrRemoveProductViewModel.getSavedProduct().observe(viewLifecycleOwner, Observer {
            val data = it.getParcelable<ProductWithStores>(RESTORE_KEY)
            setDataToUI(productWithStores = data!!)
        })

        addOrRemoveProductViewModel.getSingleProduct(prodId).observe(viewLifecycleOwner, Observer {
            if (it.data != null) {

                when (it.status) {
                    Result.Status.LOADING -> {
                    }
                    Result.Status.SUCCESS -> {

                        setDataToUI(it.data)

                        /**
                         * Saving product in saveStateHandle
                         * */
                        val bundle = Bundle()
                        bundle.putParcelable(RESTORE_KEY, it.data)
                        addOrRemoveProductViewModel.setSaveProduct(bundle)
                    }
                    Result.Status.ERROR -> {

                        it.message!!.snack(Color.RED, binding.root)
                    }
                }

            }
        })

    }

    /**
     * set data to UI
     * */
    private fun setDataToUI(productWithStores: ProductWithStores) {
        binding.product = productWithStores.product
        binding.imageUrl = productWithStores.product!!.productImage
        productColorAdapter.setColorList(productWithStores.product!!.productColor as ArrayList<String>)
        productStoreAdapter.submitList(productWithStores.storeList)
    }

    /**
     * bottom sheets
     * */
    private fun initBottomSheet() {

        /*Edit Product bottom sheet*/
        editProdBinding = EditProductLayoutBinding.inflate(
            LayoutInflater.from(context),
            null, false
        )

        editProdBottomSheetDialog =
            BottomSheetDialog(requireContext()).apply { setContentView(editProdBinding.root) }

        /*to edit the product detail*/
        editProdBinding.btnEdit.setOnClickListener {
            editProdBottomSheetDialog.dismiss()

            val destination =
                ProductDetailFragmentDirections.actionProductDetailToAddProductFragment(
                    isEdit = true,
                    editProductId = args.prodId
                )
            findNavController().navigate(destination)
        }

        /*to delete the current visible product*/
        editProdBinding.btnDelete.setOnClickListener {

            addOrRemoveProductViewModel.deleteProduct(args.prodId)
            editProdBottomSheetDialog.dismiss()

            findNavController().popBackStack()
        }
    }


}
