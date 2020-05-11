package `in`.nitin.roomdatabasesample.datasource.roomDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
class Product {
    @PrimaryKey
    @ColumnInfo(name = "prod_id")
    var _id: Long = 0L

    @ColumnInfo(name = "desc")
    var description: String = ""

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "regular_price")
    var regularPrice: String = ""

    @ColumnInfo(name = "sale_price")
    var salePrice: String = ""

    @ColumnInfo(name = "prod_img")
    var productImage: String = ""

    @ColumnInfo(name = "prod_clr")
    var productColor: List<String> = emptyList()


}