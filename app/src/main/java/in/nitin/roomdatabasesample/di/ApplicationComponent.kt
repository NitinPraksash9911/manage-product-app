package `in`.nitin.roomdatabasesample.di

import `in`.nitin.roomdatabasesample.application.ProductApplication
import `in`.nitin.roomdatabasesample.di.qualifier.ApplicationContext
import `in`.nitin.roomdatabasesample.ui.fragment.BaseFragment
import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class, ApplicationModule::class, DatabaseModule::class]
)
interface ApplicationComponent {

    fun inject(myapplication: ProductApplication?)

    fun inject(baseFragment: BaseFragment)

    @ApplicationContext
    fun getApplication(): Application;

}