package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Message
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.PredmetIGrupaRepository
import ba.etf.rma21.projekat.view.KvizListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PredmetIGrupaViewModel {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    suspend fun dajPredmete(): List<Predmet>? {
        return PredmetIGrupaRepository.getPredmeti()
    }

    fun pomNazivDajPredmete(naziv: String, onSuccess: (naziv: String, predmeti: List<Predmet>) -> Unit,
                    onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.getPredmeti()
            when (result) {
                is List<Predmet> -> onSuccess.invoke(naziv, result)
                else-> onError.invoke()
            }
        }
    }

    fun pomDajPredmete(godina: Int, onSuccess: (godina: Int, predmeti: List<Predmet>) -> Unit,
                       onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.getPredmeti()
            when (result) {
                is List<Predmet> -> onSuccess.invoke(godina, result)
                else-> onError.invoke()
            }
        }
    }

    fun dajGrupe(onSuccess: (grupe: List<Grupa>) -> Unit,
                 onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.getGrupe()
            when (result) {
                is List<Grupa> -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    suspend fun pomDajGrupe(): List<Grupa>? {
        return PredmetIGrupaRepository.getGrupe()
    }

    fun dajGrupeZaPredmet(idPredmeta:Int, onSuccess: (grupe: List<Grupa>) -> Unit,
                 onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.getGrupeZaPredmet(idPredmeta)
            when (result) {
                is List<Grupa> -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    suspend fun pomDajGrupeZaPredmet(id: Int): List<Grupa>? {
        return PredmetIGrupaRepository.getGrupeZaPredmet(id)
    }

    fun upisiStudentaUGrupu(idGrupa:Int, onSuccess: (message: Message) -> Unit,
                          onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.upisiUGrupu(idGrupa)
            when (result) {
                is Message -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    suspend fun upisiStudentaUGrupu(idGrupa:Int): Message? {
        return PredmetIGrupaRepository.upisiUGrupu(idGrupa)
    }

    fun dajGrupeZaKviz(id: Int, holder: KvizListAdapter.KvizViewHolder, onSuccess: (holder: KvizListAdapter.KvizViewHolder, kvizovi: List<Grupa>) -> Unit,
               onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.getGroupsByQuiz(id)
            when (result) {
                is List<Grupa> -> onSuccess.invoke(holder, result)
                else-> onError.invoke()
            }
        }
    }

    fun dajPredmetPoId(id: Int, holder: KvizListAdapter.KvizViewHolder, onSuccess: (holder: KvizListAdapter.KvizViewHolder, predmet: Predmet) -> Unit,
                       onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.getPredmetById(id)
            when (result) {
                is Predmet -> onSuccess.invoke(holder, result)
                else-> onError.invoke()
            }
        }
    }

    fun dajGrupeZaStudenta(holder: KvizListAdapter.KvizViewHolder, onSuccess: (holder: KvizListAdapter.KvizViewHolder, grupe: List<Grupa>) -> Unit,
                       onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.getUpisaneGrupe()
            when (result) {
                is List<Grupa> -> onSuccess.invoke(holder, result)
                else-> onError.invoke()
            }
        }
    }

    suspend fun pomDajGrupeZaStudenta(): List<Grupa>? {
        return PredmetIGrupaRepository.getUpisaneGrupe()
    }

    suspend fun deleteAllPredmeti() {
        return PredmetIGrupaRepository.deleteAllPredmeti()
    }

    suspend fun insertPredmet(id: Int, naziv: String, godina: Int) {
        PredmetIGrupaRepository.insertPredmet(id, naziv, godina)
    }

    suspend fun getAllMyPredmeti(): List<Predmet> {
        return PredmetIGrupaRepository.getAllMyPredmeti()
    }

    suspend fun deleteAllGrupe() {
        PredmetIGrupaRepository.deleteAllGrupe()
    }

    suspend fun insertGrupa(id: Int, naziv: String, PredmetId: Int) {
        PredmetIGrupaRepository.insertGrupa(id, naziv, PredmetId)
    }

    suspend fun getAllMyGrupe(): List<Grupa> {
        return PredmetIGrupaRepository.getAllMyGrupe()
    }


}