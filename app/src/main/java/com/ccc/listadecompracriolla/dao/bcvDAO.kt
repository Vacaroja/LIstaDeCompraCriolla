package com.ccc.listadecompracriolla.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccc.listadecompracriolla.entities.BcvEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BcvDao {

    // @Insert le dice a Room que inserte la entidad.
    // OnConflictStrategy.REPLACE: Si ya existe una entrada con el mismo ID, la reemplaza.
    // Esto es útil si solo guardas una entrada de BCV.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBcv(bcv: BcvEntity)

    // @Query le dice a Room que ejecute una consulta SQL.
    // SELECT * FROM bcv_data LIMIT 1: Obtiene la única (o la primera) entrada de la tabla.
    // Flow<BcvEntity?>: Retorna un Flow, lo que permite observar los cambios en la BD en tiempo real.
    // Es nullable (BcvEntity?) porque la tabla podría estar vacía.
    @Query("SELECT * FROM bcv_data WHERE id = 1 LIMIT 1")
    fun getBcv(): Flow<BcvEntity?> // Usamos Flow para observar cambios en la DB

    // Opcional: Eliminar todos los datos del BCV
    @Query("DELETE FROM bcv_data WHERE id = 1")
    suspend fun deleteAllBcv()
}