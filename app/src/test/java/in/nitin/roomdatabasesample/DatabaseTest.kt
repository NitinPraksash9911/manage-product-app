package `in`.nitin.roomdatabasesample

import `in`.nitin.roomdatabasesample.datasource.roomDb.ProductDao
import `in`.nitin.roomdatabasesample.datasource.roomDb.ProductDatabase
import `in`.nitin.roomdatabasesample.datasource.roomDb.StoreDao
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Product
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Store
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DatabaseTest {
    lateinit var context: Context

    private var db: ProductDatabase? = null
    private var productDao: ProductDao? = null
    private var storeDao: StoreDao? = null

    private var testProduct: Product? = null
    private var testStoreList: ArrayList<Store>? = null

    /*current time as product id*/
    val currentTimeAsProdId = System.currentTimeMillis()

    val myScope = GlobalScope


    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db =
            Room.inMemoryDatabaseBuilder(context, ProductDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        productDao = db?.getProductDao()
        storeDao = db?.getStoreDao()

        createTestProduct()
        createTestStoreList()

    }


    @Test
    @Throws(Exception::class)
    fun shouldInsertProduct() {
        runBlocking {
            myScope.launch {
                productDao?.insert(testProduct!!)
                storeDao?.insert(testStoreList!!)

                /**
                 * here we are getting same inserted product
                 * and then checking contains same product & store list
                 * */

                val returnedProduct = productDao!!.getProduct(currentTimeAsProdId)


                /*product check*/
                Assert.assertEquals(
                    returnedProduct.value!!.product!!.productImage,
                    testProduct!!.productImage

                )

                /*store list check*/
                Assert.assertEquals(
                    returnedProduct.value!!.storeList.size,
                    testStoreList!!.size
                )
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteSingleProduct() {

        runBlocking {
            myScope.launch {
                /*inserting to delete*/
                productDao?.insert(testProduct!!)
                storeDao?.insert(testStoreList!!)

                /*deleting single product*/
                productDao?.deleteProduct(currentTimeAsProdId)

                val getDeletedProduct = productDao!!.getProduct(currentTimeAsProdId)
                /*checking product is deleted*/
                Assert.assertEquals(null, getDeletedProduct.value)
            }
        }

    }

    @Test
    @Throws(Exception::class)
    fun shouldFlushAllProduct() {
        /*deleting all product*/
        productDao?.deleteAllProduct()
        /*checking product count in room database (must be zero)*/
        Assert.assertEquals(0, productDao?.getProductCount())
    }

    @After
    fun endTest() {
        db!!.close()
    }


    /**
     * mock product
     * */
    private fun createTestProduct() {
        val newProduct = Product()
        newProduct.name = "NewProduct"
        newProduct.description = "ProductDescription"
        newProduct.salePrice = "430"
        newProduct.regularPrice = "450"
        newProduct.productColor = arrayListOf<String>("#efefef", "#efe3e", "#3e3e3e")
        newProduct.productImage = "src/file/folderName/image1349193413.jpg"
        newProduct._id = currentTimeAsProdId
        testProduct = newProduct
    }

    /**
     * mock store list
     * */
    private fun createTestStoreList() {
        val storeAddOne = Store()
        storeAddOne.storeAddress = "Juhu Beach, Borivali, Bombay"
        storeAddOne.prodStoreId = currentTimeAsProdId
        val storeAddTwo = Store()
        storeAddTwo.storeAddress = "Khandala, Marina Beach, Bombay"
        storeAddTwo.prodStoreId = currentTimeAsProdId

        testStoreList = arrayListOf<Store>(storeAddOne, storeAddTwo)
    }

}