package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.PitanjeAPI
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PitanjeKvizViewModel {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    /*fun dajPitanja(idKviza: Int, onSuccess: (pitanja: List<Pitanje>) -> Unit,
                   onError: () -> Unit){
        scope.launch{
            val result = PitanjeKvizRepository.getPitanja(idKviza)
            when (result) {
                is List<Pitanje> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }*/

    suspend fun getPitanjaApi(idKviza: Int): List<PitanjeAPI>? {
        return PitanjeKvizRepository.getPitanjaApi(idKviza)
    }

    suspend fun getPitanja(idKviza: Int): List<Pitanje> {
        return PitanjeKvizRepository.getAllForKviz(idKviza)
    }

    suspend fun insertPitanje(id: Int, naziv: String, tekstPitanja: String, opcije: String, tacan: Int, KvizId: Int) {
        PitanjeKvizRepository.insertPitanje(id, naziv, tekstPitanja, opcije, tacan, KvizId)
    }

    suspend fun deleteAll() {
        PitanjeKvizRepository.deleteAll()
    }

}