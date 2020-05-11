package `in`.nitin.roomdatabasesample.datasource.roomDb.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation


/**
 * this class used for one-to-many relation between product and store respectively
 * */
class ProductWithStores() : Parcelable {

    @Embedded
    var product: Product? = null

    @Relation(
        parentColumn = "prod_id",
        entityColumn = "prod_store_id",
        entity = Store::class
    )
    var storeList: List<Store> = emptyList()

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductWithStores> {
        override fun createFromParcel(parcel: Parcel): ProductWithStores {
            return ProductWithStores(parcel)
        }

        override fun newArray(size: Int): Array<ProductWithStores?> {
            return arrayOfNulls(size)
        }
    }
}