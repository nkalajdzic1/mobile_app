package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Grupa

@Dao
interface GrupaDAO {

    @Query("SELECT * FROM GRUPA")
    suspend fun getAll() : List<Grupa>

    @Query("INSERT INTO GRUPA values(:id, :naziv, :PredmetId)")
    suspend fun insertGrupa(id: Int, naziv: String, PredmetId: Int)

    @Query("DELETE FROM GRUPA WHERE 1=1")
    suspend fun deleteAll()

}