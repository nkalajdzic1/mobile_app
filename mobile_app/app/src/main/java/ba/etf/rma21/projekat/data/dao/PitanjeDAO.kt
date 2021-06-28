package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Pitanje

@Dao
interface PitanjeDAO {

    @Query("SELECT * FROM PITANJE WHERE KvizId=:KvizId")
    suspend fun getAllForKviz(KvizId: Int) : List<Pitanje>

    @Query("INSERT INTO PITANJE values((SELECT COUNT(*) FROM PITANJE) + 1, :id, :naziv, :tekstPitanja, :opcije, :tacan, :KvizId)")
    suspend fun insertPitanje(id: Int, naziv: String, tekstPitanja: String, opcije: String, tacan: Int, KvizId: Int)

    @Query("DELETE FROM PITANJE WHERE 1=1")
    suspend fun deleteAll()

}