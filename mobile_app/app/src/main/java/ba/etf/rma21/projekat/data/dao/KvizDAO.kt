package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Kviz

@Dao
interface KvizDAO {

    /*@Query("SELECT * FROM KVIZ WHERE id=:id")
    suspend fun getById(id: Int) : Kviz?*/

    @Query("SELECT * FROM KVIZ")
    suspend fun getMyKvizes() : List<Kviz>

    @Query("SELECT * FROM KVIZ WHERE predan=1 ")
    suspend fun getDoneKvizes() : List<Kviz>

    @Query("SELECT * FROM KVIZ WHERE date(datumPocetka) > date('now')")
    suspend fun getFutureKvizes() : List<Kviz>

    @Query("DELETE FROM KVIZ WHERE 1=1")
    suspend fun deleteKvizes()

    @Query("INSERT INTO KVIZ values(:id, :naziv, :datumPocetka, :datumKraj, :trajanje, :predan)")
    suspend fun insertKviz(id: Int, naziv: String, datumPocetka: String, datumKraj: String, trajanje: Int, predan: Int)

    @Query("UPDATE KVIZ SET predan=1 WHERE id=:id")
    suspend fun updateKvizPredan(id: Int)

}