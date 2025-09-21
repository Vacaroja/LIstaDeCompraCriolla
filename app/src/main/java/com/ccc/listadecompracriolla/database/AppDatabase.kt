package com.ccc.listadecompracriolla.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ccc.listadecompracriolla.database.dao.BcvDao
import com.ccc.listadecompracriolla.database.dao.ClientDao
import com.ccc.listadecompracriolla.database.entities.BcvEntity
import com.ccc.listadecompracriolla.database.entities.ClientListEntity
import com.ccc.listadecompracriolla.database.entities.ClientProductCrossRef
import com.ccc.listadecompracriolla.database.entities.ProductEntity

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