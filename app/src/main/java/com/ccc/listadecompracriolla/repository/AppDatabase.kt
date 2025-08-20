package com.ccc.listadecompracriolla.repository
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ccc.listadecompracriolla.dao.BcvDao
import com.ccc.listadecompracriolla.dao.ClientDao
import com.ccc.listadecompracriolla.entities.BcvEntity
import com.ccc.listadecompracriolla.entities.ClientListEntity
import com.ccc.listadecompracriolla.entities.ClientProductCrossRef
import com.ccc.listadecompracriolla.entities.ProductEntity

@Database(
    entities = [
        BcvEntity::class,
        ClientListEntity::class,
        ProductEntity::class,
        ClientProductCrossRef::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bcvDao(): BcvDao
    abstract fun clientDao(): ClientDao

}