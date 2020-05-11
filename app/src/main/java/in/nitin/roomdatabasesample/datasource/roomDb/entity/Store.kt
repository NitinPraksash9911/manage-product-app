package `in`.nitin.roomdatabasesample.datasource.roomDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


/**
 *
 * [ForeignKey] :- it used for when any product delete or update then all related stores to that product
 * also deleted or updated respectively
 * */

@Entity(
    tableName = "store_table",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("prod_id"),
        childColumns = arrayOf("prod_store_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)

class Store {
    @ColumnInfo(name = "store_id")
    @PrimaryKey(autoGenerate = true)
    var storeId: Long = 0L

    @ColumnInfo(name = "prod_store_id", index = true)
    var prodStoreId: Long = 0L

    @ColumnInfo(name = "store_add")
    var storeAddress: String = ""
}