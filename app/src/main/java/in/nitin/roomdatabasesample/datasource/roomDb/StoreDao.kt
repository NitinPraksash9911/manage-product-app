package `in`.nitin.roomdatabasesample.datasource.roomDb

import `in`.nitin.roomdatabasesample.datasource.roomDb.entity.Store
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storeList: List<Store>)

}