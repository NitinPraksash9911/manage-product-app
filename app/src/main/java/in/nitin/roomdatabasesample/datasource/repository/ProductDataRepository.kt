package `in`.nitin.roomdatabasesample.datasource.repository

import `in`.nitin.roomdatabasesample.datasource.helper.Result
import `in`.nitin.roomdatabasesample.datasource.helper.resultLiveData
import `in`.nitin.roomdatabasesample.datasource.roomDb.ProductDao
import `in`.nitin.roomdatabasesample.datasource.roomDb.StoreDao
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Product
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.ProductWithStores
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Store
import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [NOTE]
 * [Repository] implementation saves api service responses into the database.
 * Changes to the database then trigger callbacks on active LiveData objects. Using this model,
 * the database serves as the [SingleSourceOfTruth], and
 * other parts of the app access it using our Repository.
 * Regardless of whether you use a disk cache,
 * we recommend that your repository designate a data source as the single source of truth for the rest of your app.
 *
 * */

@Singleton
class ProductDataRepository @Inject constructor(
    private var productDao: ProductDao,
    private var storeDao: StoreDao

) {

    /**
     * getting product list from local database
     * */
    val getProductList = productDao.getAllProducts()


    /**
     * inserting or updating a product and its stores
     * */
    suspend fun insertProduct(product: Product, storeList: List<Store>) {
        productDao.insert(product)
        storeDao.insert(storeList)
    }

    /*
    * deleting a product
    * */
    suspend fun deleteProduct(productId: Long) {
        productDao.deleteProduct(productId)
    }


    /**
     *  I used [resultLiveData] just for show case otherwise we can use this as like above methods used
     * */
    fun getProduct(productId: Long): LiveData<Result<ProductWithStores>> {
        return resultLiveData(
            databaseQuery = { productDao.getProduct(productId) }
        )
    }


}