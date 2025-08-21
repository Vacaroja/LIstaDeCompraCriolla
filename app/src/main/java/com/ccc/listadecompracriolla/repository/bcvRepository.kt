package com.ccc.listadecompracriolla.repository

import com.ccc.listadecompracriolla.dao.BcvDao
import com.ccc.listadecompracriolla.entities.BcvEntity
import com.ccc.listadecompracriolla.entities.toBcvEntity
import com.ccc.listadecompracriolla.pydolarnetwork.DolarApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Indica a Hilt que debe proveer una única instancia de este repositorio
class BcvRepository @Inject constructor(
    private val bcvDao: BcvDao, // Hilt inyectará BcvDao aquí
    // private val bcvApiService: BcvApiService // Si tienes un servicio de API, Hilt lo inyectaría aquí
) {

    /**
     * Obtiene la única entrada de BcvEntity desde la base de datos como un Flow.
     * Esto te permite observar los cambios en tiempo real.
     */
    fun getBcvData(): Flow<BcvEntity?> {
        return bcvDao.getBcv()
    }

    /**
     * Inserta o actualiza la BcvEntity en la base de datos.
     * Utiliza OnConflictStrategy.REPLACE en el DAO para reemplazar la entrada existente con ID=1.
     */
    suspend fun saveBcvData(bcvEntity: BcvEntity) {
        withContext(Dispatchers.IO) {
            bcvDao.insertBcv(bcvEntity)
        }
    }

    /**
     * Opcional: Función para obtener la tasa BCV desde una API y guardarla en la base de datos.
     * Esta función dependerá de tu configuración de red.
     */

    suspend fun fetchAndSaveBcvFromApi() {
        withContext(Dispatchers.IO) {
            try {
                val bcvFromApi = DolarApi.retrofitService.getData() // Llama a tu servicio de API
                val bcvEntity = bcvFromApi.toBcvEntity()// Convierte el modelo de API a la entidad de DB
                bcvDao.insertBcv(bcvEntity) // Guarda en la DB
            } catch (e: Exception) {
                // Maneja el error, ej. loguea el error o propaga una excepción más específica
                throw RuntimeException("Error al obtener y guardar la tasa BCV desde la API", e)
            }
        }
    }


    /**
     * Elimina la entrada de BCV de la base de datos.
     */
    suspend fun deleteBcvData() {
        withContext(Dispatchers.IO) {
            bcvDao.deleteAllBcv()
        }
    }
}