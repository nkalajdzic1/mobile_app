package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Account

@Dao
interface AccountDAO {

    @Query("SELECT COUNT(*) FROM Account")
    suspend fun getAccountRowCount() : Int

    @Query("SELECT * FROM Account LIMIT 1")
    suspend fun getAccountRow(): Account?

    @Query("INSERT INTO Account values(1, :accountHash, :lastUpdate)")
    suspend fun insertAccountRow(accountHash: String, lastUpdate: String)

    @Query("DELETE FROM Account WHERE id=(SELECT id FROM Account LIMIT 1)")
    suspend fun deleteAccountRow()

    @Query("UPDATE Account SET lastUpdate=:date WHERE id=(SELECT id FROM Account LIMIT 1)")
    suspend fun updateLastUpdate(date: String)

}