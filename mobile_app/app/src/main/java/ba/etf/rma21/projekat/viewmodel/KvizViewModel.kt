package ba.etf.rma21.projekat.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class KvizViewModel {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun dajSve(onSuccess: (kvizovi: List<Kviz>) -> Unit,
                             onError: () -> Unit){
        scope.launch{
            val result = KvizRepository.getAll()
            when (result) {
                is List<Kviz> -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    suspend fun pomDajSve(): List<Kviz>? {
        return KvizRepository.getAll()
    }

    fun dajMojeKvizove(onSuccess: (kvizovi: List<Kviz>) -> Unit){
        scope.launch{
            onSuccess.invoke(KvizRepository.getMyKvizes())
        }
    }

    suspend fun pomDajMojeKvizove(): List<Kviz> {
        return KvizRepository.getMyKvizes()
    }

    fun dajZavrsene(onSuccess: (kvizovi: List<Kviz>) -> Unit){
        scope.launch{
            onSuccess.invoke(KvizRepository.getDone())
        }
    }


    fun dajBuduce(onSuccess: (kvizovi: List<Kviz>) -> Unit){
        scope.launch{
            onSuccess.invoke(KvizRepository.getFuture())
        }
    }

    fun dajNeuradjene(onSuccess: (kvizovi: List<Kviz>) -> Unit){
        scope.launch{
            onSuccess.invoke(KvizRepository.getNotTaken())
        }
    }

    suspend fun getMyKvizesApi() : List<Kviz> {
        return KvizRepository.getMyKvizesApi()
    }

    suspend fun getMyKvizes(): List<Kviz> {
        return KvizRepository.getMyKvizes()
    }

    suspend fun deleteMyKvizes() {
        return KvizRepository.deleteMyKvizes()
    }

    suspend fun insertKviz(id: Int, naziv: String, datumPocetka: String, datumKraj: String, trajanje: Int, predan: Int) {
        KvizRepository.insertKviz(id, naziv, datumPocetka, datumKraj, trajanje, predan)
    }

    suspend fun updateKvizPredan(id: Int) {
        KvizRepository.updateKvizPredan(id)
    }

}