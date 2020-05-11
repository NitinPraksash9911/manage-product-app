package `in`.nitin.roomdatabasesample.datasource.roomDb

import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Product
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.ProductWithStores
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {

    @Transaction
    @Query("SELECT * FROM product_table")
    fun getAllProducts(): LiveData<List<ProductWithStores>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Transaction
    @Query("SELECT * FROM product_table WHERE prod_id = :id")
    fun getProduct(id: Long): LiveData<ProductWithStores>

    @Query("DELETE FROM product_table WHERE prod_id = :id")
    suspend fun deleteProduct(id: Long)

    @Query("DELETE  FROM  product_table")
    fun deleteAllProduct()


    @Query("SELECT count(*) FROM product_table")
    fun getProductCount(): Int

}