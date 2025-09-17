package com.ccc.listadecompracriolla.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ccc.listadecompracriolla.entities.MedidaEntities
import kotlinx.coroutines.flow.Flow

@Dao
interface MedidaDao {
    @Query("SELECT * FROM Medida")
    fun getAllMedida(): Flow<List<MedidaEntities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedida(medida: MedidaEntities): Int // Retorna el ID del cliente insertado

    @Update
    suspend fun updateClient(medida: MedidaEntities)

    @Delete
    suspend fun deleteClient(medida: MedidaEntities)
}