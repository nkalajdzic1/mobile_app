package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.dao.KvizDAO
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.objects.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class KvizRepository {

    companion object {

        private lateinit var context: Context

        fun setContext(_context: Context){
            context=_context
        }

        init {}

        suspend fun getAll(): List<Kviz>? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetKvizovi()
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        suspend fun getById(id:Int):Kviz? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetKvizById(id)
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        suspend fun getUpisani(): List<Kviz> {
            return withContext(Dispatchers.IO) {
                val kvizovi = AppDatabase.getInstance(context).KvizDao().getMyKvizes()
                return@withContext kvizovi
            }
        }

        suspend fun getKvizoviByGroup(id: Int): List<Kviz>? {
            return withContext(Dispatchers.IO) {
                val response = ApiConfig.retrofit.GetKvizoviByGroup(id)
                return@withContext response.body()
            }
        }

        suspend fun getDone(): List<Kviz> {
            return withContext(Dispatchers.IO) {
                val kvizovi: List<Kviz> = AppDatabase.getInstance(context).KvizDao().getDoneKvizes().filter { k -> LocalDate.parse(k.datumPocetka) < LocalDate.now() }
                return@withContext kvizovi
            }
        }

        suspend fun getFuture(): List<Kviz> {
            val kvizovi: List<Kviz> = AppDatabase.getInstance(context).KvizDao().getFutureKvizes()
            return kvizovi
        }

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun getNotTaken(): List<Kviz> {
            val uradjeniKvizovi: List<Kviz> = getDone()
            val kvizovi: List<Kviz> = getMyKvizes().filter { k -> uradjeniKvizovi.find { uk -> uk.naziv == k.naziv } == null }
            return kvizovi
        }

        suspend fun getMyKvizesApi(): List<Kviz> {
            return withContext(Dispatchers.IO) {
                val grupe = PredmetIGrupaRepository.getUpisaneGrupe()
                var kvizovi: MutableList<Kviz> = mutableListOf()

                if (grupe != null) {
                    for(grupa in grupe) {
                        getKvizoviByGroup(grupa.id)?.let { kvizovi.addAll(it) }
                    }
                }

                return@withContext kvizovi.distinctBy { it.id }
            }
        }

        suspend fun deleteMyKvizes() {
            AppDatabase.getInstance(context).KvizDao().deleteKvizes()
        }

        suspend fun insertKviz(id: Int, naziv: String, datumPocetka: String, datumKraj: String, trajanje: Int, predan: Int) {
            AppDatabase.getInstance(context).KvizDao().insertKviz(id, naziv, datumPocetka, datumKraj, trajanje, predan)
        }

        suspend fun getMyKvizes(): List<Kviz> {
            return AppDatabase.getInstance(context).KvizDao().getMyKvizes()
        }

        suspend fun updateKvizPredan(id: Int) {
            AppDatabase.getInstance(context).KvizDao().updateKvizPredan(id)
        }

    }

}