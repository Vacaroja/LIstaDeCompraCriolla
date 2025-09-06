package com.ccc.listadecompracriolla.repository

import com.ccc.listadecompracriolla.dao.BcvDao
import com.ccc.listadecompracriolla.entities.BcvEntity
import com.ccc.listadecompracriolla.entities.toApiDolarServices
import com.ccc.listadecompracriolla.entities.toBcvEntity
import com.ccc.listadecompracriolla.pydolarnetwork.ApiDolarServices
import com.ccc.listadecompracriolla.pydolarnetwork.DolarApi
import kotlinx.coroutines.Dispatchers
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
    suspend fun getBcvData(): ApiDolarServices? {
        return withContext(Dispatchers.IO){
            val bcvEnt = bcvDao.getBcv()
            if (bcvEnt != null) {
                val bcv = bcvEnt.toApiDolarServices()
                return@withContext bcv
            } else {
                return@withContext null
            }
        }

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

    suspend fun fetchAndSaveBcvFromApi(): ApiDolarServices? {
        return withContext(Dispatchers.IO) {
            try {
                val bcvFromApi = DolarApi.retrofitService.getData() // Llama a tu servicio de API
                val bcvEntity =
                    bcvFromApi.toBcvEntity()// Convierte el modelo de API a la entidad de DB
                bcvDao.insertBcv(bcvEntity) // Guarda en la DB
                return@withContext bcvFromApi
            } catch (_: Exception) {
                // Maneja el error, ej. loguea el error o propaga una excepción más específica
                return@withContext null
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