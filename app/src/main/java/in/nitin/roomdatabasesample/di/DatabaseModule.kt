package `in`.nitin.roomdatabasesample.di


import `in`.nitin.roomdatabasesample.datasource.roomDb.DATABASE_NAME
import `in`.nitin.roomdatabasesample.datasource.roomDb.ProductDao
import `in`.nitin.roomdatabasesample.datasource.roomDb.ProductDatabase
import `in`.nitin.roomdatabasesample.datasource.roomDb.StoreDao
import `in`.nitin.roomdatabasesample.di.qualifier.DatabaseInfo
import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule(private val mContext: Application) {

    @DatabaseInfo
    private val mDBName =
        DATABASE_NAME

    @Singleton
    @Provides
    fun provideDatabase(): ProductDatabase {
        return Room.databaseBuilder(
            mContext,
            ProductDatabase::class.java,
            mDBName
        ).fallbackToDestructiveMigration().build()
    }


    @Singleton
    @Provides
    fun provideProductDao(db: ProductDatabase): ProductDao {
        return db.getProductDao()
    }

    @Singleton
    @Provides
    fun provideStoreDao(db: ProductDatabase): StoreDao {
        return db.getStoreDao()
    }


    /**
     * [fallbackToDestructiveMigration] :: when we increase the version number of database we have to tell room
     * how to migrate to the new schema  and if don't do this and increase version number our app will actually crash
     *  and we get illegalStateException.
     * By using [fallbackToDestructiveMigration] we can avoid the above exception because it will delete the database and
     *  all its' tables and create the fresh database from the scratch
     * */

}
