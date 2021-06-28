package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Predmet

@Dao
interface PredmetDAO {

    @Query("SELECT * FROM PREDMET")
    suspend fun getAll() : List<Predmet>

    @Query("INSERT INTO PREDMET values(:id, :naziv, :godina)")
    suspend fun insertPredmet(id: Int, naziv: String, godina: Int)

    @Query("DELETE FROM PREDMET WHERE 1=1")
    suspend fun deleteAll()

}