package `in`.nitin.roomdatabasesample.di

import `in`.nitin.roomdatabasesample.di.qualifier.ApplicationContext
import android.app.Application
import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val mApplication: Application) {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideApplication(): Application {
        return mApplication
    }


    @Provides
    fun provideSavedState(): SavedStateHandle {
        return SavedStateHandle()
    }

}