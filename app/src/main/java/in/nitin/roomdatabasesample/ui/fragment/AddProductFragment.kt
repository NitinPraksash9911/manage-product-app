package `in`.nitin.roomdatabasesample.ui.fragment

import `in`.nitin.roomdatabasesample.BuildConfig
import `in`.nitin.roomdatabasesample.R
import `in`.nitin.roomdatabasesample.databinding.AddStoreLayoutBinding
import `in`.nitin.roomdatabasesample.databinding.BottomDailogLayoutBinding
import `in`.nitin.roomdatabasesample.databinding.FragmentAddProductBinding
import `in`.nitin.roomdatabasesample.datasource.helper.Result
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Store
import `in`.nitin.roomdatabasesample.ui.activity.MainActivity
import `in`.nitin.roomdatabasesample.ui.adapter.ProductColorAdapter
import `in`.nitin.roomdatabasesample.ui.adapter.ProductStoreAdapter
import `in`.nitin.roomdatabasesample.ui.utils.ItemDecorationColumns
import `in`.nitin.roomdatabasesample.ui.utils.showToast
import `in`.nitin.roomdatabasesample.ui.utils.snack
import `in`.nitin.roomdatabasesample.viewmodel.AddOrRemoveProductViewModel
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddProductFragment : BaseFragment() {


    private val REQUEST_TAKE_PHOTO = 1
    private val GALLERY_REQUEST = 2
    private var imageUri: Uri? = null

    /**
     * request code for [RuntimePermission]
     * */
    private val REQUEST_CODE_PERMISSIONS = 0X121

    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )

    lateinit var imagePickBottomSheetDialog: BottomSheetDialog
    lateinit var addStoreBinding: AddStoreLayoutBinding
    lateinit var addStoreBottomSheetDialog: BottomSheetDialog
    lateinit var imagePickerSheetBinding: BottomDailogLayoutBinding

    /**
     * to store the image in temporary file
     * */
    var outputDirectory: File? = null

    lateinit var binding: FragmentAddProductBinding
    lateinit var viewModelAddOrRemoveProd: AddOrRemoveProductViewModel
    lateinit var productColorAdapter: ProductColorAdapter
    lateinit var productStoreAdapter: ProductStoreAdapter

    val args: AddProductFragmentArgs by navArgs()

    /**
     * current time in milliseconds used as product ID
     * */
    var currentTimeAsProdId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentAddProductBinding.inflate(layoutInflater)
        (activity as MainActivity?)!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initViews()
        initBottomSheet()
        clickHandler()
        subscribeData()
        /*this is function use when user want to edit a product*/
        editProduct()
        return binding.root
    }


    /**
     * initialisation of views
     * */
    private fun initViews() {
        binding.lifecycleOwner = this
        viewModelAddOrRemoveProd =
            ViewModelProvider(this, providerFactory).get(AddOrRemoveProductViewModel::class.java)
        binding.viewModel = viewModelAddOrRemoveProd
        outputDirectory =
            getOutputDirectory(
                requireContext()
            )

        productColorAdapter = ProductColorAdapter()
        productStoreAdapter = ProductStoreAdapter()

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

        /*getting current time for productId */
        currentTimeAsProdId = System.currentTimeMillis()


    }

    /**
     * when this fragments used as edit product page so all field or data have to prefilled
     * */
    private fun editProduct() {

        /**
         *
         * we are getting current product ID and isEdit [Boolean] in argument
         * */
        if (args.isEdit) {

            currentTimeAsProdId = args.editProductId

            viewModelAddOrRemoveProd.getSingleProduct(args.editProductId)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                        }
                        Result.Status.SUCCESS -> {
                            viewModelAddOrRemoveProd.prodName.value = it!!.data!!.product!!.name
                            viewModelAddOrRemoveProd.prodDesc.value =
                                it.data!!.product!!.description
                            viewModelAddOrRemoveProd.prodSp.value = it.data.product!!.salePrice
                            viewModelAddOrRemoveProd.prodRp.value = it.data.product!!.regularPrice
                            viewModelAddOrRemoveProd.imageUrl.value = it.data.product!!.productImage
                            viewModelAddOrRemoveProd.setColorList(it.data.product!!.productColor as ArrayList<String>)
                            viewModelAddOrRemoveProd.setStoreList(it.data.storeList as ArrayList<Store>)
                        }
                        Result.Status.ERROR -> {
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }

                })
        }
        viewModelAddOrRemoveProd.prodNameError.value = ""

    }

    /**
     * click event listener
     * */
    private fun clickHandler() {
        /*open product Image picker bottom sheet*/
        binding.addProductImage.setOnClickListener {
            if (imagePickBottomSheetDialog.isShowing) {
                imagePickBottomSheetDialog.dismiss()
            } else {
                imagePickBottomSheetDialog.show()
            }
        }

        /*open store address bottom sheet*/
        binding.addStoreBtn.setOnClickListener {
            if (addStoreBottomSheetDialog.isShowing) {
                addStoreBottomSheetDialog.dismiss()
            } else {
                addStoreBottomSheetDialog.show()
            }
        }

        /*pick color*/
        binding.addColorBtn.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(requireContext())                        // Pass Activity Instance
                .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
                .setDefaultColor(Color.YELLOW)    // Pass Default Color
                .setColorListener { _, colorHex ->
                    /*adding product color in list*/
                    viewModelAddOrRemoveProd.addColor(colorHex)
                }
                .show()
        }

        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    /**
     * data observer
     * */
    private fun subscribeData() {
        /**
         * getting color list
         * */
        viewModelAddOrRemoveProd.getColorList()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                productColorAdapter.setColorList(it)

            })

        /**
         * getting store lis
         * */
        viewModelAddOrRemoveProd.getStoreList()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                productStoreAdapter.submitList(it.toMutableList())
            })

        /**
         * status of product added or updated
         * */
        viewModelAddOrRemoveProd.addProduct()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                    }
                    Result.Status.SUCCESS -> {
                        if (args.isEdit) {
                            "Updated Successfully".snack(Color.DKGRAY, binding.root)
                        } else {
                            it.data!!.snack(Color.GREEN, binding.root)
                        }
                        findNavController().popBackStack()
                    }
                    Result.Status.ERROR -> {

                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }


    /**
     * bottom sheets
     * */
    private fun initBottomSheet() {
        /*Image picker bottom sheet*/
        imagePickerSheetBinding = BottomDailogLayoutBinding.inflate(
            LayoutInflater.from(context),
            null, false
        )

        imagePickBottomSheetDialog =
            BottomSheetDialog(requireContext()).apply { setContentView(imagePickerSheetBinding.root) }
        /**
         * get image from [Camera]
         * */
        imagePickerSheetBinding.btnCamera.setOnClickListener {
            /**
             * check for [RuntimePermission]
             * */
            if (allPermissionsGranted()) {
                captureByCamera()
            } else {
                requestForPermission()
            }

        }

        /**
         * get image from [Gallery]
         * */
        imagePickerSheetBinding.fromGallery.setOnClickListener {

            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            if (allPermissionsGranted()) {
                startActivityForResult(gallery, GALLERY_REQUEST)
            } else {
                requestForPermission()

            }


        }

        /********************Add Store Bottom sheet***********************/

        /*add store address bottom sheet*/
        addStoreBinding = AddStoreLayoutBinding.inflate(LayoutInflater.from(context), null, false)

        addStoreBottomSheetDialog =
            BottomSheetDialog(requireContext()).apply { setContentView(addStoreBinding.root) }

        addStoreBinding.addStoreBtn.setOnClickListener {


            if (addStoreBinding.storeNameEt.text.isNullOrEmpty()) {
                addStoreBinding.storeName.error = " address required"
            } else {
                addStoreBinding.storeName.error = ""

                val store = Store()
                store.prodStoreId = currentTimeAsProdId!!
                store.storeAddress = addStoreBinding.storeNameEt.text.toString()
                viewModelAddOrRemoveProd.addStore(store)
                addStoreBinding.storeNameEt.setText("")
                addStoreBottomSheetDialog.dismiss()
            }
        }


    }

    /*
    * capture image using camera
    * */
    private fun captureByCamera() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createFile(
                        outputDirectory!!,
                        FILENAME,
                        PHOTO_EXTENSION
                    )
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    "Problem $ex".snack(Color.RED, binding.root)
                    return
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${BuildConfig.APPLICATION_ID}.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }


    /*
    * request for camera permission
    * */
    private fun requestForPermission() {
        requestPermissions(
            REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                REQUEST_TAKE_PHOTO -> {
                    val imgFile = File(currentPhotoPath)
                    if (imgFile.exists()) {
                        imageUri = Uri.fromFile(imgFile)
                    }
                }
                GALLERY_REQUEST -> {
                    imageUri = data!!.data

                }
            }
            viewModelAddOrRemoveProd.imageUrl.value = imageUri.toString()
        }
        imagePickBottomSheetDialog.dismiss()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                getString(R.string.proceed).showToast(requireContext())
            } else {
                getString(R.string.grant_premission).showToast(requireContext())
                /**
                 * if user denied the [Permissions] then s/he has to accept this manually in application setting
                 * */
                goToSettings()
            }
        }
    }


    /***
     * all required [RuntimePermission]
     * */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    /*
    *  go to application setting to turn ON the camera permission if user denied this required permission
    * */
    private fun goToSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireContext().packageName}")
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            startActivity(intent)
        }
    }

    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"

        lateinit var currentPhotoPath: String

        /** Helper function used to create a timestamped file */
        @Throws(IOException::class)
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            ).apply {
                currentPhotoPath = this.absolutePath
            }

        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.externalMediaDirs.firstOrNull()?.let {
                    File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
                }
            } else {
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }


    }

}
