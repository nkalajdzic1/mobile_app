package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.BuildConfig
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Message
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.objects.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PredmetIGrupaRepository {

    companion object {

        private lateinit var context: Context

        fun setContext(_context: Context){
            context=_context
        }

        suspend fun getPredmeti(): List<Predmet>? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetPredmeti()
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        suspend fun getGrupe(): List<Grupa>? {
            return withContext(Dispatchers.IO) {
                val response = ApiConfig.retrofit.GetGrupe()
                return@withContext response.body()
            }
        }

        suspend fun getGrupeZaPredmet(idPredmeta:Int):List<Grupa>? {
            return withContext(Dispatchers.IO) {
                val response = ApiConfig.retrofit.GetGroupsByPredmetId(idPredmeta)
                return@withContext response.body()
            }
        }

        suspend fun upisiUGrupu(idGrupa:Int):Message? {
            return withContext(Dispatchers.IO) {
                val response = ApiConfig.retrofit.AddStudentToGroup(idGrupa)
                return@withContext response.body()
            }
        }

        suspend fun getUpisaneGrupe(): List<Grupa>? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetGrupeByStudent()
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        suspend fun getGroupsByQuiz(id: Int): List<Grupa>? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetGrupeByKviz(id)
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        suspend fun getPredmetById(id: Int): Predmet? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetPredmetById(id)
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        suspend fun deleteAllPredmeti() {
            AppDatabase.getInstance(context).PredmetDao().deleteAll()
        }

        suspend fun insertPredmet(id: Int, naziv: String, godina: Int) {
            AppDatabase.getInstance(context).PredmetDao().insertPredmet(id, naziv, godina)
        }

        suspend fun getAllMyPredmeti(): List<Predmet> {
            return AppDatabase.getInstance(context).PredmetDao().getAll()
        }

        suspend fun deleteAllGrupe() {
            AppDatabase.getInstance(context).GrupaDao().deleteAll()
        }

        suspend fun insertGrupa(id: Int, naziv: String, PredmetId: Int) {
            AppDatabase.getInstance(context).GrupaDao().insertGrupa(id, naziv, PredmetId)
        }

        suspend fun getAllMyGrupe(): List<Grupa> {
            return AppDatabase.getInstance(context).GrupaDao().getAll()
        }

    }

}