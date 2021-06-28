package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.objects.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TakeKvizRepository {

    companion object {

        private lateinit var context: Context

        fun setContext(_context: Context){
            context=_context
        }

        suspend fun zapocniKviz(idKviza:Int): KvizTaken? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.TakeKviz(idKviza)
                return@withContext response.body()
            }
        }

        suspend fun getPocetiKvizovi(): List<KvizTaken>? {
            return withContext(Dispatchers.IO) {
                var response = ApiConfig.retrofit.GetKvizoviForStudent()
                return@withContext response.body()
            }
        }

    }

}