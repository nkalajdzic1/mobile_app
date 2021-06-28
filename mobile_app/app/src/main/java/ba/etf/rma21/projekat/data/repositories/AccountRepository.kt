package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Account
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountRepository {

    companion object {
        private val scope = CoroutineScope(Job() + Dispatchers.Main)
        var acHash: String = "086e581c-e3eb-4ae5-ac3f-31f32a843663";
        private lateinit var context: Context

        fun setContext(_context:Context){
            context=_context
        }

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun postaviHash(hash:String):Boolean {

            AppDatabase.getInstance(context).PredmetDao().deleteAll()
            AppDatabase.getInstance(context).GrupaDao().deleteAll()
            AppDatabase.getInstance(context).KvizDao().deleteKvizes()
            AppDatabase.getInstance(context).PitanjeDao().deleteAll()
            AppDatabase.getInstance(context).OdgovorDao().deleteAll()
            AppDatabase.getInstance(context).AccountDao().deleteAccountRow()

            acHash = hash

            val currentDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            AppDatabase.getInstance(context).AccountDao().insertAccountRow(hash, currentDate)

            return true
        }

        fun getHash():String { return acHash; }

        suspend fun getAccountRowCount(): Int {
            return AppDatabase.getInstance(context).AccountDao().getAccountRowCount()
        }

        suspend fun getAccountRow(): Account? {
            return AppDatabase.getInstance(context).AccountDao().getAccountRow()
        }

        suspend fun insertAccountRow(hash: String, date: String) {
            AppDatabase.getInstance(context).AccountDao().insertAccountRow(hash, date)
        }

        suspend fun deleteAccountRow() {
            AppDatabase.getInstance(context).AccountDao().deleteAccountRow()
        }

        suspend fun updateLastUpdate(date: String) {
            AppDatabase.getInstance(context).AccountDao().updateLastUpdate(date)
        }

    }

}