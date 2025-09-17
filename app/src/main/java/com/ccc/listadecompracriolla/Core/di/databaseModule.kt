package com.ccc.listadecompracriolla.Core.di

import android.content.Context
import androidx.room.Room
import com.ccc.listadecompracriolla.dao.BcvDao
import com.ccc.listadecompracriolla.dao.ClientDao
import com.ccc.listadecompracriolla.dao.MedidaDao
import com.ccc.listadecompracriolla.repository.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Instancia única de la base de datos para toda la aplicación
object DatabaseModule { // Usar 'object' es común si todos los métodos son @Provides estáticos

    @Singleton // Garantiza que solo habrá una única instancia de AppDatabase
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, // Hilt inyecta el Context de la aplicación aquí
            AppDatabase::class.java,
            "dolar_database" // El nombre de tu archivo de base de datos
        )
            // ¡CUIDADO! Usar esto en producción borraría los datos. Para producción, usa migraciones.
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideBcvDao(appDatabase: AppDatabase): BcvDao {
        // Hilt automáticamente inyectará la instancia de AppDatabase creada arriba
        return appDatabase.bcvDao()
    }

    @Provides
    fun provideClientDao(appDatabase: AppDatabase): ClientDao {
        // Hilt automáticamente inyectará la instancia de AppDatabase creada arriba
        return appDatabase.clientDao()
    }

    @Provides
    fun provideMedidaDao(appDatabase: AppDatabase): MedidaDao {
        // Hilt automáticamente inyectará la instancia de AppDatabase creada arriba
        return appDatabase.medidaDao()
    }
}