package `in`.nitin.roomdatabasesample.application

import `in`.nitin.roomdatabasesample.di.ApplicationComponent
import `in`.nitin.roomdatabasesample.di.ApplicationModule
import `in`.nitin.roomdatabasesample.di.DaggerApplicationComponent
import `in`.nitin.roomdatabasesample.di.DatabaseModule
import android.app.Application

class ProductApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()

        if (instance == null) {
            instance = this
        }

        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .databaseModule(DatabaseModule(this))
            .build()

        applicationComponent.inject(this)
    }

    companion object {
        private var instance: ProductApplication? = null

        fun getComponent(): ApplicationComponent? {
            return instance!!.applicationComponent
        }
    }
}