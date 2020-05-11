package `in`.nitin.roomdatabasesample.viewmodel

import `in`.nitin.roomdatabasesample.datasource.repository.ProductDataRepository
import androidx.lifecycle.ViewModel
import javax.inject.Inject


class ProductViewModel @Inject constructor(
    productDataRepository: ProductDataRepository
) :
    ViewModel() {

    /**
     * getting list of saved product from local database
     * */
    val listOfProducts = productDataRepository.getProductList

}

