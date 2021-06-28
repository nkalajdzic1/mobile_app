package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Odgovor

@Dao
interface OdgovorDAO {

    @Query("SELECT * FROM ODGOVOR WHERE KvizTakenId=:KvizTakenId")
    suspend fun getAllForKvizTaken(KvizTakenId: Int) : List<Odgovor>

    @Query("INSERT INTO ODGOVOR values((SELECT COUNT(*) FROM ODGOVOR) + 1, :odgovoreno, :KvizTakenId, :PitanjeId)")
    suspend fun postaviOdgovor(odgovoreno: Int, KvizTakenId: Int, PitanjeId: Int)

    @Query("DELETE FROM ODGOVOR WHERE 1=1")
    suspend fun deleteAll()

}