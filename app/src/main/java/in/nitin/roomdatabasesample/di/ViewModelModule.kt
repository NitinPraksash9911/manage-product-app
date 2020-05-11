package `in`.nitin.roomdatabasesample.di

import `in`.nitin.roomdatabasesample.di.qualifier.ViewModelKey
import `in`.nitin.roomdatabasesample.viewmodel.AddOrRemoveProductViewModel
import `in`.nitin.roomdatabasesample.viewmodel.ProductViewModel
import `in`.nitin.roomdatabasesample.viewmodel.ViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindProductViewModel(productViewModel: ProductViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AddOrRemoveProductViewModel::class)
    abstract fun bindAddProductViewModel(addOrRemoveProductViewModel: AddOrRemoveProductViewModel): ViewModel

}