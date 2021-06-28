package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.PitanjeAPI
import ba.etf.rma21.projekat.objects.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PitanjeKvizRepository {

    companion object{

        private lateinit var context: Context

        fun setContext(_context: Context){
            context=_context
        }

        suspend fun getPitanjaApi(idKviza: Int): List<PitanjeAPI>? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetPitanjaByKvizId(idKviza)
                return@withContext response.body()
            }
        }

        suspend fun getAllForKviz(idKviza:Int):List<Pitanje> {
            return AppDatabase.getInstance(context).PitanjeDao().getAllForKviz(idKviza)
        }

        suspend fun insertPitanje(id: Int, naziv: String, tekstPitanja: String, opcije: String, tacan: Int, KvizId: Int) {
            AppDatabase.getInstance(context).PitanjeDao().insertPitanje(id, naziv, tekstPitanja, opcije, tacan, KvizId)
        }

        suspend fun deleteAll() {
            AppDatabase.getInstance(context).PitanjeDao().deleteAll()
        }

    }

}