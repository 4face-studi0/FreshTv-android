package studio.forface.freshtv.androiddatabase

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import studio.forface.freshtv.localdata.Database

/** A [Module] that handles dependencies of `androiddatabase` module */
val androidDatabase = module {
    factory<SqlDriver> { AndroidSqliteDriver( Database.Schema, androidApplication() ) }
    factory { Database(
        driver = get(),
        movieChannelPojoAdapter = get(),
        tvChannelPojoAdapter = get(),
        tvGuidePojoAdapter = get()
    ) }
}