package `in`.nitin.roomdatabasesample.datasource.roomDb

import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Product
import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Store
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val DATABASE_NAME = "product_db"

@Database(entities = [Product::class, Store::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao  // to access productDao
    abstract fun getStoreDao(): StoreDao // to access storeDao
}