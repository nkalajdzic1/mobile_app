package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.repositories.TakeKvizRepository
import ba.etf.rma21.projekat.view.KvizListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TakeKvizViewModel {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun dajKvizoveZaStudenta(kviz: Kviz, holder: KvizListAdapter.KvizViewHolder, onSuccess: (kviz: Kviz, holder: KvizListAdapter.KvizViewHolder, kvizovi: List<KvizTaken>) -> Unit,
                       onError: () -> Unit){
        scope.launch{
            val result = TakeKvizRepository.getPocetiKvizovi()
            when (result) {
                is List<KvizTaken> -> onSuccess.invoke(kviz, holder, result)
                else-> onError.invoke()
            }
        }
    }

    suspend fun pomDajKvizoveZaStudenta(): List<KvizTaken>? {
        return TakeKvizRepository.getPocetiKvizovi()
    }

    fun pokusajKviz(id: Int, onSuccess: (kvizTaken: KvizTaken) -> Unit,
                             onError: () -> Unit){
        scope.launch{
            val result = TakeKvizRepository.zapocniKviz(id)
            when (result) {
                is KvizTaken -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    suspend fun pomPokusajKviz(id: Int): KvizTaken? {
        return TakeKvizRepository.zapocniKviz(id)
    }

}