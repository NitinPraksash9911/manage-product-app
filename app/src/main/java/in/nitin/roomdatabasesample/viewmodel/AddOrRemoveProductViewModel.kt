package `in`.nitin.roomdatabasesample.viewmodel

import `in`.nitin.roomdatabasesample.datasource.helper.Result
import `in`.nitin.roomdatabasesample.datasource.repository.ProductDataRepository
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Product
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.ProductWithStores
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Store
import android.os.Bundle
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddOrRemoveProductViewModel @Inject constructor(
    private var productDataRepository: ProductDataRepository,
    private var savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    /**
     * this key used by [savedStateHandle]  re/store data of fragment on configuration changes or fragment recreation
     * */
    private val SAVE_PRODUCT_WITH_STORE = "savedProduct"

    /*
    * product fields
    * */
    var prodName = MutableLiveData<String>()
    var prodDesc = MutableLiveData<String>()
    var prodSp = MutableLiveData<String>()
    var prodRp = MutableLiveData<String>()
    var imageUrl = MutableLiveData<String>()
    private val colorList = arrayListOf<String>()
    private var colorListLiveData = MutableLiveData<ArrayList<String>>()
    private val storeAddList = arrayListOf<Store>()
    private var storeAddListLiveData = MutableLiveData<ArrayList<Store>>()
    private var resultLiveData = MutableLiveData<Result<String>>()

    /**
     * product field error
     * */
    var prodNameError = MutableLiveData<String>()
    var prodDescError = MutableLiveData<String>()
    var prodSpError = MutableLiveData<String>()
    var prodRpError = MutableLiveData<String>()


    /**
     * we saved data before fragment recreating and then we get this list from [getSavedProduct] method
     * */
    fun setSaveProduct(productWithStoresinBundle: Bundle) {
        savedStateHandle.set(SAVE_PRODUCT_WITH_STORE, productWithStoresinBundle)
    }

    /**
     *  get stored data in  [savedStateHandle] on fragment restore
     * */
    fun getSavedProduct(): LiveData<Bundle> {
        return savedStateHandle.getLiveData(SAVE_PRODUCT_WITH_STORE)
    }

    /**
     * adding color in color list from [AddProductFragment]
     * */
    fun addColor(color: String) {
        colorList.add(color)
        colorListLiveData.value = colorList
    }

    /**
     * get color list when on [ProductDetailFragment] or [AddProductFragment] as product edit page
     * */
    fun getColorList(): LiveData<ArrayList<String>> {
        return colorListLiveData
    }

    /**
     * set color list from [ProductDetailFragment] or [AddProductFragment] as product edit page
     * */
    fun setColorList(clrList: ArrayList<String>) {
        colorList.addAll(clrList)
        colorListLiveData.value = clrList
    }

    /**
     * adding store in store list from [AddProductFragment]
     * */
    fun addStore(store: Store) {
        storeAddList.add(store)
        storeAddListLiveData.value = storeAddList
    }

    /**
     * get store list from [ProductDetailFragment] or [AddProductFragment] as product edit page
     * */
    fun getStoreList(): LiveData<ArrayList<Store>> {
        return storeAddListLiveData
    }

    /**
     * set store list from [ProductDetailFragment] or [AddProductFragment] as product edit page
     * */
    fun setStoreList(strList: ArrayList<Store>) {
        storeAddList.addAll(strList)
        storeAddListLiveData.value = strList
    }

    /**
     * adding new product in local database from [AddProductFragment] and also observing the field error
     * */
    fun addProduct(): LiveData<Result<String>> {
        if (validation()) {
            val newProduct = Product()
            newProduct.name = prodName.value!!
            newProduct.description = prodDesc.value!!
            newProduct.salePrice = prodSp.value!!
            newProduct.regularPrice = prodRp.value!!
            newProduct.productColor = colorListLiveData.value!!
            newProduct.productImage = imageUrl.value!!
            newProduct._id = storeAddListLiveData.value!![0].prodStoreId
            insertProduct(newProduct, storeAddList)
            resultLiveData.value = Result.success("Product Added Successfully")
        }
        return resultLiveData
    }

    /**
     * this validation used for when creating or editing the product from [AddProductFragment]
     * */
    private fun validation(): Boolean {
        if (prodName.value.isNullOrEmpty()) {
            prodNameError.value = "required"
            return false
        } else if (prodDesc.value.isNullOrEmpty()) {
            prodDescError.value = "required"
            prodNameError.value = ""
            return false
        } else if (prodSp.value.isNullOrEmpty()) {
            prodSpError.value = "required"
            prodDescError.value = ""
            prodNameError.value = ""
            return false
        } else if (prodRp.value.isNullOrEmpty()) {
            prodRpError.value = "required"
            prodSpError.value = ""
            prodDescError.value = ""
            prodNameError.value = ""
            return false
        } else if (imageUrl.value.isNullOrEmpty()) {
            prodRpError.value = ""
            resultLiveData.value = Result.error("Please add product image")
            return false
        } else if (colorListLiveData.value.isNullOrEmpty()) {
            resultLiveData.value = Result.error("Please add at least one product color")
            return false
        } else if (storeAddListLiveData.value.isNullOrEmpty()) {
            resultLiveData.value = Result.error("Please add at least one store address")
            return false
        }
        return true

    }

    /**
     * delete product from [ProductDetailFragment]
     * */
    fun deleteProduct(prodId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            productDataRepository.deleteProduct(prodId)
        }
    }

    /**
     * show single product with full detail on [ProductDetailFragment]
     * */
    fun getSingleProduct(prodId: Long): LiveData<Result<ProductWithStores>> {
        return productDataRepository.getProduct(prodId)
    }

    /**
     * used to store new product from on [addProduct] method defined above
     * */
    private fun insertProduct(product: Product, storeList: List<Store>) {
        viewModelScope.launch(Dispatchers.IO) {
            productDataRepository.insertProduct(product, storeList)
        }
    }

}